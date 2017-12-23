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
import com.sun.media.sound.WaveFileReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.lwjgl.openal.AL10;

/**
 * Wav sound.<br>
 * Warning not all wav are managed, only those managed "natively" by Java
 */
public class SoundWav extends Sound
{
    /**
     * Transfer audio data array in byte array
     *
     * @param audioBytes   Audio data to transfer
     * @param twoBytesData Indicates if data are 2 bytes per part
     * @param order        Oder to use for transfer
     * @return Filled properly audio data
     */
    private static ByteBuffer convertAudioBytes(
            @NotNull byte[] audioBytes, boolean twoBytesData, @NotNull ByteOrder order)
    {
        final ByteBuffer destination = ByteBuffer.allocateDirect(audioBytes.length);
        destination.order(ByteOrder.nativeOrder());
        final ByteBuffer source = ByteBuffer.wrap(audioBytes);
        source.order(order);

        if (twoBytesData)
        {
            final ShortBuffer destinationShort = destination.asShortBuffer();
            final ShortBuffer sourceShort      = source.asShortBuffer();

            while (sourceShort.hasRemaining())
            {
                destinationShort.put(sourceShort.get());
            }
        }
        else
        {
            while (source.hasRemaining())
            {
                destination.put(source.get());
            }
        }

        destination.rewind();
        return destination;
    }

    /**
     * Creates a SoundWav from the specified input stream
     *
     * @param inputStream InputStream to read from
     * @return Created SoundWav
     * @throws SoundException On creation issue
     */
    public static @NotNull SoundWav create(@NotNull InputStream inputStream) throws SoundException
    {
        try
        {
            return SoundWav.create(AudioSystem.getAudioInputStream(inputStream));
        }
        catch (Exception exception)
        {
            throw new SoundException(exception, "Failed to create the sound wav");
        }
    }

    /**
     * Creates a SoundWav from the specified stream
     *
     * @param audioInputStream AudioInputStream to read from
     * @return Created SoundWav
     * @throws SoundException On creation issue
     */
    public static @NotNull SoundWav create(@NotNull AudioInputStream audioInputStream) throws SoundException
    {
        //get format of data
        AudioFormat audioformat = audioInputStream.getFormat();

        // get channels
        int channels;
        int nbChannels = audioformat.getChannels();
        int sampleSize = audioformat.getSampleSizeInBits();

        if (nbChannels == 1)
        {
            if (sampleSize == 8)
            {
                channels = AL10.AL_FORMAT_MONO8;
            }
            else if (sampleSize == 16)
            {
                channels = AL10.AL_FORMAT_MONO16;
            }
            else
            {
                throw new SoundException("Only sample size of 8 or 16 are managed, not ", sampleSize);
            }
        }
        else if (nbChannels == 2)
        {
            if (sampleSize == 8)
            {
                channels = AL10.AL_FORMAT_STEREO8;
            }
            else if (sampleSize == 16)
            {
                channels = AL10.AL_FORMAT_STEREO16;
            }
            else
            {
                throw new SoundException("Only sample size of 8 or 16 are managed, not ", sampleSize);
            }
        }
        else
        {
            throw new SoundException("Only mono or stereo are managed, nbChannels=", nbChannels);
        }

        //read data into buffer
        ByteBuffer buffer;
        int        available;

        try
        {
            available = audioInputStream.available();

            if (available <= 0)
            {
                final AudioFormat audioFormat = audioInputStream.getFormat();
                available = audioFormat.getChannels() * (int) audioInputStream.getFrameLength() *
                            audioFormat.getSampleSizeInBits() / 8;
            }

            final byte[] tempBuffer = new byte[available];
            int          total      = 0;
            int          read       = audioInputStream.read(tempBuffer, total, tempBuffer.length - total);

            while (read != -1 && total < tempBuffer.length)
            {
                total += read;
                read = audioInputStream.read(tempBuffer, total, tempBuffer.length - total);
            }

            buffer = SoundWav.convertAudioBytes(tempBuffer, audioformat.getSampleSizeInBits() == 16,
                                                audioformat.isBigEndian() ? ByteOrder.BIG_ENDIAN
                                                                          : ByteOrder.LITTLE_ENDIAN);
        }
        catch (Exception exception)
        {
            throw new SoundException(exception, "Failed to read the given stream");
        }

        //create our result
        final SoundWav soundWav =
                new SoundWav(buffer, channels, (int) audioformat.getSampleRate(), available, nbChannels, sampleSize);

        //close stream
        try
        {
            audioInputStream.close();
        }
        catch (IOException ignored)
        {
        }

        return soundWav;
    }

    /**
     * Creates a SoundWWav container from the specified url
     *
     * @param path URL to file
     * @return Created SoundWav
     * @throws SoundException On creation issue
     */
    public static @NotNull SoundWav create(@NotNull URL path) throws SoundException
    {
        try
        {
            // due to an issue with AudioSystem.getAudioInputStream
            // and mixing unsigned and signed code
            // we will use the reader directly
            final WaveFileReader waveFileReader = new WaveFileReader();
            return SoundWav.create(waveFileReader.getAudioInputStream(new BufferedInputStream(path.openStream())));
        }
        catch (Exception exception)
        {
            throw new SoundException(exception, "Failed to created sound from URL: ", path);
        }
    }

    /**
     * Sound wav data
     */
    private final ByteBuffer data;
    /**
     * Sound duration in milliseconds
     */
    private final long       duration;
    /**
     * SSound format
     */
    private final int        format;
    /**
     * Sound frequency
     */
    private final int        frequency;
    /**
     * Indicates if sound is valid
     */
    private       boolean    valid;

    /**
     * Create the sound wav
     *
     * @param data       Sound data
     * @param format     Sound format
     * @param frequency  Sound frequency
     * @param length     Sound data length
     * @param channels   Number of channels
     * @param sampleSize Sample size (8 or 16)
     */
    private SoundWav(
            final @NotNull ByteBuffer data, final int format, final int frequency, int length, int channels,
            int sampleSize)
    {
        this.valid = true;
        this.data = data;
        this.format = format;
        this.frequency = frequency;
        this.duration = (long) ((8000.0 * length) / (this.frequency * channels * sampleSize));
    }

    /**
     * Transfer data to a buffer to play sound in OpenAL
     *
     * @param buffer Buffer where put data
     * @return {@code true} if transfer succeed
     */
    @Override
    boolean transferToBuffer(final int buffer)
    {
        if (!this.valid)
        {
            return false;
        }

        AL10.alBufferData(buffer, this.format, this.data, this.frequency);
        return true;
    }

    /**
     * Sound duration in milliseconds
     *
     * @return Sound duration in milliseconds
     */
    @Override
    public long duration()
    {
        return this.duration;
    }

    /**
     * Called by JVM when free space
     *
     * @throws Throwable On issue
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.dispose();
        super.finalize();
    }

    /**
     * Disposes the data sound.<br>
     * Sound can't be used after that
     */
    public void dispose()
    {
        this.data.clear();
        this.valid = false;
    }
}
