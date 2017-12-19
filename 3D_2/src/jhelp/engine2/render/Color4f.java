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
import java.awt.Color;
import java.nio.FloatBuffer;
import jhelp.engine2.util.BufferUtils;
import jhelp.engine2.util.Math3D;
import jhelp.util.text.StringCutter;
import jhelp.util.text.UtilText;
import org.lwjgl.opengl.GL11;

/**
 * OpenGL color.<br>
 * Two color's types are considered, default color and other.<br>
 * You can't modify a default color, read access only, other read and write are possible
 *
 * @author JHelp
 */
public class Color4f
{
    /**
     * Default black color
     */
    public static final Color4f BLACK = Color4f.makeDefaultColor();

    /**
     * Default blue color
     */
    public static final Color4f BLUE = Color4f.makeDefaultColor(0f, 0f, 1f);

    /**
     * Default dark gray color
     */
    public static final Color4f DARK_GRAY = Color4f.makeDefaultColor(0.25f);

    /**
     * Default red color
     */
    public static final Color4f DARK_RED = Color4f.makeDefaultColor(0.5f, 0f, 0f);

    /**
     * Default wire frame color : black semi-transparent
     */
    public static final Color4f DEFAULT_WIRE_FRAME_COLOR = Color4f.makeDefaultColor(0, 0.5f);

    /**
     * Default gray color
     */
    public static final Color4f GRAY        = Color4f.makeDefaultColor(0.5f);
    /**
     * Default green color
     */
    public static final Color4f GREEN       = Color4f.makeDefaultColor(0f, 1f, 0f);
    /**
     * Default light blue color
     */
    public static final Color4f LIGHT_BLUE  = Color4f.makeDefaultColor(0.5f, 0.5f, 1f);
    /**
     * Default light gray color
     */
    public static final Color4f LIGHT_GRAY  = Color4f.makeDefaultColor(0.75f);
    /**
     * Default green gray color
     */
    public static final Color4f LIGHT_GREEN = Color4f.makeDefaultColor(0.5f, 1, 0.5f);
    /**
     * Default red color
     */
    public static final Color4f LIGHT_RED   = Color4f.makeDefaultColor(1f, 0.5f, 0.5f);
    /**
     * Default red color
     */
    public static final Color4f RED         = Color4f.makeDefaultColor(1f, 0f, 0f);
    /**
     * Default white color
     */
    public static final Color4f WHITE       = Color4f.makeDefaultColor(1f);
    /**
     * Default yellow color
     */
    public static final Color4f YELLOW      = Color4f.makeDefaultColor(1f, 1f, 0f);

    /**
     * Make the black default color
     *
     * @return Black default color
     */
    private static final @NotNull Color4f makeDefaultColor()
    {
        return Color4f.makeDefaultColor(0);
    }

    /**
     * Make a gray default color
     *
     * @param gray Gray value
     * @return Gray default color
     */
    private static final @NotNull Color4f makeDefaultColor(final float gray)
    {
        return Color4f.makeDefaultColor(gray, 1f);
    }

    /**
     * Make gray default color
     *
     * @param gray  Gray value
     * @param alpha Alpha value
     * @return Gray default color
     */
    private static final @NotNull Color4f makeDefaultColor(final float gray, final float alpha)
    {
        return Color4f.makeDefaultColor(gray, gray, gray, 1f);
    }

    /**
     * Make default color
     *
     * @param red   Red
     * @param green Green
     * @param blue  Blue
     * @return Default color
     */
    private static final @NotNull Color4f makeDefaultColor(final float red, final float green, final float blue)
    {
        return Color4f.makeDefaultColor(red, green, blue, 1f);
    }

    /**
     * Make a default color
     *
     * @param red   Red
     * @param green Green
     * @param blue  Blue
     * @param alpha Alpha
     * @return Default color
     */
    private static final @NotNull Color4f makeDefaultColor(
            final float red, final float green, final float blue, final float alpha)
    {
        final Color4f color = new Color4f(red, green, blue, alpha);
        color.defaultColor = true;
        return color;
    }

    /**
     * Parse a string to a color.<br>
     * Format is 4 floats sperate by one space, they represents in the order : red, green, blue and alpha
     *
     * @param color String to parse
     * @return Color result
     * @throws NumberFormatException If string not represents a color
     * @throws NullPointerException  If string not represents a color
     */
    public static @NotNull Color4f parse(final @NotNull String color)
    {
        final StringCutter stringCutter = new StringCutter(color, ' ');

        final Color4f color4f = new Color4f();

        color4f.red = Float.parseFloat(stringCutter.next());
        color4f.green = Float.parseFloat(stringCutter.next());
        color4f.blue = Float.parseFloat(stringCutter.next());
        color4f.alpha = Float.parseFloat(stringCutter.next());

        return color4f;
    }

