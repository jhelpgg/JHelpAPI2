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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jhelp.sound.Sound;
import jhelp.sound.SoundException;
import jhelp.sound.SoundListener;
import jhelp.util.debug.Debug;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Sound play MP3
 */
public class SoundMP3
        implements Sound
{
    /**
     * Stream we can control the reading
     */
    private ControlInputStream controlInputStream;
    /**
     * Listener of sound events
     */
    private SoundListener      soundListener;
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
                if (SoundMP3.this.player != null)
                {
                    SoundMP3.this.player.play();
                }
            }
            catch (final JavaLayerException e)
            {
                Debug.exception(e);
            }

            SoundMP3.this.playEnd();
        }
    };

    /**
     * Constructs SoundMP3
     *
     * @param file File where found the sound
     * @throws SoundException On initialisation problem
     */
    public SoundMP3(final File file)
            throws SoundException
    {
        try
        {
            this.controlInputStream = ControlInputStream.createControlInputStream(new FileInputStream(file));
        }
        catch (final IOException e)
        {
            throw new SoundException("Reading stream problem", e);
        }
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
     *
     * @see jhelp.sound.Sound#destroy()
     */
    @Override
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
     * Play the sound
     *
     * @see jhelp.sound.Sound#play()
     */
    @Override
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
     * Indicates if sound is playing
     *
     * @return {@code true} if sound is playing
     * @see jhelp.sound.Sound#playing()
     */
    @Override
    public boolean playing()
    {
        return this.alive;
    }

    /**
     * Sound actual position
     *
     * @return Sound actual position
     * @see jhelp.sound.Sound#position()
     */
    @Override
    public long position()
    {
        return this.controlInputStream.getPosition();
    }

    /**
     * Change sound position
     *
     * @param position Sound position
     * @see jhelp.sound.Sound#position(long)
     */
    @Override
    public void position(final long position)
    {
        this.controlInputStream.setPosition((int) position);
    }

    /**
     * Change sound listener
     *
     * @param soundListener New sound listener
     * @see jhelp.sound.Sound#soundListener(jhelp.sound.SoundListener)
     */
    @Override
    public void soundListener(final SoundListener soundListener)
    {
        this.soundListener = soundListener;
    }

    /**
     * Stop sound
     *
     * @see jhelp.sound.Sound#stop()
     */
    @Override
    public void stop()
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
    }

    /**
     * Sound total size
     *
     * @return Sound total size
     * @see jhelp.sound.Sound#totalSize()
     */
    @Override
    public long totalSize()
    {
        try
        {
            return this.controlInputStream.available();
        }
        catch (final Exception exception)
        {
            return -1;
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
}