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

import com.sun.istack.internal.NotNull;
import jhelp.util.data.Observable;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.util.Utilities;

/**
 * Task that simulate a progress
 */
public class SimulateProgressTask implements ConsumerTask<Integer>
{
    /**
     * Observable progress
     */
    private final Observable<Integer> progress;

    /**
     * Create the simulation
     */
    public SimulateProgressTask()
    {
        this.progress = new Observable<>(0);
    }

    /**
     * Play the task
     *
     * @param parameter Number progress steps
     */
    @Override
    public void consume(@NotNull final Integer parameter)
    {
        this.progress.value(0);

        for (int i = 0; i < parameter; i++)
        {
            Utilities.sleep(100);
            this.progress.value((i * 100) / parameter);
        }

        Utilities.sleep(100);
        this.progress.value(100);
    }

    /**
     * Observable for follow the progress
     *
     * @return Observable for follow the progress
     */
    public @NotNull Observable<Integer> progress()
    {
        return this.progress;
    }
}
