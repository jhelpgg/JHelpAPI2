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
