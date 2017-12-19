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
import java.util.List;
import java.util.Objects;
import jhelp.util.list.ArrayObject;
import jhelp.util.thread.ThreadManager;

public class ProcessManager
{
    public static int callCommand(String command, String... parameters)
            throws IOException, InterruptedException
    {
        return ProcessManager.callCommand(null, null, command, parameters);
    }

    public static int callCommand(final ProcessStreamReader defaultStream, String command, String... parameters)
            throws IOException, InterruptedException
    {
        return ProcessManager.callCommand(defaultStream, null, command, parameters);
    }

    public static int callCommand(
            final ProcessStreamReader defaultStream, final ProcessStreamReader errorStream,
            String command, String... parameters)
            throws IOException, InterruptedException
    {
        final Process process = ProcessManager.launchProcess(defaultStream, errorStream, command, parameters);
        final int     result  = process.waitFor();
        process.destroy();
        return result;
    }

    public static Process launchProcess(String command, String... parameters)
            throws IOException
    {
        return ProcessManager.launchProcess(null, null, command, parameters);
    }

    public static Process launchProcess(final ProcessStreamReader defaultStream, String command, String... parameters)
            throws IOException
    {
        return ProcessManager.launchProcess(defaultStream, null, command, parameters);
    }

    public static Process launchProcess(
            final ProcessStreamReader defaultStream, final ProcessStreamReader errorStream,
            String command, String... parameters)
            throws IOException
    {
        Objects.requireNonNull(command, "command MUST NOT be null!");
        final List<String> completeCommand = new ArrayObject<>();
        completeCommand.add(command);

        if (parameters != null)
        {
            for (String parameter : parameters)
            {
                if (parameter != null)
                {
                    completeCommand.add(parameter);
                }
            }
        }

        final ProcessBuilder processBuilder = new ProcessBuilder(completeCommand);
        final Process        process        = processBuilder.start();

        if (defaultStream != null)
        {
            ThreadManager.parallel(new ReaderTask(defaultStream), process.getInputStream());
        }

        if (errorStream != null)
        {
            ThreadManager.parallel(new ReaderTask(errorStream), process.getErrorStream());
        }

        return process;
    }
}
