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

package jhelp.ia.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import jhelp.util.gui.UtilGUI;

public class FrameClicks extends JFrame
{
    class EventManager implements MouseListener, ActionListener
    {
        EventManager()
        {
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e
         */

        @Override
        public void actionPerformed(final ActionEvent e)
        {
            FrameClicks.this.checkBoxKeys.setEnabled(false);
            FrameClicks.this.buttonStart.setEnabled(false);
            FrameClicks.this.start();
        }

        /**
         * Invoked when the mouse button has been clicked (pressed
         * and released) on a component.
         *
         * @param e
         */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            ClicksManager.stop();
            KeysManager.stop();
            FrameClicks.this.checkBoxKeys.setEnabled(true);
            FrameClicks.this.buttonStart.setEnabled(true);
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         *
         * @param e
         */
        @Override
        public void mousePressed(final MouseEvent e)
        {
        }

        /**
         * Invoked when a mouse button has been released on a component.
         *
         * @param e
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
        }

        /**
         * Invoked when the mouse enters a component.
         *
         * @param e
         */
        @Override
        public void mouseEntered(final MouseEvent e)
        {
        }

        /**
         * Invoked when the mouse exits a component.
         *
         * @param e
         */
        @Override
        public void mouseExited(final MouseEvent e)
        {
        }
    }

    final JButton   buttonStart;
    final JCheckBox checkBoxKeys;

    public FrameClicks()
    {
        super("Clicks");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        final EventManager eventManager = new EventManager();
        final JLabel       labelStop    = new JLabel("Stop", JLabel.CENTER);
        labelStop.setFont(new Font("Arial", Font.BOLD, 42));
        labelStop.addMouseListener(eventManager);
        this.buttonStart = new JButton("Start");
        this.buttonStart.addActionListener(eventManager);
        this.checkBoxKeys = new JCheckBox("Keys");

        this.setLayout(new BorderLayout());
        this.add(labelStop, BorderLayout.CENTER);

        final JPanel panel = new JPanel(new FlowLayout());
        panel.add(this.buttonStart);
        panel.add(this.checkBoxKeys);
        this.add(panel, BorderLayout.SOUTH);

        UtilGUI.packedSize(this);
        UtilGUI.centerOnScreen(this);
    }

    void start()
    {
        Point location = this.getLocation();
        location.x += 256;
        this.setLocation(location);

        ClicksManager.start();

        if (this.checkBoxKeys.isSelected())
        {
            KeysManager.start();
        }
    }
}
