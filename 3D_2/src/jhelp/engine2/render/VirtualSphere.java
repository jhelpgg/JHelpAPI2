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
package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.util.Math3D;

/**
 * Virtual sphere<br>
 * Could be use for sphere bounding<br>
 * <br>
 *
 * @author JHelp
 */
public class VirtualSphere
{
    /**
     * Center of sphere
     */
    private       Point3D center;
    /**
     * Ray of sphere
     */
    private final float   ray;
    /**
     * X center
     */
    private final float   x;
    /**
     * Y center
     */
    private final float   y;
    /**
     * Z center
     */
    private final float   z;

    /**
     * Constructs the sphere
     *
     * @param x   X center
     * @param y   Y center
     * @param z   Z center
     * @param ray Ray
     */
    public VirtualSphere(final float x, final float y, final float z, final float ray)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ray = ray;
    }

    /**
     * Center of the sphere
     *
     * @return Center
     */
    public @NotNull Point3D center()
    {
        if (this.center == null)
        {
            this.center = new Point3D(this.x, this.y, this.z);
        }
        return this.center;
    }

    /**
     * Indicates if a point is in the sphere
     *
     * @param x X
     * @param y Y
     * @param z Y
     * @return {@code true} if a point is in the sphere
     */
    public boolean contains(final float x, final float y, final float z)
    {
        final float distance = Point3D.distance(this.x, this.y, this.z, x, y, z);
        return Math3D.equal(distance, this.ray) || (distance < this.ray);
    }

    /**
     * Indicates if a sphere is equal to this sphere
     *
     * @param sphere Sphere tested
     * @return {@code true} if a sphere is equal to this sphere
     */
    public boolean equals(final VirtualSphere sphere)
    {
        return Math3D.equal(this.ray, sphere.ray) && Math3D.equal(this.x, sphere.x) && Math3D.equal(this.y, sphere.y) &&
               Math3D.equal(this.z, sphere.z);
    }

    /**
     * Indicates is an object is equivalent to this sphere
     *
     * @param object Object tested
     * @return {@code true} is an object is equivalent to this sphere
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (object == this)
        {
            return true;
        }
        if (!(object instanceof VirtualSphere))
        {
            return false;
        }
        return this.equals((VirtualSphere) object);
    }

    /**
     * Ray
     *
     * @return Ray
     */
    public float ray()
    {
        return this.ray;
    }

    /**
     * Center x
     *
     * @return Center x
     */
    public float x()
    {
        return this.x;
    }

    /**
     * Center y
     *
     * @return Center y
     */
    public float y()
    {
        return this.y;
    }

    /**
     * Center z
     *
     * @return Center z
     */
    public float z()
    {
        return this.z;
    }
}