package jhelp.ia.solver;

import jhelp.util.debug.Debug;

/**
 * Created by jhelp on 24/06/17.
 */
public class TestSolver
{
    public static void main(String[] arguments)
    {
        Path      path      = new Path();
        PathSolve pathSolve = new PathSolve(path);
        path = Solver.solve(pathSolve);
        Debug.information("path=", path);
        World.WORLD.print(path);
    }
}
