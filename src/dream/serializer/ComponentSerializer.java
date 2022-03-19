package dream.toolbox.serializer;

import com.google.gson.*;
import dream.ecs.components.Component;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component>
{

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        result.add("properties", jsonSerializationContext.serialize(component, component.getClass()));
        return result;
    }
}
