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

import java.io.InputStream;
import java.util.Objects;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.ConsumerTask;

class ReaderTask implements ConsumerTask<InputStream>
{
    private final ProcessStreamReader processStreamReader;

    public ReaderTask(ProcessStreamReader processStreamReader)
    {
        Objects.requireNonNull(processStreamReader, "processStreamReader MUST NOT be null!");
        this.processStreamReader = processStreamReader;
    }

    /**
     * Consume the value
     *
     * @param inputStream Task parameter
     */
    @Override
    public void consume(final InputStream inputStream)
    {
        UtilIO.readLines(() -> inputStream,
                         this.processStreamReader::readLine,
                         this.processStreamReader::readIssue);
        this.processStreamReader.endRead();
    }
}
