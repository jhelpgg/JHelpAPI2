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

import jhelp.util.gui.JHelpPaint;
import jhelp.vectorial.shape.Rectangle;
import jhelp.vectorial.shape.Shape;
import jhelp.vectorial.shape.ShapeFiller;

/**
 * Filler shape with paint
 *
 * @author JHelp
 */
class PaintShapeFiller
        implements ShapeFiller
{
    /** Image where draw height */
    private final int        height;
    /** Paint to use */
    private final JHelpPaint paint;
    /** Image where draw pixels */
    private final int[]      pixels;
    /** Translation X */
    private       int        vx;
    /** Translation Y */
    private       int        vy;
    /** Image where draw width */
    private final int        width;

    /**
     * Create a new instance of PaintShapeFiller
     *
     * @param width
     *           Image where draw width
     * @param height
     *           Image where draw height
     * @param pixels
     *           Image where draw pixels
     * @param paint
     *           Paint to use
     */
    public PaintShapeFiller(final int width, final int height, final int[] pixels, final JHelpPaint paint)
    {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.paint = paint;
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
        this.pixels[x + (y * this.width)] = this.paint.obtainColor(x - this.vx, y - this.vy);
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
        final Rectangle box = shape.getBoundingBox();
        this.vx = (int) Math.floor(box.getX());
        this.vy = (int) Math.floor(box.getY());
        this.paint.initializePaint((int) Math.floor(filledArea.getWidth()), (int) Math.floor(filledArea.getHeight()));
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