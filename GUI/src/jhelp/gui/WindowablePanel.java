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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import jhelp.gui.event.WindowablePanelEvent;
import jhelp.gui.event.WindowablePanelListener;
import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.thread.Mutex;
import jhelp.util.util.Utilities;

/**
 * Panel that can become a stand alone window <br>
 * Windowable panel can be attach on other windowable panel<br>
 * <br>
 * Last modification : 25 janv. 2009<br>
 * Version 0.0.1<br>
 *
 * @author JHelp
 */
public class WindowablePanel
        extends JPanel
        implements ActionListener
{
    /**
     * List off all created Windowable panel
     */
    private static final ArrayList<WindowablePanel> arrayListWindowablePanel = new ArrayList<WindowablePanel>();

    /**
     * Get widowable panel by title
     *
     * @param title Title of looking for windowable panel
     * @return Search windowable panel
     */
    public static WindowablePanel getWindowablePanelByTitle(final String title)
    {
        for (final WindowablePanel panel : WindowablePanel.arrayListWindowablePanel)
        {
            if (panel.labelTitle.getText()
                                .equals(title))
            {
                return panel;
            }
        }
        return null;
    }

    /**
     * Compute the windowable panel intersects the given one
     *
     * @param windowablePanel Windowable panel we search intersection
     * @return Intersected windowable panel
     */
    public static WindowablePanel getWindowablePanelIntersetected(final WindowablePanel windowablePanel)
    {
        if ((windowablePanel == null) || (!windowablePanel.isShowing()) || (!windowablePanel.panelMain.isShowing()))
        {
            return null;
        }

        final Rectangle rectangle = new Rectangle(windowablePanel.panelMain.getLocationOnScreen(),
                                                  windowablePanel.getRealDimension());
        Rectangle rectangle2;
        for (final WindowablePanel panel : WindowablePanel.arrayListWindowablePanel)
        {
            if ((panel != windowablePanel) && (panel.isShowing()) && (panel.panelMain.isShowing()))
            {
                rectangle2 = new Rectangle(panel.panelMain.getLocationOnScreen(), panel.getRealDimension());
                if (rectangle.intersects(rectangle2))
                {
                    return panel;
                }
            }
        }
        return null;
    }

    /**
     * Obtain the windowable panel parent of a component
     *
     * @param component Component that search its windowable panel parent
     * @return Component windowable panel parent or {@code null} if there none
     */
    public static WindowablePanel getWindowablePanelParent(Component component)
    {
        component = component.getParent();

        while (component != null)
        {
            if (component instanceof WindowablePanel)
            {
                return (WindowablePanel) component;
            }

            if (component instanceof HaveHeaderPanel)
            {
                return ((HaveHeaderPanel) component).getWindowablePanelParent();
            }

            component = component.getParent();
        }

        return null;
    }

    /**
     * Header of the panel <br>
     * <br>
     * Last modification : 25 janv. 2009<br>
     * Version 0.0.1<br>
     *
     * @author JHelp
     */
    public class HaveHeaderPanel
            extends JPanel
            implements HaveHeader
    {

        /**
         * Constructs HaveHeaderPanel
         *
         * @param layout Layout used
         */
        HaveHeaderPanel(final LayoutManager layout)
        {
            super(layout);
        }

        /**
         * Windowable panel parent
         *
         * @return Windowable panel parent
         */
        public WindowablePanel getWindowablePanelParent()
        {
            return WindowablePanel.this;
        }

        /**
         * The component where the move window append.<br>
         * Here it is the title
         *
         * @return The component where the move window append
         * @see HaveHeader#obtainTheComponentForMove()
         */
        @Override
        public JComponent obtainTheComponentForMove()
        {
            return WindowablePanel.this.labelTitle;
        }
    }

    /**
     * Button for close the windowable panel
     */
    private       JButton         buttonClose;
    /**
     * Button for hide/show the content of the widowable panel
     */
    private       JToggleButton   buttonHide;
    /**
     * Button for attach/detach the widowable panel
     */
    private       JToggleButton   buttonToWindow;
    /**
     * Indicates if the panel can be close
     */
    private final boolean         closable;
    /**
     * Indicates if the panel is close
     */
    private       boolean         close;
    /**
     * Component reference
     */
    private       Component       component;
    /**
     * Component at center
     */
    private final JComponent      componentCenter;
    /**
     * Dialog used when it is detach
     */
    private       JDialog         dialog;
    /**
     * Indicates if it is the first time the show of detach dialog append
     */
    private       boolean         firstShow;
    /**
     * Panel height
     */
    private       int             height;
    /**
     * Indicated if the content is hide
     */
    private       boolean         hide;
    /**
     * Indicates if the content can be hide
     */
    private final boolean         hideable;
    /**
     * Indicates if the panel is detach
     */
    private       boolean         isWindow;
    /**
     * Windowable panel icon
     */
    private       JLabel          labelIcon;
    /**
     * Mutex used for synchronized force size
     */
    private       Mutex           mutexForced;
    /**
     * Panel for buttons
     */
    private       JPanel          panelButtons;
    /**
     * Panel at center
     */
    private       JPanel          panelCenter;
    /**
     * Panel for the header
     */
    private       JPanel          panelHeader;
    /**
     * Main panel witch contain the content
     */
    private       HaveHeaderPanel panelMain;
    /**
     * Tabbed pane withch carry all contents
     */
    private       JTabbedPane     tabbedPane;
    /**
     * Panel width
     */
    private       int             width;
    /**
     * Indicates if the panel can be detach
     */
    private final boolean         windowable;

    /**
     * Widowable panel parent
     */
    private WindowablePanel windowablePanelParent;

    /**
     * Label for title
     */
    JLabel labelTitle;

    /**
     * Constructs WindowablePanel
     *
     * @param component  Component reference
     * @param title      Title of the panel
     * @param icon       Icon
     * @param windowable Indicates if the panel can be detach/attach
     * @param hideable   Indicates if the content can be hide/show
     * @param closable   Indicate if the panel can be close
     */
    public WindowablePanel(
            final Component component, final String title, final Icon icon, final boolean windowable,
            final boolean hideable,
            final boolean closable)
    {
        this(component, title, icon, windowable, hideable, closable, null);
    }

    /**
     * Constructs WindowablePanel
     *
     * @param component       Component
     * @param title           Title
     * @param icon            Icon
     * @param windowable      Indicates if the panel can be detach/attach
     * @param hideable        Indicates if the content can be hide/show
     * @param closable        Indicate if the panel can be close
     * @param componentCenter Center component
     */
    public WindowablePanel(
            final Component component, String title, final Icon icon, final boolean windowable, final boolean hideable,
            final boolean closable,
            final JComponent componentCenter)
    {
        if (component == null)
        {
            throw new NullPointerException("The component mustn't be null !");
        }

        WindowablePanel.arrayListWindowablePanel.add(this);

        if (title == null)
        {
            title = "";
        }

        this.componentCenter = componentCenter;
        this.setBorder(BorderFactory.createEtchedBorder());

        this.component = component;

        this.windowable = windowable;
        this.hideable = hideable;
        this.closable = closable;

        this.isWindow = false;
        this.hide = false;
        this.firstShow = true;
        this.close = false;

        this.createComponents(title, icon);
        this.layoutComponents();
        this.addListeners();

        final Dimension dimension = new Dimension(100, 100);
        this.setMinimumSize(dimension);

        this.setVisible(true);
    }

    /**
     * Constructs WindowablePanel
     *
     * @param frame      Frame parent
     * @param title      Title of the panel
     * @param icon       Icon
     * @param windowable Indicates if the panel can be detach/attach
     * @param hideable   Indicates if the content can be hide/show
     * @param closable   Indicate if the panel can be close
     */
    public WindowablePanel(
            final JFrame frame, final String title, final Icon icon, final boolean windowable, final boolean hideable,
            final boolean closable)
    {
        this(frame, title, icon, windowable, hideable, closable, null);
    }

    /**
     * Constructs WindowablePanel
     *
     * @param frame           Frame parent
     * @param title           Title of the panel
     * @param icon            Icon
     * @param windowable      Indicates if the panel can be detach/attach
     * @param hideable        Indicates if the content can be hide/show
     * @param closable        Indicate if the panel can be close
     * @param componentCenter Component center
     */
    public WindowablePanel(
            final JFrame frame, String title, final Icon icon, final boolean windowable, final boolean hideable,
            final boolean closable,
            final JComponent componentCenter)
    {
        if (frame == null)
        {
            throw new NullPointerException("The frame mustn't be null !");
        }

        WindowablePanel.arrayListWindowablePanel.add(this);

        if (title == null)
        {
            title = "";
        }

        this.componentCenter = componentCenter;

        this.setBorder(BorderFactory.createEtchedBorder());

        this.dialog = new JDialog(frame, null, false);
        this.dialog.setUndecorated(true);
        this.dialog.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        this.windowable = windowable;
        this.hideable = hideable;
        this.closable = closable;

        this.isWindow = false;
        this.hide = false;
        this.firstShow = true;
        this.close = false;

        this.createComponents(title, icon);
        this.layoutComponents();
        this.addListeners();

        final Dimension dimension = new Dimension(100, 100);
        this.setMinimumSize(dimension);
    }

    /**
     * Add listeners
     */
    private void addListeners()
    {
        if (this.buttonToWindow != null)
        {
            this.buttonToWindow.addActionListener(this);
        }
        if (this.buttonHide != null)
        {
            this.buttonHide.addActionListener(this);
        }
        if (this.buttonClose != null)
        {
            this.buttonClose.addActionListener(this);
        }
    }

    /**
     * Indicates if this windowable panel contains a given one
     *
     * @param windowablePanel Windowable panel test
     * @return {@code true} if the windowable panel in this
     */
    private boolean containsWindowablePanel(final WindowablePanel windowablePanel)
    {
        if (this == windowablePanel)
        {
            return true;
        }

        if (this.tabbedPane == null)
        {
            return false;
        }

        final int tabCount = this.tabbedPane.getTabCount();
        for (int i = 0; i < tabCount; i++)
        {
            final Component child = this.tabbedPane.getComponentAt(i);
            if (child == windowablePanel.panelMain)
            {
                return true;
            }

            if (child instanceof HaveHeaderPanel)
            {
                if (((HaveHeaderPanel) child).getWindowablePanelParent()
                                             .containsWindowablePanel(windowablePanel))
                {
                    return true;
                }
            }

            if (child instanceof WindowablePanel)
            {
                if (((WindowablePanel) child).containsWindowablePanel(windowablePanel))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Create components inside the panel
     *
     * @param title Title
     * @param icon  Icon
     */
    private void createComponents(final String title, final Icon icon)
    {
        this.panelMain = new HaveHeaderPanel(new BorderLayout());
        this.panelHeader = new JPanel(new BorderLayout());
        if (icon != null)
        {
            this.labelIcon = new JLabel(icon);
        }
        this.labelTitle = new JLabel(title, SwingConstants.CENTER);
        if ((this.windowable) || (this.hideable) || (this.closable))
        {
            this.panelButtons = new JPanel(new FlowLayout());
        }
        if (this.windowable)
        {
            this.buttonToWindow = new JToggleButton();
            this.buttonToWindow.setIcon(ResourcesGUI.ICON_DETACH_WINDOW);
            this.buttonToWindow.setSelectedIcon(ResourcesGUI.ICON_ATTACH_WINDOW);
            this.buttonToWindow.setMargin(new Insets(0, 0, 0, 0));
            this.buttonToWindow.setBorder(null);
            this.buttonToWindow.setBorderPainted(false);
            this.buttonToWindow.setFocusPainted(false);
        }
        if (this.hideable)
        {
            this.buttonHide = new JToggleButton();
            this.buttonHide.setIcon(ResourcesGUI.ICON_HIDE_NORMAL);
            this.buttonHide.setSelectedIcon(ResourcesGUI.ICON_SHOW_NORMAL);
            this.buttonHide.setRolloverIcon(ResourcesGUI.ICON_HIDE_OVER);
            this.buttonHide.setRolloverSelectedIcon(ResourcesGUI.ICON_SHOW_OVER);
            this.buttonHide.setMargin(new Insets(0, 0, 0, 0));
            this.buttonHide.setBorder(null);
            this.buttonHide.setBorderPainted(false);
            this.buttonHide.setFocusPainted(false);
            this.buttonHide.setRolloverEnabled(true);
        }
        if (this.closable)
        {
            this.buttonClose = new JButton();
            this.buttonClose.setIcon(ResourcesGUI.ICON_COSE_NORMAL);
            this.buttonClose.setRolloverIcon(ResourcesGUI.ICON_CLOSE_OVER);
            this.buttonClose.setMargin(new Insets(0, 0, 0, 0));
            this.buttonClose.setBorder(null);
            this.buttonClose.setBorderPainted(false);
            this.buttonClose.setFocusPainted(false);
            this.buttonClose.setRolloverEnabled(true);
        }

        if (this.componentCenter == null)
        {
            this.tabbedPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        }
        else
        {
            this.panelCenter = new JPanel(new BorderLayout());
        }
    }

    /**
     * Force the associated dialog to have a specific size
     *
     * @param width  Desired width
     * @param height Desired height
     */
    private void forceSizeDialog(final int width, final int height)
    {
        if (this.mutexForced == null)
        {
            this.mutexForced = new Mutex();
        }

        this.mutexForced.playInCriticalSectionVoid(() ->
                                                   {
                                                       Utilities.sleep(16);

                                                       Dimension dimension = this.dialog.getSize();
                                                       Debug.debug("WANTED : ", dimension, "->", width, "x", height);

                                                       while ((dimension.width != width) ||
                                                              (height != dimension.height))
                                                       {
                                                           Debug.debug(dimension, "->", width, "x", height);

                                                           Utilities.sleep(16);

                                                           this.dialog.setSize(width, height);

                                                           Utilities.sleep(16);

                                                           dimension = this.dialog.getSize();
                                                       }

                                                       Debug.debug("FINISHED : ", this.dialog.getSize(), "<->", width,
                                                                   "x",
                                                                   height);
                                                   });
    }

    /**
     * Layout components inside the panel
     */
    private void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(this.panelMain, BorderLayout.CENTER);
        this.panelMain.add(this.panelHeader, BorderLayout.NORTH);

        if (this.componentCenter == null)
        {
            this.panelMain.add(this.tabbedPane, BorderLayout.CENTER);
        }
        else
        {
            this.panelCenter.add(this.componentCenter, BorderLayout.CENTER);
            this.panelMain.add(this.panelCenter, BorderLayout.CENTER);
        }

        if (this.labelIcon != null)
        {
            this.panelHeader.add(this.labelIcon, BorderLayout.WEST);
        }
        this.panelHeader.add(this.labelTitle, BorderLayout.CENTER);
        if (this.panelButtons != null)
        {
            this.panelHeader.add(this.panelButtons, BorderLayout.EAST);

            if (this.buttonToWindow != null)
            {
                this.panelButtons.add(this.buttonToWindow);
            }
            if (this.buttonHide != null)
            {
                this.panelButtons.add(this.buttonHide);
            }
            if (this.buttonClose != null)
            {
                this.panelButtons.add(this.buttonClose);
            }
        }
    }

    /**
     * Signal to listeners the windowable panel just attach
     */
    protected void fireWindowablePanelAttach()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_ATTACH);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelAttach(windowablePanelEvent);
        }
    }

    /**
     * Signal to listeners the windowable panel just close
     */
    protected void fireWindowablePanelClose()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_CLOSE);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelClose(windowablePanelEvent);
        }
    }

    /**
     * Signal to listeners the windowable panel just detach
     */
    protected void fireWindowablePanelDetach()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_DETACH);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelDetach(windowablePanelEvent);
        }
    }

    /**
     * Signal to listeners the windowable panel content just hide
     */
    protected void fireWindowablePanelHide()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_HIDE);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelHide(windowablePanelEvent);
        }
    }

    /**
     * Signal to listeners the windowable panel just open
     */
    protected void fireWindowablePanelOpen()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_OPEN);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelOpen(windowablePanelEvent);
        }
    }

    /**
     * Signal to listeners the windowable panel content just show
     */
    protected void fireWindowablePanelShow()
    {
        final WindowablePanelEvent windowablePanelEvent = new WindowablePanelEvent(this,
                                                                                   WindowablePanelEvent.EVENT_WINDOWABLE_PANEL_SHOW);
        final WindowablePanelListener[] windowablePanelListeners = this.listenerList.getListeners(
                WindowablePanelListener.class);
        for (final WindowablePanelListener windowablePanelListener : windowablePanelListeners)
        {
            windowablePanelListener.windowablePanelShow(windowablePanelEvent);
        }
    }

    /**
     * Indicated if can add other windoable panel
     *
     * @return {@code true} if can add other windoable panel
     */
    public boolean acceptAddOtherWindowablePanel()
    {
        return this.componentCenter == null;
    }

    /**
     * Action when a button is press
     *
     * @param e Event description
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        final Object source = e.getSource();
        if (source == this.buttonToWindow)
        {
            this.setWindow(this.buttonToWindow.isSelected());
        }
        else if (source == this.buttonHide)
        {
            this.setHide(this.buttonHide.isSelected());
        }
        else if (source == this.buttonClose)
        {
            this.setClose(true);
        }
    }

    /**
     * Add component to the panel
     *
     * @param component Component to add
     * @param title     Title
     * @param icon      Icon
     * @param toolTip   Tool tip text
     */
    public void addComponent(final JComponent component, final String title, final Icon icon, final String toolTip)
    {
        if (this.tabbedPane == null)
        {
            return;
        }

        this.tabbedPane.addTab(title, icon, component, toolTip);
    }

    /**
     * Attach a windowable panel in it
     *
     * @param windowablePanel Windowable panel to attach
     */
    public void addWindowablePanel(final WindowablePanel windowablePanel)
    {
        if (this.containsWindowablePanel(windowablePanel))
        {
            return;
        }
        if (windowablePanel.containsWindowablePanel(this))
        {
            return;
        }
        windowablePanel.setWindow(false);
        Icon icon = null;
        if (windowablePanel.labelIcon != null)
        {
            icon = windowablePanel.labelIcon.getIcon();
        }
        this.addComponent(windowablePanel.panelMain, windowablePanel.labelTitle.getText(), icon, null);
    }

    /**
     * Add listener to windowable panel events
     *
     * @param windowablePanelListener Listener to add
     */
    public void addWindowablePanelListener(final WindowablePanelListener windowablePanelListener)
    {
        this.listenerList.add(WindowablePanelListener.class, windowablePanelListener);
    }

    /**
     * Change component title
     *
     * @param indes Component index
     * @param title New title
     */
    public void changeTitle(final int indes, final String title)
    {
        if (this.tabbedPane == null)
        {
            return;
        }

        this.tabbedPane.setTitleAt(indes, title);
    }

    /**
     * Return componentCenter
     *
     * @return componentCenter
     */
    public Component getComponentCenter()
    {
        return this.componentCenter;
    }

    /**
     * Return panelMain
     *
     * @return panelMain
     */
    public HaveHeaderPanel getPanelMain()
    {
        return this.panelMain;
    }

    /**
     * Compute component bounds.<br>
     * Use it instead of {@link #getBounds()}
     *
     * @return Component bounds
     */
    public Rectangle getRealBounds()
    {
        if (this.isWindow)
        {
            return new Rectangle(this.panelMain.getLocationOnScreen(), this.getRealDimension());
        }

        return this.getBounds();
    }

    /**
     * Obtain a component.<br>
     * Use it instead of {@link #getComponent(int)}
     *
     * @param index Component index
     * @return Component
     */
    public JComponent getRealComponent(final int index)
    {
        if (this.tabbedPane == null)
        {
            return this.componentCenter;
        }

        return (JComponent) this.tabbedPane.getComponentAt(index);
    }

    /**
     * Number of elements.<br>
     * Use it instead of {@link #getComponentCount()}
     *
     * @return Number of elements
     */
    public int getRealComponentCount()
    {
        if (this.tabbedPane == null)
        {
            return 1;
        }

        return this.tabbedPane.getTabCount();
    }

    /**
     * Real dimension of the panel
     *
     * @return Real dimension of the panel
     */
    public Dimension getRealDimension()
    {
        if (this.isWindow)
        {
            return SwingUtilities.getWindowAncestor(this.panelMain).getSize();
        }
        return this.getSize();
    }

    /**
     * Title
     *
     * @return Title
     */
    public String getTitle()
    {
        return this.labelTitle.getText();
    }

    /**
     * Return closable
     *
     * @return closable
     */
    public boolean isClosable()
    {
        return this.closable;
    }

    /**
     * Return close
     *
     * @return close
     */
    public boolean isClose()
    {
        return this.close;
    }

    /**
     * Modify close
     *
     * @param close New close value
     */
    public void setClose(final boolean close)
    {
        if (!this.closable)
        {
            throw new IllegalStateException("The panel is not closable, so you can't change it's close state");
        }

        if (this.close == close)
        {
            return;
        }

        this.close = close;

        if (this.close)
        {
            this.fireWindowablePanelClose();
        }
        else
        {
            this.fireWindowablePanelOpen();
        }

        if (this.dialog == null)
        {
            this.dialog = new JDialog(UtilGUI.searchFrameParent(this.component), null, false);
            this.dialog.setUndecorated(true);
            this.dialog.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }

        if (this.close)
        {
            if (this.isWindow)
            {
                this.dialog.setVisible(false);
                return;
            }

            this.setVisible(false);
            final Container parent = this.getParent();
            if (parent != null)
            {
                parent.invalidate();
                parent.validate();
                parent.doLayout();
                parent.repaint();

                this.windowablePanelParent = WindowablePanel.getWindowablePanelParent(this.panelMain);
                if ((this.windowablePanelParent != null) && (this.windowablePanelParent != this))
                {
                    this.windowablePanelParent.tabbedPane.remove(this.panelMain);
                    this.windowablePanelParent.revalidate();
                    this.windowablePanelParent.doLayout();
                    this.windowablePanelParent.repaint();
                }
                else
                {
                    this.windowablePanelParent = null;
                }
            }
            return;
        }

        if (this.isWindow)
        {
            this.dialog.setVisible(true);
            return;
        }

        this.setVisible(true);
        if (this.windowablePanelParent != null)
        {
            this.windowablePanelParent.addWindowablePanel(this);

            this.windowablePanelParent.revalidate();
            this.windowablePanelParent.doLayout();
            this.windowablePanelParent.repaint();
        }

        final Container parent = this.getParent();
        if (parent != null)
        {
            parent.invalidate();
            parent.validate();
            parent.doLayout();
            parent.repaint();
        }
    }

    /**
     * Return hide
     *
     * @return hide
     */
    public boolean isHide()
    {
        return this.hide;
    }

    /**
     * Modify hide
     *
     * @param hide New hide value
     */
    public void setHide(final boolean hide)
    {
        if (!this.hideable)
        {
            throw new IllegalStateException("The panel is not hideable, so you can't change it's hide state");
        }

        if (this.hide == hide)
        {
            return;
        }

        this.hide = hide;

        if (this.dialog == null)
        {
            this.dialog = new JDialog(UtilGUI.searchFrameParent(this.component), null, false);
            this.dialog.setUndecorated(true);
            this.dialog.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }

        if (this.hide)
        {
            Dimension dimension = null;
            if (this.isWindow)
            {
                dimension = this.dialog.getSize();
            }
            else
            {
                dimension = this.panelMain.getSize();
            }
            this.width = dimension.width;
            this.height = dimension.height;

            Debug.debug("MEM=", dimension);
        }

        this.dialog.setResizable(!this.hide);
        if (this.tabbedPane != null)
        {
            this.tabbedPane.setVisible(!this.hide);
        }
        else
        {
            this.panelCenter.setVisible(!this.hide);
        }
        this.panelMain.revalidate();
        this.panelMain.doLayout();
        this.panelMain.repaint();

        final Dimension size = this.panelMain.getLayout().preferredLayoutSize(this.panelMain);

        if (this.hide)
        {
            if (this.isWindow)
            {
                final Dimension dimension = this.dialog.getSize();
                this.width = dimension.width;
                this.height = dimension.height;

                Debug.debug("MEM2=", dimension);

                Debug.debug("BEFORE=", this.dialog.getSize());
                final int width = this.dialog.getWidth();

                this.dialog.pack();

                this.forceSizeDialog(width, Math.min(this.dialog.getHeight(), size.height + 6));

                Debug.debug("AFTER=", this.dialog.getSize());
            }
        }
        else
        {
            if (this.isWindow)
            {
                Debug.debug("REST=", this.width, "x", this.height);
                this.forceSizeDialog(this.width, this.height);
            }
        }

        this.buttonHide.removeActionListener(this);
        this.buttonHide.setSelected(this.hide);
        this.buttonHide.addActionListener(this);

        if (this.hide)
        {
            this.fireWindowablePanelHide();
        }
        else
        {
            this.fireWindowablePanelShow();
        }
    }

    /**
     * Return hideable
     *
     * @return hideable
     */
    public boolean isHideable()
    {
        return this.hideable;
    }

    /**
     * Return isWindow
     *
     * @return isWindow
     */
    public boolean isWindow()
    {
        return this.isWindow;
    }

    /**
     * Modify isWindow
     *
     * @param isWindow New isWindow value
     */
    public void setWindow(final boolean isWindow)
    {
        if (!this.windowable)
        {
            throw new IllegalStateException("The panel is not windowable, so you can't change it's window state");
        }

        if (this.isWindow == isWindow)
        {
            return;
        }

        this.isWindow = isWindow;

        if (this.dialog == null)
        {
            this.dialog = new JDialog(UtilGUI.searchFrameParent(this.component), null, false);
            this.dialog.setUndecorated(true);
            this.dialog.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        }

        if (this.isWindow)
        {
            this.setBorder(null);
            final Point point = this.panelMain.getLocationOnScreen();
            this.remove(this.panelMain);

            this.dialog.setContentPane(this.panelMain);
            if ((this.firstShow) || (this.hide))
            {
                this.dialog.pack();
            }
            if (this.firstShow)
            {
                this.dialog.setLocation(point);
            }
            this.firstShow = false;
            this.dialog.setVisible(true);
            this.dialog.getRootPane().updateUI();

            this.revalidate();
            this.doLayout();
            this.repaint();
        }
        else
        {
            this.dialog.setVisible(false);
            this.dialog.getRootPane().getUI().uninstallUI(this.dialog.getRootPane());
            this.add(this.panelMain, BorderLayout.CENTER);

            this.setBorder(BorderFactory.createEtchedBorder());

            this.revalidate();
            this.doLayout();
            this.repaint();
        }

        this.buttonToWindow.removeActionListener(this);
        this.buttonToWindow.setSelected(this.isWindow);
        this.buttonToWindow.addActionListener(this);

        if (this.isWindow)
        {
            this.fireWindowablePanelDetach();
        }
        else
        {
            this.fireWindowablePanelAttach();
        }
    }

    /**
     * Return windowable
     *
     * @return windowable
     */
    public boolean isWindowable()
    {
        return this.windowable;
    }

    /**
     * Old height
     *
     * @return Old height
     */
    public int oldHeight()
    {
        return this.height;
    }

    /**
     * Old width
     *
     * @return Old width
     */
    public int oldWidth()
    {
        return this.width;
    }

    /**
     * Remove listener
     *
     * @param windowablePanelListener Listener to remove
     */
    public void removeWindowablePanelListener(final WindowablePanelListener windowablePanelListener)
    {
        this.listenerList.remove(WindowablePanelListener.class, windowablePanelListener);
    }

    /**
     * Select a component
     *
     * @param index Component index
     */
    public void selectComponent(final int index)
    {
        if (this.tabbedPane == null)
        {
            return;
        }

        this.tabbedPane.setSelectedIndex(index);
    }

    /**
     * Change component bounds.<br>
     * Use it instead of {@link #setBounds(int, int, int, int)}
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     */
    public void setRealBounds(final int x, final int y, final int width, final int height)
    {
        if (this.isWindow)
        {
            SwingUtilities.getWindowAncestor(this.panelMain).setBounds(x, y, width, height);
            return;
        }

        this.setBounds(x, y, width, height);
    }

    /**
     * String representation
     *
     * @return String representation
     * @see Component#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder(WindowablePanel.class.getName());
        stringBuffer.append(" [");
        stringBuffer.append(this.getTitle());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}