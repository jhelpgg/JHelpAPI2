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
package jhelp.vectorial.path;

import jhelp.vectorial.shape.EllipticArcToCrawler;

/**
 * Generic path drawer, for draw a path
 *
 * @author JHelp
 */
public interface PathDrawer
{
    /**
     * Close current path
     */
    void closePath();

    /**
     * Append a cubic curve
     *
     * @param firstControlPointX  First control point X
     * @param firstControlPointY  First control point Y
     * @param secondControlPointX Second control point X
     * @param secondControlPointY Second control point Y
     * @param x                   Final point X
     * @param y                   Final point Y
     */
    void cubicBezierTo(
            double firstControlPointX, double firstControlPointY, double secondControlPointX,
            double secondControlPointY,
            double x, double y);

    /**
     * Draw an elliptic arc<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple<br>
     * {@link EllipticArcToCrawler} can help to extract lines from elliptic arc
     *
     * @param radiusX               Radius on X
     * @param radiusY               Radius on Y
     * @param rotationAxisXInDegree Rotation around X axis express in degree
     * @param largeArc              Indicates if use large arc
     * @param sweep                 Indicates if use sweep side
     * @param x                     Final point X
     * @param y                     Final point Y
     */
    void ellipticalArcTo(
            double radiusX, double radiusY, double rotationAxisXInDegree, boolean largeArc, boolean sweep, double x,
            double y);

    /**
     * Append a line
     *
     * @param x Final point X
     * @param y Final point Y
     */
    void lineTo(double x, double y);

    /**
     * Move the cursor without drawing
     *
     * @param x Destination point X
     * @param y Destination point Y
     */
    void moveTo(double x, double y);

    /**
     * Append a quadric curve
     *
     * @param controlPointX Control point X
     * @param controlPointY Control point Y
     * @param x             Final point X
     * @param y             Final point Y
     */
    void quadricBezierTo(double controlPointX, double controlPointY, double x, double y);
}