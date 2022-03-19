package dream.ecs.entities;

import dream.ecs.components.Component;

import java.util.HashMap;
import java.util.Map;

public class EntityManager
{
    private static final Map<Integer, Entity> ENTITY_MAP = new HashMap<>();
    private static int NUMBER_OF_ENTITIES = 0;

    public static final int NO_PARENT = -1;


    public static Entity createEntity(String name)
    {
        Entity entity = new Entity(name, ++NUMBER_OF_ENTITIES);
        ENTITY_MAP.put(NUMBER_OF_ENTITIES, entity);
        return entity;
    }

    public static Entity createEntity()
    {
        Entity entity = new Entity(++NUMBER_OF_ENTITIES);
        ENTITY_MAP.put(NUMBER_OF_ENTITIES, entity);
        return entity;
    }

    public static Entity createEntity(String name, Component ... components)
    {
        Entity entity = new Entity(name, ++NUMBER_OF_ENTITIES);
        ENTITY_MAP.put(NUMBER_OF_ENTITIES, entity);
        entity.addComponents(components);
        return entity;
    }

    public static Entity getEntity(int entityID)
    {
        return ENTITY_MAP.get(entityID);
    }

    public static void addEntity(Entity entity)
    {
        ENTITY_MAP.put(entity.getID(), entity);
    }

    public static boolean removeEntity(int entityID)
    {
        return (ENTITY_MAP.remove(entityID) != null);
    }

    public static boolean checkIfParented(int entityID)
    {
        return entityID != NO_PARENT;
    }

}
