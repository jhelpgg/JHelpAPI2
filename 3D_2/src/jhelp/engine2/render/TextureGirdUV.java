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
import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 * Texture with "grid" based on Object UV <br>
 *
 * @author JHelp
 */
public class TextureGirdUV
        extends Texture
{
    /**
     * Shape description <br>
     *
     * @author JHelp
     */
    static class Shape
    {
        /**
         * Color to fill
         */
        public int     color;
        /**
         * Shape
         */
        public Polygon polygon;

        /**
         * Constructs Shape
         */
        public Shape()
        {
        }
    }

    /**
     * Background color
     */
    private Color       backgroundColor;
    /**
     * Border color
     */
    private Color       borderColor;
    /**
     * Shape list
     */
    private List<Shape> shapes;

    /**
     * Constructs TextureGirdUV
     *
     * @param name   Texture name
     * @param width  Width
     * @param height Height
     */
    public TextureGirdUV(final @NotNull String name, final int width, final int height)
    {
        super(name, width, height, 0xFFFFFFFF);

        this.shapes = new ArrayList<>();

        this.backgroundColor = Color.WHITE;
        this.borderColor = Color.BLACK;
    }

    /**
     * Return backgroundColor
     *
     * @return backgroundColor
     */
    public @NotNull Color backgroundColor()
    {
        return this.backgroundColor;
    }

    /**
     * Modify backgroundColor
     *
     * @param backgroundColor New backgroundColor value
     */
    public void backgroundColor(final @NotNull Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
        this.refreshShapes();
    }

    /**
     * Return borderColor
     *
     * @return borderColor
     */
    public @NotNull Color borderColor()
    {
        return this.borderColor;
    }

    /**
     * Modify borderColor
     *
     * @param borderColor New borderColor value
     */
    public void borderColor(final @NotNull Color borderColor)
    {
        this.borderColor = borderColor;
        this.refreshShapes();
    }

    /**
     * Shape color
     *
     * @param shape Shape index
     * @return Shape color
     */
    public int colorOnShape(final int shape)
    {
        return this.shapes.get(shape).color;
    }

    /**
     * Change shape color
     *
     * @param shape Shape index
     * @param color Color to apply
     */
    public void colorOnShape(final int shape, final int color)
    {
        this.shapes.get(shape).color = color;
        this.refreshShapes();
    }

    /**
     * Create grid from mesh
     *
     * @param mesh Mesh to "extract" grid
     */
    public void createGird(final @NotNull Mesh mesh)
    {
        this.shapes = mesh.obtainUVshapes(this.width, this.height);

        this.refreshShapes();
    }

    /**
     * Create grid from object
     *
     * @param object3D Object to "extract" grid
     */
    public void createGird(final @NotNull Object3D object3D)
    {
        this.createGird(object3D.mesh);
    }

    /**
     * Number of shape
     *
     * @return Number of shape
     */
    public int numberOfShape()
    {
        return this.shapes.size();
    }

    /**
     * Obtain a shape for a position
     *
     * @param x X
     * @param y Y
     * @return Shape index under the position or -1
     */
    public int obtainShape(final int x, final int y)
    {
        final int nb = this.shapes.size();
        for (int i = 0; i < nb; i++)
        {
            if (this.shapes.get(i).polygon.contains(x, y))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Refresh shapes drawing
     */
    public void refreshShapes()
    {
        this.fillRect(0, 0, this.width, this.height, this.backgroundColor, false);

        for (final Shape shape : this.shapes)
        {
            this.draw(shape.polygon, this.borderColor, false, 1);
            this.fill(shape.polygon, new Color(shape.color, true), true);
        }

        this.flush();
    }
}