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
import jhelp.engine2.render.Node;
import jhelp.engine2.render.Point3D;
import jhelp.engine2.render.event.NodePositionListener;
import jhelp.util.list.Queue;
import jhelp.util.thread.CancellableTask;
import jhelp.util.thread.ThreadManager;
import org.lwjgl.openal.AL10;

/**
 * A source of sound.  is an origin of a sound.<br>
 * A sound source can be place in 3D or attach to a {@link Node}.<br>
 * The 3D effect work only for MONO sound (See OpenAL documentation).<br>
 * STEREO sounds will ignore the 3D position.<br>
 * To create one instance use {@link SoundManager#createSource()}.
 */
public class SoundSource
{
    /**
     * Sound buffer reference
     */
    private       int                         buffer;
    /**
     * Node to follow
     */
    private       Node                        linked;
    /**
     * Listener attched to followed Node
     */
    private final NodePositionListener        nodePositionListener;
    /**
     * Indicates if a sound playing
     */
    private       boolean                     soundPlaying;
    /**
     * Queue od sound to play
     */
    private final Queue<Sound>                soundQueue;
    /**
     * Sound source reference
     */
    private       int                         source;
    /**
     * Task started when current sound is finished
     */
    private       CancellableTask<Void, Void> waitSoundFinished;
    /**
     * Source X position
     */
    private       float                       x;
    /**
     * Source Y position
     */
    private       float                       y;
    /**
     * Source Z position
     */
    private       float                       z;

    /**
     * Create a sound source
     */
    SoundSource()
    {
        this.soundQueue = new Queue<>();
        this.source = AL10.alGenSources();
        this.buffer = -1;
        this.soundPlaying = false;
        this.nodePositionListener = this::nodePositionChanged;
        this.x = this.y = this.z = 0;
        AL10.alSource3f(this.source, AL10.AL_POSITION, this.x, this.y, this.z);
    }

    /**
     * Called each time followed node position change
     *
     * @param node Node position changed
     * @param x    New node position X
     * @param y    New node position Y
     * @param z    New node position Z
     */
    private void nodePositionChanged(@NotNull Node node, float x, float y, float z)
    {
        AL10.alSource3f(this.source, AL10.AL_POSITION, x, y, z);
    }

    /**
     * Called when current sound finished
     */
    private void soundFinished()
    {
        this.soundPlaying = false;

        synchronized (this.soundQueue)
        {
            if (this.soundQueue.empty())
            {
                if (this.source >= 0 && this.buffer >= 0)
                {
                    AL10.alSourceStop(this.source);
                    AL10.alDeleteBuffers(this.buffer);
                    this.buffer = -1;
                }
            }
            else
            {
                this.play(this.soundQueue.outQueue());
            }
        }
    }

    /**
     * Enqueue a sound.<br>
     * If their not sound in waiting queue and no sound is currently playing, the given sound is playing immediately and becomes the current one.<br>
     * In other cases, the sound is just put in queue and wait its turn.
     *
     * @param sound Sound to enqueue.
     */
    void enqueue(@NotNull Sound sound)
    {
        if (this.source < 0)
        {
            return;
        }

        if (!this.soundPlaying)
        {
            this.play(sound);
            return;
        }

        synchronized (this.soundQueue)
        {
            this.soundQueue.inQueue(sound);
        }
    }

    /**
     * Play a sound immediately on stopping current one if need
     *
     * @param sound Sound to play now
     */
    void play(@NotNull Sound sound)
    {
        this.soundPlaying = true;

        if (this.waitSoundFinished != null)
        {
            this.waitSoundFinished.cancel();
            this.waitSoundFinished = null;
        }

        if (this.source < 0)
        {
            return;
        }

        if (this.buffer >= 0)
        {
            AL10.alSourceStop(this.source);
            AL10.alDeleteBuffers(this.buffer);
        }

        this.buffer = AL10.alGenBuffers();
        sound.transferToBuffer(this.buffer);
        AL10.alSourcei(this.source, AL10.AL_BUFFER, this.buffer);
        AL10.alSourcePlay(this.source);
        this.waitSoundFinished = ThreadManager.runCancellable(this::soundFinished, sound.duration());
    }

    /**
     * Stop all sounds and free memory. <br>
     * Not reuse the sound source after that call
     */
    void stopAll()
    {
        if (this.waitSoundFinished != null)
        {
            this.waitSoundFinished.cancel();
            this.waitSoundFinished = null;
        }

        if (this.source < 0)
        {
            return;
        }

        if (this.buffer >= 0)
        {
            AL10.alSourceStop(this.source);
            AL10.alDeleteBuffers(this.buffer);
            this.buffer = -1;
        }

        AL10.alDeleteSources(this.source);
        this.source = -1;
    }

    /**
     * Stop current sound (If their one) and clear the sound queue
     */
    public void clearSounds()
    {
        if (this.waitSoundFinished != null)
        {
            this.waitSoundFinished.cancel();
            this.waitSoundFinished = null;
        }

        synchronized (this.soundQueue)
        {
            this.soundQueue.clear();
        }

        if (this.source < 0)
        {
            return;
        }

        if (this.buffer >= 0)
        {
            AL10.alSourceStop(this.source);
            AL10.alDeleteBuffers(this.buffer);
            this.buffer = -1;
        }
    }

    /**
     * Enqueue a sound.<br>
     * If their not sound in waiting queue and no sound is currently playing, the given sound is playing immediately and becomes the current one.<br>
     * In other cases, the sound is just put in queue and wait its turn.
     *
     * @param sound Sound to enqueue
     */
    public final void enqueueSound(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");
        this.enqueue(sound);
    }

    /**
     * Link the source to given node.
     * That is to say the sound will take the position where the node is and each time the node move, the sound will move too.<br>
     * To free the source node call {@link #unLink()}
     *
     * @param node Node to follow
     */
    public void link(@NotNull Node node)
    {
        if (this.linked != null)
        {
            this.linked.unregisterNodePositionListener(this.nodePositionListener);
        }

        this.linked = node;

        if (this.linked != null)
        {
            AL10.alSource3f(this.source, AL10.AL_POSITION, this.linked.x(), this.linked.y(), this.linked.z());
            this.linked.registerNodePositionListener(this.nodePositionListener);
        }
        else
        {
            AL10.alSource3f(this.source, AL10.AL_POSITION, this.x, this.y, this.z);
        }
    }

    /**
     * Play a sound immediately on stopping current one if need
     *
     * @param sound Sound to play now
     */
    public final void playSound(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");
        this.play(sound);
    }

    /**
     * Change the sound source position.<br>
     * If the source is linked to a node, the given position is not taken immediately,
     * but the next time the source will be free of constraints (That is to say next call of {@link #unLink()})
     *
     * @param x X position
     * @param y Y position
     * @param z Z position
     */
    public final void position(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;

        if (this.linked == null)
        {
            AL10.alSource3f(this.source, AL10.AL_POSITION, this.x, this.y, this.z);
        }
    }

    /**
     * Remove node following constraints
     */
    public void unLink()
    {
        if (this.linked != null)
        {
            this.linked.unregisterNodePositionListener(this.nodePositionListener);
        }

        this.linked = null;
        AL10.alSource3f(this.source, AL10.AL_POSITION, this.x, this.y, this.z);
    }

    /**
     * Current source position
     *
     * @return Current source position
     */
    public @NotNull Point3D position()
    {
        if (this.linked != null)
        {
            return new Point3D(this.linked.x(), this.linked.y(), this.linked.z());
        }

        return new Point3D(this.x, this.y, this.z);
    }
}
