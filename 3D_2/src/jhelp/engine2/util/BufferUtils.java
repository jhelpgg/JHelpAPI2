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
package jhelp.engine2.util;

import com.sun.istack.internal.NotNull;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Buffer utilities.<br>
 * To not take too much memory, we reuse always the same ByteBuffer and see it like IntBuffer, FloatBuffer or DoubleBuffer
 * depends on the situation.<br>
 * Methods here are helper for manipulate this buffer. <br>
 *
 * @author JHelp
 */
public class BufferUtils
{
    //Static initializer to force the initialization order
    static
    {
        MAX_WIDTH = 4096;
        MAX_HEIGHT = 4096;
        MAX_DIMENSION = BufferUtils.MAX_WIDTH * BufferUtils.MAX_HEIGHT;
        MAX_DIMENSION_IN_BYTES = BufferUtils.MAX_DIMENSION << 2;
        TEMPORARY_BYTE_BUFFER = ByteBuffer.allocateDirect(BufferUtils.MAX_DIMENSION_IN_BYTES)
                                          .order(ByteOrder.nativeOrder());
        TEMPORARY_INT_BUFFER = BufferUtils.TEMPORARY_BYTE_BUFFER.asIntBuffer();
        TEMPORARY_FLOAT_BUFFER = BufferUtils.TEMPORARY_BYTE_BUFFER.asFloatBuffer();
        TEMPORARY_DOUBLE_BUFFER = BufferUtils.TEMPORARY_BYTE_BUFFER.asDoubleBuffer();
    }

    /**
     * Maximum dimension
     */
    private static final int          MAX_DIMENSION;
    /**
     * Maximum buffer size enough to put the biggest texture
     */
    private static final int          MAX_DIMENSION_IN_BYTES;
    /**
     * Maximum texture height
     */
    private static final int          MAX_HEIGHT;
    /**
     * Maximum texture width
     */
    private static final int          MAX_WIDTH;
    /**
     * The buffer
     */
    public static final  ByteBuffer   TEMPORARY_BYTE_BUFFER;
    /**
     * See the buffer in DoubleBuffer
     */
    public static final  DoubleBuffer TEMPORARY_DOUBLE_BUFFER;
    /**
     * See the buffer in FloatBuffer
     */
    public static final  FloatBuffer  TEMPORARY_FLOAT_BUFFER;
    /**
     * See the buffer in IntBuffer
     */
    public static final  IntBuffer    TEMPORARY_INT_BUFFER;

    /**
     * Fill byte array with the buffer
     *
     * @param array Array to fill
     */
    public static void fill(final @NotNull byte[] array)
    {
        BufferUtils.TEMPORARY_BYTE_BUFFER.rewind();
        BufferUtils.TEMPORARY_BYTE_BUFFER.get(array);
        BufferUtils.TEMPORARY_BYTE_BUFFER.rewind();
    }

    /**
     * Fill double array with the buffer
     *
     * @param array Array to fill
     */
    public static void fill(final @NotNull double[] array)
    {
        BufferUtils.TEMPORARY_DOUBLE_BUFFER.rewind();
        BufferUtils.TEMPORARY_DOUBLE_BUFFER.get(array);
        BufferUtils.TEMPORARY_DOUBLE_BUFFER.rewind();
    }

    /**
     * Fill float array with the buffer
     *
     * @param array Array to fill
     */
    public static void fill(final @NotNull float[] array)
    {
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        BufferUtils.TEMPORARY_FLOAT_BUFFER.get(array);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
    }

    /**
     * Fill int array with the buffer
     *
     * @param array Array to fill
     */
    public static void fill(final @NotNull int[] array)
    {
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        BufferUtils.TEMPORARY_INT_BUFFER.get(array);
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
    }

    /**
     * Transform a single dimension array to a double dimension array
     *
     * @param ar Array to transform
     * @return Transformed array
     */
    public static @NotNull float[][] toTwoDimensionFloatArray(final @NotNull float[] ar)
    {
        final int       nb    = ar.length;
        final int       n     = (int) (Math.sqrt(nb));
        final float[][] array = new float[n][n];
        int             index = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                array[i][j] = ar[index++];
            }
        }
        return array;
    }

    /**
     * Transfer byte array to the buffer
     *
     * @param array Array to transfer
     * @return The buffer filled
     */
    public static @NotNull ByteBuffer transferByte(final @NotNull byte... array)
    {
        BufferUtils.TEMPORARY_BYTE_BUFFER.rewind();
        BufferUtils.TEMPORARY_BYTE_BUFFER.put(array);
        BufferUtils.TEMPORARY_BYTE_BUFFER.rewind();
        return BufferUtils.TEMPORARY_BYTE_BUFFER;
    }

    /**
     * Transfer float array to the buffer
     *
     * @param array Array to transfer
     * @return The buffer filled
     */
    public static @NotNull FloatBuffer transferFloat(final @NotNull float... array)
    {
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(array);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        return BufferUtils.TEMPORARY_FLOAT_BUFFER;
    }

    /**
     * Transfer int array to the buffer
     *
     * @param array Array to transfer
     * @return The buffer filled
     */
    public static @NotNull IntBuffer transferInteger(final @NotNull int... array)
    {
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        BufferUtils.TEMPORARY_INT_BUFFER.put(array);
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        return BufferUtils.TEMPORARY_INT_BUFFER;
    }
}