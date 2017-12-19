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
import jhelp.engine2.util.Vec3f;
import jhelp.engine2.util.Vec4f;
import jhelp.util.util.HashCode;
import org.lwjgl.opengl.GL11;

/**
 * 3D point, can be used like vector <br>
 *
 * @author JHelp
 */
public class Point3D
{
    /**
     * Middle between two points
     *
     * @param point1 First point
     * @param point2 Second point
     * @return Middle
     */
    public static @NotNull Point3D center(final @NotNull Point3D point1, final @NotNull Point3D point2)
    {
        return new Point3D((point1.x + point2.x) / 2f, (point1.y + point2.y) / 2f, (point1.z + point2.z) / 2f);
    }

    /**
     * Distance between two points
     *
     * @param x1 X1
     * @param y1 Y1
     * @param z1 Z1
     * @param x2 X2
     * @param y2 Y2
     * @param z2 Z2
     * @return Distance
     */
    public static float distance(
            final float x1, final float y1, final float z1, final float x2, final float y2, final float z2)
    {
        return Point3D.length(x2 - x1, y2 - y1, z2 - z1);
    }

    /**
     * Distance between two point
     *
     * @param point1 Point 1
     * @param x2     X2
     * @param y2     Y2
     * @param z2     Z2
     * @return Distance
     */
    public static float distance(final @NotNull Point3D point1, final float x2, final float y2, final float z2)
    {
        return Point3D.distance(point1.x, point1.y, point1.z, x2, y2, z2);
    }

    /**
     * Distance between two point
     *
     * @param point1 Point 1
     * @param point2 Point 2
     * @return Distance
     */
    public static float distance(final @NotNull Point3D point1, final @NotNull Point3D point2)
    {
        return Point3D.distance(point1, point2.x, point2.y, point2.z);
    }

    /**
     * Distance square between two points
     *
     * @param x1 X1
     * @param y1 Y1
     * @param z1 Z1
     * @param x2 X2
     * @param y2 Y2
     * @param z2 Z2
     * @return Distance square
     */
    public static float distanceSquare(
            final float x1, final float y1, final float z1, final float x2, final float y2, final float z2)
    {
        return Point3D.lengthSquare(x2 - x1, y2 - y1, z2 - z1);
    }

    /**
     * Distance square between two point
     *
     * @param point1 Point 1
     * @param x2     X2
     * @param y2     Y2
     * @param z2     Z2
     * @return Distance square
     */
    public static float distanceSquare(final @NotNull Point3D point1, final float x2, final float y2, final float z2)
    {
        return Point3D.distanceSquare(point1.x, point1.y, point1.z, x2, y2, z2);
    }

    /**
     * Distance square between two point
     *
     * @param point1 Point 1
     * @param point2 Point 2
     * @return Distance square
     */
    public static float distanceSquare(final @NotNull Point3D point1, final @NotNull Point3D point2)
    {
        return Point3D.distanceSquare(point1, point2.x, point2.y, point2.z);
    }

