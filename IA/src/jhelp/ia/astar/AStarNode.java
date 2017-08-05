package jhelp.ia.astar;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jhelp.util.list.EnumerationIterator;

/**
 * Created by jhelp on 24/06/17.
 */
public class AStarNode<T> implements Iterable<AStarNode<T>>
{
    private final T                       data;
    private final int                     depth;
    private final AStartNodeComparator<T> comparator;
    private final Quantifier<T>           quantifier;
    private final List<AStarNode<T>>      children;
    private       int                     number;

    AStarNode(@NotNull T data, int depth, @NotNull Quantifier<T> quantifier)
    {
        this.data = data;
        this.depth = depth;
        this.quantifier = quantifier;
        this.comparator = new AStartNodeComparator<>(quantifier);
        this.children = new ArrayList<>();
        this.number = -1;
    }

    public AStarNode<T> addChild(@NotNull T data, @NotNull Quantifier<T> quantifier)
    {
        AStarNode<T> child = new AStarNode<>(data, this.depth + 1, quantifier);

        synchronized (this.children)
        {
            this.children.add(child);
        }

        return child;
    }

    public @NotNull T data()
    {
        return this.data;
    }

    public int depth()
    {
        return this.depth;
    }

    public void sort()
    {
        synchronized (this.children)
        {
            int count = this.children.size();

            if (count > 0)
            {
                this.children.stream().forEach(child -> child.number = this.quantifier.value(child));
            }

            this.children.stream().forEach(child -> child.sort());
/*
            int count = this.children.size();

            if (count > 0)
            {
                this.number = 0;
                this.children.stream().forEach(child -> this.number += child.number);
                this.number /= count;
            }
            else
            {
                this.number = this.quantifier.value(this);
            }
*/
            this.children.sort(this.comparator);
        }
    }

    /**
     * Returns an iterator over elements of type {@code AStarNode<T>}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<AStarNode<T>> iterator()
    {
        final List<AStarNode<T>> copy = new ArrayList<>();

        synchronized (this.children)
        {
            // this.children.sort(this.comparator);
            copy.addAll(this.children);
        }

        return new EnumerationIterator<>(copy.iterator());
    }

    public int numberOfChildren()
    {
        return this.children.size();
    }

    public AStarNode<T> child(int index)
    {
        return this.children.get(index);
    }

    public int number()
    {
        return this.number;
    }
}
