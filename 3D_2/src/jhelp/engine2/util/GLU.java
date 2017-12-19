/*
 * Copy from JOGL
 */

package jhelp.engine2.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

/**
 * Utilities GLU copied from JOGL<br>
 * Warning if used, this class will change very deeply
 *
 * @TODO Remove useless methods, do friendly methods
 */
public class GLU
{
    static class Project
    {
        private static final double[] IDENTITY_MATRIX = new double[]{1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D,
                                                                     0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D};

        private static void cross(double[] var0, double[] var1, double[] var2)
        {
            var2[0] = var0[1] * var1[2] - var0[2] * var1[1];
            var2[1] = var0[2] * var1[0] - var0[0] * var1[2];
            var2[2] = var0[0] * var1[1] - var0[1] * var1[0];
        }

        private static void cross(DoubleBuffer var0, DoubleBuffer var1, DoubleBuffer var2)
        {
            int var3 = var0.position();
            int var4 = var1.position();
            int var5 = var2.position();
            var2.put(0 + var5, var0.get(1 + var3) * var1.get(2 + var4) - var0.get(2 + var3) * var1.get(1 + var4));
            var2.put(1 + var5, var0.get(2 + var3) * var1.get(0 + var4) - var0.get(0 + var3) * var1.get(2 + var4));
            var2.put(2 + var5, var0.get(0 + var3) * var1.get(1 + var4) - var0.get(1 + var3) * var1.get(0 + var4));
        }

        private static void normalize(double[] var0)
        {
            double var1 = Math.sqrt(var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2]);
            if (var1 != 0.0D)
            {
                var1 = 1.0D / var1;
                var0[0] *= var1;
                var0[1] *= var1;
                var0[2] *= var1;
            }
        }

        private static void normalize(DoubleBuffer var0)
        {
            int var3 = var0.position();
            double var1 = Math.sqrt(var0.get(0 + var3) * var0.get(0 + var3) + var0.get(1 + var3) * var0.get(1 + var3) +
                                    var0.get(2 + var3) * var0.get(2 + var3));
            if (var1 != 0.0D)
            {
                var1 = 1.0D / var1;
                var0.put(0 + var3, var0.get(0 + var3) * var1);
                var0.put(1 + var3, var0.get(1 + var3) * var1);
                var0.put(2 + var3, var0.get(2 + var3) * var1);
            }
        }

        private static DoubleBuffer slice(DoubleBuffer var0, int var1, int var2)
        {
            var0.position(var1);
            var0.limit(var1 + var2);
            return var0.slice();
        }

        private final double[] forward = new double[3];
        private final DoubleBuffer forwardBuf;
        private final double[] in = new double[4];
        private final DoubleBuffer inBuf;
        private final double[] matrix = new double[16];
        private final DoubleBuffer matrixBuf;
        private final double[] out = new double[4];
        private final DoubleBuffer outBuf;
        private final double[] side = new double[3];
        private final DoubleBuffer sideBuf;
        private final double[][] tempMatrix = new double[4][4];
        private final DoubleBuffer tempMatrixBuf;
        private final double[] up = new double[3];
        private final DoubleBuffer upBuf;

