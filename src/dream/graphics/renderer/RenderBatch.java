package dream.graphics.renderer;

import dream.assets.Shader;
import dream.ecs.entities.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderBatch
{
    private static Map<Shader, List<Entity>> renderBatches = new HashMap<>();


    public static void createBatch()
    {

    }

     public static List<Entity> getNextBatch()
     {
         return null;
     }

     public static Shader getNextShader()
     {
         return null;
     }

}
