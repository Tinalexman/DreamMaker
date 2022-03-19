package dream.graphics.imgui;

import dream.editor.GameViewPort;

import dream.io.input.KeyListener;
import dream.io.input.MouseListener;
import dream.io.output.Window;
import dream.scenes.SceneManager;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ImguiInterface
{
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public static void initImGui(long windowID)
    {
        ImGui.createContext();
        ImGui.styleColorsDark();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini"); // We don't want to save .ini file
//        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw"); // For clarity reasons

        // ------------------------------------------------------------
        // Here goes GLFW callbacks to update user input in Dear ImGui

        glfwSetKeyCallback(windowID, (w, key, scancode, action, mods) ->
        {
            if (action == GLFW_PRESS)
                io.setKeysDown(key, true);
            else if (action == GLFW_RELEASE)
                io.setKeysDown(key, false);

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if(!io.getWantCaptureKeyboard())
                KeyListener.keyCallback(w, key, scancode, action, mods);
        });

        glfwSetCharCallback(windowID, (w, c) ->
        {
            if (c != GLFW_KEY_DELETE)
                io.addInputCharacter(c);
        });

        glfwSetMouseButtonCallback(windowID, (w, button, action, mods) ->
        {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1])
                ImGui.setWindowFocus(null);

            //if(!io.getWantCaptureMouse())
                MouseListener.mouseButtonCallback(w, button, action, mods);
        });

        glfwSetScrollCallback(windowID, (w, xOffset, yOffset) ->
        {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            MouseListener.mouseScrollCallback(w, xOffset, yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer()
        {
            @Override
            public void accept(final String s)
            {
                glfwSetClipboardString(windowID, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier()
        {
            @Override
            public String get()
            {
                String clipboardString = glfwGetClipboardString(windowID);
                return (clipboardString != null) ? clipboardString : "";
            }
        });

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Keep in mind that creation of the ImFontConfig will allocate native memory
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault()); // Additional glyphs could be added like this or in addFontFrom*() methods
        fontConfig.setRasterizerMultiply(1.5f); // This will make fonts a bit more readable
        // We can add new fonts directly from file
        fontAtlas.addFontFromFileTTF("src/dream/res/fonts/segoeui.ttf", 16, fontConfig);
        fontConfig.destroy(); // After all fonts were added we don't need this config more

        imGuiGlfw.init(windowID, false);
        imGuiGl3.init("#version 330 core");
    }

    public static void cleanUp()
    {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public static void render()
    {
        startRenderFrame();
        drawDockSpace();

        //ImGui.showDemoWindow();
        SceneManager.getCurrentScene().drawImGui();
        GameViewPort.renderViewPort();
        SceneManager.getCurrentScene().renderGameObjects();

        endRenderFrame();
    }

    private static void drawDockSpace()
    {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

//        ImGuiViewport mainViewport = ImGui.getMainViewport();
//        ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY());
//        ImGui.setNextWindowSize(mainViewport.getWorkSizeX(), mainViewport.getWorkSizeY());
//        ImGui.setNextWindowViewport(mainViewport.getID());

        ImGui.setNextWindowPos(0.0f, 0.0f);
        ImGui.setNextWindowSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowTitleAlign, 0.5f, 0.5f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockSpace Window", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(4);
        ImGui.dockSpace(ImGui.getID("DockSpace"));

        if(ImGui.beginMenuBar())
        {
            if (ImGui.beginMenu("File"))
            {
                ImGui.menuItem("New Project", "Ctrl + N");
                ImGui.menuItem("Open Project", "Ctrl + O");
                ImGui.endMenu();
            }
        }
        ImGui.endMenuBar();

        ImGui.end();
    }

    private static void startRenderFrame()
    {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    private static void endRenderFrame()
    {
        glViewport(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glBindFramebuffer(GL_FRAMEBUFFER, 0);
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

//        long backupWindowPtr = glfwGetCurrentContext();
//        ImGui.updatePlatformWindows();
//        ImGui.renderPlatformWindowsDefault();
//        glfwMakeContextCurrent(backupWindowPtr);

    }


}
