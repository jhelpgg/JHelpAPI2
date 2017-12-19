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
 * Listener to know when user click on element tree
 *
 * @param <INFORMATION> Information type
 * @author JHelp
 */
public interface TreeClickOnListener<INFORMATION>
{
    /**
     * Called when user click on element tree
     *
     * @param information Information carry on clicked element
     */
    void clickOn(INFORMATION information);
}