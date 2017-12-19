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

import jhelp.engine2.render.ThreadAnimation;

/**
 * Animation that not need Open GL and direct link to JOGL.<br>
 * So can be used from external easily
 *
 * @author JHelp
 */
public interface ExternalAnimation
{
    /**
     * Called when animation initialized
     */
    @ThreadAnimation
    void initializeAnimation();

    /**
     * Called when animation refreshed
     *
     * @param frame Animation frame
     * @return {@code true} if animation have to continue. {@code false} if animation finished
     */
    @ThreadAnimation
    boolean playAnimation(final float frame);
}