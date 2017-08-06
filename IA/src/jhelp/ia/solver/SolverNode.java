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
public abstract class SolverNode<T>
{
    private Solver solver;

    public SolverNode()
    {
    }

    void solver(Solver solver)
    {
        this.solver = solver;
    }

    protected TaskSolve<T> createTask()
    {
        return new TaskSolve<>(this.solver);
    }

    protected final void giveSolver(SolverNode<T> solverNode)
    {
        solverNode.solver = this.solver;
    }

    protected final boolean haveSolution()
    {
        return this.solver != null && this.solver.haveSolution();
    }

    protected final void solutionFound(Solution<T> solution)
    {
        if (this.solver != null)
        {
            this.solver.solutionFound(solution);
        }
    }

    protected abstract SolverNode<T> solveInternal();

    public final SolverNode<T> solve()
    {
        if (this.haveSolution())
        {
            return Solution.noSolution();
        }

        SolverNode<T> solverNode = this.solveInternal();
        solverNode.solver(this.solver);

        if (solverNode instanceof Solution)
        {
            this.solutionFound((Solution) solverNode);
        }

        return solverNode;
    }
}
