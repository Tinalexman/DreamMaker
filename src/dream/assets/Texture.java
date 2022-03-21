package dream.assets;

import dream.DreamMaker;
import dream.ecs.components.Component;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture extends Component
{
    private String filePath;
    private int textureID;

    public Texture()
    {

    }

    public Texture(String filePath)
    {
        setFilePath(filePath);
    }

    @Override
    public void onStart()
    {
        if(!this.ready)
        {
            if(this.filePath != null)
            {
                loadTexture();
                this.ready = true;
            }
            else
                throw new RuntimeException("No Texture FilePath was provided!");
        }
    }

    @Override
    public void onDestroyRequested()
    {
        glDeleteTextures(this.textureID);
    }

    @Override
    public boolean isDrawable()
    {
        return false;
    }

    public String getFilePath()
    {
        return this.filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public int getTextureID()
    {
        return this.textureID;
    }

    public Texture(int textureWidth, int textureHeight)
    {
        this.textureID = glGenTextures();
        this.filePath = "Generated for FrameBuffer";

        glBindTexture(GL_TEXTURE_2D, this.textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textureWidth, textureHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        this.ready = true;
    }

    public void loadTexture()
    {
        int width, height;
        ByteBuffer buffer;

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            File file = new File(filePath);
            String filePath = file.getAbsolutePath();

            STBImage.stbi_set_flip_vertically_on_load(true);
            buffer = STBImage.stbi_load(filePath, w, h, channels, 4);
            if(buffer == null)
                throw new Exception("Cannot load texture file: " + filePath + " due to " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

            this.textureID = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, this.textureID);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(buffer);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
