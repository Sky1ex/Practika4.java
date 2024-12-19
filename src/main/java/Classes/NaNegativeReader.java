package Classes;

import JsonParser.JsonParser;
import SubClassesShops.Shop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class NaNegativeReader<T>
{
    private static final String PATH = "src/main/java/Data/DatasetNaNegative.json";

    static ExecutorService executorService;

    public NaNegativeReader() {
        executorService = Executors.newWorkStealingPool();
    }

    public static Shop GetCommentList(Elements elements) throws IOException {
        Document ShopPage;

        int check = Integer.parseInt(elements.select("span[class^=num]").text());
        if(check < 50)
        {
            System.out.println("Пропустили: " + elements.select("a[class^=ss]").text());
            return null;
        }

        String link = elements.select("a[class^=ss]").attr("href").toString();
        ShopPage = Jsoup.connect("https://na-negative.ru" + link).get();

        Elements temp = ShopPage.select("#main > div.frame > div.pagination-holder > ul").getFirst().getElementsByTag("li");

        String shop = ShopPage.select("#main > div.frame > div.reviewers-holder > header > h1").text();
        Shop tempShop = new Shop(shop);

        try {
            for (int i = 1; i < temp.size(); i++)
            {
                String link2 = temp.get(i).child(0).attr("href");
                ShopPage = Jsoup.connect("https://na-negative.ru" + link2).get();
                Elements comms = ShopPage.select("#main > div.frame > div.reviewers-holder > div").getFirst().getElementsByClass("reviewers-box");
                tempShop.AddComments(comms);
                System.out.println("Считывание магазина: " + shop + ", страница: " + i);
            }

        } catch (IOException e) {
            System.out.println("Ссылки не существует!");
            throw new RuntimeException(e);
        }
        System.out.flush();
        System.out.println("Страница считана!");
        tempShop.CalculateRate();
        return tempShop;
    }

    public static void GetAllCommentPagesAndWriteToJson() throws IOException, InterruptedException, ExecutionException, NoSuchFieldException, IllegalAccessException
    {

        String html = "https://na-negative.ru/internet-magaziny?page=" +1;
        System.out.println("Считывание страницы под номером " + 1 + "...");

        Document doc = Jsoup.connect(html).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36").get();
        Elements elements = doc.select("#main > div.frame > div.reviewers-holder > div").get(0).getElementsByClass("find-list-box");

        CountDownLatch latch = new CountDownLatch(elements.size() + 1);

        ArrayList<Shop> MainData = new ArrayList<Shop>();
        try (FileWriter writer = new FileWriter(PATH, false))
        {
            int count = 0;

            for (int i = 0; i <= elements.size(); i++)
            {
                int pageNumber = i;
                count++;
                int finalCount = count;
                executorService.submit(() ->
                {
                        try
                        {
                            Shop Data = GetCommentList(elements.get(pageNumber).getAllElements());
                            if(Data != null)MainData.add(Data);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                         finally {
                        latch.countDown();
                        }
                });
            }
            try {
                latch.await();
                JsonParser<Shop> parser = new JsonParser<Shop>();
                try {
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
