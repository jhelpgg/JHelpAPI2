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

import java.util.List;
import jhelp.util.math.Math2;
import jhelp.vectorial.math.Point;
import jhelp.vectorial.path.PathDrawer;

/**
 * Cut a path in approximative list of lines and collect them
 *
 * @author JHelp
 */
public class LineCollector
        implements PathDrawer
{
    /**
     * Bounding box bottom
     */
    private       double     bottom;
    /**
     * Buffer for temporary X values
     */
    private       double[]   bufferX;
    /**
     * Buffer for temporary Y values
     */
    private       double[]   bufferY;
    /**
     * First point X
     */
    private       double     firstX;
    /**
     * First point Y
     */
    private       double     firstY;
    /**
     * Last/current point X
     */
    private       double     lastX;
    /**
     * Last/current point Y
     */
    private       double     lastY;
    /**
     * Bounding box left
     */
    private       double     left;
    /**
     * List where collect lines
     */
    private final List<Line> list;
    /**
     * Precision used for approximate curves
     */
    private final int        precision;
    /**
     * Bounding box right
     */
    private       double     right;
    /**
     * Scale in X
     */
    private final double     scaleX;
    /**
     * Scale in Y
     */
    private final double     scaleY;
    /**
     * Bounding box top
     */
    private       double     top;
    /**
     * X translation applied to path
     */
    private final double     translateX;
    /**
     * Y translation applied to path
     */
    private final double     translateY;

    /**
     * Create a new instance of LineCollector
     *
     * @param list      List where collect the lines
     * @param x         X translation
     * @param y         Y translation
     * @param scaleX    X scale
     * @param scaleY    Y scale
     * @param precision Precision to use
     */
    public LineCollector(
            final List<Line> list, final double x, final double y, final double scaleX, final double scaleY,
            final int precision)
    {
        if (list == null)
        {
            throw new NullPointerException("list mustn't be null");
        }

        this.list = list;
        this.translateX = x;
        this.translateY = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.precision = Math.max(precision, 8);
        this.bufferX = new double[this.precision];
        this.bufferY = new double[this.precision];
        this.lastX = 0;
        this.lastY = 0;
        this.firstX = 0;
        this.firstY = 0;
        this.left = Double.POSITIVE_INFINITY;
        this.top = Double.POSITIVE_INFINITY;
        this.right = Double.NEGATIVE_INFINITY;
        this.bottom = Double.NEGATIVE_INFINITY;
    }

    /**
     * Create a new instance of LineCollector
     *
     * @param list      List where collect the lines
     * @param x         X translation
     * @param y         Y translation
     * @param scale     X and Y scale
     * @param precision Precision to use
     */
    public LineCollector(final List<Line> list, final double x, final double y, final double scale, final int precision)
    {
        this(list, x, y, scale, scale, precision);
    }

    /**
     * Create a new instance of LineCollector
     *
     * @param list      List where collect lines
     * @param x         X translation applied to path
     * @param y         Y translation applied to path
     * @param precision Precision to use for approximate curves
     */
    public LineCollector(final List<Line> list, final double x, final double y, final int precision)
    {
        this(list, x, y, 1, 1, precision);
    }

    /**
     * Add a line in the list
     *
     * @param x1 First point X
     * @param y1 First point Y
     * @param x2 Second point X
     * @param y2 Second point Y
     */
    private void addLine(final double x1, final double y1, final double x2, final double y2)
    {
        final double xx1 = (x1 * this.scaleX) + this.translateX;
        final double yy1 = (y1 * this.scaleY) + this.translateY;
        final double xx2 = (x2 * this.scaleX) + this.translateX;
        final double yy2 = (y2 * this.scaleY) + this.translateY;

        this.left = Math2.minimum(this.left, xx1, xx2);
        this.top = Math2.minimum(this.top, yy1, yy2);
        this.right = Math2.maximum(this.right, xx1, xx2);
        this.bottom = Math2.maximum(this.bottom, yy1, yy2);

        final Line line = new Line(xx1, yy1, xx2, yy2);

        if (line.isPoint() == true)
        {
            return;
        }

        this.list.add(line);
    }

    /**
     * Called when a part path close and return to last move to coordinates <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see PathDrawer#closePath()
     */
    @Override
    public void closePath()
    {
        this.addLine(this.lastX, this.lastY, this.firstX, this.firstY);
        this.lastX = this.firstX;
        this.lastY = this.firstY;
    }

    /**
     * Called when a Bezier cubic is draw from current position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param firstControlPointX  First control point X
     * @param firstControlPointY  First control point Y
     * @param secondControlPointX Second control point X
     * @param secondControlPointY Second control point Y
     * @param x                   Final point X
     * @param y                   Final point Y
     * @see PathDrawer#cubicBezierTo(double, double, double, double, double, double)
     */
    @Override
    public void cubicBezierTo(
            final double firstControlPointX, final double firstControlPointY, final double secondControlPointX,
            final double secondControlPointY, final double x, final double y)
    {
        this.bufferX = Math2.cubic(this.lastX, firstControlPointX, secondControlPointX, x, this.precision,
                                   this.bufferX);
        this.bufferY = Math2.cubic(this.lastY, firstControlPointY, secondControlPointY, y, this.precision,
                                   this.bufferY);

        for (int index = 1; index < this.precision; index++)
        {
            this.addLine(this.bufferX[index - 1], this.bufferY[index - 1], this.bufferX[index], this.bufferY[index]);
        }

        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Called when elliptic arc is draw from current position<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param radiusX               Ellipse radius in X
     * @param radiusY               Ellipse radius in Y
     * @param rotationAxisXInDegree Rotation angle to apply to ellipse in degree
     * @param largeArc              Indicates if large arc implied
     * @param sweep                 Indicates if sweep is choose
     * @param x                     Final point X
     * @param y                     Final point Y
     * @see PathDrawer#ellipticalArcTo(double, double, double, boolean, boolean, double, double)
     */
    @Override
    public void ellipticalArcTo(
            final double radiusX, final double radiusY, final double rotationAxisXInDegree, final boolean largeArc,
            final boolean sweep,
            final double x, final double y)
    {
        if ((Math2.sign(radiusX) <= 0) || (Math2.sign(radiusY) <= 0))
        {
            this.lineTo(x, y);
            return;
        }

        if ((Math2.equals(this.lastX, x) == true) && (Math2.equals(this.lastY, y) == true))
        {
            return;
        }

        final EllipticArcToCrawler ellipticArcToCrawler = new EllipticArcToCrawler(this.lastX, this.lastY, radiusX,
                                                                                   radiusY, rotationAxisXInDegree,
                                                                                   largeArc,
                                                                                   sweep, x, y, this.precision);
        final int number = ellipticArcToCrawler.getNumberStep();
        Point     point;

        for (int step = 1; step < number; step++)
        {
            point = ellipticArcToCrawler.getPoint(step);
            this.addLine(this.lastX, this.lastY, point.getX(), point.getY());
            this.lastX = point.getX();
            this.lastY = point.getY();
        }

        this.addLine(this.lastX, this.lastY, x, y);
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Called when line draw from current position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x Final point X
     * @param y Final point Y
     * @see PathDrawer#lineTo(double, double)
     */
    @Override
    public void lineTo(final double x, final double y)
    {
        this.addLine(this.lastX, this.lastY, x, y);
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Called when cursor moved whithout drawing. Used generally at start, and each time a new path start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x Point to move X
     * @param y Point to move Y
     * @see PathDrawer#moveTo(double, double)
     */
    @Override
    public void moveTo(final double x, final double y)
    {
        this.firstX = x;
        this.firstY = y;
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Called when Bezier quadric is draw from current position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param controlPointX Control point X
     * @param controlPointY Control point Y
     * @param x             Final point X
     * @param y             Final point Y
     * @see PathDrawer#quadricBezierTo(double, double, double, double)
     */
    @Override
    public void quadricBezierTo(final double controlPointX, final double controlPointY, final double x, final double y)
    {
        this.bufferX = Math2.quadratic(this.lastX, controlPointX, x, this.precision, this.bufferX);
        this.bufferY = Math2.quadratic(this.lastY, controlPointY, y, this.precision, this.bufferY);

        for (int index = 1; index < this.precision; index++)
        {
            this.addLine(this.bufferX[index - 1], this.bufferY[index - 1], this.bufferX[index], this.bufferY[index]);
        }

        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Computed bounding box
     *
     * @return Computed bounding box
     */
    public Rectangle getBoundingBox()
    {
        return new Rectangle(this.left, this.top, this.right - this.left, this.bottom - this.top);
    }
}