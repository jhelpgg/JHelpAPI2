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

/**
 * Listener for action
 *
 * @author JHelp
 */
public interface JHelpActionListener
{
    /**
     * Called when an action happen
     *
     * @param component2d Component where action happen
     * @param identifier  Action identifier
     */
    void actionAppend(JHelpComponent2D component2d, int identifier);
}