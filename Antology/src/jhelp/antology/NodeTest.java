package jhelp.antology;

import jhelp.util.list.SeekTest;

/**
 * Created by jhelp on 22/07/17.
 */
final class NodeTest implements SeekTest<Node>
{
    private final Node reference;

    public NodeTest(final Node reference)
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
