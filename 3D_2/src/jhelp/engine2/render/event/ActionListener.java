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

package jhelp.engine2.render.event;

import com.sun.istack.internal.NotNull;

/**
 * Listener of current actions
 */
public interface ActionListener
{
    /**
     * Called each time current actions updates.<br>
     * Note if their no current active action, the method is not called
     *
     * @param actionCodes Current active action code list.
     */
    void actionsActive(@NotNull ActionCode... actionCodes);
}
