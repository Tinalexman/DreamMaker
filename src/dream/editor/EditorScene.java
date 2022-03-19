package dream.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dream.ecs.components.Component;
import dream.ecs.entities.Entity;
import dream.scenes.Scene;
import dream.toolbox.serializer.ComponentDeserializer;

import java.util.ArrayList;
import java.util.List;

public class EditorScene extends Scene
{
    private Gson gson;

    public EditorScene()
    {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .create();
    }

    @Override
    public void drawImGui()
    {
        drawSceneGraph();
        drawSceneInspector();
        //drawResources();
        //drawAssetPool();
    }

    private void drawAssetPool()
    {
        AssetsWindow.drawImGui();
    }

    private void drawResources()
    {
        ResourcesWindow.drawImGui();
    }

    public void serialize(Entity entity)
    {
        String serialized = gson.toJson(entity);
        System.out.println(serialized);
    }

    private void drawSceneGraph()
    {
        List<Entity> entities = new ArrayList<>(this.sceneObjects.values());
        SceneGraphWindow.setEntities(entities);
        SceneGraphWindow.drawImGui();
    }

    private void drawSceneInspector()
    {
        InspectorWindow.setSelectedEntity(this.selectedEntity);
        InspectorWindow.drawImGui();
    }

}
