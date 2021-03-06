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

package jhelp.util.math;

import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.thread.RunnableTask;

/**
 * Threaded task when compute adjacent matrix.<br>
 * It compute one of adjacent line
 *
 * @author JHelp
 */
class TaskAdjacent implements RunnableTask
{
    /**
     * Adjacent matrix result
     */
    private final double[]      adjacent;
    /**
     * Number of task to do
     */
    private final AtomicInteger count;
    /**
     * Matrix height
     */
    private final int           height;
    /**
     * Index where start the line to write
     */
    private       int           index;
    /**
     * Matrix to get its adjacent
     */
    private final Matrix        matrix;
    /**
     * Current line sign
     */
    private       double        sign;
    /**
     * X in matrix
     */
    private final int           x;

    /**
     * Create a new instance of TaskAdjacent
     *
     * @param count    Number of task to do
     * @param adjacent Adjacent matrix result
     * @param sign     Current line sign
     * @param index    Index where start the line to write
     * @param x        X in matrix
     * @param height   Matrix height
     * @param matrix   Matrix to get its adjacent
     */
    public TaskAdjacent(
            final AtomicInteger count, final double[] adjacent, final double sign, final int index, final int x,
            final int height, final Matrix matrix)
    {
        this.count = count;
        this.adjacent = adjacent;
        this.sign = sign;
        this.index = index;
        this.x = x;
        this.height = height;
        this.matrix = matrix;
    }

    /**
     * Do the task
     */
    @Override
    public void run()
    {
        // Compute adjacent line
        for (int y = 0; y < this.height; y++)
        {
            this.adjacent[this.index++] = this.sign * this.matrix.determinantSubMatrix(this.x, y);
            this.sign *= -1;
        }

        // Task is done, if its last one say its finish
        synchronized (this.count)
        {
            if (this.count.decrementAndGet() <= 0)
            {
                this.count.notify();
            }
        }
    }
}