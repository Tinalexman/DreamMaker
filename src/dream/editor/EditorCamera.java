package dream.editor;

import dream.ecs.containables.camera.Camera;
import dream.constants.GlobalConstants;
import dream.io.input.KeyListener;
import dream.io.input.MouseListener;
import dream.io.output.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class EditorCamera extends Camera
{
    public EditorCamera()
    {
        super();
    }

    @Override
    public void update()
    {
        float CAMERA_SPEED = (float) (GlobalConstants.CAMERA_SPEED * Window.getDelta());

        if(KeyListener.isKeyDown(GLFW_KEY_S))
        {
            Vector3f test = new Vector3f();
            cameraFront.mul(CAMERA_SPEED, test);
            Vector3f temp = new Vector3f();
            this.cameraPosition.add(test, temp);
            setPosition(temp);
        }

        if(KeyListener.isKeyDown(GLFW_KEY_W))
        {
            Vector3f test = new Vector3f();
            cameraFront.mul(CAMERA_SPEED, test);
            Vector3f temp = new Vector3f();
            this.cameraPosition.sub(test, temp);
            setPosition(temp);
        }

        if(KeyListener.isKeyDown(GLFW_KEY_D))
        {
            Vector3f test = new Vector3f();
            cameraFront.cross(upVector, test);
            test.normalize();
            test.mul(CAMERA_SPEED);
            Vector3f temp = new Vector3f();
            this.cameraPosition.sub(test, temp);
            setPosition(temp);
        }

        if(KeyListener.isKeyDown(GLFW_KEY_A))
        {
            Vector3f test = new Vector3f();
            cameraFront.cross(upVector, test);
            test.normalize();
            test.mul(CAMERA_SPEED);
            Vector3f temp = new Vector3f();
            this.cameraPosition.add(test, temp);
            setPosition(temp);
        }

        float scrollY = MouseListener.getScrollY();
        if(scrollY != 0.0f)
        {
            this.cameraZoom -= scrollY;
            if(this.cameraZoom < GlobalConstants.DEFAULT_CAMERA_MIN_ZOOM)
                this.cameraZoom = GlobalConstants.DEFAULT_CAMERA_MIN_ZOOM;
            if(this.cameraZoom > GlobalConstants.DEFAULT_CAMERA_MAX_ZOOM)
                this.cameraZoom = GlobalConstants.DEFAULT_CAMERA_MAX_ZOOM;
        }

        if(MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && MouseListener.isDragging())
        {// pitch = about x  yaw = about y roll = about z
            float xPosition = MouseListener.getX();
            float yPosition = MouseListener.getY();

            this.cameraRotation.y += xPosition;
            this.cameraRotation.x += yPosition;

            if(this.cameraRotation.x > GlobalConstants.DEFAULT_CAMERA_MAX_PITCH)
                this.cameraRotation.x = GlobalConstants.DEFAULT_CAMERA_MAX_PITCH;
            if(this.cameraRotation.x < GlobalConstants.DEFAULT_CAMERA_MIN_PITCH)
                this.cameraRotation.x = GlobalConstants.DEFAULT_CAMERA_MIN_PITCH;
        }
    }
}
