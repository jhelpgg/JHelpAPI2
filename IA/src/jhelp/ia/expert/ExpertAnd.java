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

package jhelp.ia.expert;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.list.SizedIterable;

/**
 * Created by jhelp on 01/07/17.
 */
public final class ExpertAnd implements ExpertCondition, SizedIterable<ExpertCondition>
{
    private final List<ExpertCondition> andList;
    private final boolean               valueIfEmpty;

    public ExpertAnd(boolean valueIfEmpty)
    {
        this.valueIfEmpty = valueIfEmpty;
        this.andList = new ArrayList<>();
    }

    public void add(@NotNull ExpertCondition expertCondition)
    {
        if (expertCondition == null)
        {
            return;
        }

        synchronized (this.andList)
        {
            if (!this.andList.contains(expertCondition))
            {
                this.andList.add(expertCondition);
            }
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<ExpertCondition> iterator()
    {
        return new EnumerationIterator<>(this.andList.iterator());
    }

    public void remove(@NotNull ExpertCondition expertCondition)
    {
        synchronized (this.andList)
        {
            this.andList.remove(expertCondition);
        }
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        synchronized (this.andList)
        {
            return this.andList.size();
        }
    }

    @Override
    public boolean valid()
    {
        synchronized (this.andList)
        {
            if (this.andList.isEmpty())
            {
                return this.valueIfEmpty;
            }

            for (ExpertCondition expertCondition : this.andList)
            {
                if (!expertCondition.valid())
                {
                    return false;
                }
            }
        }

        return true;
    }
}
