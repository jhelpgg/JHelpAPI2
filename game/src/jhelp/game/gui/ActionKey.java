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

package jhelp.game.gui;

import java.awt.event.KeyEvent;

/**
 * Action key
 */
public enum ActionKey
{
    /**
     * Goes up
     */
    UP(KeyEvent.VK_UP),
    /**
     * Goes down
     */
    DOWN(KeyEvent.VK_DOWN),
    /**
     * Goes left
     */
    LEFT(KeyEvent.VK_LEFT),
    /**
     * Goes right
     */
    RIGHT(KeyEvent.VK_RIGHT),
    /**
     * Do an action
     */
    ACTION(KeyEvent.VK_SPACE),
    /**
     * Cancel current menu/action
     */
    CANCEL(KeyEvent.VK_X),
    /**
     * Open menu
     */
    MENU(KeyEvent.VK_M),
    /**
     * Special action
     */
    SPECIAL(KeyEvent.VK_W),
    /**
     * Exit menu/game
     */
    EXIT(KeyEvent.VK_ESCAPE),
    /**
     * Next page/character/...
     */
    NEXT(KeyEvent.VK_PAGE_DOWN),
    /**
     * Previous  page/character/...
     */
    PREVIOUS(KeyEvent.VK_PAGE_UP),
    /**
     * Rotate camera left
     */
    ROTATE_LEFT(KeyEvent.VK_O),
    /**
     * Rotate camera right
     */
    ROTATE_RIGHT(KeyEvent.VK_P);

    /**
     * Default key code
     */
    private final int defaultKeyCode;

    /**
     * Create action key
     *
     * @param defaultKeyCode Default key code
     */
    ActionKey(final int defaultKeyCode)
    {
        this.defaultKeyCode = defaultKeyCode;
    }

    /**
     * Default key code
     *
     * @return Default key code
     */
    public int defaultKeyCode()
    {
        return this.defaultKeyCode;
    }
}
