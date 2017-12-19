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

/**
 * Listener of downloads events
 */
public interface DownloadListener
{
    /**
     * Called when download failed
     *
     * @param downloadDescription Download that failed
     */
    void downloadFailed(@NotNull DownloadDescription downloadDescription);

    /**
     * Called when download started
     *
     * @param downloadDescription Download that started
     */
    void downloadStarted(@NotNull DownloadDescription downloadDescription);

    /**
     * Called when download finish successfully
     *
     * @param downloadDescription Download that finished
     */
    void downloadSucceed(@NotNull DownloadDescription downloadDescription);
}
