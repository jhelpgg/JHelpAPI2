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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import jhelp.gui.JHelpFrameImage;
import jhelp.gui.JHelpMouseListener;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;
import jhelp.util.list.Triplet;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Frame that carry components 2D
 *
 * @author JHelp
 */
public class JHelpFrame2D
        extends JHelpFrameImage
        implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, JHelpWindow2D
{
    /**
     * serialVersionUID
     */
    private static final long      serialVersionUID  = 8861480138627452386L;
    /**
     * Default tips font
     */
    public static final  JHelpFont DEFAULT_TIPS_FONT = new JHelpFont("Arial", 14);

    /**
     * Main panel root of all others
     *
     * @author JHelp
     */
    class PanelRoot
            extends JHelpPanel2D
    {
        /**
         * Create a new instance of PanelRoot
         *
         * @param layout Layout to use
         */
        PanelRoot(final JHelpLayout layout)
        {
            super(layout);
        }

        /**
         * Component window owner, here the frame itself <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return Component window owner, here the frame itself
         * @see jhelp.gui.twoD.JHelpComponent2D#obtainOwner()
         */
        @Override
        public JHelpWindow2D obtainOwner()
        {
            return JHelpFrame2D.this;
        }
    }

    /**
     * Indicate if automatic refresh is running
     */
    private       boolean          automaticRefresh;
    /**
     * Background color
     */
    private final int              backgroundColor;
    /**
     * Actual focused component
     */
    private       JHelpComponent2D focusedComponent;
    /**
     * Synchronization lock
     */
    private final Object lock = new Object();
    /**
     * Option pane for user interaction
     */
    private       JHelpOptionPane2D optionPane;
    /**
     * Actual component where lies the mouse
     */
    private       JHelpComponent2D  overComponent;
    /**
     * Main 2D panel
     */
    private final PanelRoot         panel;
    /**
     * Task for signal key events
     */
    private final ConsumerTask<Pair<KeyListener, KeyEvent>>          signalKeyEventTask   =
            new ConsumerTask<Pair<KeyListener, KeyEvent>>()
            {
                /**
                 * Do the signal for key event
                 *
                 * @param parameter
                 *           Couple : listener to alert, event to give
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(final Pair<KeyListener, KeyEvent> parameter)
                {
                    switch (parameter.second.getID())
                    {
                        case KeyEvent.KEY_PRESSED:
                            parameter.first.keyPressed(parameter.second);
                            break;
                        case KeyEvent.KEY_RELEASED:
                            parameter.first.keyReleased(parameter.second);
                            break;
                        case KeyEvent.KEY_TYPED:
                            parameter.first.keyTyped(parameter.second);
                            break;
                    }
                }
            };
    /**
     * Task for signal mouse events
     */
    private final ConsumerTask<Pair<JHelpMouseListener, MouseEvent>> signalMouseEventTask =
            new ConsumerTask<Pair<JHelpMouseListener, MouseEvent>>()
            {
                /**
                 * Do the signal for mouse event
                 *
                 * @param parameter
                 *           Couple : listener to alert, event to give
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(final Pair<JHelpMouseListener, MouseEvent> parameter)
                {
                    if ((parameter == null) || (parameter.first == null)
                        || (parameter.second == null))
                    {
                        return;
                    }

                    switch (parameter.second.getID())
                    {
                        case MouseEvent.MOUSE_CLICKED:
                            parameter.first.mouseClicked(parameter.second);
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            parameter.first.mouseDragged(parameter.second);
                            break;
                        case MouseEvent.MOUSE_ENTERED:
                            parameter.first.mouseEntered(parameter.second);
                            break;
                        case MouseEvent.MOUSE_EXITED:
                            parameter.first.mouseExited(parameter.second);
                            break;
                        case MouseEvent.MOUSE_MOVED:
                            parameter.first.mouseMoved(parameter.second);
                            break;
                        case MouseEvent.MOUSE_PRESSED:
                            parameter.first.mousePressed(parameter.second);
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            parameter.first.mouseReleased(parameter.second);
                            break;
                        case MouseEvent.MOUSE_WHEEL:
                            parameter.first.mouseWheelMoved((MouseWheelEvent) parameter.second);
                            break;
                    }
                }
            };
    /**
     * Dialogs stack
     */
    private final ArrayList<JHelpDialog2D> stackDialog;
    /**
     * Waiting time
     */
    private       int                      wait;
    /**
     * Task that refresh automatically
     */
    private final RunnableTask automaticRefreshTask = new RunnableTask()
    {
        /**
         * Refresh the frmae <br>
         * <br>
         * <b>Parent
         * documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()
         */
        @Override
        public void run()
        {
            JHelpFrame2D.this.update();
        }
    };

    /**
     * Create a new instance of JHelpFrame2D
     *
     * @param layout Layout to use on main panel
     */
    public JHelpFrame2D(final JHelpLayout layout)
    {
        this("JHelp Frame 2D", layout);
    }

    /**
     * Create a new instance of JHelpFrame2D
     *
     * @param title  Frame title
     * @param layout Layout to use on main panel
     */
    public JHelpFrame2D(final String title, final JHelpLayout layout)
    {
        super(title);

        this.panel = new PanelRoot(layout);
        this.backgroundColor = 0xFFFEDCBA;
        this.automaticRefresh = false;

        this.componentAddMouseListener(this);
        this.componentAddMouseMotionListener(this);
        this.componentAddMouseWheelListener(this);
        this.componentAddKeyListener(this);

        this.wait = 1024;
        this.stackDialog = new ArrayList<JHelpDialog2D>();

        this.setImageOverActive(true);
    }

    /**
     * Compute component under the mouse
     *
     * @param event Mouse event description (Contains mouse position)
     * @return The component, the associated mouse listener and translated mouse event to be relative to the component
     */
    private Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> obtainMouseArea(final MouseEvent event)
    {
        final int                                  size = this.stackDialog.size();
        Pair<JHelpComponent2D, JHelpMouseListener> pair;

        int xx = 0;
        int yy = 0;

        if (size > 0)
        {
            final JHelpDialog2D dialog = this.stackDialog.get(size - 1);
            xx = -dialog.x();
            yy = -dialog.y();
            final int       mx        = event.getX();
            final int       my        = event.getY();
            final int       dx        = dialog.x();
            final int       dy        = dialog.y();
            final Dimension dimension = dialog.componentInternSize();

            pair = dialog.mouseOver(mx + xx, my + yy);

            if ((pair == null) && (MouseEvent.MOUSE_CLICKED == event.getID()) && (dialog.clickOutClose())
                && ((mx < dx) || (mx >= (dx + dimension.width)) || (my < dy) || (my >= (dy + dimension.height))))
            {
                dialog.visible(false);
            }
        }
        else
        {
            pair = this.panel.mouseOver(event.getX(), event.getY());
        }

        if (pair == null)
        {
            return null;
        }

        event.setSource(pair.first);

        return new Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent>(pair.first, pair.second, event);
    }

    /**
     * Show the option pane
     *
     * @param title                 Dialog title, {@code null} for no title
     * @param optionPaneMessageType Option pane type
     * @param message               Message to show
     * @param editText              Text of edit text, {@code null} if no input text
     * @param hasCancel             Indicates if theire a cancel button
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to callback
     * @param actionID              Action ID give back to the listener
     * @param developerInformation  Object give back to the listener
     */
    private void showOptionPane(
            final String title, final OptionPaneMessageType optionPaneMessageType,
            final String message, final String editText,
            final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        if (this.optionPane == null)
        {
            this.optionPane = new JHelpOptionPane2D();
            final JHelpDialog2D dialog2d = this.createDialog(
                    new JHelpBackgroundRoundRectangle(this.optionPane, 0xFFFFFFFF));
            dialog2d.clickOutClose(false);
        }

        this.optionPane.showOptionPane(title, optionPaneMessageType, message, editText, hasCancel, hasNo,
                                       optionPaneListener, actionID, developerInformation);
    }

    /**
     * Signal that key event happen
     *
     * @param keyEvent Key event
     */
    private void signalKeyEvent(final KeyEvent keyEvent)
    {
        final JHelpComponent2D component = this.focusedComponent;
        if (component == null)
        {
            return;
        }

        final KeyListener keyListener = component.keyListener();
        if (keyListener == null)
        {
            return;
        }

        ThreadManager.parallel(this.signalKeyEventTask,
                               new Pair<KeyListener, KeyEvent>(keyListener, keyEvent));
    }

    /**
     * Update mouse position
     *
     * @param component2d Component under the mouse
     * @param mx          Mouse X
     * @param my          Mouse Y
     * @param mouseEvent  Mouse event description
     */
    private void updateMousePosition(
            final JHelpComponent2D component2d, final int mx, final int my,
            final MouseEvent mouseEvent)
    {
        if (this.overComponent != null)
        {
            if (this.overComponent == component2d)
            {
                final Point  position = component2d.absolutePosition();
                final String tips     = this.overComponent.toolTip(mx - position.x, my - position.y);
                this.printTips(mx + 16, my - 16, tips);

                return;
            }

            final MouseEvent me = new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(),
                                                 mouseEvent.getModifiers(), mx, my,
                                                 mouseEvent.getClickCount(), mouseEvent.isPopupTrigger());
            me.setSource(this.overComponent);

            ThreadManager.parallel(this.signalMouseEventTask,
                                   new Pair<>(this.overComponent.mouseListener(), me));
        }

        if (component2d != null)
        {
            final MouseEvent me = new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(),
                                                 mouseEvent.getModifiers(), mx, my,
                                                 mouseEvent.getClickCount(), mouseEvent.isPopupTrigger());
            me.setSource(component2d);

            ThreadManager.parallel(this.signalMouseEventTask,
                                   new Pair<>(component2d.mouseListener(), me));
        }

        this.overComponent = component2d;

        if (this.overComponent != null)
        {
            final String tips = this.overComponent.toolTip(mx, my);
            this.printTips(mx + 16, my - 16, tips);
        }
        else
        {
            this.hideTips();
        }
    }

