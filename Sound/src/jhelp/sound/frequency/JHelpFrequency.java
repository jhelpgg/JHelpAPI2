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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.sound.frequency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import jhelp.util.debug.Debug;
import jhelp.util.io.ByteArray;

/**
 * Play frequency
 *
 * @author JHelp
 */
public class JHelpFrequency
{
    /**
     * Maximum frequency
     */
    public static final int MAX_FREQUENCY    = 4096;
    /**
     * Volume maximum
     */
    public static final int MAX_VOLUME       = 0x7FFF;
    /**
     * Minimum frequency
     */
    public static final int MIN_FREQUENCY    = 128;
    // sourceDataLine.getFormat()=PCM_SIGNED 44100.0 Hz, 16 bit, stereo, 4 bytes/frame, little-endian
    // sourceDataLine.getBufferSize()=88200
    /**
     * Rate sample size
     */
    public static final int SAMPLE_RATE      = 8192;
    /**
     * Frequency for silent
     */
    public static final int SILENT_FREQUENCY = 4096;

    /**
     * Describe a frequency
     */
    private class FrequencyInformation
    {
        /**
         * Start frequency
         */
        final int frequency1;
        /**
         * End frequency
         */
        final int frequency2;
        /**
         * Start volume
         */
        final int volume1;
        /**
         * End volume
         */
        final int volume2;

        /**
         * Create a new instance of FrequencyInformation
         *
         * @param frequency1 Start frequency
         * @param volume1    Start volume
         * @param frequency2 End frequency
         * @param volume2    End volume
         */
        public FrequencyInformation(final int frequency1, final int volume1, final int frequency2, final int volume2)
        {
            this.frequency1 = frequency1;
            this.volume1 = volume1;
            this.frequency2 = frequency2;
            this.volume2 = volume2;
        }
    }

    /**
     * Play thread
     *
     * @author JHelp
     */
    class PlayThread
            extends Thread
    {
        /**
         * Create a new instance of PlayThread
         */
        PlayThread()
        {
        }

        /**
         * Play frequencies <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            JHelpFrequency.this.internalPlay();
        }
    }

    /**
     * Data
     */
    private final ByteArray                  byteArray;
    /**
     * Channels to play
     */
    private final List<FrequencyInformation> chanels;
    /**
     * Synchronization lock
     */
    private final Object                     lock;
    /**
     * Play thread
     */
    private       PlayThread                 playThread;
    /**
     * Start position
     */
    private       int                        startPosition;
    /**
     * Indicates if waiting for synchronization
     */
    private final AtomicBoolean              waiting;

    /**
     * Create a new instance of JHelpFrequency
     */
    public JHelpFrequency()
    {
        this.lock = new Object();
        this.waiting = new AtomicBoolean(false);
        this.startPosition = 0;
        this.chanels = new ArrayList<FrequencyInformation>();
        this.byteArray = new ByteArray();
    }

    /**
     * Play frequencies
     */
    void internalPlay()
    {
        synchronized (this.lock)
        {
            final int length = this.chanels.size();

            if ((this.startPosition == 0) || (this.startPosition >= length))
            {
                this.startPosition = 0;
                this.byteArray.clear();
                double angle1 = 0;
                double angleStep1;
                double angle2 = 0;
                double angleStep2;

                for (final FrequencyInformation frequencyInformation : this.chanels)
                {
                    angleStep1 = (Math.PI * 2.0 * (44100 - frequencyInformation.frequency1)) / 44100;
                    // JHelpFrequency.SAMPLE_RATE;
                    angleStep2 = (Math.PI * 2.0 * (44100 - frequencyInformation.frequency2)) / 44100;
                    // JHelpFrequency.SAMPLE_RATE;

                    for (int j = 0; j < 4410; j++)
                    {
                        this.byteArray.writeShortLittleEndian(
                                (short) ((frequencyInformation.volume1 * JHelpFrequency.MAX_VOLUME * 0.01 *
                                          Math.sin(angle1))));
                        this.byteArray.writeShortLittleEndian(
                                (short) ((frequencyInformation.volume2 * JHelpFrequency.MAX_VOLUME * 0.01 *
                                          Math.sin(angle2))));

                        angle1 += angleStep1;
                        angle2 += angleStep2;
                    }
                }
            }
        }

        this.byteArray.readFromStart();
        this.byteArray.skip(this.startPosition * 64);

        this.byteArray.available();
        // final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, JHelpFrequency.SAMPLE_RATE,
        // JHelpFrequency.SAMPLE_SIZE_IN_BITS, JHelpFrequency.NUMBER_CHANELS, JHelpFrequency.FRAME_SIZE,
        // JHelpFrequency.FRAME_RATE, true);
        final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100 * 2, 16, 2, 4, 44100,
                                                        false);

        try
        {
            final DataLine.Info  dataLineInfo   = new DataLine.Info(SourceDataLine.class, audioFormat, 44100);
            final Line           line           = AudioSystem.getLine(dataLineInfo);
            final SourceDataLine sourceDataLine = (SourceDataLine) line;
            sourceDataLine.open(audioFormat, this.millisecondsToBytes(audioFormat, 2000));
            sourceDataLine.start();
            sourceDataLine.write(this.byteArray.toArray(), 0, this.byteArray.getSize());
            sourceDataLine.drain();
            sourceDataLine.stop();
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Issue while playing frequency");

            for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
            {
                Debug.mark(mixerInfo.toString());
                final Mixer mixer = AudioSystem.getMixer(mixerInfo);

                for (final Line.Info lineInfo : mixer.getSourceLineInfo())
                {
                    Debug.information(lineInfo.toString());
                    Debug.information("LineClass=", lineInfo.getLineClass());

                    try
                    {
                        final Line           line           = AudioSystem.getLine(lineInfo);
                        final SourceDataLine sourceDataLine = (SourceDataLine) line;
                        Debug.information("sourceDataLine=", sourceDataLine);
                        Debug.information("sourceDataLine.getFormat()=", sourceDataLine.getFormat());
                        Debug.information("sourceDataLine.getBufferSize()=", sourceDataLine.getBufferSize());
                    }
                    catch (final Exception ignored)
                    {
                    }

                    Debug.information("*   ---   *");
                }
            }
        }

        // final AudioInputStream audioInputStream = new AudioInputStream(this.byteArray.getInputStream(), audioFormat, length);
        //
        // try
        // {
        // new Line.Info(this.getClass());
        //
        // // Create clip for play sound
        // final Clip clip = AudioSystem.getClip();
        // clip.open(audioInputStream);
        // clip.start();
        //
        // synchronized(this.lock)
        // {
        // this.waiting.set(true);
        //
        // try
        // {
        // this.lock.wait(sleep);
        // }
        // catch(final Exception exception)
        // {
        // }
        //
        // this.waiting.set(false);
        // this.startPosition += clip.getFramePosition() >> 6;
        // }
        //
        // clip.stop();
        // clip.close();
        // }
        // catch(final Exception exception)
        // {
        // Debug.printException(exception, "Issue while playing frequency");
        // }
    }

