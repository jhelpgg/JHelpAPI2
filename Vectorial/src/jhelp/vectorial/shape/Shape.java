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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jhelp.util.io.json.ObjectJSON;
import jhelp.util.io.json.ValueJSON;
import jhelp.util.list.ArrayInt;
import jhelp.util.math.Math2;
import jhelp.vectorial.VectorialConstants;
import jhelp.vectorial.event.PathChangeListener;
import jhelp.vectorial.event.ShapeChangeListener;
import jhelp.vectorial.math.Point;
import jhelp.vectorial.path.ParsePathException;
import jhelp.vectorial.path.Path;

/**
 * Represents a shape.<br>
 * A shape in composed of a path that describe the border to know what is inside or outside.<br>
 * The path can be complex, for example a small square inside a bigger one :
 * <p>
 * <pre>
 * *********
 * *       *
 * * ##### *
 * * #   # *
 * * #   # *
 * * #   # *
 * * ##### *
 * *       *
 * *********
 * </pre>
 * <p>
 * Then the border will be the * and the #<br>
 * and it will be filled like that :
 * <p>
 * <pre>
 * xxxxxxxxx
 * xxxxxxxxx
 * xxxxxxxxx
 * xxx   xxx
 * xxx   xxx
 * xxx   xxx
 * xxxxxxxxx
 * xxxxxxxxx
 * xxxxxxxxx
 * </pre>
 * <p>
 * Notice that the hole is the inside of the small square
 *
 * @author JHelp
 */
