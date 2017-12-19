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

class RegularExpressionCapture extends RegularExpressionElement
{
    private final RegularExpression regularExpression;

    RegularExpressionCapture(RegularExpression regularExpression)
    {
        super(RegularExpressionElementType.CAPTURE);
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.regularExpression = regularExpression;
    }

    @Override
    public final void regex(final StringBuilder stringBuilder)
    {
        stringBuilder.append('(');
        this.regularExpression.regex(stringBuilder, false);
        stringBuilder.append(')');
    }

    @Override
    public int numberOfGroup()
    {
        return 1 + this.regularExpression.numberOfGroup();
    }
}
