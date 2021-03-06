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
package jhelp.engine2.twoD;

import jhelp.engine2.render.Point2D;

/**
 * A 2D line <br>
 *
 * @author JHelp
 */
public class Line2D
{
    /**
     * Additional information
     */
    public float       additional;
    /**
     * End value
     */
    public float       end;
    /**
     * Path element linked
     */
    public PathElement pathElement;
    /**
     * End point
     */
    public Point2D     pointEnd;
    /**
     * Start point
     */
    public Point2D     pointStart;
    /**
     * Start value
     */
    public float       start;

    /**
     * Constructs Line2D
     *
     * @param pointStart Start Point
     * @param pointEnd   End point
     * @param start      Start value
     * @param end        End value
     */
    public Line2D(final Point2D pointStart, final Point2D pointEnd, final float start, final float end)
    {
        this.pointStart = pointStart;
        this.pointEnd = pointEnd;
        this.start = start;
        this.end = end;
    }
}