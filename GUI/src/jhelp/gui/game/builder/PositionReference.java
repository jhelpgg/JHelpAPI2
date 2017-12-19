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

package jhelp.gui.game.builder;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Describe a relative position
 *
 * @author JHelp
 */
public class PositionReference
{
    /**
     * Rectangle the position refer
     */
    private final Rectangle             bounds;
    /**
     * Type of reference (The "relativity")
     */
    private final PositionReferenceType positionReferenceType;

    /**
     * Create a new instance of PositionReference
     *
     * @param x                     Left up corner X
     * @param y                     Left up corner Y
     * @param width                 Width
     * @param height                Height
     * @param positionReferenceType Type of reference (The "relativity")
     */
    public PositionReference(
            final int x, final int y, final int width, final int height,
            final PositionReferenceType positionReferenceType)
    {
        this(new Rectangle(x, y, width, height), positionReferenceType);
    }

    /**
     * Create a new instance of PositionReference
     *
     * @param width                 Width
     * @param height                Height
     * @param positionReferenceType Type of reference (The "relativity")
     */
    public PositionReference(final int width, final int height, final PositionReferenceType positionReferenceType)
    {
        this(0, 0, width, height, positionReferenceType);
    }

    /**
     * Create a new instance of PositionReference
     *
     * @param bounds                Rectangle the position refer
     * @param positionReferenceType Type of reference (The "relativity")
     */
    public PositionReference(final Rectangle bounds, final PositionReferenceType positionReferenceType)
    {
        if (bounds == null)
        {
            throw new NullPointerException("bounds mustn't be null");
        }

        if (positionReferenceType == null)
        {
            throw new NullPointerException("positionReferenceType mustn't be null");
        }

        this.bounds = bounds;
        this.positionReferenceType = positionReferenceType;
    }

    /**
     * Compute left up corner position of a rectangle with given size
     *
     * @param width  Rectangle width
     * @param height Rectangle height
     * @return Left up corner position
     */
    public Point computeLocation(final int width, final int height)
    {
        switch (this.positionReferenceType)
        {
            case CENTER:
                return new Point(this.bounds.x + ((this.bounds.width - width) >> 1),
                                 this.bounds.y + ((this.bounds.height - height) >> 1));
            case CENTER_BOTTOM:
                return new Point(this.bounds.x + ((this.bounds.width - width) >> 1),
                                 (this.bounds.y + this.bounds.height) - height);
            case CENTER_TOP:
                return new Point(this.bounds.x + ((this.bounds.width - width) >> 1), this.bounds.y);
            case LEFT_BOTTOM:
                return new Point(this.bounds.x, (this.bounds.y + this.bounds.height) - height);
            case LEFT_CENTER:
                return new Point(this.bounds.x, this.bounds.y + ((this.bounds.height - height) >> 1));
            case LEFT_TOP:
                return new Point(this.bounds.x, this.bounds.y);
            case RIGHT_BOTTOM:
                return new Point((this.bounds.x + this.bounds.width) - width,
                                 (this.bounds.y + this.bounds.height) - height);
            case RIGHT_CENTER:
                return new Point((this.bounds.x + this.bounds.width) - width,
                                 this.bounds.y + ((this.bounds.height - height) >> 1));
            case RIGHT_TOP:
                return new Point((this.bounds.x + this.bounds.width) - width, this.bounds.y);
        }

        return null;
    }
}