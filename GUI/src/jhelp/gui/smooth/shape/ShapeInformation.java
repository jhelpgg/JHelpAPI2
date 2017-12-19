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
package jhelp.gui.smooth.shape;

import java.awt.Rectangle;
import java.awt.Shape;

/**
 * Describe a shape
 *
 * @author JHelp
 */
public class ShapeInformation
{
    /**
     * Background and shadow shape
     */
    public final Shape     backgroundShape;
    /**
     * Area where draw component content
     */
    public final Rectangle insideArea;

    /**
     * Create a new instance of ShapeInformation
     *
     * @param backgroundShape Background and shadow shape
     * @param insideArea      Area where draw component content
     */
    public ShapeInformation(final Shape backgroundShape, final Rectangle insideArea)
    {
        this.backgroundShape = backgroundShape;
        this.insideArea = insideArea;
    }
}