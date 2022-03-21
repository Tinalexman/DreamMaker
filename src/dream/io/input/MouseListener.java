package dream.io.input;

import dream.editor.Editor;
import dream.io.output.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener
{
    private static MouseListener instance;
    private float scrollX, scrollY;
    private float currentXPosition, currentYPosition, previousXPosition, previousYPosition;
    private final boolean[] mouseButtons;
    private boolean dragging;

    private MouseListener()
    {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.currentXPosition = 0.0f;
        this.currentYPosition = 0.0f;
        this.previousXPosition = 0.0f;
        this.previousYPosition = 0.0f;

        this.mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    }

    public static MouseListener getListener()
    {
        if(instance == null)
            instance = new MouseListener();
        return instance;
    }

    public static void mousePositionCallback(long windowID, double xPosition, double yPosition)
    {
        getListener().previousXPosition = getListener().currentXPosition;
        getListener().previousYPosition = getListener().currentYPosition;

        getListener().currentXPosition = (float) xPosition;
        getListener().currentYPosition = (float) yPosition;

        getListener().dragging = getListener().mouseButtons[0] || getListener().mouseButtons[1] || getListener().mouseButtons[2];
    }

    public static void mouseButtonCallback(long windowID, int button, int action, int mods)
    {
        if(action == GLFW_PRESS)
            getListener().mouseButtons[button] = true;
        else if(action == GLFW_RELEASE)
        {
            getListener().mouseButtons[button] = false;
            getListener().dragging = false;
        }
    }

    public static void mouseScrollCallback(long windowID, double xOffset, double yOffset)
    {
        getListener().scrollX = (float) xOffset;
        getListener().scrollY = (float) yOffset;
    }

    public static void endFrame()
    {
        getListener().scrollX = 0.0f;
        getListener().scrollY = 0.0f;
        getListener().previousXPosition = getListener().currentXPosition;
        getListener().previousYPosition = getListener().currentYPosition;
    }

    public static float getX()
    {
        return getListener().currentXPosition;
    }

    public static float getY()
    {
        return getListener().currentYPosition;
    }

    public static float getDX()
    {
        return getListener().currentXPosition - getListener().previousXPosition;
    }

    public static float getDY()
    {
        return getListener().currentYPosition - getListener().previousYPosition;
    }

    public static float getScrollX()
    {
        return getListener().scrollX;
    }

    public static float getScrollY()
    {
        return getListener().scrollY;
    }

    public static boolean isDragging()
    {
        return getListener().dragging;
    }

    public static boolean isMouseButtonDown(int button)
    {
        return getListener().mouseButtons[button];
    }

    public static float getOrthoX()
    {
        float currentX = getX();
        currentX = ((currentX / (float) Window.getWindowWidth()) * 2.0f) - 1.0f;
        Vector4f tempVector = new Vector4f(currentX, 0, 0, 1);
        tempVector.mul(Editor.getEditor().getCamera().getInverseProjectionMatrix())
                .mul(Editor.getEditor().getCamera().getInverseViewMatrix());
        currentX = tempVector.x;
        return currentX;
    }

    public static float getOrthoY()
    {
        float currentY = getY();
        currentY = ((currentY / (float) Window.getWindowWidth()) * 2.0f) - 1.0f;
        Vector4f tempVector = new Vector4f(0, currentY, 0, 1);
        tempVector.mul(Editor.getEditor().getCamera().getInverseProjectionMatrix())
                .mul(Editor.getEditor().getCamera().getInverseViewMatrix());
        currentY = tempVector.y;
        return currentY;
    }

    public static float getScreenX()
    {
        float currentX = getX();
        currentX = ((currentX / (float) Window.getWindowWidth()) * 2.0f);
        return currentX;
    }

    public static float getScreenY()
    {
        float currentY = getY();
        currentY = ((currentY / (float) Window.getWindowHeight()) * 2.0f) - 1.0f;
        Vector4f tempVector = new Vector4f(0, currentY, 0, 1);
        tempVector.mul(Editor.getEditor().getCamera().getInverseProjectionMatrix())
                .mul(Editor.getEditor().getCamera().getInverseViewMatrix());
        currentY = tempVector.y;
        return currentY;
    }

    public static void setGameViewportPos(Vector2f vector2f)
    {

    }

    public static void setGameViewportSize(Vector2f vector2f)
    {

    }
}
