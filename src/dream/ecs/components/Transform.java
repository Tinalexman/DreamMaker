package dream.ecs.components;

import dream.editor.InspectorWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform extends Component
{
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform()
    {
        this.position = new Vector3f(0.0f);
        this.rotation = new Vector3f(0.0f);
        this.scale = new Vector3f(1.0f);
        this.ready = true;
    }

    public Transform(Transform transform)
    {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = new Vector3f();

        this.position.x = transform.position.x;
        this.position.y = transform.position.y;

        this.rotation.x = transform.rotation.x;
        this.rotation.y = transform.rotation.y;

        this.scale.x = transform.scale.x;
        this.scale.y = transform.scale.y;

        this.ready = true;
    }

    @Override
    public void drawImGui()
    {
        InspectorWindow.drawVector3Control( "Position:", this.position);
        InspectorWindow.drawVector3Control( "Rotation:", this.rotation);
        InspectorWindow.drawVector3Control( "Scale:", this.scale, 1.0f);
        this.changed = true;
    }

    @Override
    public void onStart()
    {
        this.changed = true;
        this.ready = true;
    }

    public Vector3f getPosition()
    {
        return this.position;
    }

    public void setPosition(Vector3f position)
    {
        if(!this.position.equals(position))
        {
            this.changed = true;
            this.position = position;
        }
    }

    public Vector3f getRotation()
    {
        return this.rotation;
    }

    public void setRotation(Vector3f rotation)
    {
        if(!this.rotation.equals(rotation))
        {
            this.changed = true;
            this.rotation = rotation;
        }
    }

    public Vector3f getScale()
    {
        return this.scale;
    }

    public void setScale(Vector3f scale)
    {
        if(!this.scale.equals(scale))
        {
            this.changed = true;
            this.scale = scale;
        }
    }

    public Matrix4f getTransformationMatrix()
    {
        if(changed)
        {
            return new Matrix4f().identity()
                    .translate(this.position)
                    .rotate((float) Math.toRadians(this.rotation.x), new Vector3f(1.0f, 0.0f, 0.0f))
                    .rotate((float) Math.toRadians(this.rotation.y), new Vector3f(0.0f, 1.0f, 0.0f))
                    .rotate((float) Math.toRadians(this.rotation.z), new Vector3f(0.0f, 0.0f, 1.0f))
                    .scale(this.scale);
        }
        return null;
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Transform transform))
            return false;
        return this.position.equals(transform.position) &&
                this.rotation.equals(transform.rotation) &&
                this.scale.equals(transform.scale);
    }

    public Transform copy(Transform transform)
    {
        return new Transform(transform);
    }

}
