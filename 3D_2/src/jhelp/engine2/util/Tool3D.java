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
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.geometry.PathGeom;
import jhelp.engine2.geometry.Plane;
import jhelp.engine2.geometry.Revolution;
import jhelp.engine2.geometry.Sphere;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.render.Color4f;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Mesh;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.ObjectClone;
import jhelp.engine2.render.Point3D;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Vertex;
import jhelp.engine2.render.VirtualBox;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.twoD.GUI2D;
import jhelp.engine2.twoD.Line2D;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.twoD.Path;
import jhelp.util.debug.Debug;
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;

/**
 * Tools for 3D manipulation <br>
 *
 * @author JHelp
 */
public class Tool3D
{
    /**
     * Add a Color4f parameter to XML markup
     *
     * @param markupXML     Markup where add
     * @param parameterName Parameter name
     * @param color4f       Color to store
     */
    public static void addColor4fParameter(
            final @NotNull MarkupXML markupXML, final @NotNull String parameterName, final @NotNull Color4f color4f)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(color4f.alpha());
        stringBuilder.append(' ');
        stringBuilder.append(color4f.red());
        stringBuilder.append(' ');
        stringBuilder.append(color4f.green());
        stringBuilder.append(' ');
        stringBuilder.append(color4f.blue());
        markupXML.addParameter(parameterName, stringBuilder.toString());
    }

    /**
     * Add a Point3D parameter to XML markup
     *
     * @param markupXML     Markup where add
     * @param parameterName Parameter name
     * @param point         Point to add
     */
    public static void addPoint3DParameter(
            final @NotNull MarkupXML markupXML, final @NotNull String parameterName, final @NotNull Point3D point)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(point.x());
        stringBuilder.append(' ');
        stringBuilder.append(point.y());
        stringBuilder.append(' ');
        stringBuilder.append(point.z());
        markupXML.addParameter(parameterName, stringBuilder.toString());
    }

    /**
     * Collect all used material in a scene
     *
     * @param scene Scene to explore
     * @return Materials collected list
     */
    public static @NotNull List<Material> collectAllUsedMaterial(final @NotNull Scene scene)
    {
        final List<Material> collectedMaterials = new ArrayList<>();
        Node                 node               = scene.root();
        final Stack<Node>    stack              = new Stack<>();
        stack.push(node);
        NodeWithMaterial nodeWithMaterial;
        Material         material;

        while (!stack.isEmpty())
        {
            node = stack.pop();

            if (node instanceof NodeWithMaterial)
            {
                nodeWithMaterial = (NodeWithMaterial) node;
                material = nodeWithMaterial.material();

                if (!collectedMaterials.contains(material))
                {
                    collectedMaterials.add(material);
                }

                material = nodeWithMaterial.materialForSelection();

                if ((material != null) && !collectedMaterials.contains(material))
                {
                    collectedMaterials.add(material);
                }
            }

            final Iterator<Node> children = node.children();

            while (children.hasNext())
            {
                stack.push(children.next());
            }
        }

        return collectedMaterials;
    }

    /**
     * Collect all texture used in a scene renderer
     *
     * @param window Window where scene draw
     * @return Textures collected list
     */
    public static @NotNull List<Texture> collectAllUsedTexture(final @NotNull Window3D window)
    {
        final Scene         scene             = window.scene();
        final GUI2D         gui2d             = window.gui2d();
        final List<Texture> collectedTextures = new ArrayList<>();
        Texture             texture;
        Material            material;
        NodeWithMaterial    nodeWithMaterial;
        Node                node              = scene.root();
        final Stack<Node>   stack             = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty())
        {
            node = stack.pop();

            if (node instanceof NodeWithMaterial)
            {
                nodeWithMaterial = (NodeWithMaterial) node;
                material = nodeWithMaterial.material();
                texture = material.textureDiffuse();

                if (texture != null && !collectedTextures.contains(texture))
                {
                    collectedTextures.add(texture);
                }

                texture = material.textureSpheric();

                if (texture != null && !collectedTextures.contains(texture))
                {
                    collectedTextures.add(texture);
                }

                material = nodeWithMaterial.materialForSelection();

                if (material != null)
                {
                    texture = material.textureDiffuse();

                    if (texture != null && !collectedTextures.contains(texture))
                    {
                        collectedTextures.add(texture);
                    }

                    texture = material.textureSpheric();

                    if (texture != null && !collectedTextures.contains(texture))
                    {
                        collectedTextures.add(texture);
                    }
                }
            }

            texture = node.textureHotspot();

            if (texture != null && !collectedTextures.contains(texture))
            {
                collectedTextures.add(texture);
            }

            final Iterator<Node> children = node.children();

            while (children.hasNext())
            {
                stack.push(children.next());
            }
        }

        Object2D           object2D;
        boolean            visible;
        Iterator<Object2D> iterator = gui2d.iteratorOver3D();

        while (iterator.hasNext())
        {
            object2D = iterator.next();
            visible = object2D.visible();
            object2D.visible(true);
            texture = object2D.texture();
            object2D.visible(visible);

            if (texture != null && !collectedTextures.contains(texture))
            {
                collectedTextures.add(texture);
            }
        }

        iterator = gui2d.iteratorUnder3D();

        while (iterator.hasNext())
        {
            object2D = iterator.next();
            visible = object2D.visible();
            object2D.visible(true);
            texture = object2D.texture();
            object2D.visible(visible);

            if (texture != null && !collectedTextures.contains(texture))
            {
                collectedTextures.add(texture);
            }
        }

        return collectedTextures;
    }

    /**
     * Create a clone and clone also the all hierarchy
     *
     * @param node Node to clone
     * @return Clone
     */
    public static @NotNull Node createCloneHierarchy(final @NotNull Node node)
    {
        return Tool3D.createCloneHierarchy(node, "");
    }

    /**
     * Create a clone and clone also the all hierarchy, and additional suffix is given to cloned node names
     *
     * @param node   Node to clone
     * @param suffix Suffix to add to clones name
     * @return Cloned hierarchy
     */
    public static @NotNull Node createCloneHierarchy(final @NotNull Node node, final @NotNull String suffix)
    {
        Node clone = new Node();

        if (node instanceof Object3D)
        {
            clone = new ObjectClone((Object3D) node);
            ((NodeWithMaterial) clone).twoSidedState(((NodeWithMaterial) node).twoSidedState());
        }
        else if (node instanceof ObjectClone)
        {
            clone = new ObjectClone(((ObjectClone) node).reference());
            ((NodeWithMaterial) clone).twoSidedState(((NodeWithMaterial) node).twoSidedState());
        }

        clone.nodeName = node.nodeName + suffix;
        clone.position(node.x(), node.y(), node.z());
        clone.setScale(node.scaleX(), node.scaleY(), node.scaleZ());
        clone.angleX(node.angleX());
        clone.angleY(node.angleY());
        clone.angleZ(node.angleZ());
        clone.visible(node.visible());
        clone.canBePick(node.canBePick());
        clone.showWire(node.showWire());
        clone.wireColor(node.wireColor());
        clone.additionalInformation(node.additionalInformation());

        if (node.xLimited())
        {
            clone.limitX(node.xMin(), node.xMax());
        }

        if (node.yLimited())
        {
            clone.limitY(node.yMin(), node.yMax());
        }

        if (node.zLimited())
        {
            clone.limitZ(node.zMin(), node.zMax());
        }

        if (node.xAngleLimited())
        {
            clone.limitAngleX(node.xAngleMin(), node.xAngleMax());
        }

        if (node.yAngleLimited())
        {
            clone.limitAngleY(node.yAngleMin(), node.yAngleMax());
        }

        if (node.zAngleLimited())
        {
            clone.limitAngleZ(node.zAngleMin(), node.zAngleMax());
        }

        final int nb = node.childCount();

        for (int i = 0; i < nb; i++)
        {
            clone.addChild(Tool3D.createCloneHierarchy(node.child(i), suffix));
        }

        return clone;
    }

    /**
     * Create a force joined mesh with to path.<br>
     * The path for V walk throw the path for U
     *
     * @param pathU          Path for U
     * @param precisionU     Path for U precision
     * @param pathV          Path for V
     * @param precisionV     Path for V precision
     * @param multU          multiplier
     * @param linearize      Indicates if we want try to linearize UV
     * @param reverseNormals Indicates if normals must be reversed
     * @return Constructed mesh
     */
    public static @NotNull Mesh createJoinedMesh(
            final @NotNull Path pathU, final int precisionU,
            final @NotNull Path pathV, final int precisionV,
            final float multU, final boolean linearize, final boolean reverseNormals)
    {
        final float mult = reverseNormals ? -1 : 1;

        // Initialization
        final Mesh mesh = new Mesh();

        final List<Line2D> linesU = pathU.computePath(precisionU);
        linesU.add(linesU.get(0));
        final List<Line2D> linesV = pathV.computePath(precisionV);

        float x00 = 0, y00 = 0, z00 = 0, u00 = 0, v00 = 0, nx00 = 0, ny00 = 0, nz00 = 0;
        float x10 = 0, y10 = 0, z10 = 0, u10 = 0, v10 = 0, nx10 = 0, ny10 = 0, nz10 = 0;
        float x01 = 0, y01 = 0, z01 = 0, u01 = 0, v01 = 0, nx01 = 0, ny01 = 0, nz01 = 0;
        float x11 = 0, y11 = 0, z11 = 0, u11 = 0, v11 = 0, nx11 = 0, ny11 = 0, nz11 = 0;

        List<Vertex> temp = null;
        List<Vertex> old  = null;

        float x, y, a0, b0, a1, b1, xp0, yp0, xp1, yp1, xx, yy;

        Vertex dir0, dir1, p0, p1;
        float  a00, b00, c00, vx00, vy00, vz00;
        float  a10, b10, c10, vx10, vy10, vz10;
        float  a01, b01, c01, vx01, vy01, vz01;
        float  a11, b11, c11, vx11, vy11, vz11;
        float  mx0, my0, mz0, mx1, my1, mz1;

        float oldU0, oldU1;
        oldU0 = oldU1 = 0;
        float l0, l1;

        float   length;
        boolean first;

        int index;
        first = true;

        // For each step in U path
        for (final Line2D lineU : linesU)
        {
            // U step goes (a0, b0) to (a1, b1), the direction vector is (x, y)
            a0 = lineU.pointStart.x();
            b0 = lineU.pointStart.y();
            a1 = lineU.pointEnd.x();
            b1 = lineU.pointEnd.y();
            x = a1 - a0;
            y = b1 - b0;

            // Normalize(x, y)
            length = (float) Math.sqrt((x * x) + (y * y));
            if (!Math3D.nul(length))
            {
                x /= length;
                y /= length;
            }

            // Compute U values of each face at this step
            u00 = u01 = lineU.start;
            u10 = u11 = lineU.end;

            // If we try to linearize, base on old values
            if (linearize)
            {
                u00 = u01 = oldU0;
                u10 = u11 = oldU1;
            }

            index = 0;
            temp = new ArrayList<>();

            // For each step in V path
            for (final Line2D lineV : linesV)
            {
                // V step goes (xp0, yp0) to (xp1, yp1), the direction vector is
                // (xx, yy)
                xp0 = lineV.pointStart.x();
                yp0 = lineV.pointStart.y();
                xp1 = lineV.pointEnd.x();
                yp1 = lineV.pointEnd.y();
                xx = xp1 - xp0;
                yy = yp1 - yp0;

                // Normalize (xx, yy)
                length = (float) Math.sqrt((xx * xx) + (yy * yy));
                if (!Math3D.nul(length))
                {
                    xx /= length;
                    yy /= length;
                }

                // Compute V for actual face
                v00 = v10 = lineV.start;
                v01 = v11 = lineV.end;

                // Up left position and normal
                x00 = a0 - (y * xp0);
                y00 = b0 + (x * xp0);
                z00 = yp0;

                nx00 = (mult * (x + (a0 - (y * xx)))) / 2f;
                ny00 = (mult * (y + (b0 + (x * xx)))) / 2f;
                nz00 = (mult * yy) / 2f;

                // Up right position and normal
                x10 = a1 - (y * xp0);
                y10 = b1 + (x * xp0);
                z10 = yp0;

                nx10 = (mult * (x + (a1 - (y * xx)))) / 2f;
                ny10 = (mult * (y + (b1 + (x * xx)))) / 2f;
                nz10 = (mult * yy) / 2f;

                // Down left position and normal
                x01 = a0 - (y * xp1);
                y01 = b0 + (x * xp1);
                z01 = yp1;

                nx01 = (mult * (x + (a0 - (y * xx)))) / 2f;
                ny01 = (mult * (y + (b0 + (x * xx)))) / 2f;
                nz01 = (mult * yy) / 2f;

                // Down right position and normal
                x11 = a1 - (y * xp1);
                y11 = b1 + (x * xp1);
                z11 = yp1;

                nx11 = (mult * (x + (a1 - (y * xx)))) / 2f;
                ny11 = (mult * (y + (b1 + (x * xx)))) / 2f;
                nz11 = (mult * yy) / 2f;

                // If it is not the first face time we goes on V path, join with old
                // face
                if (!first)
                {
                    // Get old face information to make the join join
                    dir0 = old.get(index++);
                    dir1 = old.get(index++);
                    p0 = old.get(index++);
                    p1 = old.get(index++);

                    // Get first old point and direction
                    a00 = p0.position().x();
                    b00 = p0.position().y();
                    c00 = p0.position().z();
                    vx00 = dir0.position().x();
                    vy00 = dir0.position().y();
                    vz00 = dir0.position().z();
                    // If direction is not zero vector
                    length = (float) Math.sqrt((vx00 * vx00) + (vy00 * vy00) + (vz00 * vz00));

                    if (!Math3D.nul(length))
                    {
                        // Normalize direction
                        vx00 /= length;
                        vy00 /= length;
                        vz00 /= length;

                        // Get first new point and direction
                        a10 = x00;
                        b10 = y00;
                        c10 = z00;
                        vx10 = x10 - x00;
                        vy10 = y10 - y00;
                        vz10 = z10 - z00;

                        // If the direction is not zero vector
                        length = (float) Math.sqrt((vx10 * vx10) + (vy10 * vy10) + (vz10 * vz10));

                        if (!Math3D.nul(length))
                        {
                            // Normalize the vector
                            vx10 /= length;
                            vy10 /= length;
                            vz10 /= length;

                            // If the two direction are not colinear, then a corner
                            // join is need
                            if (!Math3D.equal(Math.abs((vx00 * vx10) + (vy00 * vy10) + (vz00 * vz10)), 1f))
                            {
                                // Get second old position and direction
                                a01 = p1.position().x();
                                b01 = p1.position().y();
                                c01 = p1.position().z();
                                vx01 = dir1.position().x();
                                vy01 = dir1.position().y();
                                vz01 = dir1.position().z();

                                // Normalize direction
                                length = (float) Math.sqrt((vx01 * vx01) + (vy01 * vy01) + (vz01 * vz01));
                                vx01 /= length;
                                vy01 /= length;
                                vz01 /= length;

                                // Get second new point and direction
                                a11 = x01;
                                b11 = y01;
                                c11 = z01;
                                vx11 = x11 - x01;
                                vy11 = y11 - y01;
                                vz11 = z11 - z01;

                                // Normalize direction
                                length = (float) Math.sqrt((vx11 * vx11) + (vy11 * vy11) + (vz11 * vz11));
                                vx11 /= length;
                                vy11 /= length;
                                vz11 /= length;

                                l0 = l1 = 0f;

                                // Compute intersection between first old and first new
                                if (Math3D.nul(vx00))
                                {
                                    if (!Math3D.nul(vx10))
                                    {
                                        l0 = (a00 - a10) / vx10;
                                    }
                                    else if (Math3D.nul(vy00))
                                    {
                                        l0 = (b00 - b10) / vy10;
                                    }
                                    else if (Math3D.nul(vz00))
                                    {
                                        l0 = (c00 - c10) / vz10;
                                    }
                                    else
                                    {
                                        l0 = (((b00 * vz00) - (c00 * vy00) - (b10 * vz00)) + (c10 * vy00)) /
                                             ((vy10 * vz00) - (vz10 * vy00));
                                    }
                                }
                                else if (Math3D.nul(vy00))
                                {
                                    if (!Math3D.nul(vy10))
                                    {
                                        l0 = (b00 - b10) / vy10;
                                    }
                                    else if (Math3D.nul(vz00))
                                    {
                                        l0 = (c00 - c10) / vz10;
                                    }
                                    else
                                    {
                                        l0 = (((a00 * vz00) - (c00 * vx00) - (a10 * vz00)) + (c10 * vx00)) /
                                             ((vx10 * vz00) - (vz10 * vx00));
                                    }
                                }
                                else if (Math3D.nul(vz00) && !Math3D.nul(vz10))
                                {
                                    l0 = (c00 - c10) / vz10;
                                }
                                else
                                {
                                    l0 = (((a00 * vy00) - (b00 * vx00) - (a10 * vy00)) + (b10 * vx00)) /
                                         ((vx10 * vy00) - (vy10 * vx00));
                                }
                                mx0 = a10 + (vx10 * l0);
                                my0 = b10 + (vy10 * l0);
                                mz0 = c10 + (vz10 * l0);

                                // Compute intersection between second old and second
                                // new
                                if (Math3D.nul(vx01))
                                {
                                    if (!Math3D.nul(vx11))
                                    {
                                        l1 = (a01 - a11) / vx11;
                                    }
                                    else if (Math3D.nul(vy01))
                                    {
                                        l1 = (b01 - b11) / vy11;
                                    }
                                    else if (Math3D.nul(vz01))
                                    {
                                        l1 = (c01 - c11) / vz11;
                                    }
                                    else
                                    {
                                        l1 = (((b01 * vz01) - (c01 * vy01) - (b11 * vz01)) + (c11 * vy01)) /
                                             ((vy11 * vz01) - (vz11 * vy01));
                                    }
                                }
                                else if (Math3D.nul(vy01))
                                {
                                    if (!Math3D.nul(vy11))
                                    {
                                        l1 = (b01 - b11) / vy11;
                                    }
                                    else if (Math3D.nul(vz01))
                                    {
                                        l1 = (c01 - c11) / vz11;
                                    }
                                    else
                                    {
                                        l1 = (((a01 * vz01) - (c01 * vx01) - (a11 * vz01)) + (c11 * vx01)) /
                                             ((vx11 * vz01) - (vz11 * vx01));
                                    }
                                }
                                else if (Math3D.nul(vz01) && !Math3D.nul(vz11))
                                {
                                    l1 = (c01 - c11) / vz11;
                                }
                                else
                                {
                                    l1 = (((a01 * vy01) - (b01 * vx01) - (a11 * vy01)) + (b11 * vx01)) /
                                         ((vx11 * vy01) - (vy11 * vx01));
                                }

                                mx1 = a11 + (vx11 * l1);
                                my1 = b11 + (vy11 * l1);
                                mz1 = c11 + (vz11 * l1);

                                // If we decide to linearize, linearize U
                                if (linearize)
                                {
                                    u00 = p0.uv().x();
                                    u01 = p1.uv().x();

                                    u00 += (float) Math.sqrt(Math3D.square(mx0 - p0.position().x()) +
                                                             Math3D.square(my0 - p0.position().y())
                                                             + Math3D.square(mz0 - p0.position().z()));
                                    u01 += (float) Math.sqrt(Math3D.square(mx1 - p1.position().x()) +
                                                             Math3D.square(my1 - p1.position().y())
                                                             + Math3D.square(mz1 - p1.position().z()));
                                }

                                // Create first part of the corner
                                mesh.addVertexToTheActualFace(p0);
                                mesh.addVertexToTheActualFace(p1);
                                mesh.addVertexToTheActualFace(new Vertex(mx1, my1, mz1, u01, v01, nx01, ny01, nz01));
                                mesh.addVertexToTheActualFace(new Vertex(mx0, my0, mz0, u00, v00, nx00, ny00, nz00));
                                mesh.endFace();

                                // We consider the end of the first part like the end of
                                // the old face
                                p0.set(mx0, my0, mz0);
                                p1.set(mx1, my1, mz1);

                                if (linearize)
                                {
                                    p0.uv().set(u00, p0.uv().y());
                                    p1.uv().set(u01, p1.uv().y());
                                }
                            }
                        }
                    }

                    // Link old face to new one.
                    // Remember that it could be the end of the corner. In this case
                    // its create the end of the corner
                    if (linearize)
                    {
                        u00 = p0.uv().x();
                        u01 = p1.uv().x();

                        u00 += (float) Math.sqrt(Math3D.square(x00 - p0.position().x()) +
                                                 Math3D.square(y00 - p0.position().y())
                                                 + Math3D.square(z00 - p0.position().z()));
                        u01 += (float) Math.sqrt(Math3D.square(x01 - p1.position().x()) +
                                                 Math3D.square(y01 - p1.position().y())
                                                 + Math3D.square(z01 - p1.position().z()));
                    }

                    mesh.addVertexToTheActualFace(p0);
                    mesh.addVertexToTheActualFace(p1);
                    mesh.addVertexToTheActualFace(new Vertex(x01, y01, z01, u01, v01, nx01, ny01, nz01));
                    mesh.addVertexToTheActualFace(new Vertex(x00, y00, z00, u00, v00, nx00, ny00, nz00));
                    mesh.endFace();
                }

                // Draw actual face
                if (linearize)
                {
                    u10 = u00 + (float) Math.sqrt(
                            Math3D.square(x00 - x10) + Math3D.square(y00 - y10) + Math3D.square(z00 - z10));
                    u11 = u01 + (float) Math.sqrt(
                            Math3D.square(x01 - x11) + Math3D.square(y01 - y11) + Math3D.square(z01 - z11));
                }

                mesh.addVertexToTheActualFace(new Vertex(x00, y00, z00, u00, v00, nx00, ny00, nz00));
                mesh.addVertexToTheActualFace(new Vertex(x01, y01, z01, u01, v01, nx01, ny01, nz01));
                mesh.addVertexToTheActualFace(new Vertex(x11, y11, z11, u11, v11, nx11, ny11, nz11));
                mesh.addVertexToTheActualFace(new Vertex(x10, y10, z10, u10, v10, nx10, ny10, nz10));
                mesh.endFace();

                // Memorize informations for the next join
                if (linearize)
                {
                    u00 = u01 = oldU0;
                    u10 = u11 = oldU1;
                }

                temp.add(new Vertex(x10 - x00, y10 - y00, z10 - z00, u10, v10, nx10, ny10, nz10));
                temp.add(new Vertex(x11 - x01, y11 - y01, z11 - z01, u11, v11, nx11, ny11, nz11));
                temp.add(new Vertex(x10, y10, z10, u10, v10, nx10, ny10, nz10));
                temp.add(new Vertex(x11, y11, z11, u11, v11, nx11, ny11, nz11));
            }

            if (linearize)
            {
                oldU0 = u00 + (float) Math.sqrt(
                        Math3D.square(x00 - x10) + Math3D.square(y00 - y10) + Math3D.square(z00 - z10));
                oldU1 = u01 + (float) Math.sqrt(
                        Math3D.square(x01 - x11) + Math3D.square(y01 - y11) + Math3D.square(z01 - z11));
            }

            first = false;
            old = temp;
        }

        // On linearize mode, try to make valid U
        if (linearize)
        {
            mesh.multUV(multU / Math.max(oldU0, oldU1), 1);
        }
        else
        {
            mesh.multUV(multU, 1);
        }

        return mesh;
    }

    /**
     * Create a mesh with to path.<br>
     * The path for V walk throw the path for U
     *
     * @param pathU      Path for U
     * @param precisionU Path for U precision
     * @param pathV      Path for V
     * @param precisionV Path for V precision
     * @return Constructed mesh
     */
    public static @NotNull Mesh createMesh(
            final @NotNull Path pathU, final int precisionU,
            final @NotNull Path pathV, final int precisionV)
    {
        // Initialization
        final Mesh mesh = new Mesh();

        final List<Line2D> linesU = pathU.computePath(precisionU);
        final List<Line2D> linesV = pathV.computePath(precisionV);

        float x00, y00, z00, u00, v00, nx00, ny00, nz00;
        float x10, y10, z10, u10, v10, nx10, ny10, nz10;
        float x01, y01, z01, u01, v01, nx01, ny01, nz01;
        float x11, y11, z11, u11, v11, nx11, ny11, nz11;

        float x, y, a0, b0, a1, b1, xp0, yp0, xp1, yp1, xx, yy;

        float length;

        // For each step in U path
        for (final Line2D lineU : linesU)
        {
            // U step goes (a0, b0) to (a1, b1), the direction vector is (x, y)
            a0 = lineU.pointStart.x();
            b0 = lineU.pointStart.y();
            a1 = lineU.pointEnd.x();
            b1 = lineU.pointEnd.y();
            x = a1 - a0;
            y = b1 - b0;
            // Normalize (x,y)
            length = (float) Math.sqrt((x * x) + (y * y));

            if (!Math3D.nul(length))
            {
                x /= length;
                y /= length;
            }

            // Compute U values of each face at this step
            u00 = u01 = lineU.start;
            u10 = u11 = lineU.end;

            // For each step on V path
            for (final Line2D lineV : linesV)
            {
                // V step goes (xp0, yp0) to (xp1, yp1), the direction vector is
                // (xx, yy)
                xp0 = lineV.pointStart.x();
                yp0 = lineV.pointStart.y();
                xp1 = lineV.pointEnd.x();
                yp1 = lineV.pointEnd.y();
                xx = xp1 - xp0;
                yy = yp1 - yp0;
                // Normalize(xx, yy)
                length = (float) Math.sqrt((xx * xx) + (yy * yy));

                if (!Math3D.nul(length))
                {
                    xx /= length;
                    yy /= length;
                }
                // Compute V for the actual face
                v00 = v10 = lineV.start;
                v01 = v11 = lineV.end;

                // Up left position and normal
                x00 = a0 - (y * xp0);
                y00 = b0 + (x * xp0);
                z00 = yp0;

                nx00 = (x + (a0 - (y * xx))) / 2f;
                ny00 = (y + (b0 + (x * xx))) / 2f;
                nz00 = yy / 2f;

                // Up right position and normal
                x10 = a1 - (y * xp0);
                y10 = b1 + (x * xp0);
                z10 = yp0;

                nx10 = (x + (a1 - (y * xx))) / 2f;
                ny10 = (y + (b1 + (x * xx))) / 2f;
                nz10 = yy / 2f;

                // Down left position and normal
                x01 = a0 - (y * xp1);
                y01 = b0 + (x * xp1);
                z01 = yp1;

                nx01 = (x + (a0 - (y * xx))) / 2f;
                ny01 = (y + (b0 + (x * xx))) / 2f;
                nz01 = yy / 2f;

                // Down right position and normal
                x11 = a1 - (y * xp1);
                y11 = b1 + (x * xp1);
                z11 = yp1;

                nx11 = (x + (a1 - (y * xx))) / 2f;
                ny11 = (y + (b1 + (x * xx))) / 2f;
                nz11 = yy / 2f;

                // Create the face
                mesh.addVertexToTheActualFace(new Vertex(x00, y00, z00, u00, v00, nx00, ny00, nz00));
                mesh.addVertexToTheActualFace(new Vertex(x01, y01, z01, u01, v01, nx01, ny01, nz01));
                mesh.addVertexToTheActualFace(new Vertex(x11, y11, z11, u11, v11, nx11, ny11, nz11));
                mesh.addVertexToTheActualFace(new Vertex(x10, y10, z10, u10, v10, nx10, ny10, nz10));
                mesh.endFace();
            }
        }

        return mesh;
    }

    /**
     * Create a node on parsing XML
     *
     * @param markupXML Markup to parse
     * @return Created node
     * @throws Exception On creation problem
     */
    public static @NotNull Node createNode(final @NotNull MarkupXML markupXML) throws Exception
    {
        if (!markupXML.isParameter(ConstantsXML.MARKUP_NODE_type))
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory parameter ", ConstantsXML.MARKUP_NODE_type, " in ",
                                         markupXML.getName()));
        }

        try
        {
            final NodeType nodeType = NodeType.valueOf(markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_type, ""));
            Node           node     = null;

            switch (nodeType)
            {
                case BOX:
                    node = new Box();
                    break;
                case CLONE:
                    node = new ObjectClone((Object3D) null);
                    break;
                case NODE:
                    node = new Node();
                    break;
                case OBJECT3D:
                    node = new Object3D();
                    break;
                case PATH_GEOM:
                    node = new PathGeom();
                    break;
                case PLANE:
                    node = new Plane();
                    break;
                case REVOLUTION:
                    node = new Revolution();
                    break;
                case SPHERE:
                    node = new Sphere();
                    break;
                case EQUATION:
                    // {@todo} TODO Implements createNode in jhelp.engine.util [JHelpEngine]
                    Debug.todo("Implements createNode in jhelp.engine.util [JHelpEngine] Eqaution case");
                    node = new Node();
                    break;
                default:
                    // {@todo} TODO Implements createNode in jhelp.engine.util [JHelpEngine]
                    Debug.todo("Implements createNode in jhelp.engine.util [JHelpEngine] Missing case");
                    node = new Node();
                    break;
            }

            node.loadFromXML(markupXML);

            return node;
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on constructs the node", exception);
        }
    }

    /**
     * Compute the volume of intersection that may append when given nodes will translates of given vectors
     *
     * @param node1   First node
     * @param vector1 Translation that will apply to first node
     * @param node2   Second node
     * @param vector2 Translation that will apply to second node
     * @return Volume of future intersection. 0 means they will not have any intersection
     */
    public static float futureIntersectionVolume(
            final @NotNull Node node1, final @NotNull Point3D vector1,
            final @NotNull Node node2, final @NotNull Point3D vector2)
    {
        VirtualBox virtualBox = node1.computeProjectedTotalBox();
        virtualBox.translate(vector1);

        if (virtualBox.empty())
        {
            return 0;
        }

        final float xmin1 = virtualBox.minX();
        final float ymin1 = virtualBox.minY();
        final float zmin1 = virtualBox.minZ();
        final float xmax1 = virtualBox.maxX();
        final float ymax1 = virtualBox.maxY();
        final float zmax1 = virtualBox.maxZ();

        virtualBox = node2.computeProjectedTotalBox();
        virtualBox.translate(vector2);

        if (virtualBox.empty())
        {
            return 0;
        }

        final float xmin2 = virtualBox.minX();
        final float ymin2 = virtualBox.minY();
        final float zmin2 = virtualBox.minZ();
        final float xmax2 = virtualBox.maxX();
        final float ymax2 = virtualBox.maxY();
        final float zmax2 = virtualBox.maxZ();

        if ((xmin1 > xmax2) || (ymin1 > ymax2) || (zmin1 > zmax2) || (xmin2 > xmax1) || (ymin2 > ymax1) ||
            (zmin2 > zmax1))
        {
            return 0;
        }

        final float xmin = Math.max(xmin1, xmin2);
        final float xmax = Math.min(xmax1, xmax2);

        if (xmin >= xmax)
        {
            return 0;
        }

        final float ymin = Math.max(ymin1, ymin2);
        final float ymax = Math.min(ymax1, ymax2);

        if (ymin >= ymax)
        {
            return 0;
        }

        final float zmin = Math.max(zmin1, zmin2);
        final float zmax = Math.min(zmax1, zmax2);

        if (zmin >= zmax)
        {
            return 0;
        }

        return (xmax - xmin) * (ymax - ymin) * (zmax - zmin);
    }

    /**
     * Get a Color4f parameter from XML markup
     *
     * @param markupXML     XML markup where extract
     * @param parameterName Parameter name
     * @return The color
     */
    public static @NotNull Color4f getColor4fParameter(
            final @NotNull MarkupXML markupXML, final @NotNull String parameterName)
    {
        final Color4f color4f = new Color4f();

        try
        {
            final StringTokenizer stringTokenizer = new StringTokenizer(markupXML.obtainParameter(parameterName,
                                                                                                  "1 1 1 1"));
            color4f.alpha(Float.parseFloat(stringTokenizer.nextToken()));
            color4f.red(Float.parseFloat(stringTokenizer.nextToken()));
            color4f.green(Float.parseFloat(stringTokenizer.nextToken()));
            color4f.blue(Float.parseFloat(stringTokenizer.nextToken()));
        }
        catch (final Exception ignored)
        {
        }

        return color4f;
    }

    /**
     * Retrieve a Point3D parameter from XML markup
     *
     * @param markupXML     XML markup where extract
     * @param parameterName Parameter name
     * @return Point read
     */
    public static @NotNull Point3D getPoint3DParameter(
            final @NotNull MarkupXML markupXML, final @NotNull String parameterName)
    {
        final Point3D point = new Point3D();
        try
        {
            StringTokenizer stringTokenizer = new StringTokenizer(markupXML.obtainParameter(parameterName, "0 0 0"));
            final float     x               = Float.parseFloat(stringTokenizer.nextToken());
            final float     y               = Float.parseFloat(stringTokenizer.nextToken());
            final float     z               = Float.parseFloat(stringTokenizer.nextToken());
            point.set(x, y, z);
            stringTokenizer = null;
        }
        catch (final Exception exception)
        {
        }
        return point;
    }

    /**
     * Create a bump texture
     *
     * @param color    Unify color
     * @param bump     Texture with bump informations
     * @param contrast Contrast to apply
     * @param dark     Dark level
     * @param shiftX   X shifting
     * @param shiftY   Y shifting
     * @return Bump texture
     */
    public static @NotNull Texture obtainBumpTexture(
            final @NotNull Color color, final @NotNull Texture bump,
            final float contrast, final float dark, final int shiftX, final int shiftY)
    {
        return Tool3D.obtainBumpTexture(new Texture("Unify", bump.width(), bump.height(), color),
                                        bump, contrast, dark, shiftX, shiftY);
    }

    /**
     * Create a bump texture with 2 textures.<br>
     * The 2 textures MUST have same dimensions
     *
     * @param original Texture where add bump
     * @param bump     Texture with bump information
     * @param contrast Contrast
     * @param dark     Dark level
     * @param shiftX   X shift
     * @param shiftY   Y shift
     * @return Bump texture
     */
    public static @NotNull Texture obtainBumpTexture(
            final @NotNull Texture original, @NotNull final Texture bump,
            float contrast, final float dark, final int shiftX, final int shiftY)
    {
        if ((original.width() != bump.width()) || (original.height() != bump.height()))
        {
            throw new IllegalArgumentException("Original and bump textures must have same size");
        }

        final String name = UtilText.concatenate(original.textureName(), "_BUMP_", bump.textureName());

        final int width  = original.width();
        final int height = original.height();

        if (contrast <= 0.5)
        {
            contrast *= 2;
        }
        else
        {
            contrast *= 18;
            contrast -= 8;
        }

        Texture bumped = Texture.obtainTexture(name);

        if (bumped == null)
        {
            bumped = new Texture(name, width, height);
        }

        bumped.setPixels(bump);
        bumped.toGray();
        bumped.contrast(contrast);

        final Texture temp = new Texture("temp", width, height);

        temp.setPixels(bumped);
        temp.multiplyTexture(original);
        temp.darker(dark);

        bumped.invert();
        bumped.multiplyTexture(original);
        bumped.darker(dark);
        bumped.shift(shiftX, shiftY);
        bumped.addTexture(temp);

        temp.destroy();
        bumped.flush();
        return bumped;
    }

    /**
     * Indicates if given nodes will collide after a translation
     *
     * @param node1   First node
     * @param vector1 Translation that will apply to first node
     * @param node2   Second node
     * @param vector2 Translation that will apply to second node
     * @return {@code ture} if collision will append
     */
    public static boolean willCollide(
            final @NotNull Node node1, final @NotNull Point3D vector1,
            final @NotNull Node node2, @NotNull final Point3D vector2)
    {
        return Tool3D.futureIntersectionVolume(node1, vector1, node2, vector2) > 0;
    }
}