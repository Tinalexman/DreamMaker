package dream.scripting;

public class Script
{

    public Script()
    {

    }

    public static String getScriptTemplate(String scriptName)
    {
        return "package dream.ecs.components;" + '\n' +
                "public class " + scriptName + " implements Scriptable" + '\n' +
                "{  " + '\n' +
                "   public void onUpdate(float delta)" + '\n' +
                "   {" + '\n' +
                "   }" + '\n' +
                "}";
    }

    public String getClassName()
    {
        return "Script";
    }

    public void onUpdate(float delta)
    {

    }

}