    /**
     * Change a dialog visibility
     *
     * @param dialog  Dialog to change visibility
     * @param visible New visibility state
     */
    void dialogVisible(final JHelpDialog2D dialog, final boolean visible)
    {
        synchronized (this.lock)
        {
            final int size  = this.stackDialog.size();
            final int index = this.stackDialog.indexOf(dialog);

            if (!visible)
            {
                if (index >= 0)
                {
                    if (dialog.visible())
                    {
                        dialog.updateVisible(false);
                    }

                    this.stackDialog.remove(index);

                    if ((index == (size - 1)) && (index > 0))
                    {
                        this.stackDialog.get(index - 1)
                                        .updateVisible(true);
                    }
                    else
                    {
                        this.allDialogAreHiden();
                    }
                }

                return;
            }

            if (index < 0)
            {
                if (size > 0)
                {
                    this.stackDialog.get(size - 1)
                                    .updateVisible(false);
                }

                this.stackDialog.add(dialog);
                dialog.updateVisible(true);

                if (size == 0)
                {
                    this.atLeastOneDialogIsVisible();
                }

                return;
            }

            if (index == (size - 1))
            {
                return;
            }

            this.stackDialog.get(size - 1)
                            .updateVisible(false);
            this.stackDialog.remove(index);
            this.stackDialog.add(dialog);
            dialog.updateVisible(true);
        }
    }

