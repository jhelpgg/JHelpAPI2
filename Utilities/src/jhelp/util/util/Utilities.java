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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import jhelp.util.list.ArrayObject;
import jhelp.util.math.JHelpRandom;

/**
 * Divers utilities
 *
 * @author JHelp
 */
public final class Utilities
{
    /**
     * Regular expression for detect separators in locale string
     */
    private static final Pattern PATTERN_LOCALE = Pattern.compile("[-_]");

    /**
     * Indicates if a character is inside an array
     *
     * @param character Character search
     * @param array     Array where search
     * @return Character index
     */
    public static boolean contains(final char character, final @NotNull char... array)
    {
        for (int i = array.length - 1; i >= 0; i--)
        {
            if (array[i] == character)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Convert string to locale
     *
     * @param string String that represents a locale
     * @return Locale created
     */
    public static @Nullable Locale convertStringToLocale(final @Nullable String string)
    {
        if (string == null)
        {
            return null;
        }

        final String[] split = Utilities.PATTERN_LOCALE.split(string, 3);

        switch (split.length)
        {
            case 1:
                return new Locale(split[0]);
            case 2:
                return new Locale(split[0], split[1]);
        }

        return new Locale(split[0], split[1], split[2]);
    }

    /**
     * Copy a byte array
     *
     * @param data Array to copy
     * @return Copy of array
     */
    public static @Nullable byte[] createCopy(final @Nullable byte[] data)
    {
        if (data == null)
        {
            return null;
        }

        final int    length = data.length;
        final byte[] copy   = new byte[length];
        System.arraycopy(data, 0, copy, 0, length);
        return copy;
    }

    /**
     * Create a char array copy
     *
     * @param characters Char array to copy
     * @return Copy
     */
    public static @Nullable char[] createCopy(final @Nullable char[] characters)
    {
        if (characters == null)
        {
            return null;
        }

        final int    length = characters.length;
        final char[] copy   = new char[length];
        System.arraycopy(characters, 0, copy, 0, length);
        return copy;
    }

    /**
     * Create a copy of array double
     *
     * @param array Array to copy
     * @return Copy
     */
    public static @Nullable double[] createCopy(final @Nullable double[] array)
    {
        if (array == null)
        {
            return null;
        }

        final int      length = array.length;
        final double[] clone  = new double[length];
        System.arraycopy(array, 0, clone, 0, length);
        return clone;
    }

    /**
     * Create a int array copy
     *
     * @param array Array to copy
     * @return Copy of array
     */
    public static @Nullable int[] createCopy(final @Nullable int[] array)
    {
        if (array == null)
        {
            return null;
        }

        final int length = array.length;

        final int[] clone = new int[length];

        System.arraycopy(array, 0, clone, 0, length);

        return clone;
    }

    /**
     * Create an array copy
     *
     * @param <T>   Type of array elements
     * @param array Array to copy
     * @return Array copy
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <T> T[] createCopy(final @Nullable T[] array)
    {
        if (array == null)
        {
            return null;
        }

        final int length = array.length;

        final Class<T> classT = (Class<T>) array.getClass()
                                                .getComponentType();

        final T[] clone = (T[]) Array.newInstance(classT, length);

        System.arraycopy(array, 0, clone, 0, length);

        return clone;
    }

    /**
     * Extract sub array from an array from start index to the end of array<br>
     * Start is automatically put inside source array, that is to say, if start is lower than 0, start will be considered
     * as 0.<br>
     * If source is {@code null}, {@code null} is return.<br>
     * If start if upper or equal to array length or source array is empty then an empty array is return
     *
     * @param <T>   Array component type
     * @param array Array to get elements
     * @param start Start index
     * @return Extracted array
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <T> T[] extractSubArray(final @Nullable T[] array, final int start)
    {
        if (array == null)
        {
            return null;
        }

        return Utilities.extractSubArray(array, start, array.length);
    }

    /**
     * Extract sub array from an array from start index to end, start and end inclusive<br>
     * Start and end index are automatically put inside source array, that is to say, if start is lower than 0, start
     * will be considered as 0, if end is upper the last array index end will be the last array index.<br>
     * If source is {@code null}, {@code null} is return.<br>
     * If end lower than start or source array is empty then an empty array is return
     *
     * @param <T>   Array component type
     * @param array Array to get elements
     * @param start Start index
     * @param end   End index
     * @return Extracted array
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <T> T[] extractSubArray(final @Nullable T[] array, int start, int end)
    {
        if (array == null)
        {
            return null;
        }

        final int length = array.length;

        if (start < 0)
        {
            start = 0;
        }

        if (end >= length)
        {
            end = length - 1;
        }

        final int size = (end - start) + 1;

        if (size <= 0)
        {
            return (T[]) Array.newInstance(array.getClass()
                                                .getComponentType(), 0);
        }

        final T[] result = (T[]) Array.newInstance(array.getClass()
                                                        .getComponentType(), size);

        System.arraycopy(array, start, result, 0, size);

        return result;
    }

    /**
     * Compute index of byte array inside an other byt array
     *
     * @param container Byte array where search the content
     * @param content   Searched array inside the container
     * @param offset    Offset to start the search
     * @param length    Number of byte to take care inside the content
     * @return Index where container starts contain the content or -1 if not found
     */
    public static int indexOf(final @NotNull byte[] container, final @NotNull byte[] content, int offset, int length)
    {
        if (offset < 0)
        {
            length += offset;
            offset = 0;
        }

        if (((offset + length) > container.length) || (length > content.length) || (length < 1))
        {
            return -1;
        }

        boolean    found;
        final byte first = content[0];
        final int  end   = container.length - length;

        for (int i = offset; i <= end; i++)
        {
            if (container[i] == first)
            {
                found = true;

                for (int k = 1; k < length; k++)
                {
                    if (container[i + k] != content[k])
                    {
                        found = false;
                        break;
                    }
                }

                if (found)
                {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Obtain a character index inside a character array. If the character doesn't contains the searched character, -1 is
     * return
     *
     * @param array   Array where search the character
     * @param element Searched character
     * @return Character index or -1 if character not inside the array
     */
    public static int indexOf(final @Nullable char[] array, final char element)
    {
        if (array == null)
        {
            return -1;
        }

        final int length = array.length;

        for (int i = 0; i < length; i++)
        {

            if (array[i] == element)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Index of an element inside an array
     *
     * @param <T>     Element type
     * @param array   Array where search
     * @param element Element search
     * @return Index where is element or -1 if not found
     */
    public static <T> int indexOf(final @Nullable T[] array, final T element)
    {
        if (array == null)
        {
            return -1;
        }

        final int length = array.length;

        for (int i = 0; i < length; i++)
        {
            if ((array[i] == null) && (element == null))
            {
                return i;
            }
            else if ((array[i] != null) && (array[i].equals(element)))
            {
                return i;
            }
        }

        return -1;
    }

    public static <T> void scramble(T[] array)
    {
        if (array == null)
        {
            return;
        }

        final int size = array.length;

        if (size < 2)
        {
            return;
        }

        int index1;
        int index2;
        T   temporary;

        for (int index = size; index > 1; index--)
        {
            index1 = index - 1;
            index2 = JHelpRandom.random(index);

            if (index1 != index2)
            {
                temporary = array[index1];
                array[index1] = array[index2];
                array[index2] = temporary;
            }
        }
    }

    public static void scramble(int[] array)
    {
        if (array == null)
        {
            return;
        }

        final int size = array.length;

        if (size < 2)
        {
            return;
        }

        int index1;
        int index2;
        int temporary;

        for (int index = size; index > 1; index--)
        {
            index1 = index - 1;
            index2 = JHelpRandom.random(index);

            if (index1 != index2)
            {
                temporary = array[index1];
                array[index1] = array[index2];
                array[index2] = temporary;
            }
        }
    }

    /**
     * Make thread call it sleep for specified time in milliseconds
     *
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(final long milliseconds)
    {
        try
        {
            Thread.sleep(Math.max(1, milliseconds));
        }
        catch (final Exception ignored)
        {
        }
    }

    /**
     * Create a list with given elements
     *
     * @param array Elements to put in the list
     * @param <T>   Elements type
     * @return List with elements in it
     */
    public static @SafeVarargs @Nullable <T> ArrayObject<T> toArrayObject(@Nullable T... array)
    {
        if (array == null)
        {
            return null;
        }

        ArrayObject<T> list = new ArrayObject<>();
        Collections.addAll(list, array);
        return list;
    }

    /**
     * Create a list with given elements
     *
     * @param array Elements to put in the list
     * @param <T>   Elements type
     * @return List with elements in it
     */
    public static @SafeVarargs @Nullable <T> List<T> toList(@Nullable T... array)
    {
        if (array == null)
        {
            return null;
        }

        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    /**
     * To avoid instance
     */
    private Utilities()
    {
    }
}