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
package jhelp.gui.smooth.shape;

/**
 * Shadow level
 *
 * @author JHelp
 */
public enum ShadowLevel
{
    /**
     * Component far, shadow is big
     */
    FAR(9),
    /**
     * Component near, shadow is small
     */
    NEAR(3),
    /**
     * Component is on the container, no shadow
     */
    NO_SHADOW(0),
    /**
     * Component at normal distance, shadow is normal
     */
    NORMAL(5);
    /**
     * Number of pixels for shadow size
     */
    private final int numberOfPixels;

    /**
     * Create a new instance of ShadowLevel
     *
     * @param numberOfPixels Number of pixels for shadow size
     */
    ShadowLevel(final int numberOfPixels)
    {
        this.numberOfPixels = numberOfPixels;
    }

    /**
     * Number of pixels for shadow size
     *
     * @return Number of pixels for shadow size
     */
    public int numberOfPixels()
    {
        return this.numberOfPixels;
    }
}