    /**
     * Called when all dialod are hide, do nothing by default
     */
    protected void allDialogAreHiden()
    {
    }

    /**
     * Called when first dialog is shown, do nothing by default
     */
    protected void atLeastOneDialogIsVisible()
    {
    }

    /**
     * Root panel
     *
     * @return Root panel
     */
    protected JHelpPanel2D getPanelRoot()
    {
        return this.panel;
    }

    /**
     * Force refresh all components and dialogs size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.JHelpFrameImage#updateSize()
     */
    @Override
    protected void updateSize()
    {
        super.updateSize();

        final Stack<JHelpComponent2D> stack = new Stack<JHelpComponent2D>();

        if (this.panel != null)
        {
            stack.push(this.panel);
        }

        if (this.stackDialog != null)
        {
            for (final JHelpDialog2D dialog2d : this.stackDialog)
            {
                stack.push(dialog2d.root());
            }
        }

        JHelpComponent2D component2d;
        JHelpContainer2D container2d;

        while (!stack.isEmpty())
        {
            component2d = stack.pop();
            component2d.fastInvalidate();

            if (component2d instanceof JHelpContainer2D)
            {
                container2d = (JHelpContainer2D) component2d;

                for (final JHelpComponent2D child : container2d.children())
                {
                    stack.push(child);
                }
            }
        }
    }

