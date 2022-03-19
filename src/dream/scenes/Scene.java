package dream.scenes;

import dream.ecs.entities.Entity;
import dream.graphics.renderer.Renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene
{
    protected final Map<String, Entity> sceneObjects;
    protected static Renderer currentSceneRenderer;
    protected boolean isRunning;
    protected Entity selectedEntity;

    public Scene()
    {
        this.sceneObjects = new HashMap<>();
        isRunning = false;
    }

    public static Renderer getCurrentSceneRenderer()
    {
        return currentSceneRenderer;
    }

    public static void setCurrentSceneRenderer(Renderer renderer)
    {
        currentSceneRenderer = renderer;
    }

    public Map<String, Entity> getSceneObjects()
    {
        return this.sceneObjects;
    }

    public void addSceneObject(Entity entity)
    {
        this.sceneObjects.put(entity.getName(), entity);
        if(isRunning)
            entity.start();
    }

    public void destroy()
    {

    }

    public void removeSceneObject(Entity entity)
    {
        entity.removeAllComponents();
    }

    public void renderGameObjects()
    {
        List<Entity> entities = new ArrayList<>(sceneObjects.values());
        currentSceneRenderer.render(entities);
    }

    public void drawImGui()
    {

    }

    public void start()
    {
        for(Entity entity : sceneObjects.values())
            entity.start();
    }

    public void setSelectedEntity(Entity entity)
    {
        this.selectedEntity = entity;
    }

}
