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
package jhelp.engine2.animation;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.ThreadAnimation;
import jhelp.engine2.util.PositionNode;

/**
 * Animation witch move a node
 *
 * @author JHelp
 */
public class AnimationPositionNode
        extends AnimationKeyFrame<Node, PositionNode>
{
    /**
     * Constructs AnimationPositionNode
     *
     * @param object Node to move
     */
    public AnimationPositionNode(final @NotNull Node object)
    {
        super(object);
    }

    /**
     * Interpolate a value
     *
     * @param object  Node to move
     * @param before  Position just before the computed position
     * @param after   Position just after the computed position
     * @param percent Percent of interpolation
     * @see AnimationKeyFrame#interpolateValue(Object, Object, Object, float)
     */
    @Override
    @ThreadAnimation
    protected void interpolateValue(
            final @NotNull Node object,
            final @NotNull PositionNode before, final @NotNull PositionNode after,
            final float percent)
    {
        float anti;

        anti = 1f - percent;

        object.position((before.x * anti) + (after.x * percent), (before.y * anti) + (after.y * percent),
                        (before.z * anti) + (after.z * percent));
        object.angleX((before.angleX * anti) + (after.angleX * percent));
        object.angleY((before.angleY * anti) + (after.angleY * percent));
        object.angleZ((before.angleZ * anti) + (after.angleZ * percent));
        object.setScale((before.scaleX * anti) + (after.scaleX * percent),
                        (before.scaleY * anti) + (after.scaleY * percent), (before.scaleZ * anti)
                                                                           + (after.scaleZ * percent));
    }

    /**
     * Compute node position
     *
     * @param object Node to get it's position
     * @return Node's position
     * @see AnimationKeyFrame#obtainValue(Object)
     */
    @Override
    @ThreadAnimation
    protected @NotNull PositionNode obtainValue(final @NotNull Node object)
    {
        return new PositionNode(object);
    }

    /**
     * Change node position
     *
     * @param object Node to change
     * @param value  New value
     * @see AnimationKeyFrame#setValue(Object, Object)
     */
    @Override
    @ThreadAnimation
    protected void setValue(final @NotNull Node object, final @NotNull PositionNode value)
    {
        object.position(value.x, value.y, value.z);
        object.angleX(value.angleX);
        object.angleY(value.angleY);
        object.angleZ(value.angleZ);
        object.setScale(value.scaleX, value.scaleY, value.scaleZ);
    }
}