    /**
     * Vector length
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @return Length
     */
    public static float length(final float x, final float y, final float z)
    {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Vector length square
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @return Length square
     */
    public static float lengthSquare(final float x, final float y, final float z)
    {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * X
     */
    public float x;

    /**
     * Y
     */
    public float y;

    /**
     * Z
     */
    public float z;

    /**
     * Constructs Point3D (0, 0, 0)
     */
    public Point3D()
    {
        this.x = this.y = this.z = 0f;
    }

    /**
     * Constructs Point3D
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Point3D(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs Point3D
     *
     * @param point Point for X, Y values
     * @param z     Z
     */
    public Point3D(final @NotNull Point2D point, final float z)
    {
        this.x = point.x();
        this.y = point.y();
        this.z = z;
    }

    /**
     * Constructs Point3D copy to an other
     *
     * @param point Point to copy
     */
    public Point3D(final @NotNull Point3D point)
    {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    /**
     * Constructs Point3D
     *
     * @param vec3f Base vector
     */
    public Point3D(final @NotNull Vec3f vec3f)
    {
        this.x = vec3f.x();
        this.y = vec3f.y();
        this.z = vec3f.z();
    }

    /**
     * Constructs Point3D
     *
     * @param vec4f JOGL vector
     */
    public Point3D(final @NotNull Vec4f vec4f)
    {
        final float w = vec4f.w();
        //
        this.x = vec4f.x() / w;
        this.y = vec4f.y() / w;
        this.z = vec4f.z() / w;
    }

    /**
     * Apply like a normal in OpenGL
     */
    @ThreadOpenGL
    void glNormal3f()
    {
        GL11.glNormal3f(this.x, this.y, this.z);
    }

    /**
     * Apply like a point in OpenGL
     */
    @ThreadOpenGL
    void glVertex3f()
    {
        GL11.glVertex3f(this.x, this.y, this.z);
    }

    /**
     * Add vector or translate a point
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @return Result vector or translated point
     */
    public @NotNull Point3D add(final float x, final float y, final float z)
    {
        return new Point3D(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add vector or translate a point
     *
     * @param vector Vector to add
     * @return Result vector or translated point
     */
    public @NotNull Point3D add(final @NotNull Point3D vector)
    {
        return new Point3D(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Translate in opposite way
     *
     * @param point Translation vector
     */
    public void antiTranslate(final @NotNull Point3D point)
    {
        this.x -= point.x;
        this.y -= point.y;
        this.z -= point.z;
    }

    /**
     * Make the dot product between this vector and an other
     *
     * @param vector Vector we do the dot product
     * @return Dot product
     */
    public float dotProduct(final @NotNull Point3D vector)
    {
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    /**
     * Indicates if an other point is equal to this point
     *
     * @param point Point compare
     * @return {@code true} on equality
     */
    public boolean equals(final @NotNull Point3D point)
    {
        return Math3D.equal(this.x, point.x) && Math3D.equal(this.y, point.y) && Math3D.equal(this.z, point.z);
    }

    /**
     * Multiply the vector by a factor
     *
     * @param factor Multiply factor
     * @return Result vector
     */
    public @NotNull Point3D factor(final float factor)
    {
        return new Point3D(this.x * factor, this.y * factor, this.z * factor);
    }

    /**
     * Opposite vector
     *
     * @return Opposite vector
     */
    public @NotNull Point3D getOptsite()
    {
        return new Point3D(-this.x, -this.y, -this.z);
    }

    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.x, this.y, this.z);
    }

    /**
     * Indicates if an object is equal to this point
     *
     * @param obj Object to compare
     * @return {@code true} on equality
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Point3D))
        {
            return false;
        }
        return this.equals((Point3D) obj);
    }

    /**
     * String representation
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Point3D : (");
        sb.append(this.x);
        sb.append(", ");
        sb.append(this.y);
        sb.append(", ");
        sb.append(this.z);
        sb.append(")");
        return sb.toString();
    }

    /**
     * Vector length
     *
     * @return Vector length
     */
    public float length()
    {
        return (float) Math.sqrt(this.dotProduct(this));
    }

    /**
     * Normalize this vector
     */
    public void normalize()
    {
        final float length = this.length();

        if (!Math3D.nul(length))
        {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }
    }

    /**
     * Make dot product between this vector and an other
     *
     * @param vector Vector we do the product
     * @return The product
     */
    public @NotNull Point3D product(final @NotNull Point3D vector)
    {
        return new Point3D((this.y * vector.z) - (this.z * vector.y),
                           (this.x * vector.z) - (this.z * vector.x),
                           (this.x * vector.y) - (this.y * vector.x));
    }

    /**
     * Modify the point
     *
     * @param x New X
     * @param y New Y
     * @param z New Z
     */
    public void set(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy a point
     *
     * @param point Point copied
     */
    public void set(final @NotNull Point3D point)
    {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    /**
     * Copy JOGL vector
     *
     * @param vec3f Copied vector
     */
    public void set(final @NotNull Vec3f vec3f)
    {
        this.x = vec3f.x();
        this.y = vec3f.y();
        this.z = vec3f.z();
    }

    /**
     * Copy JOGL vector
     *
     * @param vec4f Copied vector
     */
    public void set(final @NotNull Vec4f vec4f)
    {
        final float w = vec4f.w();
        //
        this.x = vec4f.x() / w;
        this.y = vec4f.y() / w;
        this.z = vec4f.z() / w;
    }

    /**
     * Subtraction two vector or two points
     *
     * @param vector Vector or point to substract
     * @return Vector result
     */
    public @NotNull Point3D substract(final @NotNull Point3D vector)
    {
        return new Point3D(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    /**
     * To JOGL vector
     *
     * @return JOGL vector
     */
    public @NotNull Vec3f toVect3f()
    {
        return new Vec3f(this.x, this.y, this.z);
    }

    /**
     * To JOGL vector
     *
     * @return JOGL vector
     */
    public @NotNull Vec4f toVect4f()
    {
        return new Vec4f(this.x, this.y, this.z, 1f);
    }

    /**
     * Translate
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void translate(final float x, final float y, final float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Translate
     *
     * @param point Translation vector
     */
    public void translate(final @NotNull Point3D point)
    {
        this.x += point.x;
        this.y += point.y;
        this.z += point.z;
    }

    /**
     * X
     *
     * @return X
     */
    public float x()
    {
        return this.x;
    }

    /**
     * Y
     *
     * @return Y
     */
    public float y()
    {
        return this.y;
    }

    /**
     * Z
     *
     * @return Z
     */
    public float z()
    {
        return this.z;
    }
}