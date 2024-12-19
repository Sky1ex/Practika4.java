package Classes;

import JsonParser.*;
import SubClassesBookMix.CommentsBook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class BookMixReader<T>
{
    private static final String PATH = "src/main/java/Data/DatasetBook.json";

    static ExecutorService executorService;

    public BookMixReader() {
        executorService = Executors.newWorkStealingPool();
    }

    public static ArrayList<CommentsBook> GetCommentList(int pageNumber)
    {
        ArrayList<CommentsBook> Data = new ArrayList<CommentsBook>();
        Document doc, comment;
        try {
            String html = "https://bookmix.ru/comments/index.phtml?begin=" + pageNumber * 20 + "&num_point=20&num_points=20";
            System.out.println("Считывание страницы под номером " + pageNumber + "...");

            doc = Jsoup.connect(html).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36").get();

            Elements elements = doc.select("body > div.wrapper > div > div > div.col-lg-8 > div > div > div").get(1).getElementsByClass("universal-blocks");

            for (int i = 0; i < elements.size(); i++) {
                String link = elements.get(i).select("div[class^=universal-blocks-title] > h5 > a").attr("href").toString();

                comment = Jsoup.connect("https://bookmix.ru" + link).get();

                link = link.substring(link.indexOf("#") + 1);

                Elements comms = comment.select("div[id^=" + link + "]");
                Elements _book = comment.select("div[class^=review]");
                Data.add(new CommentsBook(_book, comms.first()));
            }

            int imgCount = Data.size();
            for (int i = 0; i < imgCount; i++)
            {
                if (!Data.get(i).getPicAddress().startsWith("http")) continue;
                Connection.Response resultImageResponse = Jsoup.connect(Data.get(i).getPicAddress())
                        .ignoreContentType(true).execute();
                String temp = Data.get(i).getName();
                temp = temp.replaceAll("[/|\\\\|:|*|?|\"|<|>]", "");
                FileOutputStream out = (new FileOutputStream(new java.io.File("src/main/java/Data/PicsBook/" + temp + ".jpg")));
                out.write(resultImageResponse.bodyAsBytes());
                out.close();
            }

        } catch (IOException e) {
            System.out.println("Ссылки/книги не существует!");
            throw new RuntimeException(e);
        }
        System.out.flush();
        System.out.println("Страница считана!");
        return Data;
    }

    public static void GetAllCommentPagesAndWriteToJson() throws IOException, InterruptedException, ExecutionException, NoSuchFieldException, IllegalAccessException {
        Document page = Jsoup.connect("https://bookmix.ru/comments/").get();
        int max = Integer.parseInt(page.select("div.col-12 > div > ul > li:nth-child(9) > a").getFirst().text());
        CountDownLatch latch = new CountDownLatch(100 + 1);

        ArrayList<CommentsBook> MainData = new ArrayList<CommentsBook>();
        try (FileWriter writer = new FileWriter(PATH, false)) {
            int count = 0;
            for (int i = 0; i <= 100; i++)
            {
                int pageNumber = i;
                executorService.submit(() ->
                {
                    try {
                        ArrayList<CommentsBook> Data = GetCommentList(pageNumber);
                        MainData.addAll(Data);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await();
                JsonParser<CommentsBook> parser = new JsonParser<CommentsBook>();
                try {
                    System.out.println("\nЗапись данных в json файл...");
                    parser.WriteJson(MainData);
                    writer.write(parser.getText());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    executorService.shutdown(); // Завершаем работу ExecutorService
                }

                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
