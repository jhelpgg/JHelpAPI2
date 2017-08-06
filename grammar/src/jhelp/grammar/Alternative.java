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

    public int numberOfAlternative()
    {
        return this.alternatives.size();
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
