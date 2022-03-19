package dream.ecs.components;

import dream.graphics.constants.MaterialConstants;
import org.joml.Vector3f;

public class Material extends Component
{
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float shininess;


    public Material()
    {
        this(MaterialConstants.DEFAULT);
    }

    public Material(Material material)
    {
        this.ambient = new Vector3f(material.ambient);
        this.diffuse = new Vector3f(material.diffuse);
        this.specular = new Vector3f(material.specular);
        this.shininess = material.shininess;
    }

    @Override
    public void onStart()
    {
        this.changed = true;
        this.ready = true;
    }

    public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
    {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
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

    public float getShininess()
    {
        return this.shininess;
    }

    public void setAmbient(Vector3f ambient)
    {
        if(!this.ambient.equals(ambient))
        {
            this.changed = true;
            this.ambient = ambient;
        }
    }

    public void setDiffuse(Vector3f diffuse)
    {
        if(!this.diffuse.equals(diffuse))
        {
            this.changed = true;
            this.diffuse = diffuse;
        }
    }

    public void setSpecular(Vector3f specular)
    {
        if (!this.specular.equals(specular))
        {
            this.changed = true;
            this.specular = specular;
        }
    }

    public void setShininess(float shininess)
    {
        if(this.shininess != shininess)
        {
            this.changed = true;
            this.shininess = shininess;
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Material material))
            return false;
        return material.diffuse.equals(this.diffuse)
                && material.ambient.equals(this.ambient)
                && material.specular.equals(this.specular)
                && material.shininess == this.shininess;
    }

}
