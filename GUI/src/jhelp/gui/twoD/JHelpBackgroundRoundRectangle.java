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

import jhelp.util.gui.JHelpImage;

/**
 * Background 2D with round rectangle shape
 *
 * @author JHelp
 */
public class JHelpBackgroundRoundRectangle
        extends JHelpBackground2D
{
    /**
     * Background color
     */
    private int colorBackground;

    /**
     * Create a new instance of JHelpBackgroundRoundRectangle
     *
     * @param component2d Component over the background
     */
    public JHelpBackgroundRoundRectangle(final JHelpComponent2D component2d)
    {
        this(component2d, 20, 0x80808080);
    }

    /**
     * Create a new instance of JHelpBackgroundRoundRectangle
     *
     * @param component2d     Component over the background
     * @param colorBackground Background color
     */
    public JHelpBackgroundRoundRectangle(final JHelpComponent2D component2d, final int colorBackground)
    {
        this(component2d, 20, colorBackground);
    }

    /**
     * Create a new instance of JHelpBackgroundRoundRectangle
     *
     * @param component2d     Component over the background
     * @param arc             Arc size
     * @param colorBackground Background color
     */
    public JHelpBackgroundRoundRectangle(final JHelpComponent2D component2d, final int arc, final int colorBackground)
    {
        super(component2d, arc >> 1, arc >> 1, arc >> 1, arc >> 1);

        this.colorBackground = colorBackground;
    }

    /**
     * Paint the background <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     * @param parent Image where draw
     * @param top    Border top
     * @param left   Border left
     * @param right  Border right
     * @param bottom Border bottom
     * @see JHelpBackground2D#paintBackground(int, int, int, int, JHelpImage, int, int, int, int)
     */
    @Override
    protected void paintBackground(
            final int x, final int y, final int width, final int height, final JHelpImage parent, final int top,
            final int left,
            final int right, final int bottom)
    {
        parent.fillRoundRectangle(x, y, width, height, top + bottom, left + right, this.colorBackground);
    }

    /**
     * Backgound color
     *
     * @return Backgound color
     */
    public int colorBackground()
    {
        return this.colorBackground;
    }

    /**
     * Change backgound color
     *
     * @param colorBackground New backgound color
     */
    public void colorBackground(final int colorBackground)
    {
        this.colorBackground = colorBackground;
    }
}