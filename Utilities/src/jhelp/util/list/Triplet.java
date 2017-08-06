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

package jhelp.util.list;

/**
 * Triplet of elements
 *
 * @param <TYPE1> Type of element 1
 * @param <TYPE2> Type of element 2
 * @param <TYPE3> Type of element 3
 * @author JHelp
 */
public class Triplet<TYPE1, TYPE2, TYPE3>
{
    /**
     * Element 1
     */
    public TYPE1 element1;
    /**
     * Element 2
     */
    public TYPE2 element2;
    /**
     * Element 3
     */
    public TYPE3 element3;

    /**
     * Create a new instance of Triplet
     */
    public Triplet()
    {
    }

    /**
     * Create a new instance of Triplet
     *
     * @param element1 Element 1
     * @param element2 Element 2
     * @param element3 Element 3
     */
    public Triplet(final TYPE1 element1, final TYPE2 element2, final TYPE3 element3)
    {
        this.element1 = element1;
        this.element2 = element2;
        this.element3 = element3;
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "Triplet {" + this.element1 + ", " + this.element2 + ", " + this.element3 + "}";
    }
}