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

import jhelp.util.math.Math2;
import jhelp.vectorial.math.Matrix;
import jhelp.vectorial.math.Point;
import jhelp.vectorial.path.PathDrawer;

/**
 * Helper for crawl a generic elliptic arc.<br>
 * It gives a number of points or line to approximate the elliptic arc.<br>
 * The number of points or lines depends on the given precision.<br>
 * More precision give a better look, but give also more line/point to draw, so you have to find the good compromise depends on
 * what you want to do and your constraints.<br>
 * It may help, for example, in implementation of
 * {@link PathDrawer#ellipticalArcTo(double, double, double, boolean, boolean, double, double)}. See
 * {@link LineCollector#ellipticalArcTo(double, double, double, boolean, boolean, double, double)} code<br>
 * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
 *
 * @author JHelp
 */
public class EllipticArcToCrawler
{
    /**
     * Computed angle end
     */
    private       double angleEnd;
    /**
     * Computed angle start
     */
    private       double angleStart;
    /**
     * Computed ellipse center X
     */
    private final double centerX;
    /**
     * Computed ellipse center Y
     */
    private final double centerY;
    /**
     * Given end point X
     */
    private final double endX;
    /**
     * Given end point Y
     */
    private final double endY;
    /**
     * Number of steps that the elliptic arc is cut
     */
    private final int    numberStep;
    /**
     * Given radius X
     */
    private final double radiusX;
    /**
     * Given radius Y
     */
    private final double radiusY;
    /**
     * Rotation to apply to transform the result
     */
    private final Matrix rotation;
    /**
     * Given start X
     */
    private final double startX;
    /**
     * Given start Y
     */
    private final double startY;

    /**
     * Create a new instance of EllipticArcToCrawler<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
     *
     * @param startX                Start point X
     * @param startY                Start point Y
     * @param radiusX               Ellipse radius in X
     * @param radiusY               Ellipse radius in Y
     * @param rotationAxisXInDegree Angle for rotate ellipse in degree
     * @param largeArc              Indicates if choose the large arc or not
     * @param sweep                 Indicates if choose the sweep or not
     * @param endX                  End point X
     * @param endY                  End point Y
     * @param precision             Precision to use
     */
    public EllipticArcToCrawler(
            final double startX, final double startY, double radiusX, double radiusY,
            final double rotationAxisXInDegree,
            final boolean largeArc, final boolean sweep, final double endX, final double endY, final int precision)
    {
        this.startX = startX;
        this.startY = startY;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.endX = endX;
        this.endY = endY;

        //

        // Compute the half distance between the current and the final point
        final double distanceX2 = (startX - endX) / 2.0;
        final double distanceY2 = (startY - endY) / 2.0;
        // Convert angle from degrees to radians
        final double angle    = Math.toRadians(rotationAxisXInDegree);
        final double cosAngle = Math.cos(angle);
        final double sinAngle = Math.sin(angle);

        //
        // Step 1 : Compute (x1, y1)
        //
        final double x1  = ((cosAngle * distanceX2) + (sinAngle * distanceY2));
        final double y1  = ((-sinAngle * distanceX2) + (cosAngle * distanceY2));
        double       Prx = radiusX * radiusX;
        double       Pry = radiusY * radiusY;
        final double Px1 = x1 * x1;
        final double Py1 = y1 * y1;

        // check that radii are large enough
        final double radiiCheck = (Px1 / Prx) + (Py1 / Pry);

        if (radiiCheck > 0.99999)
        {
            // don't cut it too close
            final double radiiScale = Math.sqrt(radiiCheck) * 1.00001;
            radiusX = radiiScale * radiusX;
            radiusY = radiiScale * radiusY;
            Prx = radiusX * radiusX;
            Pry = radiusY * radiusY;
        }

        //
        // Step 2 : Compute (cx1, cy1)
        //
        double sign = (largeArc == sweep)
                      ? -1
                      : 1;
        final double sq   = Math.max(0, ((Prx * Pry) - (Prx * Py1) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1)));
        final double coef = (sign * Math.sqrt(sq));
        final double cx1  = coef * ((radiusX * y1) / radiusY);
        final double cy1  = coef * -((radiusY * x1) / radiusX);

