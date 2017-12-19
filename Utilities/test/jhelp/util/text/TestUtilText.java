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

package jhelp.util.text;

import org.junit.Assert;
import org.junit.Test;

public class TestUtilText
{
    @Test
    public void testTrim()
    {
        Assert.assertEquals("test", UtilText.trim("test", "es"));
        Assert.assertEquals("", UtilText.trim("", "es"));
        Assert.assertEquals("", UtilText.trim("test", "ets"));
        Assert.assertEquals("test", UtilText.trim("eeeeesssstesteessses", "es"));
    }
}
