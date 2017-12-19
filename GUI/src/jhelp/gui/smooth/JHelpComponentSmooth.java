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

package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.gui.smooth.event.SmoothKeyInformation;
import jhelp.gui.smooth.event.SmoothMouseInformation;
import jhelp.gui.smooth.event.SmoothMouseListener;
import jhelp.gui.smooth.shape.ShadowLevel;
import jhelp.gui.smooth.shape.SmoothRectangle;
import jhelp.gui.smooth.shape.SmoothShape;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.text.UtilText;

/**
 * Generic component smooth.<br>
 * Component have a {@link SmoothShape}<br>
 * It can be land on its container or "fly over" it, the {@link ShadowLevel} defines the distance.<br>
 * The background can be either one color, a {@link JHelpPaint} or a {@link JHelpImage}.<br>
 * The shadow, if {@link ShadowLevel} not {@link ShadowLevel#NO_SHADOW}, have a color.<br>
 * Each component have its ID, an automatic one is generate, but developer have the possibility to change it on taking care not
 * give same ID to 2 different components, else in search may have unexpected result. Automatic ID are negative, and developer
 * can only set positive ID, so its impossible to conflict with automatic ID.<br>
 * Each component can carry any opaque value for developer usage.<br>
 * The referenced preferred size, is the minimum size to draw the component completely<br>
 * Focusable components will receive key events when they gain focus. Not focusable never receive any key event
 *
 * @author JHelp
 */
