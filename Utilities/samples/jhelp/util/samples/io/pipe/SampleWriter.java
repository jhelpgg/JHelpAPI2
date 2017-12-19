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

package jhelp.util.samples.io.pipe;

import java.io.File;
import jhelp.util.debug.Debug;
import jhelp.util.io.pipe.PipeWriter;

public class SampleWriter
{

    /**
     * TODO Explains what does the method main in jhelp.util.samples.io.pipe [JHelpUtil]
     *
     * @param args
     */
    public static void main(final String[] args)
    {
        // final Properties properties = System.getProperties();
        // for(final Entry<Object, Object> entry : properties.entrySet())
        // {
        // Debug.println(DebugLevel.INFORMATION, entry.getKey(), '=', entry.getValue(), " [", entry.getValue() != null
        // ? entry.getValue().getClass().getName()
        // : "NULL", ']');
        // }

        final PipeWriter pipeWriter = new PipeWriter(new File("/home/jhelp/pipeSample"));

        for (int i = 0; i < 2048; i++)
        {
            try
            {
                Debug.information(
                        "WRITE:", "Message numéro ", i);
                pipeWriter.write(("Message numéro " + i).getBytes());
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Issue on writing message : ", i);
            }
        }

        try
        {
            Debug.information("WRITE:--END--");
            pipeWriter.write("--END--".getBytes());
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Issue on writing message : ", "--END--");
        }
    }
}