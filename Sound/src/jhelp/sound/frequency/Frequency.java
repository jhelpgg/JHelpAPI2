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

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import jhelp.util.debug.Debug;
import jhelp.util.util.Utilities;

@SuppressWarnings("PointlessArithmeticExpression")
public class Frequency
        extends InputStream
{

    static
    {
        final int shift = 2;
        final int[] base = new int[]
                {
                        (6 << shift) - 1, (7 << shift) - 1, (8 << shift) - 1, (7 << shift) - 1, (7 << shift) - 1,
                        (6 << shift) - 1, (7 << shift) - 1, (8 << shift) - 1,//
                        (7 << shift) - 1, (7 << shift) - 1, (5 << shift) - 1, (5 << shift) - 1, (5 << shift) - 1,
                        (5 << shift) - 1, (4 << shift) - 1, (4 << shift) - 1,//
                        (4 << shift) - 1, (4 << shift) - 1, (3 << shift) - 1, (3 << shift) - 1, (3 << shift) - 1,
                        (3 << shift) - 1
                };
        final int length = base.length;
        final int repeat = 1;
        final int blank  = 0;
        NOTE_LENGTH = length * (repeat + blank);
        NOTE = new int[Frequency.NOTE_LENGTH];
        int index = 0;

        for (int aBase : base)
        {
            for (int j = 0; j < repeat; j++)
            {
                Frequency.NOTE[index++] = aBase;
            }

            //noinspection ConstantConditions
            for (int j = 0; j < blank; j++)
            {
                Frequency.NOTE[index++] = 0;
            }
        }

        // NOTE_LENGTH = 256;
        // NOTE = new int[Frequency.NOTE_LENGTH];
        //
        // for(int i = 0; i < Frequency.NOTE_LENGTH; i++)
        // {
        // Frequency.NOTE[i] = i;
        // }

    }

    static              int seq = 1000;
    public static final int A4  = Float.floatToIntBits(440.00f) & 0x7FFFFFFF;
    public static final int B4  = Float.floatToIntBits(493.88f) & 0x7FFFFFFF;
    public static final int[] NOTE;
    public static final int   NOTE_LENGTH;

    public static void main(final String[] args)
    {
        // final JHelpSound sound = SoundFactory.getSoundFromFile(new
        // File("/home/jhelp/WaveFun/imported_signed_8bit_stereo_8Khz.wav"));
        //
        // sound.play();
        //
        // while(sound.playing() == true)
        // {
        // Utilities.sleep(1024);
        // }
        //
        // sound.destroy();

        //

        // 24/02/2013 : 10h24m46s887ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 110 : info : interface Clip supporting
        // format PCM_UNSIGNED 8000.0 Hz, 8 bit, stereo, 2 bytes/frame, , and buffers of 14118 to 14118 bytes
        // 24/02/2013 : 10h24m46s890ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 111 : encoding : PCM_UNSIGNED
        // 24/02/2013 : 10h24m46s891ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 112 : sample rate : 8000.0
        // 24/02/2013 : 10h24m46s896ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 113 : sample size : 8
        // 24/02/2013 : 10h24m46s897ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 114 : chanel : 2
        // 24/02/2013 : 10h24m46s899ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 115 : frame size : 2
        // 24/02/2013 : 10h24m46s900ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 116 : frame rate : 8000.0
        // 24/02/2013 : 10h24m46s901ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 117 : big endian : false
        // 24/02/2013 : 10h24m46s902ms : VERBOSE : jhelp.sound.other.SoundOther.<init> at 118 : buffer size : 14118

        //

        try
        {

            final int number        = /* 512;/ */8192;
            final int numberSeconds = 4;
            final int length        = number * numberSeconds;
            final int sleep         = (length * 1000) / number;
            final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, number, 8, 2, 1,
                                                            number * 16, true);

            final AudioInputStream audioInputStream = new AudioInputStream(new Frequency(), audioFormat, length);

            // Create clip for play sound
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            Utilities.sleep(sleep >> 1);
            Debug.information(clip.getFramePosition(), '/', clip.getFrameLength());

            clip.stop();
            clip.close();
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Issue while try play A 440Hz");
        }
    }

    private int mark      = 0;
    private int readIndex = 0;

    @Override
    public int read() throws IOException
    {
        final byte[] b = new byte[4];
        //noinspection ResultOfMethodCallIgnored
        this.read(b);
        return b[0] & 0xFF;
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b
     * @return
     * @throws IOException
     * @see InputStream#read(byte[])
     */
    @Override
    public int read(final byte[] b) throws IOException
    {
        return this.read(b, 0, b.length);
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b
     * @param off
     * @param len
     * @return
     * @throws IOException
     * @see InputStream#read(byte[], int, int)
     */
    @Override
    public int read(final byte[] b, int off, final int len) throws IOException
    {
        double       angle      = 0;
        final double length     = 8192.0 / 440;
        final double angleStep  = (Math.PI * 2) / length;
        double       angle2     = 0;
        final double length2    = 8192.0 / 440;
        final double angleStep2 = (Math.PI * 2) / length2;
        final int    limit      = off + len;

        while (off < limit)
        {
            b[off++] = (byte) (128 + (127 * Math.sin(angle)));
            angle += angleStep;
            b[off++] = (byte) (128 + (127 * Math.sin(angle2)));
            angle2 += angleStep2;
        }

        // len = len & 0xFFFFFFFC;
        //
        // if(len < 4)
        // {
        // return 0;
        // }
        //
        // int index = off;
        // final int limit = index + len;
        // int noteIndex = this.readIndex % Frequency.NOTE_LENGTH;
        // int note;
        // int wave = 0;
        //
        // b[index++] = (byte) 0;
        // b[index++] = (byte) 255;
        // b[index++] = (byte) wave;
        // b[index++] = (byte) 255;
        //
        // while(index < limit)
        // {
        // note = Frequency.NOTE[noteIndex];
        // noteIndex = (noteIndex + 1) % Frequency.NOTE_LENGTH;
        //
        // if((wave + note) > 255)
        // {
        // if((wave - note) < 0)
        // {
        // wave = (wave + note) % 256;
        // }
        // else
        // {
        // wave -= note;
        // }
        // }
        // else
        // {
        // wave += note;
        // }
        //
        // // b[index++] = (byte) 0;
        // // b[index++] = (byte) 255;
        // b[index++] = (byte) ((wave * 1) + (note * 0));
        // b[index++] = (byte) 255;
        // }
        //
        // this.readIndex += len;

        return len;
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param n
     * @return
     * @throws IOException
     * @see InputStream#skip(long)
     */
    @Override
    public long skip(final long n) throws IOException
    {
        this.readIndex += n;
        return n;
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException
     * @see InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param readlimit
     * @see InputStream#mark(int)
     */
    @Override
    public synchronized void mark(final int readlimit)
    {
        this.mark = this.readIndex;
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException
     * @see InputStream#reset()
     */
    @Override
    public synchronized void reset() throws IOException
    {
        this.readIndex = this.mark;
        this.mark = 0;
    }

    /**
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return
     * @see InputStream#markSupported()
     */
    @Override
    public boolean markSupported()
    {
        return true;
    }
}