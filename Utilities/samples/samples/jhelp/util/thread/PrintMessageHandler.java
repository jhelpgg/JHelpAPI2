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
