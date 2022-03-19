package dream.graphics.constants;

import dream.ecs.components.Material;
import org.joml.Vector3f;

public class MaterialConstants
{
    public static final Material CARAMEL = new Material(new Vector3f(1.0f, 0.5f, 0.31f),
            new Vector3f(1.0f, 0.8f, 0.31f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material DEFAULT = new Material(new Vector3f(0.7f, 0.7f, 0.7f),
            new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material RED = new Material(new Vector3f(1.0f, 0.0f, 0.0f),
            new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material GREEN = new Material(new Vector3f(0.0f, 1.0f, 0.0f),
            new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material BLUE = new Material(new Vector3f(0.0f, 0.0f, 1.0f),
            new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material PURPLE = new Material(new Vector3f(1.0f, 0.0f, 1.0f),
            new Vector3f(1.0f, 0.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material CYAN = new Material(new Vector3f(0.0f, 1.0f, 1.0f),
            new Vector3f(0.0f, 1.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material YELLOW = new Material(new Vector3f(1.0f, 1.0f, 0.0f),
            new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

    public static final Material ORANGE = new Material(new Vector3f(1.0f, 0.5f, 0.0f),
            new Vector3f(1.0f, 0.5f, 0.0f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);

}
