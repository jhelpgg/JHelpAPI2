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
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.twoD;

/**
 * Listener of spinner changes
 *
 * @author JHelp
 * @param <VALUE>
 *           Value type
 */
public interface JHelpSpinner2DListener<VALUE>
{
    /**
     * Called when spinner value changed
     *
     * @param spinner2d
     *           Spinner that value chaged
     * @param newValue
     *           New value
     */
    void spinnerValueChanged(JHelpSpinner2D<VALUE> spinner2d, VALUE newValue);
}