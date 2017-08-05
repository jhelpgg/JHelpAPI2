package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.util.Objects;

/**
 * Created by jhelp on 21/06/17.
 */
public class Rule
{
    private final String          name;
    private final RuleDescription ruleDescription;

    public Rule(@NotNull String name, @NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(name, "name MUST NOT be null!");
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");
        this.name = name;
        this.ruleDescription = ruleDescription;
    }

    public @NotNull String getName()
    {
        return this.name;
    }

    public @NotNull RuleDescription getRuleDescription()
    {
        return this.ruleDescription;
    }

    @Override public String toString()
    {
        return this.name+" = "+this.ruleDescription;
    }
}