        public Project()
        {
            DoubleBuffer var1 = ByteBuffer.allocateDirect(128 * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer();
            byte         var2 = 0;
            byte         var3 = 16;
            this.matrixBuf = Project.slice(var1, var2, var3);
            int var4 = var2 + var3;
            this.tempMatrixBuf = Project.slice(var1, var4, var3);
            var4 += var3;
            var3 = 4;
            this.inBuf = Project.slice(var1, var4, var3);
            var4 += var3;
            this.outBuf = Project.slice(var1, var4, var3);
            var4 += var3;
            var3 = 3;
            this.forwardBuf = Project.slice(var1, var4, var3);
            var4 += var3;
            this.sideBuf = Project.slice(var1, var4, var3);
            var4 += var3;
            this.upBuf = Project.slice(var1, var4, var3);
        }

        private boolean __gluInvertMatrixd(double[] var1, double[] var2)
        {
            double[][] var9 = this.tempMatrix;

            int var3;
            int var4;
            for (var3 = 0; var3 < 4; ++var3)
            {
                for (var4 = 0; var4 < 4; ++var4)
                {
                    var9[var3][var4] = var1[var3 * 4 + var4];
                }
            }

            this.__gluMakeIdentityd(var2);

            for (var3 = 0; var3 < 4; ++var3)
            {
                int var6 = var3;

                for (var4 = var3 + 1; var4 < 4; ++var4)
                {
                    if (Math.abs(var9[var4][var3]) > Math.abs(var9[var3][var3]))
                    {
                        var6 = var4;
                    }
                }

                int    var5;
                double var7;
                if (var6 != var3)
                {
                    for (var5 = 0; var5 < 4; ++var5)
                    {
                        var7 = var9[var3][var5];
                        var9[var3][var5] = var9[var6][var5];
                        var9[var6][var5] = var7;
                        var7 = var2[var3 * 4 + var5];
                        var2[var3 * 4 + var5] = var2[var6 * 4 + var5];
                        var2[var6 * 4 + var5] = var7;
                    }
                }

                if (var9[var3][var3] == 0.0D)
                {
                    return false;
                }

                var7 = var9[var3][var3];

                for (var5 = 0; var5 < 4; ++var5)
                {
                    var9[var3][var5] /= var7;
                    var2[var3 * 4 + var5] /= var7;
                }

                for (var4 = 0; var4 < 4; ++var4)
                {
                    if (var4 != var3)
                    {
                        var7 = var9[var4][var3];

                        for (var5 = 0; var5 < 4; ++var5)
                        {
                            var9[var4][var5] -= var9[var3][var5] * var7;
                            var2[var4 * 4 + var5] -= var2[var3 * 4 + var5] * var7;
                        }
                    }
                }
            }

            return true;
        }

        private boolean __gluInvertMatrixd(DoubleBuffer var1, DoubleBuffer var2)
        {
            int          var9  = var1.position();
            int          var10 = var2.position();
            DoubleBuffer var11 = this.tempMatrixBuf;

            int var3;
            int var4;
            for (var3 = 0; var3 < 4; ++var3)
            {
                for (var4 = 0; var4 < 4; ++var4)
                {
                    var11.put(var3 * 4 + var4, var1.get(var3 * 4 + var4 + var9));
                }
            }

            this.__gluMakeIdentityd(var2);

            for (var3 = 0; var3 < 4; ++var3)
            {
                int var6 = var3;

                for (var4 = var3 + 1; var4 < 4; ++var4)
                {
                    if (Math.abs(var11.get(var4 * 4 + var3)) > Math.abs(var11.get(var3 * 4 + var3)))
                    {
                        var6 = var4;
                    }
                }

                int    var5;
                double var7;
                if (var6 != var3)
                {
                    for (var5 = 0; var5 < 4; ++var5)
                    {
                        var7 = var11.get(var3 * 4 + var5);
                        var11.put(var3 * 4 + var5, var11.get(var6 * 4 + var5));
                        var11.put(var6 * 4 + var5, var7);
                        var7 = var2.get(var3 * 4 + var5 + var10);
                        var2.put(var3 * 4 + var5 + var10, var2.get(var6 * 4 + var5 + var10));
                        var2.put(var6 * 4 + var5 + var10, var7);
                    }
                }

                if (var11.get(var3 * 4 + var3) == 0.0D)
                {
                    return false;
                }

                var7 = var11.get(var3 * 4 + var3);

                for (var5 = 0; var5 < 4; ++var5)
                {
                    var11.put(var3 * 4 + var5, var11.get(var3 * 4 + var5) / var7);
                    var2.put(var3 * 4 + var5 + var10, var2.get(var3 * 4 + var5 + var10) / var7);
                }

                for (var4 = 0; var4 < 4; ++var4)
                {
                    if (var4 != var3)
                    {
                        var7 = var11.get(var4 * 4 + var3);

                        for (var5 = 0; var5 < 4; ++var5)
                        {
                            var11.put(var4 * 4 + var5, var11.get(var4 * 4 + var5) - var11.get(var3 * 4 + var5) * var7);
                            var2.put(var4 * 4 + var5 + var10,
                                     var2.get(var4 * 4 + var5 + var10) - var2.get(var3 * 4 + var5 + var10) * var7);
                        }
                    }
                }
            }

            return true;
        }

        private void __gluMakeIdentityd(DoubleBuffer var1)
        {
            int var2 = var1.position();
            var1.put(Project.IDENTITY_MATRIX);
            var1.position(var2);
        }

        private void __gluMakeIdentityd(double[] var1)
        {
            for (int var2 = 0; var2 < 16; ++var2)
            {
                var1[var2] = Project.IDENTITY_MATRIX[var2];
            }

        }

        private void __gluMultMatricesd(double[] var1, int var2, double[] var3, int var4, double[] var5)
        {
            for (int var6 = 0; var6 < 4; ++var6)
            {
                for (int var7 = 0; var7 < 4; ++var7)
                {
                    var5[var6 * 4 + var7] = var1[var6 * 4 + 0 + var2] * var3[0 + var7 + var4] +
                                            var1[var6 * 4 + 1 + var2] * var3[4 + var7 + var4] +
                                            var1[var6 * 4 + 2 + var2] * var3[8 + var7 + var4] +
                                            var1[var6 * 4 + 3 + var2] * var3[12 + var7 + var4];
                }
            }

        }

        private void __gluMultMatricesd(DoubleBuffer var1, DoubleBuffer var2, DoubleBuffer var3)
        {
            int var4 = var1.position();
            int var5 = var2.position();
            int var6 = var3.position();

            for (int var7 = 0; var7 < 4; ++var7)
            {
                for (int var8 = 0; var8 < 4; ++var8)
                {
                    var3.put(var7 * 4 + var8 + var6, var1.get(var7 * 4 + 0 + var4) * var2.get(0 + var8 + var5) +
                                                     var1.get(var7 * 4 + 1 + var4) * var2.get(4 + var8 + var5) +
                                                     var1.get(var7 * 4 + 2 + var4) * var2.get(8 + var8 + var5) +
                                                     var1.get(var7 * 4 + 3 + var4) * var2.get(12 + var8 + var5));
                }
            }

        }

        private void __gluMultMatrixVecd(double[] var1, int var2, double[] var3, double[] var4)
        {
            for (int var5 = 0; var5 < 4; ++var5)
            {
                var4[var5] = var3[0] * var1[0 + var5 + var2] + var3[1] * var1[4 + var5 + var2] +
                             var3[2] * var1[8 + var5 + var2] + var3[3] * var1[12 + var5 + var2];
            }

        }

        private void __gluMultMatrixVecd(DoubleBuffer var1, DoubleBuffer var2, DoubleBuffer var3)
        {
            int var4 = var2.position();
            int var5 = var3.position();
            int var6 = var1.position();

            for (int var7 = 0; var7 < 4; ++var7)
            {
                var3.put(var7 + var5, var2.get(0 + var4) * var1.get(0 + var7 + var6) +
                                      var2.get(1 + var4) * var1.get(4 + var7 + var6) +
                                      var2.get(2 + var4) * var1.get(8 + var7 + var6) +
                                      var2.get(3 + var4) * var1.get(12 + var7 + var6));
            }

        }

        public void gluLookAt(
                double var2, double var4, double var6, double var8, double var10, double var12, double var14,
                double var16, double var18)
        {
            DoubleBuffer var20 = this.forwardBuf;
            DoubleBuffer var21 = this.sideBuf;
            DoubleBuffer var22 = this.upBuf;
            var20.put(0, var8 - var2);
            var20.put(1, var10 - var4);
            var20.put(2, var12 - var6);
            var22.put(0, var14);
            var22.put(1, var16);
            var22.put(2, var18);
            Project.normalize(var20);
            Project.cross(var20, var22, var21);
            Project.normalize(var21);
            Project.cross(var21, var20, var22);
            this.__gluMakeIdentityd(this.matrixBuf);
            this.matrixBuf.put(0, var21.get(0));
            this.matrixBuf.put(4, var21.get(1));
            this.matrixBuf.put(8, var21.get(2));
            this.matrixBuf.put(1, var22.get(0));
            this.matrixBuf.put(5, var22.get(1));
            this.matrixBuf.put(9, var22.get(2));
            this.matrixBuf.put(2, -var20.get(0));
            this.matrixBuf.put(6, -var20.get(1));
            this.matrixBuf.put(10, -var20.get(2));
            GL11.glMultMatrixd(this.matrixBuf);
            GL11.glTranslated(-var2, -var4, -var6);
        }

        public void gluOrtho2D(double var2, double var4, double var6, double var8)
        {
            GL11.glOrtho(var2, var4, var6, var8, -1.0D, 1.0D);
        }

        public void gluPerspective(double var2, double var4, double var6, double var8)
        {
            double var16 = var2 / 2.0D * 3.141592653589793D / 180.0D;
            double var14 = var8 - var6;
            double var10 = Math.sin(var16);
            if (var14 != 0.0D && var10 != 0.0D && var4 != 0.0D)
            {
                double var12 = Math.cos(var16) / var10;
                this.__gluMakeIdentityd(this.matrixBuf);
                this.matrixBuf.put(0, var12 / var4);
                this.matrixBuf.put(5, var12);
                this.matrixBuf.put(10, -(var8 + var6) / var14);
                this.matrixBuf.put(11, -1.0D);
                this.matrixBuf.put(14, -2.0D * var6 * var8 / var14);
                this.matrixBuf.put(15, 0.0D);
                GL11.glMultMatrixd(this.matrixBuf);
            }
        }

        public void gluPickMatrix(double var2, double var4, double var6, double var8, IntBuffer var10)
        {
            if (var6 > 0.0D && var8 > 0.0D)
            {
                int var11 = var10.position();
                GL11.glTranslated(
                        ((double) var10.get(2 + var11) - 2.0D * (var2 - (double) var10.get(0 + var11))) / var6,
                        ((double) var10.get(3 + var11) - 2.0D * (var4 - (double) var10.get(1 + var11))) / var8, 0.0D);
                GL11.glScaled((double) var10.get(2) / var6, (double) var10.get(3) / var8, 1.0D);
            }
        }

        public void gluPickMatrix(double var2, double var4, double var6, double var8, int[] var10, int var11)
        {
            if (var6 > 0.0D && var8 > 0.0D)
            {
                GL11.glTranslated(((double) var10[2 + var11] - 2.0D * (var2 - (double) var10[0 + var11])) / var6,
                                  ((double) var10[3 + var11] - 2.0D * (var4 - (double) var10[1 + var11])) / var8, 0.0D);
                GL11.glScaled((double) var10[2 + var11] / var6, (double) var10[3 + var11] / var8, 1.0D);
            }
        }

        public boolean gluProject(
                double var1, double var3, double var5, double[] var7, int var8, double[] var9, int var10, int[] var11,
                int var12, double[] var13, int var14)
        {
            double[] var15 = this.in;
            double[] var16 = this.out;
            var15[0] = var1;
            var15[1] = var3;
            var15[2] = var5;
            var15[3] = 1.0D;
            this.__gluMultMatrixVecd(var7, var8, var15, var16);
            this.__gluMultMatrixVecd(var9, var10, var16, var15);
            if (var15[3] == 0.0D)
            {
                return false;
            }
            else
            {
                var15[3] = 1.0D / var15[3] * 0.5D;
                var15[0] = var15[0] * var15[3] + 0.5D;
                var15[1] = var15[1] * var15[3] + 0.5D;
                var15[2] = var15[2] * var15[3] + 0.5D;
                var13[0 + var14] = var15[0] * (double) var11[2 + var12] + (double) var11[0 + var12];
                var13[1 + var14] = var15[1] * (double) var11[3 + var12] + (double) var11[1 + var12];
                var13[2 + var14] = var15[2];
                return true;
            }
        }

        public boolean gluProject(
                double var1, double var3, double var5, DoubleBuffer var7, DoubleBuffer var8, IntBuffer var9,
                DoubleBuffer var10)
        {
            DoubleBuffer var11 = this.inBuf;
            DoubleBuffer var12 = this.outBuf;
            var11.put(0, var1);
            var11.put(1, var3);
            var11.put(2, var5);
            var11.put(3, 1.0D);
            this.__gluMultMatrixVecd(var7, var11, var12);
            this.__gluMultMatrixVecd(var8, var12, var11);
            if (var11.get(3) == 0.0D)
            {
                return false;
            }
            else
            {
                var11.put(3, 1.0D / var11.get(3) * 0.5D);
                var11.put(0, var11.get(0) * var11.get(3) + 0.5D);
                var11.put(1, var11.get(1) * var11.get(3) + 0.5D);
                var11.put(2, var11.get(2) * var11.get(3) + 0.5D);
                int var13 = var9.position();
                int var14 = var10.position();
                var10.put(0 + var14, var11.get(0) * (double) var9.get(2 + var13) + (double) var9.get(0 + var13));
                var10.put(1 + var14, var11.get(1) * (double) var9.get(3 + var13) + (double) var9.get(1 + var13));
                var10.put(2 + var14, var11.get(2));
                return true;
            }
        }

        public boolean gluUnProject(
                double var1, double var3, double var5, double[] var7, int var8, double[] var9, int var10, int[] var11,
                int var12, double[] var13, int var14)
        {
            double[] var15 = this.in;
            double[] var16 = this.out;
            this.__gluMultMatricesd(var7, var8, var9, var10, this.matrix);
            if (!this.__gluInvertMatrixd(this.matrix, this.matrix))
            {
                return false;
            }
            else
            {
                var15[0] = var1;
                var15[1] = var3;
                var15[2] = var5;
                var15[3] = 1.0D;
                var15[0] = (var15[0] - (double) var11[0 + var12]) / (double) var11[2 + var12];
                var15[1] = (var15[1] - (double) var11[1 + var12]) / (double) var11[3 + var12];
                var15[0] = var15[0] * 2.0D - 1.0D;
                var15[1] = var15[1] * 2.0D - 1.0D;
                var15[2] = var15[2] * 2.0D - 1.0D;
                this.__gluMultMatrixVecd(this.matrix, 0, var15, var16);
                if (var16[3] == 0.0D)
                {
                    return false;
                }
                else
                {
                    var16[3] = 1.0D / var16[3];
                    var13[0 + var14] = var16[0] * var16[3];
                    var13[1 + var14] = var16[1] * var16[3];
                    var13[2 + var14] = var16[2] * var16[3];
                    return true;
                }
            }
        }

        public boolean gluUnProject(
                double var1, double var3, double var5, DoubleBuffer var7, DoubleBuffer var8, IntBuffer var9,
                DoubleBuffer var10)
        {
            DoubleBuffer var11 = this.inBuf;
            DoubleBuffer var12 = this.outBuf;
            this.__gluMultMatricesd(var7, var8, this.matrixBuf);
            if (!this.__gluInvertMatrixd(this.matrixBuf, this.matrixBuf))
            {
                return false;
            }
            else
            {
                var11.put(0, var1);
                var11.put(1, var3);
                var11.put(2, var5);
                var11.put(3, 1.0D);
                int var13 = var9.position();
                int var14 = var10.position();
                var11.put(0, (var11.get(0) - (double) var9.get(0 + var13)) / (double) var9.get(2 + var13));
                var11.put(1, (var11.get(1) - (double) var9.get(1 + var13)) / (double) var9.get(3 + var13));
                var11.put(0, var11.get(0) * 2.0D - 1.0D);
                var11.put(1, var11.get(1) * 2.0D - 1.0D);
                var11.put(2, var11.get(2) * 2.0D - 1.0D);
                this.__gluMultMatrixVecd(this.matrixBuf, var11, var12);
                if (var12.get(3) == 0.0D)
                {
                    return false;
                }
                else
                {
                    var12.put(3, 1.0D / var12.get(3));
                    var10.put(0 + var14, var12.get(0) * var12.get(3));
                    var10.put(1 + var14, var12.get(1) * var12.get(3));
                    var10.put(2 + var14, var12.get(2) * var12.get(3));
                    return true;
                }
            }
        }

        public boolean gluUnProject4(
                double var1, double var3, double var5, double var7, double[] var9, int var10, double[] var11, int var12,
                int[] var13, int var14, double var15, double var17, double[] var19, int var20)
        {
            double[] var21 = this.in;
            double[] var22 = this.out;
            this.__gluMultMatricesd(var9, var10, var11, var12, this.matrix);
            if (!this.__gluInvertMatrixd(this.matrix, this.matrix))
            {
                return false;
            }
            else
            {
                var21[0] = var1;
                var21[1] = var3;
                var21[2] = var5;
                var21[3] = var7;
                var21[0] = (var21[0] - (double) var13[0 + var14]) / (double) var13[2 + var14];
                var21[1] = (var21[1] - (double) var13[1 + var14]) / (double) var13[3 + var14];
                var21[2] = (var21[2] - var15) / (var17 - var15);
                var21[0] = var21[0] * 2.0D - 1.0D;
                var21[1] = var21[1] * 2.0D - 1.0D;
                var21[2] = var21[2] * 2.0D - 1.0D;
                this.__gluMultMatrixVecd(this.matrix, 0, var21, var22);
                if (var22[3] == 0.0D)
                {
                    return false;
                }
                else
                {
                    var19[0 + var20] = var22[0];
                    var19[1 + var20] = var22[1];
                    var19[2 + var20] = var22[2];
                    var19[3 + var20] = var22[3];
                    return true;
                }
            }
        }

        public boolean gluUnProject4(
                double var1, double var3, double var5, double var7, DoubleBuffer var9, DoubleBuffer var10,
                IntBuffer var11, double var12, double var14, DoubleBuffer var16)
        {
            DoubleBuffer var17 = this.inBuf;
            DoubleBuffer var18 = this.outBuf;
            this.__gluMultMatricesd(var9, var10, this.matrixBuf);
            if (!this.__gluInvertMatrixd(this.matrixBuf, this.matrixBuf))
            {
                return false;
            }
            else
            {
                var17.put(0, var1);
                var17.put(1, var3);
                var17.put(2, var5);
                var17.put(3, var7);
                int var19 = var11.position();
                var17.put(0, (var17.get(0) - (double) var11.get(0 + var19)) / (double) var11.get(2 + var19));
                var17.put(1, (var17.get(1) - (double) var11.get(1 + var19)) / (double) var11.get(3 + var19));
                var17.put(2, (var17.get(2) - var12) / (var14 - var12));
                var17.put(0, var17.get(0) * 2.0D - 1.0D);
                var17.put(1, var17.get(1) * 2.0D - 1.0D);
                var17.put(2, var17.get(2) * 2.0D - 1.0D);
                this.__gluMultMatrixVecd(this.matrixBuf, var17, var18);
                if (var18.get(3) == 0.0D)
                {
                    return false;
                }
                else
                {
                    int var20 = var16.position();
                    var16.put(0 + var20, var18.get(0));
                    var16.put(1 + var20, var18.get(1));
                    var16.put(2 + var20, var18.get(2));
                    var16.put(3 + var20, var18.get(3));
                    return true;
                }
            }
        }
    }

