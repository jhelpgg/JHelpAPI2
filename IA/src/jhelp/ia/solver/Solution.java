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

    public boolean isNoSolution()
    {
        return this.solution == null;
    }

    public T solution()
    {
        return this.solution;
    }

    protected SolverNode<T> solveInternal()
    {
        return this;
    }
}
