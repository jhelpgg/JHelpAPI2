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

package jhelp.ia.solver;

/**
 * Created by jhelp on 24/06/17.
 */
public class Solution<T> extends SolverNode<T>
{
    private static final Solution<?> NO_SOLUTION = new Solution<>(null);

    public static <T> Solution<T> noSolution()
    {
        return (Solution<T>) Solution.NO_SOLUTION;
    }

    public static <T> Solution<T> solution(T solution)
    {
        if (solution == null)
        {
            return (Solution<T>) Solution.NO_SOLUTION;
        }

        return new Solution<>(solution);
    }

    private T solution;

    private Solution(T solution)
    {
        this.solution = solution;
    }

    protected SolverNode<T> solveInternal()
    {
        return this;
    }

    public boolean isNoSolution()
    {
        return this.solution == null;
    }

    public T solution()
    {
        return this.solution;
    }
}
