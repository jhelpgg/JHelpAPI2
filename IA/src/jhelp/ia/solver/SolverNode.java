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

    protected TaskSolve<T> createTask()
    {
        return new TaskSolve<>(this.solver);
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

    protected final void giveSolver(SolverNode<T> solverNode)
    {
        solverNode.solver = this.solver;
    }

    protected abstract SolverNode<T> solveInternal();
}
