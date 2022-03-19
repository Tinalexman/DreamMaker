package dream;

import dream.events.EventManager;
import dream.graphics.Graphics;


public class DreamMaker
{
    public static volatile boolean PRE_LOADING = true;

    public static void startEngine()
    {
        Graphics.startEngine();

        while(Graphics.isRunning)
        {
            try
            {
                EventManager.respondAll();
                Thread.sleep(10);
            }
            catch (InterruptedException ignored)
            {

            }
        }
    }
}
