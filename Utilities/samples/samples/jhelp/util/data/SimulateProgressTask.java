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
     * Observable for follow the progress
     *
     * @return Observable for follow the progress
     */
    public @NotNull Observable<Integer> progress()
    {
        return this.progress;
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
}
