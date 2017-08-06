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
import java.util.Objects;
import jhelp.util.list.HashInt;
import jhelp.util.preference.Preferences;

/**
 * Created by jhelp on 21/07/17.
 */
public final class KeyMap
{
    private final HashInt<ActionKey> actionKeys;
    private final Preferences        preferences;

    KeyMap(Preferences preferences)
    {
        this.preferences = preferences;
        this.actionKeys = new HashInt<>();

        for (ActionKey actionKey : ActionKey.values())
        {
            this.actionKeys.put(this.preferences.getValue(actionKey.name(), actionKey.defaultKeyCode()), actionKey);
        }
    }

    public ActionKey actionKey(int keyCode)
    {
        return this.actionKeys.get(keyCode);
    }

    public ActionKey associate(int keyCode, ActionKey actionKey)
    {
        Objects.requireNonNull(actionKey, "actionKey MUST NOT be null!");
        ActionKey previous = this.actionKeys.get(keyCode);
        this.actionKeys.put(keyCode, actionKey);
        this.preferences.setValue(actionKey.name(), keyCode);
        return previous;
    }

    public int keyCode(ActionKey actionKey)
    {
        int key = this.actionKeys.obtainKeyOf(actionKey);

        if (key == Integer.MIN_VALUE)
        {
            return KeyEvent.VK_UNDEFINED;
        }

        return key;
    }
}
