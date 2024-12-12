package Logger;

public class Logger {

    private static int depth = 0;
    private static int lineNumber = 0;
    private static boolean isEnabled = false;
    private static boolean showLineNumbers = false;
    private static String path = "src/main/java/Data/Log.txt";

    public synchronized static void log(String message)
    {
        if(isEnabled)
        {
            if(showLineNumbers)
            {
                System.out.println(lineNumber++ + message);
            } else
            {
                System.out.println(message);
            }
        }
    }

    public synchronized static void openStack() {
        if(isEnabled) {
            depth++;
        }
    }

    public synchronized static void openStack(String message) {
        if(isEnabled) {
            depth++;
            log(getTabs() + message);
        }
    }

    public synchronized static void closeStack(String message) {
        if(isEnabled) {
            log(getTabs() + message);
            depth--;
        }
    }

    public synchronized static void closeStack() {
        if(isEnabled) {
            depth--;
        }
    }

    public synchronized static void logIntoStack(String message) {
        if(isEnabled) {
            depth++;
            log(getTabs() + message);
            depth--;
        }
    }

    private static String getTabs() {
        String tabs = "";
        for(int i = 1; i <= depth; i++) {
            tabs += "\t";
        }
        return tabs;
    }

    public static void enable() {
        isEnabled = true;
    }

    public static void disable() {
        isEnabled = false;
    }

    public static void setShowLineNumbers(boolean newValue) {
        showLineNumbers = newValue;
    }
}
