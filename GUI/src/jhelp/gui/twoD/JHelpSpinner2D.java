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
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.twoD;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import jhelp.gui.JHelpMouseListener;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Spinner for choose a value
 *
 * @param <VALUE> Value type
 * @author JHelp
 */
public class JHelpSpinner2D<VALUE>
        extends JHelpPanel2D
{
    /**
     * Text font
     */
    private static final JHelpFont FONT    = new JHelpFont("Arial", 14, true);
    /**
     * Compnent ID for "down" the value
     */
    private static final int       ID_LESS = JHelpConstants2D.ID_SPINNER_LESS;
    /**
     * Compnent ID for "up" the value
     */
    private static final int       ID_MORE = JHelpConstants2D.ID_SPINNER_MORE;

    /**
     * Create a spinner for choose an integer
     *
     * @param minimum Minium
     * @param maximum Maximum
     * @return Created spinner
     */
    public static JHelpSpinner2D<Integer> createSpinerInteger(final int minimum, final int maximum)
    {
        return JHelpSpinner2D.createSpinerInteger(minimum, maximum, minimum);
    }

    /**
     * Created spinner for choose an integer
     *
     * @param minimum Minimum
     * @param maximum Maximum
     * @param actual  Start value
     * @return Created spinner
     */
    public static JHelpSpinner2D<Integer> createSpinerInteger(final int minimum, final int maximum, final int actual)
    {
        return JHelpSpinner2D.createSpinerInteger(minimum, maximum, actual, 1);
    }

    /**
     * Create spinner for choose an integer
     *
     * @param minimum Minimum
     * @param maximum Maximum
     * @param actual  Strat value
     * @param step    Stp betwen values
     * @return Created spinner
     */
    public static JHelpSpinner2D<Integer> createSpinerInteger(
            final int minimum, final int maximum, final int actual,
            final int step)
    {
        return new JHelpSpinner2D<Integer>(new JHelpSpinnerModelInteger(minimum, maximum, actual, step));
    }

    /**
     * Event manager of mouse and do some task
     *
     * @author JHelp
     */
    class EventManager
            implements ConsumerTask<Pair<Integer, Integer>>, JHelpMouseListener
    {
        /**
         * Indicates if action task have to be repeat
         */
        private boolean reapeat;

        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Do task for "up" or "down" the value <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Couple of action to do and repeat time
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Pair<Integer, Integer> parameter)
        {
            if ((parameter.second > 0) && (!this.reapeat))
            {
                return;
            }

            switch (parameter.second)
            {
                case JHelpSpinner2D.ID_LESS:
                    if (JHelpSpinner2D.this.spinnerModel.hasPreviousValue())
                    {
                        JHelpSpinner2D.this.spinnerModel.previousValue();
                        JHelpSpinner2D.this.updateValue(true);
                    }
                    break;
                case JHelpSpinner2D.ID_MORE:
                    if (JHelpSpinner2D.this.spinnerModel.hasNextValue())
                    {
                        JHelpSpinner2D.this.spinnerModel.nextValue();
                        JHelpSpinner2D.this.updateValue(true);
                    }
            }

            if ((parameter.second > 0) && (this.reapeat))
            {
                ThreadManager.parallel(this, parameter, parameter.second - (parameter.second / 10));
            }
        }

        /**
         * Called when mouse clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
        }

        /**
         * Called when mouse pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent e)
        {
            this.reapeat = JHelpSpinner2D.this.reapeatDelay > 0;

            final JHelpComponent2D component2d = UtilTwoD.component2DFromMouseEvent(e);
            assert component2d != null;
            ThreadManager.parallel(this,
                                   new Pair<Integer, Integer>(component2d.id(), JHelpSpinner2D.this.reapeatDelay));
        }

        /**
         * Called when mouse released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
            this.reapeat = false;
        }

        /**
         * Called when mouse entered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent e)
        {
        }

        /**
         * Called when mouse exited <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent e)
        {
        }

        /**
         * Called when mouse dragged <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(final MouseEvent e)
        {
        }

        /**
         * Called when mouse moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(final MouseEvent e)
        {
        }

        /**
         * Called when mouse whell moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Mouse event
         * @see java.awt.event.MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
         */
        @Override
        public void mouseWheelMoved(final MouseWheelEvent e)
        {
        }
    }

    /**
     * Event manager
     */
    private final EventManager                  eventManager;
    /**
     * Listner of spinner changes
     */
    private       JHelpSpinner2DListener<VALUE> spinner2dListener;
    /**
     * Text cell wwidth
     */
    private final int                           textCellWidth;
    /**
     * Label shows the text
     */
    private final JHelpLabelImage2D             valueText;
    /**
     * Synchronization lock
     */
    final Object LOCK = new Object();
    /**
     * Deay between repetition
     */
    int reapeatDelay;
    /**
     * Spin,ner model
     */
    final JHelpSpinnerModel<VALUE> spinnerModel;

    /**
     * Create a new instance of JHelpSpinner2D
     *
     * @param spinnerModel Spinner model
     */
    public JHelpSpinner2D(final JHelpSpinnerModel<VALUE> spinnerModel)
    {
        super(new JHelpTableLayout());

        if (spinnerModel == null)
        {
            throw new NullPointerException("spinnerModel mustn't be null");
        }

        this.eventManager = new EventManager();
        this.reapeatDelay = 0;

        this.spinnerModel = spinnerModel;
        Pair<List<JHelpTextLineAlpha>, Dimension> linesInformation = JHelpSpinner2D.FONT.computeTextLinesAlpha(
                spinnerModel.biggestText(), JHelpTextAlign.CENTER);

        final int imageWidth  = linesInformation.second.width + 2;
        int       imageHeight = linesInformation.second.height + 2;

        linesInformation = JHelpSpinner2D.FONT.computeTextLinesAlpha("+", JHelpTextAlign.CENTER);
        this.textCellWidth = Math.max(1, imageWidth / linesInformation.second.width);
        imageHeight = Math.max(imageHeight, linesInformation.second.height << 1);

        this.valueText = new JHelpLabelImage2D(new JHelpImage(imageWidth, imageHeight));
        this.addComponent2D(this.valueText,
                            new JHelpTableLayout.JHelpTableLayoutConstraints(0, 0, this.textCellWidth, 2));

        JHelpLabelImage2D label = JHelpLabelImage2D.createTextLabel("+", JHelpSpinner2D.FONT, 0xFFFFFFFF, 0xFF000000,
                                                                    JHelpTextAlign.CENTER);
        label.id(JHelpSpinner2D.ID_MORE);
        label.mouseListener(this.eventManager);
        this.addComponent2D(label, new JHelpTableLayout.JHelpTableLayoutConstraints(this.textCellWidth, 0));

        label = JHelpLabelImage2D.createTextLabel("-", JHelpSpinner2D.FONT, 0xFFFFFFFF, 0xFF000000,
                                                  JHelpTextAlign.CENTER);
        label.id(JHelpSpinner2D.ID_LESS);
        label.mouseListener(this.eventManager);
        this.addComponent2D(label, new JHelpTableLayout.JHelpTableLayoutConstraints(this.textCellWidth, 1));

        this.updateValue(true);
    }

    /**
     * Update spinner component with last changes
     *
     * @param fireEvent Indicates if fire to listener that value changed
     */
    void updateValue(final boolean fireEvent)
    {
        final String text = this.spinnerModel.actualText();

        final JHelpImage image = this.valueText.image();

        final int width  = image.getWidth();
        final int height = image.getHeight();

        final Pair<List<JHelpTextLineAlpha>, Dimension> linesInformation = JHelpSpinner2D.FONT.computeTextLinesAlpha(
                text,
                JHelpTextAlign.CENTER,
                width);

        synchronized (this.LOCK)
        {
            image.startDrawMode();

            image.clear(0xFFFFFFFF);

            final int x = (width - linesInformation.second.width) >> 1;
            final int y = (height - linesInformation.second.height) >> 1;

            for (final JHelpTextLineAlpha textLine : linesInformation.first)
            {
                image.paintAlphaMask(x + textLine.getX(), y + textLine.getY(), textLine.getMask(), 0xFF000000,
                                     0xFFFFFFFF,
                                     false);
            }

            image.endDrawMode();
        }

        if ((fireEvent) && (this.spinner2dListener != null))
        {
            this.spinner2dListener.spinnerValueChanged(this, this.spinnerModel.actualValue());
        }
    }

    /**
     * Called when component bounds changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param bounds New bounds
     * @see jhelp.gui.twoD.JHelpComponent2D#onBoundsChanged(Rectangle)
     */
    @Override
    protected void onBoundsChanged(final Rectangle bounds)
    {
        super.onBoundsChanged(bounds);

        this.updateValue(false);
    }

    /**
     * Actual value
     *
     * @return Actual value
     */
    public VALUE getActualValue()
    {
        return this.spinnerModel.actualValue();
    }

    /**
     * Repetition delay in millisecond
     *
     * @return Repetition delay in millisecond
     */
    public int getReapeatDelay()
    {
        return this.reapeatDelay;
    }

    /**
     * Change repetition delay (0 for no repetition)
     *
     * @param reapeatDelay Repetitin delay in millisecond
     */
    public void setReapeatDelay(final int reapeatDelay)
    {
        this.reapeatDelay = Math.max(0, reapeatDelay);
    }

    /**
     * Spinner changes listener
     *
     * @return Spinner changes listener
     */
    public JHelpSpinner2DListener<VALUE> getSpinner2dListener()
    {
        return this.spinner2dListener;
    }

    /**
     * Changes/defines spinner listener
     *
     * @param spinner2dListener New spinner listener, {@code null} for no lisener
     */
    public void setSpinner2dListener(final JHelpSpinner2DListener<VALUE> spinner2dListener)
    {
        this.spinner2dListener = spinner2dListener;
    }

    /**
     * Draw the panel <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X location in parent
     * @param y      Y location in parent
     * @param parent Parent where draw
     * @see jhelp.gui.twoD.JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    public void paint(final int x, final int y, final JHelpImage parent)
    {
        this.bounds();
        Rectangle bounds;

        synchronized (this.children)
        {
            for (final Pair<JHelpComponent2D, JHelpConstraints> child : this.children)
            {
                if (!child.first.visible())
                {
                    continue;
                }

                bounds = child.first.bounds();
                bounds.x += x;
                bounds.y += y;

                child.first.paintInternal(bounds.x, bounds.y, parent);
            }
        }
    }

    /**
     * Change current value
     *
     * @param value          New value
     * @param signalListener Indicates if this changes must be fire to listener
     */
    public void setValue(final VALUE value, final boolean signalListener)
    {
        this.spinnerModel.actualValue(value);

        this.updateValue(signalListener);
    }
}