    /**
     * Add a frequency
     *
     * @param frequencyChanel1 Start channel
     * @param volume1          Start volume
     * @param frequencyChanel2 End channel
     * @param volume2          End volume
     */
    public void addFrequency(int frequencyChanel1, int volume1, int frequencyChanel2, int volume2)
    {
        synchronized (this.lock)
        {
            if (this.playThread != null)
            {
                throw new IllegalStateException("Can't add frequency while playing");
            }
        }

        frequencyChanel1 = Math.max(JHelpFrequency.MIN_FREQUENCY,
                                    Math.min(JHelpFrequency.MAX_FREQUENCY, frequencyChanel1));
        frequencyChanel2 = Math.max(JHelpFrequency.MIN_FREQUENCY,
                                    Math.min(JHelpFrequency.MAX_FREQUENCY, frequencyChanel2));
        volume1 = Math.max(0, Math.min(100, volume1));
        volume2 = Math.max(0, Math.min(100, volume2));

        if (frequencyChanel1 == JHelpFrequency.SILENT_FREQUENCY)
        {
            volume1 = 0;
        }

        if (frequencyChanel2 == JHelpFrequency.SILENT_FREQUENCY)
        {
            volume2 = 0;
        }

        synchronized (this.lock)
        {
            this.startPosition = 0;
            this.chanels.add(new FrequencyInformation(frequencyChanel1, volume1, frequencyChanel2, volume2));
        }
    }

    /**
     * Obtain the number o bytes necessary for play a given duration
     *
     * @param paramAudioFormat Audio format to use
     * @param paramInt         Number of milliseconds
     * @return Buffer size
     */
    public int millisecondsToBytes(final AudioFormat paramAudioFormat, final int paramInt)
    {
        return (int) ((paramInt * (paramAudioFormat.getSampleRate() * paramAudioFormat.getChannels() *
                                   paramAudioFormat.getSampleSizeInBits()))
                      / (double) JHelpFrequency.SAMPLE_RATE);
    }

    /**
     * Play the sound
     */
    public void play()
    {
        synchronized (this.lock)
        {
            if (this.playThread == null)
            {
                this.playThread = new PlayThread();
                this.playThread.start();
            }
        }
    }

    /**
     * Reset the sound
     */
    public void resetSound()
    {
        this.stop();

        synchronized (this.lock)
        {
            this.startPosition = 0;
        }
    }

    /**
     * Stop the sound
     */
    public void stop()
    {
        synchronized (this.lock)
        {
            if (this.playThread != null)
            {
                this.playThread = null;

                if (this.waiting.get())
                {
                    this.lock.notify();
                }
            }
        }
    }
}