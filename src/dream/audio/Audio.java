package dream.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;

public class Audio
{
    private static long audioDevice;
    private static long audioContext;


    public static void startEngine()
    {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if(!alCapabilities.OpenAL10)
        {
            cleanUp();
            throw new IllegalStateException("Audio Engine not started");
        }

    }

    public static void cleanUp()
    {
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
    }

}
