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

package jhelp.util.io.net;

import java.net.URL;

/**
 * Listener of server response
 *
 * @author JHelp
 */
public interface ResponseReceiver
{
    /**
     * Called if connection failed
     *
     * @param exception Exception give by connection
     * @param url       URL try to be connected
     */
    void connectionFailed(Exception exception, URL url);

    /**
     * Called when server finished to send response
     */
    void endOfResponse();

    /**
     * Called when error message as fill on connection
     *
     * @param message Error message
     */
    void serverError(String message);

    /**
     * Called each time a part of server message receive
     *
     * @param message Message part data
     * @param offset  Offset to start read data
     * @param length  Number of byte to read
     */
    void serverMessage(byte[] message, int offset, int length);

    /**
     * Called just before receive sever response
     */
    void startOfResponse();
}