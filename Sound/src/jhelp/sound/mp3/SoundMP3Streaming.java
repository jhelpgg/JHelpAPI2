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
package jhelp.sound.mp3;

import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jhelp.sound.SoundException;
import jhelp.sound.SoundListener;
import jhelp.util.debug.Debug;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Sound play MP3
 */
public class SoundMP3Streaming
{
    /**
     * Stream we can control the reading
     */
    private ControlBufferedInputStream controlInputStream;
    /**
     * Listener of sound events
     */
    private SoundListener              soundListener;
    /**
     * A live state
     */
    boolean alive = false;
    /**
     * Lock for synchronize
     */
    final Object lock = new Object();
    /**
     * Player from JL020 API (javazoom)
     */
    Player player;
    /**
     * Task that play the sound
     */
    private final RunnableTask taskPlayTheSound = new RunnableTask()
    {
        /**
         * Play the sound <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.util.thread.RunnableTask#run()
         */
        @Override
        public void run()
        {
            try
            {
                if (SoundMP3Streaming.this.player != null)
                {
                    SoundMP3Streaming.this.player.play();
                }
            }
            catch (final JavaLayerException e)
            {
                Debug.exception(e);
            }

            SoundMP3Streaming.this.playEnd();
        }
    };

    /**
     * Create a new instance of SoundMP3Streaming
     *
     * @param inputStream Stream to read
     */
    public SoundMP3Streaming(final InputStream inputStream)
    {
        this.controlInputStream = new ControlBufferedInputStream(inputStream);
    }

    /**
     * Call when sound is finish to play
     */
    void playEnd()
    {
        synchronized (this.lock)
        {
            this.alive = false;
        }

        // Close the player
        if (this.player != null)
        {
            this.player.close();
        }
        this.player = null;

        if (this.soundListener != null)
        {
            this.soundListener.soundEnd();
        }
    }

    /**
     * Destroy the sound
     */
    public void destroy()
    {
        if (this.player != null)
        {
            this.player.close();
        }
        this.player = null;

        synchronized (this.lock)
        {
            this.alive = false;
        }

        if (this.controlInputStream != null)
        {
            this.controlInputStream.destroy();
        }
        this.controlInputStream = null;

        this.soundListener = null;
    }

    /**
     * Indicates if sound is playing
     *
     * @return {@code true} if sound is playing
     * @see jhelp.sound.Sound#playing()
     */
    public boolean isPlaying()
    {
        return this.alive;
    }

    /**
     * Play the sound
     *
     * @see jhelp.sound.Sound#play()
     */
    public void play()
    {
        synchronized (this.lock)
        {
            if (!this.alive)
            {
                this.alive = true;

                try
                {
                    // Create the player
                    this.controlInputStream.setPause(false);
                    this.controlInputStream.reset();
                    this.player = new Player(this.controlInputStream);
                }
                catch (final Exception e)
                {
                    throw new SoundException(e, "Playing start fail");
                }

                ThreadManager.parallel(this.taskPlayTheSound);
            }
        }
    }

    /**
     * Change pause state
     *
     * @param pause New pause state
     */
    public void setPause(final boolean pause)
    {
        this.controlInputStream.setPause(pause);
    }

    /**
     * Change sound listener
     *
     * @param soundListener New sound listener
     * @see jhelp.sound.Sound#soundListener(jhelp.sound.SoundListener)
     */
    public void setSoundListener(final SoundListener soundListener)
    {
        this.soundListener = soundListener;
    }

    /**
     * Stop sound
     *
     * @see jhelp.sound.Sound#stop()
     */
    public void stop()
    {
        this.destroy();
    }
}