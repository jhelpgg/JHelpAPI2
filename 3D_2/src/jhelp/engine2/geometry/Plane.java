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
package jhelp.engine2.geometry;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.render.Mesh;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.render.Point3D;
import jhelp.engine2.render.Vertex;
import jhelp.xml.MarkupXML;

/**
 * A plane.<br>
 * It's cut on gird, so it is possible to make elevation grid with it.
 *
 * @author JHelp
 */
public class Plane
        extends Object3D
{
    /**
     * Number of horizontal part
     */
    private int     horizontal;
    /**
     * Indicates if U are inverted
     */
    private boolean invertU;
    /**
     * Indicates if V are inverted
     */
    private boolean invertV;
    /**
     * Number of vertical part
     */
    private int     vertical;

    /**
     * A basic plane
     */
    public Plane()
    {
        this(1, 1);
    }

    /**
     * Constructs Plane
     *
     * @param invertU Indicates if U are inverted
     * @param invertV Indicates if V are inverted
     */
    public Plane(final boolean invertU, final boolean invertV)
    {
        this(1, 1, invertU, invertV);
    }

    /**
     * Constructs Plane
     *
     * @param horizontal Number of horizontal part
     * @param vertical   Number of vertical part
     */
    public Plane(final int horizontal, final int vertical)
    {
        this(horizontal, vertical, false, false);
    }

    /**
     * Constructs Plane
     *
     * @param horizontal Number of horizontal part
     * @param vertical   Number of vertical part
     * @param invertU    Indicates if U are inverted
     * @param invertV    Indicates if V are inverted
     */
    public Plane(final int horizontal, final int vertical, final boolean invertU, final boolean invertV)
    {
        this.initialize(horizontal, vertical, invertU, invertV);
    }

    /**
     * Reconstruct the plane
     *
     * @param horizontal Number of horizontal parts
     * @param vertical   Number of vertical parts
     * @param invertU    Indicates if U are inverted
     * @param invertV    Indicates if V are inverted
     */
    private void initialize(int horizontal, int vertical, final boolean invertU, final boolean invertV)
    {
        this.nodeType = NodeType.PLANE;
        // Initialization
        this.horizontal = Math.max(1, horizontal);
        this.vertical = Math.max(1, vertical);
        this.invertU = invertU;
        this.invertV = invertV;

        this.mesh = new Mesh();

        int          y;
        int          x;
        float        xx;
        float        yy;
        float        xx1;
        float        yy1;
        final float  v      = (float) vertical;
        final float  h      = (float) horizontal;
        final Vertex vertex = new Vertex();
        vertex.normal(0, 0, -1f);

        // For each vertical part
        for (y = 0; y < vertical; y++)
        {
            // Compute Y
            yy = y / v;
            yy1 = (y + 1f) / v;

            // For each horizontal part
            for (x = 0; x < horizontal; x++)
            {
                // Compute X
                xx = x / h;
                xx1 = (x + 1f) / h;

                // Add the face
                vertex.position(new Point3D(xx - 0.5f, yy - 0.5f, 0));
                vertex.uv(this.makeUV(xx, yy, invertU, invertV));
                this.add(vertex);
                vertex.position(new Point3D(xx - 0.5f, yy1 - 0.5f, 0));
                vertex.uv(this.makeUV(xx, yy1, invertU, invertV));
                this.add(vertex);
                vertex.position(new Point3D(xx1 - 0.5f, yy1 - 0.5f, 0));
                vertex.uv(this.makeUV(xx1, yy1, invertU, invertV));
                this.add(vertex);
                vertex.position(new Point3D(xx1 - 0.5f, yy - 0.5f, 0));
                vertex.uv(this.makeUV(xx1, yy, invertU, invertV));
                this.add(vertex);
                //
                this.nextFace();
            }
        }

        this.reconstructTheList();
    }

    /**
     * Compute real UV
     *
     * @param u       Actual U
     * @param v       Actual V
     * @param invertU Indicates if U are inverted
     * @param invertV Indicates if V are inverted
     * @return Rel UV
     */
    private @NotNull Point2D makeUV(float u, float v, final boolean invertU, final boolean invertV)
    {
        if (invertU)
        {
            u = 1f - u;
        }

        if (invertV)
        {
            v = 1f - v;
        }

        return new Point2D(u, v);
    }

    /**
     * Call on parsing end
     *
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
        this.initialize(this.horizontal, this.vertical, this.invertU, this.invertV);
    }

    /**
     * Read Plane parameters from XML
     *
     * @param markupXML Markup to parse
     * @see Node#readFromMarkup(MarkupXML)
     */
    @Override
    protected void readFromMarkup(final @NotNull MarkupXML markupXML)
    {
        this.readMaterialFromMarkup(markupXML);
        this.horizontal = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_horizontal, 1);
        this.vertical = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_vertical, 1);
        this.invertU = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_invU, false);
        this.invertV = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_invV, false);
    }

    /**
     * Write plane on XML
     *
     * @param markupXML Markup to fill
     * @see Node#writeInMarkup(MarkupXML)
     */
    @Override
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
        this.writeMaterialInMarkup(markupXML);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_horizontal, this.horizontal);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_vertical, this.vertical);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_invU, this.invertU);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_invV, this.invertV);
    }

    /**
     * Number part in horizontal
     *
     * @return Number part in horizontal
     */
    public int horizontal()
    {
        return this.horizontal;
    }

    /**
     * Indicates if U are inverted
     *
     * @return {@code true} if U are inverted
     */
    public boolean invertU()
    {
        return this.invertU;
    }

    /**
     * Indicates if V are inverted
     *
     * @return {@code true} if V are inverted
     */
    public boolean invertV()
    {
        return this.invertV;
    }

    /**
     * Number part in vertical
     *
     * @return Number part in vertical
     */
    public int vertical()
    {
        return this.vertical;
    }
}