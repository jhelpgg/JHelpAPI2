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
