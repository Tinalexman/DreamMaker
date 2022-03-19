package dream.editor;

import dream.io.output.Window;
import dream.graphics.renderer.Renderer;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewPort
{
    public static void renderViewPort()
    {
        int windowFlags = ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoTitleBar;
        ImGui.begin("Game ViewPort", windowFlags);

        ImVec2 windowSize = getLargestSizeForViewPort();
        ImVec2 windowPosition = getCenteredPositionForViewPort(windowSize);

        ImGui.setCursorPos(windowPosition.x, windowPosition.y);
        int textureID = Renderer.getCurrentFrameBufferTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private static ImVec2 getCenteredPositionForViewPort(ImVec2 aspectSize)
    {
        ImVec2 windowSize = setup();
        float viewportX = (windowSize.x * 0.5f) - (aspectSize.x * 0.5f);
        float viewportY = (windowSize.y * 0.5f) - (aspectSize.y * 0.5f);
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    private static ImVec2 getLargestSizeForViewPort()
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

    private static ImVec2 setup()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }
}
