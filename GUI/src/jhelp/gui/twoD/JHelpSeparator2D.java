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
import java.awt.Rectangle;
import jhelp.util.gui.JHelpImage;

/**
 * A separator
 *
 * @author JHelp
 */
public class JHelpSeparator2D
        extends JHelpComponent2D
{
    /**
     * Separator color
     */
    private       int color;
    /**
     * Minimum separator size
     */
    private final int miniumSize;

    /**
     * Create a new instance of JHelpSeparator2D black, size 3
     */
    public JHelpSeparator2D()
    {
        this(0xFF000000);
    }

    /**
     * Create a new instance of JHelpSeparator2D size 3
     *
     * @param color Separator color
     */
    public JHelpSeparator2D(final int color)
    {
        this(3, color);
    }

    /**
     * Create a new instance of JHelpSeparator2D
     *
     * @param miniumSize Miminum separator size
     * @param color      Separator color
     */
    public JHelpSeparator2D(final int miniumSize, final int color)
    {
        this.miniumSize = Math.max(1, miniumSize);
        this.color = color;
    }

    /**
     * Compute separator preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Preferred size
     * @see JHelpComponent2D#computePreferredSize(int, int)
     */
    @Override
    protected Dimension computePreferredSize(final int parentWidth, final int parentHeight)
    {
        return new Dimension(this.miniumSize, this.miniumSize);
    }

    /**
     * Paint separator <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X on parent
     * @param y      Y on parent
     * @param parent Parent where draw
     * @see JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    protected void paint(final int x, final int y, final JHelpImage parent)
    {
        final Rectangle rectangle = this.bounds();
        final int       arc       = Math.min(rectangle.width, rectangle.height);
        parent.fillRoundRectangle(x, y, rectangle.width, rectangle.height, arc, arc, this.color);
    }

    /**
     * Separator color
     *
     * @return Separator color
     */
    public int color()
    {
        return this.color;
    }

    /**
     * Change separator color
     *
     * @param color New separator color
     */
    public void color(final int color)
    {
        this.color = color;
    }

    /**
     * Separator minimum size
     *
     * @return Separator minimum size
     */
    public int miniumSize()
    {
        return this.miniumSize;
    }
}