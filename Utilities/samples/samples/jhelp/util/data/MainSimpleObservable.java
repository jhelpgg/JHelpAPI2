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

import jhelp.util.data.Observable;
import jhelp.util.data.Observer;
import jhelp.util.debug.Debug;
import jhelp.util.thread.ThreadManager;

public class MainSimpleObservable implements Observer<Integer>
{
    public static void main(String[] arguments)
    {
        //Create the observer
        MainSimpleObservable mainSimpleObservable = new MainSimpleObservable();
        //Create the simulation task
        SimulateProgressTask waitingTask = new SimulateProgressTask();
        //Register the observer
        waitingTask.progress().startObserve(mainSimpleObservable);
        //Launch the task with 42 steps
        ThreadManager.parallel(waitingTask, 42);
    }

    /**
     * Called each time watched observable change.<br>
     * It is also called when register to {@link Observable} via {@link Observable#startObserve(Observer)} to do something with initial value
     *
     * @param observable {@link Observable} that value changed
     * @param newValue   New value give
     */
    @Override
    public void valueChanged(final Observable<Integer> observable, final Integer newValue)
    {
        Debug.verbose("Progress=", newValue);
    }
}
