package JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser<T>
{
    private String text = "";
    private int depth = 1;

    private void AddLine(Field item, Object value, boolean lastElement)
    {
        Tab();
        text += "\"" + item.getName() + "\": ";
        if(value.getClass() == String.class) text += "\"" +  value + "\"";
        else text += value;
        if(lastElement) text += "\n";
        else text += ",\n";
    }

    private void AddTab()
    {
        for(int i = 0; i < depth; i++)
        {
            text += "   ";
        }
        depth++;
    }

    private void DelTab()
    {
        for(int i = 0; i < depth; i++)
        {
            text += "   ";
        }
        depth--;
    }

    private void Tab()
    {
        for(int i = 0; i < depth; i++)
        {
            text += "   ";
        }
    }

    private void Del()
    {
        depth--;
        for(int i = 0; i < depth; i++)
        {
            text += "   ";
        }
    }

    public synchronized void Write(ArrayList<T> sourceClass) throws NoSuchFieldException, IllegalAccessException
    {
        Field[] fields = sourceClass.getFirst().getClass().getDeclaredFields();
        Class<?> className = sourceClass.getFirst().getClass();
        text += "[\n";

        for(int i = 0; i < sourceClass.size(); i++)
        {
            T element = sourceClass.get(i);
            AddTab();
            text += "{\n";
            for(int j = 0; j < fields.length; j++)
            {
                Field item = fields[j];
                item.setAccessible(true);
                Object value = item.get(element);

                if (value instanceof ArrayList<?> nestedList)
                {
                    AddTab();
                    WriteWithName((ArrayList<T>) nestedList);
                    continue;
                }
                else if(!(value.getClass().getPackage().getName().startsWith("java.") || value.getClass().getPackage().getName().startsWith("javax."))) {
                    Write((T) value);
                    continue;
                }

                AddLine(item, value, (j == fields.length-1));
            }
            Del();
            /*if(!flag)text += "},\n";*/
            if(i == sourceClass.size()-1)text += "}\n";
            else text += "},\n";
        }

        //text += "\n";
        Del();
        text += "]\n";
    }

    private synchronized void WriteWithName(ArrayList<T> sourceClass) throws NoSuchFieldException, IllegalAccessException
    {
        Field[] fields = sourceClass.getFirst().getClass().getDeclaredFields();
        Class<?> className = sourceClass.getFirst().getClass();
        text += "\"" + sourceClass.getFirst().getClass().getSimpleName() + "\"" + ": [\n";

        for(int i = 0; i < sourceClass.size(); i++)
        {
            T element = sourceClass.get(i);
            AddTab();
            text += "{\n";
            for(int j = 0; j < fields.length; j++)
            {
                Field item = fields[j];
                item.setAccessible(true);
                Object value = item.get(element);

                if (value instanceof ArrayList<?> nestedList)
                {
                    AddTab();
                    WriteWithName((ArrayList<T>) nestedList);
                    continue;
                }
                else if(!(value.getClass().getPackage().getName().startsWith("java.") || value.getClass().getPackage().getName().startsWith("javax."))) {
                    Write((T) value);
                    continue;
                }

                AddLine(item, value, (j == fields.length-1));
            }
            Del();
            /*if(!flag)text += "},\n";*/
            if(i == sourceClass.size()-1)text += "}\n";
            else text += "},\n";
        }

        //text += "\n";
        Del();
        text += "]\n";
    }

    public void Write(T sourceClass) throws NoSuchFieldException, IllegalAccessException
    {
        Field[] fields = sourceClass.getClass().getDeclaredFields();
        AddTab();
        text += "{\n";

        for(int i = 0; i < fields.length; i++)
        {
            Field item = fields[i];
            item.setAccessible(true);
            Object value = item.get(sourceClass);
            boolean lastElement = false;

            if (value instanceof ArrayList<?> nestedList)
            {
                Write((ArrayList<T>) nestedList/*, false*/);
                continue;
            }
            else if(!(value.getClass().getPackage().getName().startsWith("java.") || value.getClass().getPackage().getName().startsWith("javax."))) Write((T) value);

            AddLine(item, value, (i == fields.length - 1));

        }
        DelTab();
        text += "}\n";
    }

    public String getText() {
        return text;
    }

    public static <T> ArrayList<T> Read(String filePath, Class<T> clazz) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<T> objects = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonBuilder.append(line.trim());
        }
        br.close();

        String jsonString = jsonBuilder.toString();
        if (jsonString.startsWith("[") && jsonString.endsWith("]"))
        {
            jsonString = jsonString.substring(1, jsonString.length() - 1).trim();
            String[] jsonObjects;
            if(jsonString.contains("[") && jsonString.contains("]"))
            {
                List<String> temp = new ArrayList<>();
                int openBraces = 0;
                int start = -1;
                boolean inQuotes = false;
                boolean escape = false;
                for (int i = 0; i < jsonString.length(); i++) {
                    char c = jsonString.charAt(i);

                    if (c == '"') {
                        if (!escape) {
                            inQuotes = !inQuotes;
                        }
                        escape = false;
                    } else if (c == '\\') {
                        escape = !escape;
                    } else if (!inQuotes) {
                        if (c == '{') {
                            if (openBraces == 0) {
                                start = i; // Запоминаем начало объекта
                            }
                            openBraces++;
                        } else if (c == '}') {
                            openBraces--;
                            if (openBraces == 0 && start != -1) {
                                // Запоминаем конец объекта
                                temp.add(jsonString.substring(start, i + 1).trim());
                                start = -1; // Сбрасываем начало объекта
                            }
                        }
                    } else {
                        escape = false;
                    }
                }
                jsonObjects = temp.toArray(new String[0]);
            }
            else jsonObjects = jsonString.split("}\\s*,\\s*\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.trim();
                if (!jsonObject.startsWith("{")) {
                    jsonObject = "{" + jsonObject;
                }
                if (!jsonObject.endsWith("}")) {
                    jsonObject = jsonObject + "}";
                }
                T obj = createInstanceFromJson(jsonObject, clazz, jsonBuilder);
                objects.add(obj);
            }
        }
        return objects;
    }

    public static <T> ArrayList<T> Read(Class<T> clazz, StringBuilder jsonBuilder) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<T> objects = new ArrayList<>();
        String jsonString = jsonBuilder.toString().trim();
        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1).trim();
            String[] jsonObjects = jsonString.split("\\}\\s*,\\s*\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.trim();
                if (!jsonObject.startsWith("{")) {
                    jsonObject = "{" + jsonObject;
                }
                if (!jsonObject.endsWith("}")) {
                    jsonObject = jsonObject + "}";
                }
                T obj = createInstanceFromJson(jsonObject, clazz, jsonBuilder);
                objects.add(obj);
            }
        }
        return objects;
    }

    private static <T> T createInstanceFromJson(String jsonObject, Class<T> clazz, StringBuilder jsonBuilder) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        Constructor<T> constructor = null;
        for(int i = 0; i < constructors.length; i++)
        {
            if(constructors[i].getParameters().length == clazz.getDeclaredFields().length) constructor = constructors[i];

        }
        if(constructor == null) throw new IllegalArgumentException("Не обнаружен нужный конструктор!: ");
        //Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Field[] fields = clazz.getDeclaredFields();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++)
        {
            String fieldName = fields[i].getName();
            String fieldValue = extractField(jsonObject, fieldName);
            Class<?> fieldType = parameters[i].getType();

            if (fieldType == String.class)
            {
                args[i] = fieldValue;
            } else if (fieldType == int.class || fieldType == Integer.class)
            {
                args[i] = Integer.parseInt(fieldValue);
            }
            else if(fieldType == double.class || fieldType == Double.class)
            {
                args[i] = Double.parseDouble(fieldValue);
            }
            else if(fieldType == ArrayList.class)
            {
                 /*T lol = (ArrayList<T>) parameters[i].getParameterizedType();
                //fieldType = (Class<?>)
                args[i] = Read(lol, jsonBuilder);*/
                if (fields[i].getGenericType() instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) fields[i].getGenericType();
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    if (genericType instanceof Class) {
                        Class<?> genericClass = (Class<?>) genericType;
                        StringBuilder sb = new StringBuilder(jsonObject.substring(jsonObject.indexOf(": [")+1, jsonObject.lastIndexOf("]")+1));
                        args[i] = Read(genericClass, sb);
                    } else {
                        throw new IllegalArgumentException("Unsupported generic type: " + genericType);
                    }
                }
            }
            else
            {
                throw new IllegalArgumentException("Unsupported field type: " + fieldType);
            }
        }

        return constructor.newInstance(args);
    }

    /*private static <T> ArrayList<T> parseJsonArray(String jsonString, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ArrayList<T> objects = new ArrayList<>();
        if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1).trim();
            String[] jsonObjects = jsonString.split("\\}\\s*,\\s*\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.trim();
                if (!jsonObject.startsWith("{")) {
                    jsonObject = "{" + jsonObject;
                }
                if (!jsonObject.endsWith("}")) {
                    jsonObject = jsonObject + "}";
                }
                T obj = createInstanceFromJson(jsonObject, clazz);
                objects.add(obj);
            }
        }
        return objects;
    }*/

    /*private static String extractField(String jsonObject, String fieldName)
    {
        String field = "\"" + fieldName + "\":";
        int startIndex = jsonObject.indexOf(field) + field.length();
        char nextChar = jsonObject.charAt(startIndex);
        if (nextChar == '"')
        {
            startIndex++; // Skip the opening quote
            int endIndex = jsonObject.indexOf("\"", startIndex);
            return jsonObject.substring(startIndex, endIndex);
        }
        else if(Character.isDigit(nextChar))
        {
            int endIndex = jsonObject.indexOf(",", startIndex);
            return jsonObject.substring(startIndex, endIndex).trim();
        }
        else
        {
            int endIndex = jsonObject.indexOf("\",", startIndex);
            if (endIndex == -1) {
                endIndex = jsonObject.indexOf("}", startIndex);
            }
            return jsonObject.substring(startIndex, endIndex).trim();
        }
    }*/

    private static String extractField(String jsonObject, String fieldName)
    {
        String field = "\"" + fieldName + "\":";
        int startIndex = jsonObject.indexOf(field) + field.length()+1;
        char nextChar = jsonObject.charAt(startIndex);
        if (nextChar == '"')
        {
            startIndex++; // Skip the opening quote
            int endIndex = jsonObject.indexOf("\"", startIndex);
            return jsonObject.substring(startIndex, endIndex);
        }
        else if(Character.isDigit(nextChar))
        {
            int i = startIndex;
            String result = "";
            /*while(Character.isDigit(jsonObject.charAt(i)))
            {
                result += jsonObject.charAt(i); i++;
            }*/
            while(jsonObject.charAt(i) == '.' || Character.isDigit(jsonObject.charAt(i)) )
            {
                result += jsonObject.charAt(i); i++;
            }
            return result;
        }
        else
        {
            int endIndex = jsonObject.indexOf("\",", startIndex);
            if (endIndex == -1) {
                endIndex = jsonObject.indexOf("}", startIndex);
            }
            return jsonObject.substring(startIndex, endIndex).trim();
        }
    }
}
