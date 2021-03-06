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
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.ThreadAnimation;
import jhelp.util.list.SortedArray;

/**
 * Animation that change/transform a texture
 *
 * @author JHelp
 */
public class AnimationTextureTransformation
        implements Animation
{
    /**
     * A frame of transformation
     *
     * @author JHelp
     */
    class AnimationTextureFrame
            implements Comparable<AnimationTextureFrame>
    {
        /**
         * Frame number/place
         */
        final int frame;
        /**
         * Transformation apply to a texture
         */
        TextureTransformation textureTransformation;

        /**
         * Create a new instance of AnimationTextureFrame
         *
         * @param frame                 Frame position
         * @param textureTransformation Transformation apply to a texture
         */
        AnimationTextureFrame(final int frame, final @NotNull TextureTransformation textureTransformation)
        {
            this.frame = frame;
            this.textureTransformation = textureTransformation;
        }

        /**
         * Compare this frame with an other one<br>
         * Negative result means this frame is before given one<br>
         * Null results, they are in same place<br>
         * Positive means this frame is after given one <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param animationTextureFrame Frame to compare with
         * @return Comparison result
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(final @NotNull AnimationTextureFrame animationTextureFrame)
        {
            return this.frame - animationTextureFrame.frame;
        }
    }

    /**
     * Animation frames
     */
    private final SortedArray<AnimationTextureFrame> frames;
    /**
     * Current animation index
     */
    private       int                                index;
    /**
     * Frame where animation started
     */
    private       float                              startAbsoluteFrame;
    /**
     * Texture to modify
     */
    private final Texture                            texture;

    /**
     * Create a new instance of AnimationTextureTransformation
     *
     * @param texture Texture to modify
     */
    public AnimationTextureTransformation(final @NotNull Texture texture)
    {
        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        this.frames = new SortedArray<AnimationTextureFrame>(AnimationTextureFrame.class, true);
        this.texture = texture;
    }

    /**
     * Add a frame to the animation.<br>
     * If the frame already exist, the transformation is replaced
     *
     * @param frame                 Frame index
     * @param textureTransformation Texture transformation to add/replace
     * @throws IllegalArgumentException If frame is negative
     */
    public void addFrame(final int frame, final @NotNull TextureTransformation textureTransformation)
    {
        if (frame < 0)
        {
            throw new IllegalArgumentException("frame must be >=0");
        }

        if (textureTransformation == null)
        {
            throw new NullPointerException("textureTransformation mustn't be null");
        }

        final AnimationTextureFrame animationTextureFrame = new AnimationTextureFrame(frame, textureTransformation);
        final int                   index                 = this.frames.indexOf(animationTextureFrame);

        if (index < 0)
        {
            this.frames.add(animationTextureFrame);
        }
        else
        {
            this.frames.get(index).textureTransformation = textureTransformation;
        }
    }

    /**
     * Play the animation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} if animation should continue
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        if (this.index >= this.frames.size())
        {
            return false;
        }

        final float           frame                 = absoluteFrame - this.startAbsoluteFrame;
        AnimationTextureFrame animationTextureFrame = this.frames.get(this.index);

        while (frame >= animationTextureFrame.frame)
        {
            animationTextureFrame.textureTransformation.apply(this.texture);
            this.index++;

            if (this.index >= this.frames.size())
            {
                return false;
            }

            animationTextureFrame = this.frames.get(this.index);
        }

        return this.index < this.frames.size();
    }

    /**
     * Called when animation start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Started frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadAnimation
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.index = 0;
    }
}