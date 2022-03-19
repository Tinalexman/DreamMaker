package dream.events;

public class DreamKeyInput extends DreamAction
{
    protected boolean[] keys;

    public DreamKeyInput(DreamEvent event)
    {
        super(event);
    }

    public void setKeys(boolean[] keys)
    {
        this.keys = keys;
    }

    public boolean[] getKeys()
    {
        return this.keys;
    }
}
