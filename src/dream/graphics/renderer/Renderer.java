package dream.graphics.renderer;

import dream.ecs.components.*;
import dream.ecs.entities.Entity;
import dream.editor.EditorCamera;
import dream.io.output.FrameBuffer;
import dream.io.output.PickingTexture;
import dream.io.output.Window;
import dream.toolbox.Loader;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Renderer
{

    public static Renderer RENDERER;

    private final EditorCamera camera;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;
    private Shader pickingShader;

    public static final int WIREFRAME = GL_LINE;
    public static final int SOLID = GL_FILL;

    public static final int FRONT_FACE = GL_FRONT;
    public static final int BACK_FACE = GL_BACK;
    public static final int FRONT_N_BACK_FACE = GL_FRONT_AND_BACK;


    public Renderer()
    {
        this.camera = new EditorCamera();
        this.camera.requestActivation();

        this.frameBuffer = FrameBuffer.createFrameBuffer(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        this.pickingTexture = new PickingTexture(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);

        RENDERER = this;
    }

    public EditorCamera getCamera()
    {
        return this.camera;
    }

    public static int getCurrentFrameBufferTextureID()
    {
        return RENDERER.frameBuffer.getTextureID();
    }

    public static void updateFramebuffer()
    {
        if(!(Window.PREV_WINDOW_WIDTH >= Window.WINDOW_WIDTH || Window.PREV_WINDOW_HEIGHT >= Window.WINDOW_HEIGHT))
        {
            if(RENDERER.frameBuffer != null)
                RENDERER.frameBuffer.destroy();

            if(RENDERER.pickingTexture != null)
                RENDERER.pickingTexture.destroy();

            RENDERER.frameBuffer = FrameBuffer.createFrameBuffer(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
            RENDERER.pickingTexture = new PickingTexture(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
            Window.PREV_WINDOW_WIDTH = Window.WINDOW_WIDTH;
            Window.PREV_WINDOW_HEIGHT = Window.WINDOW_HEIGHT;
        }
    }

    public void setModes(int faceMode, int drawMode)
    {
        if(faceMode != FRONT_FACE && faceMode != BACK_FACE && faceMode != FRONT_N_BACK_FACE)
            return;

        if(drawMode != WIREFRAME && drawMode != SOLID)
            return;

        glPolygonMode(faceMode, drawMode);
    }

    private void prepareRenderer()
    {
        this.frameBuffer.bindFrameBuffer();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.frameBuffer.unbindFrameBuffer();
    }

    public void bindPickingShader(Shader shader)
    {
        this.pickingShader = shader;
    }

    private void loadEntityTransform(Entity object, Shader shader, boolean isPickingShaderPass) throws Exception
    {
        Transform transform = object.getComponent(Transform.class);
        if(transform == null)
            return;
        if(transform.hasChanged())
        {
            Matrix4f matrix4f = transform.getTransformationMatrix();
            shader.loadUniform("transformationMatrix", matrix4f);
            if(!isPickingShaderPass)
            {
                Matrix3f inversed = Loader.calculateInverseProjection(matrix4f);
                shader.loadUniform("inversedMatrix", inversed);
                transform.resetChange();
            }
        }
    }

    private void loadEntityMaterial(Entity object, Shader shader, boolean isPickingShaderPass) throws Exception
    {
        if(isPickingShaderPass)
            return;

        Material material = object.getComponent(Material.class);
        if(material == null)
            return;
        if(material.hasChanged())
        {
            shader.applyMaterial("material", material);
            material.resetChange();
        }
    }

    private void loadViewAndProjection(Shader shader, boolean isPickingShaderPass) throws Exception
    {
        if(!camera.hasChanged())
            return;

        shader.loadUniform("viewMatrix", this.camera.getViewMatrix());
        shader.loadUniform("projectionMatrix", Window.getProjectionMatrix(this.camera));

        if(!isPickingShaderPass)
            camera.resetChange();
    }


    private void loadUniforms(Shader shader, Entity object, boolean isPickingShaderPass) throws Exception
    {
        loadEntityTransform(object, shader, isPickingShaderPass);
        loadViewAndProjection(shader, isPickingShaderPass);
        loadEntityMaterial(object, shader, isPickingShaderPass);
    }

    public void render(List<Entity> dreamObjects)
    {
        this.camera.update();
        prepareRenderer();
        pickingTextureRenderPass(dreamObjects);
        renderEntities(dreamObjects, false);
    }

    private void pickingTextureRenderPass(List<Entity> dreamObjects)
    {
        boolean blendWasEnabled = glIsEnabled(GL_BLEND);
        if(blendWasEnabled)
            glDisable(GL_BLEND);

        this.pickingTexture.enableWriting();

        glViewport(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderEntities(dreamObjects, true);

//        if(MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
//        {
//            int x = (int) MouseListener.getScreenX();
//            int y = (int) MouseListener.getScreenY();
//            System.out.println(pickingTexture.readPixel(x, y));
//        }

        this.pickingTexture.disableWriting();

        if(blendWasEnabled)
            glEnable(GL_BLEND);
    }

    private void renderEntities(List<Entity> dreamObjects, boolean isPickingTexturePass)
    {
        glEnable(GL_DEPTH_TEST);

        if(!isPickingTexturePass)
            this.frameBuffer.bindFrameBuffer();

        for(Entity dreamObject : dreamObjects)
        {
            Shader shader;
            if(!isPickingTexturePass)
            {
                shader = dreamObject.getComponent(Shader.class);
                if(shader == null)
                    continue;
            }
            else
                shader = this.pickingShader;

            shader.startProgram();

            try
            {
                loadUniforms(shader, dreamObject, isPickingTexturePass);

                if(isPickingTexturePass)
                    shader.loadUniform("fEntityId", (float) dreamObject.getID());

                Mesh mesh = dreamObject.getComponent(Mesh.class);
                if(mesh == null)
                    continue;
                mesh.enable();

                prepareTexture(dreamObject);

                if(mesh.indices != null)
                    glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                else
                    glDrawArrays(GL_TRIANGLES, 0, mesh.getVertexCount());

                mesh.disable();

                shader.stopProgram();
            }
            catch (Exception ex)
            {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }

        if(!isPickingTexturePass)
            this.frameBuffer.unbindFrameBuffer();

        glDisable(GL_DEPTH_TEST);
    }

    private void prepareTexture(Entity entity)
    {
        Texture texture = entity.getComponent(Texture.class);
        if(texture != null)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        }
    }

    public void cleanUp()
    {
        this.frameBuffer.destroy();
        this.pickingTexture.destroy();
        this.pickingShader.onDestroyRequested();
    }
}
