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
package jhelp.engine2.geometry;

import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.Vertex;
import jhelp.xml.MarkupXML;

/**
 * Box, cube
 *
 * @author JHelp
 */
public class Box
        extends Object3D
{
    /**
     * Constructs Box
     */
    public Box()
    {
        this.nodeType = NodeType.BOX;

        this.add(new Vertex(-0.5f, 0.5f, 0.5f,//
                            0f, 0f,//
                            0f, 0f, -1f));
        this.add(new Vertex(0.5f, 0.5f, 0.5f,//
                            1f, 0f,//
                            0f, 0f, -1f));
        this.add(new Vertex(0.5f, -0.5f, 0.5f,//
                            1f, 1f,//
                            0f, 0f, -1f));
        this.add(new Vertex(-0.5f, -0.5f, 0.5f,//
                            0f, 1f,//
                            0f, 0f, -1f));
        //
        this.nextFace();
        this.add(new Vertex(-0.5f, 0.5f, -0.5f,//
                            1f, 1f,//
                            0f, -1f, 0f));
        this.add(new Vertex(0.5f, 0.5f, -0.5f,//
                            0f, 1f,//
                            0f, -1f, 0f));
        this.add(new Vertex(0.5f, 0.5f, 0.5f,//
                            0f, 0f,//
                            0f, -1f, 0f));
        this.add(new Vertex(-0.5f, 0.5f, 0.5f,//
                            1f, 0f,//
                            0f, -1f, 0f));
        //
        this.nextFace();
        this.add(new Vertex(0.5f, -0.5f, 0.5f,//
                            0f, 1f,//
                            -1f, 0f, 0f));
        this.add(new Vertex(0.5f, 0.5f, 0.5f,//
                            0f, 0f,//
                            -1f, 0f, 0f));
        this.add(new Vertex(0.5f, 0.5f, -0.5f,//
                            1f, 0f,//
                            -1f, 0f, 0f));
        this.add(new Vertex(0.5f, -0.5f, -0.5f,//
                            1f, 1f,//
                            -1f, 0f, 0f));
        //
        this.nextFace();
        this.add(new Vertex(-0.5f, -0.5f, -0.5f,//
                            1f, 1f,//
                            0f, 0f, 1f));
        this.add(new Vertex(0.5f, -0.5f, -0.5f,//
                            0f, 1f,//
                            0f, 0f, 1f));
        this.add(new Vertex(0.5f, 0.5f, -0.5f,//
                            0f, 0f,//
                            0f, 0f, 1f));
        this.add(new Vertex(-0.5f, 0.5f, -0.5f,//
                            1f, 0f,//
                            0f, 0f, 1f));
        //
        this.nextFace();
        this.add(new Vertex(-0.5f, -0.5f, 0.5f,//
                            0f, 0f,//
                            0f, 1f, 0f));
        this.add(new Vertex(0.5f, -0.5f, 0.5f,//
                            1f, 0f,//
                            0f, 1f, 0f));
        this.add(new Vertex(0.5f, -0.5f, -0.5f,//
                            1f, 1f,//
                            0f, 1f, 0f));
        this.add(new Vertex(-0.5f, -0.5f, -0.5f,//
                            0f, 1f,//
                            0f, 1f, 0f));
        //
        this.nextFace();
        this.add(new Vertex(-0.5f, -0.5f, -0.5f,//
                            0f, 1f,//
                            1f, 0f, 0f));
        this.add(new Vertex(-0.5f, 0.5f, -0.5f,//
                            0f, 0f,//
                            1f, 0f, 0f));
        this.add(new Vertex(-0.5f, 0.5f, 0.5f,//
                            1f, 0f,//
                            1f, 0f, 0f));
        this.add(new Vertex(-0.5f, -0.5f, 0.5f,//
                            1f, 1f,//
                            1f, 0f, 0f));
    }

    /**
     * Called when parse done
     *
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
    }

    /**
     * Read from XML
     *
     * @param markupXML Markup to parse
     * @see Node#readFromMarkup
     */
    @Override
    protected void readFromMarkup(final MarkupXML markupXML)
    {
        this.readMaterialFromMarkup(markupXML);
    }

    /**
     * Write on XML
     *
     * @param markupXML Markup to fill
     * @see Node#writeInMarkup
     */
    @Override
    protected void writeInMarkup(final MarkupXML markupXML)
    {
        this.writeMaterialInMarkup(markupXML);
    }
}