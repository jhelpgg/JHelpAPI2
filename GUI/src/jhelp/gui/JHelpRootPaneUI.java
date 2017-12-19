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
package jhelp.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.RootPaneUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import jhelp.gui.dnd.DragSource;
import jhelp.gui.dnd.DropTarget;
import jhelp.util.debug.Debug;

/**
 * Root pane UI for drag and drop effect and windowable panel<br>
 * <br>
 * Last modification : 18 janv. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class JHelpRootPaneUI
        extends BasicRootPaneUI
{
    /**
     * Region from edges that dragging is active from.
     */
    private static final int BORDER_DRAG_THICKNESS = 5;
    /**
     * The amount of space (in pixels) that the cursor is changed on.
     */
    private static final int CORNER_DRAG_WIDTH     = 16;
    // Get and adapt the MetalRootPaneUICode
    // ...
    /**
     * Base root pane UI
     */
    private static RootPaneUI rootPaneUI;

    // ...
    // End off the copy/adaptation
    /**
     * Maps from positions to cursor type. Refer to calculateCorner and calculatePosition for details of this.
     */
    static final int[] cursorMapping = new int[]
            {
                    Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
                    Cursor.NE_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR,
                    0, 0, 0, Cursor.NE_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
                    Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
                    Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR,
                    Cursor.SE_RESIZE_CURSOR
            };

    /**
     * Create an instance of this UI for a component
     *
     * @param c Component reference
     * @return Created UI
     */
    public static ComponentUI createUI(final JComponent c)
    {
        return new JHelpRootPaneUI();
    }

    /**
     * Initialize the use of this UI in the current look and feel
     */
    @SuppressWarnings("unchecked")
    public static void useJHelpRootPaneUI()
    {
        final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        try
        {
            final String className = (String) defaults.get("RootPaneUI");
            if (className != null)
            {
                final Class<RootPaneUI> clazz = (Class<RootPaneUI>) Class.forName(className);
                if (clazz != null)
                {
                    JHelpRootPaneUI.rootPaneUI = clazz.newInstance();
                }
                else
                {
                    throw new NullPointerException(
                            "Can't find the class for the JHelpRootPaneUI installation. No " + className + " ?");
                }
            }
            else
            {
                throw new NullPointerException(
                        "Can't find the class name in the look and feel for the JHelpRootPaneUI installation");
            }
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }
        defaults.put("RootPaneUI", JHelpRootPaneUI.class.getName());
    }

    /**
     * MouseInputHandler is responsible for handling resize/moving of the Window. It sets the cursor directly on the Window when
     * then mouse moves over a hot spot.
     */
    class MouseInputHandler
            implements MouseInputListener
    {
        /**
         * Used to determine the corner the resize is occuring from.
         */
        private int dragCursor;
        /**
         * Height of the window when the drag started.
         */
        private int dragHeight;

        /**
         * X location the mouse went down on for a drag operation.
         */
        private int dragOffsetX;

        /**
         * Y location the mouse went down on for a drag operation.
         */
        private int dragOffsetY;

        /**
         * Width of the window when the drag started.
         */
        private int dragWidth;

        /**
         * Set to true if the drag operation is moving the window.
         */
        private boolean isMovingWindow;

        /**
         * Last cursor to return
         */
        private Cursor lastCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

        /**
         * Adjust bounds for a modification
         *
         * @param bounds      Bounds to adjust
         * @param min         Minimum size
         * @param deltaX      Translation on X
         * @param deltaY      Translation on Y
         * @param deltaWidth  How width change
         * @param deltaHeight How height change
         */
        private void adjust(
                final Rectangle bounds, final Dimension min, final int deltaX, final int deltaY, final int deltaWidth,
                final int deltaHeight)
        {
            // Do the changes
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;
            // Adjust to the minimum size
            if (min != null)
            {
                if (bounds.width < min.width)
                {
                    final int correction = min.width - bounds.width;
                    if (deltaX != 0)
                    {
                        bounds.x -= correction;
                    }
                    bounds.width = min.width;
                }
                if (bounds.height < min.height)
                {
                    final int correction = min.height - bounds.height;
                    if (deltaY != 0)
                    {
                        bounds.y -= correction;
                    }
                    bounds.height = min.height;
                }
            }
        }

        /**
         * Returns the corner that contains the point <code>x</code>, <code>y</code>, or -1 if the position doesn't match a
         * corner.
         *
         * @param w Reference window
         * @param x X location
         * @param y Y location
         * @return Calculated corner
         */
        private int calculateCorner(final Window w, final int x, final int y)
        {
            final Insets insets    = w.getInsets();
            final int    xPosition = this.calculatePosition(x - insets.left, w.getWidth() - insets.left - insets.right);
            final int    yPosition = this.calculatePosition(y - insets.top, w.getHeight() - insets.top - insets.bottom);

            if ((xPosition == -1) || (yPosition == -1))
            {
                return -1;
            }
            return (yPosition * 5) + xPosition;
        }

        /**
         * Returns an integer indicating the position of <code>spot</code> in <code>width</code>. The return value will be: 0 if <
         * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >= CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS 3 if >=
         * width - CORNER_DRAG_WIDTH 4 if >= width - BORDER_DRAG_THICKNESS 5 otherwise
         *
         * @param spot  Spot
         * @param width Width
         * @return The position
         */
        private int calculatePosition(final int spot, final int width)
        {
            if (spot < JHelpRootPaneUI.BORDER_DRAG_THICKNESS)
            {
                return 0;
            }
            if (spot < JHelpRootPaneUI.CORNER_DRAG_WIDTH)
            {
                return 1;
            }
            if (spot >= (width - JHelpRootPaneUI.BORDER_DRAG_THICKNESS))
            {
                return 4;
            }
            if (spot >= (width - JHelpRootPaneUI.CORNER_DRAG_WIDTH))
            {
                return 3;
            }
            return 2;
        }

        /**
         * Returns the Cursor to render for the specified corner. This returns 0 if the corner doesn't map to a valid Cursor
         *
         * @param corner Considered corner
         * @return The cursor
         */
        private int getCursor(final int corner)
        {
            if (corner == -1)
            {
                return 0;
            }
            return JHelpRootPaneUI.cursorMapping[corner];
        }

        /**
         * Retrieve the widow where event is done
         *
         * @param ev The event
         * @return The find window
         */
        private Window windowForEvent(final MouseEvent ev)
        {
            final Component source = (Component) ev.getSource();
            return source instanceof Window
                   ? (Window) source
                   : SwingUtilities.getWindowAncestor(source);
        }

        /**
         * Action when mouse click
         *
         * @param ev Event description
         * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent ev)
        {
            final Component source = (Component) ev.getSource();
            final Window    w      = this.windowForEvent(ev);
            Frame           f      = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else
            {
                return;
            }

            JComponent titlePane = null;
            if (JHelpRootPaneUI.this.content instanceof HaveHeader)
            {
                titlePane = ((HaveHeader) JHelpRootPaneUI.this.content).obtainTheComponentForMove();
            }
            final Point convertedPoint = SwingUtilities.convertPoint(source, ev.getPoint(), titlePane);

            final int state = f.getExtendedState();
            if ((titlePane != null) && titlePane.contains(convertedPoint))
            {
                if (((ev.getClickCount() % 2) == 0) && ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0))
                {
                    if (f.isResizable())
                    {
                        if ((state & Frame.MAXIMIZED_BOTH) != 0)
                        {
                            f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                        }
                        else
                        {
                            f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                        }
                    }
                }
            }
        }

        /**
         * Action when mouse pressed
         *
         * @param ev Event description
         * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent ev)
        {
            final JRootPane rootPane  = JHelpRootPaneUI.this.rootPane;
            JComponent      titlePane = null;
            if (JHelpRootPaneUI.this.content instanceof HaveHeader)
            {
                titlePane = ((HaveHeader) JHelpRootPaneUI.this.content).obtainTheComponentForMove();
            }

            if (rootPane.getWindowDecorationStyle() == JRootPane.NONE)
            {
                return;
            }
            Point           dragWindowOffset = ev.getPoint();
            final Component source           = (Component) ev.getSource();
            final Window    w                = this.windowForEvent(ev);
            if (w != null)
            {
                w.toFront();
            }
            final Point convertedDragWindowOffset = SwingUtilities.convertPoint(source, dragWindowOffset, titlePane);

            dragWindowOffset = SwingUtilities.convertPoint(source, dragWindowOffset, w);

            Frame  f = null;
            Dialog d = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else if (w instanceof Dialog)
            {
                d = (Dialog) w;
            }

            if (w == null)
            {
                Debug.warning("Widows not found !");

                return;
            }

            final int frameState = (f != null)
                                   ? f.getExtendedState()
                                   : 0;

            if ((titlePane != null) && titlePane.contains(convertedDragWindowOffset))
            {
                if ((((f != null) && ((frameState & Frame.MAXIMIZED_BOTH) == 0)) || (d != null)) &&
                    (dragWindowOffset.y >= JHelpRootPaneUI.BORDER_DRAG_THICKNESS)
                    && (dragWindowOffset.x >= JHelpRootPaneUI.BORDER_DRAG_THICKNESS)
                    && (dragWindowOffset.x < (w.getWidth() - JHelpRootPaneUI.BORDER_DRAG_THICKNESS)))
                {
                    this.isMovingWindow = true;
                    this.dragOffsetX = dragWindowOffset.x;
                    this.dragOffsetY = dragWindowOffset.y;
                }
                else if (((f != null) && f.isResizable() && ((frameState & Frame.MAXIMIZED_BOTH) == 0)) ||
                         ((d != null) && d.isResizable()))
                {
                    this.dragOffsetX = dragWindowOffset.x;
                    this.dragOffsetY = dragWindowOffset.y;
                    this.dragWidth = w.getWidth();
                    this.dragHeight = w.getHeight();
                    this.dragCursor = this.getCursor(this.calculateCorner(w, dragWindowOffset.x, dragWindowOffset.y));
                }
            }
            else if (((f != null) && f.isResizable() && ((frameState & Frame.MAXIMIZED_BOTH) == 0)) ||
                     ((d != null) && d.isResizable()))
            {
                this.dragOffsetX = dragWindowOffset.x;
                this.dragOffsetY = dragWindowOffset.y;
                this.dragWidth = w.getWidth();
                this.dragHeight = w.getHeight();
                this.dragCursor = this.getCursor(this.calculateCorner(w, dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        /**
         * Action when mouse released
         *
         * @param ev Event description
         * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent ev)
        {
            final Window window = this.windowForEvent(ev);
            if ((this.dragCursor != 0) && (window != null) && !window.isValid())
            {
                // Some Window systems validate as you resize, others won't,
                // thus the check for validity before repainting.
                window.validate();
                JHelpRootPaneUI.this.rootPane.repaint();
            }

            if (this.isMovingWindow)
            {
                if (JHelpRootPaneUI.this.content instanceof WindowablePanel.HaveHeaderPanel)
                {
                    Debug.verbose("Test");
                    final WindowablePanel windowablePanel = ((WindowablePanel.HaveHeaderPanel) JHelpRootPaneUI.this.content)
                            .getWindowablePanelParent();
                    Debug.verbose("windowablePanel=", windowablePanel);
                    final WindowablePanel panel = WindowablePanel.getWindowablePanelIntersetected(windowablePanel);
                    Debug.verbose("panel=", panel);
                    if ((panel != null) && (panel.acceptAddOtherWindowablePanel()))
                    {
                        Debug.verbose("Decide to add");
                        panel.addWindowablePanel(windowablePanel);
                    }
                }
                else if (JHelpRootPaneUI.this.content instanceof DragSource.HaveHeaderLabelBufferedImage)
                {
                    final DragSource dragSource = ((DragSource.HaveHeaderLabelBufferedImage) JHelpRootPaneUI.this.content)
                            .obtainDragSource();
                    final Point position = ev.getLocationOnScreen();
                    final DropTarget dropTarget = DropTarget.obtainDropTargetForScreenPosition(
                            position.x + ResourcesGUI.TRANSLATE_CURSOR, position.y
                                                                        + ResourcesGUI.TRANSLATE_CURSOR);
                    if (dropTarget == null)
                    {
                        dragSource.fireDragFailed();
                    }
                    else
                    {
                        final Object information = dragSource.obtainInformationValue();
                        if (dropTarget.isAcceptableInformation(information))
                        {
                            dragSource.fireDragDone();
                            final Point location = dropTarget.obtainDropComponent().getLocationOnScreen();
                            dropTarget.dropDone(information,//
                                                (position.x - location.x) + ResourcesGUI.TRANSLATE_CURSOR,
                                                (position.y - location.y) + ResourcesGUI.TRANSLATE_CURSOR);
                        }
                        else
                        {
                            dragSource.fireDragFailed();
                        }
                    }
                }
            }

            this.isMovingWindow = false;
            this.dragCursor = 0;
        }

        /**
         * Action when mouse entered
         *
         * @param ev Event description
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent ev)
        {
            final Window w = this.windowForEvent(ev);
            this.lastCursor = w.getCursor();
            this.mouseMoved(ev);
        }

        /**
         * Action when mouse exit
         *
         * @param ev Event description
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent ev)
        {
            final Window w = this.windowForEvent(ev);
            w.setCursor(this.lastCursor);
        }

        /**
         * Action when mouse dragged
         *
         * @param ev Event description
         * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(final MouseEvent ev)
        {
            final Component source = (Component) ev.getSource();
            final Window    w      = this.windowForEvent(ev);
            final Point     pt     = SwingUtilities.convertPoint(source, ev.getPoint(), w);

            if (JHelpRootPaneUI.this.content instanceof DragSource.HaveHeaderLabelBufferedImage)
            {
                final DragSource dragSource = ((DragSource.HaveHeaderLabelBufferedImage) JHelpRootPaneUI.this.content).obtainDragSource();
                final Point      position   = ev.getLocationOnScreen();
                final DropTarget dropTarget = DropTarget.obtainDropTargetForScreenPosition(
                        position.x + ResourcesGUI.TRANSLATE_CURSOR, position.y
                                                                    + ResourcesGUI.TRANSLATE_CURSOR);
                if ((dropTarget == null) || (!dropTarget.isAcceptableInformation(dragSource.obtainInformationValue())))
                {
                    w.setCursor(ResourcesGUI.CANT_DROP_CURSOR);
                }
                else
                {
                    w.setCursor(ResourcesGUI.CAN_DROP_CURSOR);
                }
            }

            if (this.isMovingWindow)
            {
                final Point eventLocationOnScreen = ev.getLocationOnScreen();
                w.setLocation(eventLocationOnScreen.x - this.dragOffsetX, eventLocationOnScreen.y - this.dragOffsetY);
            }
            else if (this.dragCursor != 0)
            {
                final Rectangle r           = w.getBounds();
                final Rectangle startBounds = new Rectangle(r);
                final Dimension min         = w.getMinimumSize();

                switch (this.dragCursor)
                {
                    case Cursor.E_RESIZE_CURSOR:
                        this.adjust(r, min, 0, 0, (pt.x + (this.dragWidth - this.dragOffsetX)) - r.width, 0);
                        break;
                    case Cursor.S_RESIZE_CURSOR:
                        this.adjust(r, min, 0, 0, 0, (pt.y + (this.dragHeight - this.dragOffsetY)) - r.height);
                        break;
                    case Cursor.N_RESIZE_CURSOR:
                        this.adjust(r, min, 0, pt.y - this.dragOffsetY, 0, -(pt.y - this.dragOffsetY));
                        break;
                    case Cursor.W_RESIZE_CURSOR:
                        this.adjust(r, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX), 0);
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        this.adjust(r, min, 0, pt.y - this.dragOffsetY,
                                    (pt.x + (this.dragWidth - this.dragOffsetX)) - r.width, -(pt.y - this.dragOffsetY));
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        this.adjust(r, min, 0, 0, (pt.x + (this.dragWidth - this.dragOffsetX)) - r.width,
                                    (pt.y + (this.dragHeight - this.dragOffsetY)) - r.height);
                        break;
                    case Cursor.NW_RESIZE_CURSOR:
                        this.adjust(r, min, pt.x - this.dragOffsetX, pt.y - this.dragOffsetY,
                                    -(pt.x - this.dragOffsetX), -(pt.y - this.dragOffsetY));
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        this.adjust(r, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX),
                                    (pt.y + (this.dragHeight - this.dragOffsetY)) - r.height);
                        break;
                    default:
                        break;
                }
                if (!r.equals(startBounds))
                {
                    w.setBounds(r);
                    // Defer repaint/validate on mouseReleased unless dynamic
                    // layout is active.
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive())
                    {
                        w.validate();
                        JHelpRootPaneUI.this.rootPane.repaint();
                    }
                }
            }
        }

        /**
         * Action when mouse moved
         *
         * @param ev Event description
         * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(final MouseEvent ev)
        {
            final JRootPane root = JHelpRootPaneUI.this.rootPane;

            if (root.getWindowDecorationStyle() == JRootPane.NONE)
            {
                return;
            }

            final Component source = (Component) ev.getSource();
            final Window    w      = this.windowForEvent(ev);
            final Point     pt     = SwingUtilities.convertPoint(source, ev.getPoint(), w);
            Frame           f      = null;
            Dialog          d      = null;

            if (w instanceof Frame)
            {
                f = (Frame) w;
            }
            else if (w instanceof Dialog)
            {
                d = (Dialog) w;
            }

            // Update the cursor
            final int cursor = this.getCursor(this.calculateCorner(w, pt.x, pt.y));

            if ((cursor != 0) &&
                (((f != null) && (f.isResizable() && ((f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))) ||
                 ((d != null) && d.isResizable())))
            {
                w.setCursor(Cursor.getPredefinedCursor(cursor));
            }
            else if (JHelpRootPaneUI.this.content.getBounds().contains(ev.getPoint()))
            {
                if (JHelpRootPaneUI.this.content instanceof DragSource.HaveHeaderLabelBufferedImage)
                {
                    final DragSource dragSource = ((DragSource.HaveHeaderLabelBufferedImage) JHelpRootPaneUI.this.content)
                            .obtainDragSource();
                    final Point position = ev.getLocationOnScreen();
                    final DropTarget dropTarget = DropTarget.obtainDropTargetForScreenPosition(
                            position.x + ResourcesGUI.TRANSLATE_CURSOR, position.y
                                                                        + ResourcesGUI.TRANSLATE_CURSOR);
                    if ((dropTarget == null) ||
                        (!dropTarget.isAcceptableInformation(dragSource.obtainInformationValue())))
                    {
                        w.setCursor(ResourcesGUI.CANT_DROP_CURSOR);
                    }
                    else
                    {
                        w.setCursor(ResourcesGUI.CAN_DROP_CURSOR);
                    }
                }
                else
                {
                    w.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }
            else
            {
                w.setCursor(this.lastCursor);
            }
        }
    }

    /**
     * Mouse listener for the drag, re-dimension ,...
     */
    private MouseInputHandler mouseInputHandler;
    /**
     * Main container
     */
    Container content;
    /**
     * Root pane where action is done
     */
    JRootPane rootPane;

    /**
     * Constructs JHelpRootPaneUI
     */
    public JHelpRootPaneUI()
    {
        // Nothing to do
    }

    /**
     * Add listeners to the root pane and in the header (If there one)
     */
    private void addListeners()
    {
        if (this.mouseInputHandler == null)
        {
            this.mouseInputHandler = new MouseInputHandler();
        }

        this.rootPane.addMouseListener(this.mouseInputHandler);
        this.rootPane.addMouseMotionListener(this.mouseInputHandler);

        if (this.content instanceof HaveHeader)
        {
            final JComponent titlePane = ((HaveHeader) this.content).obtainTheComponentForMove();
            titlePane.addMouseListener(this.mouseInputHandler);
            titlePane.addMouseMotionListener(this.mouseInputHandler);
        }
    }

    /**
     * Create window borders
     */
    private void createBorders()
    {
        this.rootPane.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Install the UI
     */
    private void install()
    {
        this.content = this.rootPane.getContentPane();
        final int style = this.rootPane.getWindowDecorationStyle();
        if (style != JRootPane.NONE)
        {
            this.createBorders();
            this.addListeners();
        }
        this.rootPane.repaint();
        this.rootPane.revalidate();
    }

    /**
     * Remove listeners
     */
    private void removeListeners()
    {
        if (this.mouseInputHandler == null)
        {
            return;
        }

        this.rootPane.removeMouseListener(this.mouseInputHandler);
        this.rootPane.removeMouseMotionListener(this.mouseInputHandler);

        if (this.content instanceof HaveHeader)
        {
            final JComponent titlePane = ((HaveHeader) this.content).obtainTheComponentForMove();
            titlePane.removeMouseListener(this.mouseInputHandler);
            titlePane.removeMouseMotionListener(this.mouseInputHandler);
        }
    }

    /**
     * Unistall the UI
     */
    private void uninstall()
    {
        this.removeListeners();

        this.rootPane.repaint();
        this.rootPane.revalidate();
    }

    /**
     * Install this UI on a component
     *
     * @param c Component where install
     * @see BasicRootPaneUI#installUI(JComponent)
     */
    @Override
    public void installUI(final JComponent c)
    {
        if (JHelpRootPaneUI.rootPaneUI == null)
        {
            super.installUI(c);
        }
        else
        {
            JHelpRootPaneUI.rootPaneUI.installUI(c);
        }
        this.rootPane = (JRootPane) c;
        this.install();
    }

    /**
     * Uninstall the UI from a component
     *
     * @param c Component to remove the UI
     * @see BasicRootPaneUI#uninstallUI(JComponent)
     */
    @Override
    public void uninstallUI(final JComponent c)
    {
        if (JHelpRootPaneUI.rootPaneUI == null)
        {
            super.uninstallUI(c);
        }
        else
        {
            JHelpRootPaneUI.rootPaneUI.uninstallUI(c);
        }

        this.uninstall();
    }

    /**
     * Action when a property change on this UI
     *
     * @param e Event description
     * @see BasicRootPaneUI#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent e)
    {
        super.propertyChange(e);

        this.uninstall();
        this.install();
    }
}