    /**
     * Alpha part
     */
    private float   alpha;
    /**
     * Blue part
     */
    private float   blue;
    /**
     * Indicates if the color is default
     */
    private boolean defaultColor;
    /**
     * Green part
     */
    private float   green;

    /**
     * Red part
     */
    private float red;

    /**
     * Constructs black color
     */
    public Color4f()
    {
        this(0f);
    }

    /**
     * Constructs Color4f
     *
     * @param color Base color
     */
    public Color4f(final Color color)
    {
        this.red = color.getRed() / 255f;
        this.green = color.getGreen() / 255f;
        this.blue = color.getBlue() / 255f;
        this.alpha = color.getAlpha() / 255f;
        this.defaultColor = false;
    }

    /**
     * Constructs Color4f copy of an other
     *
     * @param color Color to copy
     */
    public Color4f(final Color4f color)
    {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
        this.alpha = color.alpha;
        this.defaultColor = false;
    }

    /**
     * Constructs gray color
     *
     * @param gray gray value
     */
    public Color4f(final float gray)
    {
        this(gray, 1f);
    }

    /**
     * Constructs gray color
     *
     * @param gray  Gray value
     * @param alpha Alpha value
     */
    public Color4f(final float gray, final float alpha)
    {
        this(gray, gray, gray, alpha);
    }

    /**
     * Create color
     *
     * @param red   Red
     * @param green Green
     * @param blue  Blue
     */
    public Color4f(final float red, final float green, final float blue)
    {
        this(red, green, blue, 1);
    }

    /**
     * Create color
     *
     * @param red   Red
     * @param green Green
     * @param blue  Blue
     * @param alpha Alpha
     */
    public Color4f(final float red, final float green, final float blue, final float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.defaultColor = false;
    }

    /**
     * Constructs Color4f
     *
     * @param color Color in ARGB
     */
    public Color4f(final int color)
    {
        this.red = ((color >> 16) & 0xFF) / 255f;
        this.green = ((color >> 8) & 0xFF) / 255f;
        this.blue = (color & 0xFF) / 255f;
        this.alpha = ((color >> 24) & 0xFF) / 255f;
        this.defaultColor = false;
    }

    /**
     * Apply the color to OpenGL
     */
    @ThreadOpenGL
    void glColor4f()
    {
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
    }

    /**
     * Alpha part
     *
     * @return New alpha
     */
    public float alpha()
    {
        return this.alpha;
    }

