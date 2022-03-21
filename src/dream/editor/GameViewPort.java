package dream.editor;

import dream.io.input.MouseListener;
import dream.io.output.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewport
{
    private float leftX, rightX, topY, bottomY;

    public GameViewport()
    {

    }

    public void renderViewPort(int framebufferTextureID)
    {
        int windowFlags = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoTitleBar;
        ImGui.begin("Game ViewPort", windowFlags);

        ImVec2 windowSize = getLargestSizeForViewPort();
        ImVec2 windowPosition = getCenteredPositionForViewPort(windowSize);
        ImGui.setCursorPos(windowPosition.x, windowPosition.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        ImGui.image(framebufferTextureID, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private ImVec2 getCenteredPositionForViewPort(ImVec2 aspectSize)
    {
        ImVec2 windowSize = setup();
        float viewportX = (windowSize.x * 0.5f) - (aspectSize.x * 0.5f);
        float viewportY = (windowSize.y * 0.5f) - (aspectSize.y * 0.5f);
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    private ImVec2 getLargestSizeForViewPort()
    {
        ImVec2 windowSize = setup();
        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if(aspectHeight > windowSize.y)
        {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 setup()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    public boolean getWantCaptureMouse()
    {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }
}
