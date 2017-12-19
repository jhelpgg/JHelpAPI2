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
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.TextureInterpolator;
import jhelp.engine2.render.ThreadAnimation;
import jhelp.engine2.twoD.Object2D;
import jhelp.util.text.UtilText;

/**
 * Animate a texture
 *
 * @author JHelp
 */
public class AnimationTexture
        implements Animation
{
    /**
     * Create an animation texture for color to grey or grey to color
     *
     * @param numberOfFrame     Number of frame to do the transition
     * @param texture           Texture to modify
     * @param pingPong          Indicates if transformation are ping-pong
     * @param numberOfLoop      Number of loop to repeat the transformation
     * @param interpolationType Interpolation type
     * @param toGray            Grey way. {@code true} goto grey. {@code false} goto color
     * @return Created Animation
     */
    public static @NotNull AnimationTexture graySwitch(
            final int numberOfFrame, final @NotNull Texture texture,
            final boolean pingPong, final int numberOfLoop,
            final @NotNull TextureInterpolator.InterpolationType interpolationType,
            final boolean toGray)
    {
        final Texture gray = new Texture(texture.textureName() + "_gray", texture.width(), texture.height());
        gray.setPixels(texture);
        gray.toGray();

        if (toGray)
        {
            return new AnimationTexture(numberOfFrame, texture, gray, pingPong, numberOfLoop, interpolationType);
        }

        return new AnimationTexture(numberOfFrame, gray, texture, pingPong, numberOfLoop, interpolationType);
    }

    /**
     * Number of loop left
     */
    private       int                 loopLeft;
    /**
     * Numbre of frame to do the transition between 2 textures
     */
    private final float               numberOfFrame;
    /**
     * Number total of loop
     */
    private final int                 numberOfLoop;
    /**
     * Indicates if its a "ping-pong" animation
     */
    private final boolean             pingPong;
    /**
     * Start of animation
     */
    private       float               startAbsoluteFrame;
    /**
     * Interpolator of textures
     */
    private final TextureInterpolator textureInterpolator;
    /**
     * Indicates if interpolation goes up
     */
    private       boolean             wayUp;

    /**
     * Create a new instance of AnimationTexture played one time only
     *
     * @param numberOfFrame Number of frame to interpolate 2 textures
     * @param textureStart  Texture start
     * @param textureEnd    Texture end
     */
    public AnimationTexture(
            final int numberOfFrame, final @NotNull Texture textureStart, final @NotNull Texture textureEnd)
    {
        this(numberOfFrame, textureStart, textureEnd, false, 1, TextureInterpolator.InterpolationType.UNDEFINED);
    }

    /**
     * Create a new instance of AnimationTexture played "infinite" time
     *
     * @param numberOfFrame Number of frame to interpolate 2 textures
     * @param textureStart  Texture start
     * @param textureEnd    Texture end
     * @param pingPong      Indicates if its a "ping-pong" animation
     */
    public AnimationTexture(
            final int numberOfFrame,
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd,
            final boolean pingPong)
    {
        this(numberOfFrame, textureStart, textureEnd, pingPong, Integer.MAX_VALUE,
             TextureInterpolator.InterpolationType.UNDEFINED);
    }

    /**
     * Create a new instance of AnimationTexture a number of time (For "infinite" you can use {@link Integer#MAX_VALUE})
     *
     * @param numberOfFrame Number of frame to interpolate 2 textures
     * @param textureStart  Texture start
     * @param textureEnd    Texture end
     * @param pingPong      Indicates if its a "ping-pong" animation
     * @param numberOfLoop  Number of loop (For "infinite" you can use {@link Integer#MAX_VALUE})
     */
    public AnimationTexture(
            final int numberOfFrame,
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd,
            final boolean pingPong, final int numberOfLoop)
    {
        this(numberOfFrame, textureStart, textureEnd, pingPong, numberOfLoop,
             TextureInterpolator.InterpolationType.UNDEFINED);
    }

    /**
     * Create a new instance of AnimationTexture a number of time (For "infinite" you can use {@link Integer#MAX_VALUE})
     *
     * @param numberOfFrame     Number of frame to interpolate 2 textures
     * @param textureStart      Texture start
     * @param textureEnd        Texture end
     * @param pingPong          Indicates if its a "ping-pong" animation
     * @param numberOfLoop      Number of loop (For "infinite" you can use {@link Integer#MAX_VALUE})
     * @param interpolationType Interpolation type
     */
    public AnimationTexture(
            final int numberOfFrame,
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd,
            final boolean pingPong, final int numberOfLoop,
            final TextureInterpolator.InterpolationType interpolationType)
    {
        this.pingPong = pingPong;
        this.numberOfLoop = Math.max(1, numberOfLoop);
        this.loopLeft = this.numberOfLoop;
        this.numberOfFrame = Math.max(1, numberOfFrame);
        final String name = UtilText.concatenate(textureStart.textureName(),
                                                 "_",
                                                 textureEnd.textureName(),
                                                 "_interpolation");
        this.textureInterpolator = new TextureInterpolator(textureStart, textureEnd, name, interpolationType);
    }

    /**
     * Create a new instance of AnimationTexture played "infinite" time
     *
     * @param numberOfFrame     Number of frame to interpolate 2 textures
     * @param textureStart      Texture start
     * @param textureEnd        Texture end
     * @param pingPong          Indicates if its a "ping-pong" animation
     * @param interpolationType Interpolation type
     */
    public AnimationTexture(
            final int numberOfFrame,
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd,
            final boolean pingPong,
            final @NotNull TextureInterpolator.InterpolationType interpolationType)
    {
        this(numberOfFrame, textureStart, textureEnd, pingPong, Integer.MAX_VALUE, interpolationType);
    }

    /**
     * Create a new instance of AnimationTexture played one time only
     *
     * @param numberOfFrame     Number of frame to interpolate 2 textures
     * @param textureStart      Texture start
     * @param textureEnd        Texture end
     * @param interpolationType Interpaolation type
     */
    public AnimationTexture(
            final int numberOfFrame,
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd,
            final @NotNull TextureInterpolator.InterpolationType interpolationType)
    {
        this(numberOfFrame, textureStart, textureEnd, false, 1, interpolationType);
    }

    /**
     * Called each time animation refresh <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} if animation have to continue
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        float   frame   = absoluteFrame - this.startAbsoluteFrame;
        boolean anOther = frame < this.numberOfFrame;

        if (!anOther)
        {
            frame = this.numberOfFrame;
        }

        if (!this.wayUp)
        {
            frame = this.numberOfFrame - frame;
        }

        if (!anOther)
        {
            this.startAbsoluteFrame = absoluteFrame;

            if (this.pingPong)
            {
                if (this.wayUp)
                {
                    anOther = true;
                }

                this.wayUp = !this.wayUp;
            }

            if (!anOther)
            {
                this.loopLeft--;
                anOther = this.loopLeft > 0;
            }
        }

        this.textureInterpolator.factor(frame / this.numberOfFrame);
        return anOther;
    }

    /**
     * Called when animation initialized <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Start absolute frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadAnimation
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.textureInterpolator.factor(0);
        this.loopLeft = this.numberOfLoop;
        this.wayUp = true;
    }

    /**
     * Interpolated texture, can be use by example in {@link Material} or {@link Object2D}
     *
     * @return Interpolated texture
     */
    public @NotNull Texture getInterpolatedTexture()
    {
        return this.textureInterpolator.textureInterpolated();
    }
}