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
package jhelp.gui.smooth.event;

import jhelp.gui.smooth.JHelpOptionPaneSmooth;
import jhelp.gui.smooth.OptionPaneButton;

/**
 * Listener of user action in {@link JHelpOptionPaneSmooth}
 *
 * @author JHelp
 */
public interface JHelpOptionPaneSmoothListener
{
    /**
     * Called when user press a button in {@link JHelpOptionPaneSmooth}
     *
     * @param optionPane Option pane source
     * @param button     Button pressed
     */
    void optionPaneButtonClicked(JHelpOptionPaneSmooth optionPane, OptionPaneButton button);
}