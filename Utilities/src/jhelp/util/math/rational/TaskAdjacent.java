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

package jhelp.util.math.rational;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task for compute matrix adjacent in parallel (To speed up computing)
 *
 * @author JHelp
 */
class TaskAdjacent
        extends Thread
{
    /**
     * Matrix where write the cell result
     */
    private final Rational[]     adjacent;
    /**
     * Number task left
     */
    private final AtomicInteger  count;
    /**
     * Matrix height
     */
    private final int            height;
    /**
     * Index to start for the task
     */
    private       int            index;
    /**
     * Matrix to compute its adjacent
     */
    private final MatrixRational matrixRational;
    /**
     * Starting sign
     */
    private       Rational       sign;
    /**
     * Starting X
     */
    private final int            x;

    /**
     * Create a new instance of TaskAdjacent
     *
     * @param count          Number task left
     * @param adjacent       Matrix where write task result
     * @param sign           Starting sign
     * @param index          Starting index where write
     * @param x              Task X
     * @param height         Matrix height
     * @param matrixRational Matrix to get its adjacent
     */
    public TaskAdjacent(
            final AtomicInteger count, final Rational[] adjacent, final Rational sign, final int index, final int x,
            final int height, final MatrixRational matrixRational)
    {
        this.count = count;
        this.adjacent = adjacent;
        this.sign = sign;
        this.index = index;
        this.x = x;
        this.height = height;
        this.matrixRational = matrixRational;
    }

    /**
     * Do the task <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        for (int y = 0; y < this.height; y++)
        {
            this.adjacent[this.index++] = this.sign.multiply(this.matrixRational.determinantSubMatrix(this.x, y));
            this.sign = this.sign.opposite();
        }

        synchronized (this.count)
        {
            if (this.count.decrementAndGet() <= 0)
            {
                this.count.notify();
            }
        }
    }
}