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

/**
 * Created by jhelp on 24/06/17.
 */
public class AStarTree<T>
{
    private final AStarNode<T> root;

    public AStarTree(@NotNull T data, @NotNull Quantifier<T> quantifier)
    {
        this.root = new AStarNode<>(data, 0, quantifier);
    }

    public @NotNull AStarNode<T> root()
    {
        return this.root;
    }

    public void sort()
    {
        this.root.sort();
    }
}
