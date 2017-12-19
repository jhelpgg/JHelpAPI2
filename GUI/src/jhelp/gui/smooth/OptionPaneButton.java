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
package jhelp.gui.smooth;

/**
 * Button on option pane
 *
 * @author JHelp
 */
public enum OptionPaneButton
        implements JHelpConstantsSmooth
{
    /**
     * Cancel button
     */
    CANCEL(JHelpConstantsSmooth.TEXT_KEY_CANCEL),
    /**
     * No button
     */
    NO(JHelpConstantsSmooth.TEXT_KEY_NO),
    /**
     * Ok button
     */
    OK(JHelpConstantsSmooth.TEXT_KEY_OK),
    /**
     * Yes button
     */
    YES(JHelpConstantsSmooth.TEXT_KEY_YES);
    /**
     * Button's text key
     */
    private final String keyText;

    /**
     * Create a new instance of OptionPaneButton
     *
     * @param keyText Button's text key
     */
    OptionPaneButton(final String keyText)
    {
        this.keyText = keyText;
    }

    /**
     * Button's text key
     *
     * @return Button's text key
     */
    public String keyText()
    {
        return this.keyText;
    }
}