public abstract class JHelpComponentSmooth
        implements JHelpConstantsSmooth
{
    /**
     * Next component automatic ID
     */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(Integer.MIN_VALUE);
    /**
     * Background color
     */
    private       int       background;
    /**
     * Computed bounds
     */
    private final Rectangle bounds;
    /**
     * Opaque developer information
     */
    private       Object    developerInformation;
    /**
     * Indicates if component have focus
     */
    private       boolean   focus;
    /**
     * Component ID
     */
    private       int       id;
    /**
     * Component index in parent
     */
    private       int       indexInParent;
    /**
     * Lock for synchronization
     */
    private final Object lock = new Object();
    /**
     * Mouse listeners
     */
    private final List<SmoothMouseListener> mouseListeners;
    /**
     * Paint on background
     */
    private       JHelpPaint                paintBackground;
    /**
     * Container parent
     */
    private       JHelpContainerSmooth      parent;
    /**
     * Shadow color
     */
    private       int                       shadowColor;
    /**
     * Shadow level
     */
    private       ShadowLevel               shadowLevel;
    /**
     * Component shape
     */
    private       SmoothShape               shape;
    /**
     * Background texture
     */
    private       JHelpImage                textureBackground;
    /**
     * Indicates if component is visible
     */
    private       boolean                   visible;

    /**
     * Create a new instance of JHelpComponentSmooth
     */
    public JHelpComponentSmooth()
    {
        this.id = JHelpComponentSmooth.NEXT_ID.getAndIncrement();
        this.bounds = new Rectangle();
        this.visible = true;
        this.background = 0;
        this.mouseListeners = new ArrayList<SmoothMouseListener>();
        this.parent = null;
        this.indexInParent = -1;
        this.shape = SmoothRectangle.RECTANGLE;
        this.shadowLevel = ShadowLevel.NORMAL;
        this.shadowColor = JHelpConstantsSmooth.COLOR_ALPHA_MAIN |
                           (JHelpConstantsSmooth.COLOR_BLACK & JHelpConstantsSmooth.MASK_COLOR);
    }

    /**
     * Decrement index in parent
     */
    final void decrementIndexInParent()
    {
        this.indexInParent--;
    }

    /**
     * Detach component from parent
     */
    final void detachFromParent()
    {
        this.parent = null;
    }

    /**
     * Called when component gain focus
     */
    final void gainFocus()
    {
        this.focus = true;
    }

    /**
     * Index in parent
     *
     * @return Index in parent
     */
    final int getIndexInParent()
    {
        return this.indexInParent;
    }

    /**
     * Increment index in parent
     */
    final void incrementIndexInParent()
    {
        this.indexInParent++;
    }

    /**
     * Called when component lose focus
     */
    final void lostFocus()
    {
        this.focus = false;
    }

    /**
     * Defines the component parent
     *
     * @param parent        Parent to set
     * @param indexInParent Index in given parent
     */
    final void parent(final JHelpContainerSmooth parent, final int indexInParent)
    {
        if (this.parent != null)
        {
            throw new IllegalStateException("The component is already inside something else");
        }

        this.parent = parent;
        this.indexInParent = indexInParent;
    }

    /**
     * Component bounds
     *
     * @return Component bounds
     */
    protected final Rectangle bounds()
    {
        return this.bounds;
    }

    /**
     * Draw component background, it may change component bounds
     *
     * @param image  Image where draw
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     */
    protected final void drawBackground(
            final JHelpImage image, final int x, final int y, final int width, final int height)
    {
        synchronized (this.lock)
        {
            if (this.paintBackground != null)
            {
                this.bounds.setBounds(
                        this.shape.drawShape(image, x, y, width, height, this.paintBackground, this.shadowLevel,
                                             this.shadowColor));
                return;
            }

            if (this.textureBackground != null)
            {
                if ((this.textureBackground.getWidth() != width) || (this.textureBackground.getHeight() != height))
                {
                    this.textureBackground = JHelpImage.createResizedImage(this.textureBackground, width, height);
                }

                this.bounds.setBounds(
                        this.shape.drawShape(image, x, y, width, height, this.textureBackground, this.shadowLevel,
                                             this.shadowColor));
                return;
            }

            this.bounds.setBounds(this.shape.drawShape(image, x, y, width, height, this.background, this.shadowLevel,
                                                       this.shadowColor));
        }
    }

    /**
     * Draw disable effect on component
     *
     * @param image  Image where draw
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     */
    protected final void drawDisable(
            final JHelpImage image, final int x, final int y, final int width, final int height)
    {
        synchronized (this.lock)
        {
            this.shape.fillShape(image, x, y, width, height, 0x87654321, this.shadowLevel);
        }
    }

    /**
     * Draw the component inside given bounds
     *
     * @param image        Image where draw
     * @param x            X
     * @param y            Y
     * @param width        Width
     * @param height       Height
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     */
    protected abstract void paint(
            JHelpImage image, int x, int y, int width, int height, int parentWidth, int parentHeight);

    /**
     * Compute component preferred size without taking care about shape, just take care about content
     *
     * @return Preferred size
     */
    protected abstract Dimension preferredSizeInternal();

    /**
     * Process key event.<br>
     * Do nothing by default
     *
     * @param keyInformation Key description
     */
    protected void processKeyEvent(final SmoothKeyInformation keyInformation)
    {
    }

    /**
     * Process mouse event before dispatching.<br>
     * On override it don't forget call this method to dispatch mouse event properly
     *
     * @param mouseInformation Mouse event description
     */
    protected void processMouseEvent(final SmoothMouseInformation mouseInformation)
    {
        synchronized (this.mouseListeners)
        {
            for (final SmoothMouseListener mouseListener : this.mouseListeners)
            {
                switch (mouseInformation.type())
                {
                    case MouseEvent.MOUSE_CLICKED:
                        mouseListener.mouseClicked(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_DRAGGED:
                        mouseListener.mouseDragged(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_ENTERED:
                        mouseListener.mouseEnter(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_EXITED:
                        mouseListener.mouseExit(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_MOVED:
                        mouseListener.mouseMoved(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_PRESSED:
                        mouseListener.mousePressed(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_RELEASED:
                        mouseListener.mouseReleased(mouseInformation);
                        break;
                    case MouseEvent.MOUSE_WHEEL:
                        mouseListener.mouseWheelMoved(mouseInformation);
                        break;
                }
            }
        }
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public final int background()
    {
        return this.background;
    }

    /**
     * Change background to one color
     *
     * @param color Background color
     */
    public final void background(final int color)
    {
        synchronized (this.lock)
        {
            this.background = color;
            this.paintBackground = null;
            this.textureBackground = null;
        }
    }

    /**
     * Change in same time background and shadow color
     *
     * @param color Background and shadow color
     */
    public final void backgroundAndShadowColor(final int color)
    {
        this.background(color);
        this.shadowColor(color);
    }

    /**
     * Change component bounds.<br>
     * Use with care (Prefer let system use it), may have unexpected result
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     */
    public final void bounds(final int x, final int y, final int width, final int height)
    {
        this.bounds.x = x;
        this.bounds.y = y;
        this.bounds.width = width;
        this.bounds.height = height;
    }

    /**
     * Opaque developer object
     *
     * @return Opaque developer object
     */
    public Object developerInformation()
    {
        return this.developerInformation;
    }

    /**
     * Defines the developer opaque information
     *
     * @param developerInformation New developer opaque object
     */
    public void developerInformation(final Object developerInformation)
    {
        this.developerInformation = developerInformation;
    }

    /**
     * Indicates if component have focus
     *
     * @return {@code true} if component have focus
     */
    public final boolean focus()
    {
        return this.focus;
    }

    /**
     * Indicates if component is focusable
     *
     * @return {@code true} if component is focusable
     */
    public boolean focusable()
    {
        return false;
    }

    /**
     * Component ID.<br>
     * If negative, it is an automatic ID
     *
     * @return Component ID
     */
    public int id()
    {
        return this.id;
    }

    /**
     * Change component ID.<br>
     * Only positive value are accepted.<br>
     * Beware to not give same ID to 2 different components
     *
     * @param id New id
     */
    public void id(final int id)
    {
        if (id < 0)
        {
            throw new IllegalArgumentException("Negative id are reserved for auto-generate one");
        }

        this.id = id;
    }

    /**
     * Search component under mouse position
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param rightButton Indicates if mouse right button is down
     * @return Component under mouse OR {@code null} if not found
     */
    public JHelpComponentSmooth obtainComponentUnder(final int x, final int y, final boolean rightButton)
    {
        final Rectangle bounds = this.bounds();

        if (bounds.contains(x, y))
        {
            return this;
        }

        return null;
    }

    /**
     * Paint background
     *
     * @return Paint background
     */
    public final JHelpPaint paintBackground()
    {
        return this.paintBackground;
    }

    /**
     * Define background by a paint
     *
     * @param paintBackground New paint background
     */
    public final void paintBackground(final JHelpPaint paintBackground)
    {
        synchronized (this.lock)
        {
            this.background = 0;
            this.paintBackground = paintBackground;
            this.textureBackground = null;
        }
    }

    /**
     * Component's parent
     *
     * @return Component's parent
     */
    public final JHelpContainerSmooth parent()
    {
        return this.parent;
    }

    /**
     * Compute component preferred size
     *
     * @return Preferred size
     */
    public final Dimension preferredSize()
    {
        final Dimension dimension = new Dimension(this.preferredSizeInternal());
        final Insets    insets    = this.shape.computeInsets(this.shadowLevel, dimension.width, dimension.height);
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    /**
     * Register mouse listener
     *
     * @param mouseListener Mouse listener to register
     */
    public void registerMouseListener(final SmoothMouseListener mouseListener)
    {
        if (mouseListener == null)
        {
            throw new NullPointerException("mouseListener mustn't be null");
        }

        synchronized (this.mouseListeners)
        {
            if (!this.mouseListeners.contains(mouseListener))
            {
                this.mouseListeners.add(mouseListener);
            }
        }
    }

    /**
     * Move scroll pane parent, if it exits, to make the component visible at screen
     */
    public void scrollToVisible()
    {
        if (!this.visible)
        {
            return;
        }

        JHelpContainerSmooth parent = this.parent;

        while (parent != null)
        {
            if (parent instanceof JHelpScrollPaneSmooth)
            {
                ((JHelpScrollPaneSmooth) parent).makeVisible(this.bounds());

                return;
            }

            parent = parent.parent();
        }
    }

    /**
     * Search component by ID
     *
     * @param id ID serached
     * @return Component or {@code null} if not found
     */
    public JHelpComponentSmooth searchComponent(final int id)
    {
        if (id == this.id)
        {
            return this;
        }

        return null;
    }

    /**
     * Shadow color
     *
     * @return Shadow color
     */
    public int shadowColor()
    {
        return this.shadowColor;
    }

    /**
     * Change shadow color
     *
     * @param shadowColor New shadow color
     */
    public void shadowColor(final int shadowColor)
    {
        this.shadowColor = shadowColor;
    }

    /**
     * Shadow level
     *
     * @return Shadow level
     */
    public ShadowLevel shadowLevel()
    {
        return this.shadowLevel;
    }

    /**
     * Change shadow level
     *
     * @param shadowLevel New shadow level
     */
    public void shadowLevel(final ShadowLevel shadowLevel)
    {
        if (shadowLevel == null)
        {
            throw new NullPointerException("shadowLevel mustn't be null");
        }

        this.shadowLevel = shadowLevel;
    }

    /**
     * Component shape
     *
     * @return Component shape
     */
    public SmoothShape shape()
    {
        return this.shape;
    }

    /**
     * Change component shape
     *
     * @param shape New component shape
     */
    public void shape(final SmoothShape shape)
    {
        if (shape == null)
        {
            throw new NullPointerException("shape mustn't be null");
        }

        this.shape = shape;
    }

    /**
     * Texture background
     *
     * @return Texture background
     */
    public final JHelpImage textureBackground()
    {
        return this.textureBackground;
    }

    /**
     * Change background to an image
     *
     * @param textureBackground New image background
     */
    public final void textureBackground(final JHelpImage textureBackground)
    {
        synchronized (this.lock)
        {
            this.background = 0;
            this.paintBackground = null;
            this.textureBackground = textureBackground;
        }
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return UtilText.concatenate(this.getClass().getName(), " : ", this.id, " : ", this.bounds());
    }

    /**
     * Unregister mouse listener
     *
     * @param mouseListener Mouse listener to remove
     */
    public void unregisterMouseListener(final SmoothMouseListener mouseListener)
    {
        synchronized (this.mouseListeners)
        {
            this.mouseListeners.remove(mouseListener);
        }
    }

    /**
     * Indicates if component is visible
     *
     * @return {@code true} if component is visible
     */
    public final boolean visible()
    {
        return this.visible;
    }

    /**
     * Change component visibility
     *
     * @param visible New visibility value
     */
    public final void visible(final boolean visible)
    {
        this.visible = visible;
    }
}