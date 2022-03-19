package dream.toolbox.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dream.ecs.components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonDeserializer<Component>
{

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        return null;
    }
}
