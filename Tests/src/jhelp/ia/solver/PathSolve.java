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

import java.awt.Point;

/**
 * Created by jhelp on 24/06/17.
 */
public class PathSolve extends SolverNode<Path>
{
    private Path path;

    public PathSolve(Path path)
    {
        this.path = path;
    }

    @Override
    protected SolverNode<Path> solveInternal()
    {
        Point position = this.path.currentPosition();
        int   x        = position.x;
        int   y        = position.y;

        if (World.WORLD.arrived(x, y))
        {
            return Solution.solution(this.path);
        }

        OrNode<Path> orNode = new OrNode<>();
        int          xx, yy;

        for (int j = -1; j <= 1; j++)
        {
            yy = y + j;

            for (int i = -1; i <= 1; i++)
            {
                xx = x + i;

                if ((i == 0 || j == 0) && !this.path.contains(xx, yy) && World.WORLD.canGo(xx, yy))
                {
                    orNode.add(new PathSolve(this.path.append(xx, yy)));
                }
            }
        }

        if (orNode.empty())
        {
            return Solution.noSolution();
        }

        return orNode;
    }

    @Override public String toString()
    {
        return this.path.toString();
    }
}
