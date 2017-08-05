package jhelp.util.cache;

import jhelp.util.text.UtilText;

/**
 * String cache element for test
 */
public class TestCacheElement extends CacheElement<String>
{
    private       boolean clearedCalled;
    private final int     time;
    private final String  base;

    public TestCacheElement(final int time, final String base)
    {
        this.time = time;
        this.base = base;
        this.clearedCalled = false;
    }

    /**
     * Create the element
     *
     * @return Created element
     */
    @Override
    protected String create()
    {
        return UtilText.repeat(this.base, this.time);
    }

    @Override
    protected void cleared(final String string)
    {
        this.clearedCalled = true;
    }

    public boolean clearedCalled()
    {
        return this.clearedCalled;
    }
}
