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
import jhelp.engine2.util.Math3D;

/**
 * Describe a transformation apply to a texture
 *
 * @author JHelp
 */
public class TextureTransformation
{
    /**
     * Contrast change
     */
    private float   contrast;
    /**
     * Indicates if colors should be inverted
     */
    private boolean invertColor;
    /**
     * Shift X on texture
     */
    private int     shiftX;
    /**
     * Shift Y on texture
     */
    private int     shiftY;

    /**
     * Create a new instance of TextureTransformation
     */
    public TextureTransformation()
    {
        this.shiftX = 0;
        this.shiftY = 0;
        this.contrast = 1;
        this.invertColor = false;
    }

    /**
     * Apply the transformation to given texture
     *
     * @param texture Texture to modify
     */
    public void apply(final @NotNull Texture texture)
    {
        if ((this.shiftX != 0) || (this.shiftY != 0))
        {
            texture.shift(this.shiftX, this.shiftY);
        }

        if (!Math3D.equal(this.contrast, 1))
        {
            texture.contrast(this.contrast);
        }

        if (this.invertColor)
        {
            texture.invert();
        }

        texture.flush();
    }

    /**
     * Actual contrast value
     *
     * @return Actual contrast value
     */
    public float contrast()
    {
        return this.contrast;
    }

    /**
     * Change contrast
     *
     * @param contrast New contrast value
     */
    public void contrast(final float contrast)
    {
        this.contrast = contrast;
    }

    /**
     * Actual invertColor value
     *
     * @return Actual invertColor value
     */
    public boolean invertColor()
    {
        return this.invertColor;
    }

    /**
     * Change invertColor
     *
     * @param invertColor New invertColor value
     */
    public void invertColor(final boolean invertColor)
    {
        this.invertColor = invertColor;
    }

    /**
     * Do a shift on texture
     *
     * @param shiftX Shift X
     * @param shiftY Shift Y
     */
    public void shift(final int shiftX, final int shiftY)
    {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    /**
     * Actual shiftX value
     *
     * @return Actual shiftX value
     */
    public int shiftX()
    {
        return this.shiftX;
    }

    /**
     * Actual shiftY value
     *
     * @return Actual shiftY value
     */
    public int shiftY()
    {
        return this.shiftY;
    }
}