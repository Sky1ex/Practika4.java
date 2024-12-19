package SubClassesShops;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.select.Elements;

@AllArgsConstructor
@Getter
public class CommentsNaNegative
{
    protected int rate;
    protected String author;
    protected String pluses;
    protected String minuses;
    protected String text;

    public CommentsNaNegative(Elements comment)
    {
        rate = Integer.parseInt(comment.select("span[itemprop^=ratingValue]").text());
        author = comment.select("span[itemprop^=author]").text().replaceAll("\"", "%22").replaceAll("\\\\", "\\\\\\\\");
        pluses = comment.select("td[itemprop^=pro]").text().replaceAll("\"", "%22").replaceAll("\\\\", "\\\\\\\\");
        minuses = comment.select("td[itemprop^=contra]").text().replaceAll("\"", "%22").replaceAll("\\\\", "\\\\\\\\");
        text = comment.select("td[itemprop^=reviewBody]").text().replaceAll("\"", "%22").replaceAll("\\\\", "\\\\\\\\");
    }

    public int getRate() {
        return rate;
    }
}
