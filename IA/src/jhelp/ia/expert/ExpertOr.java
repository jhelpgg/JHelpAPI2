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
public final class ExpertOr implements ExpertCondition, SizedIterable<ExpertCondition>
{
    private final List<ExpertCondition> orList;
    private final boolean               valueIfEmpty;

    public ExpertOr(boolean valueIfEmpty)
    {
        this.valueIfEmpty = valueIfEmpty;
        this.orList = new ArrayList<>();
    }

    public void add(@NotNull ExpertCondition expertCondition)
    {
        if (expertCondition == null)
        {
            return;
        }

        synchronized (this.orList)
        {
            if (!this.orList.contains(expertCondition))
            {
                this.orList.add(expertCondition);
            }
        }
    }

    public void remove(@NotNull ExpertCondition expertCondition)
    {
        synchronized (this.orList)
        {
            this.orList.remove(expertCondition);
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
        return new EnumerationIterator<>(this.orList.iterator());
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        synchronized (this.orList)
        {
            return this.orList.size();
        }
    }

    @Override
    public boolean valid()
    {
        synchronized (this.orList)
        {
            if (this.orList.isEmpty())
            {
                return this.valueIfEmpty;
            }

            for (ExpertCondition expertCondition : this.orList)
            {
                if (expertCondition.valid())
                {
                    return true;
                }
            }
        }

        return false;
    }
}
