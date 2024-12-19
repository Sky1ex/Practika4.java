package SubClassesBookMix;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class BookWithPic
{
    protected String name;
    protected String author;
    protected String PicAddress;

    public static ArrayList<BookWithPic> ConvertToBookWithPic(ArrayList<Book> commentsBook)
    {
        // Группируем комментарии по названию книги
        Map<String, List<Book>> groupedComments = commentsBook.stream()
                .collect(Collectors.groupingBy(Book::getName));

        // Создаем список книг на основе группированных комментариев
        return groupedComments.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    List<Book> commentsList = entry.getValue();
                    // Используем первый комментарий для получения автора и адреса изображения
                    Book firstCommentBook = commentsList.get(0);
                    return new BookWithPic(name, firstCommentBook.getAuthor(), firstCommentBook.getPicAddress());
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
