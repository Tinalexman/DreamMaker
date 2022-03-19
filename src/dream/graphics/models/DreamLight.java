package dream.graphics.models;

import org.joml.Vector3f;

public class DreamLight
{
    private final Vector3f ambient;
    private final Vector3f diffuse;
    private final Vector3f specular;
    private Vector3f position;

    public DreamLight(Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f position)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.position = position;
    }

    public Vector3f getAmbient()
    {
        return this.ambient;
    }

    public Vector3f getDiffuse()
    {
        return this.diffuse;
    }

    public Vector3f getSpecular()
    {
        return this.specular;
    }

    public Vector3f getPosition()
    {
        return this.position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public DreamLight(DreamLight dreamLight)
    {
        this.ambient = dreamLight.ambient;
        this.diffuse = dreamLight.diffuse;
        this.specular = dreamLight.specular;
        this.position = dreamLight.position;
    }

}
