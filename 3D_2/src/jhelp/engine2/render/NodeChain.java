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
import jhelp.engine2.animation.Animation;

/**
 * Chain of node.<br>
 * All nodes are attached to previous one. Move the head, will move other nodes when animation refresh.<br>
 * All node must be placed to their start position, constraints are computed at creation of the chain and can't be changed after
 * (For now)
 *
 * @author JHelp
 */
public class NodeChain
        implements Animation
{
    /**
     * Main node, the head, that all other nodes will follow
     */
    private final Node      head;
    /**
     * Queue size
     */
    private final int       length;
    /**
     * Links/constraints to respect
     */
    private final Bone[]    links;
    /**
     * Points reference
     */
    private final Point3D[] points;
    /**
     * Chain queue
     */
    private final Node[]    queue;

    /**
     * Create a new instance of NodeChain
     *
     * @param head  Head node (All other nodes will follow it)
     * @param queue Queue list, must have at least 1 node. Order is important, first of the queue follow the head, second follow the
     *              first, third follow the second, ...
     */
    public NodeChain(final @NotNull Node head, final @NotNull Node... queue)
    {
        this.length = queue.length;

        if (this.length < 1)
        {
            throw new IllegalArgumentException("Must have at least one element in the queue");
        }

        this.head = head;
        this.queue = queue;
        this.links = new Bone[this.length];
        this.points = new Point3D[this.length + 1];

        this.points[0] = new Point3D(head.x(), head.y(), head.z());

        for (int i = 0; i < this.length; i++)
        {
            this.points[i + 1] = new Point3D(queue[i].x(), queue[i].y(), queue[i].z());
            this.links[i] = new Bone(this.points[i], this.points[i + 1]);
        }
    }

    /**
     * Animate node to respects constraints depends on head last moves <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} because its a never ending animation. Need to use {@link Window3D#stopAnimation(Animation)}
     * to stop it
     * @see Animation#animate(float)
     */
    @Override
    @ThreadOpenGL
    public boolean animate(final float absoluteFrame)
    {
        this.points[0].set(this.head.x(), this.head.y(), this.head.z());

        Point3D point;
        for (int i = 0; i < this.length; i++)
        {
            this.links[i].updateBone();
            point = this.points[i + 1];
            this.queue[i].position(point.x, point.y, point.z);
        }

        return true;
    }

    /**
     * Called when animation initialized <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Starting frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadOpenGL
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
    }
}