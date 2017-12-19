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
import jhelp.util.io.pipe.PipeReader;

public class SampleReader
{

    public static void main(final String[] args)
    {
        final PipeReader pipeReader = new PipeReader(new File("/home/jhelp/pipeSample"));

        String message = null;
        do
        {
            try
            {
                message = new String(pipeReader.read());
                Debug.information("READ:", message);
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Issue on reading");
            }
        }
        while ("--END--".equals(message) == false);
    }
}