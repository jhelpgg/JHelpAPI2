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

package jhelp.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import javax.swing.JTextArea;
import jhelp.util.debug.Debug;

/**
 * Stream for print console message ({@link System#out}) inside a text area (use it with {@link System#setOut(PrintStream)})
 *
 * @author JHelp <br>
 */
public class ConsolePrintStream

{
    /**
     * Stream it self
     *
     * @author JHelp <br>
     */
    class ConsoleOutputStream
            extends OutputStream
    {
        /**
         * Indicates if receives bytes are UTF-8 Strings or not
         */
        private final boolean utf8;

        /**
         * Create a new instance of ConsoleOutputStream
         *
         * @param utf8 Indicates if receives bytes are UTF-8 Strings or not
         */
        ConsoleOutputStream(final boolean utf8)
        {
            this.utf8 = utf8;
        }

        /**
         * Write one byte <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param b Byte to write
         * @throws IOException Guarantee to not happen here
         * @see OutputStream#write(int)
         */
        @Override
        public void write(final int b)
        {
            ConsolePrintStream.this.append(String.valueOf((char) b));
        }

        /**
         * Write a byte array <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param b Array to write
         * @throws IOException Guarantee to not happen here
         * @see OutputStream#write(byte[])
         */
        @Override
        public void write(final byte[] b) throws IOException
        {
            this.write(b, 0, b.length);
        }

        /**
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param b   Array to write
         * @param off Offset in array to start write
         * @param len number of byte to write
         * @throws IOException Guarantee to not happen here
         * @see OutputStream#write(byte[], int, int)
         */
        @Override
        public void write(final byte[] b, int off, int len) throws IOException
        {
            if (off < 0)
            {
                len += off;
                off = 0;
            }

            len = Math.min(len, b.length - off);

            if (len <= 0)
            {
                return;
            }

            if (this.utf8)
            {
                ConsolePrintStream.this.append(new String(b, off, len, "UTF-8"));
            }
            else
            {
                ConsolePrintStream.this.append(new String(b, off, len));
            }
        }
    }

    /**
     * Text area where write
     */
    private final JTextArea   console;
    /**
     * Stream use for write in text area
     */
    private       PrintStream printStream;

    /**
     * Create a new instance of ConsolePrintStream
     *
     * @param console Text area where write
     */
    public ConsolePrintStream(final JTextArea console)
    {
        if (console == null)
        {
            throw new NullPointerException("console mustn't be null");
        }

        this.console = console;

        try
        {
            this.printStream = new PrintStream(new ConsoleOutputStream(true), true, "UTF-8");
        }
        catch (final UnsupportedEncodingException exception)
        {
            Debug.exception(exception);
            this.printStream = new PrintStream(new ConsoleOutputStream(false), true);
        }
    }

    /**
     * Append text to text area
     *
     * @param text Text append
     */
    void append(final String text)
    {
        this.console.append(text);
    }

    /**
     * Print stream to use for write in text area.<br>
     * Can be used with {@link System#setOut(PrintStream)}, {@link System#setErr(PrintStream)}
     *
     * @return Print stream to use
     */
    public PrintStream printStream()
    {
        return this.printStream;
    }
}