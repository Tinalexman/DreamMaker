package dream.io.input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener
{
    private static KeyListener instance;
    private final boolean[] keys;

    private KeyListener()
    {
        this.keys = new boolean[GLFW_KEY_LAST];
    }

    public static void keyCallback(long windowID, int key, int scancode, int action, int mods)
    {
        if(action == GLFW_PRESS)
            getListener().keys[key] = true;
        else if(action == GLFW_RELEASE)
            getListener().keys[key] = false;
    }

    public static boolean isKeyDown(int keyCode)
    {
        return getListener().keys[keyCode];
    }

    public static KeyListener getListener()
    {
        if(instance == null)
            instance = new KeyListener();
        return instance;
    }


}
