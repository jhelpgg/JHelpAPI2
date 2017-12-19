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
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;

/**
 * A clone is an object who used the same mesh than other.<br>
 * The aim is to economize video memory if we use same mesh several times.<br>
 * But if the original object change its mesh, then this change also
 *
 * @author JHelp
 */
public class ObjectClone
        extends NodeWithMaterial
{
    /**
     * Material used
     */
    private Material material;
    /**
     * Selection material
     */
    private Material materialForSelection;
    /**
     * Reference object
     */
    private Object3D reference;
    /**
     * Object reference name
     */
    private String   referenceName;

    /**
     * Constructs ObjectClone
     *
     * @param reference Reference object
     */
    public ObjectClone(final @Nullable Object3D reference)
    {
        this.nodeType = NodeType.CLONE;

        this.canBePick(true);
        this.reference = reference;
        this.material = Material.DEFAULT_MATERIAL;
        if (reference != null)
        {
            this.referenceName = reference.nodeName;
        }
        else
        {
            this.referenceName = null;
        }
    }

    /**
     * Constructs ObjectClone
     *
     * @param reference Reference object name
     */
    public ObjectClone(final @NotNull String reference)
    {
        if (reference == null)
        {
            throw new NullPointerException("reference mustn't be null");
        }

        this.nodeType = NodeType.CLONE;

        this.canBePick(true);
        this.material = Material.DEFAULT_MATERIAL;
        this.referenceName = reference;
    }

    /**
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
        this.reference = null;
    }

    /**
     * Read clone parameters form XML
     *
     * @param markupXML XML to parse
     * @see Node#readFromMarkup
     */
    @Override
    protected void readFromMarkup(final @NotNull MarkupXML markupXML)
    {
        String material = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_material, "");
        if (material.length() < 1)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mandatory parameter ", ConstantsXML.MARKUP_NODE_material, " in ",
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
        this.referenceName = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_reference, "");
        if (this.referenceName.length() < 1)
        {
            this.referenceName = null;
        }
    }

    /**
     * Render this object
     *
     * @see Node#renderSpecific()
     */
    @Override
    @ThreadOpenGL
    protected synchronized void renderSpecific()
    {
        if ((this.reference == null) && (this.referenceName != null))
        {
            this.reference = (Object3D) this.root().firstNode(this.referenceName);
        }

        if (this.reference != null)
        {
            final boolean showWire  = this.reference.showWire();
            final Color4f wireColor = this.reference.wireColor();

            this.reference.showWire(this.showWire());
            this.reference.wireColor(this.wireColor());

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

                this.materialForSelection.renderMaterial(this.reference);
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

                this.material.renderMaterial(this.reference);
                this.material.twoSided(twoSided);
            }

            this.reference.showWire(showWire);
            this.reference.wireColor(wireColor);
        }
    }

    /**
     * Render in picking mode
     *
     * @see Node#renderSpecificPicking()
     */
    @Override
    @ThreadOpenGL
    protected synchronized void renderSpecificPicking()
    {
        if ((this.reference == null) && (this.referenceName != null))
        {
            this.reference = (Object3D) this.root().firstNode(this.referenceName);
        }

        if (this.reference != null)
        {
            this.reference.drawObject();
        }
    }

    /**
     * Render for pick UV specific for clone
     *
     * @see Node#renderSpecificPickingUV()
     */
    @Override
    @ThreadOpenGL
    protected void renderSpecificPickingUV()
    {
        if ((this.reference == null) && (this.referenceName != null))
        {
            this.reference = (Object3D) this.root().firstNode(this.referenceName);
        }

        if (this.reference != null)
        {
            final boolean showWire = this.reference.showWire();
            this.reference.showWire(false);
            Material.obtainMaterialForPickUV().renderMaterial(this.reference);
            this.reference.showWire(showWire);
        }
    }

    /**
     * Start parsing XML
     *
     * @see Node#startParseXML()
     */
    @Override
    protected void startParseXML()
    {
    }

    /**
     * Write clone in XML
     *
     * @param markupXML XML to fill
     * @see Node#writeInMarkup
     */
    @Override
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_material, this.material.name());
        if (this.materialForSelection != null)
        {
            markupXML.addParameter(ConstantsXML.MARKUP_NODE_materialSelection, this.materialForSelection.name());
        }

        markupXML.addParameter(ConstantsXML.MARKUP_NODE_reference, this.referenceName);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_twoSided, this.twoSidedState().name());
    }

    /**
     * Object center
     *
     * @return Object center
     * @see Node#center()
     */
    @Override
    public @NotNull Point3D center()
    {
        if (this.reference == null)
        {
            return new Point3D();
        }

        return this.reference.center();
    }

    /**
     * Bonding box
     *
     * @return Bonding box
     * @see NodeWithMaterial#getBox()
     */
    @Override
    public VirtualBox getBox()
    {
        if (this.reference == null)
        {
            return new VirtualBox();
        }

        return this.reference.getBox();
    }

    /**
     * Material
     *
     * @return Material
     */
    @Override
    public @NotNull Material material()
    {
        return this.material;
    }

    /**
     * Change material
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
     * Selection material
     *
     * @return Selection material
     */
    @Override
    public @Nullable Material materialForSelection()
    {
        return this.materialForSelection;
    }

    /**
     * Change selection material.<br>
     * Used {@code null} for disable selection distinction
     *
     * @param materialForSelection Selection material
     */
    @Override
    public void materialForSelection(final @Nullable Material materialForSelection)
    {
        this.materialForSelection = materialForSelection;
    }

    /**
     * Reference object
     *
     * @return Reference object
     */
    public @Nullable Object3D reference()
    {
        return this.reference;
    }

    /**
     * Change reference object
     *
     * @param reference New reference
     */
    public synchronized void reference(final @Nullable Object3D reference)
    {
        this.reference = reference;

        if (reference != null)
        {
            this.referenceName = reference.nodeName;
        }
        else
        {
            this.referenceName = null;
        }
    }
}