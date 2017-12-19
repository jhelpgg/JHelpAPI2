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
package jhelp.engine2.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import jhelp.engine2.render.Point3D;

/**
 * Barycenter of a set center of 3D points <br>
 *
 * @author JHelp
 */
public class BarycenterPoint3D
{
    /**
     * Barycenter for X
     */
    private final Barycenter barycenterX;
    /**
     * Barycenter for Y
     */
    private final Barycenter barycenterY;
    /**
     * Barycenter for Z
     */
    private final Barycenter barycenterZ;

    /**
     * Constructs BarycenterPoint3D
     */
    public BarycenterPoint3D()
    {
        this.barycenterX = new Barycenter();
        this.barycenterY = new Barycenter();
        this.barycenterZ = new Barycenter();
    }

    /**
     * Add point to the set
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void add(final double x, final double y, final double z)
    {
        this.barycenterX.add(x);
        this.barycenterY.add(y);
        this.barycenterZ.add(z);
    }

    /**
     * Add point to the set
     *
     * @param point Point to add
     */
    public void add(final @NotNull Point3D point)
    {
        this.add(point.x(), point.y(), point.z());
    }

    /**
     * The barycenter.<br>
     * Return {@code null} if the set of points is empty
     *
     * @return The barycenter
     */
    public @Nullable Point3D barycenter()
    {
        if (this.empty())
        {
            return null;
        }

        return new Point3D((float) this.barycenterX.barycenter(),
                           (float) this.barycenterY.barycenter(),
                           (float) this.barycenterZ.barycenter());
    }

    /**
     * Indicates if the set of points is empty
     *
     * @return {@code true} if the set of points is empty
     */
    public boolean empty()
    {
        return this.barycenterX.empty();
    }
}