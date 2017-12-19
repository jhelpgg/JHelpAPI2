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

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 * Ellipse shape
 *
 * @author JHelp
 */
public class SmoothEllipse
        extends SmoothShape
{
    /**
     * Singleton of ellipse shape
     */
    public static final SmoothEllipse ELLIPSE = new SmoothEllipse();

    /**
     * Create a new instance of SmoothEllipse
     */
    private SmoothEllipse()
    {
    }

    /**
     * Compute ellipse shape <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     * @param level  Shadow level
     * @return Ellipse shape
     * @see SmoothShape#computeShape(int, int, int, int, ShadowLevel)
     */
    @Override
    protected ShapeInformation computeShape(
            final int x, final int y, final int width, final int height, final ShadowLevel level)
    {
        final int       numberOfPixels = level.numberOfPixels() + 1;
        final Ellipse2D background     = new Ellipse2D.Double(x, y, width - numberOfPixels,
                                                              height - (numberOfPixels << 1));
        final Rectangle bounds         = background.getBounds();
        final Rectangle inside = new Rectangle(bounds.x + (bounds.width >> 2), bounds.y + (bounds.height >> 2),
                                               bounds.width >> 1, bounds.height >> 1);

        return new ShapeInformation(background, inside);
    }

    /**
     * Compute insets to use with ellipse <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param level  Shadow level
     * @param width  Width
     * @param height Height
     * @return Insets to use
     * @see SmoothShape#computeInsets(ShadowLevel, int, int)
     */
    @Override
    public Insets computeInsets(final ShadowLevel level, final int width, final int height)
    {
        final int numberOfPixels = level.numberOfPixels() + 1;
        return new Insets(height >> 1, width >> 1, (height >> 1) + (numberOfPixels << 1),
                          (width >> 1) + numberOfPixels);
    }
}