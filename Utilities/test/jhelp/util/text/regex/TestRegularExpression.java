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

package jhelp.util.text.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

public class TestRegularExpression
{
    @Test
    public void testCapture()
    {
        //L:  Greeting {"Bonjour" "Salut"}
        RegularExpression regularExpression = new RegularExpression();
        regularExpression.appendString("L:");
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        regularExpression.appendCapture(RegularExpression.WORD);
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        regularExpression.appendString("\\{");
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        {
            RegularExpression listElement = new RegularExpression();
            listElement.appendRegularExpression(RegularExpression.STRING);
            listElement.appendRegularExpression(RegularExpression.SPACE);

            RegularExpression list = new RegularExpression();
            list.appendAny(listElement);

            regularExpression.appendCapture(list);
        }
        regularExpression.appendString("\\}");

        Pattern pattern = regularExpression.pattern();
        Matcher matcher = pattern.matcher("L:  Greeting {\"Bonjour\" \"Salut\"}");
        Assert.assertTrue(matcher.matches());
        int nameGroup = regularExpression.groupOfCapture(0);
        Assert.assertTrue(nameGroup > 0);
        int listGroup = regularExpression.groupOfCapture(1);
        Assert.assertTrue(listGroup > 0);
        Assert.assertEquals("Greeting", matcher.group(nameGroup));
        Assert.assertEquals("\"Bonjour\" \"Salut\"", matcher.group(listGroup));
    }

    @Test
    public void testStatic()
    {
        Pattern pattern = RegularExpression.LETTER.pattern();
        Assert.assertTrue(pattern.matcher("a").matches());
        Assert.assertTrue(pattern.matcher("9").matches());
        Assert.assertTrue(pattern.matcher("_").matches());
        Assert.assertTrue(pattern.matcher("F").matches());
        Assert.assertFalse(pattern.matcher(" ").matches());
        Assert.assertFalse(pattern.matcher("car").matches());
        Assert.assertEquals(0, RegularExpression.LETTER.numberOfGroup());

        pattern = RegularExpression.WORD.pattern();
        Assert.assertTrue(pattern.matcher("a").matches());
        Assert.assertTrue(pattern.matcher("9").matches());
        Assert.assertTrue(pattern.matcher("_").matches());
        Assert.assertTrue(pattern.matcher("F").matches());
        Assert.assertFalse(pattern.matcher(" ").matches());
        Assert.assertTrue(pattern.matcher("car").matches());
        Assert.assertTrue(pattern.matcher("car753_15").matches());
        Assert.assertFalse(pattern.matcher("fast car").matches());
        Assert.assertEquals(0, RegularExpression.WORD.numberOfGroup());

        pattern = RegularExpression.STRING.pattern();
        Assert.assertTrue(pattern.matcher("\"a\"").matches());
        Assert.assertTrue(pattern.matcher("\"9\"").matches());
        Assert.assertTrue(pattern.matcher("\"_\"").matches());
        Assert.assertTrue(pattern.matcher("\"F\"").matches());
        Assert.assertTrue(pattern.matcher("\" \"").matches());
        Assert.assertTrue(pattern.matcher("\"car\"").matches());
        Assert.assertTrue(pattern.matcher("\"car753_15\"").matches());
        Assert.assertTrue(pattern.matcher("\"fast car\"").matches());
        Assert.assertTrue(pattern.matcher("\"\"").matches());
        Assert.assertFalse(pattern.matcher("\"ca\"r\"").matches());
        Assert.assertTrue(pattern.matcher("\"ca\\\"r\"").matches());
        Assert.assertFalse(pattern.matcher("car\"").matches());
        Assert.assertFalse(pattern.matcher("\"car").matches());
        Assert.assertFalse(pattern.matcher("car").matches());
        Assert.assertEquals(1, RegularExpression.STRING.numberOfGroup());

        pattern = RegularExpression.SPACE.pattern();
        Assert.assertTrue(pattern.matcher("").matches());
        Assert.assertTrue(pattern.matcher(" \n\t\r   \n\n \t\t").matches());
        Assert.assertFalse(pattern.matcher("   r   ").matches());
        Assert.assertEquals(0, RegularExpression.SPACE.numberOfGroup());
    }
}
