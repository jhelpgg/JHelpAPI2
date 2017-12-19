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
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.math.Math2;
import jhelp.vectorial.event.ShapeDescriptionChangeListener;
import jhelp.vectorial.math.Point;
import jhelp.vectorial.shape.Shape;

/**
 * A layer draw/fill several shape in it.<br>
 * It have a name, position and scale
 *
 * @author JHelp
 */
public class Layer
        implements ShapeDescriptionChangeListener
{
    /**
     * Next ID
     */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    /**
     * Background color. Used if {@link #paintBackground} and {@link #textureBackground} are {@code null}
     */
    private       int                    background;
    /**
     * Embed image where layer is draw
     */
    private final JHelpImage             embedImage;
    /**
     * Layer height
     */
    private final int                    height;
    /**
     * Layer height half
     */
    private final double                 heightHalf;
    /**
     * Layer name
     */
    private       String                 name;
    /**
     * Indicates that layer changed and the embed image have to be refreshed
     */
    private       boolean                needUpdate;
    /**
     * Background paint. Used if not {@code null}
     */
    private       JHelpPaint             paintBackground;
    /**
     * Canvas parent owner
     */
    private final Canvas                 parent;
    /**
     * Shapes on layer
     */
    private final List<ShapeDescription> shapes;
    /**
     * Texture background. Used if not {@code null} and {@link #paintBackground} {@code null}
     */
    private       JHelpImage             textureBackground;
    /**
     * X translation
     */
    private       double                 translateX;
    /**
     * X translation
     */
    private       double                 translateY;
    /**
     * Indicates if layer is visible
     */
    private       boolean                visible;
    /**
     * Layer width
     */
    private final int                    width;
    /**
     * Layer width half
     */
    private final double                 widthHalf;
    /**
     * Zoom in X
     */
    private       double                 zoomX;
    /**
     * Zoom in Y
     */
    private       double                 zoomY;

    /**
     * Create a new instance of Layer
     *
     * @param parent Canvas parent owner
     * @param width  Width
     * @param height Height
     */
    Layer(final Canvas parent, final int width, final int height)
    {
        this.parent = parent;
        this.width = width;
        this.height = height;
        this.widthHalf = this.width / 2d;
        this.heightHalf = this.height / 2d;
        this.embedImage = new JHelpImage(this.width, this.height);
        this.name = "Layer_" + Layer.NEXT_ID.getAndIncrement();
        this.shapes = new ArrayList<ShapeDescription>();
        this.zoomX = 1;
        this.zoomY = 1;
        this.translateX = 0;
        this.translateY = 0;
        this.visible = true;
        this.needUpdate = true;
    }

    /**
     * Draw shape border
     *
     * @param width  Layer width
     * @param height Layer height
     * @param pixels Pixels where draw
     * @param shape  Shape to draw
     * @param color  Color to use
     */
    private void draw(final int width, final int height, final int[] pixels, final Shape shape, final int color)
    {
        final LineShapeDrawer lineShapeDrawer = new LineShapeDrawer(width, height, pixels, color);
        shape.drawShape(lineShapeDrawer);
    }

    /**
     * Draw shape border with a thick line
     *
     * @param width       Layer width
     * @param height      Layer height
     * @param pixels      Pixels where draw
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Line thickness
     */
    private void draw(
            final int width, final int height, final int[] pixels, final Shape shape, final int color,
            final int strokeWidth)
    {
        final ThickLineShapeDrawer thickLineShapeDrawer = new ThickLineShapeDrawer(width, height, pixels, color,
                                                                                   strokeWidth);
        shape.drawShape(thickLineShapeDrawer);
    }

    /**
     * Draw a shape border with color variation
     *
     * @param width       Layer width
     * @param height      Layer height
     * @param pixels      Pixels where draw
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Line thickness
     * @param neonFactor  Factor of variation
     */
    private void drawNeon(
            final int width, final int height, final int[] pixels, final Shape shape, final int color,
            final int strokeWidth,
            final double neonFactor)
    {
        final NeonShapeDrawer neonShapeDrawer = new NeonShapeDrawer(width, height, pixels, color, strokeWidth,
                                                                    neonFactor);

        for (int thick = strokeWidth - 1; thick >= 0; thick--)
        {
            neonShapeDrawer.setThick(thick);
            shape.drawShape(neonShapeDrawer);
        }
    }

    /**
     * Fill shape with one color
     *
     * @param width  Layer width
     * @param height Layer height
     * @param pixels Pixels where draw
     * @param shape  Shape to draw
     * @param color  Color to use
     */
    private void fill(final int width, final int height, final int[] pixels, final Shape shape, final int color)
    {
        final ColorShapeFiller colorShapeFiller = new ColorShapeFiller(width, height, pixels, color);
        shape.fillShape(colorShapeFiller);
    }

    /**
     * Fill shape with a texture
     *
     * @param width   Layer width
     * @param height  Layer height
     * @param pixels  Pixels where draw
     * @param shape   Shape to draw
     * @param texture Texture to use
     */
    private void fill(
            final int width, final int height, final int[] pixels, final Shape shape, final JHelpImage texture)
    {
        final TextureShapeFiller textureShapeFiller = new TextureShapeFiller(width, height, pixels, texture);
        shape.fillShape(textureShapeFiller);
    }

    /**
     * Fill shape with paint
     *
     * @param width  Layer width
     * @param height Layer height
     * @param pixels Pixels where draw
     * @param shape  Shape to draw
     * @param paint  Paint to use
     */
    private void fill(final int width, final int height, final int[] pixels, final Shape shape, final JHelpPaint paint)
    {
        final PaintShapeFiller paintShapeFiller = new PaintShapeFiller(width, height, pixels, paint);
        shape.fillShape(paintShapeFiller);
    }

    /**
     * Called when layer changed
     */
    private void layerHaveChanged()
    {
        this.needUpdate = true;
        this.parent.canvasHaveChanged();
    }

    /**
     * Paint a shape description
     *
     * @param width            Layer width
     * @param height           Layer height
     * @param pixels           Pixels where draw
     * @param shapeDescription Shape description to paint
     */
    private void paint(final int width, final int height, final int[] pixels, final ShapeDescription shapeDescription)
    {
        shapeDescription.startDraw(this);
        final Shape  shape    = shapeDescription.getShape();
        final double scaleX   = shape.getScaleX();
        final double scaleY   = shape.getScaleY();
        final Point  position = shape.getPosition();
        shape.setPosition(this.translateX + this.widthHalf + ((position.getX() - this.widthHalf) * this.zoomX),
                          this.translateY + this.heightHalf + ((position.getY() - this.heightHalf) * this.zoomY));
        shape.setScale(scaleX * this.zoomX, scaleY * this.zoomY);
        final Style style = shapeDescription.getStyle();

        if ((style == Style.FILL) || (style == Style.FILL_AND_STROKE))
        {
            final JHelpPaint paint   = shapeDescription.getFillPaint();
            final JHelpImage texture = shapeDescription.getFillTexture();

            if (paint != null)
            {
                this.fill(width, height, pixels, shape, paint);
            }
            else if (texture != null)
            {
                this.fill(width, height, pixels, shape, texture);
            }
            else
            {
                this.fill(width, height, pixels, shape, shapeDescription.getFillColor());
            }
        }

        if ((style == Style.STROKE) || (style == Style.FILL_AND_STROKE))
        {
            final int     strokeWidth = shapeDescription.getStrokeWidth();
            final boolean isNeon      = shapeDescription.isStokeIsNeon();
            final int     color       = shapeDescription.getStrokeColor();
            final double  neonFactor  = shapeDescription.getNeonFactor();

            if (strokeWidth == 1)
            {
                this.draw(width, height, pixels, shape, color);
            }
            else if (isNeon == false)
            {
                this.draw(width, height, pixels, shape, color, strokeWidth);
            }
            else
            {
                this.drawNeon(width, height, pixels, shape, color, strokeWidth, neonFactor);
            }
        }

        shape.setScale(scaleX, scaleY);
        shape.setPosition(position);
        shapeDescription.endDraw(this);
    }

    /**
     * Add shape to draw
     *
     * @param shapeDescription Shape to add
     */
    public void addShape(final ShapeDescription shapeDescription)
    {
        if (shapeDescription == null)
        {
            throw new NullPointerException("shapeDescription mustn't be null");
        }

        shapeDescription.registerShapeDescriptionChangeListener(this);
        this.shapes.add(shapeDescription);
        this.layerHaveChanged();
    }

    /**
     * Fill a shape with a color
     *
     * @param shape Shape to fill
     * @param color Color to use
     */
    public void fill(final Shape shape, final int color)
    {
        this.addShape(ShapeDescription.createFillDescription(shape, color));
    }

    /**
     * Fill a shape with texture
     *
     * @param shape   Shape to fill
     * @param texture Texture to use
     */
    public void fill(final Shape shape, final JHelpImage texture)
    {
        this.addShape(ShapeDescription.createFillDescription(shape, texture));
    }

    /**
     * Fill a shape with paint
     *
     * @param shape Shape to fill
     * @param paint Paint to use
     */
    public void fill(final Shape shape, final JHelpPaint paint)
    {
        this.addShape(ShapeDescription.createFillDescription(shape, paint));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param fillColor   Color for fill
     * @param strokeColor Color for border
     */
    public void fillAndStroke(final Shape shape, final int fillColor, final int strokeColor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, fillColor, strokeColor));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param fillColor   Color for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     */
    public void fillAndStroke(final Shape shape, final int fillColor, final int strokeColor, final int strokeWidth)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, fillColor, strokeColor, strokeWidth));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param texture     Texture for fill
     * @param strokeColor Color for border
     */
    public void fillAndStroke(final Shape shape, final JHelpImage texture, final int strokeColor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, texture, strokeColor));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param texture     Texture for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     */
    public void fillAndStroke(final Shape shape, final JHelpImage texture, final int strokeColor, final int strokeWidth)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, texture, strokeColor, strokeWidth));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param paint       Paint for fill
     * @param strokeColor Color for border
     */
    public void fillAndStroke(final Shape shape, final JHelpPaint paint, final int strokeColor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, paint, strokeColor));
    }

    /**
     * Fill a shape and draw its border
     *
     * @param shape       Shape to fill and stroke
     * @param paint       Paint for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     */
    public void fillAndStroke(final Shape shape, final JHelpPaint paint, final int strokeColor, final int strokeWidth)
    {
        this.addShape(ShapeDescription.createFillAndStrokeDescription(shape, paint, strokeColor, strokeWidth));
    }

    /**
     * Fill a shape and draw its border with color variation
     *
     * @param shape       Shape to fill and stroke
     * @param fillColor   Color for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     * @param neonFactor  Variation factor
     */
    public void fillAndStrokeNeon(
            final Shape shape, final int fillColor, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeNeonDescription(shape, fillColor, strokeColor, strokeWidth,
                                                                          neonFactor));
    }

    /**
     * Fill a shape and draw its border with color variation
     *
     * @param shape       Shape to fill and stroke
     * @param texture     Texture for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     * @param neonFactor  Variation factor
     */
    public void fillAndStrokeNeon(
            final Shape shape, final JHelpImage texture, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeNeonDescription(shape, texture, strokeColor, strokeWidth,
                                                                          neonFactor));
    }

    /**
     * Fill a shape and draw its border with color variation
     *
     * @param shape       Shape to fill and stroke
     * @param paint       Paint for fill
     * @param strokeColor Color for border
     * @param strokeWidth Border thickness
     * @param neonFactor  Variation factor
     */
    public void fillAndStrokeNeon(
            final Shape shape, final JHelpPaint paint, final int strokeColor, final int strokeWidth,
            final double neonFactor)
    {
        this.addShape(ShapeDescription.createFillAndStrokeNeonDescription(shape, paint, strokeColor, strokeWidth,
                                                                          neonFactor));
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public int getBackground()
    {
        return this.background;
    }

    /**
     * Change background to uniform color
     *
     * @param background new background color
     */
    public void setBackground(final int background)
    {
        if ((this.paintBackground == null) && (this.textureBackground == null) && (this.background == background))
        {
            return;
        }

        this.paintBackground = null;
        this.textureBackground = null;
        this.background = background;
        this.layerHaveChanged();
    }

    /**
     * Layer height
     *
     * @return Layer height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Layer name
     *
     * @return Layer name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Background paint
     *
     * @return Background paint
     */
    public JHelpPaint getPaintBackground()
    {
        return this.paintBackground;
    }

    /**
     * change background to a paint
     *
     * @param paintBackground New paint background
     */
    public void setPaintBackground(final JHelpPaint paintBackground)
    {
        if (paintBackground == null)
        {
            throw new NullPointerException("paintBackground mustn't be null");
        }

        if ((this.textureBackground == null) && (paintBackground.equals(this.paintBackground) == true))
        {
            return;
        }

        this.textureBackground = null;
        this.paintBackground = paintBackground;
        this.layerHaveChanged();
    }

    /**
     * Obtain a shape
     *
     * @param index Shape index
     * @return Desired shape
     */
    public ShapeDescription getShape(final int index)
    {
        return this.shapes.get(index);
    }

    /**
     * Background texture
     *
     * @return Background texture
     */
    public JHelpImage getTextureBackground()
    {
        return this.textureBackground;
    }

    /**
     * Change background to a texture
     *
     * @param textureBackground New texture background
     */
    public void setTextureBackground(final JHelpImage textureBackground)
    {
        if (textureBackground == null)
        {
            throw new NullPointerException("textureBackground mustn't be null");
        }

        if ((this.paintBackground == null) && (textureBackground.equals(this.textureBackground) == true))
        {
            return;
        }

        this.paintBackground = null;
        this.textureBackground = textureBackground;
        this.layerHaveChanged();
    }

    /**
     * Layer translation
     *
     * @return Layer translation
     */
    public Point getTranslate()
    {
        return new Point(this.translateX, this.translateY);
    }

    /**
     * Layer width
     *
     * @return Layer width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Zoom in X
     *
     * @return Zoom in X
     */
    public double getZoomX()
    {
        return this.zoomX;
    }

    /**
     * Change zoom in X
     *
     * @param zoomX New zoom in X
     */
    public void setZoomX(final double zoomX)
    {
        if (Math2.sign(zoomX) <= 0)
        {
            throw new IllegalArgumentException("Zoom MUST be >0 not " + zoomX);
        }

        if (Math2.equals(zoomX, this.zoomX) == true)
        {
            return;
        }

        this.zoomX = zoomX;
        this.layerHaveChanged();
    }

    /**
     * Zoom in Y
     *
     * @return Zoom in Y
     */
    public double getZoomY()
    {
        return this.zoomY;
    }

    /**
     * Change zoom in Y
     *
     * @param zoomY New zoom in Y
     */
    public void setZoomY(final double zoomY)
    {
        if (Math2.sign(zoomY) <= 0)
        {
            throw new IllegalArgumentException("Zoom MUST be >0 not " + zoomY);
        }

        if (Math2.equals(zoomY, this.zoomY) == true)
        {
            return;
        }

        this.zoomY = zoomY;
        this.layerHaveChanged();
    }

    /**
     * Insert a shape
     *
     * @param index            Index where insert
     * @param shapeDescription Shape to insert
     */
    public void insertShape(final int index, final ShapeDescription shapeDescription)
    {
        if (shapeDescription == null)
        {
            throw new NullPointerException("shapeDescription mustn't be null");
        }

        shapeDescription.registerShapeDescriptionChangeListener(this);

        if ((this.shapes.isEmpty() == true) || (index >= this.shapes.size()))
        {
            this.shapes.add(shapeDescription);
            this.layerHaveChanged();
            return;
        }

        this.shapes.add(Math.max(0, index), shapeDescription);
        this.layerHaveChanged();
    }

    /**
     * Indicates if layer is visible
     *
     * @return {@code true} if layer is visible
     */
    public boolean isVisible()
    {
        return this.visible;
    }

    /**
     * Change layer visibility
     *
     * @param visible New visibility
     */
    public void setVisible(final boolean visible)
    {
        if (this.visible == visible)
        {
            return;
        }

        this.visible = visible;
        this.layerHaveChanged();
    }

    /**
     * Number of shapes
     *
     * @return Number of shapes
     */
    public int numberOfShape()
    {
        return this.shapes.size();
    }

    /**
     * Remove a shape
     *
     * @param index Shape index to remove
     */
    public void removeShape(final int index)
    {
        this.shapes.remove(index).unregisterShapeDescriptionChangeListener(this);
        this.layerHaveChanged();
    }

    /**
     * Change the layer name.<br>
     * If given name already exists for an other layer inside the canvas parent, the name will be add az number to make it unique
     *
     * @param name New desired name
     * @return The real given name
     */
    public String setName(final String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        this.name = this.parent.validateName(this.name, name);
        return this.name;
    }

    /**
     * Change precision for all current embed shape
     *
     * @param precision New precision
     */
    public void setPrecisionForAllShape(final int precision)
    {
        for (final ShapeDescription shapeDescription : this.shapes)
        {
            shapeDescription.getShape().setPrecision(precision);
        }
    }

    /**
     * Change layer translation
     *
     * @param x New X
     * @param y New Y
     */
    public void setTranslate(final double x, final double y)
    {
        if ((Math2.equals(this.translateX, x) == true) && (Math2.equals(this.translateY, y) == true))
        {
            return;
        }

        this.translateX = x;
        this.translateY = y;
        this.layerHaveChanged();
    }

    /**
     * change zoom on X and Y
     *
     * @param zoom New zoom on X and Y
     */
    public void setZoom(final double zoom)
    {
        if (Math2.sign(zoom) <= 0)
        {
            throw new IllegalArgumentException("Zoom MUST be >0 not " + zoom);
        }

        if ((Math2.equals(zoom, this.zoomX) == true) && (Math2.equals(zoom, this.zoomY) == true))
        {
            return;
        }

        this.zoomX = zoom;
        this.zoomY = zoom;
        this.layerHaveChanged();
    }

    /**
     * Called when an embed shape changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param shapeDescription Changed shape
     * @see ShapeDescriptionChangeListener#shapeDescriptionChanged(jhelp.vectorial.layer.ShapeDescription)
     */
    @Override
    public void shapeDescriptionChanged(final ShapeDescription shapeDescription)
    {
        this.layerHaveChanged();
    }

    /**
     * Draw shape border
     *
     * @param shape Shape to draw
     * @param color Color to use
     */
    public void stroke(final Shape shape, final int color)
    {
        this.addShape(ShapeDescription.createStrokeDescription(shape, color));
    }

    /**
     * Draw shape border
     *
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Border thickness
     */
    public void stroke(final Shape shape, final int color, final int strokeWidth)
    {
        this.addShape(ShapeDescription.createStrokeDescription(shape, color, strokeWidth));
    }

    /**
     * Draw shape border with color variation
     *
     * @param shape       Shape to draw
     * @param color       Color to use
     * @param strokeWidth Border thickness
     * @param neonFactor  Variation factor
     */
    public void strokeNeon(final Shape shape, final int color, final int strokeWidth, final double neonFactor)
    {
        this.addShape(ShapeDescription.createStrokeNeonDescription(shape, color, strokeWidth, neonFactor));
    }

    /**
     * Update the embed image
     *
     * @return Updated image
     */
    public JHelpImage updateImage()
    {
        if (this.needUpdate == true)
        {
            this.needUpdate = false;
            this.embedImage.startDrawMode();

            if (this.paintBackground != null)
            {
                this.embedImage.fillRectangle(0, 0, this.width, this.height, this.paintBackground, false);
            }
            else if (this.textureBackground != null)
            {
                this.embedImage.fillRectangle(0, 0, this.width, this.height, this.textureBackground, false);
            }
            else
            {
                this.embedImage.clear(this.background);
            }

            final int[] pixels = this.embedImage.getPixels(0, 0, this.width, this.height);

            for (final ShapeDescription shapeDescription : this.shapes)
            {
                this.paint(this.width, this.height, pixels, shapeDescription);
            }

            this.embedImage.setPixels(0, 0, this.width, this.height, pixels);
            this.embedImage.endDrawMode();
        }

        return this.embedImage;
    }
}