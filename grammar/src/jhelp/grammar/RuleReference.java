package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.util.Objects;

/**
 * Created by jhelp on 21/06/17.
 */
public class RuleReference extends RuleDescription
{
    private final String ruleName;

    public RuleReference(@NotNull String ruleName)
    {
        super(DescriptionType.REFERENCE);
        Objects.requireNonNull(ruleName, "ruleName MUST NOT be null!");
        this.ruleName = ruleName;
    }

    public @NotNull String getRuleName()
    {
        return this.ruleName;
    }

    @Override public String toString()
    {
        return this.ruleName;
    }
}
