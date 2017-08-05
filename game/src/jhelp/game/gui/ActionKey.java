package jhelp.game.gui;

import java.awt.event.KeyEvent;

/**
 * Created by jhelp on 21/07/17.
 */
public enum ActionKey
{
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT),
    ACTION(KeyEvent.VK_SPACE),
    CANCEL(KeyEvent.VK_X),
    MENU(KeyEvent.VK_M),
    SPECIAL(KeyEvent.VK_W),
    EXIT(KeyEvent.VK_ESCAPE),
    NEXT(KeyEvent.VK_PAGE_DOWN),
    PREVIOUS(KeyEvent.VK_PAGE_UP),
    ROTATE_LEFT(KeyEvent.VK_O),
    ROTATE_RIGHT(KeyEvent.VK_P);
    private final int defaultKeyCode;

    ActionKey(final int defaultKeyCode)
    {
        this.defaultKeyCode = defaultKeyCode;
    }

    public int defaultKeyCode()
    {
        return this.defaultKeyCode;
    }
}
