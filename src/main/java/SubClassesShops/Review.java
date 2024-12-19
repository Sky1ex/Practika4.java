package SubClassesShops;

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

    @Override
    public String toString()
    {
        return "Review: " + "name= " + name + ", pluses= " + pluses + ", rate= " + rate + ", comment= " + text;
    }
}
