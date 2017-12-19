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

import jhelp.util.text.UtilText;

/**
 * Exception may happen while downloading
 */
public class DownloadException extends Exception
{
    /**
     * Create exception
     *
     * @param message Exception message
     */
    public DownloadException(Object... message)
    {
        super(UtilText.concatenate(message));
    }

    /**
     * Create exception
     *
     * @param cause   Exception source of this exception
     * @param message Exception message
     */
    public DownloadException(Throwable cause, Object... message)
    {
        super(UtilText.concatenate(message), cause);
    }
}
