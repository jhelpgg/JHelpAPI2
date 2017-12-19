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

import com.sun.istack.internal.NotNull;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jhelp on 24/06/17.
 */
public class Solver
{
    public static <T> T solve(@NotNull SolverNode<T> node)
    {
        Solver solver = new Solver();
        node.solver(solver);

        while (!(node instanceof Solution))
        {
            node = node.solve();
        }

        solver.haveSolution();
        return ((Solution<T>) node).solution();
    }

    private final AtomicBoolean haveSolution;

    private Solver()
    {
        this.haveSolution = new AtomicBoolean(false);
    }

    boolean haveSolution()
    {
        return this.haveSolution.get();
    }

    void solutionFound(Solution<?> solution)
    {
        if (!solution.isNoSolution())
        {
            this.haveSolution.set(true);
        }
    }
}
