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
