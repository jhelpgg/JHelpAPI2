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
import jhelp.engine2.twoD.OptionPaneButtons;

/**
 * Listener of option pane events
 *
 * @author JHelp
 */
public interface OptionPane2DListener
{
    /**
     * Called on option pane button click
     *
     * @param optionPaneID Option pane ID
     * @param button       Button clicked
     */
    void optionPaneClicked(int optionPaneID, @NotNull OptionPaneButtons button);
}