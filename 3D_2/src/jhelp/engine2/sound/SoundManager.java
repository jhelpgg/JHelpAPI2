/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

package jhelp.engine2.sound;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.engine2.render.ThreadOpenGL;
import jhelp.engine2.render.Window3D;
import jhelp.util.list.ArrayObject;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

/**
 * Manager of sounds.<br>
 * Don't create it manually, use instance given by {@link Window3D#soundManager()}. <br>
 * Don't call {@link #destroy()} manually it will be called by the system.<br>
 * If at least one of those two rules is not respected, JVM crashes may occur
 */
public final class SoundManager
{
    /**
     * Indicates if sound manager still alive
     */
    private       boolean                  alive;
    /**
     * CSound context
     */
    private final long                     context;
    /**
     * Sound device
     */
    private final long                     device;
    /**
     * Source to play background sounds
     */
    private final SoundSource              sourceBackground;
    /**
     * Created sound sources
     */
    private final ArrayObject<SoundSource> sources;

    /**
     * Create sound manager.<br>
     * Don't create it manually, use instance given by {@link Window3D#soundManager()}<br>
     * If this rules is not respected, JVM crashes may occur
     */
    @ThreadOpenGL
    public SoundManager()
    {
        this.alive = true;
        this.sources = new ArrayObject<>();
        this.device = ALC10.alcOpenDevice((CharSequence) null);
        this.context = ALC10.alcCreateContext(this.device, (int[]) null);
        ALC10.alcMakeContextCurrent(this.context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(this.device);
        ALCapabilities  alCapabilities  = AL.createCapabilities(alcCapabilities);

        this.sourceBackground = new SoundSource();
    }

    /**
     * Indicates if sound manager still alive
     *
     * @return {@code true} if sound manager still alive
     */
    public boolean alive()
    {
        return this.alive;
    }

    /**
     * Create a sound source
     *
     * @return Created source
     */
    public @NotNull SoundSource createSource()
    {
        if (!this.alive)
        {
            return DummySoundSource.DUMMY_SOUND_SOURCE;
        }

        synchronized (this.sources)
        {
            final SoundSource soundSource = new SoundSource();
            this.sources.add(soundSource);
            return soundSource;
        }
    }

    /**
     * Destroy the sound manager<br>
     * Don't call this method manually, it will be called by the system.<br>
     * If this rules is not respected, JVM crashes may occur
     */
    @ThreadOpenGL
    public void destroy()
    {
        if (!this.alive)
        {
            return;
        }

        this.alive = false;
        this.sourceBackground.stopAll();

        synchronized (this.sources)
        {
            this.sources.consume(SoundSource::stopAll);
            this.sources.clear();
        }

        ALC10.alcMakeContextCurrent(0);
        ALC10.alcDestroyContext(this.context);
        ALC10.alcCloseDevice(this.device);
    }

    public void destroySource(@NotNull SoundSource soundSource)
    {
        soundSource.stopAll();

        synchronized (this.sources)
        {
            this.sources.remove(soundSource);
        }
    }

    /**
     * Enqueue sound in background
     *
     * @param sound Sound to enqueue
     */
    public void enqueueBackground(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");

        if (!this.alive)
        {
            return;
        }

        this.sourceBackground.enqueueSound(sound);
    }

    /**
     * Play  in background
     *
     * @param sound Sound to play
     */
    public void playBackground(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");

        if (!this.alive)
        {
            return;
        }

        this.sourceBackground.playSound(sound);
    }
}
