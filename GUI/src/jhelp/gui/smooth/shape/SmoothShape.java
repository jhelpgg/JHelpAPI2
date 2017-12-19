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
package jhelp.gui.smooth.shape;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import jhelp.gui.smooth.JHelpComponentSmooth;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;

/**
 * {@link JHelpComponentSmooth} background and shadow shape
 *
 * @author JHelp
 */
public abstract class SmoothShape
        implements JHelpConstantsSmooth
{
    /**
     * Create a new instance of SmoothShape
     */
    public SmoothShape()
    {
    }

    /**
     * Draw the shadow
     *
     * @param number
     *           Shadow level value
     * @param shadow
     *           Shadow color
     * @param parent
     *           Image where draw the shadow
     * @param shape
     *           Shadow shape
     */
    private void drawShadow(final int number, final int shadow, final JHelpImage parent, final Shape shape)
    {
        final Area            area            = new Area(shape);
        final AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(number, number * 2);
        area.transform(affineTransform);
        parent.fillShape(area, 0x66000000 | (shadow & 0x00FFFFFF));
    }

    /**
     * Compute the shape to use for background and shadow
     *
     * @param x
     *           Up left corner X
     * @param y
     *           Up left corner Y
     * @param width
     *           Shape width
     * @param height
     *           Shape height
     * @param level
     *           Shadow level
     * @return Created shape
     */
    protected abstract ShapeInformation computeShape(int x, int y, int width, int height, ShadowLevel level);

    /**
     * Compute insets for place the component drawing inside the background
     *
     * @param level
     *           Shadow level
     * @param width
     *           Component width
     * @param height
     *           Component height
     * @return Insets to use
     */
    public abstract Insets computeInsets(ShadowLevel level, int width, int height);

    /**
     * Draw background and shadow shapes
     *
     * @param parent
     *           Image where draw
     * @param x
     *           X
     * @param y
     *           Y
     * @param width
     *           Width
     * @param height
     *           Height
     * @param color
     *           Background color
     * @param level
     *           Shadow level
     * @param shadow
     *           Shadow color
     * @return Area inside the background where draw the component
     */
    public final Rectangle drawShape(
            final JHelpImage parent, final int x, final int y, final int width, final int height, final int color,
            final ShadowLevel level, final int shadow)
    {
        final ShapeInformation shapeInformation = this.computeShape(x, y, width, height, level);

        if (level != ShadowLevel.NO_SHADOW)
        {
            this.drawShadow(level.numberOfPixels(), shadow, parent, shapeInformation.backgroundShape);
        }

        parent.fillShape(shapeInformation.backgroundShape, color);

        return shapeInformation.insideArea;
    }

    /**
     * Draw background and shadow shapes
     *
     * @param parent
     *           Image where draw
     * @param x
     *           X
     * @param y
     *           Y
     * @param width
     *           Width
     * @param height
     *           Height
     * @param texture
     *           Background texture
     * @param level
     *           Shadow level
     * @param shadow
     *           Shadow color
     * @return Area inside the background where draw the component
     */
    public final Rectangle drawShape(
            final JHelpImage parent, final int x, final int y, final int width, final int height,
            final JHelpImage texture,
            final ShadowLevel level, final int shadow)
    {
        final ShapeInformation shapeInformation = this.computeShape(x, y, width, height, level);

        if (level != ShadowLevel.NO_SHADOW)
        {
            this.drawShadow(level.numberOfPixels(), shadow, parent, shapeInformation.backgroundShape);
        }

        parent.fillShape(shapeInformation.backgroundShape, texture);

        return shapeInformation.insideArea;
    }

    /**
     * Draw background and shadow shapes
     *
     * @param parent
     *           Image where draw
     * @param x
     *           X
     * @param y
     *           Y
     * @param width
     *           Width
     * @param height
     *           Height
     * @param paint
     *           Background paint
     * @param level
     *           Shadow level
     * @param shadow
     *           Shadow color
     * @return Area inside the background where draw the component
     */
    public final Rectangle drawShape(
            final JHelpImage parent, final int x, final int y, final int width, final int height,
            final JHelpPaint paint,
            final ShadowLevel level, final int shadow)
    {
        final ShapeInformation shapeInformation = this.computeShape(x, y, width, height, level);

        if (level != ShadowLevel.NO_SHADOW)
        {
            this.drawShadow(level.numberOfPixels(), shadow, parent, shapeInformation.backgroundShape);
        }

        parent.fillShape(shapeInformation.backgroundShape, paint);

        return shapeInformation.insideArea;

    }

    /**
     * Fill shape with one color
     *
     * @param parent
     *           Image parent
     * @param x
     *           X
     * @param y
     *           Y
     * @param width
     *           Width
     * @param height
     *           Height
     * @param color
     *           Color to fill with
     * @param level
     *           Shadow level
     */
    public final void fillShape(
            final JHelpImage parent, final int x, final int y, final int width, final int height, final int color,
            final ShadowLevel level)
    {
        final ShapeInformation shapeInformation = this.computeShape(x, y, width, height, level);
        parent.fillShape(shapeInformation.backgroundShape, color);
    }
}