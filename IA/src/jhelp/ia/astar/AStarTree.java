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
