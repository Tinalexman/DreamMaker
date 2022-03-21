package dream.toolbox;

import dream.assets.Shader;
import dream.assets.Texture;
import dream.audio.Sound;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AssetPool
{

    private static Map<String, Shader> SHADERS = new HashMap<>();
    private static Map<String, Texture> TEXTURES = new HashMap<>();
    private static Map<String, Sound> SOUNDS = new HashMap<>();

    static
    {
        AssetPool.SHADERS.put("src/dream/res/shaders/defaultShader.glsl", new Shader());
    }

    public static Shader getDefaultShader()
    {
        return AssetPool.SHADERS.get("src/dream/res/shaders/defaultShader.glsl");
    }

    public static Shader getShader(String resourceNameFromSrc)
    {
        File shaderFile = new File(resourceNameFromSrc);
        return AssetPool.SHADERS.getOrDefault(shaderFile.getAbsolutePath(), null);
    }

    public static void addShader(String resourceNameFromSrc)
    {
        File shaderFile = new File(resourceNameFromSrc);
        if(AssetPool.SHADERS.containsKey(shaderFile.getAbsolutePath()))
            return;

        Shader shader = new Shader(resourceNameFromSrc);
        AssetPool.SHADERS.put(shaderFile.getAbsolutePath(), shader);
    }

    public static Texture getTexture(String resourceNameFromSrc)
    {
        File textureFile = new File(resourceNameFromSrc);
        return AssetPool.TEXTURES.getOrDefault(textureFile.getAbsolutePath(), null);
    }

    public static void addTexture(String resourceNameFromSrc)
    {
        File textureFile = new File(resourceNameFromSrc);
        if(AssetPool.TEXTURES.containsKey(textureFile.getAbsolutePath()))
            return;

        Texture texture = new Texture(resourceNameFromSrc);
        AssetPool.TEXTURES.put(textureFile.getAbsolutePath(), texture);
    }

    public static Sound getSound(String resourceNameFromSrc)
    {
        File soundFile = new File(resourceNameFromSrc);
        return AssetPool.SOUNDS.getOrDefault(soundFile.getAbsolutePath(), null);
    }

    public static void addSound(String resourceNameFromSrc)
    {
        File soundFile = new File(resourceNameFromSrc);
        if(AssetPool.SOUNDS.containsKey(soundFile.getAbsolutePath()))
            return;

        Sound sound = new Sound(resourceNameFromSrc);
        AssetPool.SOUNDS.put(soundFile.getAbsolutePath(), sound);
    }

    public static Shader addAndGetShader(String resourceNameFromSrc)
    {
        Shader shader = getShader(resourceNameFromSrc);
        if(shader == null)
        {
            shader = new Shader(resourceNameFromSrc);
            AssetPool.SHADERS.put(new File(resourceNameFromSrc).getAbsolutePath(), shader);
        }
        return shader;
    }

    public static Texture addAndGetTexture(String resourceNameFromSrc)
    {
        Texture texture = getTexture(resourceNameFromSrc);
        if(texture == null)
        {
            texture = new Texture(resourceNameFromSrc);
            AssetPool.TEXTURES.put(new File(resourceNameFromSrc).getAbsolutePath(), texture);
        }
        return texture;
    }

    public static Sound addAndGetSound(String resourceNameFromSrc)
    {
        Sound sound = getSound(resourceNameFromSrc);
        if(sound == null)
        {
            sound = new Sound(resourceNameFromSrc);
            AssetPool.SOUNDS.put(new File(resourceNameFromSrc).getAbsolutePath(), sound);
        }
        return sound;
    }

    public static ArrayList<Sound> getAllSounds()
    {
        return new ArrayList<>(AssetPool.SOUNDS.values());
    }

    public static ArrayList<Texture> getAllTextures()
    {
        return new ArrayList<>(AssetPool.TEXTURES.values());
    }

    public static ArrayList<Shader> getAllShaders()
    {
        return new ArrayList<>(AssetPool.SHADERS.values());
    }
}
