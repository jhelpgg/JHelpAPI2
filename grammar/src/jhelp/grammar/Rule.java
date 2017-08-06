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
        return this.name + " = " + this.ruleDescription;
    }
}
