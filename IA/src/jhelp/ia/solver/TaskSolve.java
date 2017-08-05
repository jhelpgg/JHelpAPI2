package jhelp.ia.solver;

import jhelp.util.thread.Task;

/**
 * Created by jhelp on 24/06/17.
 */
class TaskSolve<T> implements Task<SolverNode<T>, Solution<T>>
{
    private final Solver solver;

    TaskSolve(Solver solver)
    {
        this.solver = solver;
    }

    /**
     * Play the task
     *
     * @param parameter Task parameter
     * @return Task result
     */
    @Override public Solution<T> playTask(SolverNode<T> parameter)
    {
        while (!(parameter instanceof Solution))
        {
            if (this.solver.haveSolution())
            {
                return Solution.noSolution();
            }

            parameter = parameter.solve();
        }

        this.solver.solutionFound((Solution<T>) parameter);
        return (Solution<T>) parameter;
    }
}