    /**
     * Add a component 2D to main panel
     *
     * @param component   Component to add
     * @param constraints Constraints to use
     */
    public void addComponent2D(final JHelpComponent2D component, final JHelpConstraints constraints)
    {
        this.panel.addComponent2D(component, constraints);
    }

    /**
     * Indicates if automatic refresh is started
     *
     * @return {@code true} if automatic refresh is started
     */
    public boolean automaticRefresh()
    {
        return this.automaticRefresh;
    }

    /**
     * Change automatic refresh status
     *
     * @param automaticRefresh New automatic refresh status
     */
    public void automaticRefresh(final boolean automaticRefresh)
    {
        if (this.automaticRefresh == automaticRefresh)
        {
            return;
        }

        this.automaticRefresh = automaticRefresh;
        this.wait = 1;

        if (automaticRefresh)
        {
            this.update();
        }
    }

    /**
     * Create a dialog associated to the frame
     *
     * @param component2d Dialog content
     * @return Created dialog
     */
    public JHelpDialog2D createDialog(final JHelpComponent2D component2d)
    {
        if (component2d == null)
        {
            throw new NullPointerException("component2d mustn't be null");
        }

        synchronized (this.lock)
        {

            return new JHelpDialog2D(component2d, this);
        }
    }

    /**
     * Hide the tips
     */
    public void hideTips()
    {
        this.printTips(0, 0, null);
    }

    /**
     * Called when key typed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param keyEvent Key event
     * @see KeyListener#keyTyped(KeyEvent)
     */
    @Override
    public void keyTyped(final KeyEvent keyEvent)
    {
        this.signalKeyEvent(keyEvent);
    }

    /**
     * Called when key pressed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param keyEvent Key event
     * @see KeyListener#keyPressed(KeyEvent)
     */
    @Override
    public void keyPressed(final KeyEvent keyEvent)
    {
        this.signalKeyEvent(keyEvent);
    }

    /**
     * Called when key released <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param keyEvent Key event
     * @see KeyListener#keyReleased(KeyEvent)
     */
    @Override
    public void keyReleased(final KeyEvent keyEvent)
    {
        this.signalKeyEvent(keyEvent);
    }

