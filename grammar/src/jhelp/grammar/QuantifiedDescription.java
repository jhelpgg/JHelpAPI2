package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.util.io.ByteArray;

/**
 * Created by jhelp on 21/06/17.
 */
public class QuantifiedDescription extends RuleDescription
{
    private static void checkDescriptionType(@NotNull RuleDescription ruleDescription)
    {
        if (ruleDescription.getDescriptionType() == DescriptionType.ALTERNATIVE
            || ruleDescription.getDescriptionType() == DescriptionType.REGULAR_EXPRESSION
            || ruleDescription.getDescriptionType() == DescriptionType.QUANTIFIED)
        {
            throw new IllegalArgumentException(
                    ruleDescription.getDescriptionType() + " expression can't be quantified!");
        }
    }

    public static @NotNull RuleDescription anyNumber(@NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        QuantifiedDescription.checkDescriptionType(ruleDescription);
        return new QuantifiedDescription(0, Integer.MAX_VALUE, ruleDescription);
    }

    public static @NotNull RuleDescription atLeastOne(@NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        QuantifiedDescription.checkDescriptionType(ruleDescription);
        return new QuantifiedDescription(1, Integer.MAX_VALUE, ruleDescription);
    }

    public static @NotNull RuleDescription zeroOrOne(@NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        QuantifiedDescription.checkDescriptionType(ruleDescription);
        return new QuantifiedDescription(0, 1, ruleDescription);
    }

    public static @NotNull RuleDescription exactly(@NotNull RuleDescription ruleDescription, int number)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        QuantifiedDescription.checkDescriptionType(ruleDescription);

        if (number <= 0)
        {
            throw new IllegalArgumentException("number MUST be at least 1, not " + number);
        }

        return new QuantifiedDescription(number, number, ruleDescription);
    }

    public static @NotNull RuleDescription between(@NotNull RuleDescription ruleDescription, int minimum, int maximum)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        QuantifiedDescription.checkDescriptionType(ruleDescription);

        if (minimum < 0)
        {
            throw new IllegalArgumentException("minimum MUST be positive or zero, not " + minimum);
        }

        if (maximum <= 0)
        {
            throw new IllegalArgumentException("maximum MUST be at least 1, not " + maximum);
        }

        if (minimum > maximum)
        {
            throw new IllegalArgumentException(
                    "minimum MUST be lower or equal than maximum, here minimum=" + minimum + " maximum=" + maximum);
        }

        return new QuantifiedDescription(minimum, maximum, ruleDescription);
    }

    private final int             minimum;
    private final int             maximum;
    private final RuleDescription quantifiedRule;

    private QuantifiedDescription(int minimum, int maximum, @NotNull RuleDescription quantifiedRule)
    {
        super(DescriptionType.QUANTIFIED);
        this.minimum = minimum;
        this.maximum = maximum;
        this.quantifiedRule = quantifiedRule;
    }

    public int getMinimum()
    {
        return this.minimum;
    }

    public int getMaximum()
    {
        return this.maximum;
    }

    public @NotNull RuleDescription getQuantifiedRule()
    {
        return this.quantifiedRule;
    }

    @Override public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        boolean parenthesis = this.quantifiedRule.getDescriptionType() == DescriptionType.GROUP &&
                              ((GroupDescription) this.quantifiedRule).numberOfElement() > 1;

        if (parenthesis)
        {
            stringBuilder.append("(");
        }

        stringBuilder.append(this.quantifiedRule);

        if (parenthesis)
        {
            stringBuilder.append(")");
        }

        if (this.minimum == 0 && this.maximum == Integer.MAX_VALUE)
        {
            stringBuilder.append("*");
        }
        else if (this.minimum == 0 && this.maximum == 1)
        {
            stringBuilder.append("?");
        }
        else if (this.minimum == 1 && this.maximum == Integer.MAX_VALUE)
        {
            stringBuilder.append("+");
        }
        else if (this.minimum == this.maximum)
        {
            stringBuilder.append("{");
            stringBuilder.append(this.minimum);
            stringBuilder.append("}");
        }
        else
        {
            stringBuilder.append("{");
            stringBuilder.append(this.minimum);
            stringBuilder.append(", ");
            stringBuilder.append(this.maximum);
            stringBuilder.append("}");
        }

        return stringBuilder.toString();
    }
}
