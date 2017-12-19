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

package jhelp.gui.smooth;

/**
 * Generic container
 *
 * @author JHelp
 */
public abstract class JHelpContainerSmooth
        extends JHelpComponentSmooth
{
    /**
     * Create a new instance of JHelpContainerSmooth
     */
    public JHelpContainerSmooth()
    {
    }

    /**
     * Obtain a component of container
     *
     * @param index Component index
     * @return Component at index
     */
    public abstract JHelpComponentSmooth child(int index);

    /**
     * Number of children
     *
     * @return Number of children
     */
    public abstract int numberOfChildren();

}