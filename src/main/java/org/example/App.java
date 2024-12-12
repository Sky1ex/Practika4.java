package org.example;

import Classes.*;
import JsonParser.JsonParser;
import JsonParser.*;
import org.apache.commons.lang3.time.StopWatch;

import java.beans.Encoder;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static Classes.Book.ConvertToBook;
import static Classes.BookMixReader.PrintOnlyFive;
import static Classes.Comment.ConvertToComment;
import static Classes.Review.ConvertToReviewOnlyPluses;

public class App
{
    public static void main( String[] args ) throws NoSuchFieldException, IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        /*BookMixReader<?> reader = new BookMixReader<>();
        try
        {
            StopWatch watch = new StopWatch();
            watch.start();
            reader.GetAllCommentPagesAndWriteToJson();
            watch.stop();
            System.out.println("Время выполнения: " + (watch.getTime()/1000) + " секунд");
        }
        catch (IOException | InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }*/

        /*NaNegativeParser<?> reader = new NaNegativeParser<>();
        try
        {
            StopWatch watch = new StopWatch();
            watch.start();
            reader.GetAllCommentPagesAndWriteToJson();
            watch.stop();
            System.out.println("Время выполнения: " + (watch.getTime()/1000) + " секунд");
        }
        catch (IOException | InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }*/

        /*JsonParser<CommentsBook> parser = new JsonParser<CommentsBook>();
        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = (ArrayList<CommentsBook>) parser.Read("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataBook = ConvertToBook(Data);
        for(int i = 0; i < DataBook.size(); i++)
        {
            System.out.println(DataBook.get(i).toString());
        }*/

        //N1

        //1
        /*JsonParser<CommentsBook> parserJson = new JsonParser<CommentsBook>();
        CsvParser<Comment> parserCsv = new CsvParser<Comment>();

        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = parserJson.Read("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Comment> DataComment = ConvertToComment(Data);
        FileWriter writer = new FileWriter("src/main/java/Data/Comment.csv", StandardCharsets.UTF_16,false);
        parserCsv.Write(DataComment);
        writer.write(parserCsv.getText());*/

        //2
        /*JsonParser<CommentsBook> parserJson = new JsonParser<CommentsBook>();
        CsvParser<Book> parserCsv = new CsvParser<Book>();

        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = parserJson.Read("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataComment = ConvertToBook(Data);
        FileWriter writer = new FileWriter("src/main/java/Data/Book.csv", StandardCharsets.UTF_16,false);
        parserCsv.Write(DataComment);
        writer.write(parserCsv.getText());*/

        //3

        /*JsonParser<CommentsBook> parser = new JsonParser<CommentsBook>();
        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = (ArrayList<CommentsBook>) parser.Read("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataBook = ConvertToBook(Data);
        DataBook = DataBook.stream()
                .filter(book -> book.getComments().stream().allMatch(comment -> comment.getRate() == 5))
                .collect(Collectors.toCollection(ArrayList::new));

        DataBook.forEach((book -> System.out.println("Book: Name=" + book.getName() + ", Author=" + book.getAuthor())));*/

        /*for(int i = 0; i < DataBook.size(); i++)
        {
            System.out.println(DataBook.get(i).toString());
        }*/

        //N2

        //1
        /*JsonParser<CommentsBook> parser = new JsonParser<CommentsBook>();
        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = (ArrayList<CommentsBook>) parser.Read("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataBook = ConvertToBook(Data);
        PrintOnlyFive(DataBook);*/

        //2
        /*JsonParser<Shop> parser = new JsonParser<Shop>();
        ArrayList<Shop> Data = new ArrayList<>();
        Data = (ArrayList<Shop>) parser.Read("src/main/java/Data/DatasetNaNegative.json", Shop.class);
        ConvertToReviewOnlyPluses(Data);*/

        //3
        JsonParser<Shop> parser = new JsonParser<Shop>();
        ArrayList<Shop> Data = new ArrayList<>();
        Data = (ArrayList<Shop>) parser.Read("src/main/java/Data/DatasetNaNegative.json", Shop.class);
        Data.forEach(shop -> {
            Map<String, Long> commentGroups = shop.getCommentGroups();
            System.out.println("Shop: " + shop.getName());
            System.out.println("Positive comments: " + commentGroups.getOrDefault("positive", 0L));
            System.out.println("Neutral comments: " + commentGroups.getOrDefault("neutral", 0L));
            System.out.println("Negative comments: " + commentGroups.getOrDefault("negative", 0L));
        });
    }
}
