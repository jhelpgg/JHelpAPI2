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
package jhelp.sound.midi;

import java.io.File;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import jhelp.sound.Sound;
import jhelp.sound.SoundException;
import jhelp.sound.SoundListener;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Sound plays MIDI files
 */
public class SoundMidi
        implements Sound
{
    /**
     * Actual sequencer plays the MIDI
     */
    private Sequencer sequencer;
    /**
     * alive state
     */
    boolean alive;
    /**
     * Lock for synchronize the alive state
     */
    final Object lock = new Object();
    /**
     * Sound listener
     */
    SoundListener soundListener;

    /**
     * Wait that the sound end, and signal to listeners, when its done
     */
    private final RunnableTask waitForSoundEnd = new RunnableTask()
    {
        /**
         * Do the waiting task <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()                                                             */
        @Override
        public void run()
        {
            if (SoundMidi.this.playing())
            {
                ThreadManager.parallel(this, 123);

                return;
            }

            synchronized (SoundMidi.this.lock)
            {
                SoundMidi.this.alive = false;
            }

            if (SoundMidi.this.soundListener != null)
            {
                SoundMidi.this.soundListener.soundEnd();
            }
        }
    };

    /**
     * Constructs SoundMidi
     *
     * @param file File MIDI
     * @throws SoundException On creation problem
     */
    public SoundMidi(final File file)
            throws SoundException
    {
        try
        {
            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.setSequence(MidiSystem.getSequence(file));
            this.sequencer.open();
        }
        catch (final Exception exception)
        {
            throw new SoundException(exception, "Creation sound error");
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
        synchronized (this.lock)
        {
            this.alive = false;
        }

        if (this.sequencer != null)
        {
            this.sequencer.stop();
            this.sequencer.close();
        }
        this.sequencer = null;

        this.soundListener = null;
    }

    /**
     * play the sound
     *
     * @see jhelp.sound.Sound#play()
     */
    @Override
    public void play()
    {
        if (this.sequencer == null)
        {
            return;
        }

        this.sequencer.start();

        synchronized (this.lock)
        {
            if (!this.alive)
            {
                this.alive = true;

                ThreadManager.parallel(this.waitForSoundEnd);
            }
        }
    }

    /**
     * Indicates that sound is playing
     *
     * @return {@code true} if sound is playing
     * @see jhelp.sound.Sound#playing()
     */
    @Override
    public boolean playing()
    {
        if (this.sequencer != null)
        {
            return this.sequencer.isRunning();
        }

        return false;
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
        if (this.sequencer != null)
        {
            return this.sequencer.getMicrosecondPosition();
        }

        return 0;
    }

    /**
     * Change sound position
     *
     * @param position New position
     * @see jhelp.sound.Sound#position(long)
     */
    @Override
    public void position(final long position)
    {
        if (this.sequencer == null)
        {
            return;
        }

        this.sequencer.setMicrosecondPosition(position);
    }

    /**
     * Defines sound listener
     *
     * @param soundListener Sound listener
     * @see jhelp.sound.Sound#soundListener(jhelp.sound.SoundListener)
     */
    @Override
    public void soundListener(final SoundListener soundListener)
    {
        this.soundListener = soundListener;
    }

    /**
     * Stop the sound
     *
     * @see jhelp.sound.Sound#stop()
     */
    @Override
    public void stop()
    {
        if (this.sequencer == null)
        {
            return;
        }

        this.sequencer.stop();
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
        if (this.sequencer != null)
        {
            return this.sequencer.getMicrosecondLength();
        }

        return 0;
    }
}