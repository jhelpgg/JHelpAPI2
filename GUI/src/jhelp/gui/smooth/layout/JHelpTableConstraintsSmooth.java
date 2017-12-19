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
package jhelp.gui.smooth.layout;

/**
 * Constraints for {@link JHelpTableLayoutSmooth}.<br>
 * If defines the up-left cell and the number of cell in width and height of a component
 *
 * @author JHelp
 */
public class JHelpTableConstraintsSmooth
        implements JHelpConstraintsSmooth
{
    /**
     * Number cell in height
     */
    public final int height;
    /**
     * Number of cell in width
     */
    public final int width;
    /**
     * Up left cell X
     */
    public final int x;
    /**
     * Up left cell Y
     */
    public final int y;

    /**
     * Create a new instance of JHelpTableConstraintsSmooth with one cell in width and height
     *
     * @param x Cell X
     * @param y Cell Y
     */
    public JHelpTableConstraintsSmooth(final int x, final int y)
    {
        this(x, y, 1, 1);
    }

    /**
     * Create a new instance of JHelpTableConstraintsSmooth
     *
     * @param x      Cell x
     * @param y      Cell y
     * @param width  Number of cell in width
     * @param height Number of cell in height
     */
    public JHelpTableConstraintsSmooth(final int x, final int y, final int width, final int height)
    {
        this.x = x;
        this.y = y;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
    }
}