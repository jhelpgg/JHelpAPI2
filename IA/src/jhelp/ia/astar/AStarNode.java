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

package jhelp.ia.astar;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.EnumerationIterator;

/**
 * Created by jhelp on 24/06/17.
 */
public class AStarNode<T> implements Iterable<AStarNode<T>>
{
    private final ArrayObject<AStarNode<T>> children;
    private final AStartNodeComparator<T>   comparator;
    private final T                         data;
    private final int                       depth;
    private       int                       number;
    private final Quantifier<T>             quantifier;

    AStarNode(@NotNull T data, int depth, @NotNull Quantifier<T> quantifier)
    {
        this.data = data;
        this.depth = depth;
        this.quantifier = quantifier;
        this.comparator = new AStartNodeComparator<>(quantifier);
        this.children = new ArrayObject<>();
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

    public AStarNode<T> child(int index)
    {
        return this.children.get(index);
    }

    public @NotNull T data()
    {
        return this.data;
    }

    public int depth()
    {
        return this.depth;
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

    public int number()
    {
        return this.number;
    }

    public int numberOfChildren()
    {
        return this.children.size();
    }

    public void sort()
    {
        synchronized (this.children)
        {
            int count = this.children.size();

            if (count > 0)
            {
                this.children.map(child -> child.number = this.quantifier.value(child));
            }

            this.children.consume(AStarNode::sort);
            this.children.sort(this.comparator);
        }
    }
}
