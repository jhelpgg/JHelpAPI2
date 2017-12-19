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
package jhelp.gui.action;

import com.sun.istack.internal.NotNull;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Utilities for {@link GenericAction}
 */
public class UtilAction
{
    /**
     * Define a short cut and register it
     *
     * @param keyStroke     Shortcut to register
     * @param genericAction Action to associate to short cut
     * @param frame         Frame where action play
     */
    public static void defineShortCut(
            @NotNull KeyStroke keyStroke, @NotNull GenericAction genericAction, @NotNull JFrame frame)
    {
        UtilAction.defineShortCut(keyStroke, genericAction, frame.getRootPane());
    }

    /**
     * Define a short cut and register it
     *
     * @param keyStroke     Short cut to register
     * @param genericAction Action to associate to short cut
     * @param component     Component of window linked to short cut
     */
    public static void defineShortCut(
            @NotNull KeyStroke keyStroke, @NotNull GenericAction genericAction, @NotNull JComponent component)
    {
        final ActionMap actionMap = component.getActionMap();
        final InputMap  inputMap  = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        genericAction.shortcut(keyStroke);
        actionMap.put(genericAction.name(), genericAction);
        inputMap.put(keyStroke, genericAction.name());
    }
}
