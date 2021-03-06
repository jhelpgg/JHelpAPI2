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

package jhelp.util.math;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.HashCode;

/**
 * Matrix that paralleling in thread long operation, so multiplication, take adjacent, take inverse are fast.<br>
 * For determinant, the used algorithm is already fast
 *
 * @author JHelp
 */
@SuppressWarnings("SuspiciousNameCombination")
public final class Matrix
{
    /**
     * Precision used in computing
     */
    public static final double PRECISION = 1e-5;

    /**
     * Indicates if 2 real are enough near to be considered as equals
     *
     * @param real1 First
     * @param real2 Second
     * @return {@code true} if equals
     */
    public static boolean equals(final double real1, final double real2)
    {
        return Math.abs(real1 - real2) <= Matrix.PRECISION;
    }

    /**
     * Indicates if a real is enough small to be consider as zero
     *
     * @param real Real to test
     * @return {@code true} if consider as zero
     */
    public static boolean isNul(final double real)
    {
        return Math.abs(real) <= Matrix.PRECISION;
    }

    /**
     * The matrix determinant
     */
    private       double   determinant;
    /**
     * Indicates if determinant is already computed and still accurate (No need to compute it again)
     */
    private       boolean  determinantKnown;
    /**
     * Matrix height
     */
    private final int      height;
    /**
     * Matrix itself
     */
    private final double[] matrix;
    /**
     * Matrix size
     */
    private final int      size;
    /**
     * Matrix width
     */
    private final int      width;

    /**
     * Create a new instance of Matrix full of zero
     *
     * @param width  Matrix width
     * @param height Matrix height
     */
    public Matrix(final int width, final int height)
    {
        if ((width < 1) || (height < 1))
        {
            throw new IllegalArgumentException("Width and height must be > 0 not " + width + "x" + height);
        }

        this.width = width;
        this.height = height;
        this.size = this.width * this.height;
        this.matrix = new double[this.size];
        this.determinantKnown = width == height;
        this.determinant = 0;
    }

