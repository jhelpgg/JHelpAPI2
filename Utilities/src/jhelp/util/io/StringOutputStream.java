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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jhelp.util.text.UtilText;

/**
 * Stream for write in a {@link String}
 *
 * @author JHelp
 */
public class StringOutputStream
        extends OutputStream
{
    /**
     * Stream where write bytes
     */
    private final ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Create a new instance of StringOutputStream
     */
    public StringOutputStream()
    {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    /**
     * The written read string
     *
     * @return The written read string
     */
    public String getString()
    {
        final byte[] array = this.byteArrayOutputStream.toByteArray();
        return UtilText.readUTF8(array, 0, array.length);
    }

    /**
     * Write one byte <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b Byte to write
     * @throws IOException On writing issue
     * @see OutputStream#write(int)
     */
    @Override
    public void write(final int b)
    {
        this.byteArrayOutputStream.write(b);
    }

    /**
     * Write a byte array.<br>
     * Same as {@link #write(byte[], int, int) write(b, 0, b.length)} <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b Array to write
     * @throws IOException On writing issue
     * @see OutputStream#write(byte[])
     */
    @Override
    public void write(final byte[] b) throws IOException
    {
        this.byteArrayOutputStream.write(b);
    }

    /**
     * Write a part of byte array <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param b   Array to write
     * @param off Offset to start reading the array
     * @param len Number of bytes to write
     * @throws IOException On writing issue
     * @see OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
    {
        this.byteArrayOutputStream.write(b, off, len);
    }

    /**
     * Flush the last bytes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException On flushing issue
     * @see OutputStream#flush()
     */
    @Override
    public void flush() throws IOException
    {
        this.byteArrayOutputStream.flush();
    }

    /**
     * Close the stream <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws IOException On closing issue
     * @see OutputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        this.byteArrayOutputStream.close();
    }
}