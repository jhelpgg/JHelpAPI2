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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import jhelp.util.text.UtilText;

/**
 * Stream for read a {@link String}
 *
 * @author JHelp
 */
public class StringInputStream
        extends InputStream
{
    /**
     * Stream for read {@link String} bytes
     */
    private final ByteArrayInputStream byteArrayInputStream;

    /**
     * Create a new instance of StringInputStream
     *
     * @param string {@link String} to read
     */
    public StringInputStream(final String string)
    {
        final byte[] array = UtilText.toUTF8(string);
        this.byteArrayInputStream = new ByteArrayInputStream(array);
    }

    /**
     * Read one byte <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Byte read or -1 if reach stream end
     * @throws IOException On reading issue
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        return this.byteArrayInputStream.read();
    }

    /**
     * Read some bytes and fill an array.<br>
     * Same as {@link #read(byte[], int, int) read(b, 0, b.length)} <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b Array to fill
     * @return Number of read bytes or -1 if reach stream end
     * @throws IOException On reading issue
     * @see InputStream#read(byte[])
     */
    @Override
    public int read(final byte[] b) throws IOException
    {
        return this.byteArrayInputStream.read(b);
    }

    /**
     * Read some bytes and fill a part of array <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param array  Array to fill
     * @param offset Offset to start write in array
     * @param length Number of byte to read
     * @return Number of read bytes or -1 if reach stream end
     * @throws IOException On reading issue
     * @see InputStream#read(byte[], int, int)
     */
    @Override
    public int read(final byte[] array, final int offset, final int length) throws IOException
    {
        return this.byteArrayInputStream.read(array, offset, length);
    }

    /**
     * Skip a number of bytes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param n Number of bytes to skip
     * @return Number of skipped bytes
     * @throws IOException On skipping issue
     * @see InputStream#skip(long)
     */
    @Override
    public long skip(final long n)
    {
        return this.byteArrayInputStream.skip(n);
    }

    /**
     * Number of available bytes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Number of available bytes
     * @throws IOException On reading issue
     * @see InputStream#available()
     */
    @Override
    public int available()
    {
        return this.byteArrayInputStream.available();
    }

    /**
     * Close the stream <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException On closing issue
     * @see InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        this.byteArrayInputStream.close();
    }

    /**
     * Mark current reading position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param readLimit Read limit
     * @see InputStream#mark(int)
     */
    @Override
    public synchronized void mark(final int readLimit)
    {
        this.byteArrayInputStream.mark(readLimit);
    }

    /**
     * Reset the stream <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException On resting issue
     * @see InputStream#reset()
     */
    @Override
    public synchronized void reset()
    {
        this.byteArrayInputStream.reset();
    }

    /**
     * Indicates if mark are supported <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} if mark are supported
     * @see InputStream#markSupported()
     */
    @Override
    public boolean markSupported()
    {
        return this.byteArrayInputStream.markSupported();
    }
}