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

import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.gui.UtilGUI;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

public class ClicksManager
{
    final static         AtomicBoolean ALIVE    = new AtomicBoolean(false);
    private final static RunnableTask  keysTask = () ->
    {
        while (ClicksManager.ALIVE.get())
        {
            UtilGUI.simulateMouseClick(32);
            Utilities.sleep(32);
        }
    };

    public static void start()
    {
        if (ClicksManager.ALIVE.compareAndSet(false, true))
        {
            ThreadManager.parallel(ClicksManager.keysTask);
        }
    }

    public static void stop()
    {
        ClicksManager.ALIVE.set(false);
    }
}
