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
