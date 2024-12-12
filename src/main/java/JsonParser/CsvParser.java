package JsonParser;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CsvParser<T>
{
    private String text = "";

    private void AddLine(Field item, Object value, boolean lastElement)
    {
        text +="\"" + value + "\"";
        if(lastElement) return;
        else text += ";";
    }

    public synchronized void Write(ArrayList<T> sourceClass) throws IllegalAccessException
    {
        Field[] fields = sourceClass.getFirst().getClass().getDeclaredFields();
        Class<?> className = sourceClass.getFirst().getClass();

        T element = sourceClass.getFirst();
        for(int j = 0; j < fields.length-1; j++)
        {
            text += fields[j].getName() + ";";
        }
        text += fields[fields.length-1].getName() + "\n";

        for(int i = 0; i < sourceClass.size(); i++)
        {
            element = sourceClass.get(i);
            for(int j = 0; j < fields.length; j++)
            {
                Field item = fields[j];
                item.setAccessible(true);
                Object value = item.get(element);
                AddLine(item, value, (j == fields.length-1));
            }
            text += "\n";
        }
    }

    public String getText() {
        return text;
    }
}
