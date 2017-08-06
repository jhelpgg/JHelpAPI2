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

import com.sun.istack.internal.NotNull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import jhelp.util.debug.Debug;
import jhelp.util.text.StringCutter;

/**
 * Helper for write text in output stream.<br>
 * It automatically flush and close when issue on writing happen
 */
public class TextWriter
{
    /**
     * Embed writer where write
     */
    private BufferedWriter bufferedWriter;

    /**
     * Create the writer
     *
     * @param outputStream Stream where write
     */
    public TextWriter(@NotNull OutputStream outputStream)
    {
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * Create the writer
     *
     * @param writer Writer where write
     */
    public TextWriter(@NotNull Writer writer)
    {
        this.bufferedWriter = new BufferedWriter(writer);
    }

    /**
     * Flush and close the writer.<br>
     * Does nothing if writer was already closed
     */
    public void close()
    {
        if (this.bufferedWriter != null)
        {
            try
            {
                this.bufferedWriter.flush();
            }
            catch (Exception ignored)
            {
            }

            try
            {
                this.bufferedWriter.close();
            }
            catch (Exception ignored)
            {
            }
        }

        this.bufferedWriter = null;
    }

    /**
     * Indicates if writer is closed.<br>
     * Nothing will be write if close
     *
     * @return {@code true} if writer is closed.
     */
    public boolean closed()
    {
        return this.bufferedWriter == null;
    }

    /**
     * Print a message<br>
     * Does nothing if writer is closed
     *
     * @param message Message to write
     */
    public void print(@NotNull String message)
    {
        if (this.bufferedWriter != null)
        {
            try
            {
                StringCutter stringCutter = new StringCutter(message, '\n');
                String       part         = stringCutter.next();

                while (part != null)
                {
                    this.bufferedWriter.write(part);
                    part = stringCutter.next();

                    if (part != null)
                    {
                        this.bufferedWriter.newLine();
                    }
                }
            }
            catch (IOException ioException)
            {
                this.close();
                Debug.exception(ioException, "Failed to write message");
            }
        }
    }

    /**
     * Print a message and Writes a line separator.<br>
     * Does nothing if writer is closed
     *
     * @param message Message to write
     */
    public void println(@NotNull String message)
    {
        this.print(message);
        this.println();
    }

    /**
     * Writes a line separator.
     * The line separator string is defined by the system property line.separator,
     * and is not necessarily a single newline ('\n') character.<br>
     * Does nothing if writer is closed
     */
    public void println()
    {
        if (this.bufferedWriter != null)
        {
            try
            {
                this.bufferedWriter.newLine();
            }
            catch (IOException ioException)
            {
                this.close();
                Debug.exception(ioException, "Failed to write new line");
            }
        }
    }
}
