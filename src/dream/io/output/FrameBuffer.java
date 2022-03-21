package dream.io.output;

import dream.assets.Texture;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    private final int FBO_ID;
    private final int RBO_ID;
    private Texture texture;

    private FrameBuffer(int frameWidth, int frameHeight)
    {
        this.FBO_ID = glGenFramebuffers();
        bindFrameBuffer();

        this.texture = new Texture(frameWidth, frameHeight);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTextureID(), 0);

        this.RBO_ID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, RBO_ID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, frameWidth, frameHeight);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, RBO_ID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("FrameBuffer is not complete!");

        unbindFrameBuffer();
    }

    public void bindFrameBuffer()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, this.FBO_ID);
    }

    public void unbindFrameBuffer()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID()
    {
        return this.FBO_ID;
    }

    public int getTextureID()
    {
        return this.texture.getTextureID();
    }

    public void destroy()
    {
        glDeleteRenderbuffers(this.RBO_ID);
        glDeleteFramebuffers(this.FBO_ID);
    }

    public static FrameBuffer createFrameBuffer()
    {
        return new FrameBuffer(Window.getWindowWidth(), Window.getWindowHeight());
    }
}
