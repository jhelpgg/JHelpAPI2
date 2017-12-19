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
import jhelp.engine2.render.event.NodePositionListener;
import jhelp.util.list.Queue;
import jhelp.util.thread.CancellableTask;
import jhelp.util.thread.ThreadManager;
import org.lwjgl.openal.AL10;

public class SoundSource
{
    private       int                         buffer;
    private       Node                        linked;
    private final NodePositionListener        nodePositionListener;
    private       boolean                     soundPlaying;
    private final Queue<Sound>                soundQueue;
    private       int                         source;
    private       CancellableTask<Void, Void> waitSoundFinished;
    private       float                       x;
    private       float                       y;
    private       float                       z;

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

    private void nodePositionChanged(Node node, float x, float y, float z)
    {
        AL10.alSource3f(this.source, AL10.AL_POSITION, x, y, z);
    }

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

    void enqueue(Sound sound)
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

    void play(Sound sound)
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

    public final void enqueueSound(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");
        this.enqueue(sound);
    }

    public void link(Node node)
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

    public final void playSound(@NotNull Sound sound)
    {
        Objects.requireNonNull(sound, "sound MUST NOT be null!");
        this.play(sound);
    }

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

    public void unLink()
    {
        if (this.linked != null)
        {
            this.linked.unregisterNodePositionListener(this.nodePositionListener);
        }

        this.linked = null;
        AL10.alSource3f(this.source, AL10.AL_POSITION, this.x, this.y, this.z);
    }
}
