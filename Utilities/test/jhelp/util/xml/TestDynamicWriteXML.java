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

package jhelp.util.xml;

import jhelp.util.debug.Debug;
import jhelp.util.io.StringOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 09/07/17.
 */
public class TestDynamicWriteXML
{
    @Test
    public void test1()
    {
        final StringOutputStream stringOutputStream = new StringOutputStream();

        try
        {
            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(stringOutputStream, true, true, true);
            dynamicWriteXML.openMarkup("Main");
            dynamicWriteXML.openMarkup("Sub");
            dynamicWriteXML.appendParameter("p1", "v1");
            dynamicWriteXML.appendParameter("p2", true);
            dynamicWriteXML.appendParameter("p3", 3.14);
            dynamicWriteXML.appendParameter("p4", 1.23f);
            dynamicWriteXML.appendParameter("p5", 73);
            dynamicWriteXML.setText("Text");
            dynamicWriteXML.closeMarkup();
            dynamicWriteXML.closeMarkup();
            Assert.assertEquals(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<Main><Sub p1=\"v1\" p2=\"TRUE\" p3=\"3.14\" p4=\"1.23\" p5=\"73\">Text</Sub></Main>",
                    stringOutputStream.getString());
        }
        catch (Exception exception)
        {
            Debug.exception(exception);
            Assert.fail("Exception happen : " + exception);
        }
    }

    @Test
    public void testComment1()
    {
        final StringOutputStream stringOutputStream = new StringOutputStream();

        try
        {
            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(stringOutputStream, true, true, true);
            dynamicWriteXML.appendComment("Main");
            dynamicWriteXML.openMarkup("Main");
            dynamicWriteXML.appendComment("Sub");
            dynamicWriteXML.openMarkup("Sub");
            dynamicWriteXML.appendParameter("p1", "v1");
            dynamicWriteXML.appendParameter("p2", true);
            dynamicWriteXML.appendParameter("p3", 3.14);
            dynamicWriteXML.appendParameter("p4", 1.23f);
            dynamicWriteXML.appendParameter("p5", 73);
            dynamicWriteXML.appendComment("Text");
            dynamicWriteXML.setText("Text");
            dynamicWriteXML.appendComment("Text");
            dynamicWriteXML.closeMarkup();
            dynamicWriteXML.appendComment("Sub");
            dynamicWriteXML.closeMarkup();
            Assert.assertEquals(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!-- Main --><Main><!-- Sub --><Sub p1=\"v1\" p2=\"TRUE\" p3=\"3.14\" p4=\"1.23\" p5=\"73\"><!-- Text -->Text<!-- Text --></Sub><!-- Sub --></Main>",
                    stringOutputStream.getString());
        }
        catch (Exception exception)
        {
            Debug.exception(exception);
            Assert.fail("Exception happen : " + exception);
        }
    }

    @Test
    public void testCommentAtStart()
    {
        final StringOutputStream stringOutputStream = new StringOutputStream();

        try
        {
            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(stringOutputStream, true, true, true);
            dynamicWriteXML.appendComment("Comment");
            dynamicWriteXML.openMarkup("M");
            dynamicWriteXML.closeMarkup();
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!-- Comment --><M/>",
                                stringOutputStream.getString());
        }
        catch (Exception exception)
        {
            Debug.exception(exception);
            Assert.fail("Exception happen : " + exception);
        }
    }

    @Test
    public void testMinimumXML()
    {
        final StringOutputStream stringOutputStream = new StringOutputStream();

        try
        {
            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(stringOutputStream, true, true, true);
            dynamicWriteXML.openMarkup("M");
            dynamicWriteXML.closeMarkup();
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<M/>", stringOutputStream.getString());
        }
        catch (Exception exception)
        {
            Debug.exception(exception);
            Assert.fail("Exception happen : " + exception);
        }
    }
}
