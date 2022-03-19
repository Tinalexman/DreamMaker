package dream.ecs.containables.camera;

import dream.ecs.containables.Containable;
import dream.constants.GlobalConstants;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera implements Containable
{
    protected Vector3f cameraRotation; // pitch = about x  yaw = about y roll = about z
    protected Vector3f cameraPosition;
    protected Vector3f upVector;
    protected Vector3f cameraFront;
    protected float cameraZoom;
    protected boolean hasChanged;
    protected Matrix4f inverseViewMatrix;
    protected float nearPlane;
    protected float farPlane;
    protected boolean active;

    private static Camera CURRENT_CAMERA;

    private void getCameraDirection()
    {
        Vector3f cameraDirection = new Vector3f();
        cameraDirection.x = (float) (Math.cos(Math.toRadians(this.cameraRotation.y)) * Math.cos(Math.toRadians(this.cameraRotation.x)));
        cameraDirection.y = (float) (Math.sin(Math.toRadians(this.cameraRotation.x)));
        cameraDirection.z = (float) (Math.sin(Math.toRadians(this.cameraRotation.y)) * Math.cos(Math.toRadians(this.cameraRotation.x)));
        cameraDirection.normalize(this.cameraFront);
    }

    public Camera()
    {
        this.cameraPosition = new Vector3f(0.0f, 0.0f, 5.0f);
        this.cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        this.upVector = new Vector3f(0.0f, 1.0f, 0.0f);
        this.cameraZoom = GlobalConstants.DEFAULT_CAMERA_MAX_ZOOM;
        this.cameraRotation = new Vector3f(0.0f, -90.0f, 0.0f);
        this.hasChanged = true;
        this.active = false;
        this.nearPlane = GlobalConstants.DEFAULT_EDITOR_CAMERA_NEAR_PLANE;
        this.farPlane = GlobalConstants.DEFAULT_EDITOR_CAMERA_FAR_PLANE;

        this.inverseViewMatrix = new Matrix4f().identity();
    }

    public void update()
    {

    }

    public static Camera getCurrentCamera()
    {
        return CURRENT_CAMERA;
    }

    public void requestActivation()
    {
        CURRENT_CAMERA = this;
    }

    public Matrix4f getViewMatrix()
    {
        Matrix4f viewMatrix = new Matrix4f().identity();
        Vector3f test = new Vector3f();
        this.cameraPosition.add(cameraFront, test);
        viewMatrix.lookAt(this.cameraPosition, test, this.upVector);

        viewMatrix.invert(this.inverseViewMatrix);

        return viewMatrix;
    }

    public Matrix4f getInverseViewMatrix()
    {
        return this.inverseViewMatrix;
    }

    public float getFieldOfView()
    {
        return this.cameraZoom;
    }

    public float getNearPlane()
    {
        return this.nearPlane;
    }

    public float getFarPlane()
    {
        return this.farPlane;
    }

    public void setNearPlane(float nearPlane)
    {
        this.nearPlane = nearPlane;
    }

    public void setFarPlane(float farPlane)
    {
        this.farPlane = farPlane;
    }

    public void setUpVector(Vector3f upVector)
    {
        this.upVector = upVector;
    }

    public Vector3f getPosition()
    {
        return this.cameraPosition;
    }

    public void setPosition(Vector3f position)
    {
        if(!this.cameraPosition.equals(position))
        {
            this.hasChanged = true;
            this.cameraPosition = position;
        }
    }

    public boolean hasChanged()
    {
        return hasChanged;
    }

    public void resetChange()
    {
        this.hasChanged = false;
    }

}
