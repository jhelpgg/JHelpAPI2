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

import jhelp.util.debug.Debug;
import jhelp.util.thread.MessageHandler;

public class PrintMessageHandler extends MessageHandler<PrintMessage>
{
    public PrintMessageHandler()
    {
    }

    /**
     * Called when a message received and treat it
     *
     * @param message Received message
     */
    @Override
    protected void handleMessage(final PrintMessage message)
    {
        switch (message.printArea())
        {
            case TITLE:
                // print message.text() as title
                Debug.mark(message.text());
                break;
            case INFORMATION:
                // print message.text() as information
                Debug.information(message.text());
                break;
        }
    }
}
