package dream.scenes;

import java.util.ArrayList;
import java.util.List;

public class SceneManager
{
    private static final List<Scene> SCENES = new ArrayList<>();
    private static Scene CURRENT_SCENE = null;

    public static void addScene(Scene scene, boolean currentScene)
    {
        SCENES.add(scene);
        if(currentScene)
        {
            CURRENT_SCENE = scene;
            scene.start();
        }
    }

    public static void removeScene(Scene scene)
    {
        if(SCENES.contains(scene))
        {
            scene.destroy();
        }
    }

    public static List<Scene> getScenes()
    {
        return SCENES;
    }

    public static Scene getCurrentScene()
    {
        if(CURRENT_SCENE == null)
            return SCENES.get(0);
        return CURRENT_SCENE;
    }
}
