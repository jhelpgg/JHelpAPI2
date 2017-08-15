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

package jhelp.video;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.process.ProcessStreamReader;

/**
 * Extract from process the video raw information
 */
class VideoRawProcessReader implements ProcessStreamReader
{
    /**
     * Indicates if information is computed
     */
    private       boolean             computed;
    /**
     * Current information
     */
    private       VideoRawInformation videoRawInformation;
    /**
     * Indicates if there a thread that wait information is computed
     */
    private final AtomicBoolean       waiting;

    /**
     * Create the process reader
     */
    VideoRawProcessReader()
    {
        this.waiting = new AtomicBoolean(false);
        this.computed = false;
        this.videoRawInformation = VideoRawInformation.DEFAULT;
    }

    /**
     * Unlock thread that waiting for answer
     */
    private void unlock()
    {
        synchronized (this.waiting)
        {
            if (this.waiting.get())
            {
                this.waiting.notify();
            }
        }
    }

    /**
     * Called when process have no more line to read
     */
    @Override
    public void endRead()
    {
        this.unlock();
    }

    /**
     * Called when process reading have issue
     *
     * @param exception Issue happen
     */
    @Override
    public void readIssue(final IOException exception)
    {
        this.unlock();
    }

    /**
     * Called when a new line read
     *
     * @param line Line read
     */
    @Override
    public void readLine(final String line)
    {
        if (this.computed)
        {
            return;
        }

        final VideoRawInformation videoRawInformation = VideoRawInformation.parseLine(line);

        if (videoRawInformation != null)
        {
            this.computed = true;
            this.videoRawInformation = videoRawInformation;
            this.unlock();
        }
    }

    /**
     * Video raw information.<br>
     * It will block current thread until video information is known
     *
     * @return Video raw information
     */
    public VideoRawInformation videoRawInformation()
    {
        synchronized (this.waiting)
        {
            while (!this.computed)
            {
                this.waiting.set(true);

                try
                {
                    this.waiting.wait();
                }
                catch (Exception ignored)
                {
                }

                this.waiting.set(false);
            }
        }

        return this.videoRawInformation;
    }
}
