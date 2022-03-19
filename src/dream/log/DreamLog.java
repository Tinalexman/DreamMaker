package dream.log;

import java.io.File;
import java.io.PrintStream;

public class DreamLog
{
    private static File outputFile;
    private static PrintStream printStream;
    private static boolean redirectToFile;
    private static boolean respondToLog;
    private static LogFormat logFormat;


    public static void init()
    {
        logFormat = new LogFormat();
        respondToLog = true;
        redirectToFile = false;
    }

    public static boolean isRespondingToLog()
    {
        return respondToLog;
    }

    public static void setResponseToLog(boolean respondToLog)
    {
        DreamLog.respondToLog = respondToLog;
    }

    public static void setPrintStream(PrintStream stream)
    {
        printStream = stream;
    }

    public void setOutputFile(File file)
    {
        outputFile = file;
    }

    public static void redirectOutput()
    {
        redirectToFile = !redirectToFile;
    }

    public static void log(String header, String message, LogFlag flag)
    {
        if(!respondToLog)
            return;

        switch (flag)
        {
            case ERROR -> logFormat.onError();
            case NORMAL -> logFormat.normalize();
            case SUCCESS -> logFormat.onSuccess();
            case WARNING -> logFormat.onWarning();
            default -> { return; }
        }

        if(!redirectToFile)
        {
            printStream.println(logFormat.getHeaderTextbackgroundColor() + logFormat.getHeaderTextColor()
                    + header + ":" + logFormat.getResetFlag() + logFormat.getMainTextBackgroundColor()
                    + logFormat.getMainTextColor() + " " + message + logFormat.getResetFlag());
        }
    }

}
