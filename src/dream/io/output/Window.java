package dream.io.output;

import dream.io.input.KeyListener;
import dream.io.input.MouseListener;
import dream.io.input.WindowResizeListener;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private static int WINDOW_WIDTH = 1200, WINDOW_HEIGHT = 800;
    public static int PREV_WINDOW_WIDTH = WINDOW_WIDTH, PREV_WINDOW_HEIGHT = WINDOW_HEIGHT;

    public static String WINDOW_TITLE = "DreamMaker";
    private long windowID;

    private static Window CURRENT_WINDOW;

    public static double lastFrameTime = 0.0, delta = 0.0;


    public static Window getDreamWindow()
    {
        if(CURRENT_WINDOW == null)
            CURRENT_WINDOW = new Window();
        return CURRENT_WINDOW;
    }

    private Window()
    {
        GLFWErrorCallback.createPrint(System.err).set();
        GLFWInit();
    }

    public static float getTargetAspectRatio()
    {
        return 16.0f / 9.0f;
    }

    private void GLFWInit()
    {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW!");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        this.windowID = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        if (this.windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW Window!");

        glfwSetCursorPosCallback(windowID, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(windowID, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowID, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(windowID, KeyListener::keyCallback);

        try (MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(this.windowID, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(this.windowID,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);

            glfwSetWindowSizeCallback(this.windowID, WindowResizeListener::onResize);

            glfwMakeContextCurrent(this.windowID);
            glfwSwapInterval(1);
        }

        lastFrameTime = glfwGetTime();

        GL.createCapabilities();
    }

    public void showWindow()
    {
        glfwShowWindow(this.windowID);
    }

    public void startFrame()
    {
        double currentFrameTime = glfwGetTime();
        delta = (lastFrameTime > 0) ? (currentFrameTime - lastFrameTime) : (1f / 60);
        lastFrameTime = currentFrameTime;
    }

    public void endFrame()
    {
        glfwSwapBuffers(this.windowID);
        glfwPollEvents();
        MouseListener.endFrame();
    }

    public static double getDelta()
    {
        return delta;
    }

    public long getWindowId()
    {
        return this.windowID;
    }

    public void cleanUp()
    {
        glfwFreeCallbacks(this.windowID);
        glfwDestroyWindow(this.windowID);
    }

    public static int getWindowWidth()
    {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight()
    {
        return WINDOW_HEIGHT;
    }

    public static void setWindowWidth(int newWidth)
    {
        WINDOW_WIDTH = newWidth;
    }

    public static void setWindowHeight(int newHeight)
    {
        WINDOW_HEIGHT = newHeight;
    }

    public static int getPrevWindowWidth()
    {
        return PREV_WINDOW_WIDTH;
    }

    public static int getPrevWindowHeight()
    {
        return PREV_WINDOW_HEIGHT;
    }

    public static void updateWindowSize()
    {
        PREV_WINDOW_WIDTH = WINDOW_WIDTH;
        PREV_WINDOW_HEIGHT = WINDOW_HEIGHT;
    }
}
