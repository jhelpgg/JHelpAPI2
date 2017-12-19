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

import java.util.Objects;

final class RegularExpressionString extends RegularExpressionElement
{
    private final String string;

    RegularExpressionString(String string)
    {
        super(RegularExpressionElementType.STRING);
        Objects.requireNonNull(string, "string MUST NOT be null!");
        this.string = string;
    }

    @Override
    public int numberOfGroup()
    {
        return 0;
    }

    @Override
    public void regex(final StringBuilder stringBuilder)
    {
        stringBuilder.append(this.string);
    }

    public String string()
    {
        return this.string;
    }
}
