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

package jhelp.util.io.download;

import com.sun.istack.internal.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import jhelp.util.thread.ThreadPool;

/**
 * Manager of downloads.<br>
 * It limits the number of download in same time. If more download, they wait their turn.
 */
public final class DownloadManager
{
    /**
     * Pool of thread where downloads played
     */
    private final ThreadPool threadPool;

    /**
     * Create the download manager
     *
     * @param maximumDownloadInSameTime Number maximum of download in same time
     */
    public DownloadManager(int maximumDownloadInSameTime)
    {
        this.threadPool = new ThreadPool(maximumDownloadInSameTime);
    }

    /**
     * Queue a download.<br>
     * If the number of download in same time not reach the download starts immediately, else the download wait its turn.
     *
     * @param id               Download ID. It is developer decision of the real meaning of this.
     * @param source           URL source where download comes from
     * @param destination      File where copy the download. If file not exits method tries to create it. If exists it will be overwrite
     * @param downloadListener Listener to alert for download events
     * @return Description of the download
     * @throws IOException If destination file don't exits and can't be create. Or destination can't be open in write mode
     */
    public @NotNull DownloadDescription download(
            int id, @NotNull URL source, @NotNull File destination, @NotNull DownloadListener downloadListener)
            throws IOException
    {
        final DownloadDescription downloadDescription =
                new DownloadDescription(id, source, destination, downloadListener);
        this.threadPool.runThread(downloadDescription::startDownload);
        return downloadDescription;
    }
}
