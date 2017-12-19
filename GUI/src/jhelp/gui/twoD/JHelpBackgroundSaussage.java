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
 * Background component with sausage look
 *
 * @author JHelp
 */
public class JHelpBackgroundSaussage
        extends JHelpBackground2D
{
    /**
     * Background color
     */
    private int colorBackground;

    /**
     * Create a new instance of JHelpBackgroundSaussage
     *
     * @param component2d Component content
     */
    public JHelpBackgroundSaussage(final JHelpComponent2D component2d)
    {
        this(component2d, 10, 0x80808080);
    }

    /**
     * Create a new instance of JHelpBackgroundSaussage
     *
     * @param component2d     Component content
     * @param colorBackground Background color
     */
    public JHelpBackgroundSaussage(final JHelpComponent2D component2d, final int colorBackground)
    {
        this(component2d, 10, colorBackground);
    }

    /**
     * Create a new instance of JHelpBackgroundSaussage
     *
     * @param component2d     Component content
     * @param space           Border size
     * @param colorBackground Background color
     */
    public JHelpBackgroundSaussage(final JHelpComponent2D component2d, final int space, final int colorBackground)
    {
        super(component2d, space, space, space, space);

        this.colorBackground = colorBackground;
    }

    /**
     * Draw the background <br>
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
        final int arc = Math.min(width, height);

        parent.fillRoundRectangle(x, y, width, height, arc, arc, this.colorBackground);
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public int colorBackground()
    {
        return this.colorBackground;
    }

    /**
     * Change background color
     *
     * @param colorBackground New background color
     */
    public void colorBackground(final int colorBackground)
    {
        this.colorBackground = colorBackground;
    }
}