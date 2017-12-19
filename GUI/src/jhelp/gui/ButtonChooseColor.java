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
package jhelp.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.border.Border;
import jhelp.gui.event.ColorChooserListener;

/**
 * Button for choose a color
 */
public class ButtonChooseColor
        extends JComponent
        implements MouseListener, ColorChooserListener
{
    /**
     * Actual color
     */
    private Color  color;
    /**
     * Action command
     */
    private String command;

    /**
     * Constructs ButtonChooseColor
     */
    public ButtonChooseColor()
    {
        this(Color.BLACK, 50);
    }

    /**
     * Constructs ButtonChooseColor
     *
     * @param color Start color
     */
    public ButtonChooseColor(final Color color)
    {
        this(color, 50);
    }

    /**
     * Constructs ButtonChooseColor
     *
     * @param color Start color
     * @param size  Button size
     */
    public ButtonChooseColor(final Color color, int size)
    {
        if (color == null)
        {
            throw new NullPointerException("color mustn't be null");
        }

        this.color = color;

        if (size < 25)
        {
            size = 25;
        }

        final Dimension dimension = new Dimension(size, size);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);

        this.addMouseListener(this);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Constructs ButtonChooseColor
     *
     * @param size Button size
     */
    public ButtonChooseColor(final int size)
    {
        this(Color.BLACK, size);
    }

    /**
     * Signal to listeners that color change
     */
    protected void fireActionPerformed()
    {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.command);

        for (final ActionListener actionListener : this.listenerList.getListeners(ActionListener.class))
        {
            actionListener.actionPerformed(actionEvent);
        }

        actionEvent = null;
    }

    /**
     * Paint the button
     *
     * @param g Graphics context
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        if (this.color != null)
        {
            int x      = 0;
            int y      = 0;
            int width  = this.getWidth();
            int height = this.getHeight();

            final Border border = this.getBorder();
            if (border != null)
            {
                final Insets insets = border.getBorderInsets(this);
                x += insets.left;
                y += insets.top;
                width -= insets.right + insets.left;
                height -= insets.bottom + insets.top;
            }

            g.setColor(this.color);
            g.fillRect(x, y, width, height);
        }
    }

    /**
     * Add listener of change color
     *
     * @param actionListener Listener to add
     */
    public void addActionListener(final ActionListener actionListener)
    {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    /**
     * Return color
     *
     * @return color
     */
    public Color color()
    {
        return this.color;
    }

    /**
     * Modify color
     *
     * @param color New color value
     */
    public void color(final Color color)
    {
        if (color == null)
        {
            throw new NullPointerException("color mustn't be null");
        }

        if (color.equals(this.color))
        {
            return;
        }

        this.color = color;
        this.repaint();

        this.fireActionPerformed();
    }

    /**
     * Called when color chooser closed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param colorChooser Color chooser source
     * @param color        Chosen color
     * @see ColorChooserListener#colorChoose(ColorChooser, int)
     */
    @Override
    public void colorChoose(final ColorChooser colorChooser, final int color)
    {
        this.color = new Color(color, true);
        this.repaint();

        this.fireActionPerformed();

    }

    /**
     * Called when choose color canceled <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param colorChooser Color chooser source
     * @see ColorChooserListener#colorChooseCanceled(ColorChooser)
     */
    @Override
    public void colorChooseCanceled(final ColorChooser colorChooser)
    {
    }

    /**
     * Return command
     *
     * @return command
     */
    public String command()
    {
        return this.command;
    }

    /**
     * Modify command
     *
     * @param command New command value
     */
    public void command(final String command)
    {
        this.command = command;
    }

    /**
     * Action on clicking on the button
     *
     * @param e Event description
     * @see MouseListener#mouseClicked(MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e)
    {
        ColorChooserFrame.COLOR_CHOOSER_FRAME.chooseColor(this.color.getRGB(), this);
    }

    /**
     * Action on mouse pressed
     *
     * @param e Event description
     * @see MouseListener#mousePressed(MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
    }

    /**
     * Action on mouse released
     *
     * @param e Event description
     * @see MouseListener#mouseReleased(MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {
    }

    /**
     * Action on mouse enter
     *
     * @param e Event description
     * @see MouseListener#mouseEntered(MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {
    }

    /**
     * Action on mouse exited
     *
     * @param e Event description
     * @see MouseListener#mouseExited(MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
    }

    /**
     * Remove listener
     *
     * @param actionListener Listener to remove
     */
    public void removeActionListener(final ActionListener actionListener)
    {
        this.listenerList.remove(ActionListener.class, actionListener);
    }
}