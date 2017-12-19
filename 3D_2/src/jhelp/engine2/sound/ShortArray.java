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

package jhelp.engine2.sound;

public final class ShortArray
{
    private short[] array;
    private int     size;

    public ShortArray(int capacity)
    {
        this.array = new short[Math.max(capacity, 4096)];
        this.size = 0;
    }

    private void ensureCapacity(int more)
    {
        if (this.size + more >= this.array.length)
        {
            int capacity = this.size + more;
            capacity += capacity >> 3;
            final short[] temp = new short[capacity];
            System.arraycopy(this.array, 0, temp, 0, this.size);
            this.array = temp;
        }
    }

    public short[] array()
    {
        final short[] array = new short[this.size];
        System.arraycopy(this.array, 0, array, 0, this.size);
        return array;
    }

    public void write(short[] array, int offset, int length)
    {
        if (offset < 0)
        {
            length += offset;
            offset = 0;
        }

        if (offset + length > array.length)
        {
            length = array.length - offset;
        }

        if (length <= 0)
        {
            return;
        }

        this.ensureCapacity(length);
        System.arraycopy(array, offset, this.array, this.size, length);
        this.size += length;
    }
}
