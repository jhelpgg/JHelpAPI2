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

public class MainPrintHandler
{
    public static void main(String[] args)
    {
        PrintMessageHandler printMessageHandler = new PrintMessageHandler();
        printMessageHandler.post(PrintMessage.setTitle("Title 1"));
        printMessageHandler.post(PrintMessage.setInformation("Info 1"));
        printMessageHandler.post(PrintMessage.setTitle("Title in 2 seconds"), 2000);
        printMessageHandler.post(PrintMessage.setInformation("Info in 2 seconds"), 2000);
        printMessageHandler.post(PrintMessage.setTitle("Title in 1 second"), 1000);
        printMessageHandler.post(PrintMessage.setInformation("Info in 1 second"), 1000);
    }
}
