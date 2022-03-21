package dream.audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound
{
    private int bufferID;
    private int sourceID;
    private String filepath;

    private boolean isPlaying;

    public Sound(String filepath)
    {
        this.filepath = filepath;

        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);

        if(rawAudioBuffer == null)
        {
            System.err.println("Could not load file '" + filepath + "'");
            stackPop();
            stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        stackPop();
        stackPop();

        int format = -1;
        if(channels == 1)
            format = AL_FORMAT_MONO16;
        else if(channels == 2)
            format = AL_FORMAT_STEREO16;

        this.bufferID = alGenBuffers();
        alBufferData(this.bufferID, format, rawAudioBuffer, sampleRate);

        this.sourceID = alGenSources();
        alSourcei(this.sourceID, AL_BUFFER, this.bufferID);
        alSourcei(this.sourceID, AL_POSITION, 0);
        alSourcef(this.sourceID, AL_GAIN, 0.35f);

        free(rawAudioBuffer);
    }

    public void play()
    {
        int state = alGetSourcei(this.sourceID, AL_SOURCE_STATE);
        if(state == AL_STOPPED)
        {
            this.isPlaying = false;
            alSourcei(this.sourceID, AL_POSITION, 0);
        }
        if(!this.isPlaying)
        {
            alSourcePlay(this.sourceID);
            this.isPlaying = true;
        }
    }

    public void stop()
    {
        if(this.isPlaying)
        {
            alSourceStop(this.sourceID);
            this.isPlaying = false;
        }
    }

    public void setVolume(float volume)
    {
        if(volume < 0.0f || volume > 1.0f)
            return;

        alSourcef(this.sourceID, AL_GAIN, volume);
    }

    public void pause()
    {
        if(this.isPlaying)
        {
            alSourcePause(this.sourceID);
            this.isPlaying = false;
        }
    }

    public void startOver()
    {
        stop();
        play();
    }

    public void destroy()
    {
        alDeleteBuffers(this.bufferID);
        alDeleteSources(this.sourceID);
    }

    public String getFilepath()
    {
        return this.filepath;
    }

    public boolean isPlaying()
    {
        int state = alGetSourcei(this.sourceID, AL_SOURCE_STATE);
        if(state == AL_STOPPED)
            this.isPlaying = false;
        return this.isPlaying;
    }

    public void loopSound(boolean loop)
    {
        alSourcei(this.sourceID, AL_LOOPING, loop ? 1 : 0);
    }

}
