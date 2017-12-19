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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream we can control the reading <br>
 */
class ControlInputStream
        extends InputStream
{
    /**
     * Create a control input stream from a given input stream
     *
     * @param inputStream Input stream base
     * @return Creates control input stream
     * @throws IOException On reading problem
     */
    public static ControlInputStream createControlInputStream(final InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[]                temp                  = new byte[4096];
        int                   read                  = inputStream.read(temp);
        while (read >= 0)
        {
            byteArrayOutputStream.write(temp, 0, read);
            read = inputStream.read(temp);
        }

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        inputStream.close();

        temp = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream = null;

        final ControlInputStream controlInputStream = new ControlInputStream();
        controlInputStream.data = temp;

        temp = null;

        return controlInputStream;
    }

    /**
     * Data to read
     */
    private byte[]  data;
    /**
     * Actual read index
     */
    private int     index;
    /**
     * Last mark
     */
    private int     mark;
    /**
     * Pause status
     */
    private boolean pause;

    /**
     * Constructs ControlInputStream
     */
    private ControlInputStream()
    {
        this.index = 0;
        this.mark = 0;
        this.pause = false;
    }

    /**
     * Destroy the control input stream.<br>
     * Call it to free memory and will not continue to use it
     */
    public void destroy()
    {
        this.data = null;
    }

    /**
     * Actual position
     *
     * @return Actual position
     */
    public int getPosition()
    {
        return this.index;
    }

    /**
     * Change actual position
     *
     * @param position New position
     */
    public void setPosition(final int position)
    {
        if ((position < 0) || (position >= this.data.length))
        {
            throw new IllegalArgumentException("position must be in [0, " + this.data.length + "[ not : " + position);
        }

        this.index = position;
        this.mark = Math.min(this.mark, position);
    }

    /**
     * Indicates if control is in pause
     *
     * @return {@code true} if control is in pause
     */
    public boolean isPause()
    {
        return this.pause;
    }

    /**
     * Change pause status
     *
     * @param pause New pause status
     */
    public void setPause(final boolean pause)
    {
        this.pause = pause;
    }

    /**
     * Read a byte
     *
     * @return Byte read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#read()
     */
    @Override
    public int read()
    {
        if ((this.index >= this.data.length) || (this.pause))
        {
            return -1;
        }

        return this.data[this.index++] & 0xFF;
    }

    /**
     * Read several bytes
     *
     * @param b Array to fill
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#read(byte[])
     */
    @Override
    public int read(final byte[] b)
    {
        return this.read(b, 0, b.length);
    }

    /**
     * Read several bytes
     *
     * @param b   Array to fill
     * @param off Where start to fill
     * @param len Number of desired bytes
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#read(byte[], int, int)
     */
    @Override
    public int read(final byte[] b, final int off, int len)
    {
        if (b == null)
        {
            throw new NullPointerException();
        }
        else if ((off < 0) || (len < 0) || (len > (b.length - off)))
        {
            throw new IndexOutOfBoundsException();
        }
        else if (len == 0)
        {
            return 0;
        }

        if ((this.index >= this.data.length) || (this.pause))
        {
            return -1;
        }

        len = Math.min(len, this.data.length - this.index);
        System.arraycopy(this.data, this.index, b, off, len);
        this.index += len;
        return len;
    }

    /**
     * Skip number of bytes
     *
     * @param n Number of bytes to skip
     * @return Number of bytes really skipped
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#skip(long)
     */
    @Override
    public long skip(long n)
    {
        n = Math.min(n, this.data.length - this.index);
        this.index += n;
        return n;
    }

    /**
     * Data size
     *
     * @return Data size
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#available()
     */
    @Override
    public int available()
    {
        return this.data.length;
    }

    /**
     * Do nothing here (But keep to respect InputStream extends)
     *
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#close()
     */
    @Override
    public void close()
    {
    }

    /**
     * Mark the actual position
     *
     * @param readlimit Limit to keep
     * @see InputStream#mark(int)
     */
    @Override
    public synchronized void mark(final int readlimit)
    {
        this.mark = this.index;
    }

    /**
     * Reset to last mark
     *
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream#reset()
     */
    @Override
    public synchronized void reset()
    {
        this.index = this.mark;
    }

    /**
     * Indicates that mark are supported
     *
     * @return {@code true}
     * @see InputStream#markSupported()
     */
    @Override
    public boolean markSupported()
    {
        return true;
    }
}