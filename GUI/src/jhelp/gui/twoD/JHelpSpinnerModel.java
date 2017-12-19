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
 * Spinner model
 *
 * @param <VALUE> Value type
 * @author JHelp
 */
public interface JHelpSpinnerModel<VALUE>
{
    /**
     * Current value text representation
     *
     * @return Current value text representation
     */
    String actualText();

    /**
     * Current value
     *
     * @return Current value
     */
    VALUE actualValue();

    /**
     * Change current value.<br>
     * Implementation choice to do things if value is not valid
     *
     * @param value New value
     */
    void actualValue(VALUE value);

    /**
     * Obtain the biggest text that spinner can provide, to be able reserve enough room
     *
     * @return Biggest text spinner can provide
     */
    String biggestText();

    /**
     * Indicates if theire are a next value
     *
     * @return {@code true} if theire are a next value
     */
    boolean hasNextValue();

    /**
     * Indicates if theire are a prevoius value
     *
     * @return {@code true} if theire are a previous value
     */
    boolean hasPreviousValue();

    /**
     * Go next value.<br>
     * Do nothing if have no next value
     */
    void nextValue();

    /**
     * Go prevoius value.<br>
     * Do nothing if have no previous value
     */
    void previousValue();
}