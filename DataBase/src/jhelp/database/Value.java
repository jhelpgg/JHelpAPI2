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

package jhelp.database;

/**
 * A value have column name and data<br>
 * Use to insert value in table
 */
public class Value
{
    /**
     * Data
     */
    private final Data   data;
    /**
     * Column name
     */
    private final String name;

    /**
     * Constructs Value
     *
     * @param name Column name
     * @param data Data
     */
    public Value(final String name, final Data data)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }
        if (data == null)
        {
            throw new NullPointerException("data mustn't be null");
        }
        this.name = name;
        this.data = data;
    }

    /**
     * Return data
     *
     * @return data
     */
    public Data getData()
    {
        return this.data;
    }

    /**
     * Return name
     *
     * @return name
     */
    public String getName()
    {
        return this.name;
    }
}