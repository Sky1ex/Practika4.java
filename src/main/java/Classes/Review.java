package Classes;

import java.util.ArrayList;

public class Review
{
    protected String name;
    protected String pluses;
    protected int rate;
    protected String text;

    public Review(String name, String pluses, int rate, String text)
    {
        this.name = name;
        this.pluses = pluses;
        this.rate = rate;
        this.text = text;
    }

    public static void ConvertToReviewOnlyPluses(ArrayList<Shop> shop)
    {
        for(int i = 0; i < shop.size(); i++)
        {
            for(int j = 0; j < shop.get(i).comments.size(); j++)
            {
                CommentsNaNegative temp = shop.get(i).comments.get(j);
                if(temp.minuses.length() == 1) System.out.println(new Review(shop.get(i).name, temp.pluses, temp.rate, temp.text).toString());
            }
        }
    }

    @Override
    public String toString()
    {
        return "Review: " + "name= " + name + ", pluses= " + pluses + ", rate= " + rate + ", comment= " + text;
    }
}
