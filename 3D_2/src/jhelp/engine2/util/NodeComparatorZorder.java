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
package jhelp.engine2.util;

import java.util.Comparator;
import jhelp.engine2.render.Node;

/**
 * Compare two nodes in Z Order <br>
 *
 * @author JHelp
 */
public class NodeComparatorZorder
        implements Comparator<Node>
{
    /**
     * Node comparator Z-order singleton
     */
    public static final NodeComparatorZorder NODE_COMPARATOR_Z_ORDER = new NodeComparatorZorder();

    /**
     * Constructs NodeComparator
     */
    private NodeComparatorZorder()
    {
    }

    /**
     * Compare two nodes with there Z-order
     *
     * @param node1 Node 1
     * @param node2 Node 2
     * @return Result
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(final Node node1, final Node node2)
    {
        if (Math3D.equal(node1.zOrder, node2.zOrder))
        {
            return 0;
        }

        if (node1.zOrder < node2.zOrder)
        {
            return -1;
        }

        return 1;
    }

}
