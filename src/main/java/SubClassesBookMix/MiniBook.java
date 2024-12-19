package SubClassesBookMix;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class MiniBook
{
    protected String name;
    protected String author;

    public static ArrayList<MiniBook> ConvertToMiniBook(ArrayList<CommentsBook> commentsBook)
    {
        // Группируем комментарии по названию книги
        Map<String, List<CommentsBook>> groupedComments = commentsBook.stream()
                .collect(Collectors.groupingBy(CommentsBook::getName));

        // Создаем список книг на основе группированных комментариев
        return groupedComments.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    List<CommentsBook> commentsList = entry.getValue();
                    // Используем первый комментарий для получения автора и адреса изображения
                    CommentsBook firstCommentBook = commentsList.get(0);
                    ArrayList<Comment> comments = commentsList.stream()
                            .map(Comment::new)
                            .collect(Collectors.toCollection(ArrayList::new));
                    return new MiniBook(name, firstCommentBook.getAuthor());
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
