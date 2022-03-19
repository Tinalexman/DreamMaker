package dream.editor;

import dream.ecs.entities.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class InspectorWindow
{
    private static float DEFAULT_COLUMN_WIDTH = 80.0f;
    private static Entity selectedEntity;

    public static void setSelectedEntity(Entity entity)
    {
        selectedEntity = entity;
    }

    public static void drawImGui()
    {
        ImGui.begin("Inspector");

        if(selectedEntity == null)
            ImGui.text("NO SELECTED ENTITY");
        else
            selectedEntity.drawImGui();
        ImGui.end();
    }

    public static boolean drawVector2Control(String label, Vector2f values, float resetValue, float columnWidth)
    {
        boolean res = false;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);

        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) * 0.5f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.15f, 0.15f, 1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.8f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.9f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.8f, 0.15f, 1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
            res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);

        ImGui.popItemWidth();
        ImGui.sameLine();
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

    public static boolean drawVector2Control(String label, Vector2f values, float resetValue)
    {
        return drawVector2Control(label, values, resetValue, DEFAULT_COLUMN_WIDTH);
    }

    public static boolean drawVector2Control(String label, Vector2f values)
    {
        return drawVector2Control(label, values, 0.0f, DEFAULT_COLUMN_WIDTH);
    }

    public static void drawVector3Control(String label, Vector3f values, float resetValue, float columnWidth)
    {
//        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);

        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) * 0.5f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.15f, 0.15f, 1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
            //res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.8f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.9f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.8f, 0.15f, 1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
            //res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.15f, 0.15f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.2f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.15f, 0.8f, 1.0f);
        if(ImGui.button("Z", buttonSize.x, buttonSize.y))
        {
            values.z = resetValue;
            //res = true;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {values.z};
        ImGui.dragFloat("##z", vecValuesZ, 0.1f);

        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        values.x = vecValuesX[0];
        values.y = vecValuesY[0];
        values.z = vecValuesZ[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();

        //return res;
    }

    public static void drawVector3Control(String label, Vector3f values, float resetValue)
    {
        drawVector3Control(label, values, resetValue, DEFAULT_COLUMN_WIDTH);
    }

    public static void drawVector3Control(String label, Vector3f values)
    {
        drawVector3Control(label, values, 0.0f, DEFAULT_COLUMN_WIDTH);
    }

    public static int drawIntControl(String label, int value)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valueArr = {value};
        ImGui.dragInt("##dragInt", valueArr, 1);

        ImGui.columns(1);
        ImGui.popID();

        return valueArr[0];
    }

    public static float drawFloatControl(String label, float value)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valueArr = {value};
        ImGui.dragFloat("##dragFloat", valueArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valueArr[0];
    }

    public static boolean drawBooleanControl(String label, boolean value)
    {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.checkbox("##dragBoolean", value);
        value = !value;

        ImGui.columns(1);
        ImGui.popID();

        return value;
    }

    public static boolean drawColorPicker4(String label, Vector4f color)
    {
        boolean res = false;

        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
        ImGui.text(label);
        ImGui.nextColumn();


        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("Color Picker", imColor))
        {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }



}
