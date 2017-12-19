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
