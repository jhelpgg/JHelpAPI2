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

package jhelp.antology;

import com.sun.istack.internal.NotNull;

/**
 * Visitor of graph
 */
public interface GraphVisitor
{
    /**
     * Called when enter inside a node
     *
     * @param node Node entered
     */
    void enterNode(@NotNull Node node);

    /**
     * Called when exit a node
     *
     * @param node Node exited
     */
    void exitNode(@NotNull Node node);
}
