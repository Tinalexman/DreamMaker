package dream.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
    private static final Map<DreamEvent, List<DreamHandler<DreamKeyInput>>> keyHandlers = new HashMap<>();
    private static final Map<DreamEvent, List<DreamHandler<DreamMouseInput>>> mouseHandlers = new HashMap<>();
    private static final List<DreamAction> actions = new ArrayList<>();

    public static void registerKeyHandler(DreamEvent event, DreamHandler<DreamKeyInput> handler)
    {
        var handlers = keyHandlers.computeIfAbsent(event, k -> new ArrayList<>());
        if(!handlers.contains(handler))
            handlers.add(handler);
    }

    public static void registerMouseHandler(DreamEvent event, DreamHandler<DreamMouseInput> handler)
    {
        var handlers = mouseHandlers.computeIfAbsent(event, k -> new ArrayList<>());
        if(!handlers.contains(handler))
            handlers.add(handler);
    }

    public static void unregisterMouseHandler(DreamEvent event, DreamHandler<DreamMouseInput> handler)
    {
        var handlers = mouseHandlers.get(event);
        if(handlers != null)
            handlers.remove(handler);
    }

    public static void unregisterKeyHandler(DreamEvent event, DreamHandler<DreamKeyInput> handler)
    {
        var handlers = keyHandlers.get(event);
        if(handlers != null)
            handlers.remove(handler);
    }

    public static void update(DreamAction action)
    {
        actions.add(action);
    }

    public static void respondAll()
    {
        for(int pos = 0; pos < actions.size(); ++pos)
        {
            DreamAction action = actions.get(pos);
            DreamEvent event = action.event;
            switch(event)
            {
                case KEY_TYPED, KEY_PRESSED, KEY_RELEASED ->
                {
                    var handlers = keyHandlers.get(event);
                    if(handlers != null)
                    {
                        DreamKeyInput keyInput = (DreamKeyInput) action;
                        for(var handler : handlers)
                            handler.respond(keyInput);
                    }
                }
                case MOUSE_CLICKED, MOUSE_MOVED, MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_SCROLL ->
                {
                    var handlers = mouseHandlers.get(event);
                    if(handlers != null)
                    {
                        DreamMouseInput mouseInput = (DreamMouseInput) action;
                        for(var handler : handlers)
                            handler.respond(mouseInput);
                    }
                }
            }
        }
        actions.clear();
    }
}
