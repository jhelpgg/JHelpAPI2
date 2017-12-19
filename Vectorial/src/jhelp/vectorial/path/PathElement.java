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

import java.util.ArrayList;
import java.util.List;
import jhelp.util.io.json.ObjectJSON;
import jhelp.util.io.json.ValueJSON;
import jhelp.util.util.HashCode;
import jhelp.vectorial.VectorialConstants;
import jhelp.vectorial.event.PathElementChangeListener;
import jhelp.vectorial.math.Point;

/**
 * Path element
 *
 * @author JHelp
 */
public abstract class PathElement
        implements VectorialConstants
{
    /**
     * Parse JSON
     *
     * @param json JSON to parse
     * @return Parsed element
     * @throws ParsePathException If JSON not valid
     */
    public static PathElement parse(final ObjectJSON json) throws ParsePathException
    {
        try
        {
            final String     name  = json.getKeys().iterator().next();
            final ObjectJSON value = json.get(name).getObject();

            if (VectorialConstants.KEY_PATH_ELEMENT_CLOSE_PATH.equals(name) == true)
            {
                return ClosePath.CLOSE_PATH;
            }

            final boolean relative = value.get(VectorialConstants.PARAMETER_RELATIVE).getBoolean();

            if (VectorialConstants.KEY_PATH_ELEMENT_CUBIC_BEZIER_TO.equals(name) == true)
            {
                return new CubicBezierTo(relative, value);
            }

            if (VectorialConstants.KEY_PATH_ELEMENT_ELLIPTICAL_ARC_TO.equals(name) == true)
            {
                return new EllipticalArcTo(relative, value);
            }

            if (VectorialConstants.KEY_PATH_ELEMENT_LINE_TO.equals(name) == true)
            {
                return new LineTo(relative, value);
            }

            if (VectorialConstants.KEY_PATH_ELEMENT_MOVE_TO.equals(name) == true)
            {
                return new MoveTo(relative, value);
            }

            if (VectorialConstants.KEY_PATH_ELEMENT_QUADRIC_BEZIER_TO.equals(name) == true)
            {
                return new QuadricBezierTo(relative, value);
            }

            throw new ParsePathException("Invalid path element type : ", name);
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Invalid path element description : ", json);
        }
    }

    /**
     * Path element changes listeners
     */
    private final List<PathElementChangeListener> listeners;
    /**
     * Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false})
     */
    private       boolean                         relative;

    /**
     * Create a new instance of PathElement
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     */
    public PathElement(final boolean relative)
    {
        this.listeners = new ArrayList<PathElementChangeListener>();
        this.relative = relative;
    }

    /**
     * Indicates if path element equals to this one.<br>
     * Given path element is good typed, you can cast it safely
     *
     * @param pathElement Path element to compare good typed, you can cast it safely
     * @return {@code true} if path element equals to this one.
     */
    protected abstract boolean equalsIntern(PathElement pathElement);

    /**
     * Fill JSON specific parameters
     *
     * @param json JSON to fill
     */
    protected abstract void fillJSON(ObjectJSON json);

    /**
     * Signal to listeners that element changed
     */
    protected final void firePathElementChanged()
    {
        synchronized (this.listeners)
        {
            for (final PathElementChangeListener listener : this.listeners)
            {
                listener.pathElementChanged(this);
            }
        }
    }

    /**
     * Complete the hash code
     *
     * @param hashCode Hash code to complete
     */
    protected abstract void hashCodeIntern(HashCode hashCode);

    /**
     * Draw path element
     *
     * @param pathDrawer Path drawer
     * @param referenceX Start X
     * @param referenceY Start Y
     * @return Final point
     */
    public abstract Point draw(PathDrawer pathDrawer, double referenceX, double referenceY);

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
    public final int hashCode()
    {
        final HashCode hashCode = new HashCode();
        hashCode.add(this.getClass().getName());
        hashCode.add(this.relative);
        this.hashCodeIntern(hashCode);
        return hashCode.getHashCode();
    }

    /**
     * Indicates if object is equals to this path <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if object is equals to this path
     * @see Object#equals(Object)
     */
    @Override
    public final boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }

        if (object == null)
        {
            return false;
        }

        if (this.getClass().equals(object.getClass()) == false)
        {
            return false;
        }

        final PathElement pathElement = (PathElement) object;

        if (this.relative != pathElement.relative)
        {
            return false;
        }

        return this.equalsIntern(pathElement);
    }

    /**
     * Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false})
     *
     * @return Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false})
     */
    public final boolean isRelative()
    {
        return this.relative;
    }

    /**
     * Change relativity of coordinates
     *
     * @param relative New relative value
     */
    public final void setRelative(final boolean relative)
    {
        if (this.relative == relative)
        {
            return;
        }

        this.relative = relative;
        this.firePathElementChanged();
    }

    /**
     * Register listener of path element changes
     *
     * @param listener Listener to register
     */
    public final void registerPathElementChangeListener(final PathElementChangeListener listener)
    {
        if (listener == null)
        {
            throw new NullPointerException("listener mustn't be null");
        }

        synchronized (this.listeners)
        {
            if (this.listeners.contains(listener) == false)
            {
                this.listeners.add(listener);
            }
        }
    }

    /**
     * Serialize to JSON
     *
     * @return Serialized JSON
     */
    public final ObjectJSON toJSON()
    {
        final ObjectJSON value = new ObjectJSON();
        value.put(VectorialConstants.PARAMETER_RELATIVE, ValueJSON.newValue(this.relative));
        this.fillJSON(value);

        final ObjectJSON json = new ObjectJSON();
        json.put(this.getClass().getSimpleName(), ValueJSON.newValue(value));
        return json;
    }

    /**
     * Unregister listener of path element changes
     *
     * @param listener Listener to unregister
     */
    public final void unregisterPathElementChangeListener(final PathElementChangeListener listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(listener);
        }
    }
}