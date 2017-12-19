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

import javax.swing.JComponent;

/**
 * For object with a component header for the move on detach (Drag/drop and windowable panel)
 */
public interface HaveHeader
{
    /**
     * Component we hang for move
     *
     * @return Component we hang for move
     */
    JComponent obtainTheComponentForMove();
}