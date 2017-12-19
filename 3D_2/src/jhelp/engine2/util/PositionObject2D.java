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
package jhelp.engine2.util;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.twoD.Object2D;

/**
 * 2D object position <br>
 *
 * @author JHelp
 */
public class PositionObject2D
{
    /**
     * Height
     */
    public int height;
    /**
     * Width
     */
    public int width;
    /**
     * X
     */
    public int x;
    /**
     * Y
     */
    public int y;

    /**
     * Constructs PositionObject2D
     */
    public PositionObject2D()
    {
        this.x = this.y = 0;
        this.width = this.height = 1;
    }

    /**
     * Create a new instance of PositionObject2D
     *
     * @param x X
     * @param y Y
     */
    public PositionObject2D(final int x, final int y)
    {
        this.x = x;
        this.y = y;
        this.width = this.height = 1;
    }

    /**
     * Create a new instance of PositionObject2D
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     */
    public PositionObject2D(final int x, final int y, final int width, final int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs PositionObject2D from current position of an object
     *
     * @param object2D Base object to read its current position
     */
    public PositionObject2D(final @NotNull Object2D object2D)
    {
        this.x = object2D.x();
        this.y = object2D.x();
        this.width = object2D.width();
        this.height = object2D.height();
    }

    /**
     * Create a new instance of PositionObject2D copy an other one
     *
     * @param positionObject2D Position to copy
     */
    public PositionObject2D(final @NotNull PositionObject2D positionObject2D)
    {
        this.x = positionObject2D.x;
        this.y = positionObject2D.y;
        this.width = positionObject2D.width;
        this.height = positionObject2D.height;
    }

    /**
     * Create a copy of the position
     *
     * @return The copy
     */
    public @NotNull PositionObject2D copy()
    {
        return new PositionObject2D(this);
    }
}