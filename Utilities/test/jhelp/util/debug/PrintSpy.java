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

package jhelp.util.debug;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * Created by jhelp on 11/06/17.
 */
public class PrintSpy extends PrintStream
{
    static class SpyOutputStream extends OutputStream
    {
        private byte[] data;
        private int    size;

        SpyOutputStream()
        {
            this.data = new byte[4096];
            this.size = 0;
        }

        private void append(byte... buffer)
        {
            this.append(0, buffer.length, buffer);
        }

        private void append(int offset, int length, byte... buffer)
        {
            if (offset < 0)
            {
                length += offset;
                offset = 0;
            }

            if (offset + length > buffer.length)
            {
                length = buffer.length - offset;
            }

            if (length <= 0)
            {
                return;
            }

            this.ensureCapacity(length);
            System.arraycopy(buffer, offset, this.data, this.size, length);
            this.size += length;
        }

        private void ensureCapacity(int more)
        {
            if (this.size + more >= this.data.length)
            {
                int newLength = this.size + more;
                newLength += newLength >> 3;
                byte[] buffer = new byte[newLength];
                System.arraycopy(this.data, 0, buffer, 0, this.size);
                this.data = buffer;
            }
        }

        public String popLastWrote()
        {
            String lastWrote = new String(this.data, 0, this.size);
            this.size = 0;
            return lastWrote;
        }

        /**
         * Writes the specified byte to this output stream. The general
         * contract for <code>write</code> is that one byte is written
         * to the output stream. The byte to be written is the eight
         * low-order bits of the argument <code>b</code>. The 24
         * high-order bits of <code>b</code> are ignored.
         * <p>
         * Subclasses of <code>OutputStream</code> must provide an
         * implementation for this method.
         *
         * @param b the <code>byte</code>.
         * @throws IOException if an I/O error occurs. In particular,
         *                     an <code>IOException</code> may be thrown if the
         *                     output stream has been closed.
         */
        @Override
        public void write(final int b) throws IOException
        {
            this.append((byte) b);
        }

        @Override
        public void write(final byte[] b) throws IOException
        {
            this.append(b);
        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException
        {
            this.append(off, len, b);
        }
    }
    private final PrintStream     printStreamToSpy;
    private final SpyOutputStream spyOutputStream;

    public PrintSpy(PrintStream printStream)
    {
        super(new SpyOutputStream());

        if (printStream == null)
        {
            throw new NullPointerException("printStream MUST NOT be null");
        }

        this.printStreamToSpy = printStream;
        this.spyOutputStream = (SpyOutputStream) this.out;
    }

    @Override
    public void flush()
    {
        super.flush();
        this.printStreamToSpy.flush();
    }

    @Override
    public void close()
    {
        super.close();
        this.printStreamToSpy.close();
    }

    @Override
    public boolean checkError()
    {
        return super.checkError() || this.printStreamToSpy.checkError();
    }

    @Override
    public void write(final int b)
    {
        try
        {
            this.spyOutputStream.write(b);
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void write(final byte[] buf, final int off, final int len)
    {
        try
        {
            this.spyOutputStream.write(buf, off, len);
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void print(final boolean message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final char message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final int message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final long message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final float message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final double message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final char[] message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final String message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void print(final Object message)
    {
        super.print(message);
        this.printStreamToSpy.print(message);
    }

    @Override
    public void println()
    {
        super.println();
        this.printStreamToSpy.println();
    }

    @Override
    public void println(final boolean message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final char message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final int message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final long message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final float message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final double message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final char[] message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final String message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public void println(final Object message)
    {
        super.println(message);
        this.printStreamToSpy.println(message);
    }

    @Override
    public PrintStream printf(final String format, final Object... args)
    {
        super.printf(format, args);
        this.printStreamToSpy.printf(format, args);
        return this;
    }

    @Override
    public PrintStream printf(final Locale l, final String format, final Object... args)
    {
        super.printf(l, format, args);
        this.printStreamToSpy.printf(l, format, args);
        return this;
    }

    @Override
    public PrintStream format(final String format, final Object... args)
    {
        super.format(format, args);
        this.printStreamToSpy.format(format, args);
        return this;
    }

    @Override
    public PrintStream format(final Locale l, final String format, final Object... args)
    {
        super.format(l, format, args);
        this.printStreamToSpy.format(l, format, args);
        return this;
    }

    @Override
    public PrintStream append(final CharSequence message)
    {
        super.append(message);
        this.printStreamToSpy.append(message);
        return this;
    }

    @Override
    public PrintStream append(final CharSequence message, final int start, final int end)
    {
        super.append(message, start, end);
        this.printStreamToSpy.append(message, start, end);
        return this;
    }

    @Override
    public PrintStream append(final char message)
    {
        super.append(message);
        this.printStreamToSpy.append(message);
        return this;
    }

    public String popLastWrote()
    {
        return this.spyOutputStream.popLastWrote();
    }

    @Override
    public void write(final byte[] b) throws IOException
    {
        try
        {
            this.spyOutputStream.write(b);
        }
        catch (Exception ignored)
        {
        }
    }
}
