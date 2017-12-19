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

import jhelp.vectorial.math.Point;

/**
 * Represents a rectangle
 *
 * @author JHelp
 */
public class Rectangle
{
    /** Height */
    private double height;
    /** Width */
    private double width;
    /** Up-left corner X */
    private double x;
    /** Up-left corner Y */
    private double y;

    /**
     * Create a new instance of Rectangle
     */
    public Rectangle()
    {
    }

    /**
     * Create a new instance of Rectangle
     *
     * @param x
     *           Up-left corner X
     * @param y
     *           Up-left corner Y
     * @param width
     *           Width
     * @param height
     *           Height
     */
    public Rectangle(final double x, final double y, final double width, final double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Create a new instance of Rectangle
     *
     * @param rectangle
     *           Rectangle to copy
     */
    public Rectangle(final Rectangle rectangle)
    {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    /**
     * Copy a rectangle
     *
     * @param rectangle
     *           Rectangle to copy
     */
    public void copy(final Rectangle rectangle)
    {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }

    /**
     * Create a copy
     *
     * @return Copy created
     */
    public Rectangle createCopy()
    {
        return new Rectangle(this.x, this.y, this.width, this.height);
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
     * Change height
     *
     * @param height
     *           New height
     */
    public void setHeight(final double height)
    {
        this.height = height;
    }

    /**
     * Up-left corner position
     *
     * @return Up-left corner position
     */
    public Point getPosition()
    {
        return new Point(this.x, this.y);
    }

    /**
     * Change up-left corner
     *
     * @param position
     *           Up-left corner position
     */
    public void setPosition(final Point position)
    {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Rectangle size
     *
     * @return Rectangle size
     */
    public Size getSize()
    {
        return new Size(this.width, this.height);
    }

    /**
     * Change rectangle size
     *
     * @param size
     *           new size
     */
    public void setSize(final Size size)
    {
        this.width = size.getWidth();
        this.height = size.getHeight();
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
     * Change width
     *
     * @param width
     *           New width
     */
    public void setWidth(final double width)
    {
        this.width = width;
    }

    /**
     * Up-left corner X
     *
     * @return Up-left corner X
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * Change up-left corner X
     *
     * @param x
     *           New up-left corner X
     */
    public void setX(final double x)
    {
        this.x = x;
    }

    /**
     * Up-left corner Y
     *
     * @return Up-left corner Y
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * Change up-left corner Y
     *
     * @param y
     *           New up-left corner Y
     */
    public void setY(final double y)
    {
        this.y = y;
    }

    /**
     * Compute intersection with a rectangle
     *
     * @param rectangle
     *           Rectangle to intersect
     * @return Intersection
     */
    public Rectangle intersects(final Rectangle rectangle)
    {
        if (rectangle == null)
        {
            return this;
        }

        final double xmax1 = this.x + this.width;
        final double ymax1 = this.y + this.height;
        final double xmin2 = rectangle.x;
        final double xmax2 = rectangle.x + rectangle.width;
        final double ymin2 = rectangle.y;
        final double ymax2 = rectangle.y + rectangle.height;

        if ((this.x > xmax2) || (this.y > ymax2) || (xmin2 > xmax1) || (ymin2 > ymax1))
        {
            return new Rectangle(0, 0, -1, -1);
        }

        final double xmin = Math.max(this.x, xmin2);
        final double xmax = Math.min(xmax1, xmax2);

        if (xmin > xmax)
        {
            return new Rectangle(0, 0, -1, -1);
        }

        final double ymin = Math.max(this.y, ymin2);
        final double ymax = Math.min(ymax1, ymax2);

        if (ymin > ymax)
        {
            return new Rectangle(0, 0, -1, -1);
        }

        return new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
    }

    /**
     * Indicates if given point insides rectangle
     *
     * @param x
     *           Point X
     * @param y
     *           Point Y
     * @return {@code true} if given point insides rectangle
     */
    public boolean isInside(final double x, final double y)
    {
        return (x >= this.x) && (y >= this.y) && (x <= (this.x + this.width)) && (y <= (this.y + this.height));
    }

    /**
     * Change the rectangle
     *
     * @param x
     *           Up-left corner X
     * @param y
     *           Up-left corner Y
     * @param width
     *           Width
     * @param height
     *           Height
     */
    public void setBounds(final double x, final double y, final double width, final double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Change up-left corner
     *
     * @param x
     *           Up-left corner X
     * @param y
     *           Up-left corner Y
     */
    public void setPosition(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Change rectangle size
     *
     * @param width
     *           New width
     * @param height
     *           New height
     */
    public void setSize(final double width, final double height)
    {
        this.width = width;
        this.height = height;
    }
}