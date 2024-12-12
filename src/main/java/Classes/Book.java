package Classes;


import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Book
{
    protected String name;
    protected String author;
    protected String PicAddress;
    public ArrayList<Comment> comments;

    public Book(CommentsBook FullComment)
    {
        name = FullComment.Name;
        author = FullComment.Author;
        PicAddress = FullComment.PicAddress;
        comments = new ArrayList<>();
        comments.add(new Comment(FullComment));
    }

    public void AddComment(CommentsBook FullComment)
    {
        Comment comment = new Comment(FullComment);
        comments.add(comment);
    }

    public static ArrayList<Book> ConvertToBook(ArrayList<CommentsBook> CommentsBook)
    {
        ArrayList<Book> Books = new ArrayList<>();
        for(int i = 0; i < CommentsBook.size(); i++)
        {
            if(Books.contains(CommentsBook.get(i).Name))
            {
                Books.get(Books.indexOf(CommentsBook.get(i).Name)).AddComment(CommentsBook.get(i));
            }
            else Books.add(new Book(CommentsBook.get(i)));
        }
        return Books;
    }

    /*@Override
    public String toString()
    {
        if(comments.isEmpty()) return "";
        String temp = "Book{" + "Name='" + name + '\'' + ", Author='" + author + '\'';
        for(int i = 0; i < comments.size()-1; i++)
        {
            temp+= comments.get(i).toString() + ", ";
        }
        temp+= comments.get(comments.size()-1).toString();
        return temp;
    }*/

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
