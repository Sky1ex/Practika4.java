package SubClassesBookMix;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Comment
{
    protected String author;
    protected String text;
    protected int rate;

    public Comment(CommentsBook FullComment)
    {
        author = FullComment.getAuthorComment();
        text = FullComment.getComment();
        rate = FullComment.getRating();
    }

    public static ArrayList<Comment> ConvertToComment(ArrayList<CommentsBook> FullComment)
    {
        return FullComment.stream()
                .map(commentBook -> new Comment(commentBook.getAuthor(), commentBook.getComment(), commentBook.getRating()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return "Comment: " + "Author=" + author + ", Text=" + text + ", Rating=" + rate;
    }
}
