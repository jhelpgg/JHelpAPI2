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

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import jhelp.util.gui.ConstantsGUI;
import jhelp.util.io.json.ArrayJSON;
import jhelp.util.io.json.ObjectJSON;
import jhelp.util.io.json.ValueJSON;
import jhelp.util.list.Pair;
import jhelp.util.math.Math2;
import jhelp.util.util.HashCode;
import jhelp.vectorial.VectorialConstants;
import jhelp.vectorial.event.PathChangeListener;
import jhelp.vectorial.event.PathElementChangeListener;
import jhelp.vectorial.math.Point;

/**
 * Represents a path
 *
 * @author JHelp
 */
public class Path
        implements VectorialConstants, PathElementChangeListener
{
    /**
     * Create an arc path
     *
     * @param x          Up-left corner X
     * @param y          Up-left corner Y
     * @param width      Width
     * @param height     Height
     * @param angleStart Starting angle
     * @param angleTotal Arc total angle
     * @param arcMode    Arc mode
     * @return Created path
     */
    public static Path createArc(
            final double x, final double y, final double width, final double height, final double angleStart,
            final double angleTotal,
            final ArcMode arcMode)
    {
        return Path.createCenterArc(x - (width / 2), y - (height / 2), width / 2, height / 2, angleStart,
                                    angleStart + angleTotal, arcMode);
    }

    /**
     * Create an arc path
     *
     * @param centerX    Arc center X
     * @param centerY    Arc center Y
     * @param radiusX    Radius in X
     * @param radiusY    Radius in Y
     * @param angleStart Starting angle
     * @param angleEnd   Ending angle
     * @param arcMode    Arc mode
     * @return Created path
     */
    public static Path createCenterArc(
            final double centerX, final double centerY, final double radiusX, final double radiusY, double angleStart,
            double angleEnd, final ArcMode arcMode)
    {
        angleStart = Math2.modulo(angleStart, Math2.TWO_PI);
        angleEnd = Math2.modulo(angleEnd, Math2.TWO_PI);

        final Path path = new Path();

        final double startX = centerX + (radiusX * Math.cos(angleStart));
        final double startY = centerY + (radiusY * Math.sin(angleStart));
        final double endX   = centerX + (radiusX * Math.cos(angleEnd));
        final double endY   = centerY + (radiusY * Math.sin(angleEnd));
        boolean      large  = Math2.modulo(Math.abs(angleEnd - angleStart), Math2.TWO_PI) > Math.PI;

        if (angleStart > angleEnd)
        {
            large = !large;
        }

        path.addElement(new MoveTo(false, startX, startY));
        path.addElement(new EllipticalArcTo(false, radiusX, radiusY, 0, large, true, endX, endY));

        if (arcMode == ArcMode.PIE)
        {
            path.addElement(new LineTo(false, centerX, centerY));
        }

        if (arcMode != ArcMode.OPEN)
        {
            path.addElement(ClosePath.CLOSE_PATH);
        }

        return path;
    }

    /**
     * Create a circle path
     *
     * @param centerX Circle center X
     * @param centerY Circle center Y
     * @param radius  Circle radius
     * @return Created path
     */
    public static Path createCenterCircle(final double centerX, final double centerY, final double radius)
    {
        return Path.createEllipse(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    /**
     * Create an ellipse path
     *
     * @param centerX Ellipse center X
     * @param centerY Ellipse center Y
     * @param radiusX Radius in X
     * @param radiusY Radius in Y
     * @return Created path
     */
    public static Path createCenterEllipse(
            final double centerX, final double centerY, final double radiusX, final double radiusY)
    {
        return Path.createEllipse(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);
    }

    /**
     * Create a rectangle path
     *
     * @param centerX Rectangle center X
     * @param centerY Rectangle center Y
     * @param width   Width
     * @param height  Height
     * @return Created path
     */
    public static Path createCenterRectangle(
            final double centerX, final double centerY, final double width, final double height)
    {
        return Path.createRectangle(centerX - (width / 2), centerY - (height / 2), width, height);
    }

    /**
     * Create a square path
     *
     * @param centerX Square center X
     * @param centerY Square center Y
     * @param size    Square size
     * @return Created path
     */
    public static Path createCenterSquare(final double centerX, final double centerY, final double size)
    {
        return Path.createRectangle(centerX - (size / 2), centerY - (size / 2), size, size);
    }

    /**
     * Create a circle path
     *
     * @param x    Up-left corner X
     * @param y    Up-left corner Y
     * @param size Square size
     * @return Created path
     */
    public static Path createCircle(final double x, final double y, final double size)
    {
        return Path.createEllipse(x, y, size, size);
    }

    /**
     * Create an ellipse path
     *
     * @param x      Up-left corner X
     * @param y      Up-left corner Y
     * @param width  Width
     * @param height Height
     * @return Created path
     */
    public static Path createEllipse(final double x, final double y, final double width, final double height)
    {
        final Path path = new Path();

        path.addElement(new MoveTo(false, x, y + (height / 2)));
        path.addElement(new EllipticalArcTo(true, width / 2, height / 2, 0, true, false, width, 0));
        path.addElement(new EllipticalArcTo(true, width / 2, height / 2, 0, true, false, -width, 0));

        return path;
    }

    /**
     * Create path from glyph vector
     *
     * @param glyphVector Glyph vector to transform
     * @return Created path
     */
    public static Path createGlyphVector(final GlyphVector glyphVector)
    {
        return Path.createJavaShape(glyphVector.getOutline());
    }

    /**
     * Create path from shape (awt)
     *
     * @param shape Shape to transform
     * @return Created path
     */
    public static Path createJavaShape(final Shape shape)
    {
        return Path.createPathIterator(shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM));
    }

    /**
     * Create path from path iterator
     *
     * @param pathIterator Path iterator to transform
     * @return Created path
     */
    public static Path createPathIterator(final PathIterator pathIterator)
    {
        final Path     path   = new Path();
        final double[] coords = new double[6];

        while (pathIterator.isDone() == false)
        {
            switch (pathIterator.currentSegment(coords))
            {
                case PathIterator.SEG_CLOSE:
                    path.closePath();
                    break;
                case PathIterator.SEG_CUBICTO:
                    path.cubicBezierTo(false, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_LINETO:
                    path.lineTo(false, coords[0], coords[1]);
                    break;
                case PathIterator.SEG_MOVETO:
                    path.moveTo(false, coords[0], coords[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    path.quadricBezierTo(false, coords[0], coords[1], coords[2], coords[3]);
                    break;
            }

            pathIterator.next();
        }

        return path;
    }

    /**
     * Create path form list of points, to make polygon or polyline
     *
     * @param close  Indicates if have close the polyline (to become a polygon)
     * @param points Points list
     * @return Created path
     */
    public static Path createPolyline(final boolean close, final Point... points)
    {
        final int  size = points.length;
        final Path path = new Path();

        if (size > 0)
        {
            Point point = points[0];
            path.addElement(new MoveTo(false, point.getX(), point.getY()));

            for (int index = 1; index < size; index++)
            {
                point = points[index];
                path.addElement(new LineTo(false, point.getX(), point.getY()));
            }

            if ((close == true) && (size >= 3))
            {
                path.addElement(ClosePath.CLOSE_PATH);
            }
        }

        return path;
    }

    /**
     * Create rectangle path
     *
     * @param x      Up-left corner X
     * @param y      Up-left corner Y
     * @param width  Width
     * @param height Height
     * @return Created path
     */
    public static Path createRectangle(final double x, final double y, final double width, final double height)
    {
        final Path path = new Path();

        path.addElement(new MoveTo(false, x, y));
        path.addElement(new LineTo(true, width, 0));
        path.addElement(new LineTo(true, 0, height));
        path.addElement(new LineTo(true, -width, 0));
        path.addElement(ClosePath.CLOSE_PATH);

        return path;
    }

    /**
     * Create round rectangle path
     *
     * @param x         Up-left corner X
     * @param y         Up-left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @return Created path
     */
    public static Path createRoundRectangle(
            final double x, final double y, final double width, final double height, double arcWidth, double arcHeight)
    {
        arcWidth = Math.min(arcWidth, width / 2);
        arcHeight = Math.min(arcHeight, height / 2);
        final double x1 = x + arcWidth;
        final double x2 = (x + width) - arcWidth;
        final double y1 = y + arcHeight;
        final double y2 = (y + height) - arcHeight;
        final double xx = x + width;
        final double yy = y + height;

        final Path path = new Path();

        path.moveTo(false, x, y1);
        path.ellipticalArcTo(false, arcWidth, arcHeight, 0, false, true, x1, y);
        path.lineTo(false, x2, y);
        path.ellipticalArcTo(false, arcWidth, arcHeight, 0, false, true, xx, y1);
        path.lineTo(false, xx, y2);
        path.ellipticalArcTo(false, arcWidth, arcHeight, 0, false, true, x2, yy);
        path.lineTo(false, x1, yy);
        path.ellipticalArcTo(false, arcWidth, arcHeight, 0, false, true, x, y2);
        path.closePath();

        return path;
    }

    /**
     * Create square path
     *
     * @param x    Up-left corner X
     * @param y    Up-left corner Y
     * @param size Square size
     * @return Created path
     */
    public static Path createSquare(final double x, final double y, final double size)
    {
        return Path.createRectangle(x, y, size, size);
    }

    /**
     * Create text path
     *
     * @param font Font to use
     * @param text Text
     * @return Created path
     */
    public static Path createText(final Font font, final String text)
    {
        return Path.createGlyphVector(font.createGlyphVector(ConstantsGUI.FONT_RENDER_CONTEXT, text));
    }

    /**
     * Indicates if given character is a part of a number
     *
     * @param character Tested character
     * @return {@code true} if given character is a part of a number
     */
    private static boolean isNumberCharacter(final char character)
    {
        return ((character >= '0') && (character <= '9')) || (character == '.') || (character == '-') ||
               (character == '+') || (character == 'e')
               || (character == 'E');
    }

    /**
     * Jump to next meaning character inside characters array
     *
     * @param index      Offset where start reading characters array
     * @param characters Characters array
     * @param length     Characters array length
     * @return Offset of next meaning character
     */
    private static int jumpToNextMeaningCharacter(int index, final char[] characters, final int length)
    {
        while ((index < length) && ((characters[index] <= 32) || (characters[index] == ',')))
        {
            index++;
        }

        return index;
    }

    /**
     * Create path from string.<br>
     * String respects the same format as SVG path
     *
     * @param string String to parse
     * @return Created path
     */
    public static Path parsePath(final String string)
    {
        final Path path = new Path();
        path.parse(string);
        return path;
    }

    /**
     * Read next number form character array
     *
     * @param index      Offset where start reading characters array
     * @param characters Characters array
     * @param length     Characters array length
     * @return Couple of offset after the number and the number read
     */
    private static Pair<Integer, Double> readNextNumber(int index, final char[] characters, final int length)
    {
        index = Path.jumpToNextMeaningCharacter(index, characters, length);

        if (index >= length)
        {
            return new Pair<Integer, Double>(length, 0.0);
        }

        final int start = index;

        while ((index < length) && (Path.isNumberCharacter(characters[index]) == true))
        {
            index++;
        }

        return new Pair<Integer, Double>(index, Double.parseDouble(new String(characters, start, index - start)));
    }

    /**
     * Listeners of path changes
     */
    private final List<PathChangeListener> listeners;
    /**
     * Path elements
     */
    private final List<PathElement>        path;

    /**
     * Create a new instance of Path
     */
    public Path()
    {
        this.path = new ArrayList<PathElement>();
        this.listeners = new ArrayList<PathChangeListener>();
    }

    /**
     * Create a new instance of Path
     *
     * @param json JSON to parse
     * @throws ParsePathException If JSON not valid
     */
    public Path(final ObjectJSON json)
            throws ParsePathException
    {
        this();

        try
        {
            final ArrayJSON arrayJSON = json.get(VectorialConstants.KEY_PATH).getArray();
            final int       length    = arrayJSON.numberOfValue();

            for (int element = 0; element < length; element++)
            {
                this.addElement(PathElement.parse(arrayJSON.getValue(element).getObject()));
            }
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Invalid path description : ", json);
        }
    }

    /**
     * Signal to listeners that path changed
     */
    private void firePathChanged()
    {
        synchronized (this.listeners)
        {
            for (final PathChangeListener pathChangeListener : this.listeners)
            {
                pathChangeListener.pathChanged(this);
            }
        }
    }

    /**
     * Add element
     *
     * @param element Element to add
     */
    public void addElement(final PathElement element)
    {
        if (element == null)
        {
            throw new NullPointerException("element mustn't be null");
        }

        element.registerPathElementChangeListener(this);
        this.path.add(element);
        this.firePathChanged();
    }

    /**
     * Append a path to this path
     *
     * @param path Path to append
     */
    public void append(final Path path)
    {
        for (final PathElement element : path.path)
        {
            element.registerPathElementChangeListener(this);
            this.addElement(element);
        }

        this.firePathChanged();
    }

    /**
     * Clear the path
     */
    public void clearPath()
    {
        for (final PathElement element : this.path)
        {
            element.unregisterPathElementChangeListener(this);
        }

        this.path.clear();
        this.firePathChanged();
    }

    /**
     * Close current sub path
     */
    public void closePath()
    {
        this.addElement(ClosePath.CLOSE_PATH);
    }

    /**
     * Copy a path
     *
     * @param path Path to copy
     */
    public void copy(final Path path)
    {
        if (path == null)
        {
            throw new NullPointerException("path mustn't be null");
        }

        this.clearPath();

        for (final PathElement element : path.path)
        {
            element.registerPathElementChangeListener(this);
            this.path.add(element);
        }

        this.firePathChanged();
    }

    /**
     * Append cubic curve
     *
     * @param relative            Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                            )
     * @param firstControlPointX  First control point X
     * @param firstControlPointY  First control point Y
     * @param secondControlPointX Second control point X
     * @param secondControlPointY Second control point Y
     * @param x                   Final point X
     * @param y                   Final point Y
     */
    public void cubicBezierTo(
            final boolean relative, final double firstControlPointX, final double firstControlPointY,
            final double secondControlPointX,
            final double secondControlPointY, final double x, final double y)
    {
        this.addElement(new CubicBezierTo(relative, firstControlPointX, firstControlPointY, secondControlPointX,
                                          secondControlPointY, x, y));
    }

    /**
     * Draw the path
     *
     * @param pathDrawer Path drawer where draw the path
     */
    public void drawPath(final PathDrawer pathDrawer)
    {
        double firstX     = 0;
        double firstY     = 0;
        double referenceX = 0;
        double referenceY = 0;
        Point  point;

        for (final PathElement element : this.path)
        {
            point = element.draw(pathDrawer, referenceX, referenceY);
            referenceX = point.getX();
            referenceY = point.getY();

            if (element == ClosePath.CLOSE_PATH)
            {
                referenceX = firstX;
                referenceY = firstY;
            }
            else if (element instanceof MoveTo)
            {
                firstX = referenceX;
                firstY = referenceY;
            }
        }
    }

    /**
     * Add an elliptic arc.<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
     *
     * @param relative              Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                              )
     * @param radiusX               Radius in X
     * @param radiusY               Radius in Y
     * @param rotationAxisXInDegree Rotation around X axis in degree
     * @param largeArc              Indicates if use the large arc
     * @param sweep                 Indicates if use sweep side
     * @param x                     Final point X
     * @param y                     Final point Y
     */
    public void ellipticalArcTo(
            final boolean relative, final double radiusX, final double radiusY, final double rotationAxisXInDegree,
            final boolean largeArc,
            final boolean sweep, final double x, final double y)
    {
        this.addElement(new EllipticalArcTo(relative, radiusX, radiusY, rotationAxisXInDegree, largeArc, sweep, x, y));
    }

    /**
     * Obtain a path element
     *
     * @param index Path element index
     * @return Path element
     */
    public PathElement getElement(final int index)
    {
        return this.path.get(index);
    }

    /**
     * Hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.path);
    }

    /**
     * Indicates if given object equals to this path <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this path
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }

        if (object == null)
        {
            return false;
        }

        if ((object instanceof Path) == false)
        {
            return false;
        }

        final Path path   = (Path) object;
        final int  length = this.path.size();

        if (path.path.size() != length)
        {
            return false;
        }

        for (int index = 0; index < length; index++)
        {
            if (this.path.get(index).equals(path.path.get(index)) == false)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Create line to given point
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param x        Point destination X
     * @param y        Point destination Y
     */
    public void lineTo(final boolean relative, final double x, final double y)
    {
        this.addElement(new LineTo(relative, x, y));
    }

    /**
     * Move current cursor
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param x        New position X
     * @param y        New position Y
     */
    public void moveTo(final boolean relative, final double x, final double y)
    {
        this.addElement(new MoveTo(relative, x, y));
    }

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    public int numberOfElements()
    {
        return this.path.size();
    }

    /**
     * Parse string to make the path.<br>
     * Same format as SVG path
     *
     * @param string String to parse
     */
    public void parse(final String string)
    {
        this.clearPath();

        final char[]          characters   = string.trim().toCharArray();
        double                firstX       = 0;
        double                firstY       = 0;
        double                referenceX   = 0;
        double                referenceY   = 0;
        double                cubicSecondX = 0;
        double                cubicSecondY = 0;
        double                quadricX     = 0;
        double                quadricY     = 0;
        boolean               relative     = false;
        final int             length       = characters.length;
        int                   index        = 0;
        char                  character;
        Pair<Integer, Double> pair;
        final double[]        values       = new double[7];
        int                   numberToRead;
        char                  nextImplicit;

        while (index < length)
        {
            character = characters[index];
            index++;
            nextImplicit = character;

            do
            {
                numberToRead = 0;

                switch (character)
                {
                    case 'M':
                        numberToRead = 2;
                        nextImplicit = 'L';
                        relative = false;
                        break;
                    case 'L':
                        numberToRead = 2;
                        relative = false;
                        break;
                    case 'H':
                        numberToRead = 1;
                        relative = false;
                        break;
                    case 'V':
                        numberToRead = 1;
                        relative = false;
                        break;
                    case 'C':
                        numberToRead = 6;
                        relative = false;
                        break;
                    case 'S':
                        numberToRead = 4;
                        relative = false;
                        break;
                    case 'Q':
                        numberToRead = 4;
                        relative = false;
                        break;
                    case 'T':
                        numberToRead = 2;
                        relative = false;
                        break;
                    case 'A':
                        numberToRead = 7;
                        relative = false;
                        break;
                    case 'm':
                        numberToRead = 2;
                        nextImplicit = 'l';
                        relative = true;
                        break;
                    case 'l':
                        numberToRead = 2;
                        relative = true;
                        break;
                    case 'h':
                        numberToRead = 1;
                        relative = true;
                        break;
                    case 'v':
                        numberToRead = 1;
                        relative = true;
                        break;
                    case 'c':
                        numberToRead = 6;
                        relative = true;
                        break;
                    case 's':
                        numberToRead = 4;
                        relative = true;
                        break;
                    case 'q':
                        numberToRead = 4;
                        relative = true;
                        break;
                    case 't':
                        numberToRead = 2;
                        relative = true;
                        break;
                    case 'a':
                        numberToRead = 7;
                        relative = true;
                        break;
                }

                for (int read = 0; read < numberToRead; read++)
                {
                    pair = Path.readNextNumber(index, characters, length);
                    index = pair.first;
                    values[read] = pair.second;
                }

                switch (character)
                {
                    case 'Z':
                    case 'z':
                        this.addElement(ClosePath.CLOSE_PATH);
                        referenceX = firstX;
                        referenceY = firstY;
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'M':
                    case 'm':
                        this.addElement(new MoveTo(relative, values[0], values[1]));
                        referenceX = values[0];
                        referenceY = values[1];
                        firstX = referenceX;
                        firstY = referenceY;
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'L':
                    case 'l':
                        this.addElement(new LineTo(relative, values[0], values[1]));
                        referenceX = values[0];
                        referenceY = values[1];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'H':
                    case 'h':
                        this.addElement(new LineTo(relative, values[0], referenceY));
                        referenceX = values[0];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'V':
                    case 'v':
                        this.addElement(new LineTo(relative, referenceX, values[0]));
                        referenceY = values[0];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'C':
                    case 'c':
                        this.addElement(
                                new CubicBezierTo(relative, values[0], values[1], values[2], values[3], values[4],
                                                  values[5]));
                        referenceX = values[4];
                        referenceY = values[5];
                        cubicSecondX = values[2];
                        cubicSecondY = values[3];
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'S':
                    case 's':
                        this.addElement(
                                new CubicBezierTo(relative, referenceX + cubicSecondX, referenceY + cubicSecondY,
                                                  values[0], values[1], values[2], values[3]));
                        referenceX = values[2];
                        referenceY = values[3];
                        cubicSecondX = values[0];
                        cubicSecondY = values[1];
                        quadricX = 0;
                        quadricY = 0;
                        break;
                    case 'Q':
                    case 'q':
                        this.addElement(new QuadricBezierTo(relative, values[0], values[1], values[2], values[3]));
                        referenceX = values[2];
                        referenceY = values[3];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = values[0];
                        quadricY = values[1];
                        break;
                    case 'T':
                    case 't':
                        this.addElement(
                                new QuadricBezierTo(relative, referenceX + quadricX, referenceY + quadricY, values[0],
                                                    values[1]));
                        referenceX = values[0];
                        referenceY = values[1];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = referenceX + quadricX;
                        quadricY = referenceY + quadricY;
                        break;
                    case 'A':
                    case 'a':
                        this.addElement(new EllipticalArcTo(relative, values[0], values[1], values[2],
                                                            Math2.equals(values[3], 1), Math2.equals(values[4], 1),
                                                            values[5], values[6]));
                        referenceX = values[5];
                        referenceY = values[6];
                        cubicSecondX = 0;
                        cubicSecondY = 0;
                        quadricX = 0;
                        quadricY = 0;
                        break;
                }

                character = nextImplicit;
                index = Path.jumpToNextMeaningCharacter(index, characters, length);
            }
            while ((index < length) && (Path.isNumberCharacter(characters[index]) == true));
        }

        this.firePathChanged();
    }

    /**
     * Called when a path element changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathElement Path element changed
     * @see jhelp.vectorial.event.PathElementChangeListener#pathElementChanged(jhelp.vectorial.path.PathElement)
     */
    @Override
    public void pathElementChanged(final PathElement pathElement)
    {
        this.firePathChanged();
    }

    /**
     * Append a quadric curve
     *
     * @param relative      Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                      )
     * @param controlPointX Control point X
     * @param controlPointY Control point Y
     * @param x             Final point X
     * @param y             Final point Y
     */
    public void quadricBezierTo(
            final boolean relative, final double controlPointX, final double controlPointY, final double x,
            final double y)
    {
        this.addElement(new QuadricBezierTo(relative, controlPointX, controlPointY, x, y));
    }

    /**
     * Register a path change listener
     *
     * @param pathChangeListener Listener to register
     */
    public void registerPathChangeListener(final PathChangeListener pathChangeListener)
    {
        if (pathChangeListener == null)
        {
            throw new NullPointerException("pathChangeListener mustn't be null");
        }

        synchronized (this.listeners)
        {
            if (this.listeners.contains(pathChangeListener) == false)
            {
                this.listeners.add(pathChangeListener);
            }
        }
    }

    /**
     * Serialize the path to JSON
     *
     * @return Parsed JSON
     */
    public ObjectJSON toJSON()
    {
        final ArrayJSON arrayJSON = new ArrayJSON();

        for (final PathElement pathElement : this.path)
        {
            arrayJSON.addValue(ValueJSON.newValue(pathElement.toJSON()));
        }

        final ObjectJSON json = new ObjectJSON();
        json.put(VectorialConstants.KEY_PATH, ValueJSON.newValue(arrayJSON));
        return json;
    }

    /**
     * Unregister listener
     *
     * @param pathChangeListener Listener to unregister
     */
    public void unregisterPathChangeListener(final PathChangeListener pathChangeListener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(pathChangeListener);
        }
    }
}