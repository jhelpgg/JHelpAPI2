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
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpMask;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.gui.UtilImage;
import jhelp.util.gui.UtilImage.WayTriangle;

/**
 * Folding header with title text. Over a triangle to indicates the folding state. The shape is round rectangle
 *
 * @author JHelp
 */
public class FoldingAreaTitle
        implements FoldingAreaInterface
{
    /**
     * Background color
     */
    private final int        color;
    /**
     * Foreground color
     */
    private final int        colorText;
    /**
     * Font to use
     */
    private final JHelpFont  font;
    /**
     * Minimum height
     */
    private       int        miniumHeight;
    /**
     * Minimum width
     */
    private       int        miniumWidth;
    /**
     * Background paint
     */
    private final JHelpPaint paint;
    /**
     * Foreground paint
     */
    private final JHelpPaint paintText;
    /**
     * Title to write
     */
    private final String     title;

    /**
     * Create a new instance of FoldingAreaTitle
     *
     * @param color     Background color used if <b>paint</b> is {@code null}
     * @param paint     Background paint
     * @param title     Test to print as title
     * @param font      Font to use
     * @param colotText Foreground color if <b>paintText</b> is {@code null}
     * @param paintText Foreground paint
     */
    public FoldingAreaTitle(
            final int color, final JHelpPaint paint, final String title, final JHelpFont font, final int colotText,
            final JHelpPaint paintText)
    {
        this.color = color;
        this.paint = paint;
        this.title = title;
        this.font = font;
        this.colorText = colotText;
        this.paintText = paintText;

        this.miniumWidth = 0;
        this.miniumHeight = 0;
        for (final char character : title.toCharArray())
        {
            this.miniumWidth = Math.max(this.miniumWidth, font.stringWidth(String.valueOf(character)) + 2);
            this.miniumHeight = Math.max(this.miniumHeight, font.getHeight() + 2);
        }

        this.miniumWidth = Math.max(16, this.miniumWidth + 6);
        this.miniumHeight = Math.max(16, this.miniumHeight + 6);
    }

    /**
     * Folding header minimum size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Folding header minimum size
     * @see jhelp.gui.twoD.FoldingAreaInterface#minimumSize()
     */
    @Override
    public Dimension minimumSize()
    {
        return new Dimension(this.miniumWidth, this.miniumHeight);
    }

    /**
     * Paint the header <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x                   x
     * @param y                   Y
     * @param width               Width
     * @param height              Height
     * @param parent              Image where draw
     * @param fold                Indicates if content component is fold
     * @param foldingAreaPosition Header position relative to component content
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
            parent.fillRoundRectangle(x, y, width, height, this.miniumWidth, this.miniumHeight, this.color);
        }
        else
        {
            parent.fillRoundRectangle(x, y, width, height, this.miniumWidth, this.miniumHeight, this.paint);
        }

        WayTriangle wayTriangle = null;
        boolean     horizontal  = true;

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
                horizontal = false;
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
                horizontal = false;
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

        final int tri = Math.min(this.miniumWidth, this.miniumHeight) - 6;

        if (horizontal)
        {
            final int stringWidth = this.font.computeShape(this.title, 0, 0).getBounds().width;

            if (this.paintText == null)
            {
                parent.fillString(x + 3 + (((width - 6 - tri) - stringWidth) >> 1), y, this.title, this.font,
                                  this.colorText);
            }
            else
            {
                parent.fillString(x + 3 + (((width - 6 - tri) - stringWidth) >> 1), y, this.title, this.font,
                                  this.paintText, this.colorText);
            }

            UtilImage.drawIncrustedTriangle((x + width) - tri - 3, y + ((height - tri) >> 1), tri, parent, wayTriangle);
        }
        else
        {
            UtilImage.drawIncrustedTriangle(x + ((width - tri) >> 1), y + 3, tri, parent, wayTriangle);

            final int fontHeight = this.font.getHeight();
            int       yy         = y + tri + (((height - 6 - tri) - (fontHeight * this.title.length())) >> 1);
            JHelpMask mask;

            if (this.paintText == null)
            {
                for (final char charater : this.title.toCharArray())
                {
                    mask = this.font.createMask(String.valueOf(charater));
                    parent.paintMask(x + 3, yy, mask, this.colorText, 0, true);
                    yy += fontHeight;
                }
            }
            else
            {
                for (final char charater : this.title.toCharArray())
                {
                    mask = this.font.createMask(String.valueOf(charater));
                    parent.paintMask(x + 3, yy, mask, this.paintText, 0, true);
                    yy += fontHeight;
                }
            }
        }
    }
}