    private static final Project PROJECT                        = new Project();
    public static final  int     GLU_BEGIN                      = 100100;
    public static final  int     GLU_CCW                        = 100121;
    public static final  int     GLU_CW                         = 100120;
    public static final  int     GLU_EDGE_FLAG                  = 100104;
    public static final  int     GLU_END                        = 100102;
    public static final  int     GLU_ERROR                      = 100103;
    public static final  int     GLU_EXTENSIONS                 = 100801;
    public static final  int     GLU_EXTERIOR                   = 100123;
    public static final  int     GLU_FALSE                      = 0;
    public static final  int     GLU_FILL                       = 100012;
    public static final  int     GLU_FLAT                       = 100001;
    public static final  int     GLU_INSIDE                     = 100021;
    public static final  int     GLU_INTERIOR                   = 100122;
    public static final  int     GLU_INVALID_ENUM               = 100900;
    public static final  int     GLU_INVALID_OPERATION          = 100904;
    public static final  int     GLU_INVALID_VALUE              = 100901;
    public static final  int     GLU_LINE                       = 100011;
    public static final  int     GLU_NONE                       = 100002;
    public static final  int     GLU_OUTSIDE                    = 100020;
    public static final  int     GLU_OUT_OF_MEMORY              = 100902;
    public static final  int     GLU_POINT                      = 100010;
    public static final  int     GLU_SILHOUETTE                 = 100013;
    public static final  int     GLU_SMOOTH                     = 100000;
    public static final  int     GLU_TESS_BEGIN                 = 100100;
    public static final  int     GLU_TESS_BEGIN_DATA            = 100106;
    public static final  int     GLU_TESS_BOUNDARY_ONLY         = 100141;
    public static final  int     GLU_TESS_COMBINE               = 100105;
    public static final  int     GLU_TESS_COMBINE_DATA          = 100111;
    public static final  int     GLU_TESS_COORD_TOO_LARGE       = 100155;
    public static final  int     GLU_TESS_EDGE_FLAG             = 100104;
    public static final  int     GLU_TESS_EDGE_FLAG_DATA        = 100110;
    public static final  int     GLU_TESS_END                   = 100102;
    public static final  int     GLU_TESS_END_DATA              = 100108;
    public static final  int     GLU_TESS_ERROR                 = 100103;
    public static final  int     GLU_TESS_ERROR1                = 100151;
    public static final  int     GLU_TESS_ERROR2                = 100152;
    public static final  int     GLU_TESS_ERROR3                = 100153;
    public static final  int     GLU_TESS_ERROR4                = 100154;
    public static final  int     GLU_TESS_ERROR5                = 100155;
    public static final  int     GLU_TESS_ERROR6                = 100156;
    public static final  int     GLU_TESS_ERROR7                = 100157;
    public static final  int     GLU_TESS_ERROR8                = 100158;
    public static final  int     GLU_TESS_ERROR_DATA            = 100109;
    public static final  double  GLU_TESS_MAX_COORD             = 1.0E150D;
    public static final  int     GLU_TESS_MISSING_BEGIN_CONTOUR = 100152;
    public static final  int     GLU_TESS_MISSING_BEGIN_POLYGON = 100151;
    public static final  int     GLU_TESS_MISSING_END_CONTOUR   = 100154;
    public static final  int     GLU_TESS_MISSING_END_POLYGON   = 100153;
    public static final  int     GLU_TESS_NEED_COMBINE_CALLBACK = 100156;
    public static final  int     GLU_TESS_TOLERANCE             = 100142;
    public static final  int     GLU_TESS_VERTEX                = 100101;
    public static final  int     GLU_TESS_VERTEX_DATA           = 100107;
    public static final  int     GLU_TESS_WINDING_ABS_GEQ_TWO   = 100134;
    public static final  int     GLU_TESS_WINDING_NEGATIVE      = 100133;
    public static final  int     GLU_TESS_WINDING_NONZERO       = 100131;
    public static final  int     GLU_TESS_WINDING_ODD           = 100130;
    public static final  int     GLU_TESS_WINDING_POSITIVE      = 100132;
    public static final  int     GLU_TESS_WINDING_RULE          = 100140;
    public static final  int     GLU_TRUE                       = 1;
    public static final  int     GLU_UNKNOWN                    = 100124;
    public static final  int     GLU_VERSION                    = 100800;
    public static final  int     GLU_VERTEX                     = 100101;
    public static final  String  extensionString                = "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";
    public static final  String  versionString                  = "1.3";

