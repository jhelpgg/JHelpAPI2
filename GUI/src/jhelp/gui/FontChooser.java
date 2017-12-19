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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jhelp.gui.event.FontChooserEvent;
import jhelp.gui.event.FontChooserListener;
import jhelp.gui.layout.VerticalLayout;
import jhelp.gui.model.FontModel;
import jhelp.util.gui.UtilGUI;

/**
 * Font chooser.<br>
 * It link to a dialog to be able to popup stand alone<br>
 * For a compact version see {@link FontChooserCompact}
 */
public class FontChooser
        extends JPanel
        implements ListSelectionListener, ActionListener, ChangeListener
{
    /**
     * Dialog to show the choose of font
     */
    public static class FontChooserDialog
            extends JDialog
            implements ActionListener, FontChooserListener
    {
        /**
         * FontChooserSample.java [long] : TODO Explain what represents the filed FontChooserSample.java in jhelp.gui [JHelpGUI]
         */
        private static final long serialVersionUID = -4915503511936091332L;
        /**
         * Cancel button
         */
        private JButton     buttonCancel;
        /**
         * OK button
         */
        private JButton     buttonOk;
        /**
         * Linked font chooser component
         */
        private FontChooser fontChooser;
        /**
         * Indicates if the last button is Ok or not
         */
        private boolean     validate;

        /**
         * Constructs FontChooserDialog
         *
         * @param parent Dialog parent
         */
        FontChooserDialog(final Dialog parent)
        {
            super(parent, "Font chooser", true);

            this.createDialog();
        }

        /**
         * Constructs FontChooserDialog
         *
         * @param parent Frame parent
         */
        FontChooserDialog(final Frame parent)
        {
            super(parent, "Font chooser", true);

            this.createDialog();
        }

        /**
         * Add listeners
         */
        private void addListeners()
        {
            this.buttonOk.addActionListener(this);
            this.buttonCancel.addActionListener(this);
        }

        /**
         * Create inside components
         */
        private void createComponents()
        {
            this.fontChooser = new FontChooser();
            this.buttonOk = new JButton("Ok");
            this.buttonCancel = new JButton("Cancel");
        }

        /**
         * Create the dialog
         */
        private void createDialog()
        {
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

            this.createComponents();
            this.layoutComponents();
            this.addListeners();

            this.pack();

            final Dimension dimension = this.getSize();
            final Rectangle bounds    = UtilGUI.getScreenBounds(0);

            dimension.width = Math.min(dimension.width, bounds.width);
            dimension.height = Math.min(dimension.height, bounds.height);

            this.setLocation((bounds.width - dimension.width) >> (1 + bounds.x),
                             (bounds.height - dimension.height) >> (1 + bounds.y));
            this.setSize(dimension);
        }

        /**
         * Exit the dialog
         *
         * @param validate Indicates if it's the OK button
         */
        private void exitDialog(final boolean validate)
        {
            this.validate = validate;
            this.setVisible(false);
        }

        /**
         * Layout components
         */
        private void layoutComponents()
        {
            this.setLayout(new BorderLayout());

            this.add(this.fontChooser, BorderLayout.CENTER);

            final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.add(this.buttonOk);
            panel.add(this.buttonCancel);
            this.add(panel, BorderLayout.SOUTH);
        }

        /**
         * Call on widow event
         *
         * @param e Event description
         * @see JDialog#processWindowEvent(WindowEvent)
         */
        @Override
        protected void processWindowEvent(final WindowEvent e)
        {
            switch (e.getID())
            {
                case WindowEvent.WINDOW_CLOSED:
                case WindowEvent.WINDOW_CLOSING:
                    this.exitDialog(false);
                    break;
            }
        }

        /**
         * Call when a button is click
         *
         * @param actionEvent Event description
         * @see ActionListener#actionPerformed(ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent actionEvent)
        {
            this.exitDialog(actionEvent.getSource() == this.buttonOk);
        }

        /**
         * Call when font changed
         *
         * @param fontChooserEvent Event description
         * @see FontChooserListener#fontChanged(FontChooserEvent)
         */
        @Override
        public void fontChanged(final FontChooserEvent fontChooserEvent)
        {
            this.invalidate();
            this.validate();
            this.doLayout();
            this.repaint();

            this.fontChooser.listFont.scrollRectToVisible(
                    this.fontChooser.listFont.getCellBounds(this.fontChooser.listFont.getSelectedIndex(),
                                                            this.fontChooser.listFont.getSelectedIndex()));
        }

        /**
         * Show the dialog and wit user choice
         *
         * @param start Font to start with. You can use {@code null} if you want use default font to start. Default is Arial, plain,
         *              12 OR last choose
         * @return Choose font or {@code null} if no choice
         */
        public Font showDialogFont(final Font start)
        {
            if (start != null)
            {
                this.fontChooser.actualFont(start);
            }

            this.fontChooser.addFontChooserListener(this);

            this.setVisible(true);

            this.fontChooser.removeFontChooserListener(this);

            if (this.validate)
            {
                return this.fontChooser.actualFont;
            }

            return null;
        }
    }

    /**
     * Preview text
     */
    private static final String PREVIEW = "0123456789\nabcedfghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ\n,;.#:!/*-+&([{<?>}])@";

    /**
     * FontChooserSample.java [long] : TODO Explain what represents the filed FontChooserSample.java in jhelp.gui [JHelpGUI]
     */
    private static final long serialVersionUID = 8124184522278375118L;

    /**
     * Create a dialog for choose a font
     *
     * @param component Component over the dialog have to appear
     * @return Created dialog
     */
    public static FontChooserDialog createFontChooserDialog(Component component)
    {
        Frame  frame  = null;
        Dialog dialog = null;

        while ((component != null) && (frame == null) && (dialog == null))
        {
            if ((component instanceof Frame))
            {
                frame = (Frame) component;
            }
            else if ((component instanceof Dialog))
            {
                dialog = (Dialog) component;
            }
            else
            {
                component = component.getParent();
            }
        }

        if (dialog != null)
        {
            return new FontChooserDialog(dialog);
        }

        return new FontChooserDialog(frame);
    }

    /**
     * Show a dialog for choose the font and wait the user choice
     *
     * @param component Component over the dialog have to appear
     * @param start     Font to in the dialog at start
     * @return The select font or {@code null} if no selection
     */
    public static Font showFontChooserDialog(final Component component, final Font start)
    {
        return FontChooser.createFontChooserDialog(component).showDialogFont(start);
    }

    /**
     * Bold state
     */
    private JCheckBox checkBoxBold;
    /**
     * Italic state
     */
    private JCheckBox checkBoxItalic;
    /**
     * Size choice
     */
    private JSpinner  spinnerSize;
    /**
     * preview
     */
    private JTextArea textAreaPreview;
    /**
     * Actual font
     */
    Font          actualFont;
    /**
     * List to show families name
     */
    JList<String> listFont;

    /**
     * Constructs FontChooserSample
     */
    public FontChooser()
    {
        this.actualFont = new Font("Arial", Font.PLAIN, 12);

        this.createComponents();
        this.layoutComponents();
        this.addListeners();
    }

    /**
     * Add listeners
     */
    private void addListeners()
    {
        this.checkBoxBold.addActionListener(this);
        this.checkBoxItalic.addActionListener(this);

        this.listFont.addListSelectionListener(this);

        this.spinnerSize.addChangeListener(this);
    }

    /**
     * Create components
     */
    private void createComponents()
    {
        this.listFont = new JList<String>(FontModel.obtainFontModel());
        this.listFont.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.checkBoxBold = new JCheckBox("Bold", false);
        this.checkBoxItalic = new JCheckBox("Italic", false);

        this.spinnerSize = new JSpinner(new SpinnerNumberModel(12, 5, 30, 1));

        this.textAreaPreview = new JTextArea(FontChooser.PREVIEW);
        this.textAreaPreview.setEditable(false);
    }

    /**
     * Layout components
     */
    private void layoutComponents()
    {
        this.setLayout(new BorderLayout());

        // Center
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(this.listFont, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                  BorderLayout.CENTER);

        final JPanel p          = new JPanel(new VerticalLayout(VerticalLayout.EXTENDS_WIDTH));
        final JPanel panelStyle = new JPanel(new BorderLayout());
        panelStyle.add(this.checkBoxBold, BorderLayout.NORTH);
        panelStyle.add(this.checkBoxItalic, BorderLayout.SOUTH);

        p.add(panelStyle);
        p.add(this.spinnerSize);
        panel.add(p, BorderLayout.EAST);

        this.add(panel, BorderLayout.CENTER);

        // South
        this.add(new JScrollPane(this.textAreaPreview, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                 BorderLayout.SOUTH);
    }

    /**
     * Remove listeners
     */
    private void removeListeners()
    {
        this.checkBoxBold.removeActionListener(this);
        this.checkBoxItalic.removeActionListener(this);

        this.listFont.removeListSelectionListener(this);

        this.spinnerSize.removeChangeListener(this);
    }

    /**
     * Update the font
     */
    private void updateFont()
    {
        final String name = this.listFont.getSelectedValue();

        int style = Font.PLAIN;

        if (this.checkBoxBold.isSelected())
        {
            style |= Font.BOLD;
        }

        if (this.checkBoxItalic.isSelected())
        {
            style |= Font.ITALIC;
        }

        final int size = ((Integer) this.spinnerSize.getValue()).intValue();

        if ((size == this.actualFont.getSize()) && (style == this.actualFont.getStyle()) && (this.actualFont.getFamily()
                                                                                                            .equals(name)))
        {
            return;
        }

        this.actualFont = new Font(name, style, size);

        this.textAreaPreview.setFont(this.actualFont);

        this.fireFontChanged();
    }

    /**
     * Signal to listeners that font changed
     */
    protected void fireFontChanged()
    {
        this.invalidate();
        this.validate();
        this.doLayout();
        this.repaint();

        this.listFont.scrollRectToVisible(
                this.listFont.getCellBounds(this.listFont.getSelectedIndex(), this.listFont.getSelectedIndex()));

        FontChooserEvent fontChooserEvent = new FontChooserEvent(this, FontChooserEvent.FONT_CHANGED);

        for (final FontChooserListener fontChooserListener : this.listenerList.getListeners(FontChooserListener.class))
        {
            fontChooserListener.fontChanged(fontChooserEvent);
        }

        fontChooserEvent = null;
    }

    /**
     * Actual font
     *
     * @return Actual font
     */
    public Font actualFont()
    {
        return this.actualFont;
    }

    /**
     * Change the font
     *
     * @param font New font
     */
    public void actualFont(final Font font)
    {
        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        if (font.equals(this.actualFont))
        {
            return;
        }

        this.removeListeners();

        this.actualFont = font;

        this.listFont.setSelectedValue(this.actualFont.getFamily(), true);

        this.checkBoxBold.setSelected(this.actualFont.isBold());
        this.checkBoxItalic.setSelected(this.actualFont.isItalic());

        this.spinnerSize.setValue(this.actualFont.getSize());

        this.addListeners();

        this.textAreaPreview.setFont(this.actualFont);

        this.fireFontChanged();
    }

    /**
     * Call when check bold or italic change
     *
     * @param e Event description
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        this.updateFont();
    }

    /**
     * Add font chooser listener
     *
     * @param fontChooserListener Listener to add
     */
    public void addFontChooserListener(final FontChooserListener fontChooserListener)
    {
        this.listenerList.add(FontChooserListener.class, fontChooserListener);
    }

    /**
     * Remove font chooser listener
     *
     * @param fontChooserListener Listener to remove
     */
    public void removeFontChooserListener(final FontChooserListener fontChooserListener)
    {
        this.listenerList.remove(FontChooserListener.class, fontChooserListener);
    }

    /**
     * Call when size change
     *
     * @param e Event description
     * @see ChangeListener#stateChanged(ChangeEvent)
     */
    @Override
    public void stateChanged(final ChangeEvent e)
    {
        this.updateFont();
    }

    /**
     * Call when a family choose
     *
     * @param e Event description
     * @see ListSelectionListener#valueChanged(ListSelectionEvent)
     */
    @Override
    public void valueChanged(final ListSelectionEvent e)
    {
        this.updateFont();
    }


}