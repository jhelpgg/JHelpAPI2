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

package jhelp.ontology;

import jhelp.util.list.SeekTest;

/**
 * Test if node match a reference one
 */
final class NodeTest implements SeekTest<Node>
{
    /**
     * Node to match with
     */
    private final Node reference;

    /**
     * Create the test
     *
     * @param reference Node to match with
     */
    NodeTest(final Node reference)
    {
        this.reference = reference;
    }

    /**
     * Indicates if an element is the seek one
     *
     * @param element Element test
     * @return {@code true} if the element match the seek test
     */
    @Override
    public boolean isElementSeek(final Node element)
    {
        return this.reference == Node.WILDCARD || this.reference.equals(element);
    }
}
