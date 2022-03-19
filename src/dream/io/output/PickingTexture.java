package dream.io.output;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.*;

public class PickingTexture
{
    private int pickingTextureID;
    private int FBO_ID;
    private int depthTextureID;

    public PickingTexture(int textureWidth, int textureHeight)
    {
        if(!init(textureWidth, textureHeight))
            throw new RuntimeException("Picking Texture could not be created");
    }

    private boolean init(int width, int height)
    {
        this.FBO_ID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.FBO_ID);

        this.pickingTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.pickingTextureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.pickingTextureID, 0);

        glEnable(GL_TEXTURE_2D);
        this.depthTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.depthTextureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depthTextureID, 0);

        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("FrameBuffer is not complete!");


        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        return true;
    }

    public void enableWriting()
    {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.FBO_ID);
    }

    public void disableWriting()
    {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int xCoordinate, int yCoordinate)
    {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, this.FBO_ID);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        glReadPixels(xCoordinate, yCoordinate, 1, 1, GL_RGB, GL_FLOAT, pixels);

        return (int) pixels[0];
    }

    public void destroy()
    {
        glDeleteTextures(this.pickingTextureID);
        glDeleteTextures(this.depthTextureID);
        glDeleteFramebuffers(this.FBO_ID);
    }
}
