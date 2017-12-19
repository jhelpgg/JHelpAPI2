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

package samples.jhelp.util.thread;

/**
 * Message to say where print a message
 */
public class PrintMessage
{
    public enum PrintArea
    {
        TITLE,
        INFORMATION
    }

    public static PrintMessage setInformation(String title)
    {
        return new PrintMessage(PrintArea.INFORMATION, title);
    }

    public static PrintMessage setTitle(String title)
    {
        return new PrintMessage(PrintArea.TITLE, title);
    }

    private final PrintArea printArea;
    private final String    text;

    PrintMessage(final PrintArea printArea, final String text)
    {
        this.printArea = printArea;
        this.text = text;
    }

    public PrintArea printArea()
    {
        return this.printArea;
    }

    public String text()
    {
        return this.text;
    }
}
