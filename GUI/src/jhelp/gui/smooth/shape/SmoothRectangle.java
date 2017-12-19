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

/**
 * Rectangle shape
 *
 * @author JHelp
 */
public class SmoothRectangle
        extends SmoothShape
{
    /**
     * Singleton of rectangle shape
     */
    public static final SmoothRectangle RECTANGLE = new SmoothRectangle();

    /**
     * Create a new instance of SmoothRectangle
     */
    private SmoothRectangle()
    {
    }

    /**
     * Compute rectangle shape <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     * @param level  Shadow level
     * @return Computed shape
     * @see SmoothShape#computeShape(int, int, int, int, ShadowLevel)
     */
    @Override
    protected ShapeInformation computeShape(
            final int x, final int y, final int width, final int height, final ShadowLevel level)
    {
        Rectangle background, inside;

        if (level == ShadowLevel.NO_SHADOW)
        {
            background = inside = new Rectangle(x, y, width, height);
        }
        else
        {
            final int numberOfPixels = level.numberOfPixels() + 1;
            background = new Rectangle(x, y, width - numberOfPixels, height - (numberOfPixels << 1));
            inside = new Rectangle(background.x + 2, background.y + 2, background.width - 4, background.height - 4);
        }

        return new ShapeInformation(background, inside);
    }

    /**
     * Compute shape to use with rectangle shape <br>
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
        return new Insets(0, 0, numberOfPixels << 1, numberOfPixels);
    }
}