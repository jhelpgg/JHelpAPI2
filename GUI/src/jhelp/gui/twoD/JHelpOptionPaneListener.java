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

/**
 * Listener of option pane user response.<br>
 * If the user click on yes or ok when the dialog is for type some text, then it
 * {@link #optionPaneTextTyped(int, Object, String)} called
 *
 * @author JHelp
 */
public interface JHelpOptionPaneListener
{
    /**
     * Called when user click on cancel button
     *
     * @param actionID             Action ID given when option pane was shown
     * @param developerInformation Object given when option pane was shown
     */
    void optionPaneCancel(int actionID, Object developerInformation);

    /**
     * Called when user click on no button
     *
     * @param actionID             Action ID given when option pane was shown
     * @param developerInformation Object given when option pane was shown
     */
    void optionPaneNo(int actionID, Object developerInformation);

    /**
     * Called when user click on yes or ok button and theire input text
     *
     * @param actionID             Action ID given when option pane was shown
     * @param developerInformation Object given when option pane was shown
     * @param text                 Typed text
     */
    void optionPaneTextTyped(int actionID, Object developerInformation, String text);

    /**
     * Called when user click on tes or ok button and theire are no input text
     *
     * @param actionID             Action ID given when option pane was shown
     * @param developerInformation Object given when option pane was shown
     */
    void optionPaneYesOk(int actionID, Object developerInformation);
}