        //
        // Step 3 : Compute (centerX, centerY) from (cx1, cy1)
        //
        final double sx2 = (startX + endX) / 2.0;
        final double sy2 = (startY + endY) / 2.0;
        this.centerX = sx2 + ((cosAngle * cx1) - (sinAngle * cy1));
        this.centerY = sy2 + ((sinAngle * cx1) + (cosAngle * cy1));

        //
        // Step 4 : Compute the angleStart and the angleEnd
        //
        final double ux = (x1 - cx1) / radiusX;
        final double uy = (y1 - cy1) / radiusY;
        final double vx = (-x1 - cx1) / radiusX;
        final double vy = (-y1 - cy1) / radiusY;
        double       p, n;
        // Compute the angle start
        n = Math.sqrt((ux * ux) + (uy * uy));
        p = ux; // (1 * ux) + (0 * uy)
        sign = Math2.sign(uy);
        this.angleStart = sign * Math.acos(p / n);

        // Compute the angle extent
        n = Math.sqrt(((ux * ux) + (uy * uy)) * ((vx * vx) + (vy * vy)));
        p = (ux * vx) + (uy * vy);
        sign = Math2.sign(((ux * vy) - (uy * vx)));
        double angleExtent = sign * Math.acos(p / n);

        if ((sweep == false) && (angleExtent > 0))
        {
            angleExtent -= Math2.TWO_PI;
        }
        else if ((sweep == true) && (angleExtent < 0))
        {
            angleExtent += Math2.TWO_PI;
        }

        this.angleEnd = this.angleStart + angleExtent;
        this.rotation = Matrix.obtainRotateMatrix(-this.centerX, -this.centerY, -angle);

        if ((sweep != largeArc) && (startX < endX) && (startY > endY))
        {
            this.angleEnd -= Math.PI;
            this.angleStart -= Math.PI;
        }

        if ((sweep == largeArc) && (startX < endX) && (startY < endY))
        {
            this.angleEnd -= Math.PI;
            this.angleStart -= Math.PI;
        }

        this.numberStep = precision * (1 + (int) Math.floor((4 * Math.abs(angleExtent)) / Math.PI));
    }

    /**
     * Obtain a line approximate the elliptic arc.<br>
     * The given step is the step of the line, must be in [0, {@link #getNumberStep()} [
     *
     * @param step Step to get the line
     * @return Line for asked step
     */
    public Line getLine(final int step)
    {
        if ((step < 0) || (step >= this.numberStep))
        {
            throw new IllegalArgumentException("step MUST be in [0, " + this.numberStep + "[");
        }

        return new Line(this.getPoint(step), this.getPoint(step + 1));
    }

    /**
     * Number of steps
     *
     * @return Number of steps
     */
    public int getNumberStep()
    {
        return this.numberStep;
    }

    /**
     * Obtain a point for a given step.<br>
     * Step must be in [0, {@link #getNumberStep()} ]
     *
     * @param step Step to get the point
     * @return Point for asked step
     */
    public Point getPoint(final int step)
    {
        if ((step < 0) || (step > this.numberStep))
        {
            throw new IllegalArgumentException("step MUST be in [0, " + this.numberStep + "]");
        }

        if (step == 0)
        {
            return new Point(this.startX, this.startY);
        }

        if (step == this.numberStep)
        {
            return new Point(this.endX, this.endY);
        }

        final double angle = this.angleStart + (((this.angleEnd - this.angleStart) * step) / this.numberStep);
        final double x     = this.centerX + (this.radiusX * Math.cos(angle));
        final double y     = this.centerY + (this.radiusY * Math.sin(angle));
        return this.rotation.transform(new Point(x, y), null);
    }
}