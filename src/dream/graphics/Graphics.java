package dream.graphics;

import dream.ecs.components.Shape;
import dream.ecs.entities.Entity;
import dream.ecs.entities.EntityManager;
import dream.editor.Editor;
import dream.io.output.Window;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Graphics implements Runnable
{
    private Window window;
    private Editor editor;

    private static Graphics engine;
    public static volatile boolean isRunning = true;

    public static void startEngine()
    {
        if(engine == null)
            engine = new Graphics();
    }

    private Graphics()
    {
        new Thread(this, "Graphics Thread").start();
    }

    @Override
    public void run()
    {
        init();
        dreamLoop();
    }

    private void init()
    {
        this.window = Window.getDreamWindow();
        this.window.showWindow();

        this.editor = Editor.getEditor();
        this.editor.initializeEditor(this.window.getWindowId());

        Entity sun = EntityManager.createEntity("Sun");
        sun.addComponent(new Shape());

        this.editor.addObjectToScene(sun);
        this.editor.startScene();
//            DreamCube mercury = EntityManager.createNewCube();
//            mercury.attachShader(new Shader("shaders//entity.vert", "shaders//entity.frag"));
//            mercury.setPosition(new Vector3f(0.0f, 1.5f, 0.0f));
//            mercury.setScale(new Vector3f(0.15f));
//            mercury.load();
//            Shader mercuryShader = mercury.getShader();
//            mercuryShader.startProgram();
//            mercuryShader.loadUniform("viewPosition", this.renderer.getCamera().getPosition());
//            mercuryShader.loadUniform("isTextured", false);
//            mercury.applyMaterial(Materials.CARAMEL);
//            mercury.applyLighting(Lights.WHITE_LIGHT);
//            mercuryShader.stopProgram();

    }

    private void dreamLoop()
    {
        if(!isRunning)
            return;

        while(!glfwWindowShouldClose(this.window.getWindowId()))
            updateState();

        cleanUp();
        isRunning = false;
    }

    private void showSplash()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        glfwSwapBuffers(this.window.getWindowId());
        glfwPollEvents();
    }

    private void updateState()
    {
        this.window.startFrame();
        this.editor.drawEditor();
        this.window.endFrame();
    }

//    private void render()
//    {
//        List<DreamModel> models = EntityManager.getEntities();
//
//        DreamModel mercury = models.get(1);
//        float mercuryX = (float) (Math.sin(glfwGetTime() * 0.85) * 1.5f);
//        float mercuryY = (float) (Math.cos(glfwGetTime() * 0.85) * 1.5f);
//        mercury.setTransformationMatrix(new Matrix4f().identity()
//                .translate(new Vector3f(mercuryX, mercuryY, mercury.getPosition().z))
//                .rotate((float) (Math.toRadians(glfwGetTime()) * 80f), new Vector3f(0.0f, 1.0f, 0.0f))
//                .scale(mercury.getScale()));
//
//        this.renderer.render(models);
//    }

    private void cleanUp()
    {
        this.window.cleanUp();
        this.editor.cleanUp();
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

}
