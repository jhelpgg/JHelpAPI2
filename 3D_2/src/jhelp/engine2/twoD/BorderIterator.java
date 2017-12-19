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
package jhelp.engine2.twoD;

import com.sun.istack.internal.NotNull;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import jhelp.engine2.util.Font3D;
import jhelp.engine2.util.Math3D;

/**
 * Iterator around a path border.
 *
 * @author JHelp
 */
public class BorderIterator
        implements PathIterator
{
    /**
     * Path element <br>
     *
     * @author JHelp
     */
    private class PathElement
    {
        /**
         * Element type : {@link PathIterator#SEG_CLOSE}, {@link PathIterator#SEG_LINETO} or {@link PathIterator#SEG_MOVETO}
         */
        public int    type;
        /**
         * X
         */
        public double x;
        /**
         * Y
         */
        public double y;

        /**
         * Constructs PathElement
         */
        PathElement()
        {
        }
    }

    /**
     * Actual index read
     */
    private       int               index;
    /**
     * Path length
     */
    private       double            length;
    /**
     * Maximum X
     */
    private       double            maxX;
    /**
     * Maximum Y
     */
    private       double            maxY;
    /**
     * Minimum X
     */
    private       double            minX;
    /**
     * Minimum Y
     */
    private       double            minY;
    /**
     * Array of elements
     */
    private       List<PathElement> pathElementsList;
    /**
     * Winding rule : {@link PathIterator#WIND_EVEN_ODD} or {@link PathIterator#WIND_NON_ZERO}
     */
    private final int               windingRule;

    /**
     * Constructs BorderIterator
     *
     * @param shape    Shape to "walk" on its border
     * @param flatness The maximum distance that the line segments used to approximate the curved segments are allowed to deviate from
     *                 any point on the original curve
     */
    public BorderIterator(final Shape shape, final float flatness)
    {
        this.pathElementsList = new ArrayList<>();
        this.index = 0;

        PathElement        pathElement;
        final double[]     bufferReadCoordinates = new double[6];
        final PathIterator pathIterator          = shape.getPathIterator(Font3D.affineTransform, flatness);
        this.windingRule = pathIterator.getWindingRule();
        boolean firstPoint = true;
        this.length = 0;

        double xStart = 0, yStart = 0, xCurrent = 0, yCurrent = 0, xFuture, yFuture;

        while (!pathIterator.isDone())
        {
            pathElement = new PathElement();

            pathElement.type = pathIterator.currentSegment(bufferReadCoordinates);
            pathElement.x = bufferReadCoordinates[0];
            pathElement.y = bufferReadCoordinates[1];

            switch (pathElement.type)
            {
                case PathIterator.SEG_MOVETO:
                    xStart = xCurrent = pathElement.x;
                    yStart = yCurrent = pathElement.y;
                    break;
                case PathIterator.SEG_LINETO:
                    xFuture = pathElement.x;
                    yFuture = pathElement.y;

                    this.length += Math.sqrt(Math3D.square(xCurrent - xFuture) + Math3D.square(yCurrent - yFuture));

                    xCurrent = xFuture;
                    yCurrent = yFuture;
                    break;
                case PathIterator.SEG_CLOSE:
                    this.length += Math.sqrt(Math3D.square(xCurrent - xStart) + Math3D.square(yCurrent - yStart));

                    xCurrent = xStart;
                    yCurrent = yStart;
                    break;
            }

            if (firstPoint)
            {
                this.minX = this.maxX = pathElement.x;
                this.minY = this.maxY = pathElement.y;
            }
            else
            {
                this.minX = Math.min(this.minX, pathElement.x);
                this.maxX = Math.max(this.maxX, pathElement.x);

                this.minY = Math.min(this.minY, pathElement.y);
                this.maxY = Math.max(this.maxY, pathElement.y);
            }

            firstPoint = false;

            this.pathElementsList.add(pathElement);

            pathIterator.next();
        }
    }

    /**
     * Destroy the iterator
     */
    public void destroy()
    {
        this.pathElementsList.clear();
        this.pathElementsList = null;
    }

    /**
     * * Returns the winding rule for determining the interior of the path.
     *
     * @return {@link PathIterator#WIND_EVEN_ODD} or {@link PathIterator#WIND_NON_ZERO}
     * @see PathIterator#getWindingRule()
     */
    @Override
    public int getWindingRule()
    {
        return this.windingRule;
    }

    /**
     * Indicates if we are at the end of the path
     *
     * @return {@code true} if we are at the end of the path
     * @see PathIterator#isDone()
     */
    @Override
    public boolean isDone()
    {
        return this.index >= this.pathElementsList.size();
    }

    /**
     * Got to next path element
     *
     * @see PathIterator#next()
     */
    @Override
    public void next()
    {
        this.index++;
    }

    /**
     * Read the current path element
     *
     * @param readCoordinates Coordinate to fill
     * @return Path element type : {@link PathIterator#SEG_CLOSE}, {@link PathIterator#SEG_LINETO} or
     * {@link PathIterator#SEG_MOVETO}
     * @see PathIterator#currentSegment(float[])
     */
    @Override
    public int currentSegment(final @NotNull float[] readCoordinates)
    {
        final PathElement pathElement = this.pathElementsList.get(this.index);

        readCoordinates[0] = (float) pathElement.x;
        readCoordinates[1] = (float) pathElement.y;

        return pathElement.type;
    }

    /**
     * Read current path element
     *
     * @param readCoordinates Coordinates to fill
     * @return Path type : {@link PathIterator#SEG_CLOSE}, {@link PathIterator#SEG_LINETO} or {@link PathIterator#SEG_MOVETO}
     * @see PathIterator#currentSegment(double[])
     */
    @Override
    public int currentSegment(final @NotNull double[] readCoordinates)
    {
        final PathElement pathElement = this.pathElementsList.get(this.index);

        readCoordinates[0] = pathElement.x;
        readCoordinates[1] = pathElement.y;

        return pathElement.type;
    }

    /**
     * Path length
     *
     * @return Path length
     */
    public double length()
    {
        return this.length;
    }

    /**
     * Return X maximum value in path
     *
     * @return X maximum value
     */
    public double maximumX()
    {
        return this.maxX;
    }

    /**
     * Return Y maximum value in path
     *
     * @return Y maximum value
     */
    public double maximumY()
    {
        return this.maxY;
    }

    /**
     * Return X minimum value in path
     *
     * @return X minimum value
     */
    public double minimumX()
    {
        return this.minX;
    }

    /**
     * Return Y minimum value in path
     *
     * @return Y minimum value
     */
    public double minimumY()
    {
        return this.minY;
    }

    /**
     * Number of {@link PathIterator#SEG_CLOSE} in the description
     *
     * @return Number of {@link PathIterator#SEG_CLOSE}
     */
    public int numberOfClose()
    {
        int number = 0;

        for (final PathElement pathElement : this.pathElementsList)
        {
            if (pathElement.type == PathIterator.SEG_CLOSE)
            {
                number++;
            }
        }

        return number;
    }

    /**
     * Number of elements inside the iterator
     *
     * @return Number of elements inside the iterator
     */
    public int numberOfElements()
    {
        return this.pathElementsList.size();
    }

    /**
     * Number of {@link PathIterator#SEG_LINETO} in the description
     *
     * @return Number of {@link PathIterator#SEG_LINETO}
     */
    public int numberOfLineTo()
    {
        int number = 0;

        for (final PathElement pathElement : this.pathElementsList)
        {
            if (pathElement.type == PathIterator.SEG_LINETO)
            {
                number++;
            }
        }

        return number;
    }

    /**
     * Number of {@link PathIterator#SEG_MOVETO} in the description
     *
     * @return Number of {@link PathIterator#SEG_MOVETO}
     */
    public int numberOfMoveTo()
    {
        int number = 0;

        for (final PathElement pathElement : this.pathElementsList)
        {
            if (pathElement.type == PathIterator.SEG_MOVETO)
            {
                number++;
            }
        }

        return number;
    }

    /**
     * Restart the iterator to the first path element
     */
    public void reset()
    {
        this.index = 0;
    }
}