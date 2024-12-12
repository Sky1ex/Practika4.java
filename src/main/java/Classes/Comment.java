package Classes;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Comment
{
    protected String author;
    protected  String text;
    protected  int rate;

    public Comment(CommentsBook FullComment)
    {
        author = FullComment.AuthorComment;
        text = FullComment.Comment;
        rate = FullComment.Rating;
    }

    public static ArrayList<Comment> ConvertToComment(ArrayList<CommentsBook> FullComment)
    {
        ArrayList<Comment> comments = new ArrayList<>();
        for(int i = 0; i < FullComment.size(); i++)
        {
            comments.add(new Comment(FullComment.get(i)));
        }
        return comments;
    }

    /*@Override
    public String toString() {
        return "CommentsBook{" +
                "Author='" + author + '\'' +
                ", Text='" + text + '\'' +
                ", Rating='" + rate + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "Comment: " + "Author=" + author + ", Text=" + text + ", Rating=" + rate;
    }
}
