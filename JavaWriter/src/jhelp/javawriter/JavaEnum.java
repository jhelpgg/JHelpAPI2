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

package jhelp.javawriter;

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

    public void addChoice(String name)
    {
        this.choices.add(name);
    }
}
