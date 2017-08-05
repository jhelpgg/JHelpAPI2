package jhelp.ia.astar;

/**
 * Created by jhelp on 24/06/17.
 */
public interface Quantifier<T>
{
    int value(AStarNode<T> starNode);
}