    public static void gluLookAt(
            double var2, double var4, double var6, double var8, double var10, double var12, double var14,
            double var16, double var18)
    {
        GLU.PROJECT.gluLookAt(var2, var4, var6, var8, var10, var12, var14, var16, var18);
    }

    public static void gluOrtho2D(double var2, double var4, double var6, double var8)
    {
        GLU.PROJECT.gluOrtho2D(var2, var4, var6, var8);
    }

    /**
     * Compute and apply a matrix to have a perspective view of the scene
     *
     * @param angle Angle (in degree) for vanishing point
     * @param ratio View ratio (usually width/height)
     * @param nearZ Value of Z near the screen
     * @param farZ  Value of Z in depth
     */
    public static void gluPerspective(double angle, double ratio, double nearZ, double farZ)
    {
        GLU.PROJECT.gluPerspective(angle, ratio, nearZ, farZ);
    }

    public static void gluPickMatrix(double var2, double var4, double var6, double var8, int[] var10, int var11)
    {
        GLU.PROJECT.gluPickMatrix(var2, var4, var6, var8, var10, var11);
    }

    public static void gluPickMatrix(double var2, double var4, double var6, double var8, IntBuffer var10)
    {
        GLU.PROJECT.gluPickMatrix(var2, var4, var6, var8, var10);
    }

