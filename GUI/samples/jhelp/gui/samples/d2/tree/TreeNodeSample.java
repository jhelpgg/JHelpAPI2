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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.samples.d2.tree;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tree node
 *
 * @author JHelp
 */
public class TreeNodeSample
{
    /** Next trre node ID */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    /** Indicates if node is exapnd */
    private       boolean expand;
    /** Node ID */
    private final int     id;
    /** Carry message */
    private final String  message;

    /**
     * Create a new instance of TreeNodeSample
     *
     * @param message
     *           Message to carry
     */
    public TreeNodeSample(final String message)
    {
        synchronized (TreeNodeSample.NEXT_ID)
        {
            this.id = TreeNodeSample.NEXT_ID.getAndIncrement();
        }

        this.message = message;
        this.expand = false;
    }

    /**
     * Node ID
     *
     * @return Node ID
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Carry message
     *
     * @return Carry message
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * Indicates if node is exapnd
     *
     * @return {@code true} if node is expand
     */
    public boolean isExpand()
    {
        return this.expand;
    }

    /**
     * Cahnge expand state
     *
     * @param expand
     *           New expand state
     */
    void setExpand(final boolean expand)
    {
        this.expand = expand;
    }
}