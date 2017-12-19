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

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jhelp.gui.event.FontChooserEvent;
import jhelp.gui.event.FontChooserListener;
import jhelp.gui.model.FontModel;

/**
 * Component for choose a font.<br>
 * It's the compact version and don't have a link dialog.<br>
 * For having a dialog or have a bigger version see {@link FontChooser}
 */
public class FontChooserCompact
        extends JPanel
        implements ActionListener, ChangeListener
{
    /**
     * Actual font
     */
    private Font              actualFont;
    /**
     * Bold state
     */
    private JCheckBox         checkBoxBold;
    /**
     * Italic state
     */
    private JCheckBox         checkBoxItalic;
    /**
     * Font family name list
     */
    private JComboBox<String> comboBoxFont;
    /**
     * Choose size
     */
    private JSpinner          spinnerSize;

    /**
     * Constructs FontChooserCompact
     */
    public FontChooserCompact()
    {
        this.actualFont = new Font("Arial", Font.PLAIN, 12);

        this.createComponents();
        this.layoutComponents();
        this.addListeners();

        this.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * Add listeners
     */
    private void addListeners()
    {
        this.checkBoxBold.addActionListener(this);
        this.checkBoxItalic.addActionListener(this);

        this.comboBoxFont.addActionListener(this);

        this.spinnerSize.addChangeListener(this);
    }

    /**
     * Create components inside the component
     */
    private void createComponents()
    {
        this.comboBoxFont = new JComboBox<String>(FontModel.obtainFontModel());

        this.checkBoxBold = new JCheckBox("Bold", false);
        this.checkBoxItalic = new JCheckBox("Italic", false);

        this.spinnerSize = new JSpinner(new SpinnerNumberModel(12, 5, 30, 1));
    }

    /**
     * Layout components
     */
    private void layoutComponents()
    {
        this.setLayout(new FlowLayout());

        this.add(this.comboBoxFont);
        this.add(this.checkBoxBold);
        this.add(this.checkBoxItalic);
        this.add(this.spinnerSize);
    }

    /**
     * Remove listeners
     */
    private void removeListeners()
    {
        this.checkBoxBold.removeActionListener(this);
        this.checkBoxItalic.removeActionListener(this);

        this.comboBoxFont.removeActionListener(this);

        this.spinnerSize.removeChangeListener(this);
    }

    /**
     * Update actual font
     */
    private void updateFont()
    {
        final String name = (String) this.comboBoxFont.getSelectedItem();

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

        this.fireFontChanged();
    }

    /**
     * Signal to listeners that font changed
     */
    protected void fireFontChanged()
    {
        FontChooserEvent fontChooserEvent = new FontChooserEvent(this, FontChooserEvent.FONT_CHANGED);

        for (final FontChooserListener fontChooserListener : this.listenerList.getListeners(FontChooserListener.class))
        {
            fontChooserListener.fontChanged(fontChooserEvent);
        }

        fontChooserEvent = null;
    }

    /**
     * Call when a check box change of status
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
     * Change the actual font
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

        this.comboBoxFont.setSelectedItem(this.actualFont.getFamily());

        this.checkBoxBold.setSelected(this.actualFont.isBold());
        this.checkBoxItalic.setSelected(this.actualFont.isItalic());

        this.spinnerSize.setValue(this.actualFont.getSize());

        this.addListeners();

        this.fireFontChanged();
    }

    /**
     * Add font choose listener
     *
     * @param fontChooserListener Listener to add
     */
    public void addFontChooserListener(final FontChooserListener fontChooserListener)
    {
        this.listenerList.add(FontChooserListener.class, fontChooserListener);
    }

    /**
     * Actual font
     *
     * @return Actual font
     */
    public Font getActualFont()
    {
        return this.actualFont;
    }

    /**
     * Remove font choose listener
     *
     * @param fontChooserListener Listener to remove
     */
    public void removeFontChooserListener(final FontChooserListener fontChooserListener)
    {
        this.listenerList.remove(FontChooserListener.class, fontChooserListener);
    }

    /**
     * Call when family choice change
     *
     * @param e Event description
     * @see ChangeListener#stateChanged(ChangeEvent)
     */
    @Override
    public void stateChanged(final ChangeEvent e)
    {
        this.updateFont();
    }
}