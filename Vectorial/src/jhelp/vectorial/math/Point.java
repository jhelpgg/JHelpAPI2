/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.vectorial.math;

/**
 * A point
 *
 * @author JHelp
 */
public class Point
{
    /** X */
    private double x;
    /** Y */
    private double y;

    /**
     * Create a new instance of Point
     *
     * @param x
     *           X
     * @param y
     *           Y
     */
    public Point(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * X
     *
     * @return X
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * Y
     *
     * @return Y
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * Change point
     *
     * @param x
     *           New X
     * @param y
     *           New Y
     */
    public void setCoordinate(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }
}