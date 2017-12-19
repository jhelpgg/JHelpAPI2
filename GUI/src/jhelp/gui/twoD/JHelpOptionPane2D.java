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

import jhelp.gui.ResourcesGUI;
import jhelp.gui.twoD.JHelpBorderLayout.JHelpBorderLayoutConstraints;
import jhelp.gui.twoD.JHelpVerticalLayout.JHelpVerticalLayoutConstraints;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;

/**
 * Option pane for show message to user, ask question, or ask some input text
 *
 * @author JHelp
 */
class JHelpOptionPane2D
        extends JHelpPanel2D
        implements JHelpActionListener
{
    /**
     * Cancel button ID
     */
    private static final int CANCEL = 1;
    /**
     * No button ID
     */
    private static final int NO     = 2;
    /**
     * Ok/Yes button ID
     */
    private static final int OK_YES = 3;
    /**
     * Developer action ID, to get back to listener
     */
    private       int                     actionID;
    /**
     * Developer object to get back to listener
     */
    private       Object                  developerInformation;
    /**
     * Edit text area
     */
    private final JHelpEditText           editText;
    /**
     * Dialog icon
     */
    private final JHelpLabelImage2D       labelImageIcon;
    /**
     * Cancel button
     */
    private final JHelpLabelText2D        labelTextButtonCancel;
    /**
     * No button
     */
    private final JHelpLabelText2D        labelTextButtonNo;
    /**
     * Ok/Yes button
     */
    private final JHelpLabelText2D        labelTextButtonOkYes;
    /**
     * Dialog message
     */
    private final JHelpLabelText2D        labelTextMessage;
    /**
     * Dialog title
     */
    private final JHelpLabelText2D        labelTextTitle;
    /**
     * Listener of option pane
     */
    private       JHelpOptionPaneListener optionPaneListener;

    /**
     * Create a new instance of JHelpOptionPane2D
     */
    JHelpOptionPane2D()
    {
        super(new JHelpBorderLayout());

        this.labelTextTitle = new JHelpLabelText2D("", JHelpTextAlign.CENTER);
        this.labelTextMessage = new JHelpLabelText2D();
        this.editText = new JHelpEditText(JHelpFont.DEFAULT, 0xFF000000, 32, 2);
        this.labelImageIcon = new JHelpLabelImage2D(JHelpImage.DUMMY);
        this.labelTextButtonOkYes = new JHelpLabelText2D("Yes", JHelpTextAlign.CENTER, 0xFF000000, 0);
        this.labelTextButtonCancel = new JHelpLabelText2D("Cancel", JHelpTextAlign.CENTER, 0xFF000000, 0);
        this.labelTextButtonNo = new JHelpLabelText2D("No", JHelpTextAlign.CENTER, 0xFF000000, 0);

        this.addComponent2D(this.labelTextTitle, JHelpBorderLayoutConstraints.TOP);
        this.addComponent2D(this.labelImageIcon, JHelpBorderLayoutConstraints.TOP_LEFT);

        JHelpPanel2D panel = new JHelpPanel2D(new JHelpVerticalLayout());
        panel.addComponent2D(this.labelTextMessage, JHelpVerticalLayoutConstraints.EXPANDED);
        panel.addComponent2D(this.editText, JHelpVerticalLayoutConstraints.EXPANDED);
        this.addComponent2D(panel, JHelpBorderLayoutConstraints.CENTER);

        panel = new JHelpPanel2D(new JHelpTableLayout());
        panel.addComponent2D(new JHelpBackgroundSaussage(this.labelTextButtonOkYes, 0xFFDDFFDD),
                             new JHelpTableLayout.JHelpTableLayoutConstraints(0, 0, 2, 1));
        panel.addComponent2D(new JHelpBackgroundSaussage(this.labelTextButtonCancel, 0xFFDDDDFF),
                             new JHelpTableLayout.JHelpTableLayoutConstraints(3, 0, 2, 1));
        panel.addComponent2D(new JHelpBackgroundSaussage(this.labelTextButtonNo, 0xFFFFDDDD),
                             new JHelpTableLayout.JHelpTableLayoutConstraints(6, 0, 2, 1));
        this.addComponent2D(panel, JHelpBorderLayoutConstraints.BOTTOM_EXPANDED);

        JHelpButtonBehavior.giveButtonBehavior(JHelpOptionPane2D.OK_YES, this.labelTextButtonOkYes, this);
        JHelpButtonBehavior.giveButtonBehavior(JHelpOptionPane2D.CANCEL, this.labelTextButtonCancel, this);
        JHelpButtonBehavior.giveButtonBehavior(JHelpOptionPane2D.NO, this.labelTextButtonNo, this);
    }

    /**
     * Called when a button is clicked <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param component2d Button clicked
     * @param identifier  Action ID
     * @see jhelp.gui.twoD.JHelpActionListener#actionAppend(JHelpComponent2D, int)
     */
    @Override
    public void actionAppend(final JHelpComponent2D component2d, final int identifier)
    {
        this.dialogParent().visible(false);

        if (this.optionPaneListener != null)
        {
            switch (identifier)
            {
                case JHelpOptionPane2D.OK_YES:
                    if (this.editText.visible())
                    {
                        this.optionPaneListener.optionPaneTextTyped(this.actionID, this.developerInformation,
                                                                    this.editText.text());
                    }
                    else
                    {
                        this.optionPaneListener.optionPaneYesOk(this.actionID, this.developerInformation);
                    }
                    break;
                case JHelpOptionPane2D.CANCEL:
                    this.optionPaneListener.optionPaneCancel(this.actionID, this.developerInformation);
                    break;
                case JHelpOptionPane2D.NO:
                    this.optionPaneListener.optionPaneNo(this.actionID, this.developerInformation);
                    break;
            }
        }
    }

    /**
     * Dialog parent owner
     *
     * @return Dialog parent owner
     */
    public JHelpDialog2D dialogParent()
    {
        return (JHelpDialog2D) this.obtainOwner();
    }

    /**
     * Show the option pane
     *
     * @param title                 Title
     * @param optionPaneMessageType Message type
     * @param message               Message
     * @param editText              Start edit text
     * @param hasCancel             Indicates if have cancel button
     * @param hasNo                 Indicates if have no button
     * @param optionPaneListener    Listener to callback
     * @param actionID              Action ID to give back to listener
     * @param developerInformation  Object to give back to listener
     */
    public void showOptionPane(
            final String title, final OptionPaneMessageType optionPaneMessageType, final String message,
            final String editText,
            final boolean hasCancel, final boolean hasNo, final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        this.labelTextTitle.visible(title != null);
        if (title != null)
        {
            this.labelTextTitle.text(title);
        }

        JHelpImage icon = null;
        if (optionPaneMessageType != null)
        {
            icon = optionPaneMessageType.image();
        }
        this.labelImageIcon.visible(icon != null);
        if (icon != null)
        {
            this.labelImageIcon.image(icon);
        }

        this.labelTextMessage.visible(message != null);
        if (message != null)
        {
            this.labelTextMessage.text(message);
        }

        this.editText.visible(editText != null);
        if (editText != null)
        {
            this.editText.text(editText);
        }

        if (hasNo)
        {
            this.labelTextButtonOkYes.text(ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_YES));
        }
        else
        {
            this.labelTextButtonOkYes.text(ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_OK));
        }

        this.labelTextButtonCancel.visible(hasCancel);
        this.labelTextButtonCancel.parent().visible(hasCancel);
        this.labelTextButtonCancel.text(ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_CANCEL));

        this.labelTextButtonNo.visible(hasNo);
        this.labelTextButtonNo.parent().visible(hasNo);
        this.labelTextButtonNo.text(ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_NO));

        this.optionPaneListener = optionPaneListener;
        this.actionID = actionID;
        this.developerInformation = developerInformation;

        this.dialogParent().updateImage();
        this.dialogParent().visible(true);
    }
}