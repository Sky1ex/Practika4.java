package SubClassesBookMix;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Book
{
    protected String name;
    protected String author;
    protected String PicAddress;
    public ArrayList<Comment> comments;

    public static ArrayList<Book> ConvertToBook(ArrayList<CommentsBook> commentsBook)
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
                    return new Book(name, firstCommentBook.getAuthor(), firstCommentBook.getPicAddress(), comments);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString()
    {
        if(comments.isEmpty()) return "";
        String temp = "Book: " + "Name=" + name + ", Author=" + author + " ";
        for(int i = 0; i < comments.size()-1; i++)
        {
            temp+= comments.get(i).toString() + ", ";
        }
        temp+= comments.get(comments.size()-1).toString();
        return temp;
    }
}
