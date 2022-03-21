package dream.editor;

import dream.ecs.entities.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneGraphWindow
{

    public void drawImGui(List<Entity> entities)
    {
        ImGui.begin("Scene Graph");
        int index = 0;
        for(Entity entity : entities)
        {
            if(!entity.isSerializable())
                continue;

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(entity.getName(),
                    ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding
                            |   ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth, entity.getName());

            ImGui.popID();

            if(treeNodeOpen)
                ImGui.treePop();

            ++index;
        }
        ImGui.end();
    }
}
