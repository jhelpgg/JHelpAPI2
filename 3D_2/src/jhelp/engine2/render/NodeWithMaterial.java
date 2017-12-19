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
package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Node with material <br>
 *
 * @author JHelp
 */
public abstract class NodeWithMaterial
        extends NodeWithBox
{
    /**
     * Tow side "philosophy" <br>
     *
     * @author JHelp
     */
    public enum TwoSidedState
    {
        /**
         * Use the material setting for the tow side mode
         */
        AS_MATERIAL,
        /**
         * Force the object be one sided
         */
        FORCE_ONE_SIDE,
        /**
         * Force the object be 2 sided
         */
        FORCE_TWO_SIDE
    }

    /**
     * 2 sided "philosophy"
     */
    private TwoSidedState twoSidedState = TwoSidedState.AS_MATERIAL;

    /**
     * Object material
     *
     * @return Object material
     */
    public abstract @NotNull Material material();

    /**
     * Change material
     *
     * @param material New material
     */
    public abstract void material(@NotNull Material material);

    /**
     * Selection material
     *
     * @return Selection material
     */
    public abstract @Nullable Material materialForSelection();

    /**
     * Define material for selection
     *
     * @param materialForSelection New selection material
     */
    public abstract void materialForSelection(@Nullable Material materialForSelection);

    /**
     * Return twoSidedState
     *
     * @return twoSidedState
     */
    public final @NotNull TwoSidedState twoSidedState()
    {
        return this.twoSidedState;
    }

    /**
     * Modify twoSidedState
     *
     * @param twoSidedState New twoSidedState value
     */
    public final void twoSidedState(final @NotNull TwoSidedState twoSidedState)
    {
        if (twoSidedState == null)
        {
            throw new NullPointerException("twoSidedState mustn't be null");
        }

        this.twoSidedState = twoSidedState;
    }
}