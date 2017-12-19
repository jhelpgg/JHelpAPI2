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
import com.sun.istack.internal.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.util.NodeComparatorZorder;
import jhelp.engine2.util.Tool3D;
import jhelp.xml.MarkupXML;
import org.lwjgl.opengl.GL11;

/**
 * Scene 3D
 *
 * @author JHelp
 */
public class Scene
{
    /**
     * Background color'd blue
     */
    private       float  blueBackground;
    /**
     * Background color'd green
     */
    private       float  greenBackground;
    /**
     * List of nodes
     */
    private       Node[] nodeList;
    /**
     * Background color'd red
     */
    private       float  redBackground;
    /**
     * Root node
     */
    private final Node   root;

    /**
     * Constructs empty Scene
     */
    public Scene()
    {
        this.redBackground = this.greenBackground = this.blueBackground = 1f;
        this.root = new Node();
        this.root.nodeName = "ROOT";
    }

    /**
     * Force scene to refresh.
     */
    private synchronized void refresh()
    {
        this.nodeList = null;
    }

    /**
     * Draw the background
     */
    @ThreadOpenGL
    void drawBackground()
    {
        GL11.glClearColor(this.redBackground, this.greenBackground, this.blueBackground, 1f);
    }

    /**
     * Change mouse state
     *
     * @param leftButton  Indicates if left button is down
     * @param rightButton Indicates if right button is down
     * @param over        Node that mouse is over
     */
    void mouseState(final boolean leftButton, final boolean rightButton, final @Nullable Node over)
    {
        Node              node       = this.root;
        final Stack<Node> stackNodes = new Stack<>();
        stackNodes.push(node);

        while (!stackNodes.isEmpty())
        {
            node = stackNodes.pop();
            node.mouseState(leftButton, rightButton, node == over);
            for (final Node child : node.children)
            {
                stackNodes.push(child);
            }
        }
    }

    /**
     * Render a node in picking mode
     *
     * @param node Node to render
     */
    @ThreadOpenGL
    void renderPickingUV(final @NotNull Node node)
    {
        this.root.renderPickingUV(node);
    }

