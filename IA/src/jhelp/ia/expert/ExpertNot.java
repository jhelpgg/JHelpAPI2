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

package jhelp.ia.expert;

import com.sun.istack.internal.NotNull;
import java.util.Objects;

/**
 * Created by jhelp on 01/07/17.
 */
public final class ExpertNot implements ExpertCondition
{
    private final ExpertCondition expertCondition;

    public ExpertNot(@NotNull ExpertCondition expertCondition)
    {
        Objects.requireNonNull(expertCondition, "expertCondition MUST NOT be null!");
        this.expertCondition = expertCondition;
    }

    public ExpertCondition expertCondition()
    {
        return this.expertCondition;
    }

    @Override
    public boolean valid()
    {
        return !this.expertCondition.valid();
    }
}
