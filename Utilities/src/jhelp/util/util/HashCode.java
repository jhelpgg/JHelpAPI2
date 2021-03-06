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

package jhelp.util.util;

import com.sun.istack.internal.Nullable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper for generate hash codes
 *
 * @author JHelp
 */
public final class HashCode
{
    /**
     * Prime number use to "separate" hash code elements
     */
    public static final int PRIME = 31;

    /**
     * Compute hash code for a list of elements
     *
     * @param elements Elements (Order matter) for compute the hash code
     * @return Computed hash code
     */
    public static int computeHashCode(final @Nullable Object... elements)
    {
        if (elements == null)
        {
            return 0;
        }

        final HashCode hashCode = new HashCode();

        for (final Object element : elements)
        {
            hashCode.add(element);
        }

        return hashCode.getHashCode();
    }

    /**
     * Actual hash code value
     */
    private int hashCode;

    /**
     * Create a new instance of HashCode
     */
    public HashCode()
    {
    }

    /**
     * Add boolean to hash code
     *
     * @param value Value to add
     */
    public void add(final boolean value)
    {
        this.hashCode *= HashCode.PRIME;

        if (value)
        {
            this.hashCode += 1;
        }
    }

    /**
     * Add byte to hash code
     *
     * @param value Value to add
     */
    public void add(final byte value)
    {
        this.hashCode *= HashCode.PRIME;
        this.hashCode += value & 0xFF;
    }

    /**
     * Add char to hash code
     *
     * @param value Value to add
     */
    public void add(final char value)
    {
        this.hashCode *= HashCode.PRIME;
        this.hashCode += value & 0xFFFF;
    }

    /**
     * Add collection to hash code
     *
     * @param <TYPE>     Collection elements type
     * @param collection Collection to add
     */
    public <TYPE> void add(final @Nullable Collection<TYPE> collection)
    {
        this.hashCode *= HashCode.PRIME;

        if (collection == null)
        {
            return;
        }

        this.hashCode += collection.size();

        for (final TYPE element : collection)
        {
            this.add(element);
        }
    }

    /**
     * Add double to hash code
     *
     * @param value Value to add
     */
    public void add(final double value)
    {
        this.add(Double.doubleToLongBits(value));
    }

    /**
     * Add float to hash code
     *
     * @param value Value to add
     */
    public void add(final float value)
    {
        this.add(Float.floatToIntBits(value));
    }

    /**
     * Add int to hash code
     *
     * @param value Value to add
     */
    public void add(final int value)
    {
        this.hashCode *= HashCode.PRIME;
        this.hashCode += value;
    }

    /**
     * Add long to hash code
     *
     * @param value Value to add
     */
    public void add(final long value)
    {
        this.hashCode *= HashCode.PRIME;
        this.hashCode += (int) (value ^ (value >>> 32));
    }

    /**
     * Add map to hash code
     *
     * @param <KEY>   Map key type
     * @param <VALUE> Map value type
     * @param map     Map to add
     */
    public <KEY, VALUE> void add(final @Nullable Map<KEY, VALUE> map)
    {
        this.hashCode *= HashCode.PRIME;

        if (map == null)
        {
            return;
        }

        for (final Entry<KEY, VALUE> entry : map.entrySet())
        {
            this.add(entry.getKey());
            this.add(entry.getValue());
        }
    }

    /**
     * Add generic object to hash code
     *
     * @param object Object to add
     */
    public void add(final @Nullable Object object)
    {
        if (object == null)
        {
            this.hashCode *= HashCode.PRIME;
            return;
        }

        if (object instanceof Collection)
        {
            this.add((Collection<?>) object);
            return;
        }

        if (object instanceof Map)
        {
            this.add((Map<?, ?>) object);
            return;
        }

        Class<?> type = object.getClass();

        if (type.isPrimitive())
        {
            if (boolean.class.equals(type))
            {
                this.add((boolean) object);
                return;
            }

            if (byte.class.equals(type))
            {
                this.add((byte) object);
                return;
            }

            if (char.class.equals(type))
            {
                this.add((char) object);
                return;
            }

            if (double.class.equals(type))
            {
                this.add((double) object);
                return;
            }

            if (float.class.equals(type))
            {
                this.add((float) object);
                return;
            }

            if (int.class.equals(type))
            {
                this.add((int) object);
                return;
            }

            if (long.class.equals(type))
            {
                this.add((long) object);
                return;
            }

            if (short.class.equals(type))
            {
                this.add((short) object);
                return;
            }

            return;
        }

        if (Boolean.class.equals(type))
        {
            this.add(((Boolean) object).booleanValue());
            return;
        }

        if (Character.class.equals(type))
        {
            this.add(((Character) object).charValue());
            return;
        }

        if (Byte.class.equals(type))
        {
            this.add(((Byte) object).byteValue());
            return;
        }

        if (Short.class.equals(type))
        {
            this.add(((Short) object).shortValue());
            return;
        }

        if (Integer.class.equals(type))
        {
            this.add(((Integer) object).intValue());
            return;
        }

        if (Long.class.equals(type))
        {
            this.add(((Long) object).longValue());
            return;
        }

        if (Float.class.equals(type))
        {
            this.add(((Float) object).floatValue());
            return;
        }

        if (Double.class.equals(type))
        {
            this.add(((Double) object).doubleValue());
            return;
        }

        if (type.isEnum())
        {
            this.add(((Enum<?>) object).ordinal());
            return;
        }

        this.hashCode *= HashCode.PRIME;

        if (!type.isArray())
        {
            this.hashCode += object.hashCode();
            return;
        }

        type = type.getComponentType();
        final int length = Array.getLength(object);

        this.hashCode += length;

        if (type.isPrimitive())
        {
            if (boolean.class.equals(type))
            {
                for (final boolean value : (boolean[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (byte.class.equals(type))
            {
                for (final byte value : (byte[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (char.class.equals(type))
            {
                for (final char value : (char[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (double.class.equals(type))
            {
                for (final double value : (double[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (float.class.equals(type))
            {
                for (final float value : (float[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (int.class.equals(type))
            {
                for (final int value : (int[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (long.class.equals(type))
            {
                for (final long value : (long[]) object)
                {
                    this.add(value);
                }

                return;
            }

            if (short.class.equals(type))
            {
                for (final short value : (short[]) object)
                {
                    this.add(value);
                }

                return;
            }

            return;
        }

        for (int index = 0; index < length; index++)
        {
            this.add(Array.get(object, index));
        }
    }

    /**
     * Add short to hash code
     *
     * @param value Value to add
     */
    public void add(final short value)
    {
        this.hashCode *= HashCode.PRIME;
        this.hashCode += value & 0xFFFF;
    }

    /**
     * Current computed hash code
     *
     * @return Current computed hash code
     */
    public int getHashCode()
    {
        return this.hashCode;
    }
}