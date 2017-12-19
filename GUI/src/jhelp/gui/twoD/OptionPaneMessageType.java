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
import jhelp.util.gui.JHelpImage;

/**
 * Option message type
 *
 * @author JHelp
 */
public enum OptionPaneMessageType
{
    /**
     * Error message type
     */
    ERROR("error.png"),
    /**
     * Information message type
     */
    INFORMATION("information.png"),
    /**
     * Message neutral type
     */
    MESSAGE(null),
    /**
     * Question message type
     */
    QUESTION("question.png"),
    /**
     * Warning message type
     */
    WARNING("warning.png");
    /**
     * Associted icon
     */
    private       JHelpImage image;
    /**
     * Resource where get the icon
     */
    private final String     resource;

    /**
     * Create a new instance of OptionPaneMessageType
     *
     * @param resource Resource where get the icon
     */
    OptionPaneMessageType(final String resource)
    {
        this.resource = resource;
    }

    /**
     * Obtain associated icon
     *
     * @return Associated icon
     */
    public JHelpImage image()
    {
        if (this.resource == null)
        {
            return null;
        }

        if (this.image == null)
        {
            try
            {
                this.image = ResourcesGUI.RESOURCES.obtainJHelpImage(this.resource);
            }
            catch (final Exception exception)
            {
                this.image = JHelpImage.DUMMY;
            }
        }

        return this.image;
    }
}