public class Shape
        implements VectorialConstants, PathChangeListener
{
    /**
     * Shape bounding box
     */
    private       Rectangle                 boundingBox;
    /**
     * Indexes of end of "simple" polygons
     */
    private       ArrayInt                  ends;
    /**
     * List of lines
     */
    private       List<Line>                list;
    /**
     * Listeners of shape changes
     */
    private final List<ShapeChangeListener> listeners;
    /**
     * Path to draw
     */
    private final Path                      path;
    /**
     * Actual precision
     */
    private       int                       precision;
    /**
     * Shape scale on X
     */
    private       double                    scaleX;
    /**
     * Shape scale on Y
     */
    private       double                    scaleY;
    /**
     * Shape translate X
     */
    private       double                    x;
    /**
     * Shape translate Y
     */
    private       double                    y;

    /**
     * Create a new instance of Shape on parsing a JSON
     *
     * @param json JSON to parse
     * @throws ParsePathException If JSON not represents a valid shape
     */
    public Shape(ObjectJSON json)
            throws ParsePathException
    {
        this.listeners = new ArrayList<ShapeChangeListener>();

        try
        {
            json = json.get(VectorialConstants.KEY_SHAPE).getObject();
            this.path = new Path(json.get(VectorialConstants.PARAMETER_PATH).getObject());
            this.precision = (int) json.get(VectorialConstants.PARAMETER_PRECISION).getNumber();
            this.scaleX = json.get(VectorialConstants.PARAMETER_SCALE_X).getNumber();
            this.scaleY = json.get(VectorialConstants.PARAMETER_SCALE_Y).getNumber();
            this.x = json.get(VectorialConstants.PARAMETER_X).getNumber();
            this.y = json.get(VectorialConstants.PARAMETER_Y).getNumber();

            this.path.registerPathChangeListener(this);
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Invalid json descrion for Shape : ", json);
        }
    }

    /**
     * Create a new instance of Shape
     *
     * @param path Path to draw
     */
    public Shape(final Path path)
    {
        if (path == null)
        {
            throw new NullPointerException("path mustn't be null");
        }

        this.listeners = new ArrayList<ShapeChangeListener>();
        this.path = path;
        this.x = 0;
        this.y = 0;
        this.scaleX = 1;
        this.scaleY = 1;
        this.precision = 8;

        this.path.registerPathChangeListener(this);
    }

    /**
     * Compute next index for end current polygon
     *
     * @param list  Line list
     * @param start Offset where polygon start
     * @param size  Number of lines
     * @return Offset where end polygon OR -1 if reach the end of the list
     */
    private int endPolygon(final List<Line> list, int start, final int size)
    {
        if ((start + 1) >= size)
        {
            return -1;
        }

        Line   line = list.get(start);
        double x    = line.getEndX();
        double y    = line.getEndY();

        do
        {
            start++;
            line = list.get(start);

            if ((Math2.equals(line.getStartX(), x) == false) || (Math2.equals(line.getStartY(), y) == false))
            {
                return start;
            }

            x = line.getEndX();
            y = line.getEndY();
        }
        while ((start + 1) < size);

        return size;
    }

    /**
     * Signal to listners that shape changed
     */
    private void fireShapeChanged()
    {
        synchronized (this.listeners)
        {
            for (final ShapeChangeListener shapeChangeListener : this.listeners)
            {
                shapeChangeListener.shapeChanged(this);
            }
        }
    }

    /**
     * Indicates if given point is inside a polygon
     *
     * @param x     Point x
     * @param y     Point Y
     * @param list  Lines list
     * @param start Offset where polygon start
     * @param end   Offset where polygon end
     * @return {@code true} if given point is inside the polygon
     */
    private boolean insidePolygon(final double x, final double y, final List<Line> list, final int start, final int end)
    {
        if ((end - start) < 3)
        {
            return false;
        }

        Line   line  = list.get(end - 1);
        double lastx = line.getStartX();
        double lasty = line.getStartY();
        int    hits  = 0;
        double curx, cury, leftx, test1, test2;

        for (int i = start; i < end; lastx = curx, lasty = cury, i++)
        {
            line = list.get(i);
            curx = line.getStartX();
            cury = line.getStartY();

            if (Math2.equals(cury, lasty) == true)
            {
                continue;
            }

            if (curx < lastx)
            {
                if (x >= lastx)
                {
                    continue;
                }

                leftx = curx;
            }
            else
            {
                if (x >= curx)
                {
                    continue;
                }

                leftx = lastx;
            }

            if (cury < lasty)
            {
                if ((y < cury) || (y >= lasty))
                {
                    continue;
                }

                if (x < leftx)
                {
                    hits++;
                    continue;
                }

                test1 = x - curx;
                test2 = y - cury;
            }
            else
            {
                if ((y < lasty) || (y >= cury))
                {
                    continue;
                }

                if (x < leftx)
                {
                    hits++;
                    continue;
                }

                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < ((test2 / (lasty - cury)) * (lastx - curx)))
            {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    /**
     * Reset all precomputed objects
     */
    private void resetComputings()
    {
        this.list = null;
        this.ends = null;
        this.boundingBox = null;
        this.fireShapeChanged();
    }

    /**
     * Draw the shape
     *
     * @param shapeDrawer Shape drawer, that will receive lines to draw
     */
    public void drawShape(final ShapeDrawer shapeDrawer)
    {
        shapeDrawer.drawShapeStart(this);

        for (final Line line : this.toPolyline())
        {
            shapeDrawer.drawShapeSegment(this, line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        }

        shapeDrawer.drawShapeEnd(this);
    }

    /**
     * Fill the shape
     *
     * @param shapeFiller Shape fill that will fill the shape
     */
    public void fillShape(final ShapeFiller shapeFiller)
    {
        final Rectangle work   = this.getBoundingBox().intersects(shapeFiller.obtainDrawingAreaBounds(this));
        final int       xStart = (int) Math.floor(work.getX());
        final int       xEnd   = (int) Math.ceil(work.getX() + work.getWidth());
        final int       yStart = (int) Math.floor(work.getY());
        final int       yEnd   = (int) Math.ceil(work.getY() + work.getHeight());

        shapeFiller.fillShapeStart(this, work);

        for (int y = yStart; y < yEnd; y++)
        {
            for (int x = xStart; x < xEnd; x++)
            {
                if (this.insideSupposeInsideBox(x, y) == true)
                {
                    shapeFiller.fillShapePixel(this, x, y);
                }
            }
        }

        shapeFiller.fillShapeEnd(this);
    }

    /**
     * Shape bounding box
     *
     * @return Shape bounding box
     */
    public Rectangle getBoundingBox()
    {
        if (this.boundingBox != null)
        {
            return new Rectangle(this.boundingBox);
        }

        this.toPolyline();
        return new Rectangle(this.boundingBox);
    }

    /**
     * Shape position
     *
     * @return Shape position
     */
    public Point getPosition()
    {
        return new Point(this.x, this.y);
    }

    /**
     * change shape position
     *
     * @param position New position
     */
    public void setPosition(final Point position)
    {
        this.setPosition(position.getX(), position.getY());
    }

    /**
     * Current precision
     *
     * @return Current precision
     */
    public int getPrecision()
    {
        return this.precision;
    }

    /**
     * Change shape precision
     *
     * @param precision New precision
     */
    public void setPrecision(int precision)
    {
        precision = Math.max(8, precision);

        if (this.precision == precision)
        {
            return;
        }

        this.precision = precision;
        this.resetComputings();
    }

    /**
     * X scale
     *
     * @return X scale
     */
    public double getScaleX()
    {
        return this.scaleX;
    }

    /**
     * Y scale
     *
     * @return Y scale
     */
    public double getScaleY()
    {
        return this.scaleY;
    }

    /**
     * Shape X location
     *
     * @return Shape X location
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * Change shape X
     *
     * @param x New X
     */
    public void setX(final double x)
    {
        this.setPosition(x, this.y);
    }

    /**
     * Shape Y location
     *
     * @return Shape Y location
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * Change shape Y
     *
     * @param y New Y
     */
    public void setY(final double y)
    {
        this.setPosition(this.x, y);
    }

    /**
     * Indicates if given point inside the shape
     *
     * @param x Point X
     * @param y Point Y
     * @return {@code true} if given point inside the shape
     */
    public boolean inside(final double x, final double y)
    {
        if (this.getBoundingBox().isInside(x, y) == false)
        {
            return false;
        }

        return this.insideSupposeInsideBox(x, y);
    }

    /**
     * Indicates if given point inside the shape.<br>
     * Call it if you are sure that point inside bounding box
     *
     * @param x Point X
     * @param y Point Y
     * @return {@code true} if given point inside the shape
     */
    public boolean insideSupposeInsideBox(final double x, final double y)
    {
        final List<Line> list = this.toPolyline();

        final int size  = list.size();
        int       start = 0;

        int     indexEnd = 0;
        boolean fillEnds = false;

        if (this.ends == null)
        {
            fillEnds = true;
            this.ends = new ArrayInt();
        }

        int end;

        if (fillEnds == true)
        {
            end = this.endPolygon(list, start, size);
            this.ends.add(end);
        }
        else
        {
            end = this.ends.getInteger(indexEnd);
            indexEnd++;
        }

        int numberInside = 0;

        do
        {
            if (this.insidePolygon(x, y, list, start, end) == true)
            {
                numberInside++;
            }

            start = end;

            if (fillEnds == true)
            {
                end = this.endPolygon(list, start, size);
                this.ends.add(end);
            }
            else
            {
                end = this.ends.getInteger(indexEnd);
                indexEnd++;
            }
        }
        while (end >= 0);

        return (numberInside & 1) != 0;
    }

    /**
     * Called when the embed path changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param path Changed path
     * @see PathChangeListener#pathChanged(Path)
     */
    @Override
    public void pathChanged(final Path path)
    {
        this.resetComputings();
    }

    /**
     * Register a shape change listener
     *
     * @param shapeChangeListener Listener to register
     */
    public void registerShapeChangeListener(final ShapeChangeListener shapeChangeListener)
    {
        if (shapeChangeListener == null)
        {
            throw new NullPointerException("shapeChangeListener mustn't be null");
        }

        synchronized (this.listeners)
        {
            if (this.listeners.contains(shapeChangeListener) == false)
            {
                this.listeners.add(shapeChangeListener);
            }
        }
    }

    /**
     * Change shape location
     *
     * @param x New X
     * @param y New Y
     */
    public void setPosition(final double x, final double y)
    {
        if ((Math2.equals(this.x, x) == true) && (Math2.equals(this.y, y) == true))
        {
            return;
        }

        this.x = x;
        this.y = y;
        this.resetComputings();
    }

    /**
     * Change shape scale in X and Y in same time
     *
     * @param scale New scale for X and Y
     */
    public void setScale(final double scale)
    {
        this.setScale(scale, scale);
    }

    /**
     * Change shape scale
     *
     * @param scaleX Scale on X
     * @param scaleY Scale on Y
     */
    public void setScale(final double scaleX, final double scaleY)
    {
        if ((Math2.sign(scaleX) <= 0) || (Math2.sign(scaleY) <= 0))
        {
            throw new IllegalArgumentException("Scales MUST be > 0 , not " + scaleX + "x" + scaleY);
        }

        if ((Math2.equals(this.scaleX, scaleX) == true) && (Math2.equals(this.scaleY, scaleY) == true))
        {
            return;
        }

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.resetComputings();
    }

    /**
     * Serialize shape to JSON
     *
     * @return Serialized shape
     */
    public ObjectJSON toJSON()
    {
        final ObjectJSON value = new ObjectJSON();
        value.put(VectorialConstants.PARAMETER_PATH, ValueJSON.newValue(this.path.toJSON()));
        value.put(VectorialConstants.PARAMETER_PRECISION, ValueJSON.newValue(this.precision));
        value.put(VectorialConstants.PARAMETER_SCALE_X, ValueJSON.newValue(this.scaleX));
        value.put(VectorialConstants.PARAMETER_SCALE_Y, ValueJSON.newValue(this.scaleY));
        value.put(VectorialConstants.PARAMETER_X, ValueJSON.newValue(this.x));
        value.put(VectorialConstants.PARAMETER_Y, ValueJSON.newValue(this.y));

        final ObjectJSON json = new ObjectJSON();
        json.put(VectorialConstants.KEY_SHAPE, ValueJSON.newValue(value));
        return json;
    }

    /**
     * Obtain the lines for shape border.<br>
     * The number of lines change with precision.<br>
     * More the precision is big more accurate is the border, but more lines are necessary
     *
     * @return Lines for shape border.
     */
    public List<Line> toPolyline()
    {
        if (this.list != null)
        {
            return this.list;
        }

        final List<Line> list = new ArrayList<Line>();
        final LineCollector lineCollector = new LineCollector(list, this.x, this.y, this.scaleX, this.scaleY,
                                                              this.precision);
        this.path.drawPath(lineCollector);
        this.list = Collections.unmodifiableList(list);
        this.boundingBox = lineCollector.getBoundingBox();
        return this.list;
    }

    /**
     * Translate the shape
     *
     * @param x Translate in X
     * @param y Translate in Y
     */
    public void translate(final double x, final double y)
    {
        this.setPosition(this.x + x, this.y + y);
    }

    /**
     * Unregister listener of shape changes
     *
     * @param shapeChangeListener Listener to unregister
     */
    public void unregisterShapeChangeListener(final ShapeChangeListener shapeChangeListener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(shapeChangeListener);
        }
    }
}