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
import java.io.InputStream;

/**
 * Fill pixels array from data in input stream
 */
interface PixelsFiller
{
    /**
     * Fill pixels array from data in input stream
     *
     * @param inputStream Stream to parse
     * @param pixels      Pixels array to fill
     * @return {@code true} if filling succeed
     * @throws IOException On stream reading issue
     */
    boolean fillPixels(InputStream inputStream, int[] pixels) throws IOException;
}
