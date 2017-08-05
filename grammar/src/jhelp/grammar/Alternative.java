package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jhelp.util.list.EnumerationIterator;

/**
 * Created by jhelp on 21/06/17.
 */
public class Alternative extends RuleDescription implements Iterable<RuleDescription>
{
    private final List<RuleDescription> alternatives;

    public Alternative()
    {
        super(DescriptionType.ALTERNATIVE);
        this.alternatives = new ArrayList<>();
    }

    public void addAlternative(@NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");

        if (ruleDescription.getDescriptionType() == DescriptionType.ALTERNATIVE)
        {
            throw new IllegalArgumentException("Alternative can't be add inside an alternative");
        }

        this.alternatives.add(ruleDescription);
    }

    public int numberOfAlternative()
    {
        return this.alternatives.size();
    }

    public @NotNull RuleDescription getAlternative(int index)
    {
        return this.alternatives.get(index);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<RuleDescription> iterator()
    {
        return new EnumerationIterator<>(this.alternatives.iterator(), false);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        boolean       first         = true;

        for (RuleDescription ruleDescription : this.alternatives)
        {
            if (!first)
            {
                stringBuilder.append("\n\t| ");
            }

            first = false;
            stringBuilder.append(ruleDescription);
        }

        return stringBuilder.toString();
    }
}
