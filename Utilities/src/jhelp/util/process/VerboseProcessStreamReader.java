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

package jhelp.util.process;

import java.io.IOException;
import jhelp.util.debug.Debug;

public class VerboseProcessStreamReader implements ProcessStreamReader
{
    public static final VerboseProcessStreamReader VERBOSE_PROCESS_STREAM_READER = new VerboseProcessStreamReader();

    private VerboseProcessStreamReader()
    {
    }

    @Override
    public void endRead()
    {
        Debug.verbose("Process end read!");
    }

    @Override
    public void readIssue(final IOException exception)
    {
        Debug.exception(exception, "Issue while executing task!");
    }

    @Override
    public void readLine(final String line)
    {
        Debug.verbose("Line read: ", line);
    }
}
