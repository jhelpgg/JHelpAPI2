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
