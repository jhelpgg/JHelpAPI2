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

enum VideoRawFormat
{
    /**
     * RGBA video format
     */
    RGBA("rgba"),
    /**
     * YUV 410 P video format
     */
    YUV410P("yuv410p"),
    /**
     * YUV 420 P video format
     */
    YUV420P("yuv420p");

    private final String format;

    VideoRawFormat(String format)
    {
        this.format = format;
    }

    public String format()
    {
        return this.format;
    }
}
