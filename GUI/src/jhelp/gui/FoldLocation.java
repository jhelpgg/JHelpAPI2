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
package jhelp.gui;

/**
 * Position of fold bar for {@link JHelpFoldablePanel}
 *
 * @author JHelp <br>
 */
public enum FoldLocation
{
    /**
     * Put the bar bellow the component
     */
    BOTTOM(true),
    /**
     * Put the bar at the left of the component
     */
    LEFT(false),
    /**
     * Put the bar at the left of the component
     */
    RIGHT(false),
    /**
     * Put the bar over the component
     */
    TOP(true);

    /**
     * Indicates if position is horizontal
     */
    private final boolean horizontal;

    /**
     * Create a new instance of FoldLocation
     *
     * @param horizontal Indicates if position is horizontal
     */
    FoldLocation(final boolean horizontal)
    {
        this.horizontal = horizontal;
    }

    /**
     * Indicates if position is horizontal
     *
     * @return <code>true</code> if position is horizontal OR <code>false</code> if position is vertical
     */
    public boolean horizontal()
    {
        return this.horizontal;
    }
}