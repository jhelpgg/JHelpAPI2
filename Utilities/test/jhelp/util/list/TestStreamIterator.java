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

package jhelp.util.list;

import jhelp.util.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

public class TestStreamIterator
{
    @Test
    public void testBasic()
    {
        final ArrayObject<String>    array          = Utilities.toArrayObject("first", "second", "third");
        final StreamIterator<String> streamIterator = array.streamIterator();
        Assert.assertTrue(streamIterator.hasNext());
        Assert.assertEquals("first", streamIterator.next());
        Assert.assertTrue(streamIterator.hasNext());
        Assert.assertEquals("second", streamIterator.next());
        Assert.assertTrue(streamIterator.hasNext());
        Assert.assertEquals("third", streamIterator.next());
        Assert.assertFalse(streamIterator.hasNext());

        try
        {
            streamIterator.next();
            Assert.fail("Exception expected!");
        }
        catch (Exception ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testBig()
    {
        final ArrayInt array  = new ArrayInt();
        final int      number = 10000000;

        for (int i = 0; i < number; i++)
        {
            array.add(i);
        }

        final StreamIterator<Integer> streamIterator = array.streamIterator();

        for (int i = 0; i < number; i++)
        {
            Assert.assertTrue(streamIterator.hasNext());
            Assert.assertEquals(i, (int) streamIterator.next());
        }

        Assert.assertFalse(streamIterator.hasNext());

        try
        {
            streamIterator.next();
            Assert.fail("Exception expected!");
        }
        catch (Exception ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testConsume()
    {
        final ArrayObject<String> array = Utilities.toArrayObject("first", "second", "third", "fourth",
                                                                  "fifth");
        final ArrayObject<String>    consumed       = new ArrayObject<>();
        final StreamIterator<String> streamIterator = array.streamIterator().consume(consumed::add);
        streamIterator.waitFinish();

        Assert.assertEquals(array.size(), consumed.size());

        for (int index = array.size() - 1; index >= 0; index--)
        {
            Assert.assertEquals(array.get(index), consumed.get(index));
        }
    }

    @Test
    public void testConsumeInside()
    {
        final ArrayObject<String> array = Utilities.toArrayObject("first", "second", "third", "fourth",
                                                                  "fifth", "sixth", "seventh", "eight");

        for (int i = 0; i < 42; i++)
        {
            array.add("n" + i);
        }

        final ArrayObject<String> consumed = new ArrayObject<>();
        final StreamIterator<String> streamIterator =
                array.streamIterator()
                     .consume(string1 ->
                                      array.streamIterator()
                                           .consume(string2 ->
                                                            array.streamIterator()
                                                                 .consume(string3 -> consumed.add(
                                                                         string1 + "_" + string2 + "_" + string3))
                                                                 .waitFinish())
                                           .waitFinish())
                     .waitFinish();
        final ArrayObject<String> result = new ArrayObject<>();
        final int                 size   = array.size();
        String                    prefix;
        String                    middle;

        for (int first = 0; first < size; first++)
        {
            prefix = array.get(first) + "_";

            for (int second = 0; second < size; second++)
            {
                middle = prefix + array.get(second) + "_";

                for (int third = 0; third < size; third++)
                {
                    result.add(middle + array.get(third));
                }
            }
        }

        Assert.assertEquals(result.size(), consumed.size());

        for (int index = result.size() - 1; index >= 0; index--)
        {
            Assert.assertEquals(result.get(index), consumed.get(index));
        }
    }

    @Test
    public void testMapConsume()
    {
        final ArrayObject<String> array = Utilities.toArrayObject("first", "second", "third", "fourth",
                                                                  "fifth");
        final ArrayObject<String> consumed = new ArrayObject<>();
        final StreamIterator<String> streamIterator = array.streamIterator()
                                                           .map(String::toUpperCase)
                                                           .consume(consumed::add);
        streamIterator.waitFinish();

        Assert.assertEquals(array.size(), consumed.size());

        for (int index = array.size() - 1; index >= 0; index--)
        {
            Assert.assertEquals(array.get(index).toUpperCase(), consumed.get(index));
        }
    }
}
