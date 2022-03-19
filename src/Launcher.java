import dream.DreamMaker;

public final class Launcher
{

    public static String PROJECT_PATH;

    static
    {
        PROJECT_PATH = System.getProperty("user.dir");
    }

    public static void main(String[] args)
    {
        DreamMaker.startEngine();
    }
}
