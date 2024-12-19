package SubClassesShops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Shop
{
    protected String name;
    protected double averageRate = 0;
    protected ArrayList<CommentsNaNegative> comments;

    public Shop(String name)
    {
        this.name = name;
        this.comments = new ArrayList<>();
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

    public static void countReviewsWithLowRating(ArrayList<Shop> shops) {
        shops.forEach(shop -> {
            long count = shop.getComments().stream()
                    .filter(comment -> comment.getRate() <= 2)
                    .count();
            System.out.println("Магазин: " + shop.getName() + ", Количество отзывов с рейтингом ≤ 2: " + count);
        });
    }

    public Map<String, Long> getCommentGroups() {
        return comments.stream()
                .collect(Collectors.groupingBy(comment -> {
                    int rate = comment.getRate();
                    if (rate > 3) {
                        return "позитивный";
                    } else if (rate < 3) {
                        return "негативный";
                    } else {
                        return "нейтральный";
                    }
                }, Collectors.counting()));
    }

}
