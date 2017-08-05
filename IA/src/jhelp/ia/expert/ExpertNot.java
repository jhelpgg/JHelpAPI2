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
