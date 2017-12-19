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
package jhelp.engine2.twoD.event;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.twoD.Button2D;

/**
 * Listener of button 2D click
 *
 * @author JHelp
 */
public interface Button2DListener
{
    /**
     * Called when button click
     *
     * @param button2d Clicked button
     * @param buttonID Clicked button ID
     */
    void button2DClicked(@NotNull Button2D button2d, int buttonID);
}