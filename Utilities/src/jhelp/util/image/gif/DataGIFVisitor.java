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

package jhelp.util.image.gif;

import jhelp.util.gui.JHelpImage;

/**
 * Visitor of {@link DataGIF} to collect images in {@link DataGIF#collectImages(DataGIFVisitor)}
 *
 * @author JHelp
 */
public interface DataGIFVisitor
{
    /**
     * Called when collecting is finished
     */
    void endCollecting();

    /**
     * Called when next image is computed
     *
     * @param duration Image duration in milliseconds
     * @param image    Image computed
     */
    void nextImage(long duration, JHelpImage image);

    /**
     * Called when collecting starts
     *
     * @param width  Images width
     * @param height Images height
     */
    void startCollecting(int width, int height);
}