package dream;

import dream.audio.Audio;
import dream.graphics.Graphics;


public class DreamMaker
{
    public static volatile boolean PRE_LOADING = true;

    public static void startDream()
    {
        Audio.startEngine();
        Graphics.startEngine();
    }
}
