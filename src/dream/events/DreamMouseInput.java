package dream.events;

import org.joml.Vector2f;

public class DreamMouseInput extends DreamAction
{
    protected boolean[] buttons;
    protected Vector2f currentMousePosition;
    protected Vector2f previousMousePosition;
    protected Vector2f mouseScroll;

    public boolean[] getButtons()
    {
        return this.buttons;
    }

    public void setButtons(boolean[] buttons)
    {
        this.buttons = buttons;
    }

    public Vector2f getCurrentMousePosition()
    {
        return this.currentMousePosition;
    }

    public void setCurrentMousePosition(Vector2f currentMousePosition)
    {
        this.currentMousePosition = currentMousePosition;
    }

    public Vector2f getPreviousMousePosition()
    {
        return this.previousMousePosition;
    }

    public void setPreviousMousePosition(Vector2f previousMousePosition)
    {
        this.previousMousePosition = previousMousePosition;
    }

    public Vector2f getMouseScroll()
    {
        return this.mouseScroll;
    }

    public void setMouseScroll(Vector2f mouseScroll)
    {
        this.mouseScroll = mouseScroll;
    }

    public DreamMouseInput(DreamEvent event)
    {
        super(event);
    }

}
