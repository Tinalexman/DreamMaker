package dream.ecs.components;

import org.joml.Vector3f;

public class Color extends Component
{
    private final Vector3f rgb;

    public Color()
    {
        this.rgb = new Vector3f(1.0f);
    }

    public Color(float red, float green, float blue)
    {
        this.rgb = new Vector3f(red, green, blue);
    }

    public Color(Vector3f color)
    {
        this.rgb = new Vector3f(color);
    }

    public Color(Color color)
    {
        this.rgb = new Vector3f(color.rgb);
    }

    @Override
    public void onStart()
    {
        this.ready = true;
        this.changed = true;
    }

    public Vector3f getRGB()
    {
        return this.rgb;
    }

    public float getRed()
    {
        return this.rgb.x;
    }

    public float getGreen()
    {
        return this.rgb.y;
    }

    public float getBlue()
    {
        return this.rgb.z;
    }

    public void setRed(float red)
    {
        if(this.rgb.x != red)
        {
            this.changed = true;
            this.rgb.x = red;
        }
    }

    public void setGreen(float green)
    {
        if(this.rgb.y != green)
        {
            this.changed = true;
            this.rgb.y = green;
        }
    }

    public void setBlue(float blue)
    {
        if(this.rgb.z != blue)
        {
            this.changed = true;
            this.rgb.z = blue;
        }
    }
}
