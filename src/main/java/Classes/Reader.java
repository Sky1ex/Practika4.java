package Classes;

import JsonParser.JsonParser;
import SubClassesBookMix.Book;
import SubClassesBookMix.CommentsBook;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static SubClassesBookMix.Book.ConvertToBook;

public class Reader
{
    public static void readBookMix()
    {
        try
        {
            StopWatch watch = new StopWatch();
            watch.start();
            BookMixReader.GetAllCommentPagesAndWriteToJson();
            watch.stop();
            System.out.println("Время выполнения: " + (watch.getTime()/1000) + " секунд");
        }
        catch (IOException | InterruptedException | ExecutionException | NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static void readNaNegative()
    {
        try
        {
            StopWatch watch = new StopWatch();
            watch.start();
            NaNegativeReader.GetAllCommentPagesAndWriteToJson();
            watch.stop();
            System.out.println("Время выполнения: " + (watch.getTime()/1000) + " секунд");
        }
        catch (IOException | InterruptedException | ExecutionException | NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static void ReadNaNegativeAndPrint() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException
    {
        ArrayList<CommentsBook> Data = new ArrayList<>();
        Data = (ArrayList<CommentsBook>) JsonParser.ReadJson("src/main/java/Data/DatasetBook.json", CommentsBook.class);
        ArrayList<Book> DataBook = ConvertToBook(Data);
        for(int i = 0; i < DataBook.size(); i++)
        {
            System.out.println(DataBook.get(i).toString());
        }
    }
}
