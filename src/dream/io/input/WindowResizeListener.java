package dream.io.input;

import dream.editor.Editor;
import dream.graphics.renderer.Renderer;
import dream.io.output.Window;

import static org.lwjgl.opengl.GL11.glViewport;

public class WindowResizeListener
{
    public static void onResize(long windowID, int newWidth, int newHeight)
    {
        Window.setWindowWidth(newWidth);
        Window.setWindowHeight(newHeight);

        if(Editor.updateBuffers())
            Window.updateWindowSize();

        glViewport(0, 0, newWidth, newHeight);
    }
}
