package jhelp.util.list;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 12/06/17.
 */
public class TestSortedArray
{
    @Test
    public void testSortedArray()
    {
        SortedArray<Integer> sortedArray = new SortedArray<>(Integer.class);
        Assert.assertTrue(sortedArray.empty());
        Assert.assertEquals(0, sortedArray.size());
        Assert.assertEquals("[]", sortedArray.toString());

        try
        {
            sortedArray.get(1000);
            Assert.fail();
        }
        catch (Exception ignored)
        {
            //That is what we want
        }

        sortedArray.add(12);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(1, sortedArray.size());
        Assert.assertEquals("[12]", sortedArray.toString());

        sortedArray.add(5);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(2, sortedArray.size());
        Assert.assertEquals("[5, 12]", sortedArray.toString());

        sortedArray.add(8);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(3, sortedArray.size());
        Assert.assertEquals("[5, 8, 12]", sortedArray.toString());

        sortedArray.add(24);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(4, sortedArray.size());
        Assert.assertEquals("[5, 8, 12, 24]", sortedArray.toString());

        sortedArray.add(2);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(5, sortedArray.size());
        Assert.assertEquals("[2, 5, 8, 12, 24]", sortedArray.toString());
        Assert.assertEquals(5, sortedArray.size());
        Assert.assertEquals(-1, sortedArray.indexOf(1));
        Assert.assertEquals(-1, sortedArray.indexOf(32));
        Assert.assertEquals(-1, sortedArray.indexOf(9));
        Assert.assertEquals(0, sortedArray.indexOf(2));
        Assert.assertEquals(2, sortedArray.indexOf(8));
        Assert.assertEquals(4, sortedArray.indexOf(24));

        sortedArray.add(8);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(6, sortedArray.size());
        Assert.assertEquals("[2, 5, 8, 8, 12, 24]", sortedArray.toString());

        sortedArray.remove((Integer) 8);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(5, sortedArray.size());
        Assert.assertEquals("[2, 5, 8, 12, 24]", sortedArray.toString());

        sortedArray.remove((Integer) 9);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(5, sortedArray.size());
        Assert.assertEquals("[2, 5, 8, 12, 24]", sortedArray.toString());

        sortedArray.remove((Integer) 2);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(4, sortedArray.size());
        Assert.assertEquals("[5, 8, 12, 24]", sortedArray.toString());

        sortedArray.remove((Integer) 24);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(3, sortedArray.size());
        Assert.assertEquals("[5, 8, 12]", sortedArray.toString());

        sortedArray.remove((Integer) 8);
        Assert.assertFalse(sortedArray.empty());
        Assert.assertEquals(2, sortedArray.size());
        Assert.assertEquals("[5, 12]", sortedArray.toString());
    }
}
