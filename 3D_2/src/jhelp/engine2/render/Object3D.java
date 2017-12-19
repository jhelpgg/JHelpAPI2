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
import jhelp.engine2.io.ConstantsXML;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;
import org.lwjgl.opengl.GL11;

/**
 * 3D object
 *
 * @author JHelp
 */
public class Object3D
        extends NodeWithMaterial
{
    /**
     * Lock for synchronization
     */
    private static final Object LOCK = new Object();
    /**
     * Bounding box
     */
    private VirtualBox box;
    /**
     * Isobarycenter of object
     */
    private Point3D    center;
    /**
     * List ID for OpenGL
     */
    private int        idList;
    /**
     * Material apply to the object
     */
    private Material   material;
    /**
     * Material used on selection
     */
    private Material   materialForSelection;
    /**
     * Indicates if the list must be reconstruct
     */
    private boolean    needReconstructTheList;
    /**
     * Object's mesh
     */
    public  Mesh       mesh;

    /**
     * Empty object
     */
    public Object3D()
    {
        this.nodeType = NodeType.OBJECT3D;
        this.canBePick(true);
        this.material = Material.DEFAULT_MATERIAL;
        this.idList = -1;
        this.reconstructTheList();
        this.mesh = new Mesh();
    }

    /**
     * Draw object
     */
    @ThreadOpenGL
    protected void drawObject()
    {
        // If no list is create or actual list needs to be update
        if ((this.idList < 0) || this.needReconstructTheList)
        {
            this.needReconstructTheList = false;

            // Delete old list
            if (this.idList >= 0)
            {
                GL11.glDeleteLists(this.idList, 1);
            }
            // Create list
            this.idList = GL11.glGenLists(1);
            GL11.glNewList(this.idList, GL11.GL_COMPILE);
            try
            {
                this.mesh.render();
            }
            catch (final Exception | Error e)
            {
                this.needReconstructTheList = true;
            }
            GL11.glEndList();
        }
        // Draw the list
        GL11.glCallList(this.idList);
    }

    /**
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
        this.reconstructTheList();
    }

    /**
     * Extract Object parameters from XML
     *
     * @param markupXML Markup to parse
     * @throws Exception On parsing problem
     * @see Node#readFromMarkup
     */
    @Override
    protected void readFromMarkup(final @NotNull MarkupXML markupXML) throws Exception
    {
        this.readMaterialFromMarkup(markupXML);

        final EnumerationIterator<MarkupXML> enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_MESH);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_MESH, " in ",
                                         markupXML.getName()));
        }

        final MarkupXML mesh = enumerationIterator.nextElement();
        this.mesh.loadFromXML(mesh);
    }

    /**
     * Render the object
     *
     * @see Node#renderSpecific()
     */
    @Override
    @ThreadOpenGL
    protected void renderSpecific()
    {
        if (this.selected() && (this.materialForSelection != null))
        {
            final boolean twoSided = this.materialForSelection.twoSided();

            switch (this.twoSidedState())
            {
                case AS_MATERIAL:
                    break;
                case FORCE_ONE_SIDE:
                    this.materialForSelection.twoSided(false);
                    break;
                case FORCE_TWO_SIDE:
                    this.materialForSelection.twoSided(true);
                    break;
            }

            this.materialForSelection.renderMaterial(this);
            this.materialForSelection.twoSided(twoSided);
        }
        else
        {
            final boolean twoSided = this.material.twoSided();

            switch (this.twoSidedState())
            {
                case AS_MATERIAL:
                    break;
                case FORCE_ONE_SIDE:
                    this.material.twoSided(false);
                    break;
                case FORCE_TWO_SIDE:
                    this.material.twoSided(true);
                    break;
            }

            this.material.renderMaterial(this);
            this.material.twoSided(twoSided);
        }
    }

    /**
     * Render object in picking mode
     *
     * @see Node#renderSpecificPicking()
     */
    @Override
    @ThreadOpenGL
    protected void renderSpecificPicking()
    {
        this.drawObject();
    }

    /**
     * Render specific for picking UV
     *
     * @see Node#renderSpecificPickingUV()
     */
    @Override
    @ThreadOpenGL
    protected void renderSpecificPickingUV()
    {
        final boolean showWire = this.showWire();
        this.showWire(false);
        Material.obtainMaterialForPickUV().renderMaterial(this);
        this.showWire(showWire);
    }

    /**
     * @see Node#startParseXML()
     */
    @Override
    protected final void startParseXML()
    {
        synchronized (Object3D.LOCK)
        {
            this.box = null;
            this.center = null;
        }
    }

    /**
     * Write object in XML
     *
     * @param markupXML XML to fill
     * @see Node#writeInMarkup
     */
    @Override
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
        this.writeMaterialInMarkup(markupXML);
        markupXML.addChild(this.mesh.saveToXML());
    }

    /**
     * Center of object
     *
     * @return Object's center
     * @see Node#center()
     */
    @Override
    public @NotNull Point3D center()
    {
        synchronized (Object3D.LOCK)
        {
            if (this.center == null)
            {
                if (this.box == null)
                {
                    this.box = this.mesh.computeBox();
                }
                this.center = this.box.center();
            }
            return this.center;
        }
    }

    /**
     * Extract Materials in XML
     *
     * @param markupXML Markup to parse
     */
    protected void readMaterialFromMarkup(final @NotNull MarkupXML markupXML)
    {
        String material = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_material, "");
        if (material.length() < 1)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory parameter ", ConstantsXML.MARKUP_NODE_material, " in ",
                                         markupXML.getName()));
        }
        this.material = Material.obtainMaterial(material);
        material = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_materialSelection, "");
        if (material.length() > 0)
        {
            this.materialForSelection = Material.obtainMaterial(material);
        }

        material = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_twoSided, TwoSidedState.AS_MATERIAL.name());
        this.twoSidedState(TwoSidedState.valueOf(material));

        material = null;
    }

    /**
     * Write materials in XML
     *
     * @param markupXML Markup to fill
     */
    protected void writeMaterialInMarkup(final @NotNull MarkupXML markupXML)
    {
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_material, this.material.name());
        if (this.materialForSelection != null)
        {
            markupXML.addParameter(ConstantsXML.MARKUP_NODE_materialSelection, this.materialForSelection.name());
        }

        markupXML.addParameter(ConstantsXML.MARKUP_NODE_twoSided, this.twoSidedState().name());
    }

    /**
     * Add vertex to actual object face.<br>
     * the result is see as soon as possible
     *
     * @param vertex Vertex to add
     */
    public void add(final @NotNull Vertex vertex)
    {
        this.addFast(vertex);
        this.reconstructTheList();
    }

    /**
     * Add vertex to the actual face of the object.<br>
     * It is call fast because the vertex is only add, but list is not reconstructs, you have to call
     * <code>reconstructTheList</code> method to see the result<br>
     * It is use when you want add several vertex and see result at the end
     *
     * @param vertex Vertex to add
     */
    public void addFast(final @NotNull Vertex vertex)
    {
        if (vertex == null)
        {
            throw new NullPointerException("The vertex couldn't be null");
        }
        this.mesh.addVertexToTheActualFace(vertex);
    }

    /**
     * Generate UV on using the better plane for each face.
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public void computeUVfromMax(final float multU, final float multV)
    {
        this.mesh.computeUVfromMax(multU, multV);
        this.reconstructTheList();
    }

    /**
     * Generate UV on using (X, Y) plane.<br>
     * X values are considered like U, Y like V, and we normalize to have good values
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public void computeUVfromPlaneXY(final float multU, final float multV)
    {
        this.mesh.computeUVfromPlaneXY(multU, multV);
        this.reconstructTheList();
    }

    /**
     * Generate UV on using (X, Z) plane.<br>
     * X values are considered like U, Z like V, and we normalize to have good values
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public void computeUVfromPlaneXZ(final float multU, final float multV)
    {
        this.mesh.computeUVfromPlaneXZ(multU, multV);
        this.reconstructTheList();
    }

    /**
     * Generate UV on using (Y, Z) plane.<br>
     * Y values are considered like U, Z like V, and we normalize to have good values
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public void computeUVfromPlaneYZ(final float multU, final float multV)
    {
        this.mesh.computeUVfromPlaneYZ(multU, multV);
        this.reconstructTheList();
    }

    /**
     * Generate UV in spherical way.<br>
     * Imagine you have a mapped sphere around your object, then project it to him
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public void computeUVspherical(final float multU, final float multV)
    {
        this.mesh.computeUVspherical(multU, multV);
        this.reconstructTheList();
    }

    /**
     * Update last changes
     */
    public void flush()
    {
        this.reconstructTheList();
        this.mesh.recomputeTheBox();
    }

    /**
     * Compute bounding box
     *
     * @return Bounding box
     */
    @Override
    public @NotNull VirtualBox getBox()
    {
        synchronized (Object3D.LOCK)
        {
            if (this.box == null)
            {
                this.box = this.mesh.computeBox();
            }
            return this.box;
        }
    }

    /**
     * Object's material
     *
     * @return Object's material
     */
    @Override
    public @NotNull Material material()
    {
        return this.material;
    }

    /**
     * Change object's material
     *
     * @param material New material
     */
    @Override
    public void material(final @NotNull Material material)
    {
        if (material == null)
        {
            throw new NullPointerException("The material couldn't be null");
        }
        this.material = material;
    }

    /**
     * Object's selection material (Can be {@code null}
     *
     * @return Object's selection material
     */
    @Override
    public @Nullable Material materialForSelection()
    {
        return this.materialForSelection;
    }

    /**
     * Change object's selection material.<br>
     * Use {@code null} if you don't have a selection state
     *
     * @param materialForSelection New object's selection material
     */
    @Override
    public void materialForSelection(final @Nullable Material materialForSelection)
    {
        this.materialForSelection = materialForSelection;
    }

    /**
     * Translate a vertex in the mesh.<br>
     * This translation can translate neighbor vertex, the translation apply to them depends of the solidity.<br>
     * If you specify a 0 solidity, then neighbor don't move, 1, all vertex translate in the same translate, some where between,
     * the object morph
     *
     * @param indexPoint Vertex index to translate
     * @param vx         X
     * @param vy         Y
     * @param vz         Z
     * @param solidity   Solidity
     */
    public void movePoint(final int indexPoint, final float vx, final float vy, final float vz, final float solidity)
    {
        this.mesh.movePoint(indexPoint, vx, vy, vz, solidity);
        this.reconstructTheList();
    }

    /**
     * Translate some vertex in the mesh.<br>
     * This translation can translate neighbor vertex, the translation apply to them depends of the solidity.<br>
     * If you specify a 0 solidity, then neighbor don't move, 1, all vertex translate in the same translate, some where between,
     * the object morph<br>
     * You specify a near deep to determine the level of points are translate the same way as the specified index
     *
     * @param indexPoint Vertex index to translate
     * @param vx         X
     * @param vy         Y
     * @param vz         Z
     * @param solidity   Solidity
     * @param near       Level of neighbor move with specified point. 0 the point, 1 : one level neighbor, ...
     */
    public void movePoint(
            final int indexPoint, final float vx, final float vy, final float vz, final float solidity, final int near)
    {
        this.mesh.movePoint(indexPoint, vx, vy, vz, solidity, near);
        this.reconstructTheList();
    }

    /**
     * Create a new face for the object, and this new face become the actual one
     */
    public void nextFace()
    {
        this.mesh.endFace();
    }

    /**
     * Make the center of object vertexes be also the center of the object
     */
    public void recenterObject()
    {
        this.mesh.centerMesh();
        this.flush();
    }

    /**
     * Force update the last changes on the mesh
     */
    public void reconstructTheList()
    {
        synchronized (Object3D.LOCK)
        {
            this.box = null;
            this.center = null;
            this.needReconstructTheList = true;
        }
    }

    /**
     * Remove all children and make the object empty
     */
    public void reset()
    {
        this.removeAllChildren();
        this.mesh.reset();
        this.flush();
    }
}