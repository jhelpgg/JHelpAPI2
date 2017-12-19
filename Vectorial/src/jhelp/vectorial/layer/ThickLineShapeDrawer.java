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
package jhelp.vectorial.layer;

import jhelp.util.math.Math2;
import jhelp.vectorial.shape.Shape;
import jhelp.vectorial.shape.ShapeDrawer;

/**
 * Draw border of shape with color thickness
 *
 * @author JHelp
 */
class ThickLineShapeDrawer
        implements ShapeDrawer
{
    /**
     * Border color
     */
    private final int   color;
    /**
     * Image where draw height
     */
    private final int   height;
    /**
     * Maximum
     */
    private final int   max;
    /**
     * Image where draw pixels
     */
    private final int[] pixels;
    /**
     * Border thickness
     */
    private final int   strokeWidth;
    /**
     * Image where draw width
     */
    private final int   width;

    /**
     * Create a new instance of ThickLineShapeDrawer
     *
     * @param width       Image where draw width
     * @param height      Image where draw height
     * @param pixels      Image where draw pixels
     * @param color       Color to use
     * @param strokeWidth Border thickness
     */
    public ThickLineShapeDrawer(
            final int width, final int height, final int[] pixels, final int color, final int strokeWidth)
    {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.strokeWidth = strokeWidth;
        this.max = this.strokeWidth - 1;
        this.color = color;

    }

    /**
     * Draw a point
     *
     * @param x X
     * @param y Y
     */
    private void drawPoint(final int x, final int y)
    {
        for (int yy = y - this.max; yy <= (y + this.max); yy++)
        {
            for (int xx = x - this.max; xx <= (x + this.max); xx++)
            {
                this.drawPoint(xx, yy, this.color);
            }
        }
    }

    /**
     * Draw a point with a color
     *
     * @param x     X
     * @param y     Y
     * @param color Color
     */
    private void drawPoint(final int x, final int y, final int color)
    {
        if ((x >= 0) && (x < this.width) && (y >= 0) && (y < this.height))
        {
            this.pixels[x + (y * this.width)] = color;
        }
    }

    /**
     * Called when draw shape end <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape Shape draw
     * @see jhelp.vectorial.shape.ShapeDrawer#drawShapeEnd(jhelp.vectorial.shape.Shape)
     */
    @Override
    public void drawShapeEnd(final Shape shape)
    {
    }

    /**
     * Called when to draw segment for shape <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape Shape draw
     * @param x1    First point X
     * @param y1    First point Y
     * @param x2    Second point X
     * @param y2    Second point Y
     * @see jhelp.vectorial.shape.ShapeDrawer#drawShapeSegment(jhelp.vectorial.shape.Shape, double, double, double, double)
     */
    @Override
    public void drawShapeSegment(final Shape shape, final double x1, final double y1, final double x2, final double y2)
    {
        int       x     = (int) Math.floor(x1);
        int       y     = (int) Math.floor(y1);
        final int xx    = (int) Math.floor(x2);
        final int yy    = (int) Math.floor(y2);
        int       error = 0;
        final int dx    = Math.abs(xx - x);
        final int sx    = Math2.sign(xx - x);
        final int dy    = Math.abs(yy - y);
        final int sy    = Math2.sign(yy - y);

        if (dx >= dy)
        {
            while ((x != xx) || (y != yy))
            {
                this.drawPoint(x, y);

                x += sx;
                error += dy;

                if (error >= dx)
                {
                    error -= dx;
                    y += sy;
                }
            }
        }
        else
        {
            while ((x != xx) || (y != yy))
            {
                this.drawPoint(x, y);

                y += sy;
                error += dx;

                if (error >= dy)
                {
                    error -= dy;
                    x += sx;
                }
            }
        }

        this.drawPoint(xx, yy);
    }

    /**
     * Called when draw start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape Shape draw
     * @see jhelp.vectorial.shape.ShapeDrawer#drawShapeStart(jhelp.vectorial.shape.Shape)
     */
    @Override
    public void drawShapeStart(final Shape shape)
    {
    }
}