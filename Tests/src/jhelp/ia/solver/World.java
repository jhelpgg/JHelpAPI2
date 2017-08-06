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

/**
 * Created by jhelp on 24/06/17.
 */
public class World
{
    public static final World WORLD = new World();

    private static char characterFor(int index)
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

    private final int         height;
    private final int         width;
    private final boolean[][] world;

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

    public boolean arrived(int x, int y)
    {
        return x == this.width - 1 && y == this.height - 1;
    }

    public boolean canGo(int x, int y)
    {
        return x >= 0 && x < this.width && y >= 0 && y < this.height && this.world[x][y];
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
