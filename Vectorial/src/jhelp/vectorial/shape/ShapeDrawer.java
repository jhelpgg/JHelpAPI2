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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.vectorial.shape;

/**
 * Generic shape drawer.<br>
 * It is used on {@link Shape#drawShape(ShapeDrawer)} to know how draw the shape
 *
 * @author JHelp
 */
public interface ShapeDrawer
{
    /**
     * Called when drawing finished
     *
     * @param shape
     *           Draw shape
     */
    void drawShapeEnd(Shape shape);

    /**
     * Called to draw a segment
     *
     * @param shape
     *           Drawing shape
     * @param x1
     *           First point X
     * @param y1
     *           First point Y
     * @param x2
     *           Second point X
     * @param y2
     *           Second point Y
     */
    void drawShapeSegment(Shape shape, double x1, double y1, double x2, double y2);

    /**
     * Called when draw start
     *
     * @param shape
     *           Drawing shape
     */
    void drawShapeStart(Shape shape);
}