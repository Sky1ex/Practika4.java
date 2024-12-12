package Classes;

import lombok.Getter;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Shop
{
    protected String name;
    protected double averageRate = 0;
    protected ArrayList<CommentsNaNegative> comments;

    public Shop(String name, double averageRate, ArrayList<CommentsNaNegative> comments)
    {
        this.name = name;
        this.averageRate = averageRate;
        this.comments = comments;
    }

    public Shop(String name/*, Elements comments*/)
    {
        this.name = name;
        this.comments = new ArrayList<>();
        /*this.comments = new ArrayList<>();
        for(int i = 0; i < comments.size(); i++)
        {
            this.comments.add(new Comments(comments.get(i).getAllElements()));
        }*/

        /*for(int i = 0; i < this.comments.size(); i++)
        {
            averageRate += this.comments.get(i).getRate();
        }
        averageRate /= this.comments.size();*/
    }

    public void AddComments(Elements comments)
    {
        for(int i = 0; i < comments.size(); i++)
        {
            this.comments.add(new CommentsNaNegative(comments.get(i).getAllElements()));
        }
    }

    public void CalculateRate()
    {
        for(int i = 0; i < this.comments.size(); i++)
        {
            averageRate += this.comments.get(i).getRate();
        }
        averageRate /= this.comments.size();
    }

    public Map<String, Long> getCommentGroups() {
        return comments.stream()
                .collect(Collectors.groupingBy(comment -> {
                    int rate = comment.getRate();
                    if (rate > 3) {
                        return "positive";
                    } else if (rate < 3) {
                        return "negative";
                    } else {
                        return "neutral";
                    }
                }, Collectors.counting()));
    }
}
