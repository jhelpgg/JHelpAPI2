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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.vectorial.math;

import jhelp.util.math.Math2;
import jhelp.util.util.HashCode;

/**
 * A matrix
 *
 * @author JHelp
 */
public class Matrix
{
    /**
     * Create a rotation matrix around (0, 0)
     *
     * @param angle Rotation angle
     * @return Rotation matrix
     */
    public static Matrix obtainRotateMatrix(final Angle angle)
    {
        return Matrix.obtainRotateMatrix(angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Create a rotation matrix around (0, 0)
     *
     * @param angle Rotation angle
     * @return Rotation matrix
     */
    public static Matrix obtainRotateMatrix(final double angle)
    {
        final Matrix matrix = new Matrix();
        matrix.toRotate(angle);
        return matrix;
    }

    /**
     * Create a rotation matrix
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     * @return Rotation matrix
     */
    public static Matrix obtainRotateMatrix(final double centerX, final double centerY, final Angle angle)
    {
        return Matrix.obtainRotateMatrix(centerX, centerY, angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Create a rotation matrix
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     * @return Rotation matrix
     */
    public static Matrix obtainRotateMatrix(final double centerX, final double centerY, final double angle)
    {
        final Matrix matrix = new Matrix();
        matrix.toRotate(centerX, centerY, angle);
        return matrix;
    }

    /**
     * Create scale matrix
     *
     * @param scale Scale on X and Y
     * @return Scale matrix
     */
    public static Matrix obtainScaleMatrix(final double scale)
    {
        return Matrix.obtainScaleMatrix(scale, scale);
    }

    /**
     * Create scale matrix
     *
     * @param sx Scale on X
     * @param sy Scale on Y
     * @return Scale matrix
     */
    public static Matrix obtainScaleMatrix(final double sx, final double sy)
    {
        final Matrix matrix = new Matrix();
        matrix.toScale(sx, sy);
        return matrix;
    }

    /**
     * Create translate matrix
     *
     * @param tx Translate in X
     * @param ty Translate in Y
     * @return Translate matrix
     */
    public static Matrix obtainTranslateMatrix(final double tx, final double ty)
    {
        final Matrix matrix = new Matrix();
        matrix.toTranslate(tx, ty);
        return matrix;
    }

    /**
     * Matrix determinant
     */
    private double determinant;
    /**
     * Matrix position 0,0
     */
    private double matrix00;
    /**
     * Matrix position 0,1
     */
    private double matrix01;
    /**
     * Matrix position 0,2
     */
    private double matrix02;
    /**
     * Matrix position 1,0
     */
    private double matrix10;
    /**
     * Matrix position 1,1
     */
    private double matrix11;
    /**
     * Matrix position 1,2
     */
    private double matrix12;
    /**
     * Matrix position 2,0
     */
    private double matrix20;
    /**
     * Matrix position 2,1
     */
    private double matrix21;
    /**
     * Matrix position 2,2
     */
    private double matrix22;

    /**
     * Create a new instance of Matrix identity
     */
    public Matrix()
    {
        this.toIdentity();
    }

    /**
     * Create a new instance of Matrix must be invertible
     *
     * @param matrix00 Matrix position 0,0
     * @param matrix10 Matrix position 1,0
     * @param matrix20 Matrix position 2,0
     * @param matrix01 Matrix position 0,1
     * @param matrix11 Matrix position 1,1
     * @param matrix21 Matrix position 2,1
     */
    public Matrix(
            final double matrix00, final double matrix10, final double matrix20, final double matrix01,
            final double matrix11, final double matrix21)
    {
        this.matrix00 = matrix00;
        this.matrix10 = matrix10;
        this.matrix20 = matrix20;
        this.matrix01 = matrix01;
        this.matrix11 = matrix11;
        this.matrix21 = matrix21;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = (matrix00 * matrix11) - (matrix01 * matrix10);

        if (Math2.isNul(this.determinant) == true)
        {
            throw new IllegalArgumentException("The matrix MUST be invertible !");
        }
    }

    /**
     * Copy a matrix
     *
     * @param matrix Matrix to copy
     */
    public void copy(final Matrix matrix)
    {
        this.matrix00 = matrix.matrix00;
        this.matrix10 = matrix.matrix10;
        this.matrix20 = matrix.matrix20;
        this.matrix01 = matrix.matrix01;
        this.matrix11 = matrix.matrix11;
        this.matrix21 = matrix.matrix21;
        this.matrix02 = matrix.matrix02;
        this.matrix12 = matrix.matrix12;
        this.matrix22 = matrix.matrix22;

        this.determinant = matrix.determinant;
    }

    /**
     * Create matrix copy
     *
     * @return Matrix copy
     */
    public Matrix createCopy()
    {
        final Matrix copy = new Matrix();

        copy.matrix00 = this.matrix00;
        copy.matrix10 = this.matrix10;
        copy.matrix20 = this.matrix20;
        copy.matrix01 = this.matrix01;
        copy.matrix11 = this.matrix11;
        copy.matrix21 = this.matrix21;
        copy.matrix02 = this.matrix02;
        copy.matrix12 = this.matrix12;
        copy.matrix22 = this.matrix22;

        copy.determinant = this.determinant;

        return copy;
    }

    /**
     * Matrix determinant
     *
     * @return Matrix determinant
     */
    public double getDeterminant()
    {
        return this.determinant;
    }

    /**
     * Hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.determinant, this.matrix00, this.matrix10, this.matrix20, this.matrix01,
                                        this.matrix11, this.matrix21,
                                        this.matrix02, this.matrix12, this.matrix22);
    }

    /**
     * Indicates if given object equals to this matrix <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this matrix
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }

        if (object == null)
        {
            return false;
        }

        if ((object instanceof Matrix) == false)
        {
            return false;
        }

        final Matrix matrix = (Matrix) object;

        return (Math2.equals(this.determinant, matrix.determinant) == true) //
               && (Math2.equals(this.matrix00, matrix.matrix00) == true) //
               && (Math2.equals(this.matrix10, matrix.matrix10) == true) //
               && (Math2.equals(this.matrix20, matrix.matrix20) == true) //
               && (Math2.equals(this.matrix01, matrix.matrix01) == true) //
               && (Math2.equals(this.matrix11, matrix.matrix11) == true) //
               && (Math2.equals(this.matrix21, matrix.matrix21) == true) //
               && (Math2.equals(this.matrix02, matrix.matrix02) == true) //
               && (Math2.equals(this.matrix12, matrix.matrix12) == true) //
               && (Math2.equals(this.matrix22, matrix.matrix22) == true) //
                ;
    }

    /**
     * Invert the matrix.<br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix invert
     */
    public Matrix invert(Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |A B C| |a b c| |1 0 0|
         * |D E F|x|d e f|=|0 1 0|
         * |G H I| |g h i| |0 0 1|
         *
         * Z = A(EI-FH)-B(DI-FG)+C(DH-EG) = AEI-AFH-BDI+BFG+CDH-CEG (determinant)
         *
         * a = (EI-FH)/Z
         * b = -(BI-CH)/Z
         * c = (BF-EC)/Z
         * d = -(DI-FG)/Z
         * e = (AI-CG)/Z
         * f = -(AF-CD)/Z
         * g = (DH-EG)/Z
         * h = -(AH-BG)/Z
         * i = (AE-BD)/Z
         *
         * </pre>
         */

        final double m00 = ((this.matrix11 * this.matrix22) - (this.matrix21 * this.matrix12)) / this.determinant;
        final double m10 = ((this.matrix20 * this.matrix12) - (this.matrix10 * this.matrix22)) / this.determinant;
        final double m20 = ((this.matrix10 * this.matrix21) - (this.matrix11 * this.matrix20)) / this.determinant;
        final double m01 = ((this.matrix21 * this.matrix02) - (this.matrix01 * this.matrix22)) / this.determinant;
        final double m11 = ((this.matrix00 * this.matrix22) - (this.matrix20 * this.matrix02)) / this.determinant;
        final double m21 = ((this.matrix20 * this.matrix01) - (this.matrix00 * this.matrix21)) / this.determinant;
        final double m02 = ((this.matrix01 * this.matrix12) - (this.matrix11 * this.matrix02)) / this.determinant;
        final double m12 = ((this.matrix10 * this.matrix02) - (this.matrix00 * this.matrix12)) / this.determinant;
        final double m22 = ((this.matrix00 * this.matrix11) - (this.matrix10 * this.matrix01)) / this.determinant;

        result.matrix00 = m00;
        result.matrix10 = m10;
        result.matrix20 = m20;
        result.matrix01 = m01;
        result.matrix11 = m11;
        result.matrix21 = m21;
        result.matrix02 = m02;
        result.matrix12 = m12;
        result.matrix22 = m22;

        result.determinant = 1 / this.determinant;

        return result;
    }

    /**
     * Multiply matrix by an other one.<br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param matrix Matrix to multiply
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Multiplication result
     */
    public Matrix multiply(final Matrix matrix, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        final double m00 = (this.matrix00 * matrix.matrix00) + (this.matrix10 * matrix.matrix01) +
                           (this.matrix20 * matrix.matrix02);
        final double m10 = (this.matrix00 * matrix.matrix10) + (this.matrix10 * matrix.matrix11) +
                           (this.matrix20 * matrix.matrix12);
        final double m20 = (this.matrix00 * matrix.matrix20) + (this.matrix10 * matrix.matrix21) +
                           (this.matrix20 * matrix.matrix22);
        final double m01 = (this.matrix01 * matrix.matrix00) + (this.matrix11 * matrix.matrix01) +
                           (this.matrix21 * matrix.matrix02);
        final double m11 = (this.matrix01 * matrix.matrix10) + (this.matrix11 * matrix.matrix11) +
                           (this.matrix21 * matrix.matrix12);
        final double m21 = (this.matrix01 * matrix.matrix20) + (this.matrix11 * matrix.matrix21) +
                           (this.matrix21 * matrix.matrix22);
        final double m02 = (this.matrix02 * matrix.matrix00) + (this.matrix12 * matrix.matrix01) +
                           (this.matrix22 * matrix.matrix02);
        final double m12 = (this.matrix02 * matrix.matrix10) + (this.matrix12 * matrix.matrix11) +
                           (this.matrix22 * matrix.matrix12);
        final double m22 = (this.matrix02 * matrix.matrix20) + (this.matrix12 * matrix.matrix21) +
                           (this.matrix22 * matrix.matrix22);

        result.matrix00 = m00;
        result.matrix10 = m10;
        result.matrix20 = m20;
        result.matrix01 = m01;
        result.matrix11 = m11;
        result.matrix21 = m21;
        result.matrix02 = m02;
        result.matrix12 = m12;
        result.matrix22 = m22;

        result.determinant = this.determinant * matrix.determinant;

        return result;
    }

    /**
     * Add rotation <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Rotation angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix rotate(final Angle angle, final Matrix result)
    {
        return this.rotate(angle.convert(AngleUnit.RADIAN).getAngle(), result);
    }

    /**
     * Add rotation <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     * @param result  Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix rotate(final double centerX, final double centerY, final Angle angle, final Matrix result)
    {
        return this.rotate(centerX, centerY, angle.convert(AngleUnit.RADIAN).getAngle(), result);
    }

    /**
     * Add rotation <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     * @param result  Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix rotate(final double centerX, final double centerY, final double angle, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |m00 m10 m20| | c s cx+sy-x| |m00*c-m10*s m00*s+m10*c m00(cx+sy-x)+m10(cy-sx-y)+m20|
         * |m01 m11 m21|*|-s c cy-sx-y|=|m01*c-m11*s m01*s+m11*c m01(cx+sy-x)+m11(cy-sx-y)+m21|
         * |m02 m12 m22| | 0 0    1   | |m02*c-m12*s m02*s+m12*c m02(cx+sy-x)+m12(cy-sx-y)+m22|
         * </pre>
         */

        final double cos   = Math.cos(angle);
        final double sin   = Math.sin(angle);
        final double rot20 = ((cos * centerX) + (sin * centerY)) - centerX;
        final double rot21 = (cos * centerY) - (sin * centerX) - centerY;

        final double m00 = (this.matrix00 * cos) - (this.matrix10 * sin);
        final double m10 = (this.matrix00 * sin) + (this.matrix10 * cos);
        final double m20 = (this.matrix00 * rot20) + (this.matrix10 * rot21) + this.matrix20;
        final double m01 = (this.matrix01 * cos) - (this.matrix11 * sin);
        final double m11 = (this.matrix01 * sin) + (this.matrix11 * cos);
        final double m21 = (this.matrix01 * rot20) + (this.matrix11 * rot21) + this.matrix21;
        final double m02 = (this.matrix02 * cos) - (this.matrix12 * sin);
        final double m12 = (this.matrix02 * sin) + (this.matrix12 * cos);
        final double m22 = (this.matrix02 * rot20) + (this.matrix12 * rot21) + this.matrix22;

        result.matrix00 = m00;
        result.matrix10 = m10;
        result.matrix20 = m20;
        result.matrix01 = m01;
        result.matrix11 = m11;
        result.matrix21 = m21;
        result.matrix02 = m02;
        result.matrix12 = m12;
        result.matrix22 = m22;

        result.determinant = this.determinant;

        return result;
    }

    /**
     * Add rotation <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Rotation angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix rotate(final double angle, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |m00 m10 m20| | cos sin 0| |m00*cos-m10*sin m00*sin+m10*cos m20|
         * |m01 m11 m21|*|-sin cos 0|=|m01*cos-m11*sin m01*sin+m11*cos m21|
         * |m02 m12 m22| |  0   0  1| |m02*cos-m12*sin m02*sin+m12*cos m22|
         * </pre>
         */

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);

        final double m00 = (this.matrix00 * cos) - (this.matrix10 * sin);
        final double m10 = (this.matrix00 * sin) + (this.matrix10 * cos);
        final double m20 = this.matrix20;
        final double m01 = (this.matrix01 * cos) - (this.matrix11 * sin);
        final double m11 = (this.matrix01 * sin) + (this.matrix11 * cos);
        final double m21 = this.matrix21;
        final double m02 = (this.matrix02 * cos) - (this.matrix12 * sin);
        final double m12 = (this.matrix02 * sin) + (this.matrix12 * cos);
        final double m22 = this.matrix22;

        result.matrix00 = m00;
        result.matrix10 = m10;
        result.matrix20 = m20;
        result.matrix01 = m01;
        result.matrix11 = m11;
        result.matrix21 = m21;
        result.matrix02 = m02;
        result.matrix12 = m12;
        result.matrix22 = m22;

        result.determinant = this.determinant;

        return result;
    }

    /**
     * Add scale <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param scaleX Scale X
     * @param scaleY Scale Y
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix scale(final double scaleX, final double scaleY, Matrix result)
    {
        if ((Math2.isNul(scaleX) == true) || (Math2.isNul(scaleY) == true))
        {
            throw new IllegalArgumentException("Scale can't be zero");
        }

        if (result == null)
        {
            result = new Matrix();
        }

        result.copy(this);

        result.matrix00 *= scaleX;
        result.matrix11 *= scaleY;
        result.determinant = this.determinant * scaleX * scaleY;

        return result;
    }

    /**
     * Add scale <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param scale  Scale
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix scale(final double scale, final Matrix result)
    {
        return this.scale(scale, scale, result);
    }

    /**
     * Add shear <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param shearX Shear X
     * @param shearY Shear Y
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix shear(final double shearX, final double shearY, Matrix result)
    {
        if (Math2.equals(shearX * shearY, 1) == true)
        {
            throw new IllegalArgumentException("shearX *shearY mustn't be 1");
        }

        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |m00 m10 m20| | 1 sx 0| |m00+m10*sy m00*sx+m10 m20|
         * |m01 m11 m21|x|sy  1 0|=|m01+m11*sy m01*sx+m11 m21|
         * |m02 m12 m22| | 0  0 1| |m02+m12*sy m02*sx+m12 m22|
         * </pre>
         */

        final double m00 = this.matrix00 + (this.matrix10 * shearY);
        final double m10 = (this.matrix00 * shearX) + this.matrix10;
        final double m20 = this.matrix20;
        final double m01 = this.matrix01 + (this.matrix11 * shearY);
        final double m11 = (this.matrix01 * shearX) + this.matrix11;
        final double m21 = this.matrix21;
        final double m02 = this.matrix02 + (this.matrix12 * shearY);
        final double m12 = (this.matrix02 * shearX) + this.matrix12;
        final double m22 = this.matrix22;

        result.matrix00 = m00;
        result.matrix10 = m10;
        result.matrix20 = m20;
        result.matrix01 = m01;
        result.matrix11 = m11;
        result.matrix21 = m21;
        result.matrix02 = m02;
        result.matrix12 = m12;
        result.matrix22 = m22;

        result.determinant = this.determinant * (1 - (shearX * shearY));

        return result;
    }

