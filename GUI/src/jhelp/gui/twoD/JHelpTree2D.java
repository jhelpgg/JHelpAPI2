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
package jhelp.gui.twoD;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import jhelp.gui.JHelpMouseListener;
import jhelp.gui.ResourcesGUI;
import jhelp.util.debug.Debug;
import jhelp.util.gui.Bounds;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpMask;
import jhelp.util.list.Pair;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Tree of elements
 *
 * @param <INFORMATION> Elements type
 * @author JHelp
 */
public class JHelpTree2D<INFORMATION>
        extends JHelpComponent2D
{
    static
    {
        JHelpImage image = null;
        try
        {
            image = ResourcesGUI.RESOURCES.obtainJHelpImage("JHelpTreeExpand.png");
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to get expand image");
            image = new JHelpImage(1, 1);
        }
        IMAGE_EXPAND = image;

        image = null;
        try
        {
            image = ResourcesGUI.RESOURCES.obtainJHelpImage("JHelpTreeCollapse.png");
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to get collapse image");
            image = new JHelpImage(1, 1);
        }
        IMAGE_COLLAPSE = image;

        MOVE_X = Math.max(JHelpTree2D.IMAGE_EXPAND.getWidth(), JHelpTree2D.IMAGE_COLLAPSE.getWidth());
        MOVE_Y = Math.max(JHelpTree2D.IMAGE_EXPAND.getHeight(), JHelpTree2D.IMAGE_COLLAPSE.getHeight());
        FONT = new JHelpFont("Arial", Math.max(JHelpTree2D.MOVE_X, JHelpTree2D.MOVE_Y));
    }

    /**
     * Mouse sensitive area
     *
     * @param <ELEMENT> Carry element type
     * @author JHelp
     */
    static class Area<ELEMENT>
    {
        /**
         * Area height
         */
        final int        height;
        /**
         * Image representation
         */
        final JHelpImage image;
        /**
         * Information carry
         */
        final ELEMENT    node;
        /**
         * Area type
         */
        final int        type;
        /**
         * Area width
         */
        final int        width;
        /**
         * Area top left corner X
         */
        final int        x;
        /**
         * Area bottom right corner X
         */
        final int        xx;
        /**
         * Area top left corner Y
         */
        final int        y;
        /**
         * Area bottom right corner Y
         */
        final int        yy;

        /**
         * Create a new instance of Area
         *
         * @param type   Area type
         * @param node   Information
         * @param x      Top left corner X
         * @param y      Top left corner Y
         * @param width  Area width
         * @param height Area height
         * @param image  Image representation
         */
        Area(
                final int type, final ELEMENT node, final int x, final int y, final int width, final int height,
                final JHelpImage image)
        {
            this.type = type;
            this.node = node;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.xx = x + width;
            this.yy = y + height;
            this.image = image;
        }

        /**
         * Indicates if a point is inside the area
         *
         * @param x Point X
         * @param y Point Y
         * @return {@code true} if point is inside
         */
        boolean inside(final int x, final int y)
        {
            return (x >= this.x) && (x <= this.xx) && (y >= this.y) && (y <= this.yy);
        }
    }

    /**
     * Image used for collapse
     */
    private static final JHelpImage IMAGE_COLLAPSE;
    /**
     * Image used for expand
     */
    private static final JHelpImage IMAGE_EXPAND;
    /**
     * X translation
     */
    private static final int        MOVE_X;
    /**
     * Y translation
     */
    private static final int        MOVE_Y;
    /**
     * Area type : button collapse
     */
    static final        int AREA_COLLAPSE_BUTTON = 0x0;
    /**
     * Area type : expand button
     */
    static final        int AREA_EXPAND_BUTTON   = 0x1;
    /**
     * Area type : Node representation
     */
    static final        int AREA_NODE            = 0x2;
    /**
     * Default background color
     */
    public static final int BACKGROUND           = 0xFFFFFFFF;
    /**
     * Default text font
     */
    public static final JHelpFont FONT;
    /**
     * Default text color
     */
    public static final int FOREGROUND = 0xFF000000;

    /**
     * Event manager for react to tree model changed and mouse events
     *
     * @author JHelp
     */
    class EventManager
            implements JHelpTreeModelListener<INFORMATION>, JHelpMouseListener
    {
        /**
         * Secondary mouse listener
         */
        JHelpMouseListener secondMouseListener;

        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when mouse clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            JHelpTree2D.this.selectedArea = JHelpTree2D.this.obtainArea(e);
            if (JHelpTree2D.this.selectedArea != null)
            {
                switch (JHelpTree2D.this.selectedArea.type)
                {
                    case JHelpTree2D.AREA_COLLAPSE_BUTTON:
                        JHelpTree2D.this.treeModel.expand(JHelpTree2D.this.selectedArea.node, false);
                        break;
                    case JHelpTree2D.AREA_EXPAND_BUTTON:
                        JHelpTree2D.this.treeModel.expand(JHelpTree2D.this.selectedArea.node, true);
                        break;
                    case JHelpTree2D.AREA_NODE:
                        JHelpTree2D.this.clickOn(JHelpTree2D.this.selectedArea.node);
                        break;
                }
            }
            else if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseClicked(e);
            }
        }

        /**
         * Called when mouse pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mousePressed(e);
            }
        }

        /**
         * Called when mouse released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseReleased(e);
            }
        }

        /**
         * Called when mouse entered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseEntered(e);
            }
        }

        /**
         * Called when mouse exited <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseExited(e);
            }
        }

        /**
         * Called when mouse dragged <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseDragged(e);
            }
        }

        /**
         * Called when mouse moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(final MouseEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseMoved(e);
            }
        }

        /**
         * Called when mouse wheel moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event description
         * @see java.awt.event.MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
         */
        @Override
        public void mouseWheelMoved(final MouseWheelEvent e)
        {
            if (this.secondMouseListener != null)
            {
                this.secondMouseListener.mouseWheelMoved(e);
            }
        }

        /**
         * Czlled when tree model changed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param treeModel Tree model that changed
         * @see jhelp.gui.twoD.JHelpTreeModelListener#treeModelChanged(jhelp.gui.twoD.JHelpTreeModel)
         */
        @Override
        public void treeModelChanged(final JHelpTreeModel<INFORMATION> treeModel)
        {
            JHelpTree2D.this.refreshTreeImage();
        }
    }

    /**
     * Task for fire signal to a listner that an information was clicked
     *
     * @author JHelp
     */
    class TaskFireTreeOnClick
            implements ConsumerTask<Pair<TreeClickOnListener<INFORMATION>, INFORMATION>>
    {
        /**
         * Create a new instance of TaskFireTreeOnClick
         */
        TaskFireTreeOnClick()
        {
        }

        /**
         * Do the task for fire signal to a listner that an information was clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Pair of the listener to alert and information to provide
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Pair<TreeClickOnListener<INFORMATION>, INFORMATION> parameter)
        {
            parameter.first.clickOn(parameter.second);
        }
    }

    /**
     * Mouse sensitive areas
     */
    private final ArrayList<Area<INFORMATION>> areas;
    /**
     * Backgorund color
     */
    private       int                          background;
    /**
     * Event manager
     */
    private final EventManager                 eventManager;
    /**
     * Text font
     */
    private       JHelpFont                    font;
    /**
     * Foreground color
     */
    private       int                          foreground;
    /**
     * Synchronization lock
     */
    private final Object lock = new Object();
    /**
     * Task for fire signal to a listner that an information was clicked
     */
    private final TaskFireTreeOnClick                    taskFireTreeOnClick;
    /**
     * Listeners on element click in tree
     */
    private final List<TreeClickOnListener<INFORMATION>> treeClickOnListeners;
    /**
     * Image of the tree
     */
    private       JHelpImage                             treeImage;
    /**
     * Current selected area
     */
    Area<INFORMATION>           selectedArea;
    /**
     * Tree model
     */
    JHelpTreeModel<INFORMATION> treeModel;

    /**
     * Create a new instance of JHelpTree2D
     *
     * @param treeModel Tree model to use
     */
    public JHelpTree2D(final JHelpTreeModel<INFORMATION> treeModel)
    {
        this.font = JHelpTree2D.FONT;
        this.background = JHelpTree2D.BACKGROUND;
        this.foreground = JHelpTree2D.FOREGROUND;
        this.areas = new ArrayList<Area<INFORMATION>>();
        this.treeClickOnListeners = new Vector<TreeClickOnListener<INFORMATION>>();
        this.taskFireTreeOnClick = new TaskFireTreeOnClick();
        this.eventManager = new EventManager();
        this.treeModel = treeModel;
        this.refreshTreeImage();

        this.treeModel.registerTreeModelListener(this.eventManager);
        super.mouseListener(this.eventManager);
    }

    /**
     * Compute mouse sensitive areas for an information and all its children
     *
     * @param information Information (and its children) to compute areas
     * @param x           X of top left corner to start the first area
     * @param y           Y of top left corner to start the first area
     * @return Total size of computed areas
     */
    private Dimension computeAreas(final INFORMATION information, final int x, final int y)
    {
        final int     numberOfChildren = this.treeModel.numberOfChildren(information);
        final boolean expand           = this.treeModel.expanded(information);
        JHelpImage    image            = null;
        String        text             = null;

        if (this.treeModel.useImageRepresentation(information))
        {
            image = this.treeModel.obtainImageRepresentation(information);
        }

        if (image == null)
        {
            text = this.treeModel.obtainTextRepresentation(information);
        }

        if ((image == null) && (text == null))
        {
            text = "null";
        }

        if (image == null)
        {
            final JHelpMask mask = this.font.createMask(text);
            image = new JHelpImage(mask.getWidth(), mask.getHeight());
            image.startDrawMode();
            image.paintMask(0, 0, mask, this.foreground, 0, false);
            image.endDrawMode();
        }

        int height = Math.max(JHelpTree2D.MOVE_Y, image.getHeight());

        synchronized (this.lock)
        {
            if (numberOfChildren > 0)
            {
                if (expand)
                {
                    this.areas.add(new Area<INFORMATION>(JHelpTree2D.AREA_COLLAPSE_BUTTON, information, x,
                                                         y + ((height - JHelpTree2D.MOVE_Y) >> 1),
                                                         JHelpTree2D.MOVE_X, JHelpTree2D.MOVE_Y,
                                                         JHelpTree2D.IMAGE_COLLAPSE));
                }
                else
                {
                    this.areas.add(new Area<INFORMATION>(JHelpTree2D.AREA_EXPAND_BUTTON, information, x,
                                                         y + ((height - JHelpTree2D.MOVE_Y) >> 1),
                                                         JHelpTree2D.MOVE_X, JHelpTree2D.MOVE_Y,
                                                         JHelpTree2D.IMAGE_EXPAND));
                }
            }

            this.areas.add(new Area<INFORMATION>(JHelpTree2D.AREA_NODE, information, x + JHelpTree2D.MOVE_X, y,
                                                 image.getWidth(), height, image));
        }

        int width = x + JHelpTree2D.MOVE_X + image.getWidth();

        if (expand)
        {
            Dimension size;
            for (int child = 0; child < numberOfChildren; child++)
            {
                size = this.computeAreas(this.treeModel.child(information, child), x + JHelpTree2D.MOVE_X,
                                         y + height);

                width = Math.max(width, size.width);
                height += size.height;
            }
        }

        return new Dimension(width, height);
    }

    /**
     * Compute area under a position
     *
     * @param x X position
     * @param y Y position
     * @return Area under position
     */
    Area<INFORMATION> obtainArea(final int x, final int y)
    {
        synchronized (this.lock)
        {
            for (final Area<INFORMATION> area : this.areas)
            {
                if (area.inside(x, y))
                {
                    return area;
                }
            }
        }

        return null;
    }

    /**
     * Obtain area under mouse event
     *
     * @param mouseEvent Mouse event description
     * @return Area under mouse event
     */
    Area<INFORMATION> obtainArea(final MouseEvent mouseEvent)
    {
        final Bounds screenBounds = this.screenBounds();
        return this.obtainArea(mouseEvent.getX() - screenBounds.getxMin(), mouseEvent.getY() - screenBounds.getyMin());
    }

    /**
     * Refresh image tree
     */
    void refreshTreeImage()
    {
        synchronized (this.lock)
        {
            this.areas.clear();
        }
        final Dimension size = this.computeAreas(this.treeModel.root(), 0, 0);

        final JHelpImage image = new JHelpImage(Math.max(size.width, 1), Math.max(1, size.height), this.background);

        synchronized (this.lock)
        {
            image.startDrawMode();
            for (final Area<INFORMATION> area : this.areas)
            {
                image.drawImage(area.x, area.y, area.image);
            }
            image.endDrawMode();
        }

        this.treeImage = image;
        this.invalidate();
    }

    /**
     * Compute tree preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Preferred size
     * @see jhelp.gui.twoD.JHelpComponent2D#computePreferredSize(int, int)
     */
    @Override
    protected Dimension computePreferredSize(final int parentWidth, final int parentHeight)
    {
        if (this.treeImage != null)
        {
            return new Dimension(this.treeImage.getWidth(), this.treeImage.getHeight());
        }

        return new Dimension(128, 128);
    }

    /**
     * Draw the tree <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X location
     * @param y      Y location
     * @param parent Image where draw the tree
     * @see jhelp.gui.twoD.JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    protected void paint(final int x, final int y, final JHelpImage parent)
    {
        if (this.treeImage == null)
        {
            return;
        }

        parent.drawImage(x, y, this.treeImage);

        if (this.selectedArea != null)
        {
            parent.fillRectangle(x + this.selectedArea.x, y + this.selectedArea.y, this.selectedArea.width,
                                 this.selectedArea.height, 0x408080FF);
        }
    }

    /**
     * Defines the mouse listener.<br>
     * Use {@code nuul} for no mouse listener <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param mouseListener New mouse listener or {@code null} for remove mouse listener
     * @see jhelp.gui.twoD.JHelpComponent2D#mouseListener(jhelp.gui.JHelpMouseListener)
     */
    @Override
    public void mouseListener(final JHelpMouseListener mouseListener)
    {
        this.eventManager.secondMouseListener = mouseListener;
    }

    /**
     * Called by java to free memory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.treeModel.unregisterTreeModelListener(this.eventManager);

        super.finalize();
    }

    /**
     * Alert listeners that an elment was cicked
     *
     * @param information Carry information by the clicked element
     */
    protected void fireTreeOnClick(final INFORMATION information)
    {
        for (final TreeClickOnListener<INFORMATION> treeClickOnListener : this.treeClickOnListeners)
        {
            ThreadManager.parallel(this.taskFireTreeOnClick,
                                   new Pair<>(treeClickOnListener, information));
        }
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public int background()
    {
        return this.background;
    }

    /**
     * Change background color
     *
     * @param background New background color
     */
    public void background(final int background)
    {
        if (this.background == background)
        {
            return;
        }

        this.background = background;
        this.refreshTreeImage();
    }

    /**
     * Called when an element wad clicked.<br>
     * By default it alert listenrs that this event happen
     *
     * @param information Carray information by clicked element
     */
    public void clickOn(final INFORMATION information)
    {
        this.fireTreeOnClick(information);
    }

    /**
     * Font text
     *
     * @return Font text
     */
    public JHelpFont font()
    {
        return this.font;
    }

    /**
     * change text font
     *
     * @param font New text font
     */
    public void font(final JHelpFont font)
    {
        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        if (this.font.equals(font))
        {
            return;
        }

        this.font = font;
        this.refreshTreeImage();
    }

    /**
     * Texts color
     *
     * @return Texts color
     */
    public int foreground()
    {
        return this.foreground;
    }

    /**
     * Cahnge texts color
     *
     * @param foreground New texts color
     */
    public void foreground(final int foreground)
    {
        if (this.foreground == foreground)
        {
            return;
        }

        this.foreground = foreground;
        this.refreshTreeImage();
    }

    /**
     * Register a tree click listener
     *
     * @param treeClickOnListener Listener to register
     */
    public void registerTreeClickOnListener(final TreeClickOnListener<INFORMATION> treeClickOnListener)
    {
        if (treeClickOnListener == null)
        {
            throw new NullPointerException("treeClickOnListener mustn't be null");
        }

        if (!this.treeClickOnListeners.contains(treeClickOnListener))
        {
            this.treeClickOnListeners.add(treeClickOnListener);
        }
    }

    /**
     * Current selected value.<br>
     * Return {@code null} if no selection
     *
     * @return Current selected value OR {@code null} if no selection
     */
    public INFORMATION selection()
    {
        if (this.selectedArea == null)
        {
            return null;
        }

        return this.selectedArea.node;
    }

    /**
     * Select an element.<br>
     * If given element is {@code null} or not inside the model it remove the current selection and nothing will be selected
     *
     * @param information Information to select
     */
    public void selection(final INFORMATION information)
    {
        for (final Area<INFORMATION> area : this.areas)
        {
            if (area.node.equals(information))
            {
                this.selectedArea = area;
                return;
            }
        }

        this.selectedArea = null;
    }

    /**
     * Tree model
     *
     * @return Tree model
     */
    public JHelpTreeModel<INFORMATION> treeModel()
    {
        return this.treeModel;
    }

    /**
     * Change the tree model
     *
     * @param treeModel New tree model
     */
    public void treeModel(final JHelpTreeModel<INFORMATION> treeModel)
    {
        if (treeModel.equals(this.treeModel))
        {
            return;
        }

        this.selectedArea = null;
        this.treeModel.unregisterTreeModelListener(this.eventManager);
        this.treeModel = treeModel;
        this.treeModel.registerTreeModelListener(this.eventManager);

        this.refreshTreeImage();
    }

    /**
     * Unregister a tree click listener
     *
     * @param treeClickOnListener Listener to unregister
     */
    public void unregisterTreeClickOnListener(final TreeClickOnListener<INFORMATION> treeClickOnListener)
    {
        this.treeClickOnListeners.remove(treeClickOnListener);
    }
}