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
import jhelp.util.list.ArrayObject;

class RegularExpressionChoice extends RegularExpressionElement
{
    private final ArrayObject<RegularExpression> regularExpressions;

    RegularExpressionChoice(RegularExpression first, RegularExpression second, RegularExpression... others)
    {
        super(RegularExpressionElementType.CHOICE);
        Objects.requireNonNull(first, "first MUST NOT be null!");
        Objects.requireNonNull(second, "second MUST NOT be null!");
        this.regularExpressions = new ArrayObject<>();
        this.regularExpressions.add(first);
        this.regularExpressions.add(second);

        if (others != null)
        {
            for (RegularExpression regularExpression : others)
            {
                if (regularExpression != null)
                {
                    this.regularExpressions.add(regularExpression);
                }
            }
        }
    }

    void appendChoice(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.regularExpressions.add(regularExpression);
    }

    @Override
    public final void regex(final StringBuilder stringBuilder)
    {
        int size = this.regularExpressions.size();
        this.regularExpressions.get(0).regex(stringBuilder, true);

        for (int index = 1; index < size; index++)
        {
            stringBuilder.append('|');
            this.regularExpressions.get(index).regex(stringBuilder, true);
        }
    }

    @Override
    public int numberOfGroup()
    {
        int number = 0;

        for (RegularExpression regularExpression : this.regularExpressions)
        {
            number += regularExpression.numberOfGroup();

            if (regularExpression.complex())
            {
                number++;
            }
        }

        return number;
    }
}