    /**
     * Add shear <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param shear  Shear
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix shear(final double shear, final Matrix result)
    {
        if (Math2.equals(shear, 1) == true)
        {
            throw new IllegalArgumentException("shear mustn't be 1");
        }

        return this.shear(shear, shear, result);
    }

    /**
     * Add skew in X <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix skewX(final Angle angle, final Matrix result)
    {
        return this.skewX(angle.convert(AngleUnit.RADIAN).getAngle(), result);
    }

    /**
     * Add skew in X <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix skewX(final double angle, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |m00 m10 m20| |1 tan 0| |m00 m00*tan+m10 m20|
         * |m01 m11 m21|x|0  1  0|=|m01 m01*tan+m11 m21|
         * |m02 m12 m22| |0  0  1| |m02 m02*tan+m12 m22|
         * </pre>
         */

        final double tan = Math.tan(angle);
        result.copy(this);

        result.matrix10 += this.matrix00 * tan;
        result.matrix11 += this.matrix01 * tan;
        result.matrix12 += this.matrix02 * tan;

        result.determinant = this.determinant;

        return result;
    }

    /**
     * Add skew in Y <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix skewY(final Angle angle, final Matrix result)
    {
        return this.skewY(angle.convert(AngleUnit.RADIAN).getAngle(), result);
    }

    /**
     * Add skew in Y <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param angle  Angle
     * @param result Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix skewY(final double angle, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        /**
         * <pre>
         * |m00 m10 m20| | 1  0 0| |m00+m10*tan m10 m20|
         * |m01 m11 m21|x|tan 1 0|=|m01+m11*tan m11 m21|
         * |m02 m12 m22| | 0  0 1| |m02+m12*tan m02 m22|
         * </pre>
         */

        final double tan = Math.tan(angle);
        result.copy(this);

        result.matrix00 += this.matrix10 * tan;
        result.matrix01 += this.matrix11 * tan;
        result.matrix02 += this.matrix12 * tan;

        result.determinant = this.determinant;

        return result;
    }

    /**
     * Put matrix to identity
     */
    public void toIdentity()
    {
        this.matrix00 = 1;
        this.matrix10 = 0;
        this.matrix20 = 0;
        this.matrix01 = 0;
        this.matrix11 = 1;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform matrix to rotation around (0,0)
     *
     * @param angle Rotation angle
     */
    public void toRotate(final Angle angle)
    {
        this.toRotate(angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Transform matrix to rotation around (0,0)
     *
     * @param angle Rotation angle
     */
    public void toRotate(final double angle)
    {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);

        this.matrix00 = cos;
        this.matrix10 = sin;
        this.matrix20 = 0;
        this.matrix01 = -sin;
        this.matrix11 = cos;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform matrix to rotation
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     */
    public void toRotate(final double centerX, final double centerY, final Angle angle)
    {
        this.toRotate(centerX, centerY, angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Transform matrix to rotation
     *
     * @param centerX Rotation center X
     * @param centerY Rotation center Y
     * @param angle   Rotation angle
     */
    public void toRotate(final double centerX, final double centerY, final double angle)
    {
        /**
         * <pre>
         * x=centerX;
         * y=centerY;
         * c=cos(angle)
         * s=sin(angle)
         *
         *     |1 0 -x| | c s 0| |1 0 x|
         * R = |0 1 -y|x|-s c 0|x|0 1 y|
         *     |0 0  1| | 0 0 1| |0 0 1|
         *
         *     | c s -x| |1 0 x|
         * R = |-s c -y|x|0 1 y|
         *     | 0 0  1| |0 0 1|
         *
         *     | c s cx+sy-x|
         * R = |-s c cy-sx-y|
         *     | 0 0    1   |
         *
         * </pre>
         */

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);

        this.matrix00 = cos;
        this.matrix10 = sin;
        this.matrix20 = ((cos * centerX) + (sin * centerY)) - centerX;
        this.matrix01 = -sin;
        this.matrix11 = cos;
        this.matrix21 = (cos * centerY) - (sin * centerX) - centerY;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform matrix to scale
     *
     * @param scale Scale
     */
    public void toScale(final double scale)
    {
        this.toScale(scale, scale);
    }

    /**
     * Transform matrix to scale
     *
     * @param scaleX Scale in X
     * @param scaleY Scale in Y
     */
    public void toScale(final double scaleX, final double scaleY)
    {
        if ((Math2.isNul(scaleX) == true) || (Math2.isNul(scaleY) == true))
        {
            throw new IllegalArgumentException("Scale can't be zero");
        }

        this.matrix00 = scaleX;
        this.matrix10 = 0;
        this.matrix20 = 0;
        this.matrix01 = 0;
        this.matrix11 = scaleY;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = scaleX * scaleY;
    }

    /**
     * Transform matrix to shear
     *
     * @param shear Shear
     */
    public void toShear(final double shear)
    {
        if (Math2.equals(shear, 1) == true)
        {
            throw new IllegalArgumentException("shear mustn't be 1");
        }

        this.toShear(shear, shear);
    }

    /**
     * Transform matrix to shear
     *
     * @param shearX Shear in X
     * @param shearY Shear in Y
     */
    public void toShear(final double shearX, final double shearY)
    {
        if (Math2.equals(shearX * shearY, 1) == true)
        {
            throw new IllegalArgumentException("shearX *shearY mustn't be 1");
        }

        this.matrix00 = 1;
        this.matrix10 = shearX;
        this.matrix20 = 0;
        this.matrix01 = shearY;
        this.matrix11 = 1;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1 - (shearX * shearY);
    }

    /**
     * Transform to skew in X
     *
     * @param angle Angle
     */
    public void toSkewX(final Angle angle)
    {
        this.toSkewX(angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Transform to skew in X
     *
     * @param angle Angle
     */
    public void toSkewX(final double angle)
    {
        this.matrix00 = 1;
        this.matrix10 = Math.tan(angle);
        this.matrix20 = 0;
        this.matrix01 = 0;
        this.matrix11 = 1;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform to skew in Y
     *
     * @param angle Angle
     */
    public void toSkewY(final Angle angle)
    {
        this.toSkewY(angle.convert(AngleUnit.RADIAN).getAngle());
    }

    /**
     * Transform to skew in Y
     *
     * @param angle Angle
     */
    public void toSkewY(final double angle)
    {
        this.matrix00 = 1;
        this.matrix10 = 0;
        this.matrix20 = 0;
        this.matrix01 = Math.tan(angle);
        this.matrix11 = 1;
        this.matrix21 = 0;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform to translate
     *
     * @param translateX Translate X
     * @param translateY Translate Y
     */
    public void toTranslate(final double translateX, final double translateY)
    {
        this.matrix00 = 1;
        this.matrix10 = 0;
        this.matrix20 = translateX;
        this.matrix01 = 0;
        this.matrix11 = 1;
        this.matrix21 = translateY;
        this.matrix02 = 0;
        this.matrix12 = 0;
        this.matrix22 = 1;

        this.determinant = 1;
    }

    /**
     * Transform point throw matrix.<br>
     * The source and result can be the same instance
     *
     * @param source Point to transform
     * @param result Point where write result, if {@code null} a new instance is created
     * @return Transform result
     */
    public Point transform(final Point source, Point result)
    {
        if (result == null)
        {
            result = new Point(0, 0);
        }

        final double x = source.getX();
        final double y = source.getY();
        result.setCoordinate(//
                             (this.matrix00 * x) + (this.matrix10 * y) + this.matrix20//
                , (this.matrix01 * x) + (this.matrix11 * y) + this.matrix21//
                            );

        return result;
    }

    /**
     * Add translation matrix <br>
     * The result can be the matrix itself, the computing is safe
     *
     * @param translateX Translate X
     * @param translateY Translate Y
     * @param result     Matrix where write the result, if {@code null} a new matrix is created
     * @return Matrix result
     */
    public Matrix translate(final double translateX, final double translateY, Matrix result)
    {
        if (result == null)
        {
            result = new Matrix();
        }

        result.copy(this);
        result.matrix20 += translateX;
        result.matrix21 += translateY;
        result.determinant = this.determinant;

        return result;
    }
}