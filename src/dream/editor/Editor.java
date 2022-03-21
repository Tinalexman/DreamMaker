package dream.editor;


import dream.assets.Shader;
import dream.camera.Camera;
import dream.ecs.entities.Entity;
import dream.ecs.entities.EntityManager;
import dream.graphics.renderer.Renderer;
import dream.io.input.MouseListener;
import dream.io.output.FrameBuffer;
import dream.io.output.PickingTexture;
import dream.io.output.Window;
import dream.scenes.Scene;
import dream.toolbox.AssetPool;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Editor
{
    private static Editor EDITOR;


    private final ImguiInterface imguiInterface;

    private final AssetsWindow assetsWindow;
    private final ResourcesWindow resourcesWindow;
    private final InspectorWindow inspectorWindow;
    private final SceneGraphWindow sceneGraphWindow;
    private final GameViewport gameViewport;

    private final EditorCamera editorCamera;
    private final Renderer renderer;
    private final Scene scene;

    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;
    private Shader pickingShader;


    private Editor()
    {
        this.imguiInterface = new ImguiInterface();

        this.assetsWindow = new AssetsWindow();
        this.resourcesWindow = new ResourcesWindow();
        this.inspectorWindow = new InspectorWindow();
        this.sceneGraphWindow = new SceneGraphWindow();
        this.gameViewport = new GameViewport();

        this.editorCamera = new EditorCamera();
        this.renderer = new Renderer();
        this.scene = new Scene();

        this.frameBuffer = FrameBuffer.createFrameBuffer();
        this.pickingTexture = new PickingTexture();
        this.pickingShader = AssetPool.addAndGetShader("src/dream/res/shaders/pickingShader.glsl");
    }

    public void initializeEditor(long windowID)
    {
        this.imguiInterface.initImGui(windowID, this.gameViewport);
        this.pickingShader.createShaderObject();
        this.editorCamera.requestActivation();
        this.scene.startScene();
    }

    public void startScene()
    {
        this.scene.startScene();
    }

    public void addObjectToScene(Entity entity)
    {
        this.scene.addSceneObject(entity);
    }

    public void drawEditor()
    {
        this.imguiInterface.startRenderFrame();
        this.imguiInterface.drawDockSpace();

        this.editorCamera.update();

        List<Entity> entities = this.scene.getSceneObjects();

        this.gameViewport.renderViewPort(this.frameBuffer.getTextureID());
        this.sceneGraphWindow.drawImGui(entities);
        this.inspectorWindow.drawImGui();
        this.renderer.prepareRenderer(this.frameBuffer);
        renderPickingTexture(entities);
        renderFrameBuffer(entities);
        //ImGui.showDemoWindow();

        this.imguiInterface.endRenderFrame();
    }

    private void renderFrameBuffer(List<Entity> entities)
    {
        this.renderer.renderEntities(this.editorCamera, this.frameBuffer, null, entities, false);
    }

    private void renderPickingTexture(List<Entity> entities)
    {
        boolean blended = this.renderer.startPickingTexturePass(this.pickingTexture);
        this.renderer.renderEntities(this.editorCamera, null, this.pickingShader, entities, true);
        if(MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int entityID = pickingTexture.readPixel(x, y);
            //System.out.println(entityID);
            Entity selectedEntity = EntityManager.getEntity(entityID);
            this.inspectorWindow.setSelectedEntity(selectedEntity);
        }
        this.renderer.endPickingTexturePass(this.pickingTexture, blended);
    }

    public void cleanUp()
    {
        this.imguiInterface.cleanUp();
        this.pickingShader.destroy();
        this.frameBuffer.destroy();
        this.pickingTexture.destroy();
    }


    public static Editor getEditor()
    {
        if(EDITOR == null)
            EDITOR = new Editor();
        return EDITOR;
    }

    public static boolean updateBuffers()
    {
        if(!(Window.getPrevWindowWidth() >= Window.getWindowWidth() ||
                Window.getPrevWindowHeight() >= Window.getWindowHeight()))
        {
            if(EDITOR.frameBuffer != null)
                EDITOR.frameBuffer.destroy();

            if(EDITOR.pickingTexture != null)
                EDITOR.pickingTexture.destroy();

            EDITOR.frameBuffer = FrameBuffer.createFrameBuffer();
            EDITOR.pickingTexture = new PickingTexture();
            return true;
        }
        return false;
    }


    public Camera getCamera()
    {
        return this.editorCamera;
    }
}
