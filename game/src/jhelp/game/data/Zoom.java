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

package jhelp.game.data;

import jhelp.util.math.Math2;

/**
 * Zoom factor on object
 */
public final class Zoom
{
    /**
     * Zoom in height
     */
    private final float zoomHeight;
    /**
     * Zoom in width
     */
    private final float zoomWidth;

    /**
     * Create zoom information
     *
     * @param zoomWidth  Zoom in width
     * @param zoomHeight Zoom in height
     */
    public Zoom(final float zoomWidth, final float zoomHeight)
    {
        if (Math2.sign(zoomWidth) <= 0 || Math2.compare(zoomWidth, 1f) > 0 ||
            Math2.sign(zoomHeight) <= 0 || Math2.compare(zoomHeight, 1f) > 0)
        {
            throw new IllegalArgumentException(
                    "zooms Must be in ]0, 1], but zoomWidth=" + zoomWidth + " and zoomHeight=" + zoomHeight);
        }

        this.zoomWidth = zoomWidth;
        this.zoomHeight = zoomHeight;
    }

    /**
     * Indicates if given object equals to this zoom
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this zoom
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        if (!(object instanceof Zoom))
        {
            return false;
        }

        Zoom zoom = (Zoom) object;
        return Math2.equals(this.zoomWidth, zoom.zoomWidth) && Math2.equals(this.zoomHeight, zoom.zoomHeight);
    }

    /**
     * Zoom in height
     *
     * @return Zoom in height
     */
    public float zoomHeight()
    {
        return this.zoomHeight;
    }

    /**
     * Zoom in width
     *
     * @return Zoom in width
     */
    public float zoomWidth()
    {
        return this.zoomWidth;
    }
}
