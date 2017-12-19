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
import jhelp.util.text.UtilText;
import jhelp.vectorial.event.CanvasChangeListener;

/**
 * Canvas with layers
 *
 * @author JHelp
 */
public class Canvas
{
    /** Embed image */
    private final JHelpImage                 embedImage;
    /** Canvas height */
    private final int                        height;
    /** Layers in canvas */
    private final List<Layer>                layers;
    /** Listeners of canvas changes */
    private final List<CanvasChangeListener> listeners;
    /** Indicates if have to update canvas */
    private       boolean                    needToUpdate;
    /** Canvas width */
    private final int                        width;

    /**
     * Create a new instance of Canvas
     *
     * @param width
     *           Width
     * @param height
     *           Height
     */
    public Canvas(final int width, final int height)
    {
        this.width = width;
        this.height = height;
        this.embedImage = new JHelpImage(this.width, this.height);
        this.layers = new ArrayList<Layer>();
        this.listeners = new ArrayList<CanvasChangeListener>();
        this.needToUpdate = true;
    }

    /**
     * Collect layers name
     *
     * @return Layers name
     */
    private List<String> collectNames()
    {
        final List<String> names = new ArrayList<String>(this.layers.size());

        for (final Layer layer : this.layers)
        {
            names.add(layer.getName());
        }

        return names;
    }

    /**
     * Called when canvas changed
     */
    void canvasHaveChanged()
    {
        this.needToUpdate = true;

        synchronized (this.listeners)
        {
            for (final CanvasChangeListener canvasChangeListener : this.listeners)
            {
                canvasChangeListener.canvasChanged(this);
            }
        }
    }

    /**
     * Compute a valid name (name not already used)
     *
     * @param currentName
     *           Current name
     * @param futureName
     *           Name to give
     * @return Name to use
     */
    String validateName(final String currentName, final String futureName)
    {
        final List<String> names = this.collectNames();
        names.remove(currentName);
        return UtilText.computeNotInsideName(futureName, names);
    }

    /**
     * Change index layer
     *
     * @param currentIndex
     *           Layer index
     * @param desiredIndex
     *           New index
     */
    public void changeIndexLayer(final int currentIndex, final int desiredIndex)
    {
        if (currentIndex == desiredIndex)
        {
            return;
        }

        final Layer layer = this.layers.remove(currentIndex);
        this.layers.add(desiredIndex, layer);
        this.canvasHaveChanged();
    }

    /**
     * Create a new layer
     *
     * @return New layer
     */
    public Layer createNewLayer()
    {
        final Layer layer = new Layer(this, this.width, this.height);
        this.layers.add(layer);
        this.canvasHaveChanged();
        return layer;
    }

    /**
     * Create a new layer
     *
     * @param name
     *           Name to give
     * @return Created layer
     */
    public Layer createNewLayer(final String name)
    {
        final Layer layer = this.createNewLayer();
        layer.setName(UtilText.computeNotInsideName(name, this.collectNames()));
        return layer;
    }

    /**
     * Exchange two layers
     *
     * @param index1
     *           First layer index
     * @param index2
     *           Second layer index
     */
    public void exchangeLayers(final int index1, final int index2)
    {
        if (index1 == index2)
        {
            return;
        }

        final Layer layer1 = this.layers.get(index1);
        final Layer layer2 = this.layers.get(index2);
        this.layers.set(index1, layer2);
        this.layers.set(index2, layer1);
        this.canvasHaveChanged();
    }

    /**
     * Canvas height
     *
     * @return Canvas height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Obtain a layer
     *
     * @param index
     *           Layer index
     * @return The layer
     */
    public Layer getLayer(final int index)
    {
        return this.layers.get(index);
    }

    /**
     * Canvas width
     *
     * @return Canvas width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Number of layers
     *
     * @return Number of layers
     */
    public int numberOfLayer()
    {
        return this.layers.size();
    }

    /**
     * Obtain a layer by name
     *
     * @param name
     *           Layer name
     * @return Layer OR {@code null} if not found
     */
    public Layer obtainLayerByName(final String name)
    {
        for (final Layer layer : this.layers)
        {
            if (layer.getName().equals(name) == true)
            {
                return layer;
            }
        }

        return null;
    }

    /**
     * Register canvas change listener
     *
     * @param canvasChangeListener
     *           Listener to register
     */
    public void registerCanvasChangeListener(final CanvasChangeListener canvasChangeListener)
    {
        if (canvasChangeListener == null)
        {
            throw new NullPointerException("canvasChangeListener mustn't be null");
        }

        synchronized (this.listeners)
        {
            if (this.listeners.contains(canvasChangeListener) == false)
            {
                this.listeners.add(canvasChangeListener);
            }
        }
    }

    /**
     * Remove a layer
     *
     * @param index
     *           Layer index
     */
    public void removeLayer(final int index)
    {
        this.layers.remove(index);
        this.canvasHaveChanged();
    }

    /**
     * Change precision of all shapes in all layers
     *
     * @param precision
     *           Precision to give
     */
    public void setPrecisionForAllShape(final int precision)
    {
        for (final Layer layer : this.layers)
        {
            layer.setPrecisionForAllShape(precision);
        }
    }

    /**
     * Unregister canvas change listener
     *
     * @param canvasChangeListener
     *           Listener to unregister
     */
    public void unregisterCanvasChangeListener(final CanvasChangeListener canvasChangeListener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(canvasChangeListener);
        }
    }

    /**
     * Update canvas image
     *
     * @return Updated image
     */
    public JHelpImage updateImage()
    {
        if (this.needToUpdate == true)
        {
            this.embedImage.startDrawMode();
            this.embedImage.clear(0);

            for (final Layer layer : this.layers)
            {
                if (layer.isVisible() == true)
                {
                    this.embedImage.drawImage(0, 0, layer.updateImage());
                }
            }

            this.embedImage.endDrawMode();
        }

        this.needToUpdate = false;
        return this.embedImage;
    }
}