    public static boolean gluProject(
            double var1, double var3, double var5, double[] var7, int var8, double[] var9, int var10, int[] var11,
            int var12, double[] var13, int var14)
    {
        return GLU.PROJECT.gluProject(var1, var3, var5, var7, var8, var9, var10, var11, var12, var13, var14);
    }

    public static boolean gluUnProject(
            double var1, double var3, double var5, double[] var7, int var8, double[] var9, int var10, int[] var11,
            int var12, double[] var13, int var14)
    {
        return GLU.PROJECT.gluUnProject(var1, var3, var5, var7, var8, var9, var10, var11, var12, var13, var14);
    }

    public static boolean gluUnProject(
            double var1, double var3, double var5, DoubleBuffer var7, DoubleBuffer var8, IntBuffer var9,
            DoubleBuffer var10)
    {
        return GLU.PROJECT.gluUnProject(var1, var3, var5, var7, var8, var9, var10);
    }

    public static boolean gluUnProject4(
            double var1, double var3, double var5, double var7, double[] var9, int var10, double[] var11, int var12,
            int[] var13, int var14, double var15, double var17, double[] var19, int var20)
    {
        return GLU.PROJECT.gluUnProject4(var1, var3, var5, var7, var9, var10, var11, var12, var13, var14, var15, var17,
                                         var19, var20);
    }

    public static boolean gluUnProject4(
            double var1, double var3, double var5, double var7, DoubleBuffer var9, DoubleBuffer var10,
            IntBuffer var11, double var12, double var14, DoubleBuffer var16)
    {
        return GLU.PROJECT.gluUnProject4(var1, var3, var5, var7, var9, var10, var11, var12, var14, var16);
    }

    public boolean gluProject(
            double var1, double var3, double var5, DoubleBuffer var7, DoubleBuffer var8, IntBuffer var9,
            DoubleBuffer var10)
    {
        return GLU.PROJECT.gluProject(var1, var3, var5, var7, var8, var9, var10);
    }
}
