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
package jhelp.gui.twoD;

import java.awt.Dimension;
import jhelp.gui.twoD.JHelpFoldable2D.FoldingAreaPosition;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.gui.UtilImage;
import jhelp.util.gui.UtilImage.WayTriangle;

/**
 * Folding header unify color or with a paint. Over a triangle to indicates the folding state. The shape is sausage
 *
 * @author JHelp
 */
public class FoldingAreaSimple
        implements FoldingAreaInterface
{
    /**
     * Default color
     */
    private final int        color;
    /**
     * Paint to use
     */
    private final JHelpPaint paint;
    /**
     * Minimum size
     */
    private final int        size;

    /**
     * Create a new instance of FoldingAreaSimple
     *
     * @param size  Minimum size
     * @param color Default color
     * @param paint Paint to use
     */
    private FoldingAreaSimple(final int size, final int color, final JHelpPaint paint)
    {
        this.size = Math.max(size, 16);
        this.color = color;
        this.paint = paint;
    }

    /**
     * Create a new instance of FoldingAreaSimple with default blue color and {@link JHelpFoldable2D#DEFAULT_PAINT_HORIZONTAL} as
     * paint
     */
    public FoldingAreaSimple()
    {
        this(16, 0xFF0F00F0, JHelpFoldable2D.DEFAULT_PAINT_HORIZONTAL);
    }

    /**
     * Create a new instance of FoldingAreaSimple with default blue color and {@link JHelpFoldable2D#DEFAULT_PAINT_HORIZONTAL} as
     * paint
     *
     * @param size Minimum size
     */
    public FoldingAreaSimple(final int size)
    {
        this(size, 0xFF0F00F0, JHelpFoldable2D.DEFAULT_PAINT_HORIZONTAL);
    }

    /**
     * Create a new instance of FoldingAreaSimple
     *
     * @param size  Minimum size
     * @param color Color unify to use
     */
    public FoldingAreaSimple(final int size, final int color)
    {
        this(size, color, null);
    }

    /**
     * Create a new instance of FoldingAreaSimple with default blue color
     *
     * @param size  Minimum size
     * @param paint Paint to use
     */
    public FoldingAreaSimple(final int size, final JHelpPaint paint)
    {
        this(size, 0xFF0F00F0, paint);
    }

    /**
     * Compute header minimum size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Header minimum size
     * @see jhelp.gui.twoD.FoldingAreaInterface#minimumSize()
     */
    @Override
    public Dimension minimumSize()
    {
        return new Dimension(this.size, this.size);
    }

    /**
     * Draw the header <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x                   X location
     * @param y                   Y location
     * @param width               Header width
     * @param height              Header height
     * @param parent              Image where draw
     * @param fold                Indicates if content component is fold
     * @param foldingAreaPosition Header relative position to the content
     * @see jhelp.gui.twoD.FoldingAreaInterface#paintArea(int, int, int, int, JHelpImage, boolean,
     * FoldingAreaPosition)
     */
    @Override
    public void paintArea(
            final int x, final int y, final int width, final int height, final JHelpImage parent, final boolean fold,
            final FoldingAreaPosition foldingAreaPosition)
    {
        if (this.paint == null)
        {
            parent.fillRoundRectangle(x, y, width, height, this.size, this.size, this.color);
        }
        else
        {
            parent.fillRoundRectangle(x, y, width, height, this.size, this.size, this.paint);
        }

        WayTriangle wayTriangle = null;

        switch (foldingAreaPosition)
        {
            case TOP:
                if (fold)
                {
                    wayTriangle = WayTriangle.DOWN;
                }
                else
                {
                    wayTriangle = WayTriangle.UP;
                }
                break;
            case RIGHT:
                if (fold)
                {
                    wayTriangle = WayTriangle.RIGHT;
                }
                else
                {
                    wayTriangle = WayTriangle.LEFT;
                }
                break;
            case LEFT:
                if (fold)
                {
                    wayTriangle = WayTriangle.LEFT;
                }
                else
                {
                    wayTriangle = WayTriangle.RIGHT;
                }
                break;
            case BOTTOM:
                if (fold)
                {
                    wayTriangle = WayTriangle.UP;
                }
                else
                {
                    wayTriangle = WayTriangle.DOWN;
                }
                break;
        }

        final int tri = this.size - 6;
        UtilImage.drawIncrustedTriangle(x + ((width - tri) >> 1), y + ((height - tri) >> 1), tri, parent, wayTriangle);
    }
}