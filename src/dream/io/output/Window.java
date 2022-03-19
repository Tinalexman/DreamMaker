package dream.io.output;

import dream.ecs.containables.camera.Camera;
import dream.graphics.imgui.ImguiInterface;
import dream.io.input.KeyListener;
import dream.io.input.MouseListener;
import dream.graphics.renderer.Renderer;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    public static int WINDOW_WIDTH = 1200, WINDOW_HEIGHT = 800;
    public static int PREV_WINDOW_WIDTH = WINDOW_WIDTH, PREV_WINDOW_HEIGHT = WINDOW_HEIGHT;

    public static String WINDOW_TITLE = "DreamMaker";
    private long windowID;

    private static Window CURRENT_WINDOW;

    public static double lastFrameTime = 0.0, delta = 0.0;

    private Matrix4f inverseProjectionMatrix;


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

        ImguiInterface.initImGui(this.windowID);

        this.inverseProjectionMatrix = new Matrix4f().identity();
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

            glfwSetWindowSizeCallback(this.windowID, (window, width, height) ->
            {
                WINDOW_WIDTH = width;
                WINDOW_HEIGHT = height;
                Renderer.updateFramebuffer();
                glViewport(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
            });

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

    public void update()
    {
        startFrame();
        ImguiInterface.render();
        glfwSwapBuffers(this.windowID);
        glfwPollEvents();
        MouseListener.endFrame();
    }

    private void startFrame()
    {
        double currentFrameTime = glfwGetTime();
        delta = (lastFrameTime > 0) ? (currentFrameTime - lastFrameTime) : (1f / 60);
        lastFrameTime = currentFrameTime;
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
        ImguiInterface.cleanUp();
        glfwFreeCallbacks(this.windowID);
        glfwDestroyWindow(this.windowID);
    }

    public Matrix4f getInverseProjectionMatrix()
    {
        return this.inverseProjectionMatrix;
    }

    public static Matrix4f getProjectionMatrix(Camera camera)
    {
        Matrix4f projectionMatrix = new Matrix4f().identity();
        projectionMatrix.perspective(camera.getFieldOfView(), (float) (Window.WINDOW_WIDTH / Window.WINDOW_HEIGHT),
                camera.getNearPlane(), camera.getFarPlane());
        projectionMatrix.invert(CURRENT_WINDOW.inverseProjectionMatrix);
        return projectionMatrix;
    }
}
