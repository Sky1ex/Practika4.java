package Classes;

import JsonParser.*;
import SubClassesBookMix.*;
import SubClassesShops.Review;
import SubClassesShops.Shop;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static SubClassesShops.Shop.countReviewsWithLowRating;
import static SubClassesBookMix.Book.ConvertToBook;
import static SubClassesBookMix.Comment.ConvertToComment;
import static JsonParser.JsonParser.ReadJson;
import static SubClassesBookMix.BookWithPic.ConvertToBookWithPic;
import static SubClassesBookMix.MiniBook.ConvertToMiniBook;

public class Print
{

    public static void print1_1() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        CsvParser<Comment> parserCsv = new CsvParser<Comment>();

        ArrayList<CommentsBook> Data = JsonParser.ReadJson("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Comment> DataComment = ConvertToComment(Data);
        try (FileWriter writer = new FileWriter("src/main/java/Data/Comment.csv", StandardCharsets.UTF_16, false)) {
            parserCsv.Write(DataComment);
            writer.write(parserCsv.getText());
        }
    }

    public static void print1_2() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        CsvParser<MiniBook> parserCsv = new CsvParser<MiniBook>();

        ArrayList<CommentsBook> Data  = JsonParser.ReadJson("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<MiniBook> DataComment = ConvertToMiniBook(Data);
        try (FileWriter writer = new FileWriter("src/main/java/Data/Book.csv", StandardCharsets.UTF_16, false)) {
            parserCsv.Write(DataComment);
            writer.write(parserCsv.getText());
        }
    }

    public static void print1_3() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        CsvParser<BookWithPic> parserCsv = new CsvParser<BookWithPic>();

        ArrayList<CommentsBook> Data = JsonParser.ReadJson("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataBook = ConvertToBook(Data);
        DataBook = DataBook.stream()
                .filter(book -> book.getComments().stream().allMatch(comment -> comment.getRate() == 5))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<BookWithPic> Books = ConvertToBookWithPic(DataBook);

        try (FileWriter writer = new FileWriter("src/main/java/Data/Book5.csv", StandardCharsets.UTF_16, false)) {
            parserCsv.Write(Books);
            writer.write(parserCsv.getText());
        }

        Books.forEach((book -> System.out.println("Book: Name=" + book.getName() + ", Author=" + book.getAuthor())));
    }

    public static void print2_1() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        ArrayList<Shop> Data = JsonParser.ReadJson("src/main/java/Data/DatasetNaNegative.json", Shop.class);
        Data.stream()
                .flatMap(shop -> shop.getComments().stream()
                        .filter(comment -> comment.getMinuses().length() == 1)
                        .map(comment -> new Review(shop.getName(), comment.getPluses(), comment.getRate(), comment.getText()))
                )
                .forEach(System.out::println);
    }

    public static void print2_2() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        ArrayList<Shop> Data = JsonParser.ReadJson("src/main/java/Data/DatasetNaNegative.json", Shop.class);
        countReviewsWithLowRating(Data);
    }

    public static void print2_3() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        ArrayList<Shop> Data = JsonParser.ReadJson("src/main/java/Data/DatasetNaNegative.json", Shop.class);

        Data.forEach(shop -> {
            Map<String, Long> commentGroups = shop.getCommentGroups();
            System.out.println("Магазин: " + shop.getName());
            System.out.println("Позитивные комментарии: " + commentGroups.getOrDefault("позитивный", 0L));
            System.out.println("Нейтральные комментарии: " + commentGroups.getOrDefault("нейтральный", 0L));
            System.out.println("Негативные комментарии: " + commentGroups.getOrDefault("негативный", 0L) + "\n");
        });
    }
}