    /**
     * Render the scene
     *
     * @param window3D Window where the scene draw
     */
    @ThreadOpenGL
    synchronized void renderTheScene(@NotNull Window3D window3D)
    {
        Node node;
        // Get the node's list
        if (this.nodeList == null)
        {
            final Stack<Node> stack = new Stack<>();
            stack.push(this.root);
            final ArrayList<Node> nodes = new ArrayList<>();

            while (!stack.isEmpty())
            {
                node = stack.pop();
                nodes.add(node);
                for (final Node child : node.children)
                {
                    stack.push(child);
                }
            }

            this.nodeList = new Node[nodes.size()];
            this.nodeList = nodes.toArray(this.nodeList);
        }

        // Compute Z-Orders
        int length = this.nodeList.length;

        for (int i = 0; i < length; i++)
        {
            node = this.nodeList[i];
            node.zOrder = node.reverseProjection(node.center()).z();
        }

        // Sort nodes
        Arrays.sort(this.nodeList, NodeComparatorZorder.NODE_COMPARATOR_Z_ORDER);

        length--;

        // Draw nodes
        for (; length >= 0; length--)
        {
            node = this.nodeList[length];

            if (node.visible)
            {
                GL11.glPushMatrix();
                node.matrixRootToMe();
                node.renderSpecific();

                if (node.textureHotspot != null)
                {
                    window3D.showHotspot(node);
                }

                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Render the scene in picking mode
     *
     * @param window3D Window where scene is draw
     */
    @ThreadOpenGL
    void renderTheScenePicking(final @NotNull Window3D window3D)
    {
        this.root.renderTheNodePicking(window3D);
    }

    /**
     * Add node to the scene
     *
     * @param node Node to add
     */
    public void add(final @NotNull Node node)
    {
        this.root.addChild(node);
        this.refresh();
    }

    /**
     * Obtain all child node hierarchical with the given name
     *
     * @param nodeName Name search
     * @return List of matches nodes
     */
    public @NotNull List<Node> allNodes(final @Nullable String nodeName)
    {
        return this.root.allNodes(nodeName);
    }

    /**
     * Change the rotation on X axis
     *
     * @param angleX Rotation angle
     */
    public void angleX(final float angleX)
    {
        this.root.angleX(angleX);
    }

    /**
     * Change the rotation on Y axis
     *
     * @param angleY Rotation angle
     */
    public void angleY(final float angleY)
    {
        this.root.angleY(angleY);
    }

    /**
     * Change the rotation on Z axis
     *
     * @param angleZ Rotation angle
     */
    public void angleZ(final float angleZ)
    {
        this.root.angleZ(angleZ);
    }

    /**
     * Change background color
     *
     * @param background New background color
     */
    public void background(final @NotNull Color background)
    {
        this.redBackground = background.getRed() / 255f;
        this.greenBackground = background.getGreen() / 255f;
        this.blueBackground = background.getBlue() / 255f;
    }

    /**
     * Change background color
     *
     * @param red   Red
     * @param green Green
     * @param blue  Blue
     */
    public void background(final float red, final float green, final float blue)
    {
        this.redBackground = red;
        this.greenBackground = green;
        this.blueBackground = blue;
    }

    /**
     * Search throw child hierarchical and return the first node with the given name
     *
     * @param nodeName Name search
     * @return Find node
     */
    public @Nullable Node firstNode(final @Nullable String nodeName)
    {
        return this.root.firstNode(nodeName);
    }

    /**
     * Load scene parameter form XML
     *
     * @param markupXML Markup to parse
     * @throws Exception On parsing problem
     */
    public void loadFromXML(final @NotNull MarkupXML markupXML) throws Exception
    {
        final Color4f color4f = Tool3D.getColor4fParameter(markupXML, ConstantsXML.MARKUP_SCENE_background);
        this.redBackground = color4f.red();
        this.greenBackground = color4f.green();
        this.blueBackground = color4f.blue();

        final MarkupXML markup = markupXML.obtainChildren(ConstantsXML.MARKUP_NODE).nextElement();
        this.root.loadFromXML(markup);

        this.refresh();
    }

    /**
     * A child
     *
     * @param index Child index
     * @return The child
     */
    public @NotNull Node node(final int index)
    {
        return this.root.child(index);
    }

    /**
     * Children list
     *
     * @return Children list
     */
    public @NotNull Iterator<Node> nodes()
    {
        return this.root.children();
    }

    /**
     * Search the node with a specific picking color
     *
     * @param color Picking color
     * @return The node
     */
    public @Nullable Node pickingNode(final @NotNull Color4f color)
    {
        return this.root.pickingNode(color);
    }

    /**
     * Change scene position
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void position(final float x, final float y, final float z)
    {
        this.root.position(x, y, z);
    }

    /**
     * Remove node from the scene
     *
     * @param node Node to remove
     */
    public void remove(final @NotNull Node node)
    {
        this.root.removeChild(node);
        this.refresh();
    }

    /**
     * Remove all nodes from scene
     */
    public void removeAllNodes()
    {
        this.root.removeAllChildren();
        this.refresh();
    }

    /**
     * Scene root
     *
     * @return Scene root
     */
    public @NotNull Node root()
    {
        return this.root;
    }

    /**
     * Rotate around X axis
     *
     * @param angleX Rotation angle
     */
    public void rotateAngleX(final float angleX)
    {
        this.root.rotateAngleX(angleX);
    }

    /**
     * Rotate around Y axis
     *
     * @param angleY Rotation angle
     */
    public void rotateAngleY(final float angleY)
    {
        this.root.rotateAngleY(angleY);
    }

    /**
     * Rotate around Z axis
     *
     * @param angleZ Rotation angle
     */
    public void rotateAngleZ(final float angleZ)
    {
        this.root.rotateAngleZ(angleZ);
    }

    /**
     * Save scen in XML
     *
     * @return XML representation
     */
    public @NotNull MarkupXML saveToXML()
    {
        final MarkupXML markupXML = new MarkupXML(ConstantsXML.MARKUP_SCENE);
        Tool3D.addColor4fParameter(markupXML, ConstantsXML.MARKUP_SCENE_background,
                                   new Color4f(this.redBackground, this.greenBackground, this.blueBackground));
        markupXML.addChild(this.root.saveToXML());
        return markupXML;
    }

    /**
     * Translate the scene
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void translate(final float x, final float y, final float z)
    {
        this.root.translate(x, y, z);
    }
}