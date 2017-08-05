package jhelp.ia.solver;

/**
 * Created by jhelp on 24/06/17.
 */
public class World
{
    public static final World WORLD = new World();
    private final boolean[][] world;
    private final int         width;
    private final int         height;

    private static final char characterFor(int index)
    {
        if (index < 0)
        {
            return '*';
        }

        if (index < '9' - '0' + 1)
        {
            return (char) ('0' + index);
        }

        index -= '9' - '0' + 1;

        if (index < 'z' - 'a' + 1)
        {
            return (char) ('a' + index);
        }

        index -= 'z' - 'a' + 1;
        return (char) ('A' + index);
    }

    private World()
    {
        this.width = 3 + (int) (Math.random() * 5);
        this.height = 3 + (int) (Math.random() * 5);
        this.world = new boolean[this.width][this.height];

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                this.world[x][y] = Math.random() >= 0.2;
            }
        }

        this.world[0][0] = true;
        this.world[this.width - 1][this.height - 1] = true;
        this.print(null);
    }

    public boolean canGo(int x, int y)
    {
        return x >= 0 && x < this.width && y >= 0 && y < this.height && this.world[x][y];
    }

    public boolean arrived(int x, int y)
    {
        return x == this.width - 1 && y == this.height - 1;
    }

    public synchronized void print(Path path)
    {
        System.out.println();

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                if (this.world[x][y])
                {
                    if (path != null)
                    {
                        System.out.print(World.characterFor(path.indexOf(x, y)));
                    }
                    else
                    {
                        System.out.print('*');
                    }
                }
                else
                {
                    System.out.print(' ');
                }
            }

            System.out.println();
        }
    }
}
