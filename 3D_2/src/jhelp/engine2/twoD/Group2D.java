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
package jhelp.engine2.twoD;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Group of objects<br>
 * A group is to translate or change visibility of several 2D objects in same time, but have no graphic representation <br>
 *
 * @author JHelp
 */
public class Group2D
{
    /**
     * Developer additional information
     */
    private       Object         additionalInformation;
    /**
     * Members of the group
     */
    private final List<Object2D> members;

    /**
     * Constructs Group2D<br>
     * Create an empty group
     */
    public Group2D()
    {
        this.members = new ArrayList<>();
    }

    /**
     * Add a member to the group
     *
     * @param member Member adds
     */
    public void addMember(final @NotNull Object2D member)
    {
        if (member == null)
        {
            throw new NullPointerException("The member mustn't be null !");
        }

        this.members.add(member);
    }

    /**
     * Actual additionalInformation value
     *
     * @return Actual additionalInformation value
     */
    public @Nullable Object additionalInformation()
    {
        return this.additionalInformation;
    }

    /**
     * Change additionalInformation
     *
     * @param additionalInformation New additionalInformation value
     */
    public void additionalInformation(final @Nullable Object additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }

    /**
     * Translate all members of the group
     *
     * @param x X translation
     * @param y Y translation
     */
    public void translate(final int x, final int y)
    {
        for (final Object2D object2D : this.members)
        {
            object2D.x(object2D.x() + x);
            object2D.y(object2D.y() + y);
        }
    }

    /**
     * Change the visibility of all members of the group
     *
     * @param visible New visibility
     */
    public void visible(final boolean visible)
    {
        for (final Object2D object2D : this.members)
        {
            object2D.visible(visible);
        }
    }
}