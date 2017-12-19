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

import jhelp.util.gui.JHelpImage;
import jhelp.vectorial.shape.Rectangle;
import jhelp.vectorial.shape.Shape;
import jhelp.vectorial.shape.ShapeFiller;

/**
 * Fill shape with texture
 *
 * @author JHelp
 */
class TextureShapeFiller
        implements ShapeFiller
{
    /** Image where draw height */
    private final int   height;
    /** Image where draw pixels */
    private final int[] pixels;
    /** Texture height */
    private final int   textureHeight;
    /** Texture pixels */
    private final int[] texturePixels;
    /** Texture width */
    private final int   textureWidth;
    /** Translation X */
    private       int   vx;
    /** Translation Y */
    private       int   vy;
    /** Image where draw width */
    private final int   width;

    /**
     * Create a new instance of TextureShapeFiller
     *
     * @param width
     *           Image where draw width
     * @param height
     *           Image where draw height
     * @param pixels
     *           Image where draw pixels
     * @param texture
     *           Texture to use
     */
    public TextureShapeFiller(final int width, final int height, final int[] pixels, final JHelpImage texture)
    {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.textureWidth = texture.getWidth();
        this.textureHeight = texture.getHeight();
        this.texturePixels = texture.getPixels(0, 0, this.textureWidth, this.textureHeight);
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
        this.pixels[x + (y * this.width)] = this.texturePixels[((x - this.vx) % this.textureWidth) +
                                                               (((y - this.vy) % this.textureHeight) *
                                                                this.textureWidth)];
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