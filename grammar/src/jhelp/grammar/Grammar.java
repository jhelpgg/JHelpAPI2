package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.list.SizedIterable;

/**
 * Created by jhelp on 21/06/17.
 */
public class Grammar implements SizedIterable<Rule>
{
    private final List<Rule> rules;

    public Grammar()
    {
        this.rules = new ArrayList<>();
    }

    public void addRule(@NotNull Rule rule)
    {
        Objects.requireNonNull(rule, "rule MUST NOT be null!");

        if (this.findRule(rule.getName()) != null)
        {
            throw new IllegalArgumentException("A rule named " + rule.getName() + " is already registered!");
        }

        this.rules.add(rule);
    }

    public @Nullable Rule findRule(@NotNull String name)
    {
        for (Rule rule : this.rules)
        {
            if (rule.getName().equals(name))
            {
                return rule;
            }
        }

        return null;
    }

    @Override
    public int size()
    {
        return this.rules.size();
    }

    public @NotNull Rule getRule(int index)
    {
        return this.rules.get(index);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<Rule> iterator()
    {
        return new EnumerationIterator<>(this.rules.iterator(), false);
    }

    @Override public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Rule rule : this.rules)
        {
            stringBuilder.append(rule);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
