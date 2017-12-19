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
package jhelp.security.io;

import java.io.IOException;
import java.io.OutputStream;
import jhelp.util.util.Utilities;

/**
 * Stream write with "noise" (Additional bytes thats means nothing)<br>
 * <br>
 * Last modification : 16 juil. 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class NoiseOutputStream
        extends OutputStream
{
    static
    {
        SIZE_CHOICE = 32896;
        final int[] choice = new int[NoiseOutputStream.SIZE_CHOICE];
        int         index  = 0;

        for (int bit = 0; bit < 256; bit++)
        {
            for (int time = 256 - bit; time > 0; time--)
            {
                choice[index++] = bit;
            }
        }

        Utilities.scramble(choice);
        CHOICE = choice;
    }

    /**
     * Random choices
     */
    private static final int[] CHOICE;
    /**
     * Random choice size
     */
    private static final int   SIZE_CHOICE;
    /**
     * Count noise maximum value
     */
    private              int   count;

    /**
     * Actual noise value
     */
    private       int          noise;
    /**
     * Stream where write
     */
    private final OutputStream outputStream;

    /**
     * Constructs NoiseOutputStream
     *
     * @param outputStream Stream where write
     */
    public NoiseOutputStream(final OutputStream outputStream)
    {
        this.noise = 0;
        this.count = 3;
        this.outputStream = outputStream;
    }

    /**
     * Write on byte
     *
     * @param b Byte to write
     * @throws IOException On writing issue
     * @see OutputStream#write(int)
     */
    @Override
    public void write(final int b) throws IOException
    {
        while (this.noise == 0)
        {
            this.noise = (int) (Math.random() * Math.min(this.count, NoiseOutputStream.CHOICE[(int) (Math.random() *
                                                                                                     NoiseOutputStream.SIZE_CHOICE)]));
            this.outputStream.write(this.noise);

            this.count++;
        }

        this.count++;
        if (this.count > 256)
        {
            this.count = 256;
        }

        this.noise--;
        this.outputStream.write(b);
    }

    /**
     * Flush last change
     *
     * @throws IOException On flushing issue
     * @see OutputStream#flush()
     */
    @Override
    public void flush() throws IOException
    {
        this.outputStream.flush();
    }

    /**
     * Close the stream
     *
     * @throws IOException On closing issue
     * @see OutputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        this.outputStream.close();
    }
}