    /**
     * Change alpha part
     *
     * @param alpha New alpha
     * @throws IllegalStateException If this color is a default one
     */
    public void alpha(final float alpha)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.alpha = alpha;
    }

    /**
     * Color in ARGB format
     *
     * @return ARGB format
     */
    public int argb()
    {
        return ((((int) (this.alpha * 255)) & 0xFF) << 24) | ((((int) (this.red * 255)) & 0xFF) << 16) |
               ((((int) (this.green * 255)) & 0xFF) << 8)
               | (((int) (this.blue * 255)) & 0xFF);
    }

    /**
     * Blue part
     *
     * @return Blue part
     */
    public float blue()
    {
        return this.blue;
    }

    /**
     * Change blue part
     *
     * @param blue New blue
     * @throws IllegalStateException If this color is a default one
     */
    public void blue(final float blue)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.blue = blue;
    }

    /**
     * Create a copy
     *
     * @return Copy
     */
    public Color4f copy()
    {
        return new Color4f(this);
    }

    /**
     * Indicates if the color is a default one
     *
     * @return {@code true} if the color is a default one
     */
    public boolean defaultColor()
    {
        return this.defaultColor;
    }

    /**
     * Indicates if an object is the same color
     *
     * @param object Object to compare
     * @return {@code true} if an object is the same color
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (super.equals(object))
        {
            return true;
        }
        if (!(object instanceof Color4f))
        {
            return false;
        }
        final Color4f color4f = (Color4f) object;
        if (!Math3D.equal(this.alpha, color4f.alpha))
        {
            return false;
        }
        if (!Math3D.equal(this.red, color4f.red))
        {
            return false;
        }
        if (!Math3D.equal(this.blue, color4f.blue))
        {
            return false;
        }
        return Math3D.equal(this.green, color4f.green);
    }

    /**
     * Fill the color with values from {@link Color}
     *
     * @param color Color to extract information
     * @throws IllegalStateException If this color is a default one
     */
    public void fromColor(final @NotNull Color color)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }

        this.red = color.getRed() / 255f;
        this.green = color.getGreen() / 255f;
        this.blue = color.getBlue() / 255f;
        this.alpha = color.getAlpha() / 255f;
    }

    /**
     * Green part
     *
     * @return Green part
     */
    public float green()
    {
        return this.green;
    }

    /**
     * Change green part
     *
     * @param green New green
     * @throws IllegalStateException If this color is a default one
     */
    public void green(final float green)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.green = green;
    }

    /**
     * Parse a string to a color.<br>
     * Format is 4 floats sperate by one space, they represents in the order : red, green, blue and alpha
     *
     * @param color String to parse
     * @throws NumberFormatException If string not represents a color
     * @throws NullPointerException  If string not represents a color
     */
    public void parseString(final @NotNull String color)
    {
        final StringCutter stringCutter = new StringCutter(color, ' ');

        this.red = Float.parseFloat(stringCutter.next());
        this.green = Float.parseFloat(stringCutter.next());
        this.blue = Float.parseFloat(stringCutter.next());
        this.alpha = Float.parseFloat(stringCutter.next());
    }

    /**
     * Push the color in the float buffer
     *
     * @return Filled float buffer
     */
    public @NotNull FloatBuffer putInFloatBuffer()
    {
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.red);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.green);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.blue);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.alpha);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();

        return BufferUtils.TEMPORARY_FLOAT_BUFFER;
    }

    /**
     * Push the color in the float buffer
     *
     * @param percent Multiplier of percent of color
     * @return Filled float buffer
     */
    public @NotNull FloatBuffer putInFloatBuffer(final float percent)
    {
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.red * percent);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.green * percent);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.blue * percent);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.put(this.alpha);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();

        return BufferUtils.TEMPORARY_FLOAT_BUFFER;
    }

    /**
     * Red part
     *
     * @return Red part
     */
    public float red()
    {
        return this.red;
    }

    /**
     * Change red part
     *
     * @param red New red
     * @throws IllegalStateException If this color is a default one
     */
    public void red(final float red)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.red = red;
    }

    /**
     * Serialize color to String.<br>
     * Can be later used by {@link #parseString(String)} or {@link #parse(String)}
     *
     * @return Serialized color
     */
    public @NotNull String serialize()
    {
        return UtilText.concatenate(this.red, ' ', this.green, ' ', this.blue, ' ', this.alpha);
    }

    /**
     * Change color to black
     *
     * @throws IllegalStateException If this color is a default one
     */
    public void set()
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.set(0, 0, 0);
    }

    /**
     * Change color
     *
     * @param color Base color
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final @NotNull Color color)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.red = color.getRed() / 255f;
        this.green = color.getGreen() / 255f;
        this.blue = color.getBlue() / 255f;
        this.alpha = color.getAlpha() / 255f;
    }

    /**
     * Change color
     *
     * @param color Color to copy
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final @NotNull Color4f color)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
        this.alpha = color.alpha;
    }

    /**
     * Change color to gray
     *
     * @param gray Gray value
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final float gray)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.set(gray, gray, gray);
    }

    /**
     * Change color to gray
     *
     * @param gray  Gray value
     * @param alpha Alpha value
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final float gray, final float alpha)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.set(gray, gray, gray, alpha);
    }

    /**
     * Change the color
     *
     * @param red   New red
     * @param green New blue
     * @param blue  New green
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final float red, final float green, final float blue)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.set(red, green, blue, 1);
    }

    /**
     * Change the color
     *
     * @param red   New red
     * @param green New green
     * @param blue  New blue
     * @param alpha New alpha
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final float red, final float green, final float blue, final float alpha)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Change color
     *
     * @param color Color in ARGB
     * @throws IllegalStateException If this color is a default one
     */
    public void set(final int color)
    {
        if (this.defaultColor)
        {
            throw new IllegalStateException("A default color couldn't be change");
        }
        this.red = ((color >> 16) & 0xFF) / 255f;
        this.green = ((color >> 8) & 0xFF) / 255f;
        this.blue = (color & 0xFF) / 255f;
        this.alpha = ((color >> 24) & 0xFF) / 255f;
    }

    /**
     * Convert to a {@link Color}
     *
     * @return Color result
     */
    public @NotNull Color toColor()
    {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }
}