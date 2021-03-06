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

/**
 * A vertex is compose of a position, UV and normal
 *
 * @author JHelp
 */
public class Vertex
{
    /**
     * Normal
     */
    private Point3D normal;
    /**
     * Position
     */
    private Point3D position;
    /**
     * UV
     */
    private Point2D uv;

    /**
     * Constructs Vertex <br>
     * This vertex position is (0, 0, 0), UV (0, 0) and normal (0, 0, 1)
     */
    public Vertex()
    {
        this.position = new Point3D();
        this.normal = new Point3D(0, 0, 1);
        this.uv = new Point2D();
    }

    /**
     * Constructs Vertex with a position and UV (0, 0) and normal (0, 0, 1)
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Vertex(final float x, final float y, final float z)
    {
        this();
        this.position.set(x, y, z);
    }

    /**
     * Constructs Vertex with a position and UV and normal (0, 0, 1)
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @param u U
     * @param v V
     */
    public Vertex(final float x, final float y, final float z, final float u, final float v)
    {
        this();
        this.position.set(x, y, z);
        this.uv.set(u, v);
    }

    /**
     * Constructs Vertex with a position and UV(0,0) and normal
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param nx Normal X
     * @param ny Normal Y
     * @param nz Normal Z
     */
    public Vertex(final float x, final float y, final float z, final float nx, final float ny, final float nz)
    {
        this();
        this.position.set(x, y, z);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Constructs Vertex with a position and UV and normal
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param u  U
     * @param v  V
     * @param nx Normal X
     * @param ny Normal Y
     * @param nz Normal Z
     */
    public Vertex(
            final float x, final float y, final float z, final float u, final float v, final float nx, final float ny,
            final float nz)
    {
        this();
        this.position.set(x, y, z);
        this.uv.set(u, v);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Constructs Vertex with a position and UV and normal (0, 0, 1)
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param uv UV couple
     */
    public Vertex(final float x, final float y, final float z, final @NotNull Point2D uv)
    {
        this();
        if (uv == null)
        {
            throw new NullPointerException("The uv couldn't be null");
        }
        this.position.set(x, y, z);
        this.uv = uv;
    }

    /**
     * Constructs Vertex with a position and UV and normal
     *
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param uv     UV couple
     * @param normal Normal vector
     */
    public Vertex(final float x, final float y, final float z, final @NotNull Point2D uv, final @NotNull Point3D normal)
    {
        this();
        if (uv == null)
        {
            throw new NullPointerException("The uv couldn't be null");
        }
        if (normal == null)
        {
            throw new NullPointerException("The normal couldn't be null");
        }
        this.position.set(x, y, z);
        this.uv = uv;
        this.normal = normal;
    }

    /**
     * Constructs Vertex with a position and UV(0,0) and normal
     *
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param normal Normal vector
     */
    public Vertex(final float x, final float y, final float z, final @NotNull Point3D normal)
    {
        this();
        if (normal == null)
        {
            throw new NullPointerException("The normal couldn't be null");
        }
        this.position.set(x, y, z);
        this.normal = normal;
    }

    /**
     * Constructs Vertex with position and UV(0,0) and Noraml(0,0,1)
     *
     * @param position Position
     */
    public Vertex(final @NotNull Point3D position)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
    }

    /**
     * Constructs Vertex with position and UV and Normal(0,0,1)
     *
     * @param position Position
     * @param u        U
     * @param v        V
     */
    public Vertex(final @NotNull Point3D position, final float u, final float v)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
        this.uv.set(u, v);
    }

    /**
     * Constructs Vertex with position, UV(0,0) and Normal
     *
     * @param position Position
     * @param nx       Normal X
     * @param ny       Normal Y
     * @param nz       Normal Z
     */
    public Vertex(final @NotNull Point3D position, final float nx, final float ny, final float nz)
    {
        this();
        this.position.set(position);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Constructs Vertex with position and UV and normal
     *
     * @param position Position
     * @param u        U
     * @param v        V
     * @param nx       Normal X
     * @param ny       Normal Y
     * @param nz       Normal Z
     */
    public Vertex(
            final @NotNull Point3D position, final float u, final float v, final float nx, final float ny,
            final float nz)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
        this.uv.set(u, v);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Constructs Vertex with position, UV, normal(0,0,1)
     *
     * @param position Position
     * @param uv       UV couple
     */
    public Vertex(final @NotNull Point3D position, final @NotNull Point2D uv)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
        if (uv == null)
        {
            throw new NullPointerException("The uv couldn't be null");
        }
        this.uv = uv;
    }

