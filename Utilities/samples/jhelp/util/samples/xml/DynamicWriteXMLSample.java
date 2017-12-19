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

package jhelp.util.samples.xml;

import java.io.IOException;
import jhelp.util.debug.Debug;
import jhelp.util.xml.DynamicWriteXML;

public class DynamicWriteXMLSample
{

    /**
     * TODO Explains what does the method main in jhelp.util.samples.xml [JHelpUtil]
     *
     * @param args
     */
    public static void main(final String[] args)
    {
        try
        {
            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(System.out, false, false);

            dynamicWriteXML.appendComment("The scene");
            dynamicWriteXML.openMarkup("Scene");
            for (int i = 0; i < 5; i++)
            {
                dynamicWriteXML.openMarkup("Object");
                dynamicWriteXML.appendParameter("id", i);
                dynamicWriteXML.appendParameter("name", "object" + i);
                dynamicWriteXML.closeMarkup();
                dynamicWriteXML.appendComment("This was the object " + i);
            }
            dynamicWriteXML.closeMarkup();
        }
        catch (final IOException exception)
        {
            Debug.exception(exception, "Issue while writing XML");
        }
    }
}