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

package jhelp.gui.code;

import com.sun.istack.internal.NotNull;

/**
 * Describe a decorator of code.<br>
 * Decorators says how decorate code for rules
 */
public interface Decorator
{
    /**
     * Font familly to use
     *
     * @return Font familly to use
     */
    @NotNull String fontFamily();

    /**
     * Normal text size (Other sizes are computed on using this value)
     *
     * @return Normal text size
     */
    int normalTextSize();

    /**
     * Obtain decoration for giving rule
     *
     * @param rules Rule
     * @return Decoration
     */
    @NotNull Decoration obtainDecoration(@NotNull Rules rules);
}
