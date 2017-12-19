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
import com.sun.istack.internal.Nullable;
import jhelp.vectorial.event.CanvasChangeListener;
import jhelp.vectorial.layer.Canvas;
import jhelp.vectorial.layer.Layer;

/**
 * Texture with vectorial drawing
 *
 * @author JHelp
 */
public class TextureVectorial
        extends Texture
        implements CanvasChangeListener
{
    /**
     * Canvas with vectorial drawing
     */
    private final Canvas canvas;

    /**
     * Create a new instance of TextureVectorial
     *
     * @param name   Texture name
     * @param width  Texture width
     * @param height Texture height
     */
    public TextureVectorial(final @NotNull String name, final int width, final int height)
    {
        super(name, width, height);
        this.canvas = new Canvas(width, height);
        this.canvas.registerCanvasChangeListener(this);
    }

    /**
     * Embed canvas where vectorial draw
     *
     * @return Embed canvas where vectorial draw
     */
    public @NotNull Canvas canvas()
    {
        return this.canvas;
    }

    /**
     * Called when canvas changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param canvas Canvas changed
     * @see jhelp.vectorial.event.CanvasChangeListener#canvasChanged(jhelp.vectorial.layer.Canvas)
     */
    @Override
    public void canvasChanged(final @NotNull Canvas canvas)
    {
        this.setImage(this.canvas.updateImage());
    }

    /**
     * Create a new layer
     *
     * @param name Layer name
     * @return New layer
     */
    public @NotNull Layer createLayer(final @NotNull String name)
    {
        return this.canvas.createNewLayer(name);
    }

    /**
     * Create a new layer
     *
     * @return New layer
     */
    public @NotNull Layer createLayer()
    {
        return this.canvas.createNewLayer();
    }

    /**
     * Obtain a layer
     *
     * @param index Layer index
     * @return The layer
     */
    public @NotNull Layer layer(final int index)
    {
        return this.canvas.getLayer(index);
    }

    /**
     * Number of layer
     *
     * @return Number of layer
     */
    public int numberOfLayer()
    {
        return this.canvas.numberOfLayer();
    }

    /**
     * Obtain layer by name
     *
     * @param name Searched name
     * @return Found layer OR {@code null} if not found
     */
    public @Nullable Layer obtainLayerByName(final @NotNull String name)
    {
        return this.canvas.obtainLayerByName(name);
    }
}