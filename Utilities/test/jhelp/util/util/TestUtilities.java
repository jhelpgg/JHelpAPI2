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

import java.util.Locale;
import jhelp.util.data.Combiner;
import jhelp.util.debug.Debug;
import jhelp.util.math.JHelpRandom;
import jhelp.util.math.Math2;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 09/07/17.
 */
public class TestUtilities
{
    @Test
    public void testContains()
    {
        Assert.assertTrue(Utilities.contains('k', 'a', 'r', 'c', 'k', ' ', 'c', 'o', 'o', 'l'));
        Assert.assertFalse(Utilities.contains('z', 'a', 'r', 'c', 'k', ' ', 'c', 'o', 'o', 'l'));
    }

    @Test
    public void testConvertStringToLocale()
    {
        Locale locale = Utilities.convertStringToLocale("fr");
        Assert.assertEquals("fr", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
        Assert.assertEquals("", locale.getVariant());

        locale = Utilities.convertStringToLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
        Assert.assertEquals("", locale.getVariant());

        locale = Utilities.convertStringToLocale("en_US_posix");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
        Assert.assertEquals("posix", locale.getVariant());
    }

    @Test
    public void testCopyBytes()
    {
        int    size = JHelpRandom.random(3, 9);
        byte[] data = new byte[size];

        for (int i = 0; i < size; i++)
        {
            data[i] = (byte) JHelpRandom.random(0, 255);
        }

        Assert.assertArrayEquals(data, Utilities.createCopy(data));
    }

    @Test
    public void testCopyChars()
    {
        int    size = JHelpRandom.random(3, 9);
        char[] data = new char[size];

        for (int i = 0; i < size; i++)
        {
            data[i] = (char) JHelpRandom.random(32, 128);
        }

        Assert.assertArrayEquals(data, Utilities.createCopy(data));
    }

    @Test
    public void testCopyDoubles()
    {
        int      size = JHelpRandom.random(3, 9);
        double[] data = new double[size];

        for (int i = 0; i < size; i++)
        {
            data[i] = Math.random();
        }

        Assert.assertArrayEquals(data, Utilities.createCopy(data), Math2.EPSILON);
    }

    @Test
    public void testCopyInts()
    {
        int   size = JHelpRandom.random(3, 9);
        int[] data = new int[size];

        for (int i = 0; i < size; i++)
        {
            data[i] = JHelpRandom.random(0, 4096);
        }

        Assert.assertArrayEquals(data, Utilities.createCopy(data));
    }

    @Test
    public void testCopyObjects()
    {
        int      size = JHelpRandom.random(3, 9);
        String[] data = new String[size];

        for (int i = 0; i < size; i++)
        {
            data[i] = String.valueOf(JHelpRandom.random(0, 8192));
        }

        Assert.assertArrayEquals(data, Utilities.createCopy(data));
    }

    @Test
    public void testExtractSubArray1()
    {
        Assert.assertArrayEquals(new String[]{}, Utilities.extractSubArray(new String[]{}, 0));
        Assert.assertArrayEquals(new String[]{"hello", "hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, -1));
        Assert.assertArrayEquals(new String[]{"hello", "hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 0));
        Assert.assertArrayEquals(new String[]{"hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 1));
        Assert.assertArrayEquals(new String[]{"yo"}, Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 2));
        Assert.assertArrayEquals(new String[]{}, Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 3));
    }

    @Test
    public void testExtractSubArray2()
    {
        Assert.assertArrayEquals(new String[]{}, Utilities.extractSubArray(new String[]{}, 0, 0));
        Assert.assertArrayEquals(new String[]{"hello", "hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, -1, 100));
        Assert.assertArrayEquals(new String[]{"hello", "hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 0, 2));
        Assert.assertArrayEquals(new String[]{"hi", "yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 1, 2));
        Assert.assertArrayEquals(new String[]{"yo"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 2, 2));
        Assert.assertArrayEquals(new String[]{}, Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 3, 2));
        Assert.assertArrayEquals(new String[]{"hi"},
                                 Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 1, 1));
        Assert.assertArrayEquals(new String[]{}, Utilities.extractSubArray(new String[]{"hello", "hi", "yo"}, 2, 1));
    }

    @Test
    public void testIndexOfBytes()
    {
        byte[] container = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7,
                                      (byte) 8, (byte) 9};
        byte[] content = new byte[]{(byte) 3, (byte) 4, (byte) 5};
        Assert.assertEquals(3, Utilities.indexOf(container, content, 0, content.length));
        content = new byte[]{(byte) 0, (byte) 1, (byte) 2};
        Assert.assertEquals(-1, Utilities.indexOf(container, content, 1, content.length));
    }

    @Test
    public void testReduce()
    {
        String string = "car";
        Debug.verbose("optional 1 = ", Combiner.<String>IF().combine(string.contains("a"), string));
        Debug.verbose("optional 2 = ", Combiner.<String>IF().combine(string.contains("b"), string));
    }
}
