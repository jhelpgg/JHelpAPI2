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
public class GroupDescription extends RuleDescription implements Iterable<RuleDescription>
{
    private final List<RuleDescription> group;

    public GroupDescription()
    {
        super(DescriptionType.GROUP);
        this.group = new ArrayList<>();
    }

    public void addElement(@NotNull RuleDescription ruleDescription)
    {
        Objects.requireNonNull(ruleDescription, "ruleDescription MUST NOT be null!");

        if (ruleDescription.getDescriptionType() == DescriptionType.ALTERNATIVE)
        {
            throw new IllegalArgumentException("Alternative can't be add inside a group");
        }

        if (ruleDescription.getDescriptionType() == DescriptionType.GROUP)
        {
            GroupDescription groupDescription = (GroupDescription) ruleDescription;

            for (RuleDescription description : groupDescription)
            {
                this.addElement(description);
            }

            return;
        }

        this.group.add(ruleDescription);
    }

    public int numberOfElement()
    {
        return this.group.size();
    }

    public @NotNull RuleDescription getElement(int index)
    {
        return this.group.get(index);
    }

    /**
     * Returns an iterator over elements of type {@code RuleDescription}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<RuleDescription> iterator()
    {
        return new EnumerationIterator<>(this.group.iterator(), false);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        boolean       first         = true;

        for (RuleDescription ruleDescription : this.group)
        {
            if (!first)
            {
                stringBuilder.append(" ");
            }

            first = false;
            stringBuilder.append(ruleDescription);
        }

        return stringBuilder.toString();
    }
}
