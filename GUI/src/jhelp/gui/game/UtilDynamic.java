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
package jhelp.gui.game;

import jhelp.util.gui.dynamic.AnimationPosition;
import jhelp.util.gui.dynamic.Interpolations;
import jhelp.util.gui.dynamic.JHelpDynamicImage;
import jhelp.util.gui.dynamic.Position;

/**
 * Utilities for dynamic game
 *
 * @author JHelp
 */
public class UtilDynamic
{
    /**
     * Create an animation that change the position of a sensitive animation
     *
     * @param sensitiveAnimation Animation to move
     * @param destinationX       Future location X
     * @param destinationY       Future location Y
     * @param millisecond        Time, in milliseconds, for go from actual position to the specified one
     * @return Created animation
     */
    public static AnimationPosition createTranslateSensitiveAnimation(
            final SensitiveAnimation sensitiveAnimation, final int destinationX,
            final int destinationY, final int millisecond)
    {
        if (sensitiveAnimation == null)
        {
            throw new NullPointerException("sensitiveAnimation mustn't be null");
        }

        final AnimationPosition animationPosition = new AnimationPosition(sensitiveAnimation, 1, Interpolations.SINUS);
        animationPosition.addFrame(Math.max(1, UtilDynamic.millisecondToNumberOfFrame(millisecond)),
                                   new Position(destinationX, destinationY));
        return animationPosition;
    }

    /**
     * Create an animation that change the position of a sensitive element
     *
     * @param sensitiveElement Element to move
     * @param destinationX     Future location X
     * @param destinationY     Future location Y
     * @param millisecond      Time, in milliseconds, for go from actual position to the specified one
     * @return Created animation
     */
    public static AnimationPosition createTranslateSensitiveElement(
            final SensitiveElement sensitiveElement, final int destinationX, final int destinationY,
            final int millisecond)
    {
        if (sensitiveElement == null)
        {
            throw new NullPointerException("sensitiveElement mustn't be null");
        }

        final AnimationPosition animationPosition = new AnimationPosition(sensitiveElement, 1, Interpolations.SINUS);
        animationPosition.addFrame(Math.max(1, UtilDynamic.millisecondToNumberOfFrame(millisecond)),
                                   new Position(destinationX, destinationY));
        return animationPosition;
    }

    /**
     * Transform time, in milliseconds, to number of frame
     *
     * @param millisecond Time, in milliseconds, to convert
     * @return Number of frame
     */
    public static int millisecondToNumberOfFrame(final int millisecond)
    {
        return (millisecond * JHelpDynamicImage.FPS) / 1000;
    }

    /**
     * Create and play an animation that translate a sensitive animation
     *
     * @param gameDynamic        Game dynamic where the animation will be play
     * @param sensitiveAnimation Animation to move
     * @param destinationX       Future location X
     * @param destinationY       Future location Y
     * @param millisecond        Time, in milliseconds, for go from actual position to the specified one
     */
    public static void translateSensitiveAnimation(
            final JHelpGameDynamic gameDynamic, final SensitiveAnimation sensitiveAnimation, final int destinationX,
            final int destinationY, final int millisecond)
    {
        gameDynamic.playAnimation(
                UtilDynamic.createTranslateSensitiveAnimation(sensitiveAnimation, destinationX, destinationY,
                                                              millisecond));
    }

    /**
     * Create and play an animation that translate a sensitive element
     *
     * @param gameDynamic      Game dynamic where the animation will be play
     * @param sensitiveElement Element to move
     * @param destinationX     Future location X
     * @param destinationY     Future location Y
     * @param millisecond      Time, in milliseconds, for go from actual position to the specified one
     */
    public static void translateSensitiveElement(
            final JHelpGameDynamic gameDynamic, final SensitiveElement sensitiveElement, final int destinationX,
            final int destinationY, final int millisecond)
    {
        gameDynamic.playAnimation(
                UtilDynamic.createTranslateSensitiveElement(sensitiveElement, destinationX, destinationY, millisecond));
    }
}