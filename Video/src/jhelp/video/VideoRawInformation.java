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

/**
 * Video raw information
 */
class VideoRawInformation
{
    /**
     * Default raw information video
     */
    public static final VideoRawInformation DEFAULT = new VideoRawInformation(25, VideoRawFormat.YUV420P);

    /**
     * Parse a output video line and try to extract video raw information.<br>
     * It will return {@code null} if given line contains nothing about raw information
     *
     * @param line Line to parse
     * @return Extracted information OR {@code null} if line not contains the information
     */
    public static VideoRawInformation parseLine(String line)
    {
        VideoRawFormat format = null;

        for (VideoRawFormat videoRawFormat : VideoRawFormat.values())
        {
            if (line.contains(videoRawFormat.format()))
            {
                format = videoRawFormat;
                break;
            }
        }

        if (format != null)
        {
            int end = line.indexOf("fps");

            if (end >= 2)
            {
                int start = line.lastIndexOf(' ', end - 2);

                if (start >= 0)
                {
                    try
                    {
                        int fps = (int) Math.ceil(Double.parseDouble(line.substring(start, end).trim()));
                        return new VideoRawInformation(fps, format);
                    }
                    catch (Exception ignored)
                    {
                    }
                }
            }
        }

        return null;
    }

    /**
     * Video FPS
     */
    final int            fps;
    /**
     * Video raw format
     */
    final VideoRawFormat videoRawFormat;

    /**
     * Create video raw information
     *
     * @param fps            Video FPS
     * @param videoRawFormat Video raw format
     */
    private VideoRawInformation(final int fps, final VideoRawFormat videoRawFormat)
    {
        this.fps = Math.max(1, fps);
        this.videoRawFormat = videoRawFormat;
    }
}
