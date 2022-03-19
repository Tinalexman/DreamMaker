package dream.constants;

import dream.graphics.models.DreamLight;
import org.joml.Vector3f;

public class LightConstants
{
    public static DreamLight WHITE_LIGHT = new DreamLight(new Vector3f(0.2f), new Vector3f(1.0f),
            new Vector3f(0.5f), new Vector3f(1.0f));

    public static DreamLight NO_LIGHT = new DreamLight(new Vector3f(0.2f), new Vector3f(0.5f),
            new Vector3f(0.5f), new Vector3f(1.0f));
}
