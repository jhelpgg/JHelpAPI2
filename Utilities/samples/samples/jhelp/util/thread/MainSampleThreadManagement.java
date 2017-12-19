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

package samples.jhelp.util.thread;

import jhelp.util.thread.ThreadManager;

public class MainSampleThreadManagement
{
    public static void main(String[] arguments)
    {
        FibonacciTask fibonacciTask = new FibonacciTask();
        ThreadManager.doTask(fibonacciTask, 6);
        ThreadManager.doTask(fibonacciTask, 120);
        ThreadManager.doTask(fibonacciTask, 12);
        ThreadManager.doTask(fibonacciTask, -66);
    }
}
