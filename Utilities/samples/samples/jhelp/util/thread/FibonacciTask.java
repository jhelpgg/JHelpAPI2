package samples.jhelp.util.thread;

import jhelp.util.debug.Debug;
import jhelp.util.thread.Task;
import jhelp.util.thread.TaskException;

public class FibonacciTask implements Task<Integer, Long>
{
    public FibonacciTask()
    {
    }

    /**
     * Play the task
     *
     * @param rankValue Fibonacci's number rank
     * @return Fibonacci's number
     */
    @Override
    public Long playTask(final Integer rankValue)
    {
        int rank = rankValue;

        if (rank < 0)
        {
            throw new IllegalArgumentException("rankValue MUST be >=0 not " + rank);
        }

        if (rank < 2)
        {
            return (long) rank;
        }

        long value  = 1;
        long first  = 0;
        long second = 1;

        for (; rank > 1; rank--)
        {
            if (Long.MAX_VALUE - first < second)
            {
                throw new IllegalArgumentException("rankValue " + rankValue + " is too big !");
            }

            value = first + second;
            first = second;
            second = value;
        }

        return value;
    }

    /**
     * Called if task failed.<br>
     * Does nothing by default
     *
     * @param taskException Task exception
     */
    @Override
    public void taskError(final TaskException taskException)
    {
        Debug.exception(taskException, "Issue while computing Fibonacci's number");
    }

    /**
     * Called when result is computed.<br>
     * Does nothing by default
     *
     * @param result Task result
     */
    @Override
    public void taskResult(final Long result)
    {
        Debug.verbose("Fibonacci's number is ", result);
    }
}
