package jhelp.javawriter;

import java.io.IOException;
import jhelp.util.io.TextWriter;
import jhelp.util.list.SortedArray;

/**
 * Created by jhelp on 24/06/17.
 */
public class JavaEnum extends JavaFile
{
    private final SortedArray<String> choices;

    public JavaEnum(final String fullName, final boolean isPublic, final boolean isFinal)
    {
        super(JavaConstants.ENUM, fullName, isPublic, isFinal);
        this.choices = new SortedArray<>(String.class, String.CASE_INSENSITIVE_ORDER, true);
    }

    public void addChoice(String name)
    {
        this.choices.add(name);
    }

    @Override
    protected void writeInternal(final TextWriter textWriter, final String spaces)
    {
        boolean first = true;

        for (String choice : this.choices)
        {
            if (!first)
            {
                textWriter.println(",");
            }

            first = false;
            textWriter.print(spaces);
            textWriter.print(choice);
        }

        if (!first)
        {
            textWriter.println();
        }
    }
}
