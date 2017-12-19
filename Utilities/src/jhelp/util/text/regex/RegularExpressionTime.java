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

class RegularExpressionTime extends RegularExpressionElement
{
    private final RegularExpression regularExpression;
    private final String            suffix;

    RegularExpressionTime(
            RegularExpressionElementType regularExpressionElementType, RegularExpression regularExpression,
            String suffix)
    {
        super(regularExpressionElementType);
        Objects.requireNonNull(suffix, "suffix MUST NOT be null!");
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.regularExpression = regularExpression;
        this.suffix = suffix;
    }

    @Override
    public int numberOfGroup()
    {
        int number = this.regularExpression.numberOfGroup();

        if (this.regularExpression.complex())
        {
            number++;
        }

        return number;
    }

    @Override
    public final void regex(final StringBuilder stringBuilder)
    {
        this.regularExpression.regex(stringBuilder, true);
        stringBuilder.append(this.suffix);
    }
}
