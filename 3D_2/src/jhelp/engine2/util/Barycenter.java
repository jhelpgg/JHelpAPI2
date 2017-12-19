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
package jhelp.engine2.util;

/**
 * Barycenter of set of value <br>
 *
 * @author JHelp
 */
public class Barycenter
{
    /**
     * Actual barycenter
     */
    private double barycenter;
    /**
     * Number of elements put in the set
     */
    private int    count;

    /**
     * Constructs Barycenter
     */
    public Barycenter()
    {
        this.count = 0;
    }

    /**
     * Add a value to the set
     *
     * @param value Value add
     */
    public void add(final double value)
    {
        if (this.count == 0)
        {
            this.barycenter = value;
            this.count = 1;
            return;
        }
        //
        this.barycenter = ((this.count * this.barycenter) + value) / (this.count + 1d);
        this.count++;
    }

    /**
     * The actual barycenter
     *
     * @return Actual barycenter
     */
    public double barycenter()
    {
        return this.barycenter;
    }

    /**
     * Indicates if the barycenter is empty
     *
     * @return {@code true} if the barycenter is empty
     */
    public boolean empty()
    {
        return this.count == 0;
    }
}