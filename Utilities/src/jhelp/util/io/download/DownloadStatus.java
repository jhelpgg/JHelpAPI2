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

/**
 * Download status
 */
public enum DownloadStatus
{
    /**
     * The download not already started (Waits for its turn)
     */
    NOT_STARTED,
    /**
     * The file currently downloading
     */
    DOWNLOADING,
    /**
     * Download finished successfully
     */
    SUCCEED,
    /**
     * Download failed
     */
    FAILED
}
