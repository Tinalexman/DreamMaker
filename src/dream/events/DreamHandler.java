package dream.events;

public interface DreamHandler<T extends DreamAction>
{
    void respond(T action);
}
