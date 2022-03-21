package dream.editor;

import imgui.ImGui;

public class ResourcesWindow
{

    public void drawImGui()
    {
        ImGui.begin("Resources");
        ImGui.text("Some Text");
        ImGui.end();
    }
}
