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

import com.sun.istack.internal.NotNull;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.twoD.Path;
import jhelp.engine2.util.Tool3D;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;

/**
 * Path geom is build with a path for U, and path for V.<br>
 * V path was repeat several times throw U path
 *
 * @author JHelp
 */
public class PathGeom
        extends Object3D
{
    /**
     * Indicates if path is joined
     */
    private      boolean joined;
    /**
     * Indicates if path is linearized
     */
    private      boolean linearized;
    /**
     * U multiplier
     */
    private      float   multiplierU;
    /**
     * precision for U path
     */
    private      int     precisionU;
    /**
     * precision for V path
     */
    private      int     precisionV;
    /**
     * U path
     */
    public final Path    pathU;
    /**
     * V path
     */
    public final Path    pathV;

    /**
     * Constructs default PathGeom
     */
    public PathGeom()
    {
        this(5, 5);
    }

    /**
     * Constructs PathGeom
     *
     * @param precisionU U path precision
     * @param precisionV V path precision
     */
    public PathGeom(final int precisionU, final int precisionV)
    {
        this.nodeType = NodeType.PATH_GEOM;

        this.pathU = new Path();
        this.pathV = new Path();
        this.precisionU(precisionU);
        this.precisionV(precisionV);

        this.joined = true;
        this.multiplierU = 1;
        this.linearized = false;
    }

    /**
     * Called when parsing finished
     *
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
        if (this.joined)
        {
            this.refreshJoinedPath(this.multiplierU, this.linearized);
            return;
        }
        this.refreshPath();
    }

    /**
     * Read parameters for XML
     *
     * @param markupXML Markup to parse
     * @throws Exception On reading/parsing issue
     * @see Node#readFromMarkup(MarkupXML)
     */
    @Override
    protected void readFromMarkup(final @NotNull MarkupXML markupXML) throws Exception
    {
        this.readMaterialFromMarkup(markupXML);
        this.precisionU = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_precisionU, 5);
        this.precisionV = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_precisionV, 5);
        this.multiplierU = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_multU, 1f);
        this.joined = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_joined, true);
        this.linearized = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_linearize, false);

        EnumerationIterator<MarkupXML> enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_PATH_U);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_PATH_U, " in ",
                                         markupXML.getName()));
        }

        MarkupXML path = enumerationIterator.nextElement();

        enumerationIterator = path.obtainChildren(ConstantsXML.MARKUP_PATH);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mandatory child ", ConstantsXML.MARKUP_PATH, " in ", path.getName()));
        }

        path = enumerationIterator.nextElement();
        this.pathU.loadFromXML(path);

        //

        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_PATH_V);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mandatory child ", ConstantsXML.MARKUP_PATH_V, " in ",
                                         markupXML.getName()));
        }

        path = enumerationIterator.nextElement();

        enumerationIterator = path.obtainChildren(ConstantsXML.MARKUP_PATH);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mandatory child ", ConstantsXML.MARKUP_PATH, " in ", path.getName()));
        }

        path = enumerationIterator.nextElement();
        this.pathV.loadFromXML(path);

        path = null;
    }

    /**
     * Write on XML
     *
     * @param markupXML Markup to fill
     * @see Node#writeInMarkup(MarkupXML)
     */
    @Override
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
        this.writeMaterialInMarkup(markupXML);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_precisionU, this.precisionU);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_precisionV, this.precisionV);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_multU, this.multiplierU);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_joined, this.joined);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_linearize, this.linearized);

        MarkupXML path = new MarkupXML(ConstantsXML.MARKUP_PATH_U);
        path.addChild(this.pathU.saveToXML());
        markupXML.addChild(path);

        path = new MarkupXML(ConstantsXML.MARKUP_PATH_V);
        path.addChild(this.pathV.saveToXML());
        markupXML.addChild(path);

        path = null;
    }

    /**
     * Append cubic to U path
     *
     * @param startPoint    Start point
     * @param start         Start value
     * @param controlPoint1 First control point
     * @param control1      First control value
     * @param controlPoint2 Second control point
     * @param control2      Second control value
     * @param endPoint      End point
     * @param end           End value
     */
    public void appendCubicU(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint1, final float control1,
            final @NotNull Point2D controlPoint2, final float control2,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathU.appendCubic(startPoint, start, controlPoint1, control1, controlPoint2, control2, endPoint, end);
    }

    /**
     * Append cubic to U path
     *
     * @param startPoint    Start point
     * @param controlPoint1 First control point
     * @param controlPoint2 Second control point
     * @param endPoint      End point
     */
    public void appendCubicU(
            final @NotNull Point2D startPoint,
            final @NotNull Point2D controlPoint1,
            final @NotNull Point2D controlPoint2,
            final @NotNull Point2D endPoint)
    {
        this.pathU.appendCubic(startPoint, controlPoint1, controlPoint2, endPoint);
    }

    /**
     * Append cubic to V path
     *
     * @param startPoint    Start point
     * @param start         Start value
     * @param controlPoint1 First control point
     * @param control1      First control value
     * @param controlPoint2 Second control point
     * @param control2      Second control value
     * @param endPoint      End point
     * @param end           End value
     */
    public void appendCubicV(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint1, final float control1,
            final @NotNull Point2D controlPoint2, final float control2,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathV.appendCubic(startPoint, start, controlPoint1, control1, controlPoint2, control2, endPoint, end);
    }

    /**
     * Append cubic to V path
     *
     * @param startPoint    Start point
     * @param controlPoint1 First control point
     * @param controlPoint2 Second control point
     * @param endPoint      End point
     */
    public void appendCubicV(
            final @NotNull Point2D startPoint,
            final @NotNull Point2D controlPoint1,
            final @NotNull Point2D controlPoint2,
            final @NotNull Point2D endPoint)
    {
        this.pathV.appendCubic(startPoint, controlPoint1, controlPoint2, endPoint);
    }

    /**
     * Append line on U path
     *
     * @param startPoint Start point
     * @param start      Start value
     * @param endPoint   End point
     * @param end        End value
     */
    public void appendLineU(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathU.appendLine(startPoint, start, endPoint, end);
    }

    /**
     * Append line on U path
     *
     * @param startPoint Start point
     * @param endPoint   End point
     */
    public void appendLineU(final @NotNull Point2D startPoint, final @NotNull Point2D endPoint)
    {
        this.pathU.appendLine(startPoint, endPoint);
    }

    /**
     * Append line on V path
     *
     * @param startPoint Start point
     * @param start      Start value
     * @param endPoint   End point
     * @param end        End value
     */
    public void appendLineV(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathV.appendLine(startPoint, start, endPoint, end);
    }

    /**
     * Append line on V path
     *
     * @param startPoint Start point
     * @param endPoint   End point
     */
    public void appendLineV(final @NotNull Point2D startPoint, final @NotNull Point2D endPoint)
    {
        this.pathV.appendLine(startPoint, endPoint);
    }

    /**
     * Append quadratic to U path
     *
     * @param startPoint   Start point
     * @param start        Start value
     * @param controlPoint Control point
     * @param control      Control value
     * @param endPoint     End point
     * @param end          End value
     */
    public void appendQuadU(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint, final float control,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathU.appendQuad(startPoint, start, controlPoint, control, endPoint, end);
    }

    /**
     * Append quadratic to U path
     *
     * @param startPoint   Start point
     * @param controlPoint Control point
     * @param endPoint     End point
     */
    public void appendQuadU(
            final @NotNull Point2D startPoint, final @NotNull Point2D controlPoint, final @NotNull Point2D endPoint)
    {
        this.pathU.appendQuad(startPoint, controlPoint, endPoint);
    }

    /**
     * Append quadratic to V path
     *
     * @param startPoint   Start point
     * @param start        Start value
     * @param controlPoint Control point
     * @param control      Control value
     * @param endPoint     End point
     * @param end          End value
     */
    public void appendQuadV(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint, final float control,
            final @NotNull Point2D endPoint, final float end)
    {
        this.pathV.appendQuad(startPoint, start, controlPoint, control, endPoint, end);
    }

    /**
     * Append quadratic to V path
     *
     * @param startPoint   Start point
     * @param controlPoint Control point
     * @param endPoint     End point
     */
    public void appendQuadV(
            final @NotNull Point2D startPoint, final @NotNull Point2D controlPoint, final @NotNull Point2D endPoint)
    {
        this.pathV.appendQuad(startPoint, controlPoint, endPoint);
    }

    /**
     * Try to linearized paths
     *
     * @param startU Start U
     * @param endU   End U
     * @param startV Start V
     * @param endV   End V
     */
    public void linearize(final float startU, final float endU, final float startV, final float endV)
    {
        this.pathU.linearize(startU, endU);
        this.pathV.linearize(startV, endV);
    }

    /**
     * Return precisionU
     *
     * @return precisionU
     */
    public int precisionU()
    {
        return this.precisionU;
    }

    /**
     * Modify precisionU
     *
     * @param precisionU New precisionU value
     */
    public void precisionU(int precisionU)
    {
        this.precisionU = Math.max(2, precisionU);
    }

    /**
     * Return precisionV
     *
     * @return precisionV
     */
    public int precisionV()
    {
        return this.precisionV;
    }

    /**
     * Modify precisionV
     *
     * @param precisionV New precisionV value
     */
    public void precisionV(int precisionV)
    {
        this.precisionV = Math.max(2, precisionV);
    }

    /**
     * Refresh path and force join each part
     *
     * @param multiplierU U multiplier
     * @param linearized  Indicates if we try to linearized (Some try done good effect, some don't, so try)
     */
    public void refreshJoinedPath(final float multiplierU, final boolean linearized)
    {
        this.refreshJoinedPath(multiplierU, linearized, false);
    }

    /**
     * Refresh path and force join each part
     *
     * @param multiplierU    U multiplier
     * @param linearized     Indicates if we try to linearized (Some try done good effect, some don't, so try)
     * @param reverseNormals Indicates if normals must be reversed
     */
    public void refreshJoinedPath(final float multiplierU, final boolean linearized, final boolean reverseNormals)
    {
        this.joined = true;
        this.multiplierU = multiplierU;
        this.linearized = linearized;
        this.mesh = Tool3D.createJoinedMesh(this.pathU, this.precisionU, this.pathV, this.precisionV, multiplierU,
                                            linearized, reverseNormals);
        this.reconstructTheList();
    }

    /**
     * Refresh the path
     */
    public void refreshPath()
    {
        this.joined = false;
        this.multiplierU = 1;
        this.linearized = false;
        this.mesh = Tool3D.createMesh(this.pathU, this.precisionU, this.pathV, this.precisionV);
        this.reconstructTheList();
    }
}