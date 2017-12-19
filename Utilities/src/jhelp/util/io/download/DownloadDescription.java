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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.ThreadManager;

/**
 * Describe a download.<br>
 * To obtain an instance of a description download something with {@link DownloadManager#download(int, URL, File, DownloadListener)}
 */
public final class DownloadDescription
{
    /**
     * File where download is copy
     */
    private final File              destination;
    /**
     * Exception happen while download. {@code null} if no exception for now
     */
    private       DownloadException downloadException;
    /**
     * Listener of download events
     */
    private final DownloadListener  downloadListener;
    /**
     * Current download status
     */
    private       DownloadStatus    downloadStatus;
    /**
     * Time, in milliseconds from epoch, when download finished (Completed or failed). &lt;0 values means download not finished
     */
    private       long              finishTime;
    /**
     * Download id
     */
    private final int               id;
    /**
     * Download URL source
     */
    private final URL               source;
    /**
     * Time, in milliseconds from epoch, when download started. &lt;0 values means download not started
     */
    private       long              startTime;

    /**
     * Create a download desription
     *
     * @param id               Download id
     * @param source           Download URL source
     * @param destination      File where download is copy
     * @param downloadListener Listener of download events
     * @throws IOException If destination not a valid file or can't be created
     */
    DownloadDescription(
            int id, @NotNull URL source, @NotNull File destination, @NotNull DownloadListener downloadListener)
            throws IOException
    {
        Objects.requireNonNull(source, "source MUST NOT be null!");
        Objects.requireNonNull(destination, "destination MUST NOT be null!");
        Objects.requireNonNull(downloadListener, "downloadListener MUST NOT be null!");

        if (!UtilIO.createFile(destination))
        {
            throw new IOException("Failed to create " + destination.getAbsolutePath());
        }

        this.id = id;
        this.source = source;
        this.destination = destination;
        this.downloadListener = downloadListener;
        this.startTime = -1;
        this.finishTime = -1;
        this.downloadStatus = DownloadStatus.NOT_STARTED;
    }

    /**
     * Launch the download
     */
    void startDownload()
    {
        if (this.downloadStatus != DownloadStatus.NOT_STARTED)
        {
            return;
        }

        this.downloadStatus = DownloadStatus.DOWNLOADING;
        this.startTime = System.currentTimeMillis();

        ThreadManager.parallel(() -> this.downloadListener.downloadStarted(this));
        final boolean succeed = UtilIO.treatInputOutputStream(this.source::openStream,
                                                              () -> new FileOutputStream(this.destination),
                                                              UtilIO::write,
                                                              exception ->
                                                              {
                                                                  this.downloadException = new DownloadException(
                                                                          exception,
                                                                          "Failed to download ",
                                                                          this.id,
                                                                          " in ",
                                                                          this.destination.getAbsolutePath());
                                                                  this.downloadStatus = DownloadStatus.FAILED;
                                                                  this.finishTime = System.currentTimeMillis();
                                                                  UtilIO.delete(this.destination);
                                                                  ThreadManager.parallel(
                                                                          () -> this.downloadListener.downloadFailed(
                                                                                  this));
                                                              });

        if (succeed)
        {
            this.finishTime = System.currentTimeMillis();
            this.downloadStatus = DownloadStatus.SUCCEED;
            ThreadManager.parallel(() -> this.downloadListener.downloadSucceed(this));
        }
    }

    /**
     * File where download is copy
     *
     * @return File where download is copy
     */
    public File destination()
    {
        return this.destination;
    }

    /**
     * Current download status
     *
     * @return Current download status
     */
    public DownloadStatus downloadStatus()
    {
        return this.downloadStatus;
    }

    /**
     * Exception happen while download. <br>
     * {@code null} if no exception for now
     *
     * @return Last exception or {@code null} if no exception
     */
    public DownloadException error()
    {
        return this.downloadException;
    }

    /**
     * Time, in milliseconds from epoch, when download finished (Completed or failed). <br>
     * &lt;0 values means download not finished
     *
     * @return Time, in milliseconds from epoch, when download finished
     */
    public long finishTime()
    {
        return this.finishTime;
    }

    /**
     * Download ID
     *
     * @return Download ID
     */
    public int id()
    {
        return this.id;
    }

    /**
     * Time, in milliseconds from epoch, when download started. <br>
     * &lt;0 values means download not started
     *
     * @return Time, in milliseconds from epoch, when download started
     */
    public long startTime()
    {
        return this.startTime;
    }
}