    /**
     * Called when mouse clicked <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseListener#mouseClicked(MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element1.focusable())
            {
                if (this.focusedComponent != null)
                {
                    this.focusedComponent.haveFocus(false);
                }

                this.focusedComponent = triplet.element1;
                this.focusedComponent.haveFocus(true);
            }

            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Called when mouse pressed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseListener#mousePressed(MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Called when mouse released <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseListener#mouseReleased(MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Called when mouse entered <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseListener#mouseEntered(MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Called when mouse exited <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseListener#mouseExited(MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element1 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Called when mouse dragged <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseMotionListener#mouseDragged(MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();

        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            this.updateMousePosition(triplet.element1, mx, my, triplet.element3);

            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
        else
        {
            this.updateMousePosition(null, mx, my, e);
        }
    }

    /**
     * Called when mouse moved <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseMotionListener#mouseMoved(MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();

        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            this.updateMousePosition(triplet.element1, mx, my, triplet.element3);

            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
        else
        {
            this.updateMousePosition(UtilTwoD.component2DFromMouseEvent(e), mx, my, e);
        }
    }

    /**
     * Called when mouse wheel moved <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(final MouseWheelEvent e)
    {
        final Triplet<JHelpComponent2D, JHelpMouseListener, MouseEvent> triplet = this.obtainMouseArea(e);

        if (triplet != null)
        {
            if (triplet.element2 != null)
            {
                ThreadManager.parallel(this.signalMouseEventTask,
                                       new Pair<>(triplet.element2, triplet.element3));
            }
        }
    }

    /**
     * Print a tips
     *
     * @param x    X
     * @param y    Y
     * @param tips Tips text ({@code null} for hide the tip)
     */
    public void printTips(final int x, final int y, final String tips)
    {
        this.printTips(x, y, tips, JHelpFrame2D.DEFAULT_TIPS_FONT, 0xFF000000, 0x80FFFF00, 0xFF000000,
                       JHelpTextAlign.CENTER, true);
    }

