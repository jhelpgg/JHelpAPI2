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
import java.io.InputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;
import org.lwjgl.openal.AL10;

/**
 * MP3 sound
 */
public class SoundMP3 extends Sound
{
    /**
     * Sound data
     */
    private final short[] data;
    /**
     * Sound duration in millisecond
     */
    private final long    duration;
    /**
     * Sound format
     */
    private final int     format;
    /**
     * Sound frequency
     */
    private final int     frequency;

    /**
     * Create sound from stream
     *
     * @param inputStream Stream to read
     * @throws SoundException If creation failed
     */
    public SoundMP3(@NotNull InputStream inputStream) throws SoundException
    {
        try
        {
            //Decode each frames
            final Bitstream  bitStream  = new Bitstream(inputStream);
            final Decoder    decoder    = new Decoder();
            final ShortArray shortArray = new ShortArray(8192);

            while (this.decodeFrame(bitStream, decoder, shortArray))
            {
                //Nothing to do
            }

            //Collect/compute sound information
            this.data = shortArray.array();
            this.frequency = decoder.getOutputFrequency();
            final int channels = decoder.getOutputChannels();

            if (channels == 1)
            {
                this.format = AL10.AL_FORMAT_MONO16;
            }
            else if (channels == 2)
            {
                this.format = AL10.AL_FORMAT_STEREO16;
            }
            else
            {
                throw new IllegalArgumentException("Only mono OR stereo are supported, not: channels=" + channels);
            }

            this.duration = (long) ((1000.0 * this.data.length) / (this.frequency * channels));
        }
        catch (Exception exception)
        {
            throw new SoundException(exception, "Failed to create the sound");
        }
    }

    /**
     * Decode next frame
     *
     * @param bitStream  Stream to read
     * @param decoder    Decoder for decode MP3 data
     * @param shortArray Buffer to fill with decoded data
     * @return {@code true} if there more frame to read. {@code false} if their no more frame to read
     * @throws Exception If decode failed
     */
    private boolean decodeFrame(@NotNull Bitstream bitStream, @NotNull Decoder decoder, @NotNull ShortArray shortArray)
            throws Exception
    {
        final Header header = bitStream.readFrame();

        if (header == null)
        {
            return false;
        }

        final SampleBuffer sampleBuffer = (SampleBuffer) decoder.decodeFrame(header, bitStream);
        shortArray.write(sampleBuffer.getBuffer(), 0, sampleBuffer.getBufferLength());
        bitStream.closeFrame();
        return true;
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
}
