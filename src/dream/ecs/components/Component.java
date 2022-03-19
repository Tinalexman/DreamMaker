package dream.ecs.components;

import dream.ecs.entities.EntityManager;
import dream.editor.InspectorWindow;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component
{
    protected int parentID;
    protected boolean ready;
    protected boolean changed;

    public Component()
    {
        this.parentID = EntityManager.NO_PARENT;
        this.ready = false;
    }

    public void setParentID(int entityID)
    {
        this.parentID = entityID;
    }

    public int getParentID()
    {
        return this.parentID;
    }

    public void onStart()
    {

    }

    public void onDestroyRequested()
    {

    }

    public boolean hasChanged()
    {
        return this.changed;
    }

    public void hasChanged(boolean changed)
    {
        this.changed = changed;
    }

    public void resetChange()
    {
        hasChanged(false);
    }

    public void drawImGui()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields)
            {
                boolean isTransientOrStatic = Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers());
                if(isTransientOrStatic)
                    continue;

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate)
                    field.setAccessible(true);

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class)
                {
                    int imInt = (int) value;
                    field.set(this, InspectorWindow.drawIntControl(name, imInt));
                }
                else if(type == float.class)
                {
                    float imFloat = (float) value;
                    field.set(this, InspectorWindow.drawFloatControl(name, imFloat));
                }
                else if(type == boolean.class)
                {
                    boolean val = (boolean) value;
                    field.set(this, InspectorWindow.drawBooleanControl(name, val));
                }
                else if (type == Vector2f.class)
                {
                    Vector2f val = (Vector2f) value;
                    if(InspectorWindow.drawVector2Control(name + ":", val))
                        this.changed = true;
                }
                else if (type == Vector3f.class)
                {
                    Vector3f val = (Vector3f) value;
                    InspectorWindow.drawVector3Control(name + ":", val);
                }
                else if(type == Vector4f.class)
                {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ":", imVec))
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                }

                if(isPrivate)
                    field.setAccessible(false);

                this.changed = true;
            }
        }
        catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isDrawable()
    {
        return true;
    }
}


