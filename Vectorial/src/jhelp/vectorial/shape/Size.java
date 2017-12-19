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
package jhelp.vectorial.shape;

/**
 * Represents a size/dimension
 *
 * @author JHelp
 */
public class Size
{
    /** Height */
    private double height;
    /** Width */
    private double width;

    /**
     * Create a new instance of Size
     */
    public Size()
    {
    }

    /**
     * Create a new instance of Size
     *
     * @param width
     *           Width
     * @param height
     *           Height
     */
    public Size(final double width, final double height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * Height
     *
     * @return Height
     */
    public double getHeight()
    {
        return this.height;
    }

    /**
     * Change the height
     *
     * @param height
     *           New height
     */
    public void setHeight(final double height)
    {
        this.height = height;
    }

    /**
     * Width
     *
     * @return Width
     */
    public double getWidth()
    {
        return this.width;
    }

    /**
     * Chage the width
     *
     * @param width
     *           New width
     */
    public void setWidth(final double width)
    {
        this.width = width;
    }
}