    /**
     * Constructs Vertex with position, UV, normal
     *
     * @param position Position
     * @param uv       UV couple
     * @param normal   Normal vector
     */
    public Vertex(final @NotNull Point3D position, final @NotNull Point2D uv, final @NotNull Point3D normal)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
        if (uv == null)
        {
            throw new NullPointerException("The uv couldn't be null");
        }
        this.uv = uv;
        if (normal == null)
        {
            throw new NullPointerException("The normal couldn't be null");
        }
        this.normal = normal;
    }

    /**
     * Constructs Vertex with position, UV(0,0), normal
     *
     * @param position Position
     * @param normal   Normal
     */
    public Vertex(final @NotNull Point3D position, final @NotNull Point3D normal)
    {
        this();
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
        if (normal == null)
        {
            throw new NullPointerException("The normal couldn't be null");
        }
        this.normal = normal;
    }

    /**
     * Draw vertex on OpenGL
     */
    @ThreadOpenGL
    public void glVertex()
    {
        this.normal.glNormal3f();
        this.uv.glTexCoord2f();
        this.position.glVertex3f();
    }

    /**
     * The normal
     *
     * @return Normal
     */
    public @NotNull Point3D normal()
    {
        return this.normal;
    }

    /**
     * Change normal
     *
     * @param normal Normal
     */
    public void normal(final @NotNull Point3D normal)
    {
        if (normal == null)
        {
            throw new NullPointerException("The normal couldn't be null");
        }
        this.normal = normal;
    }

    /**
     * Change normal
     *
     * @param x Normal X
     * @param y Normal Y
     * @param z Normal Z
     */
    public void normal(final float x, final float y, final float z)
    {
        this.normal.set(x, y, z);
    }

    /**
     * Position
     *
     * @return Position
     */
    public @NotNull Point3D position()
    {
        return this.position;
    }

    /**
     * Change position
     *
     * @param position Position
     */
    public void position(final @NotNull Point3D position)
    {
        if (position == null)
        {
            throw new NullPointerException("The position couldn't be null");
        }
        this.position = position;
    }

    /**
     * Change position
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void position(final float x, final float y, final float z)
    {
        this.position.set(x, y, z);
    }

    /**
     * Change the position
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void set(final float x, final float y, final float z)
    {
        this.position.set(x, y, z);
    }

    /**
     * Change position, UV
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @param u U
     * @param v V
     */
    public void set(final float x, final float y, final float z, final float u, final float v)
    {
        this.position.set(x, y, z);
        this.uv.set(u, v);
    }

    /**
     * Change position, normal
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param nx Normal X
     * @param ny Normal Y
     * @param nz Normal Z
     */
    public void set(final float x, final float y, final float z, final float nx, final float ny, final float nz)
    {
        this.position.set(x, y, z);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Change position, UV, normal
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param u  U
     * @param v  V
     * @param nx Normal X
     * @param ny Normal Y
     * @param nz Normal Z
     */
    public void set(
            final float x, final float y, final float z, final float u, final float v, final float nx, final float ny,
            final float nz)
    {
        this.position.set(x, y, z);
        this.uv.set(u, v);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Change position, UV
     *
     * @param x  X
     * @param y  Y
     * @param z  Z
     * @param uv UV
     */
    public void set(final float x, final float y, final float z, final @NotNull Point2D uv)
    {
        this.position.set(x, y, z);
        this.uv = uv;
    }

    /**
     * Change position, UV, normal
     *
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param uv     UV
     * @param normal Normal
     */
    public void set(
            final float x, final float y, final float z, final @NotNull Point2D uv, final @NotNull Point3D normal)
    {
        this.position.set(x, y, z);
        this.uv = uv;
        this.normal = normal;
    }

    /**
     * Change position, normal
     *
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param normal Normal
     */
    public void set(final float x, final float y, final float z, final @NotNull Point3D normal)
    {
        this.position.set(x, y, z);
        this.normal = normal;
    }

    /**
     * Change position
     *
     * @param position Position
     */
    public void set(final @NotNull Point3D position)
    {
        this.position = position;
    }

    /**
     * Change position, UV
     *
     * @param position Position
     * @param u        U
     * @param v        V
     */
    public void set(final @NotNull Point3D position, final float u, final float v)
    {
        this.position = position;
        this.uv.set(u, v);
    }

    /**
     * Change position, normal
     *
     * @param position Position
     * @param nx       Normal X
     * @param ny       Normal Y
     * @param nz       Normal Z
     */
    public void set(final @NotNull Point3D position, final float nx, final float ny, final float nz)
    {
        this.position = position;
        this.normal.set(nx, ny, nz);
    }

    /**
     * Change position, UV, normal
     *
     * @param position Position
     * @param u        U
     * @param v        V
     * @param nx       Normal X
     * @param ny       Normal Y
     * @param nz       Normal Z
     */
    public void set(
            final @NotNull Point3D position, final float u, final float v, final float nx, final float ny,
            final float nz)
    {
        this.position = position;
        this.uv.set(u, v);
        this.normal.set(nx, ny, nz);
    }

    /**
     * Change position, UV
     *
     * @param position Position
     * @param uv       UV
     */
    public void set(final @NotNull Point3D position, final @NotNull Point2D uv)
    {
        this.position = position;
        this.uv = uv;
    }

    /**
     * Change position, UV, normal
     *
     * @param position Position
     * @param uv       UV
     * @param normal   Normal
     */
    public void set(final @NotNull Point3D position, final @NotNull Point2D uv, final @NotNull Point3D normal)
    {
        this.position = position;
        this.uv = uv;
        this.normal = normal;
    }

    /**
     * Change position, normal
     *
     * @param position Position
     * @param normal   Normal
     */
    public void set(final @NotNull Point3D position, final @NotNull Point3D normal)
    {
        this.position = position;
        this.normal = normal;
    }

    /**
     * String representation of vertex
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "Position=" + this.position + " UV=" + this.uv + " Normal=" + this.normal;
    }

    /**
     * UV
     *
     * @return UV
     */
    public @NotNull Point2D uv()
    {
        return this.uv;
    }

    /**
     * Change UV
     *
     * @param uv UV
     */
    public void uv(final @NotNull Point2D uv)
    {
        if (uv == null)
        {
            throw new NullPointerException("The uv couldn't be null");
        }
        this.uv = uv;
    }

    /**
     * Change UV
     *
     * @param u U
     * @param v V
     */
    public void uv(final float u, final float v)
    {
        this.uv.set(u, v);
    }
}