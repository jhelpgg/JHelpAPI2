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

import jhelp.gui.smooth.JHelpEditTextSmooth;

/**
 * Listener of text validation by "ENTER" key in {@link JHelpEditTextSmooth}
 *
 * @author JHelp
 */
public interface SmoothEditTextListener
{
    /**
     * Called when "ENTER" is pressed in {@link JHelpEditTextSmooth}
     *
     * @param editTextSmooth {@link JHelpEditTextSmooth} where "ENTER" is pressed
     */
    void editTextEnterTyped(JHelpEditTextSmooth editTextSmooth);
}