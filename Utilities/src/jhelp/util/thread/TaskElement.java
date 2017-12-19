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

package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task element in {@link ThreadManager}
 */
class TaskElement<P, R> implements Comparable<TaskElement<P, R>>
{
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    final int        id;
    /**
     * Task parameter
     */
    final P          parameter;
    /**
     * Task to play
     */
    final Task<P, R> task;
    /**
     * Time when play the task
     */
    final long       time;

    /**
     * Create the task element
     *
     * @param time      Time when play the task
     * @param task      Task to play
     * @param parameter Task parameter
     */
    public TaskElement(final long time, final @NotNull Task<P, R> task, final @Nullable P parameter)
    {
        this.id = TaskElement.NEXT_ID.getAndIncrement();
        this.time = time;
        this.task = task;
        this.parameter = parameter;
    }

    TaskElement(int id)
    {
        this.id = id;
        this.task = null;
        this.time = 0;
        this.parameter = null;
    }

    /**
     * Play embed task
     */
    void playTask()
    {
        try
        {
            this.task.taskResult(this.task.playTask(this.parameter));
        }
        catch (Throwable throwable)
        {
            this.task.taskError(new TaskException("Issue while execute a task", throwable));
        }
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param taskElement the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final TaskElement<P, R> taskElement)
    {
        return Long.compare(this.time, taskElement.time);
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }

        if (object == null)
        {
            return false;
        }

        if (!object.getClass().equals(TaskElement.class))
        {
            return false;
        }

        return this.id == ((TaskElement) object).id;
    }
}
