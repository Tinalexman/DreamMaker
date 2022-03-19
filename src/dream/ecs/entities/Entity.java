package dream.ecs.entities;

import dream.ecs.components.Component;
import dream.scripting.Script;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entity
{
    private String name;
    private final int ID;

    private final List<Component> components;

    private Script script;


    Entity(String name, int ID)
    {
        this.name = name;
        this.ID = ID;
        this.components = new ArrayList<>();
        this.script = new Script();
    }

    Entity(int ID)
    {
        this("Entity " + ID, ID);
    }

    public Script getScript()
    {
        return this.script;
    }

    public void setScript(Script script)
    {
        this.script = script;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getID()
    {
        return this.ID;
    }

    public void drawImGui()
    {
        for(Component component : components)
        {
            if(component.isDrawable() && ImGui.collapsingHeader(component.getClass().getSimpleName()))
                component.drawImGui();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        if(this.components.isEmpty())
            return null;

        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                try
                {
                    return componentClass.cast(component);
                }
                catch (ClassCastException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void start()
    {
        for(int pos = 0; pos < components.size(); ++pos)
            this.components.get(pos).onStart();
    }

    public void addComponents(Component ... components)
    {
        Collections.addAll(this.components, components);
    }

    public void addComponent(Component component)
    {
        this.components.add(component);
        component.setParentID(this.ID);
    }

    public void addComponentIfAbsent(Component component)
    {
        for(Component comp : components)
        {
            if(component.getClass().isAssignableFrom(comp.getClass()))
                return;
        }
        addComponent(component);
    }

    public <T extends Component> boolean removeComponent(Class<T> componentClass)
    {
        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                try
                {
                    component.onDestroyRequested();
                    components.remove(component);
                    return true;
                }
                catch (ClassCastException ignored)
                {

                }
            }
        }
        return false;
    }

    public void removeAllComponents()
    {
        for(Component component : components)
            component.onDestroyRequested();
        components.clear();
    }

    public boolean isSerializable()
    {
        return true;
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Entity entity))
            return false;
        return this.name.equals(entity.name)
                && this.script.equals(entity.script)
                && this.ID == entity.ID
                && this.components.equals(entity.components);
    }

}
