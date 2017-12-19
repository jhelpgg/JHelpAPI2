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

/**
 * Node with bounding box
 *
 * @author JHelp
 */
public abstract class NodeWithBox
        extends Node
{
    /**
     * Create a new instance of NodeWithBox
     */
    public NodeWithBox()
    {
    }

    /**
     * Bonding box
     *
     * @return Bonding box
     */
    public abstract @NotNull VirtualBox getBox();

    /**
     * Compute the bounding box and projected it in world space
     *
     * @return Computed projected in world space bounding box
     */
    public @NotNull VirtualBox projectedBox()
    {
        final VirtualBox projected  = new VirtualBox();
        final VirtualBox virtualBox = this.getBox();

        if (virtualBox.empty())
        {
            return projected;
        }

        Point3D point = new Point3D(virtualBox.minX(), virtualBox.minY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.minY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.maxY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.minX(), virtualBox.maxY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.minY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.minY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.maxY(), virtualBox.minZ());
        point = this.projection(point);
        projected.add(point);

        point = new Point3D(virtualBox.maxX(), virtualBox.maxY(), virtualBox.maxZ());
        point = this.projection(point);
        projected.add(point);

        return projected;
    }
}