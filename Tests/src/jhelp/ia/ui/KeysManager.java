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

package jhelp.ia.ui;

import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.gui.UtilGUI;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

public class KeysManager
{
    final static         AtomicBoolean ALIVE    = new AtomicBoolean(false);
    private final static RunnableTask  keysTask = () ->
    {
        while (KeysManager.ALIVE.get())
        {
            UtilGUI.simulateKeyPress(KeyEvent.VK_1);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_1);
            UtilGUI.simulateKeyPress(KeyEvent.VK_2);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_2);
            UtilGUI.simulateKeyPress(KeyEvent.VK_3);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_3);
            UtilGUI.simulateKeyPress(KeyEvent.VK_4);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_4);
            UtilGUI.simulateKeyPress(KeyEvent.VK_5);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_5);
            UtilGUI.simulateKeyPress(KeyEvent.VK_6);
            Utilities.sleep(32);
            UtilGUI.simulateKeyRelease(KeyEvent.VK_6);
        }
    };

    public static void start()
    {
        if (KeysManager.ALIVE.compareAndSet(false, true))
        {
            ThreadManager.parallel(KeysManager.keysTask);
        }
    }

    public static void stop()
    {
        KeysManager.ALIVE.set(false);
    }
}