    /**
     * Check if position is inside the matrix
     *
     * @param x X
     * @param y Y
     * @throws IllegalArgumentException If position outside the matrix
     */
    private void check(final int x, final int y)
    {
        if ((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
        {
            throw new IllegalArgumentException(
                    "x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ not (" + x + ", " + y +
                    ")");
        }
    }

    /**
     * Compute the determinant (Here we have already checked that determinant need to be computed and matrix is square)
     *
     * @return Determinant
     */
    private double determinantInternal()
    {
        if (this.width == 1)
        {
            this.determinantKnown = true;
            this.determinant = this.matrix[0];
            return this.determinant;
        }

        if (this.width == 2)
        {
            this.determinantKnown = true;
            this.determinant = (this.matrix[0] * this.matrix[3]) - (this.matrix[2] * this.matrix[1]);
            return this.determinant;
        }

        return this.determinantInternalMore2();
    }

    /**
     * Compute determinant for matrix bigger than 2x2
     *
     * @return Computed determinant
     */
    private double determinantInternalMore2()
    {
        final Matrix work        = this.copy();
        double       determinant = 1;
        int          y2;
        int          diagonal    = 0;
        final int    more        = this.width + 1;
        double       diagonalValue;

        for (int y = 0; y < this.height; y++)
        {
            y2 = y;
            diagonalValue = work.matrix[diagonal];

            while (Matrix.isNul(diagonalValue))
            {
                y2++;

                if (y2 >= this.height)
                {
                    this.determinantKnown = true;
                    this.determinant = 0;
                    return 0;
                }

                determinant *= -1;
                work.interExchangeRowInternal(y, y2);
                diagonalValue = work.matrix[diagonal];
            }

            determinant *= diagonalValue;
            work.pivotInternal(y);
            diagonal += more;
        }

        this.determinantKnown = true;
        this.determinant = determinant;
        return determinant;
    }

    /**
     * Exchange 2 row.<br>
     * The determinant not change except by the sign (y1 and y2 are inside matrix and different)
     *
     * @param y1 First row
     * @param y2 Second row
     */
    private void interExchangeRowInternal(final int y1, final int y2)
    {
        final int      line1 = y1 * this.width;
        final int      line2 = y2 * this.width;
        final double[] temp  = new double[this.width];
        System.arraycopy(this.matrix, line1, temp, 0, this.width);
        System.arraycopy(this.matrix, line2, this.matrix, line1, this.width);
        System.arraycopy(temp, 0, this.matrix, line2, this.width);
        this.determinant *= -1;
    }

    /**
     * Pivot matrix from one diagonal point
     *
     * @param xy X and Y coordinate of diagonal point
     */
    private void pivotInternal(final int xy)
    {
        final int    startY = xy * this.width;
        int          y      = startY + this.width;
        int          destination;
        int          source;
        double       coefficient;
        final double div    = this.matrix[startY + xy];

        for (int yy = xy + 1; yy < this.height; yy++)
        {
            coefficient = this.matrix[y + xy];

            if (!Matrix.isNul(coefficient))
            {
                destination = y;
                source = startY;
                coefficient /= div;

                for (int xx = 0; xx < this.width; xx++)
                {
                    this.matrix[destination++] -= coefficient * this.matrix[source++];
                }
            }

            y += this.width;
        }

        this.determinantKnown = false;
    }

    /**
     * Extract a sub matrix by remove one column and one row
     *
     * @param removedColumn Column to remove
     * @param removedRow    Row to remove
     * @return Extracted matrix
     */
    private Matrix subMatrix(final int removedColumn, final int removedRow)
    {
        final int    w               = this.width - 1;
        final Matrix result          = new Matrix(w, this.height - 1);
        int          lineSource      = 0;
        int          lineDestination = 0;
        final int    x               = removedColumn + 1;
        final int    left            = w - removedColumn;

        for (int y = 0; y < removedRow; y++)
        {
            System.arraycopy(this.matrix, lineSource, result.matrix, lineDestination, removedColumn);
            System.arraycopy(this.matrix, lineSource + x, result.matrix, lineDestination + removedColumn, left);
            lineSource += this.width;
            lineDestination += w;
        }

        lineSource += this.width;

        for (int y = removedRow + 1; y < this.height; y++)
        {
            System.arraycopy(this.matrix, lineSource, result.matrix, lineDestination, removedColumn);
            System.arraycopy(this.matrix, lineSource + x, result.matrix, lineDestination + removedColumn, left);
            lineSource += this.width;
            lineDestination += w;
        }

        result.determinantKnown = false;
        return result;
    }

    /**
     * Compute the determinant of a sub matrix (The matrix with one column and one row removed)
     *
     * @param x Column to remove
     * @param y Row to remove
     * @return Determinant computed
     */
    double determinantSubMatrix(final int x, final int y)
    {
        if (this.width == 2)
        {
            return this.matrix[(1 - x) + ((1 - y) * this.width)];
        }

        if (this.width == 3)
        {
            switch (x)
            {
                case 0:
                    switch (y)
                    {
                        case 0:
                            return (this.matrix[4] * this.matrix[8]) - (this.matrix[5] * this.matrix[7]);
                        case 1:
                            return (this.matrix[1] * this.matrix[8]) - (this.matrix[2] * this.matrix[7]);
                        case 2:
                            return (this.matrix[1] * this.matrix[5]) - (this.matrix[2] * this.matrix[4]);
                    }
                case 1:
                    switch (y)
                    {
                        case 0:
                            return (this.matrix[3] * this.matrix[8]) - (this.matrix[5] * this.matrix[6]);
                        case 1:
                            return (this.matrix[0] * this.matrix[8]) - (this.matrix[2] * this.matrix[6]);
                        case 2:
                            return (this.matrix[0] * this.matrix[5]) - (this.matrix[2] * this.matrix[3]);
                    }
                case 2:
                    switch (y)
                    {
                        case 0:
                            return (this.matrix[3] * this.matrix[7]) - (this.matrix[4] * this.matrix[6]);
                        case 1:
                            return (this.matrix[0] * this.matrix[7]) - (this.matrix[1] * this.matrix[6]);
                        case 2:
                            return (this.matrix[0] * this.matrix[4]) - (this.matrix[1] * this.matrix[3]);
                    }
            }
        }

        return this.subMatrix(x, y)
                   .determinantInternalMore2();
    }

    /**
     * Addition wit an other matrix
     *
     * @param matrix Matrix to add
     * @throws IllegalArgumentException If given matrix haven't same width and same height as this matrix
     */
    public void addition(final Matrix matrix)
    {
        if ((this.width != matrix.width) || (this.height != matrix.height))
        {
            throw new IllegalArgumentException("Matrix must have same size !");
        }

        for (int i = 0; i < this.size; i++)
        {
            this.matrix[i] += matrix.matrix[i];
        }

        this.determinantKnown = false;
    }

    /**
     * Compute adjacent matrix
     *
     * @return Adjacent matrix
     * @throws IllegalStateException If matrix is not square
     */
    public Matrix adjacent()
    {
        if (this.width != this.height)
        {
            throw new IllegalStateException("Adjacent only for square matrix");
        }

        if (this.width == 1)
        {
            return this.copy();
        }

        if (this.width == 2)
        {
            final Matrix adjacent = new Matrix(2, 2);
            adjacent.matrix[0] = this.matrix[3];
            adjacent.matrix[1] = -this.matrix[1];
            adjacent.matrix[2] = -this.matrix[2];
            adjacent.matrix[3] = this.matrix[0];
            adjacent.determinantKnown = this.determinantKnown;
            adjacent.determinant = this.determinant;
            return adjacent;
        }

        final Matrix        adjacent = new Matrix(this.width, this.height);
        int                 index    = 0;
        double              signMain = 1;
        final AtomicInteger count    = new AtomicInteger(this.width);

        for (int x = 0; x < this.width; x++)
        {
            ThreadManager.parallel(new TaskAdjacent(count, adjacent.matrix, signMain, index, x, this.height, this));
            signMain *= -1;
            index += this.width;
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (count)
        {
            while (count.get() > 0)
            {
                try
                {
                    count.wait();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        adjacent.determinantKnown = this.determinantKnown;
        adjacent.determinant = Math.pow(this.determinant, this.width - 1);
        return adjacent;
    }

    /**
     * Indicates if matrix can be invert
     *
     * @return {@code true} If matrix can be invert
     */
    public boolean canBeInvert()
    {
        return (this.isSquare()) && (!Matrix.isNul(this.determinant()));
    }

    /**
     * Copy the matrix
     *
     * @return Matrix copy
     */
    public Matrix copy()
    {
        final Matrix matrix = new Matrix(this.width, this.height);
        System.arraycopy(this.matrix, 0, matrix.matrix, 0, this.size);
        matrix.determinantKnown = this.determinantKnown;
        matrix.determinant = this.determinant;
        return matrix;
    }

    /**
     * Compute matrix determinant
     *
     * @return Matrix determinant
     * @throws IllegalStateException If matrix is not square
     */
    public double determinant()
    {
        if (this.width != this.height)
        {
            throw new IllegalStateException("Matrix must be square");
        }

        if (this.determinantKnown)
        {
            return this.determinant;
        }

        return this.determinantInternal();
    }

    /**
     * Obtain a matrix cell
     *
     * @param x X
     * @param y Y
     * @return Cell value
     * @throws IllegalArgumentException If coordinate outside the matrix
     */
    public double get(final int x, final int y)
    {
        this.check(x, y);

        return this.matrix[x + (y * this.width)];
    }

    /**
     * Matrix height
     *
     * @return Matrix height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Matrix width
     *
     * @return Matrix width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Matrix hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Matrix hash code
     * @see Object#hashCode()
     */
    // @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.width, this.height, this.matrix);
    }

    /**
     * Compare the matrix to an object <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to compare with
     * @return {@code true} if object is equals to this matrix
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (object == null)
        {
            return false;
        }

        if (this.getClass() != object.getClass())
        {
            return false;
        }

        final Matrix matrix = (Matrix) object;

        if (this.height != matrix.height)
        {
            return false;
        }

        if (this.width != matrix.width)
        {
            return false;
        }

        for (int i = 0; i < this.size; i++)
        {
            if (!Matrix.equals(this.matrix[i], matrix.matrix[i]))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder("Matrix ");
        stringBuilder.append(this.width);
        stringBuilder.append('x');
        stringBuilder.append(this.height);

        if (this.determinantKnown)
        {
            stringBuilder.append(" determinant=");
            stringBuilder.append(this.determinant);
        }

        stringBuilder.append(" : ");
        int                w            = 0;
        final String[]     texts        = new String[this.size];
        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(5);
        numberFormat.setMaximumFractionDigits(5);
        int length;

        for (int i = 0; i < this.size; i++)
        {
            texts[i] = numberFormat.format(this.matrix[i]);
            length = texts[i].length();
            w = Math.max(length, w);
        }

        for (int i = 0; i < this.size; i++)
        {
            if ((i % this.width) == 0)
            {
                stringBuilder.append("\n     ");
            }

            length = texts[i].length();

            for (int s = length; s < w; s++)
            {
                stringBuilder.append(' ');
            }

            stringBuilder.append(texts[i]);
            stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }

    /**
     * Exchange two matrix row
     *
     * @param y1 First row
     * @param y2 Second row
     * @throws IllegalArgumentException If at least one specified rows is outside the matrix
     */
    public void interExchangeRow(final int y1, final int y2)
    {
        if ((y1 < 0) || (y1 >= this.height) || (y2 < 0) || (y2 >= this.height))
        {
            throw new IllegalArgumentException(
                    "It is a " + this.width + "x" + this.height + " matrix, but y1=" + y1 + " and y2=" + y2);
        }

        if (y1 == y2)
        {
            return;
        }

        this.interExchangeRowInternal(y1, y2);
    }

    /**
     * Invert the matrix
     *
     * @return Matrix invert
     * @throws IllegalStateException    If the matrix is not square
     * @throws IllegalArgumentException If the matrix determinant is zero
     */
    public Matrix invert()
    {
        if (this.width != this.height)
        {
            throw new IllegalStateException("Only square matrix can be invert");
        }

        final double determinant = this.determinant();

        if (Matrix.isNul(determinant))
        {
            throw new IllegalArgumentException("Matrix determinant is zero, so have no invert");
        }

        final Matrix invert = this.adjacent();

        for (int i = 0; i < this.size; i++)
        {
            invert.matrix[i] /= determinant;
        }

        invert.determinantKnown = true;
        invert.determinant = 1 / determinant;

        return invert;
    }

    /**
     * Indicates if matrix is the identity matrix
     *
     * @return {@code true} if matrix is the identity one
     */
    public boolean isIdentity()
    {
        final int diagonal = this.width + 1;
        final int max      = this.width * this.width;

        for (int i = 0; i < this.size; i++)
        {
            if ((i < max) && ((i % diagonal) == 0))
            {
                if (!Matrix.equals(1, this.matrix[i]))
                {
                    return false;
                }
            }
            else if (!Matrix.isNul(this.matrix[i]))
            {
                return false;
            }
        }

        this.determinantKnown = this.width == this.height;
        this.determinant = 1;
        return true;
    }

    /**
     * Indicates if matrix is square
     *
     * @return {@code true} if matrix is square
     */
    public boolean isSquare()
    {
        return this.width == this.height;
    }

    /**
     * Indicates if matrix is zero matrix
     *
     * @return {@code true} if matrix is zero matrix
     */
    public boolean isZero()
    {
        for (int i = 0; i < this.size; i++)
        {
            if (!Matrix.isNul(this.matrix[i]))
            {
                return false;
            }
        }

        this.determinantKnown = this.width == this.height;
        this.determinant = 0;
        return true;
    }

    /**
     * Multiply all matrix cell by a value
     *
     * @param factor Value to multiply with
     */
    public void multiplication(final double factor)
    {
        for (int i = 0; i < this.size; i++)
        {
            this.matrix[i] *= factor;
        }

        this.determinant *= Math.pow(factor, this.width);
    }

    /**
     * Multiply by an other matrix
     *
     * @param matrix Matrix to multiply with
     * @return Matrix result
     * @throws IllegalArgumentException if given matrix width isn't this matrix height OR given matrix height isn't this
     *                                  matrix width
     */
    public Matrix multiplication(final Matrix matrix)
    {
        if ((this.width != matrix.height) || (this.height != matrix.width))
        {
            throw new IllegalArgumentException(
                    "The multiplied matrix must have size : " + this.height + "x" + this.width);
        }

        final Matrix        result = new Matrix(matrix.width, this.height);
        final AtomicInteger count  = new AtomicInteger(result.size);

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < matrix.width; x++)
            {
                ThreadManager.parallel(
                        new TaskMultiplicationCell(count, x, y, this.width, matrix.width, matrix.width, result.matrix,
                                                   this.matrix, matrix.matrix));
            }
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (count)
        {
            while (count.get() > 0)
            {
                try
                {
                    count.wait();
                }
                catch (final Exception ignored)
                {
                }
            }
        }

        if ((this.determinantKnown) && (matrix.determinantKnown))
        {
            result.determinantKnown = true;
            result.determinant = this.determinant * matrix.determinant;
        }
        else
        {
            result.determinantKnown = false;
        }

        return result;
    }

    /**
     * Pivot matrix from one diagonal
     *
     * @param xy X and Y diagonal coordinate
     * @throws IllegalArgumentException If diagonal outside the matrix
     */
    public void pivot(final int xy)
    {
        if ((xy < 0) || (xy >= this.width) || (xy >= this.height))
        {
            throw new IllegalArgumentException("It is a " + this.width + "x" + this.height + " matrix, but xy=" + xy);
        }

        if (Matrix.isNul(this.matrix[xy + (xy * this.width)]))
        {
            return;
        }

        this.pivotInternal(xy);
    }

    /**
     * Change a cell value
     *
     * @param x     X
     * @param y     Y
     * @param value new value
     * @throws IllegalArgumentException If coordinate outside the matrix
     */
    public void set(final int x, final int y, final double value)
    {
        this.check(x, y);
        this.matrix[x + (y * this.width)] = value;
        this.determinantKnown = false;
    }

    /**
     * Push list of values inside the matrix, from up left, left to right, then up to down.<br>
     * If not enough value are given, the rest of the matrix is fill by 0.<br>
     * If to much value is given, the matrix is fill and other values are ignored
     *
     * @param values Values for fill the matrix
     */
    public void setValues(final double... values)
    {
        final int nb = Math.min(this.size, values.length);
        System.arraycopy(values, 0, this.matrix, 0, nb);

        for (int i = nb; i < this.size; i++)
        {
            this.matrix[i] = 0;
        }

        this.determinantKnown = false;
    }

    /**
     * Subtract a matrix to current one
     *
     * @param matrix Matrix to subtract
     * @throws IllegalArgumentException If given matrix haven't this matrix size
     */
    public void subtraction(final Matrix matrix)
    {
        if ((this.width != matrix.width) || (this.height != matrix.height))
        {
            throw new IllegalArgumentException("Matrix must have same size !");
        }

        for (int i = 0; i < this.size; i++)
        {
            this.matrix[i] -= matrix.matrix[i];
        }

        this.determinantKnown = false;
    }

    /**
     * Transform to matrix to identity one
     */
    public void toIdentity()
    {
        this.toZero();

        final int number = this.width * Math.min(this.width, this.height);

        for (int p = 0; p < number; p += this.width + 1)
        {
            this.matrix[p] = 1;
        }

        this.determinantKnown = this.width == this.height;
        this.determinant = 1;
    }

    /**
     * Transform this matrix to zero matrix (Matrix fill of zero)
     */
    public void toZero()
    {
        for (int i = 0; i < this.size; i++)
        {
            this.matrix[i] = 0;
        }

        this.determinantKnown = this.width == this.height;
        this.determinant = 0;
    }

    /**
     * Compute transpose matrix
     *
     * @return Transpose matrix
     */
    public Matrix transpose()
    {
        final Matrix transpose = new Matrix(this.height, this.width);
        int          index     = 0;
        int          pos;

        for (int y = 0; y < this.height; y++)
        {
            pos = y;
            for (int x = 0; x < this.width; x++)
            {
                transpose.matrix[pos] = this.matrix[index++];
                pos += this.height;
            }
        }

        transpose.determinantKnown = this.determinantKnown;
        transpose.determinant = this.determinant;

        return transpose;
    }
}