package jhelp.util.cache;

import java.util.Set;
import jhelp.util.test.UtilTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Cache}
 */
public class TestCache
{
    @Test
    public void testAddGet()
    {
        Cache<String>    cache            = new Cache<>();
        TestCacheElement testCacheElement = new TestCacheElement(5, "a");
        cache.add("5a", testCacheElement);
        Assert.assertEquals("aaaaa", cache.get("5a"));
        Assert.assertNull(cache.get("5b"));
        UtilTest.testException("Null key not accepted", () -> cache.add(null, new TestCacheElement(5, "a")));
        UtilTest.testException("Null value not accepted", () -> cache.add("6b", null));
        UtilTest.testException("Null key not accepted", () -> cache.get(null));
        Assert.assertFalse(testCacheElement.clearedCalled());
        cache.add("5a", new TestCacheElement(5, "b"));
        Assert.assertEquals("bbbbb", cache.get("5a"));
        Assert.assertTrue(testCacheElement.clearedCalled());
        //
        Assert.assertEquals("ccccccc", cache.get("7c", new TestCacheElement(7, "c")));
        Assert.assertEquals("ccccccc", cache.get("7c", new TestCacheElement(3, "d")));
        UtilTest.testException("Null key not accepted", () -> cache.get(null, new TestCacheElement(3, "e")));
        Assert.assertEquals("ccccccc", cache.get("7c", null));
        UtilTest.testException("Null value not accepted", () -> cache.get("8f", null));
    }

    @Test
    public void testClear()
    {
        Cache<String>    cache            = new Cache<>();
        TestCacheElement testCacheElement = new TestCacheElement(5, "a");
        cache.add("5a", testCacheElement);
        cache.clear();
        Assert.assertFalse(testCacheElement.clearedCalled());
        Assert.assertNull(cache.get("5a"));
        //
        cache.add("5a", testCacheElement);
        Assert.assertEquals("aaaaa", cache.get("5a"));
        cache.clear();
        Assert.assertTrue(testCacheElement.clearedCalled());
        Assert.assertNull(cache.get("5a"));
    }

    @Test
    public void testKeys()
    {
        Cache<String> cache = new Cache<>();
        Assert.assertEquals("ccccccc", cache.get("7c", new TestCacheElement(7, "c")));
        Assert.assertEquals("aaaaa", cache.get("5a", new TestCacheElement(5, "a")));
        Set<String> keys = cache.keys();
        Assert.assertEquals(2, keys.size());
        Assert.assertTrue(keys.contains("5a"));
        Assert.assertTrue(keys.contains("7c"));
    }

    @Test
    public void testRemove()
    {
        Cache<String>    cache            = new Cache<>();
        TestCacheElement testCacheElement = new TestCacheElement(5, "a");
        cache.add("5a", testCacheElement);
        Assert.assertEquals("aaaaa", cache.get("5a"));
        UtilTest.testException("Null key not accepted", () -> cache.remove(null));
        cache.remove("8r");
        Assert.assertEquals("aaaaa", cache.get("5a"));
        Assert.assertFalse(testCacheElement.clearedCalled());
        cache.remove("5a");
        Assert.assertNull(cache.get("5a"));
        Assert.assertTrue(testCacheElement.clearedCalled());

    }
}
