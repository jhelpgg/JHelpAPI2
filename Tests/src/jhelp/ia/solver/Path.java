package jhelp.ia.solver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhelp on 24/06/17.
 */
public class Path
{
    private final List<Point> path;

    public Path()
    {
        this.path = new ArrayList<>();
        this.path.add(new Point(0, 0));
    }

    private Path(Path path, int x, int y)
    {
        this.path = new ArrayList<>();
        this.path.addAll(path.path);
        this.path.add(new Point(x, y));

        World.WORLD.print(this);
    }

    public Point currentPosition()
    {
        return new Point(this.path.get(this.path.size() - 1));
    }

    @Override
    public String toString()
    {
        return this.path.toString();
    }

    public Path append(int x, int y)
    {
        return new Path(this, x, y);
    }

    public boolean contains(int x, int y)
    {
        for (Point point : this.path)
        {
            if (point.x == x && point.y == y)
            {
                return true;
            }
        }

        return false;
    }

    public int indexOf(int x, int y)
    {
        int index = -1;

        for (Point point : this.path)
        {
            index++;

            if (point.x == x && point.y == y)
            {
                return index;
            }
        }

        return -1;
    }
}
