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

import jhelp.vectorial.shape.Rectangle;
import jhelp.vectorial.shape.Shape;
import jhelp.vectorial.shape.ShapeFiller;

/**
 * Fill shape with color
 *
 * @author JHelp
 */
class ColorShapeFiller
        implements ShapeFiller
{
    /** Color to use */
    private final int   color;
    /** Image where draw height */
    private final int   height;
    /** Image where draw pixels */
    private final int[] pixels;
    /** Image where draw width */
    private final int   width;

    /**
     * Create a new instance of ColorShapeFiller
     *
     * @param width
     *           Image where draw width
     * @param height
     *           Image where draw height
     * @param pixels
     *           Image where draw pixels
     * @param color
     *           Color to use
     */
    public ColorShapeFiller(final int width, final int height, final int[] pixels, final int color)
    {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.color = color;
    }

    /**
     * Called when fill finished <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape
     *           Filled shape
     * @see jhelp.vectorial.shape.ShapeFiller#fillShapeEnd(jhelp.vectorial.shape.Shape)
     */
    @Override
    public void fillShapeEnd(final Shape shape)
    {
    }

    /**
     * Called when fill a pixel in a shape <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape
     *           Shape to fill
     * @param x
     *           X
     * @param y
     *           Y
     * @see jhelp.vectorial.shape.ShapeFiller#fillShapePixel(jhelp.vectorial.shape.Shape, int, int)
     */
    @Override
    public void fillShapePixel(final Shape shape, final int x, final int y)
    {
        this.pixels[x + (y * this.width)] = this.color;
    }

    /**
     * Called when fill start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape
     *           Shape to fill
     * @param filledArea
     *           Area where the fill will happen
     * @see jhelp.vectorial.shape.ShapeFiller#fillShapeStart(jhelp.vectorial.shape.Shape, jhelp.vectorial.shape.Rectangle)
     */
    @Override
    public void fillShapeStart(final Shape shape, final Rectangle filledArea)
    {
    }

    /**
     * Obtain the maximum area can be filled, if shape have part outside area, only the intersection will be filled <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape
     *           Shape will be filled
     * @return Maximum area can be filled
     * @see jhelp.vectorial.shape.ShapeFiller#obtainDrawingAreaBounds(jhelp.vectorial.shape.Shape)
     */
    @Override
    public Rectangle obtainDrawingAreaBounds(final Shape shape)
    {
        return new Rectangle(0, 0, this.width, this.height);
    }
}