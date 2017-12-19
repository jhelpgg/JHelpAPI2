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
import java.io.InputStream;

/**
 * Stream to read "noise" stream create with {@link NoiseOutputStream}
 */
public class NoiseInputStream
        extends InputStream
{
    /**
     * Stream to read
     */
    private final InputStream inputStream;
    /**
     * Actual noise
     */
    private       int         noise;

    /**
     * Constructs NoiseInputStream
     *
     * @param inputStream Stream to read
     */
    public NoiseInputStream(final InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.noise = 0;
    }

    /**
     * Read one byte
     *
     * @return Byte read
     * @throws IOException On read issue
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        while (this.noise == 0)
        {
            this.noise = this.inputStream.read();
        }

        this.noise--;

        return this.inputStream.read();
    }

    /**
     * Close the stream
     *
     * @throws IOException On closing issue
     * @see InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        this.inputStream.close();
    }
}