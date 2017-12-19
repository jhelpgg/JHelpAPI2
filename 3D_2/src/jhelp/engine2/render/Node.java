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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.render.event.NodeListener;
import jhelp.engine2.render.event.NodePositionListener;
import jhelp.engine2.render.event.PickUVlistener;
import jhelp.engine2.util.Math3D;
import jhelp.engine2.util.Rotf;
import jhelp.engine2.util.Tool3D;
import jhelp.engine2.util.Vec3f;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.Triplet;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;
import jhelp.xml.MarkupXML;
import org.lwjgl.opengl.GL11;

/**
 * General node of the graph scene.<br>
 * It could be use also as a virtual object<br>
 * <br>
 *
 * @author JHelp
 */
public class Node
{
    /**
     * Color picking ID
     */
    private static int ID_PICKING = 0;

    /**
     * Task for say to one listener that a click happen on the node
     *
     * @author JHelp
     */
    class TaskFireMouseClick
            implements ConsumerTask<Triplet<NodeListener, Boolean, Boolean>>
    {
        /**
         * Create a new instance of TaskFireMouseClick
         */
        TaskFireMouseClick()
        {
        }

        /**
         * Called when task turn comes <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter The triplet of the listener to alert, left button state, right button state
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Triplet<NodeListener, Boolean, Boolean> parameter)
        {
            parameter.element1.mouseClick(Node.this, parameter.element2, parameter.element3);
        }
    }

    /**
     * Task say to one listener that mouse enter the node
     *
     * @author JHelp
     */
    class TaskFireMouseEnter
            implements ConsumerTask<NodeListener>
    {
        /**
         * Create a new instance of TaskFireMouseEnter
         */
        TaskFireMouseEnter()
        {
        }

        /**
         * Called when task turn comes <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter The listener to alert
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final NodeListener parameter)
        {
            parameter.mouseEnter(Node.this);
        }
    }

    /**
     * Task say to one listener that mouse exit the node
     *
     * @author JHelp
     */
    class TaskFireMouseExit
            implements ConsumerTask<NodeListener>
    {
        /**
         * Create a new instance of TaskFireMouseExit
         */
        TaskFireMouseExit()
        {
        }

        /**
         * Called when task turn comes <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter The listener to alert
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final NodeListener parameter)
        {
            parameter.mouseExit(Node.this);
        }
    }

    /**
     * Additional information
     */
    private       Object                            additionalInformation;
    /**
     * Angle X relative to it's parent
     */
    private       float                             angleX;
    /**
     * Angle Y relative to it's parent
     */
    private       float                             angleY;
    /**
     * Angle Z relative to it's parent
     */
    private       float                             angleZ;
    /**
     * Listeners list
     */
    private final List<NodeListener>                arrayListListners;
    /**
     * Blue value of the color use in color picking
     */
    private final float                             bluePicking;
    /**
     * Indicates if the node could be pick
     */
    private       boolean                           canBePick;
    /**
     * ID for color picking
     */
    private final int                               colorPickingId;
    /**
     * Green value of the color use in color picking
     */
    private final float                             greenPicking;
    private final ArrayObject<NodePositionListener> nodePositionListeners;
    /**
     * Indicates if the mouse is over the object
     */
    private       boolean                           over;
    /**
     * Node's parent
     */
    private       Node                              parent;
    /**
     * Red value of the color use in color picking
     */
    private final float                             redPicking;
    /**
     * Scale on X
     */
    private       float                             scaleX;
    /**
     * Scale on Y
     */
    private       float                             scaleY;
    /**
     * Scale on Z
     */
    private       float                             scaleZ;
    /**
     * Indicates is the node is selected
     */
    private       boolean                           selected;
    /**
     * Indicates if the wire frame are showing
     */
    private       boolean                           showWire;
    /**
     * Task for alert one listener that mouse click on the node
     */
    private final TaskFireMouseClick taskFireMouseClick = new TaskFireMouseClick();
    /**
     * Task for alert one listener that mouse enter on the node
     */
    private final TaskFireMouseEnter taskFireMouseEnter = new TaskFireMouseEnter();
    /**
     * Task for alert one listener that mouse exit from the node
     */
    private final TaskFireMouseExit  taskFireMouseExit  = new TaskFireMouseExit();
    /**
     * Color to use for wire frame
     */
    private Color4f wireColor;
    /**
     * X relative to it's parent
     */
    private float   x;
    /**
     * Indicates if the angle X rotation is limited
     */
    private boolean xAngleLimited;
    /**
     * Angle X maximum
     */
    private float   xAngleMax;
    /**
     * Angle X minimum
     */
    private float   xAngleMin;
    /**
     * Indicates if the X move is limited
     */
    private boolean xLimited;
    /**
     * X maximum
     */
    private float   xMax;
    /**
     * X minimum
     */
    private float   xMin;
    /**
     * Y relative to it's parent
     */
    private float   y;
    /**
     * Indicates if the angle Y rotation is limited
     */
    private boolean yAngleLimited;
    /**
     * Angle Y maximum
     */
    private float   yAngleMax;
    /**
     * Angle Y minimum
     */
    private float   yAngleMin;
    /**
     * Indicates if the Y move is limited
     */
    private boolean yLimited;
    /**
     * Y maximum
     */
    private float   yMax;
    /**
     * Y minimum
     */
    private float   yMin;
    /**
     * Z relative to it's parent
     */
    private float   z;
    /**
     * Indicates if the angle Z rotation is limited
     */
    private boolean zAngleLimited;
    /**
     * Angle Z maximum
     */
    private float   zAngleMax;
    /**
     * Angle Z minimum
     */
    private float   zAngleMin;
    /**
     * Indicates if the Z move is limited
     */
    private boolean zLimited;
    /**
     * Z maximum
     */
    private float   zMax;
    /**
     * Z minimum
     */
    private float   zMin;
    /**
     * Node's children
     */
    Vector<Node> children;
    /**
     * Texture hotspot linked to the node or {@code null}
     */
    Texture      textureHotspot;
    /**
     * Indicates if the node is visible
     */
    boolean      visible;
    /**
     * Node type
     */
    protected NodeType       nodeType;
    /**
     * Node's name
     */
    public    String         nodeName;
    /**
     * Listener for UV picking
     */
    public    PickUVlistener pickUVlistener;
    /**
     * Order Z
     */
    public    float          zOrder;

    /**
     * Constructs the node
     */
    public Node()
    {
        this.nodeType = NodeType.NODE;
        this.arrayListListners = new ArrayList<>(4);
        this.visible = true;
        this.children = new Vector<>(8);
        this.x = this.y = this.z = this.angleX = this.angleY = this.angleZ = 0f;
        this.scaleX = this.scaleY = this.scaleZ = 1;
        this.nodePositionListeners = new ArrayObject<>();
        this.wireColor = Color4f.DEFAULT_WIRE_FRAME_COLOR;
        this.showWire = false;
        this.selected = false;
        this.over = false;
        this.colorPickingId = Node.ID_PICKING;
        Node.ID_PICKING += Math3D.PICKING_PRECISION;
        this.canBePick = false;
        this.redPicking = ((this.colorPickingId >> 16) & 0xFF) / 255f;
        this.greenPicking = ((this.colorPickingId >> 8) & 0xFF) / 255f;
        this.bluePicking = (this.colorPickingId & 0xFF) / 255f;
    }

    /**
     * Check if locations and rotations value are valid.<br>
     * Make corrections if need
     */
    private void checkValues()
    {
        if (this.xLimited)
        {
            if (this.x < this.xMin)
            {
                this.x = this.xMin;
            }
            if (this.x > this.xMax)
            {
                this.x = this.xMax;
            }
        }

        if (this.yLimited)
        {
            if (this.y < this.yMin)
            {
                this.y = this.yMin;
            }
            if (this.y > this.yMax)
            {
                this.y = this.yMax;
            }
        }

        if (this.zLimited)
        {
            if (this.z < this.zMin)
            {
                this.z = this.zMin;
            }
            if (this.z > this.zMax)
            {
                this.z = this.zMax;
            }
        }

        if (this.xAngleLimited)
        {
            if (this.angleX < this.xAngleMin)
            {
                this.angleX = this.xAngleMin;
            }
            if (this.angleX > this.xAngleMax)
            {
                this.angleX = this.xAngleMax;
            }
        }

        if (this.yAngleLimited)
        {
            if (this.angleY < this.yAngleMin)
            {
                this.angleY = this.yAngleMin;
            }
            if (this.angleY > this.yAngleMax)
            {
                this.angleY = this.yAngleMax;
            }
        }

        if (this.zAngleLimited)
        {
            if (this.angleZ < this.zAngleMin)
            {
                this.angleZ = this.zAngleMin;
            }
            if (this.angleZ > this.zAngleMax)
            {
                this.angleZ = this.zAngleMax;
            }
        }
    }

    private void fireNodePositionChanged()
    {
        synchronized (this.nodePositionListeners)
        {
            this.nodePositionListeners.forEach(
                    (ConsumerTask<NodePositionListener>) nodePositionListener ->
                            nodePositionListener.nodePositionChanged(this, this.x, this.y, this.z));
        }
    }

    /**
     * Locate the node in the scene
     */
    @ThreadOpenGL
    void matrix()
    {
        GL11.glTranslatef(this.x, this.y, this.z);
        GL11.glRotatef(this.angleX, 1f, 0f, 0f);
        GL11.glRotatef(this.angleY, 0f, 1f, 0f);
        GL11.glRotatef(this.angleZ, 0f, 0f, 1f);
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
    }

    /**
     * Apply the matrix for to go root to this node
     */
    @ThreadOpenGL
    void matrixRootToMe()
    {
        final Stack<Node> stack = new Stack<>();
        Node              node  = this;
        while (node != null)
        {
            stack.push(node);
            node = node.parent;
        }

        while (!stack.isEmpty())
        {
            stack.pop().matrix();
        }
    }

    /**
     * Action on mouse state change
     *
     * @param leftButton  Left mouse button state
     * @param rightButton Right mouse button state
     * @param over        Indicates if the mouse is over the node
     */
    void mouseState(final boolean leftButton, final boolean rightButton, final boolean over)
    {
        if (this.over != over)
        {
            this.over = over;

            if (this.over)
            {
                this.fireMouseEnter();
            }
            else
            {
                this.fireMouseExit();
            }

            return;
        }
        //
        if (!over)
        {
            return;
        }
        //
        if (leftButton || rightButton)
        {
            this.fireMouseClick(leftButton, rightButton);
        }
    }

    /**
     * Render for pick UV
     *
     * @param node Picking node
     */
    @ThreadOpenGL
    synchronized void renderPickingUV(final @NotNull Node node)
    {
        GL11.glPushMatrix();
        this.matrix();

        if (node == this)
        {
            this.renderSpecificPickingUV();
        }

        for (final Node child : this.children)
        {
            child.renderPickingUV(node);
        }

        GL11.glPopMatrix();
    }

    /**
     * Render the node for color picking
     *
     * @param window Window where the scene draw
     */
    @ThreadOpenGL
    synchronized void renderTheNodePicking(final @NotNull Window3D window)
    {
        GL11.glPushMatrix();
        this.matrix();
        if (this.visible && this.canBePick)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(this.redPicking, this.greenPicking, this.bluePicking, 1f);
            this.renderSpecificPicking();
            if (this.textureHotspot != null)
            {
                window.drawPickHotspot(this, this.redPicking, this.greenPicking, this.bluePicking);
            }
        }
        for (final Node child : this.children)
        {
            child.renderTheNodePicking(window);
        }
        GL11.glPopMatrix();
    }

    /**
     * Over write to do something just after parsing
     */
    protected void endParseXML()
    {
    }

    /**
     * Signals to the listener that mouse click on the node
     *
     * @param leftButton  Left mouse button state
     * @param rightButton Right mouse button state
     */
    protected void fireMouseClick(final boolean leftButton, final boolean rightButton)
    {
        synchronized (this.arrayListListners)
        {
            for (final NodeListener nodeListener : this.arrayListListners)
            {
                ThreadManager.parallel(this.taskFireMouseClick,
                                       new Triplet<>(nodeListener, leftButton, rightButton));
            }
        }
    }

    /**
     * Signals to the listener that mouse enter on the node
     */
    protected void fireMouseEnter()
    {
        synchronized (this.arrayListListners)
        {
            for (final NodeListener nodeListener : this.arrayListListners)
            {
                ThreadManager.parallel(this.taskFireMouseEnter, nodeListener);
            }
        }
    }

    /**
     * Signals to the listener that mouse exit off the node
     */
    protected void fireMouseExit()
    {
        synchronized (this.arrayListListners)
        {
            for (final NodeListener nodeListener : this.arrayListListners)
            {
                ThreadManager.parallel(this.taskFireMouseExit, nodeListener);
            }
        }
    }

    /**
     * Over write to complete specific parsing
     *
     * @param markupXML Markup to parse
     * @throws Exception If markup don't have any required informations
     */
    protected void readFromMarkup(final MarkupXML markupXML) throws Exception
    {
    }

    /**
     * Render specific, used by sub-classes
     */
    @ThreadOpenGL
    protected void renderSpecific()
    {
    }

    /**
     * Render specific for color picking, used by sub-classes
     */
    @ThreadOpenGL
    protected void renderSpecificPicking()
    {
    }

    /**
     * Render specific for UV picking.<br>
     * Override it to do the specific part
     */
    @ThreadOpenGL
    protected void renderSpecificPickingUV()
    {
    }

    /**
     * Over write to prepare the object just before the parse (Clear informations)
     */
    protected void startParseXML()
    {
    }

    /**
     * Over write to save specific information
     *
     * @param markupXML Markup to fill
     */
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
    }

    /**
     * Add a child
     *
     * @param child Child to add
     */
    public synchronized void addChild(final @NotNull Node child)
    {
        if (child == null)
        {
            throw new NullPointerException("The child couldn't be null");
        }

        if (child.parent != null)
        {
            child.parent.removeChild(child);
        }

        this.children.add(child);
        child.parent = this;
    }

    /**
     * Add listener to the node
     *
     * @param nodeListener Listener add
     */
    public void addNodeListener(final @NotNull NodeListener nodeListener)
    {
        if (nodeListener == null)
        {
            throw new NullPointerException("nodeListener mustn't be null");
        }

        synchronized (this.arrayListListners)
        {
            if (!this.arrayListListners.contains(nodeListener))
            {
                this.arrayListListners.add(nodeListener);
            }
        }
    }

    /**
     * Developer additional information
     *
     * @return Developer additional information
     */
    public @Nullable Object additionalInformation()
    {
        return this.additionalInformation;
    }

    /**
     * Defines/changes developer additional information
     *
     * @param additionalInformation New developer additional information
     */
    public void additionalInformation(final @Nullable Object additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }

    /**
     * Obtain all child node hierarchical with the given name
     *
     * @param nodeName Name search
     * @return List of matches nodes
     */
    public @NotNull List<Node> allNodes(final @Nullable String nodeName)
    {
        final List<Node>  allNodes = new ArrayList<Node>();
        final Stack<Node> stack    = new Stack<Node>();
        stack.add(this);
        Node node;

        while (!stack.isEmpty())
        {
            node = stack.pop();

            if ((node.nodeName == null) && (nodeName == null))
            {
                allNodes.add(node);
            }
            else if ((nodeName != null) && (node.nodeName != null) && nodeName.equals(node.nodeName))
            {
                allNodes.add(node);
            }

            for (final Node child : node.children)
            {
                stack.push(child);
            }
        }

        return allNodes;
    }

    /**
     * Angle X
     *
     * @return Angle X
     */
    public float angleX()
    {
        return this.angleX;
    }

    /**
     * Change angle X
     *
     * @param angleX New angle X
     */
    public void angleX(final float angleX)
    {
        this.angleX = angleX;
        this.checkValues();
    }

    /**
     * Angle Y
     *
     * @return Angle Y
     */
    public float angleY()
    {
        return this.angleY;
    }

    /**
     * Change angle Y
     *
     * @param angleY New angle Y
     */
    public void angleY(final float angleY)
    {
        this.angleY = angleY;
        this.checkValues();
    }

    /**
     * Angle on Z
     *
     * @return Angle on Z
     */
    public float angleZ()
    {
        return this.angleZ;
    }

    /**
     * Change angle Z
     *
     * @param angleZ New zngle Z
     */
    public void angleZ(final float angleZ)
    {
        this.angleZ = angleZ;
        this.checkValues();
    }

    /**
     * Apply same material to the all hierarchy
     *
     * @param material Material to apply
     */
    public void applyMaterialHierarchicaly(final @NotNull Material material)
    {
        if (this instanceof NodeWithMaterial)
        {
            ((NodeWithMaterial) this).material(material);
        }

        for (final Node node : this.children)
        {
            node.applyMaterialHierarchicaly(material);
        }
    }

    /**
     * Assign node listener to all hierarchy
     *
     * @param nodeListener Node listener to add
     */
    public void applyNodeListenerHierarchicaly(final @NotNull NodeListener nodeListener)
    {
        this.addNodeListener(nodeListener);

        for (final Node node : this.children)
        {
            node.applyNodeListenerHierarchicaly(nodeListener);
        }
    }

    /**
     * Indicates if the node can be pick
     *
     * @return {@code true} if the node can be pick
     */
    public final boolean canBePick()
    {
        return this.canBePick;
    }

    /**
     * Change the can be pick state
     *
     * @param canBePick New can be pick state
     */
    public final void canBePick(final boolean canBePick)
    {
        this.canBePick = canBePick;
    }

    /**
     * Node center
     *
     * @return Node center
     */
    public @NotNull Point3D center()
    {
        return new Point3D(this.x, this.y, this.z);
    }

    /**
     * Put the manipulation point to the center of the node
     */
    public final void centerGravityPoint()
    {
        final VirtualBox totalBox = this.computeTotalBox();
        final Point3D    center   = totalBox.center();
        this.translateGravityPoint(-center.x, -center.y, -center.z);
    }

    /**
     * Obtain a child
     *
     * @param index Child's index
     * @return The child
     */
    public @NotNull Node child(final int index)
    {
        return this.children.get(index);
    }

    /**
     * Number of children
     *
     * @return Number of children
     */
    public int childCount()
    {
        return this.children.size();
    }

    /**
     * Iterator on children
     *
     * @return Children iterator
     */
    public @NotNull Iterator<Node> children()
    {
        return this.children.iterator();
    }

    /**
     * Color picking ID
     *
     * @return Color picking ID
     */
    public final int colorPickingId()
    {
        return this.colorPickingId;
    }

    /**
     * Compute the complete box that contains the node and all its hierarchy and projected it in world space
     *
     * @return Total box projected in the world space
     */
    public @NotNull VirtualBox computeProjectedTotalBox()
    {
        final VirtualBox projected  = new VirtualBox();
        final VirtualBox virtualBox = this.computeTotalBox();

        if (virtualBox.empty())
        {
            return projected;
        }

        Point3D point = new Point3D(virtualBox.minX(), virtualBox.minY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.minY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.maxY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.maxY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.minY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.minY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.maxY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.maxY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        return projected;
    }

    /**
     * Compute the minimal box that contains the node and its children
     *
     * @return Computed box
     */
    public final VirtualBox computeTotalBox()
    {
        final VirtualBox virtualBox = new VirtualBox();

        if (this instanceof NodeWithBox)
        {
            virtualBox.add(((NodeWithBox) this).getBox(), this.x, this.y, this.z);
        }

        for (final Node child : this.children)
        {
            virtualBox.add(child.computeTotalBox());
        }

        return virtualBox;
    }

    /**
     * Search throw child hierarchical and return the first node with the given name
     *
     * @param nodeName Name search
     * @return Find node
     */
    public @Nullable Node firstNode(final @Nullable String nodeName)
    {
        final Stack<Node> stack = new Stack();
        stack.add(this);
        Node node;

        while (!stack.isEmpty())
        {
            node = stack.pop();
            if ((node.nodeName == null) && (nodeName == null))
            {
                return node;
            }
            else if ((nodeName != null) && (node.nodeName != null) && nodeName.equals(node.nodeName))
            {
                return node;
            }

            for (final Node child : node.children)
            {
                stack.push(child);
            }
        }

        return null;
    }

    /**
     * Free rotation on X
     */
    public void freeAngleX()
    {
        this.xAngleLimited = false;
    }

    /**
     * Free rotation on Y
     */
    public void freeAngleY()
    {
        this.yAngleLimited = false;
    }

    /**
     * Free rotation on Z
     */
    public void freeAngleZ()
    {
        this.zAngleLimited = false;
    }

    /**
     * Free X movement
     */
    public void freeX()
    {
        this.xLimited = false;
    }

    /**
     * Free Y movement
     */
    public void freeY()
    {
        this.yLimited = false;
    }

    /**
     * Free Z movement
     */
    public void freeZ()
    {
        this.zLimited = false;
    }

    /**
     * Indicates if a node is an ancestor of this node
     *
     * @param node Node tested
     * @return {@code true} if a node is an ancestor of this node
     */
    public boolean isAncestor(final @NotNull Node node)
    {
        if (this.isParent(node))
        {
            return true;
        }

        return (this.parent != null) && this.parent.isAncestor(node);

    }

    /**
     * Indicates if a node is a child to this node
     *
     * @param node Node tested
     * @return {@code true} if a node is a child to this node
     */
    public boolean isChild(final @NotNull Node node)
    {
        return this.children.contains(node);
    }

    /**
     * Indicates if a node is a decedent of this node
     *
     * @param node Node tested
     * @return {@code true} if a node is a decedent of this node
     */
    public boolean isDecedent(final @NotNull Node node)
    {
        if (this.isChild(node))
        {
            return true;
        }

        for (final Node child : this.children)
        {
            if (child.isDecedent(node))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates if the node is pick or not
     *
     * @param red   Red part of picking color
     * @param green Green part of picking color
     * @param blue  Blue part of picking color
     * @return {@code true} if the node is pick or not
     */
    public final boolean isMePick(final float red, final float green, final float blue)
    {
        if (!this.canBePick)
        {
            return false;
        }

        return Math3D.equalPick(red, this.redPicking) &&
               Math3D.equalPick(green, this.greenPicking) &&
               Math3D.equalPick(blue, this.bluePicking);
    }

    /**
     * Indicates if a node is the parent to this node
     *
     * @param node Node to test
     * @return {@code true} if a node is the parent to this node
     */
    public boolean isParent(final @NotNull Node node)
    {
        return (this.parent != null) && this.parent.equals(node);
    }

    /**
     * Limit rotation on X
     *
     * @param xAngleMin Angle minimum
     * @param xAngleMax Angle maximum
     */
    public void limitAngleX(float xAngleMin, float xAngleMax)
    {
        if (xAngleMin > xAngleMax)
        {
            final float f = xAngleMin;
            xAngleMin = xAngleMax;
            xAngleMax = f;
        }
        this.xAngleLimited = true;
        this.xAngleMin = xAngleMin;
        this.xAngleMax = xAngleMax;
    }

    /**
     * Limit rotation on Y
     *
     * @param yAngleMin Angle minimum
     * @param yAngleMax Angle maximum
     */
    public void limitAngleY(float yAngleMin, float yAngleMax)
    {
        if (yAngleMin > yAngleMax)
        {
            final float f = yAngleMin;
            yAngleMin = yAngleMax;
            yAngleMax = f;
        }
        this.yAngleLimited = true;
        this.yAngleMin = yAngleMin;
        this.yAngleMax = yAngleMax;
    }

    /**
     * Limit rotation on Z
     *
     * @param zAngleMin Angle minimum
     * @param zAngleMax Angle maximum
     */
    public void limitAngleZ(float zAngleMin, float zAngleMax)
    {
        if (zAngleMin > zAngleMax)
        {
            final float f = zAngleMin;
            zAngleMin = zAngleMax;
            zAngleMax = f;
        }
        this.zAngleLimited = true;
        this.zAngleMin = zAngleMin;
        this.zAngleMax = zAngleMax;
    }

    /**
     * Limit X movement
     *
     * @param xMin X minimum
     * @param xMax X maximum
     */
    public void limitX(float xMin, float xMax)
    {
        if (xMin > xMax)
        {
            final float f = xMin;
            xMin = xMax;
            xMax = f;
        }
        this.xLimited = true;
        this.xMin = xMin;
        this.xMax = xMax;
    }

    /**
     * Limit Y movement
     *
     * @param yMin Y minimum
     * @param yMax Y maximum
     */
    public void limitY(float yMin, float yMax)
    {
        if (yMin > yMax)
        {
            final float f = yMin;
            yMin = yMax;
            yMax = f;
        }
        this.yLimited = true;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Limit Z movement
     *
     * @param zMin Z minimum
     * @param zMax Z maximum
     */
    public void limitZ(float zMin, float zMax)
    {
        if (zMin > zMax)
        {
            final float f = zMin;
            zMin = zMax;
            zMax = f;
        }
        this.zLimited = true;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    /**
     * Load the node from XML markup
     *
     * @param markupXML XML to parse
     * @throws Exception If the markup not a valid node description
     */
    public final void loadFromXML(final @NotNull MarkupXML markupXML) throws Exception
    {
        this.children.clear();

        this.startParseXML();

        this.x = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_x, 0f);
        this.y = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_y, 0f);
        this.z = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_z, 0f);

        this.angleX = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_angleX, 0f);
        this.angleY = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_angleY, 0f);
        this.angleZ = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_angleZ, 0f);

        this.scaleX = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_scaleX, 1f);
        this.scaleY = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_scaleY, 1f);
        this.scaleZ = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_scaleZ, 1f);

        this.nodeName = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_nodeName, "");
        this.canBePick = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_canBePick, false);
        this.showWire = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_showWire, false);
        this.visible = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_visible, true);
        this.wireColor = Tool3D.getColor4fParameter(markupXML, ConstantsXML.MARKUP_NODE_wireColor);

        this.xLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xLimited, false);
        this.xMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xMax, 100000f);
        this.xMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xMin, -100000f);

        this.yLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yLimited, false);
        this.yMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yMax, 100000f);
        this.yMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yMin, -100000f);

        this.zLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zLimited, false);
        this.zMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zMax, 100000f);
        this.zMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zMin, -100000f);

        this.xAngleLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xAngleLimited, false);
        this.xAngleMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xAngleMax, 360f);
        this.xAngleMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_xAngleMin, 0f);

        this.yAngleLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yAngleLimited, false);
        this.yAngleMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yAngleMax, 360f);
        this.yAngleMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_yAngleMin, 0f);

        this.zAngleLimited = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zAngleLimited, false);
        this.zAngleMax = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zAngleMax, 360f);
        this.zAngleMin = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_zAngleMin, 0f);

        final String textureHotspot = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_textureHotspot, null);
        if (textureHotspot != null)
        {
            this.textureHotspot = Texture.obtainTexture(textureHotspot);
        }

        this.readFromMarkup(markupXML);

        for (final MarkupXML child : markupXML.obtainChildren(ConstantsXML.MARKUP_NODE))
        {
            this.addChild(Tool3D.createNode(child));
        }

        this.endParseXML();
    }

    /**
     * Return nodeType
     *
     * @return nodeType
     */
    public @NotNull NodeType nodeType()
    {
        return this.nodeType;
    }

    /**
     * Node's parent
     *
     * @return Node's parent
     */
    public @Nullable Node parent()
    {
        return this.parent;
    }

    /**
     * Looking for a child pick
     *
     * @param color Picking color
     * @return Node pick
     */
    public @Nullable Node pickingNode(final @NotNull Color4f color)
    {
        final float red   = color.red();
        final float green = color.green();
        final float blue  = color.blue();

        if (this.isMePick(red, green, blue))
        {
            return this;
        }

        Node              node       = this;
        final Stack<Node> stackNodes = new Stack<Node>();
        stackNodes.push(node);

        while (!stackNodes.isEmpty())
        {
            node = stackNodes.pop();
            if (node.isMePick(red, green, blue))
            {
                return node;
            }

            for (final Node child : node.children)
            {
                stackNodes.push(child);
            }
        }

        return null;
    }

    /**
     * Change node location
     *
     * @param x New x
     * @param y New y
     * @param z New z
     */
    public void position(final float x, final float y, final float z)
    {
        final float oldX = this.x;
        final float oldY = this.y;
        final float oldZ = this.z;
        this.x = x;
        this.y = y;
        this.z = z;
        this.checkValues();

        if (!Math3D.equal(this.x, oldX) || !Math3D.equal(this.y, oldY) || !Math3D.equal(this.z, oldZ))
        {
            this.fireNodePositionChanged();
        }
    }

    /**
     * Compute a point projection from node space to world space
     *
     * @param point Point to project
     * @return Projected point
     */
    public @NotNull Point3D projection(@NotNull Point3D point)
    {
        if (this.parent != null)
        {
            point = this.parent.projection(point);
        }

        point = point.add(this.x, this.y, this.z);
        Vec3f      vect = point.toVect3f();
        final Rotf rotX = new Rotf(new Vec3f(1, 0, 0), Math3D.degreeToRadian(this.angleX));
        vect = rotX.rotateVector(vect);
        final Rotf rotY = new Rotf(new Vec3f(0, 1, 0), Math3D.degreeToRadian(this.angleY));
        vect = rotY.rotateVector(vect);
        final Rotf rotZ = new Rotf(new Vec3f(0, 0, 1), Math3D.degreeToRadian(this.angleZ));
        vect = rotZ.rotateVector(vect);
        return new Point3D(vect);
    }

    /**
     * Project the point on using only rotations, not take care of translation
     *
     * @param point Point to project
     * @return Projected point
     */
    public @NotNull Point3D projectionRotateOnly(@NotNull Point3D point)
    {
        if (this.parent != null)
        {
            point = this.parent.projectionRotateOnly(point);
        }

        Vec3f      vect = point.toVect3f();
        final Rotf rotX = new Rotf(new Vec3f(1, 0, 0), Math3D.degreeToRadian(this.angleX));
        vect = rotX.rotateVector(vect);
        final Rotf rotY = new Rotf(new Vec3f(0, 1, 0), Math3D.degreeToRadian(this.angleY));
        vect = rotY.rotateVector(vect);
        final Rotf rotZ = new Rotf(new Vec3f(0, 0, 1), Math3D.degreeToRadian(this.angleZ));
        vect = rotZ.rotateVector(vect);
        return new Point3D(vect);
    }

    public void registerNodePositionListener(NodePositionListener nodePositionListener)
    {
        if (nodePositionListener == null)
        {
            return;
        }

        synchronized (this.nodePositionListeners)
        {
            if (!this.nodePositionListeners.contains(nodePositionListener))
            {
                this.nodePositionListeners.add(nodePositionListener);
            }
        }
    }

    /**
     * Remove all children
     */
    public void removeAllChildren()
    {
        for (final Node child : this.children)
        {
            child.parent = null;
        }

        this.children.clear();
    }

    /**
     * Remove a child
     *
     * @param child Child to remove
     */
    public void removeChild(final @NotNull Node child)
    {
        if (child == null)
        {
            throw new NullPointerException("The child couldn't be null");
        }

        this.children.remove(child);
        child.parent = null;
    }

    /**
     * Remove a listener to the node
     *
     * @param nodeListener Listener remove
     */
    public void removeNodeListener(final @NotNull NodeListener nodeListener)
    {
        synchronized (this.arrayListListners)
        {
            this.arrayListListners.remove(nodeListener);
        }
    }

    /**
     * Compute a point projection from world space to node space
     *
     * @param point Point to project
     * @return Projected point
     */
    public @NotNull Point3D reverseProjection(@NotNull Point3D point)
    {
        Vec3f      vect = point.toVect3f();
        final Rotf rotZ = new Rotf(new Vec3f(0, 0, 1), -Math3D.degreeToRadian(this.angleZ));
        vect = rotZ.rotateVector(vect);
        final Rotf rotY = new Rotf(new Vec3f(0, 1, 0), -Math3D.degreeToRadian(this.angleY));
        vect = rotY.rotateVector(vect);
        final Rotf rotX = new Rotf(new Vec3f(1, 0, 0), -Math3D.degreeToRadian(this.angleX));
        vect = rotX.rotateVector(vect);
        point = new Point3D(vect);
        point = point.add(-this.x, -this.y, -this.z);

        if (this.parent != null)
        {
            point = this.parent.reverseProjection(point);
        }

        return point;
    }

    /**
     * Unproject a point on using rotation only. Not take care of translation
     *
     * @param point Point to unproject
     * @return Unprojected point
     */
    public @NotNull Point3D reverseProjectionRotateOnly(@NotNull Point3D point)
    {
        Vec3f      vect = point.toVect3f();
        final Rotf rotZ = new Rotf(new Vec3f(0, 0, 1), -Math3D.degreeToRadian(this.angleZ));
        vect = rotZ.rotateVector(vect);
        final Rotf rotY = new Rotf(new Vec3f(0, 1, 0), -Math3D.degreeToRadian(this.angleY));
        vect = rotY.rotateVector(vect);
        final Rotf rotX = new Rotf(new Vec3f(1, 0, 0), -Math3D.degreeToRadian(this.angleX));
        vect = rotX.rotateVector(vect);
        point = new Point3D(vect);
        if (this.parent != null)
        {
            point = this.parent.reverseProjectionRotateOnly(point);
        }
        return point;
    }

    /**
     * Obtain root
     *
     * @return Root
     */
    public @NotNull Node root()
    {
        Node root = this;

        while (root.parent != null)
        {
            root = root.parent;
        }

        return root;
    }

    /**
     * Rotate on X
     *
     * @param angleX Angle to rotate
     */
    public void rotateAngleX(final float angleX)
    {
        this.angleX += angleX;
        this.checkValues();
    }

    /**
     * Rotate on Y
     *
     * @param angleY Angle to rotate
     */
    public void rotateAngleY(final float angleY)
    {
        this.angleY += angleY;
        this.checkValues();
    }

    /**
     * Rotate on Z
     *
     * @param angleZ Angle to rotate
     */
    public void rotateAngleZ(final float angleZ)
    {
        this.angleZ += angleZ;
        this.checkValues();
    }

    /**
     * Save the node to XML markup
     *
     * @return XML representation
     */
    public final @NotNull MarkupXML saveToXML()
    {
        final MarkupXML markupXML = new MarkupXML(ConstantsXML.MARKUP_NODE);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_type, this.nodeType.name());
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_angleX, this.angleX);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_angleY, this.angleY);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_angleZ, this.angleZ);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_canBePick, this.canBePick);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_nodeName, this.nodeName);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_scaleX, this.scaleX);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_scaleY, this.scaleY);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_scaleZ, this.scaleZ);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_showWire, this.showWire);
        if (this.textureHotspot != null)
        {
            markupXML.addParameter(ConstantsXML.MARKUP_NODE_textureHotspot, this.textureHotspot.textureName());
        }
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_visible, this.visible);
        Tool3D.addColor4fParameter(markupXML, ConstantsXML.MARKUP_NODE_wireColor, this.wireColor);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_x, this.x);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xAngleLimited, this.xAngleLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xAngleMax, this.xAngleMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xAngleMin, this.xAngleMin);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xLimited, this.xLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xMax, this.xMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_xMin, this.xMin);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_y, this.y);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yAngleLimited, this.yAngleLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yAngleMax, this.yAngleMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yAngleMin, this.yAngleMin);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yLimited, this.yLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yMax, this.yMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_yMin, this.yMin);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_z, this.z);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zAngleLimited, this.zAngleLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zAngleMax, this.zAngleMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zAngleMin, this.zAngleMin);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zLimited, this.zLimited);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zMax, this.zMax);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_zMin, this.zMin);

        this.writeInMarkup(markupXML);

        for (final Node child : this.children)
        {
            markupXML.addChild(child.saveToXML());
        }

        return markupXML;
    }

    /**
     * Scale the node.<br>
     * Use the same value for X, Y and Z
     *
     * @param scale Scale quantity
     */
    public void scale(final float scale)
    {
        this.scale(scale, scale, scale);
    }

    /**
     * Scale the node
     *
     * @param x Scale on x
     * @param y Scale on y
     * @param z Scale on z
     */
    public void scale(final float x, final float y, final float z)
    {
        this.scaleX *= x;
        this.scaleY *= y;
        this.scaleZ *= z;
    }

    /**
     * Scale on X
     *
     * @return Scale on X
     */
    public float scaleX()
    {
        return this.scaleX;
    }

    /**
     * Scale on Y
     *
     * @return Scale on Y
     */
    public float scaleY()
    {
        return this.scaleY;
    }

    /**
     * Scale on Z
     *
     * @return Scale on Z
     */
    public float scaleZ()
    {
        return this.scaleZ;
    }

    /**
     * Indicates if the node is selected
     *
     * @return {@code true} if the node is selected
     */
    public boolean selected()
    {
        return this.selected;
    }

    /**
     * Change the node select state
     *
     * @param selected New node select state
     */
    public void selected(final boolean selected)
    {
        this.selected = selected;
    }

    /**
     * Change scale value.<br>
     * Use the same value for X, Y and Z
     *
     * @param scale New scale value
     */
    public void setScale(final float scale)
    {
        this.setScale(scale, scale, scale);
    }

    /**
     * Change scale values
     *
     * @param x New scale x
     * @param y New scale y
     * @param z New scale Z
     */
    public void setScale(final float x, final float y, final float z)
    {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    /**
     * Indicates if the wire frame are showing
     *
     * @return {@code true} if the wire frame are showing
     */
    public boolean showWire()
    {
        return this.showWire;
    }

    /**
     * Change wire frame state
     *
     * @param showWire New wire frame state
     */
    public void showWire(final boolean showWire)
    {
        this.showWire = showWire;
    }

    /**
     * Texture linked for hotspot
     *
     * @return Texture linked for hotspot
     */
    public @Nullable Texture textureHotspot()
    {
        return this.textureHotspot;
    }

    /**
     * Change texture linked for hotspot.<br>
     * Use {@code null} to remove hotspot
     *
     * @param textureHotspot New texture linked for hotspot
     */
    public void textureHotspot(final @Nullable Texture textureHotspot)
    {
        this.textureHotspot = textureHotspot;
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
        return "Node [" + this.nodeName + "] : " + super.toString();
    }

    /**
     * Translate the node
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public void translate(final float x, final float y, final float z)
    {
        this.position(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Translate the manipulation point of the node
     *
     * @param vx Translation X
     * @param vy Translation Y
     * @param vz Translation Z
     */
    public final void translateGravityPoint(final float vx, final float vy, final float vz)
    {
        for (final Node child : this.children)
        {
            child.translate(vx, vy, vz);
        }
    }

    public void unregisterNodePositionListener(NodePositionListener nodePositionListener)
    {
        synchronized (this.nodePositionListeners)
        {
            this.nodePositionListeners.remove(nodePositionListener);
        }
    }

    /**
     * Indicates if the node is visible
     *
     * @return {@code true} if the node is visible
     */
    public boolean visible()
    {
        return this.visible;
    }

    /**
     * Change visible state
     *
     * @param visible New visible state
     */
    public void visible(final boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Change the visibility of the node and all of its children
     *
     * @param visible New visibility state
     */
    public void visibleHierarchy(final boolean visible)
    {
        this.visible = visible;

        for (final Node child : this.children)
        {
            child.visibleHierarchy(visible);
        }
    }

    /**
     * Wire frame color
     *
     * @return Wire frame color
     */
    public @NotNull Color4f wireColor()
    {
        return this.wireColor;
    }

    /**
     * Change wire frame color
     *
     * @param wireColor New wire frame color
     */
    public void wireColor(final @NotNull Color4f wireColor)
    {
        if (wireColor == null)
        {
            throw new NullPointerException("The wireColor couldn't be null");
        }

        this.wireColor = wireColor;
    }

    /**
     * X position
     *
     * @return X
     */
    public float x()
    {
        return this.x;
    }

    /**
     * Indicates if rotation on X is limited
     *
     * @return {@code true} if rotation on X is limited
     */
    public boolean xAngleLimited()
    {
        return this.xAngleLimited;
    }

    /**
     * Angle on X maximum
     *
     * @return Angle on X maximum
     */
    public float xAngleMax()
    {
        return this.xAngleMax;
    }

    /**
     * Angle on X minimum
     *
     * @return Angle on X minimum
     */
    public float xAngleMin()
    {
        return this.xAngleMin;
    }

    /**
     * Indicates if X is limited
     *
     * @return {@code true} if X is limited
     */
    public boolean xLimited()
    {
        return this.xLimited;
    }

    /**
     * X maximum
     *
     * @return X maximum
     */
    public float xMax()
    {
        return this.xMax;
    }

    /**
     * X minimum
     *
     * @return X minimum
     */
    public float xMin()
    {
        return this.xMin;
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
     * Indicates if rotation on Y is limited
     *
     * @return {@code true} if rotation on Y is limited
     */
    public boolean yAngleLimited()
    {
        return this.yAngleLimited;
    }

    /**
     * Angle on Y maximum
     *
     * @return Angle on Y maximum
     */
    public float yAngleMax()
    {
        return this.yAngleMax;
    }

    /**
     * Angle on Y minimum
     *
     * @return Angle on Y minimum
     */
    public float yAngleMin()
    {
        return this.yAngleMin;
    }

    /**
     * Indicates if Y is limited
     *
     * @return {@code true} if Y is limited
     */
    public boolean yLimited()
    {
        return this.yLimited;
    }

    /**
     * Y maximum
     *
     * @return Y maximum
     */
    public float yMax()
    {
        return this.yMax;
    }

    /**
     * Y minimum
     *
     * @return Y minimum
     */
    public float yMin()
    {
        return this.yMin;
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

    /**
     * Indicates if rotation on Z is limited
     *
     * @return {@code true} if rotation on Z is limited
     */
    public boolean zAngleLimited()
    {
        return this.zAngleLimited;
    }

    /**
     * Angle on Z maximum
     *
     * @return Angle on Z maximum
     */
    public float zAngleMax()
    {
        return this.zAngleMax;
    }

    /**
     * Angle on Z minimum
     *
     * @return Angle on Z minimum
     */
    public float zAngleMin()
    {
        return this.zAngleMin;
    }

    /**
     * Indicates if Z is limited
     *
     * @return {@code true} if Z is limited
     */
    public boolean zLimited()
    {
        return this.zLimited;
    }

    /**
     * Z maximum
     *
     * @return Z maximum
     */
    public float zMax()
    {
        return this.zMax;
    }

    /**
     * Z minimum
     *
     * @return Z minimum
     */
    public float zMin()
    {
        return this.zMin;
    }
}