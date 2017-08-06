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

package jhelp.util.cache;

import jhelp.util.text.UtilText;

/**
 * String cache element for test
 */
public class TestCacheElement extends CacheElement<String>
{
    private final String  base;
    private       boolean clearedCalled;
    private final int     time;

    public TestCacheElement(final int time, final String base)
    {
        this.time = time;
        this.base = base;
        this.clearedCalled = false;
    }

    @Override
    protected void cleared(final String string)
    {
        this.clearedCalled = true;
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

    public boolean clearedCalled()
    {
        return this.clearedCalled;
    }
}
