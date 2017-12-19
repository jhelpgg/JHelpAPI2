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
import java.util.List;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.render.Mesh;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.render.Vertex;
import jhelp.engine2.twoD.Line2D;
import jhelp.engine2.twoD.Path;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;

/**
 * Revolution is a path draw on (X, Y) plane, then turn it around Y axis.
 *
 * @author JHelp
 */
public class Revolution
        extends Object3D
{
    /**
     * Angle of rotation
     */
    private       float angle;
    /**
     * U multiplier for rotation (V defines by the path)
     */
    private       float multiplierU;
    /**
     * The path (Values are U)
     */
    private final Path  path;
    /**
     * Precision used to draw the path
     */
    private       int   pathPrecision;

    /**
     * Precision for rotation
     */
    private int rotationPrecision;

    /**
     * Constructs default Revolution
     */
    public Revolution()
    {
        this(360f, 5, 12, 1f);
    }

    /**
     * Constructs Revolution
     *
     * @param angle             Angle of rotation
     * @param pathPrecision     Path precision
     * @param rotationPrecision Rotation precision
     * @param multiplierU       U multiplier (V are carry by the path)
     */
    public Revolution(final float angle, final int pathPrecision, final int rotationPrecision, final float multiplierU)
    {
        this.nodeType = NodeType.REVOLUTION;
        this.path = new Path();
        this.angle = angle;
        this.pathPrecision(pathPrecision);
        this.rotationPrecision(rotationPrecision);
        this.multiplierU = multiplierU;
    }

    /**
     * Refresh the revolution's mesh.
     */
    private void recomputeTheMesh()
    {
        this.recomputeTheMesh(false, 0, 1);
    }

    /**
     * Refresh the revolution's mesh.
     *
     * @param homogeneous Indicates if try to make it homogeneous or not
     * @param start       Interpolation value at path start (Available only for homogeneous at {@code true})
     * @param end         Interpolation value at path end (Available only for homogeneous at {@code true})
     */
    private void recomputeTheMesh(final boolean homogeneous, final float start, final float end)
    {
        // Initialization
        final Mesh mesh = new Mesh();

        final double radian = Math.toRadians(this.angle);
        double       angle, angleFuture;
        double       cos, cosFuture, sin, sinFuture;
        double       x0, y0, x1, y1, vx, vy;
        double       u0, u1, v0, v1;
        double       length;

        float xAA, yAA, zAA, uAA, vAA, nxAA, nyAA, nzAA;
        float xAF, yAF, zAF, uAF, vAF, nxAF, nyAF, nzAF;
        float xFA, yFA, zFA, uFA, vFA, nxFA, nyFA, nzFA;
        float xFF, yFF, zFF, uFF, vFF, nxFF, nyFF, nzFF;

        int an;

        List<Line2D> list;
        if (homogeneous)
        {
            list = this.path.computePathHomogeneous(this.pathPrecision, start, end);
        }
        else
        {
            list = this.path.computePath(this.pathPrecision);
        }

        // For each line of the path
        for (final Line2D line2D : list)
        {
            // Get start and end point
            x0 = line2D.pointStart.x();
            y0 = line2D.pointStart.y();
            v0 = line2D.start;

            x1 = line2D.pointEnd.x();
            y1 = line2D.pointEnd.y();
            v1 = line2D.end;

            // Compute the vector start to end and normalize it
            vx = x1 - x0;
            vy = y1 - y0;

            length = Math.sqrt((vx * vx) + (vy * vy));
            if (Math.abs(length) >= 1e-5)
            {
                vx /= length;
                vy /= length;
            }

            // For each rotation step
            for (an = 0; an < this.rotationPrecision; an++)
            {
                // Compute U
                u0 = (an * this.multiplierU) / this.rotationPrecision;
                u1 = ((an + 1f) * this.multiplierU) / this.rotationPrecision;

                // Compute angles, cosinus and sinus
                angle = (radian * an) / this.rotationPrecision;
                angleFuture = (radian * (an + 1)) / this.rotationPrecision;

                cos = Math.cos(angle);
                sin = Math.sin(angle);
                cosFuture = Math.cos(angleFuture);
                sinFuture = Math.sin(angleFuture);

                // Compute each vertex
                xAA = (float) (cos * x0);
                yAA = (float) (y0);
                zAA = (float) (-sin * x0);
                uAA = (float) (u0);
                vAA = (float) (v0);
                nxAA = (float) (cos * vy);
                nyAA = (float) (vx);
                nzAA = (float) (-sin * vy);

                xAF = (float) (cos * x1);
                yAF = (float) (y1);
                zAF = (float) (-sin * x1);
                uAF = (float) (u0);
                vAF = (float) (v1);
                nxAF = (float) (cos * vy);
                nyAF = (float) (vx);
                nzAF = (float) (-sin * vy);

                xFA = (float) (cosFuture * x0);
                yFA = (float) (y0);
                zFA = (float) (-sinFuture * x0);
                uFA = (float) (u1);
                vFA = (float) (v0);
                nxFA = (float) (cosFuture * vy);
                nyFA = (float) (vx);
                nzFA = (float) (-sinFuture * vy);

                xFF = (float) (cosFuture * x1);
                yFF = (float) (y1);
                zFF = (float) (-sinFuture * x1);
                uFF = (float) (u1);
                vFF = (float) (v1);
                nxFF = (float) (cosFuture * vy);
                nyFF = (float) (vx);
                nzFF = (float) (-sinFuture * vy);

                // Draw the face
                mesh.addVertexToTheActualFace(new Vertex(xAA, yAA, zAA, uAA, vAA, nxAA, nyAA, nzAA));
                mesh.addVertexToTheActualFace(new Vertex(xFA, yFA, zFA, uFA, vFA, nxFA, nyFA, nzFA));
                mesh.addVertexToTheActualFace(new Vertex(xFF, yFF, zFF, uFF, vFF, nxFF, nyFF, nzFF));
                mesh.addVertexToTheActualFace(new Vertex(xAF, yAF, zAF, uAF, vAF, nxAF, nyAF, nzAF));

                mesh.endFace();
            }
        }

        // Change object's mesh by the computed one
        this.mesh = mesh;
    }

    /**
     * Refresh the revolution's mesh homogeneously
     *
     * @param start Interpolation value at path start
     * @param end   Interpolation value at path
     */
    private void recomputeTheMesh(final float start, final float end)
    {
        this.recomputeTheMesh(true, start, end);
    }

    /**
     * @see Node#endParseXML()
     */
    @Override
    protected void endParseXML()
    {
        this.refreshRevolution();
    }

    /**
     * Read revolution parameters form XML
     *
     * @param markupXML XML to parse
     * @throws Exception On parsing problem
     * @see Node#readFromMarkup
     */
    @Override
    protected void readFromMarkup(final @NotNull MarkupXML markupXML) throws Exception
    {
        this.readMaterialFromMarkup(markupXML);
        this.angle = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_angle, 360f);
        this.pathPrecision = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_pathPrecision, 5);
        this.rotationPrecision = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_rotationPrecision, 12);
        this.multiplierU = markupXML.obtainParameter(ConstantsXML.MARKUP_NODE_multU, 1f);

        final EnumerationIterator<MarkupXML> enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_PATH);
        if (!enumerationIterator.hasMoreElements())
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_PATH, " in ",
                                         markupXML.getName()));
        }

        MarkupXML path = enumerationIterator.nextElement();
        this.path.loadFromXML(path);
        path = null;
    }

    /**
     * Write revolution in XML
     *
     * @param markupXML Markup to fill
     * @see Node#writeInMarkup
     */
    @Override
    protected void writeInMarkup(final @NotNull MarkupXML markupXML)
    {
        this.writeMaterialInMarkup(markupXML);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_angle, this.angle);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_pathPrecision, this.pathPrecision);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_rotationPrecision, this.rotationPrecision);
        markupXML.addParameter(ConstantsXML.MARKUP_NODE_multU, this.multiplierU);
        markupXML.addChild(this.path.saveToXML());
    }

    /**
     * Return angle
     *
     * @return angle
     */
    public float angle()
    {
        return this.angle;
    }

    /**
     * Modify angle
     *
     * @param angle New angle value
     */
    public void angle(final float angle)
    {
        this.angle = angle;
    }

    /**
     * Append cubic element to the path
     *
     * @param startPoint    Start point
     * @param start         start value
     * @param controlPoint1 First control point
     * @param control1      First control value
     * @param controlPoint2 Second control point
     * @param control2      Second control value
     * @param endPoint      End point
     * @param end           End value
     */
    public void appendCubic(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint1, final float control1,
            final @NotNull Point2D controlPoint2, final float control2,
            final @NotNull Point2D endPoint, final float end)
    {
        this.path.appendCubic(startPoint, start, controlPoint1, control1, controlPoint2, control2, endPoint, end);
    }

    /**
     * Append cubic element to the path
     *
     * @param startPoint    Start point
     * @param controlPoint1 First control point
     * @param controlPoint2 Second control point
     * @param endPoint      End point
     */
    public void appendCubic(
            final @NotNull Point2D startPoint,
            final @NotNull Point2D controlPoint1,
            final @NotNull Point2D controlPoint2,
            final @NotNull Point2D endPoint)
    {
        this.path.appendCubic(startPoint, controlPoint1, controlPoint2, endPoint);
    }

    /**
     * Append line to the path
     *
     * @param startPoint Start point
     * @param start      Start value
     * @param endPoint   End point
     * @param end        End value
     */
    public void appendLine(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D endPoint, final float end)
    {
        this.path.appendLine(startPoint, start, endPoint, end);
    }

    /**
     * Append line to the path
     *
     * @param startPoint Start point
     * @param endPoint   End point
     */
    public void appendLine(final @NotNull Point2D startPoint, final @NotNull Point2D endPoint)
    {
        this.path.appendLine(startPoint, endPoint);
    }

    /**
     * Append quadratic element to the path
     *
     * @param startPoint   Start point
     * @param start        Start value
     * @param controlPoint Control point
     * @param control      Control value
     * @param endPoint     End point
     * @param end          End value
     */
    public void appendQuad(
            final @NotNull Point2D startPoint, final float start,
            final @NotNull Point2D controlPoint, final float control,
            final @NotNull Point2D endPoint, final float end)
    {
        this.path.appendQuad(startPoint, start, controlPoint, control, endPoint, end);
    }

    /**
     * Append quadratic element to the path
     *
     * @param startPoint   Start point
     * @param controlPoint Control point
     * @param endPoint     End point
     */
    public void appendQuad(
            final @NotNull Point2D startPoint,
            final @NotNull Point2D controlPoint,
            final @NotNull Point2D endPoint)
    {
        this.path.appendQuad(startPoint, controlPoint, endPoint);
    }

    /**
     * Try to linearized the path
     *
     * @param start Start value
     * @param end   End value
     */
    public void linearize(final float start, final float end)
    {
        this.path.linearize(start, end);
    }

    /**
     * Return multiplierU
     *
     * @return multiplierU
     */
    public float multiplierU()
    {
        return this.multiplierU;
    }

    /**
     * Modify multiplierU
     *
     * @param multiplierU New multiplierU value
     */
    public void multiplierU(final float multiplierU)
    {
        this.multiplierU = multiplierU;
    }

    /**
     * Obtain revolution path
     *
     * @return Revolution path
     */
    public @NotNull Path path()
    {
        return this.path;
    }

    /**
     * Return pathPrecision
     *
     * @return pathPrecision
     */
    public int pathPrecision()
    {
        return this.pathPrecision;
    }

    /**
     * Modify pathPrecision
     *
     * @param pathPrecision New pathPrecision value
     */
    public void pathPrecision(int pathPrecision)
    {
        this.pathPrecision = Math.max(2, pathPrecision);
    }

    /**
     * Refresh the revolution.<br>
     * Call it when you made modification and want see the result.
     */
    public void refreshRevolution()
    {
        this.recomputeTheMesh();
        this.reconstructTheList();
    }

    /**
     * Refresh the revolution's mesh homogeneously
     *
     * @param start Interpolation value at path start
     * @param end   Interpolation value at path
     */
    public void refreshRevolution(final float start, final float end)
    {
        this.recomputeTheMesh(start, end);
        this.reconstructTheList();
    }

    /**
     * Return rotationPrecision
     *
     * @return rotationPrecision
     */
    public int rotationPrecision()
    {
        return this.rotationPrecision;
    }

    /**
     * Modify rotationPrecision
     *
     * @param rotationPrecision New rotationPrecision value
     */
    public void rotationPrecision(int rotationPrecision)
    {
        this.rotationPrecision = Math.max(3, rotationPrecision);
    }
}