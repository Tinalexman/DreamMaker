package dream.toolbox;

import dream.ecs.components.Shader;
import dream.ecs.components.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class AssetPool
{

    private static Map<String, Shader> SHADERS = new HashMap<>();
    private static Map<String, Texture> TEXTURES = new HashMap<>();

    static
    {
        SHADERS.put("src/dream/res/shaders/defaultShader.glsl", new Shader());
    }

    public static Shader getDefaultShader()
    {
        return SHADERS.get("src/dream/res/shaders/defaultShader.glsl");
    }

    public static Shader getShader(String resourceNameFromSrc)
    {
        File shaderFile = new File(resourceNameFromSrc);
        if(SHADERS.containsKey(shaderFile.getAbsolutePath()))
            return SHADERS.get(shaderFile.getAbsolutePath());
        else
        {
            Shader shader = new Shader(resourceNameFromSrc);
            SHADERS.put(shaderFile.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceNameFromSrc)
    {
        File textureFile = new File(resourceNameFromSrc);
        if(TEXTURES.containsKey(textureFile.getAbsolutePath()))
            return TEXTURES.get(textureFile.getAbsolutePath());
        else
        {
            Texture texture = new Texture(resourceNameFromSrc);
            TEXTURES.put(textureFile.getAbsolutePath(), texture);
            return texture;
        }
    }

}
