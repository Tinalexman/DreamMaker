package dream.events;

public abstract class DreamAction
{
    protected DreamEvent event;

    public DreamAction(DreamEvent event)
    {
        this.event = event;
    }

    public DreamEvent getEvent()
    {
        return this.event;
    }
}
