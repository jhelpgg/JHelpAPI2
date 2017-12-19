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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.vectorial.layer;

import java.util.ArrayList;
import java.util.List;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.math.Math2;
import jhelp.vectorial.event.ShapeChangeListener;
import jhelp.vectorial.event.ShapeDescriptionChangeListener;
import jhelp.vectorial.shape.Shape;

/**
 * Describe a shape and how draw/fill it
 *
 * @author JHelp
 */
public class ShapeDescription
        implements ShapeChangeListener
{
    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param fillColor   Fill color
     * @param strokeColor Border color
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final int fillColor, final int strokeColor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, fillColor, null, null, strokeColor, 1, false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param fillColor   Fill color
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final int fillColor, final int strokeColor, final int strokeWidth)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, fillColor, null, null, strokeColor,
                                    Math.max(1, strokeWidth), false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param texture     Fill texture
     * @param strokeColor Border color
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final JHelpImage texture, final int strokeColor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, texture, null, strokeColor, 1, false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param texture     Fill texture
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final JHelpImage texture, final int strokeColor, final int strokeWidth)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, texture, null, strokeColor,
                                    Math.max(1, strokeWidth), false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param paint       Fill paint
     * @param strokeColor Border color
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final JHelpPaint paint, final int strokeColor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (paint == null)
        {
            throw new NullPointerException("paint mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, null, paint, strokeColor, 1, false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param paint       Fill paint
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeDescription(
            final Shape shape, final JHelpPaint paint, final int strokeColor, final int strokeWidth)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (paint == null)
        {
            throw new NullPointerException("paint mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, null, paint, strokeColor, Math.max(1, strokeWidth),
                                    false, 1.5);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param fillColor   Fill color
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @param neonFactor  Color variation factor
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeNeonDescription(
            final Shape shape, final int fillColor, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (Math2.sign(neonFactor) <= 0)
        {
            throw new IllegalArgumentException("Neon factor MUST be >0, not :" + neonFactor);
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, fillColor, null, null, strokeColor,
                                    Math.max(1, strokeWidth), true, neonFactor);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param texture     Fill texture
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @param neonFactor  Color variation factor
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeNeonDescription(
            final Shape shape, final JHelpImage texture, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        if (Math2.sign(neonFactor) <= 0)
        {
            throw new IllegalArgumentException("Neon factor MUST be >0, not :" + neonFactor);
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, texture, null, strokeColor,
                                    Math.max(1, strokeWidth), true, neonFactor);
    }

    /**
     * Create description for fill and draw border of a shape
     *
     * @param shape       Shape to fill and draw border
     * @param paint       Fill paint
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @param neonFactor  Color variation factor
     * @return Description created
     */
    public static ShapeDescription createFillAndStrokeNeonDescription(
            final Shape shape, final JHelpPaint paint, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (paint == null)
        {
            throw new NullPointerException("paint mustn't be null");
        }

        if (Math2.sign(neonFactor) <= 0)
        {
            throw new IllegalArgumentException("Neon factor MUST be >0, not :" + neonFactor);
        }

        return new ShapeDescription(shape, Style.FILL_AND_STROKE, 0, null, paint, strokeColor, Math.max(1, strokeWidth),
                                    true, neonFactor);
    }

    /**
     * Create description for fill a shape
     *
     * @param shape Shape to fill
     * @param color Fill color
     * @return Description created
     */
    public static ShapeDescription createFillDescription(final Shape shape, final int color)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL, color, null, null, 0, 1, false, 1.5);
    }

    /**
     * Create description for fill a shape
     *
     * @param shape   Shape to fill
     * @param texture Fill texture
     * @return Description created
     */
    public static ShapeDescription createFillDescription(final Shape shape, final JHelpImage texture)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL, 0, texture, null, 0, 1, false, 1.5);
    }

    /**
     * Create description for fill a shape
     *
     * @param shape Shape to fill
     * @param paint Fill paint
     * @return Description created
     */
    public static ShapeDescription createFillDescription(final Shape shape, final JHelpPaint paint)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (paint == null)
        {
            throw new NullPointerException("paint mustn't be null");
        }

        return new ShapeDescription(shape, Style.FILL, 0, null, paint, 0, 1, false, 1.5);
    }

    /**
     * Create description for draw shape border
     *
     * @param shape Shape to draw
     * @param color Color to use
     * @return Description created
     */
    public static ShapeDescription createStrokeDescription(final Shape shape, final int color)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        return new ShapeDescription(shape, Style.STROKE, 0, null, null, color, 1, false, 1.5);
    }

    /**
     * Create description for draw shape border
     *
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Border thickness
     * @return Description created
     */
    public static ShapeDescription createStrokeDescription(final Shape shape, final int color, final int strokeWidth)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        return new ShapeDescription(shape, Style.STROKE, 0, null, null, color, Math.max(1, strokeWidth), false, 1.5);
    }

    /**
     * Create description for draw shape border
     *
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Border thickness
     * @param neonFactor  Variation color factor
     * @return Description created
     */
    public static ShapeDescription createStrokeNeonDescription(
            final Shape shape, final int color, final int strokeWidth, final double neonFactor)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (Math2.sign(neonFactor) <= 0)
        {
            throw new IllegalArgumentException("Neon factor MUST be >0, not :" + neonFactor);
        }

        return new ShapeDescription(shape, Style.STROKE, 0, null, null, color, Math.max(1, strokeWidth), true,
                                    neonFactor);
    }

    /**
     * Filling color
     */
    private       int                                  fillColor;
    /**
     * Filling paint
     */
    private       JHelpPaint                           fillPaint;
    /**
     * Filling texture
     */
    private       JHelpImage                           fillTexture;
    /**
     * Listeners of description changes
     */
    private final List<ShapeDescriptionChangeListener> listeners;
    /**
     * Color variation factor
     */
    private       double                               neonFactor;
    /**
     * Shape to draw/fill
     */
    private       Shape                                shape;
    /**
     * Indicates if have color variation
     */
    private       boolean                              stokeIsNeon;
    /**
     * Border color
     */
    private       int                                  strokeColor;
    /**
     * Border thickness
     */
    private       int                                  strokeWidth;
    /**
     * Style to use
     */
    private       Style                                style;

    /**
     * Create a new instance of ShapeDescription
     *
     * @param shape       Shape to draw/fill
     * @param style       Style to use
     * @param fillColor   Filling color
     * @param fillTexture Filling texture
     * @param fillPaint   Filling paint
     * @param strokeColor Border color
     * @param strokeWidth Border thickness
     * @param stokeIsNeon Indicates if have color variation
     * @param neonFactor  Color variation factor
     */
    private ShapeDescription(
            final Shape shape, final Style style, final int fillColor, final JHelpImage fillTexture,
            final JHelpPaint fillPaint,
            final int strokeColor, final int strokeWidth, final boolean stokeIsNeon, final double neonFactor)
    {
        this.listeners = new ArrayList<ShapeDescriptionChangeListener>();
        this.shape = shape;
        this.style = style;
        this.fillColor = fillColor;
        this.fillTexture = fillTexture;
        this.fillPaint = fillPaint;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.stokeIsNeon = stokeIsNeon;
        this.neonFactor = neonFactor;
        this.shape.registerShapeChangeListener(this);
    }

    /**
     * Signal to listeners that description changed
     */
    private void fireShapeDescriptionChanged()
    {
        synchronized (this.listeners)
        {
            for (final ShapeDescriptionChangeListener shapeDescriptionChangeListener : this.listeners)
            {
                shapeDescriptionChangeListener.shapeDescriptionChanged(this);
            }
        }
    }

    /**
     * Called when layer have fish to draw the description
     *
     * @param layer Layer finished
     */
    void endDraw(final Layer layer)
    {
        this.registerShapeDescriptionChangeListener(layer);
        this.shape.registerShapeChangeListener(this);
    }

    /**
     * Called when layer start to draw the description
     *
     * @param layer Layer started
     */
    void startDraw(final Layer layer)
    {
        this.unregisterShapeDescriptionChangeListener(layer);
        this.shape.unregisterShapeChangeListener(this);
    }

    /**
     * Filling color
     *
     * @return Filling color
     */
    public int getFillColor()
    {
        return this.fillColor;
    }

    /**
     * Use a unify color for fill
     *
     * @param fillColor Filling color
     */
    public void setFillColor(final int fillColor)
    {
        if ((this.fillPaint == null) && (this.fillTexture == null) && (this.fillColor == fillColor))
        {
            return;
        }

        this.fillPaint = null;
        this.fillTexture = null;
        this.fillColor = fillColor;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Filling paint
     *
     * @return Filling paint
     */
    public JHelpPaint getFillPaint()
    {
        return this.fillPaint;
    }

    /**
     * Use a paint for fill
     *
     * @param fillPaint Filling paint
     */
    public void setFillPaint(final JHelpPaint fillPaint)
    {
        if (fillPaint == null)
        {
            throw new NullPointerException("fillPaint mustn't be null");
        }

        if ((this.fillTexture == null) && (fillPaint.equals(this.fillPaint) == true))
        {
            return;
        }

        this.fillTexture = null;
        this.fillPaint = fillPaint;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Filling texture
     *
     * @return Filling texture
     */
    public JHelpImage getFillTexture()
    {
        return this.fillTexture;
    }

    /**
     * Use a texture for fill
     *
     * @param fillTexture Filling texture
     */
    public void setFillTexture(final JHelpImage fillTexture)
    {
        if (fillTexture == null)
        {
            throw new NullPointerException("fillTexture mustn't be null");
        }

        if ((this.fillPaint == null) && (fillTexture.equals(this.fillTexture) == true))
        {
            return;
        }

        this.fillPaint = null;
        this.fillTexture = fillTexture;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Color variation factor
     *
     * @return Color variation factor
     */
    public double getNeonFactor()
    {
        return this.neonFactor;
    }

    /**
     * Change color variation factor
     *
     * @param neonFactor New color variation factor
     */
    public void setNeonFactor(final double neonFactor)
    {
        if (Math2.sign(neonFactor) <= 0)
        {
            throw new IllegalArgumentException("Neon factor MUST be >0, not :" + neonFactor);
        }

        if (Math2.equals(neonFactor, this.neonFactor) == true)
        {
            return;
        }

        this.neonFactor = neonFactor;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Shape to draw
     *
     * @return Shape to draw
     */
    public Shape getShape()
    {
        return this.shape;
    }

    /**
     * change managed shape
     *
     * @param shape New shape to drw/fill
     */
    public void setShape(final Shape shape)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        if (this.shape.equals(shape) == true)
        {
            return;
        }

        this.shape.unregisterShapeChangeListener(this);
        this.shape = shape;
        this.shape.registerShapeChangeListener(this);
        this.fireShapeDescriptionChanged();
    }

    /**
     * Border color
     *
     * @return Border color
     */
    public int getStrokeColor()
    {
        return this.strokeColor;
    }

    /**
     * Change border color
     *
     * @param stokeColor New border color
     */
    public void setStrokeColor(final int stokeColor)
    {
        if (this.strokeColor == stokeColor)
        {
            return;
        }

        this.strokeColor = stokeColor;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Border thickness
     *
     * @return Border thickness
     */
    public int getStrokeWidth()
    {
        return this.strokeWidth;
    }

    /**
     * Change border thickness
     *
     * @param strokeWidth New border thickness
     */
    public void setStrokeWidth(int strokeWidth)
    {
        strokeWidth = Math.max(1, strokeWidth);

        if (this.strokeWidth == strokeWidth)
        {
            return;
        }

        this.strokeWidth = strokeWidth;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Style to use
     *
     * @return Style to use
     */
    public Style getStyle()
    {
        return this.style;
    }

    /**
     * Change style to use
     *
     * @param style New style to use
     */
    public void setStyle(final Style style)
    {
        if (style == null)
        {
            throw new NullPointerException("style mustn't be null");
        }

        if (this.style.equals(style) == true)
        {
            return;
        }

        this.style = style;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Indicates if when draw border have to use color variation
     *
     * @return {@code true} if when draw border have to use color variation
     */
    public boolean isStokeIsNeon()
    {
        return this.stokeIsNeon;
    }

    /**
     * Change usage of color variation status
     *
     * @param stokeIsNeon New usage of color variation status
     */
    public void setStokeIsNeon(final boolean stokeIsNeon)
    {
        if (this.stokeIsNeon == stokeIsNeon)
        {
            return;
        }

        this.stokeIsNeon = stokeIsNeon;
        this.fireShapeDescriptionChanged();
    }

    /**
     * Register a listener to description changed
     *
     * @param shapeDescriptionChangeListener Listener to register
     */
    public void registerShapeDescriptionChangeListener(
            final ShapeDescriptionChangeListener shapeDescriptionChangeListener)
    {
        if (shapeDescriptionChangeListener == null)
        {
            throw new NullPointerException("shapeDescriptionChangeListener mustn't be null");
        }

        synchronized (this.listeners)
        {
            if (this.listeners.contains(shapeDescriptionChangeListener) == false)
            {
                this.listeners.add(shapeDescriptionChangeListener);
            }
        }
    }

    /**
     * called when embed shape changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shape Changed shape
     * @see ShapeChangeListener#shapeChanged(jhelp.vectorial.shape.Shape)
     */
    @Override
    public void shapeChanged(final Shape shape)
    {
        this.fireShapeDescriptionChanged();
    }

    /**
     * Unregister listener of shape description changes
     *
     * @param shapeDescriptionChangeListener Listener to unregister
     */
    public void unregisterShapeDescriptionChangeListener(
            final ShapeDescriptionChangeListener shapeDescriptionChangeListener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(shapeDescriptionChangeListener);
        }
    }
}