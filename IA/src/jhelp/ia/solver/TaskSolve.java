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
