package dream.ecs.components;

import dream.ecs.entities.EntityManager;

public class Shape extends Component
{

    public Shape()
    {

    }

    @Override
    public boolean isDrawable()
    {
        return false;
    }

    @Override
    public void onStart()
    {
        if(!this.ready)
        {
            EntityManager.getEntity(this.parentID).addComponentIfAbsent(new Mesh());
            EntityManager.getEntity(this.parentID).addComponentIfAbsent(new Color());
            EntityManager.getEntity(this.parentID).addComponentIfAbsent(new Transform());
            this.ready = true;
        }
    }

}
