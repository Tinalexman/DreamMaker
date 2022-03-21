package dream.graphics.renderer;

import dream.assets.Shader;
import dream.assets.Texture;
import dream.camera.Camera;
import dream.ecs.components.*;
import dream.ecs.entities.Entity;
import dream.ecs.entities.EntityManager;
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
    public static final int WIREFRAME = GL_LINE;
    public static final int SOLID = GL_FILL;

    public static final int FRONT_FACE = GL_FRONT;
    public static final int BACK_FACE = GL_BACK;
    public static final int FRONT_N_BACK_FACE = GL_FRONT_AND_BACK;


    public Renderer()
    {

    }

    public void setModes(int faceMode, int drawMode)
    {
        if(faceMode != FRONT_FACE && faceMode != BACK_FACE && faceMode != FRONT_N_BACK_FACE)
            return;

        if(drawMode != WIREFRAME && drawMode != SOLID)
            return;

        glPolygonMode(faceMode, drawMode);
    }

    public void prepareRenderer(FrameBuffer frameBuffer)
    {
        frameBuffer.bindFrameBuffer();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        frameBuffer.unbindFrameBuffer();
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
//                Matrix3f inversed = Loader.calculateInverseProjection(matrix4f);
//                shader.loadUniform("inversedMatrix", inversed);
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
        {
            Color color = object.getComponent(Color.class);
            if(color.hasChanged())
            {
                shader.loadUniform("color", color.getRGB());
                color.resetChange();
            }
        }
        else
        {
            if(material.hasChanged())
            {
                shader.applyMaterial("material", material);
                material.resetChange();
            }
        }
    }

    private void loadViewAndProjection(Camera camera, Shader shader, boolean isPickingShaderPass) throws Exception
    {
        if(!camera.hasChanged())
            return;

        shader.loadUniform("viewMatrix", camera.getViewMatrix());
        shader.loadUniform("projectionMatrix", camera.getProjectionMatrix());

        if(!isPickingShaderPass)
            camera.resetChange();
    }


    private void loadUniforms(Camera camera, Shader shader, Entity object, boolean isPickingShaderPass) throws Exception
    {
        loadEntityTransform(object, shader, isPickingShaderPass);
        loadViewAndProjection(camera, shader, isPickingShaderPass);
        loadEntityMaterial(object, shader, isPickingShaderPass);
    }

    public boolean startPickingTexturePass(PickingTexture pickingTexture)
    {
        boolean blendWasEnabled = glIsEnabled(GL_BLEND);
        if(blendWasEnabled)
            glDisable(GL_BLEND);

        pickingTexture.enableWriting();

        glViewport(0, 0, Window.getWindowWidth(), Window.getWindowHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        return blendWasEnabled;
    }


    public void endPickingTexturePass(PickingTexture pickingTexture, boolean blended)
    {
        pickingTexture.disableWriting();

        if(blended)
            glEnable(GL_BLEND);
    }

    public void renderEntities(Camera camera, FrameBuffer frameBuffer, Shader pickingShader,
                                List<Entity> dreamObjects, boolean isPickingTexturePass)
    {
        glEnable(GL_DEPTH_TEST);

        if(!isPickingTexturePass)
            frameBuffer.bindFrameBuffer();

        Shader shader = (isPickingTexturePass) ? pickingShader : EntityManager.getEntityShader();

        for(Entity dreamObject : dreamObjects)
        {
            shader.startProgram();

            try
            {
                loadUniforms(camera, shader, dreamObject, isPickingTexturePass);

                if(isPickingTexturePass)
                    shader.loadUniform("fEntityId", (float) (dreamObject.getID() + 1));

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
            frameBuffer.unbindFrameBuffer();

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

}
