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

package jhelp.gui.game;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.list.ArrayInt;
import jhelp.util.util.Utilities;

/**
 * Area mouse sensitive
 *
 * @author JHelp
 */
public final class MouseSensitiveArea
{
    static
    {
        final ArrayInt colors = new ArrayInt();

        for (final int color : JHelpConstantsSmooth.COLOR_GREYS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_AMBERS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_BLUE_GREYS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_BLUES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_BROWNS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_CYANS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_DEEP_ORANGES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_DEEP_PURPLES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_GREENS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_INDIGOS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_LIGHT_BLUES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_LIGHT_GREENS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_LIMES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_ORANGES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_PINKS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_PURPLES)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_REDS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_TEALS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        for (final int color : JHelpConstantsSmooth.COLOR_YELLOWS)
        {
            colors.add(0x80000000 | (color & 0x00FFFFFF));
        }

        COLORS = colors.toArray();
        Utilities.scramble(MouseSensitiveArea.COLORS);
    }

    /**
     * Area colors
     */
    private static final int[] COLORS;
    /**
     * Next area color index
     */
    private static final AtomicInteger NEXT_COLOR = new AtomicInteger(0);

    /**
     * Create a circle sensitive area
     *
     * @param identifier Area identifier
     * @param centerX    Circle center X
     * @param centerY    Circle center Y
     * @param radius     Circle radius
     * @return Created area
     */
    public static MouseSensitiveArea createCircleArea(
            final int identifier, final int centerX, final int centerY, final int radius)
    {
        return new MouseSensitiveArea(identifier, new Ellipse2D.Double(centerX - radius, centerY - radius, radius << 1,
                                                                       radius << 1));
    }

    /**
     * Create an ellipse sensitive area
     *
     * @param identifier Area identifier
     * @param x          X upper left corner
     * @param y          Y upper left corner
     * @param width      Ellipse width
     * @param height     Ellipse height
     * @return Created area
     */
    public static MouseSensitiveArea createEllipseArea(
            final int identifier, final int x, final int y, final int width, final int height)
    {
        return new MouseSensitiveArea(identifier, new Ellipse2D.Double(x, y, width, height));
    }

    /**
     * Create a rectangle sensitive area
     *
     * @param identifier Area identifier
     * @param x          X upper left corner
     * @param y          Y upper left corner
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @return Created area
     */
    public static MouseSensitiveArea createRectangleArea(
            final int identifier, final int x, final int y, final int width, final int height)
    {
        return new MouseSensitiveArea(identifier, new Rectangle(x, y, width, height));
    }

    /**
     * Area bounds
     */
    private final Rectangle bounds;
    /**
     * Area color
     */
    private       int       color;
    /**
     * Indicates if area is enable
     */
    private       boolean   enable;
    /**
     * Area identifier
     */
    private final int       identifer;
    /**
     * Area shape
     */
    private final Shape     shape;
    /**
     * Translation X
     */
    private       int       x;
    /**
     * Translation Y
     */
    private       int       y;

    /**
     * Create a new instance of MouseSensitiveArea
     *
     * @param identifer Area identifier
     * @param shape     Area shape
     */
    public MouseSensitiveArea(final int identifer, final Shape shape)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        synchronized (MouseSensitiveArea.NEXT_COLOR)
        {
            this.color = MouseSensitiveArea.COLORS[MouseSensitiveArea.NEXT_COLOR.getAndIncrement()];

            if (MouseSensitiveArea.NEXT_COLOR.get() >= MouseSensitiveArea.COLORS.length)
            {
                MouseSensitiveArea.NEXT_COLOR.set(0);
            }
        }

        this.identifer = identifer;
        this.shape = shape;
        this.bounds = shape.getBounds();
        this.enable = true;
    }

    /**
     * Area bounds
     *
     * @return Area bounds
     */
    public Rectangle bounds()
    {
        return new Rectangle(this.bounds);
    }

    /**
     * Area color
     *
     * @return Area color
     */
    public int color()
    {
        return this.color;
    }

    /**
     * Indicates if a position is inside the area
     *
     * @param x X
     * @param y Y
     * @return {@code true} if position is inside
     */
    public boolean contains(final int x, final int y)
    {
        return this.shape.contains(x - this.x, y - this.y);
    }

    /**
     * Detection border
     *
     * @return Detection border
     */
    public Rectangle detection()
    {
        return new Rectangle(this.bounds.x + this.x, this.bounds.y + this.y, this.bounds.width, this.bounds.height);
    }

    /**
     * Indicates if area is enable
     *
     * @return {@code true} if area is enable
     */
    public boolean enable()
    {
        return this.enable;
    }

    /**
     * Change area enable state
     *
     * @param enable New area enable state
     */
    public void enable(final boolean enable)
    {
        this.enable = enable;
    }

    /**
     * Area identifier
     *
     * @return Area identifier
     */
    public int identifier()
    {
        return this.identifer;
    }

    /**
     * Translate the area
     *
     * @param x Translation X
     * @param y Translation Y
     * @return The area
     */
    public MouseSensitiveArea translation(final int x, final int y)
    {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * X translation
     *
     * @return X translation
     */
    public int x()
    {
        return this.x;
    }

    /**
     * Y translation
     *
     * @return Y translation
     */
    public int y()
    {
        return this.y;
    }
}