    /**
     * Print a tips
     *
     * @param x               X
     * @param y               Y
     * @param tips            Tips text ({@code null} for hide the tip)
     * @param font            Font to use
     * @param colorText       Text color
     * @param colorBackground Background color
     * @param colorBorder     Border color
     * @param textAlign       Text alignment
     * @param borderLetter    Indicates if draw border letters
     */
    public synchronized void printTips(
            int x, int y, final String tips, final JHelpFont font, final int colorText,
            final int colorBackground,
            final int colorBorder, final JHelpTextAlign textAlign, final boolean borderLetter)
    {
        final JHelpImage imageOver = this.getImageOver();

        if (tips == null)
        {
            imageOver.startDrawMode();
            imageOver.clear(0);
            imageOver.endDrawMode();

            return;
        }

        final Pair<List<JHelpTextLineAlpha>, Dimension> pair = font.computeTextLinesAlpha(tips, textAlign);

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (imageOver)
        {
            imageOver.startDrawMode();

            imageOver.clear(0);

            final int width  = pair.second.width + 6;
            final int height = pair.second.height + 6;

            x = Math.max(0, Math.min(x, imageOver.getWidth() - width - 1));
            y = Math.max(0, Math.min(y, imageOver.getHeight() - height - 1));

            imageOver.fillRectangle(x, y, width, height, colorBackground);

            final int col = colorText ^ 0x00FFFFFF;

            for (final JHelpTextLineAlpha textLine : pair.first)
            {
                if (borderLetter)
                {
                    for (int yy = -1; yy <= 1; yy++)
                    {
                        for (int xx = -1; xx <= 1; xx++)
                        {
                            imageOver.paintAlphaMask(x + textLine.getX() + 3 + xx, y + textLine.getY() + 3 + yy,
                                                     textLine.getMask(), col);
                        }
                    }
                }

                imageOver.paintAlphaMask(x + textLine.getX() + 3, y + textLine.getY() + 3, textLine.getMask(),
                                         colorText);
            }

            imageOver.drawRectangle(x, y, width, height, colorBorder);

            imageOver.endDrawMode();
        }
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param optionPaneMessageType Option pane type
     * @param message               Message to user
     * @param hasCancel             Indicates if theire a cancel button
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String message,
            final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneInput(optionPaneMessageType, null, message, "", hasCancel, hasNo, optionPaneListener,
                                 actionID,
                                 developerInformation);
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param optionPaneMessageType Option pane type
     * @param title                 Dialog title
     * @param message               Message to user
     * @param editText              Start edit text
     * @param hasCancel             Indicates if theire a cancel button
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String title,
            final String message, final String editText,
            final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        if (optionPaneMessageType == null)
        {
            throw new NullPointerException("optionPaneMessageType mustn't be null");
        }

        if (message == null)
        {
            throw new NullPointerException("message mustn't be null");
        }

        if ((!hasCancel) && (!hasNo))
        {
            throw new IllegalArgumentException(
                    "Question message must have at least 2 buttons, so at least one of hasCancel or hasNo must be true");
        }

        if (optionPaneListener == null)
        {
            throw new NullPointerException(
                    "optionPaneListener mustn't be null (You asked a question, so you should be interest by the answer)");
        }

        if (editText == null)
        {
            throw new NullPointerException("editText mustn't be null");
        }

        this.showOptionPane(title, optionPaneMessageType, message, editText, hasCancel, hasNo, optionPaneListener,
                            actionID, developerInformation);
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param optionPaneMessageType Option pane type
     * @param message               Message to user
     * @param editText              Start edit text
     * @param hasCancel             Indicates if theire a cancel button
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String message,
            final String editText, final boolean hasCancel,
            final boolean hasNo, final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        this.showOptionPaneInput(optionPaneMessageType, null, message, editText, hasCancel, hasNo, optionPaneListener,
                                 actionID, developerInformation);
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param message              Message to user
     * @param hasCancel            Indicates if theire a cancel button
     * @param hasNo                Indicates if theire a no button
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneInput(
            final String message, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        this.showOptionPaneInput(OptionPaneMessageType.MESSAGE, null, message, "", hasCancel, hasNo, optionPaneListener,
                                 actionID, developerInformation);
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param message              Message to user
     * @param editText             Start edit text
     * @param hasCancel            Indicates if theire a cancel button
     * @param hasNo                Indicates if theire a no button
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneInput(
            final String message, final String editText, final boolean hasCancel,
            final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneInput(OptionPaneMessageType.MESSAGE, null, message, editText, hasCancel, hasNo,
                                 optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show dialog input text for user type some text
     *
     * @param title                Dialog title
     * @param message              Message to user
     * @param editText             Start edit text
     * @param hasCancel            Indicates if theire a cancel button
     * @param hasNo                Indicates if theire a no button
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneInput(
            final String title, final String message, final String editText,
            final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneInput(OptionPaneMessageType.MESSAGE, title, message, editText, hasCancel, hasNo,
                                 optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show dialog for show message to user
     *
     * @param optionPaneMessageType Option pane type
     * @param message               Message to show
     */
    public void showOptionPaneMessage(final OptionPaneMessageType optionPaneMessageType, final String message)
    {
        this.showOptionPaneMessage(optionPaneMessageType, null, message, null, 0, null);
    }

    /**
     * Show dialog for show message to user
     *
     * @param optionPaneMessageType Option pane type
     * @param title                 Dialog title
     * @param message               Message to show
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String title,
            final String message,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        if (optionPaneMessageType == null)
        {
            throw new NullPointerException("optionPaneMessageType mustn't be null");
        }

        if (message == null)
        {
            throw new NullPointerException("message mustn't be null");
        }

        this.showOptionPane(title, optionPaneMessageType, message, null, false, false, optionPaneListener, actionID,
                            developerInformation);
    }

    /**
     * Show dialog for show message to user
     *
     * @param optionPaneMessageType Option pane type
     * @param message               Message to show
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String message,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        this.showOptionPaneMessage(optionPaneMessageType, null, message, optionPaneListener, actionID,
                                   developerInformation);
    }

    /**
     * Show dialog for show message to user
     *
     * @param optionPaneMessageType Option pane type
     * @param title                 Dialog title
     * @param message               Message to show
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String title,
            final String message)
    {
        this.showOptionPaneMessage(optionPaneMessageType, null, message, null, 0, null);
    }

    /**
     * Show dialog for show message to user
     *
     * @param message Message to show
     */
    public void showOptionPaneMessage(final String message)
    {
        this.showOptionPaneMessage(OptionPaneMessageType.MESSAGE, null, message, null, 0, null);
    }

    /**
     * Show dialog for show message to user
     *
     * @param message              Message to show
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneMessage(
            final String message, final JHelpOptionPaneListener optionPaneListener,
            final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneMessage(OptionPaneMessageType.MESSAGE, null, message, optionPaneListener, actionID,
                                   developerInformation);
    }

    /**
     * Show a question dialog to user
     *
     * @param optionPaneMessageType Option pane type
     * @param message               Question to user
     * @param hasCancel             Indicates if theire a cancel butonn
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneQuestion(
            final OptionPaneMessageType optionPaneMessageType, final String message,
            final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneQuestion(optionPaneMessageType, null, message, hasCancel, hasNo, optionPaneListener,
                                    actionID,
                                    developerInformation);
    }

    /**
     * Show a question dialog to user
     *
     * @param optionPaneMessageType Option pane type
     * @param title                 Dialog title
     * @param message               Question to user
     * @param hasCancel             Indicates if theire a cancel butonn
     * @param hasNo                 Indicates if theire a no button
     * @param optionPaneListener    Listener to call back when user answers
     * @param actionID              Action ID given back to the listener
     * @param developerInformation  Object given back to the listener
     */
    public void showOptionPaneQuestion(
            final OptionPaneMessageType optionPaneMessageType, final String title,
            final String message, final boolean hasCancel,
            final boolean hasNo, final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        if (optionPaneMessageType == null)
        {
            throw new NullPointerException("optionPaneMessageType mustn't be null");
        }

        if (message == null)
        {
            throw new NullPointerException("message mustn't be null");
        }

        if ((!hasCancel) && (!hasNo))
        {
            throw new IllegalArgumentException(
                    "Question message must have at least 2 buttons, so at least one of hasCancel or hasNo must be true");
        }

        if (optionPaneListener == null)
        {
            throw new NullPointerException(
                    "optionPaneListener mustn't be null (You asked a question, so you should be interest by the answer)");
        }

        this.showOptionPane(title, optionPaneMessageType, message, null, hasCancel, hasNo, optionPaneListener, actionID,
                            developerInformation);
    }

    /**
     * Show a question dialog to user
     *
     * @param message              Question to user
     * @param hasCancel            Indicates if theire a cancel butonn
     * @param hasNo                Indicates if theire a no button
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneQuestion(
            final String message, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        this.showOptionPaneQuestion(OptionPaneMessageType.QUESTION, null, message, hasCancel, hasNo, optionPaneListener,
                                    actionID, developerInformation);
    }

    /**
     * Show a question dialog to user
     *
     * @param title                Dialog title
     * @param message              Question to user
     * @param hasCancel            Indicates if theire a cancel butonn
     * @param hasNo                Indicates if theire a no button
     * @param optionPaneListener   Listener to call back when user answers
     * @param actionID             Action ID given back to the listener
     * @param developerInformation Object given back to the listener
     */
    public void showOptionPaneQuestion(
            final String title, final String message, final boolean hasCancel,
            final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        this.showOptionPaneQuestion(OptionPaneMessageType.QUESTION, title, message, hasCancel, hasNo,
                                    optionPaneListener,
                                    actionID, developerInformation);
    }

    /**
     * Update the frame
     */
    public void update()
    {
        synchronized (this.lock)
        {
            this.updateSize();

            final JHelpImage image = this.getImage();

            this.panel.preferredSize(image.getWidth(), image.getHeight());

            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (image)
            {
                image.startDrawMode();

                image.clear(this.backgroundColor);

                this.panel.paintInternal(0, 0, image);

                image.endDrawMode();
            }

            final int size = this.stackDialog.size();
            if (size > 0)
            {
                this.stackDialog.get(size - 1)
                                .updateImage();
            }

            if (this.automaticRefresh)
            {
                ThreadManager.parallel(this.automaticRefreshTask, this.wait);
                this.wait = 32;
            }
        }
    }
}