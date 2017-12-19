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

package jhelp.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Stream for read an array of integer<br>
 * <br>
 * Last modification : 8 mai 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class IntegerArrayInputStream
        extends InputStream
{
    /**
     * Array to read
     */
    private       int[] array;
    /**
     * Temporary bytes to read
     */
    private       int[] bytes;
    /**
     * Read index in array
     */
    private       int   index;
    /**
     * Array size
     */
    private final int   length;
    /**
     * Read index in temporary bytes
     */
    private       int   read;

    /**
     * Constructs IntegerArrayInputStream
     *
     * @param array Array to read
     */
    public IntegerArrayInputStream(final int[] array)
    {
        this.array = array;
        this.index = 0;
        this.length = this.array.length;
        this.bytes = new int[4];
        this.read = 4;
    }

    /**
     * Read one byte
     *
     * @return Byte read or -1
     * @throws IOException On read problem
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        if ((this.index >= this.length) && (this.read >= 4))
        {
            return -1;
        }

        if (this.read > 3)
        {
            final int i = this.array[this.index++];
            this.bytes[0] = (i >> 24) & 0xFF;
            this.bytes[1] = (i >> 16) & 0xFF;
            this.bytes[2] = (i >> 8) & 0xFF;
            this.bytes[3] = i & 0xFF;

            this.read = 0;
        }

        return this.bytes[this.read++];
    }

    /**
     * Close the stream
     *
     * @throws IOException On close problem
     * @see InputStream#close()
     */
    @Override
    public void close()
    {
        this.array = null;
        this.bytes = null;
    }
}