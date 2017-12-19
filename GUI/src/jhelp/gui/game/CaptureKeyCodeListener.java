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

/**
 * Received the captured key code in {@link JHelpGame2D}
 *
 * @author JHelp
 */
public interface CaptureKeyCodeListener
{
    /**
     * Indicates if the listener can loose focus. That is to say there are no more key to capture
     *
     * @return {@code true} if the listener can loose focus
     */
    boolean canLooseFocus();

    /**
     * Called when the key code is captured
     *
     * @param keycode Captured key code
     */
    void capturedkeyCode(int keycode);
}