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

package jhelp.util.samples.math.maya;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.oldSystem.MayaNumber;
import jhelp.util.samples.common.gui.SampleLabelJHelpImageCenter;

public class SampleMayaFrame
        extends JFrame
        implements ActionListener
{
    private final JLabel                      label;
    private final SampleLabelJHelpImageCenter sampleLabelJHelpImageCenter;
    private final JTextField                  textField;

    public SampleMayaFrame()
            throws HeadlessException
    {
        super("Maya");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.label = new JLabel("", SwingConstants.CENTER);
        this.label.setFont(MayaNumber.FONT_MAYA.getFont());
        this.sampleLabelJHelpImageCenter = new SampleLabelJHelpImageCenter();
        this.textField = new JTextField(128);
        this.textField.setFont(MayaNumber.FONT_NUMBERS.getFont());
        this.textField.addActionListener(this);

        this.setLayout(new BorderLayout());
        this.add(this.label, BorderLayout.NORTH);
        this.add(new JScrollPane(this.sampleLabelJHelpImageCenter), BorderLayout.CENTER);
        this.add(this.textField, BorderLayout.SOUTH);

        UtilGUI.takeAllScreen(this);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        final String text = this.textField.getText().trim();
        this.textField.setText("");

        try
        {
            final MayaNumber mayaNumber = new MayaNumber(Long.parseLong(text));
            this.sampleLabelJHelpImageCenter.setJHelpImage(mayaNumber.toTotemImage(128, true));
            this.label.setText(mayaNumber.toString());
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Bad number : ", text);
            this.label.setText("ERROR : Bad number : " + text);
        }
    }
}