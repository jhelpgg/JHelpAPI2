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

package jhelp.antology;

import java.util.Iterator;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.SizedIterable;

/**
 * Node path
 */
public final class Path implements SizedIterable<Triplet>
{
    /**
     * The path
     */
    private final ArrayObject<Triplet> path;

    /**
     * Create empty path
     */
    Path()
    {
        this.path = new ArrayObject<>();
    }

    /**
     * Add triplet to path
     *
     * @param triplet Triplet to add
     */
    void add(Triplet triplet)
    {
        if (this.path.isEmpty() || this.path.last().information().equals(triplet.subject()))
        {
            this.path.add(triplet);
        }
        else
        {
            throw new IllegalArgumentException(
                    "The adding triplet subject must be the last triplet information. Last=" +
                    this.path.last() + " | triplet=" + triplet);
        }
    }

    /**
     * Copy the path
     *
     * @return Path copy
     */
    public Path copy()
    {
        Path copy = new Path();
        this.path.consume(copy::add);
        return copy;
    }

    /**
     * Start subject
     *
     * @return Start subject
     */
    public Node firstSubject()
    {
        if (this.path.isEmpty())
        {
            return null;
        }

        return this.path.first().subject();
    }

    /**
     * Returns an iterator over elements of type {@code Triplet}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Triplet> iterator()
    {
        return this.path.iterator();
    }

    /**
     * Last information of path
     *
     * @return Last information of path
     */
    public Node lastInformation()
    {
        if (this.path.isEmpty())
        {
            return null;
        }

        return this.path.last().information();
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        return this.path.size();
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nPath:");

        for (Triplet triplet : this.path)
        {
            stringBuilder.append("\n\t");
            stringBuilder.append(triplet);
        }

        stringBuilder.append("\n-+*+-\n");

        return stringBuilder.toString();
    }
}
