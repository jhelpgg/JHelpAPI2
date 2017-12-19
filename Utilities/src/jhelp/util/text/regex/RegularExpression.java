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

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import java.util.regex.Pattern;
import jhelp.util.list.ArrayInt;
import jhelp.util.list.ArrayObject;

public final class RegularExpression
{
    static
    {
        LETTER = RegularExpression.string("\\w");

        WORD = new RegularExpression();
        RegularExpression.WORD.appendAtLeastOne(RegularExpression.LETTER);
        RegularExpression.WORD.immutable();

        STRING = new RegularExpression();
        RegularExpression.STRING.appendString("\"");
        RegularExpression.STRING.appendAny(RegularExpression.choice(RegularExpression.string("\\\\\""),
                                                                    RegularExpression.string("[^\"]")));
        RegularExpression.STRING.appendString("\"");
        RegularExpression.STRING.immutable();

        RegularExpression space = RegularExpression.string("\\s");
        SPACE = new RegularExpression();
        RegularExpression.SPACE.appendAny(space);
        RegularExpression.SPACE.immutable();
    }

    public static final RegularExpression LETTER;
    public static final RegularExpression SPACE;
    public static final RegularExpression STRING;
    public static final RegularExpression WORD;

    public static RegularExpression capture(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        RegularExpression capture = new RegularExpression();
        capture.appendCapture(regularExpression);
        capture.immutable();
        return capture;
    }

    public static RegularExpression choice(
            RegularExpression first, RegularExpression second, RegularExpression... others)
    {
        RegularExpression choice = new RegularExpression();
        choice.appendChoice(first, second, others);
        choice.immutable();
        return choice;
    }

    public static RegularExpression string(String string)
    {
        RegularExpression regularExpression = new RegularExpression();
        regularExpression.appendString(string);
        regularExpression.immutable();
        return regularExpression;
    }

    private final ArrayInt                              captureGroups;
    private final ArrayObject<RegularExpressionElement> elements;
    private       boolean                               mutable;
    private       int                                   numberOfGroup;
    private       Pattern                               pattern;
    private       String                                regex;

    public RegularExpression()
    {
        this.elements = new ArrayObject<>();
        this.captureGroups = new ArrayInt();
        this.mutable = true;
    }

    void regex(@NotNull StringBuilder stringBuilder, boolean complexityTest)
    {
        if (this.regex == null)
        {
            final StringBuilder regex = new StringBuilder();

            for (RegularExpressionElement regularExpressionElement : this.elements)
            {
                regularExpressionElement.regex(regex);

                if (regularExpressionElement.regularExpressionElementType() == RegularExpressionElementType.CAPTURE)
                {
                    this.captureGroups.add(this.numberOfGroup + 1);
                }

                this.numberOfGroup += regularExpressionElement.numberOfGroup();
            }

            this.regex = regex.toString();
        }

        boolean addParenthesis = complexityTest && this.complex();

        if (addParenthesis)
        {
            stringBuilder.append('(');
        }

        stringBuilder.append(this.regex);

        if (addParenthesis)
        {
            stringBuilder.append(')');
        }
    }

    public void appendAny(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.refresh();
        this.elements.add(new RegularExpressionAny(regularExpression));
    }

    public void appendAtLeastOne(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.refresh();
        this.elements.add(new RegularExpressionAtLeastOne(regularExpression));
    }

    public void appendCapture(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.refresh();
        this.elements.add(new RegularExpressionCapture(regularExpression));
    }

    public void appendChoice(RegularExpression first, RegularExpression second, RegularExpression... others)
    {
        this.refresh();
        this.elements.add(new RegularExpressionChoice(first, second, others));
    }

    public void appendRegularExpression(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.refresh();
        this.elements.addAll(regularExpression.elements);
    }

    public void appendString(String string)
    {
        Objects.requireNonNull(string, "string MUST NOT be null!");

        if (string.length() == 0)
        {
            return;
        }

        this.refresh();

        if (this.elements.size() > 0)
        {
            RegularExpressionElement previous = this.elements.last();

            if (previous.regularExpressionElementType() == RegularExpressionElementType.STRING)
            {
                string = ((RegularExpressionString) previous).string() + string;
                this.elements.set(this.elements.size() - 1, new RegularExpressionString(string));
                return;
            }
        }

        this.elements.add(new RegularExpressionString(string));
    }

    public void appendZeroOrOne(RegularExpression regularExpression)
    {
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.refresh();
        this.elements.add(new RegularExpressionZeroOrOne(regularExpression));
    }

    public boolean complex()
    {
        int size = this.elements.size();

        if (size == 0)
        {
            return false;
        }

        if (size > 1)
        {
            return true;
        }

        RegularExpressionElementType regularExpressionElementType = this.elements.first()
                                                                                 .regularExpressionElementType();
        return regularExpressionElementType == RegularExpressionElementType.CHOICE ||
               regularExpressionElementType == RegularExpressionElementType.ZERO_OR_ONE ||
               regularExpressionElementType == RegularExpressionElementType.AT_LEAST_ONE ||
               regularExpressionElementType == RegularExpressionElementType.ANY;
    }

    public int groupOfCapture(int captureIndex)
    {
        if (captureIndex < 0 || captureIndex >= this.captureGroups.size())
        {
            return -1;
        }

        return this.captureGroups.getInteger(captureIndex);
    }

    public void immutable()
    {
        this.mutable = false;
    }

    public boolean mutable()
    {
        return this.mutable;
    }

    public int numberOfGroup()
    {
        return this.numberOfGroup;
    }

    public Pattern pattern()
    {
        if (this.pattern == null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            this.regex(stringBuilder);
            this.pattern = Pattern.compile(stringBuilder.toString());
        }

        return this.pattern;
    }

    public void refresh()
    {
        if (!this.mutable)
        {
            throw new IllegalStateException("This regular  expression is not mutable");
        }

        this.captureGroups.clear();
        this.pattern = null;
        this.regex = null;
        this.numberOfGroup = 0;
    }

    public void regex(@NotNull StringBuilder stringBuilder)
    {
        this.regex(stringBuilder, false);
    }
}
