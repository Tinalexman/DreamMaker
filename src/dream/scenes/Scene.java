package dream.scenes;

import dream.ecs.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene
{
    protected final Map<String, Entity> sceneObjects;
    protected boolean isRunning;

    public Scene()
    {
        this.sceneObjects = new HashMap<>();
        isRunning = false;
    }

    public List<Entity> getSceneObjects()
    {
        return new ArrayList<>(this.sceneObjects.values());
    }

    public void addSceneObject(Entity entity)
    {
        this.sceneObjects.put(entity.getName(), entity);
        if(isRunning)
            entity.start();
    }

    public void removeSceneObject(Entity entity)
    {
        entity.removeAllComponents();
        this.sceneObjects.remove(entity.getName());
    }

    public void startScene()
    {
        for(Entity entity : sceneObjects.values())
            entity.start();
    }

    public void stopScene()
    {
        this.isRunning = false;
    }
}
