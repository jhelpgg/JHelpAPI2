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
import com.sun.istack.internal.Nullable;
import java.awt.geom.Rectangle2D;
import java.util.List;
import jhelp.engine2.geometry.event.Equation3DListener;
import jhelp.engine2.render.NodeType;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.Object3D;
import jhelp.engine2.render.Vertex;
import jhelp.engine2.twoD.Line2D;
import jhelp.engine2.twoD.Path;
import jhelp.engine2.util.Math3D;
import jhelp.engine2.util.Rotf;
import jhelp.engine2.util.Vec3f;
import jhelp.util.math.formal.Function;
import jhelp.util.math.formal.Variable;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Equation 3D.<br>
 * It it a easy way to represents a equation 3D
 *
 * @author JHelp
 */
public class Equation3D
        extends Object3D
{
    /**
     * Creator of equation
     *
     * @author JHelp
     */
    private class Creator
            implements RunnableTask
    {
        /**
         * Path border
         */
        Path     border;
        /**
         * Precision to use
         */
        int      borderPrecision;
        /**
         * Function of X : x = X(t)
         */
        Function functionX;
        /**
         * Function of Y : y = Y(t)
         */
        Function functionY;
        /**
         * Function of Z : z = Z(t)
         */
        Function functionZ;
        /**
         * t end value
         */
        float    tEnd;
        /**
         * t start value
         */
        float    tStart;
        /**
         * t step to increment
         */
        float    tStep;

        /**
         * Constructs Creator
         *
         * @param border          Path border
         * @param borderPrecision Precision to use
         * @param start           t start
         * @param end             t end
         * @param step            step to increment t
         * @param functionX       Function of X : x = X(t)
         * @param functionY       Function of Y : y = Y(t)
         * @param functionZ       Function of Z : z = Z(t)
         */
        Creator(
                final @NotNull Path border, final int borderPrecision,
                final float start, final float end, final float step,
                final @NotNull Function functionX, final @NotNull Function functionY, final @NotNull Function functionZ)
        {
            this.border = border;
            this.borderPrecision = borderPrecision;
            this.tStart = start;
            this.tEnd = end;
            this.tStep = step;
            this.functionX = functionX;
            this.functionY = functionY;
            this.functionZ = functionZ;
        }

        /**
         * Compute the equation
         */
        @Override
        public void run()
        {
            this.functionX = this.functionX.simplifyMaximum();
            this.functionY = this.functionY.simplifyMaximum();
            this.functionZ = this.functionZ.simplifyMaximum();

            final Variable varT    = new Variable("t");
            final Function deriveX = this.functionX.derive(varT).simplifyMaximum();
            final Function deriveY = this.functionY.derive(varT).simplifyMaximum();
            final Function deriveZ = this.functionZ.derive(varT).simplifyMaximum();

            final List<Line2D> lines = this.border.computePath(this.borderPrecision);
            Line2D             line;
            final int          size  = lines.size();
            float              dx, dy, dz, l, x, y, z, xStart, xEnd, yStart, yEnd, dx2, dy2, dz2, x2, y2, z2;
            Vertex             vertex00, vertex01, vertex10, vertex11;
            final Vec3f        axisZ = new Vec3f(0, 0, 1);
            Vec3f              normal, point;
            Rotf               rot, rot2;
            final Rectangle2D  limit = this.border.computeBorder();
            final float        minU  = (float) limit.getX();
            final float        minV  = (float) limit.getY();
            final float        multU = 1f / (float) limit.getWidth();
            final float        multV = 1f / (float) limit.getHeight();

            for (float t = this.tStart; t < this.tEnd; t += this.tStep)
            {
                dx = (float) deriveX.replace(varT, t).simplifyMaximum().obtainRealValueNumber();
                dy = (float) deriveY.replace(varT, t).simplifyMaximum().obtainRealValueNumber();
                dz = (float) deriveZ.replace(varT, t).simplifyMaximum().obtainRealValueNumber();

                l = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                if (!Math3D.nul(l))
                {
                    dx /= l;
                    dy /= l;
                    dz /= l;
                }

                normal = new Vec3f(dx, dy, dz);
                rot = new Rotf(axisZ, normal);

                x = (float) this.functionX.replace(varT, t).simplifyMaximum().obtainRealValueNumber();
                y = (float) this.functionY.replace(varT, t).simplifyMaximum().obtainRealValueNumber();
                z = (float) this.functionZ.replace(varT, t).simplifyMaximum().obtainRealValueNumber();

                //

                dx2 = (float) deriveX.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();
                dy2 = (float) deriveY.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();
                dz2 = (float) deriveZ.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();

                l = (float) Math.sqrt((dx2 * dx2) + (dy2 * dy2) + (dz2 * dz2));

                if (!Math3D.nul(l))
                {
                    dx2 /= l;
                    dy2 /= l;
                    dz2 /= l;
                }

                normal = new Vec3f(dx2, dy2, dz2);
                rot2 = new Rotf(axisZ, normal);

                x2 = (float) this.functionX.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();
                y2 = (float) this.functionY.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();
                z2 = (float) this.functionZ.replace(varT, t + this.tStep).simplifyMaximum().obtainRealValueNumber();

                for (int lig = 0; lig < size; lig++)
                {
                    line = lines.get(lig);

                    // Start
                    xStart = line.pointStart.x();
                    yStart = line.pointStart.y();

                    point = new Vec3f(xStart, yStart, 0);
                    point = rot.rotateVector(point);

                    vertex00 = new Vertex(point.x() + x, point.y() + y, point.z() + z,//
                                          (xStart - minU) * multU, (yStart - minV) * multV,//
                                          -point.x(), -point.y(), -point.z());

                    // End
                    xEnd = line.pointEnd.x();
                    yEnd = line.pointEnd.y();

                    point = new Vec3f(xEnd, yEnd, 0);
                    point = rot.rotateVector(point);

                    vertex01 = new Vertex(point.x() + x, point.y() + y, point.z() + z,//
                                          (xEnd - minU) * multU, (yEnd - minV) * multV,//
                                          -point.x(), -point.y(), -point.z());

                    // ---*---

                    // Start
                    point = new Vec3f(xStart, yStart, 0);
                    point = rot2.rotateVector(point);

                    vertex10 = new Vertex(point.x() + x2, point.y() + y2, point.z() + z2,//
                                          (xStart - minU) * multU, (yStart - minV) * multV,//
                                          -point.x(), -point.y(), -point.z());

                    // End
                    point = new Vec3f(xEnd, yEnd, 0);
                    point = rot2.rotateVector(point);

                    vertex11 = new Vertex(point.x() + x2, point.y() + y2, point.z() + z2,//
                                          (xEnd - minU) * multU, (yEnd - minV) * multV,//
                                          -point.x(), -point.y(), -point.z());

                    // ---*---

                    Equation3D.this.mesh.addVertexToTheActualFace(vertex10);
                    Equation3D.this.mesh.addVertexToTheActualFace(vertex11);
                    Equation3D.this.mesh.addVertexToTheActualFace(vertex01);
                    Equation3D.this.mesh.addVertexToTheActualFace(vertex00);

                    Equation3D.this.mesh.endFace();
                    Equation3D.this.flush();
                }
            }

            Equation3D.this.flush();
            Equation3D.this.computeUVspherical(1, 1);

            if (Equation3D.this.equation3DListener != null)
            {
                Equation3D.this.equation3DListener.equation3Dready(Equation3D.this);
            }
        }
    }

    /**
     * Border repeated path
     */
    private final Path     border;
    /**
     * Precision used for border
     */
    private final int      borderPrecision;
    /**
     * Function X(t)
     */
    private final Function functionX;
    /**
     * Function Y(t)
     */
    private final Function functionY;
    /**
     * Function Z(t)
     */
    private final Function functionZ;
    /**
     * t value at end
     */
    private final float    tEnd;
    /**
     * t value at start
     */
    private final float    tStart;
    /**
     * t step size to use
     */
    private final float    tStep;
    /**
     * Listener of equation computing
     */
    Equation3DListener equation3DListener;

    /**
     * Constructs Equation3D
     *
     * @param border          Path border
     * @param borderPrecision Border precision
     * @param tStart          t start
     * @param tEnd            t end
     * @param tStep           t step for increment
     * @param functionX       Function X : x = X(t)
     * @param functionY       Function Y : y = Y(t)
     * @param functionZ       Function Z : z = Z(t)
     */
    public Equation3D(
            final @NotNull Path border, final int borderPrecision,
            final float tStart, final float tEnd, final float tStep,
            final @NotNull Function functionX, final @NotNull Function functionY, final @NotNull Function functionZ)
    {
        this(border, borderPrecision, tStart, tEnd, tStep, functionX, functionY, functionZ, null);
    }

    /**
     * Constructs Equation3D
     *
     * @param border             Path border
     * @param borderPrecision    Border precision
     * @param tStart             t start
     * @param tEnd               t end
     * @param tStep              t step for increment
     * @param functionX          Function X : x = X(t)
     * @param functionY          Function Y : y = Y(t)
     * @param functionZ          Function Z : z = Z(t)
     * @param equation3DListener Listener for know when ready. If {@code null} no listener registered
     */
    public Equation3D(
            final @NotNull Path border, final int borderPrecision,
            final float tStart, final float tEnd, final float tStep,
            final @NotNull Function functionX, final @NotNull Function functionY, final @NotNull Function functionZ,
            final @Nullable Equation3DListener equation3DListener)
    {
        this.nodeType = NodeType.EQUATION;
        this.equation3DListener = equation3DListener;

        this.border = border;
        this.borderPrecision = borderPrecision;
        this.tStart = tStart;
        this.tEnd = tEnd;
        this.tStep = tStep;
        this.functionX = functionX.simplifyMaximum();
        this.functionY = functionY.simplifyMaximum();
        this.functionZ = functionZ.simplifyMaximum();
        this.twoSidedState(NodeWithMaterial.TwoSidedState.FORCE_TWO_SIDE);

        ThreadManager.parallel(new Creator(border, borderPrecision,
                                           tStart, tEnd, tStep,
                                           functionX.simplifyMaximum(),
                                           functionY.simplifyMaximum(),
                                           functionZ.simplifyMaximum()));
    }

    /**
     * Constructs Equation3D
     *
     * @param border          Path border
     * @param borderPrecision Border precision
     * @param tStart          t start
     * @param tEnd            t end
     * @param tStep           t step for increment
     * @param functionX       Function X : x = X(t)
     * @param functionY       Function Y : y = Y(t)
     * @param functionZ       Function Z : z = Z(t)
     */
    public Equation3D(
            final @NotNull Path border, final int borderPrecision,
            final float tStart, final float tEnd, final float tStep,
            final @NotNull String functionX, final @NotNull String functionY, final @NotNull String functionZ)
    {
        this(border, borderPrecision,
             tStart, tEnd, tStep,
             Function.parse(functionX), Function.parse(functionY), Function.parse(functionZ),
             null);
    }

    /**
     * Constructs Equation3D
     *
     * @param border             Path border
     * @param borderPrecision    Border precision
     * @param tStart             t start
     * @param tEnd               t end
     * @param tStep              t step for increment
     * @param functionX          Function X : x = X(t)
     * @param functionY          Function Y : y = Y(t)
     * @param functionZ          Function Z : z = Z(t)
     * @param equation3DListener Listener for know when ready. If {@code null} no listener registered
     */
    public Equation3D(
            final @NotNull Path border, final int borderPrecision,
            final float tStart, final float tEnd, final float tStep,
            final @NotNull String functionX, final @NotNull String functionY, final @NotNull String functionZ,
            final @Nullable Equation3DListener equation3DListener)
    {
        this(border, borderPrecision,
             tStart, tEnd, tStep,
             Function.parse(functionX), Function.parse(functionY), Function.parse(functionZ),
             equation3DListener);
    }

    /**
     * Border path (Path repeat along the equation)
     *
     * @return Border path
     */
    public @NotNull Path border()
    {
        return this.border;
    }

    /**
     * Border precision
     *
     * @return Border precision
     */
    public int borderPrecision()
    {
        return this.borderPrecision;
    }

    /**
     * X(t)
     *
     * @return X(t)
     */
    public @NotNull Function functionX()
    {
        return this.functionX;
    }

    /**
     * Y(t)
     *
     * @return Y(t)
     */
    public @NotNull Function functionY()
    {
        return this.functionY;
    }

    /**
     * Z(t)
     *
     * @return Z(t)
     */
    public @NotNull Function functionZ()
    {
        return this.functionZ;
    }

    /**
     * t value at end
     *
     * @return t value at end
     */
    public float tEnd()
    {
        return this.tEnd;
    }

    /**
     * t value at start
     *
     * @return t value at start
     */
    public float tStart()
    {
        return this.tStart;
    }

    /**
     * t step size
     *
     * @return t step size
     */
    public float tStep()
    {
        return this.tStep;
    }
}