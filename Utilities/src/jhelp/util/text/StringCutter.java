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

package jhelp.util.text;

/**
 * Cut a string in several part with a separator character
 *
 * @author JHelp
 */
public class StringCutter
{
    /**
     * Separator character
     */
    private final char   cut;
    /**
     * Current read index
     */
    private       int    index;
    /**
     * String length
     */
    private final int    length;
    /**
     * String to cut
     */
    private final String string;

    /**
     * Create a new instance of StringCutter
     *
     * @param string String to cut
     * @param cut    Separator character
     */
    public StringCutter(final String string, final char cut)
    {
        this.string = string;
        this.cut = cut;

        this.index = 0;
        this.length = string.length();
    }

    /**
     * Give the next part or {@code null} if no more part to read
     *
     * @return Next part
     */
    public String next()
    {
        if (this.index < 0)
        {
            return null;
        }

        if (this.index >= this.length)
        {
            this.index = -1;

            return "";
        }

        final int ind = this.string.indexOf(this.cut, this.index);

        if (ind < 0)
        {
            final int i = this.index;
            this.index = -1;

            return this.string.substring(i);
        }

        final int start = this.index;
        this.index = ind + 1;

        return this.string.substring(start, ind);
    }
}