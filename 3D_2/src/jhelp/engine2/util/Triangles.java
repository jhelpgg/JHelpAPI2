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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jhelp.engine2.render.Vertex;

/**
 * Triangle list
 *
 * @author JHelp
 */
public class Triangles
{
    /**
     * Trinale list
     */
    private final List<Triangle> triangles;

    /**
     * Create a new instance of Triangles
     */
    public Triangles()
    {
        this.triangles = new ArrayList<>();
    }

    /**
     * Add a triangle
     *
     * @param triangle Triangle to add
     */
    public void addTriangle(final @NotNull Triangle triangle)
    {
        this.triangles.add(triangle);
    }

    /**
     * Add a triangle
     *
     * @param first  First point of triangle
     * @param second Second point of triangle
     * @param third  Third point of triangle
     */
    public void addTriangle(final @NotNull Vertex first, final @NotNull Vertex second, final @NotNull Vertex third)
    {
        this.triangles.add(new Triangle(first, second, third));
    }

    /**
     * Add a convex polygon in traingle set
     *
     * @param polygon Points of the convex polygon
     */
    public void convertInTriangles(final @NotNull Vertex... polygon)
    {
        if ((polygon == null) || (polygon.length < 3))
        {
            return;
        }

        final int    length = polygon.length;
        final Vertex first  = polygon[0];

        for (int i = 2; i < length; i++)
        {
            this.triangles.add(new Triangle(first, polygon[i - 1], polygon[i]));
        }
    }

    /**
     * List of triangles
     *
     * @return List of triangles
     */
    public @NotNull List<Triangle> obtainTriangleList()
    {
        return Collections.unmodifiableList(this.triangles);
    }
}