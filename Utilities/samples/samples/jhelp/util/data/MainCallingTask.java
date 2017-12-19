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

package samples.jhelp.util.data;

import jhelp.util.debug.Debug;
import jhelp.util.thread.ThreadManager;

public class MainCallingTask
{
    public static void main(String[] arguments)
    {
        //Create the simulation task
        SimulateProgressTask waitingTask = new SimulateProgressTask();
        //Link observable to tasks
        waitingTask.progress()
                   .when(value -> value == 0,
                         parameter -> Debug.verbose("Progress started"))
                   .eachTime(value -> value >= 25 && value <= 75,
                             parameter -> Debug.verbose(parameter, " inside [25, 75]"))
                   .when(value -> value == 100,
                         parameter -> Debug.verbose("Progress finished"));
        //Launch the task with 73 steps
        ThreadManager.parallel(waitingTask, 73);
    }
}
