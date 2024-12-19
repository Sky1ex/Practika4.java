package SubClassesBookMix;

import lombok.Getter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Getter
public class CommentsBook
{
    protected String Name;
    protected String Author;
    protected String PicAddress;
    protected String Comment;
    protected String AuthorComment;
    protected int Rating;

    public CommentsBook(String Name, String Author, String PicAddress, String Comment, String AuthorComment, int Rating)
    {
        this.Name = Name;
        this.Author = Author;
        this.PicAddress = PicAddress;
        this.AuthorComment = AuthorComment;
        this.Comment = Comment;
        this.Rating = Rating;
    }

    public CommentsBook(Elements book, Element comment)
    {
        this.Name = book.select("strong > a:nth-child(1)").getFirst().text().replaceAll("\"", "%22");
        this.Author = book.select("span[itemprop^=author] > a").text().replaceAll("\"", "%22");
        this.PicAddress = book.select("img[itemprop^=image]").attr("src").replaceAll("\"", "%22");
        this.AuthorComment = comment.select("div.comment-head > div > div.col > a").text().replaceAll("\"", "%22");
        this.Comment = comment.select("div[class^=comment-content]").text().replaceAll("\"", "%22");
        if(comment.select("div.comment-head > div > div.col > div").text().isEmpty())
        {
            String temp = comment.select("div.comment-head > div > div.col > div > div").getFirst().className();
            if(temp.charAt(temp.length() - 1) == ' ') this.Rating = 0;
            else this.Rating = Character.getNumericValue(temp.charAt(temp.length() - 1));
        }
        else this.Rating = -1;
    }
    @Override
    public String toString() {
        return "CommentsBook{" + "Name=" + Name + ", Author=" + Author + ", PicAddress=" + PicAddress + ", Comment=" + Comment + ", AuthorComment=" + AuthorComment + ", Rating=" + Rating;
    }
}
