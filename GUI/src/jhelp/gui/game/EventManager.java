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
package jhelp.gui.game;

import java.util.Map;

/**
 * Event state manager.<br>
 * The method {@link #actionState(Map, int, int, boolean, boolean, boolean)} is called in loop, even if nothing change
 *
 * @author JHelp
 */
public interface EventManager
{
    /**
     * Actual action key and mouse state
     *
     * @param actionsStates Action states. Just use {@link Map#get(Object) actionsStates.get(actionKey)} to know a action key status
     * @param mouseX        Mouse X position
     * @param mouseY        Mouse Y position
     * @param buttonLeft    Indicates if mouse button left is down
     * @param buttonMiddle  Indicates if mouse button middle is down
     * @param buttonRight   Indicates if mouse button right is down
     * @return {@code true} if key events are consumed
     */
    boolean actionState(
            Map<ActionKey, Boolean> actionsStates, int mouseX, int mouseY, boolean buttonLeft, boolean buttonMiddle,
            boolean buttonRight);
}