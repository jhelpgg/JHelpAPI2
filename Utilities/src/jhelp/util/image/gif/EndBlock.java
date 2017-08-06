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

package jhelp.util.image.gif;

import java.io.IOException;
import java.io.InputStream;

/**
 * Block that end GIF stream<br>
 *
 * @author JHelp
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 */
class EndBlock
        extends Block
{
    /**
     * End block single instance
     */
    static final EndBlock END_BLOCK = new EndBlock();

    /**
     * Create a new instance of EndBlock
     */
    private EndBlock()
    {
    }

    /**
     * Read the block <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param inputStream Stream to read
     * @throws IOException Never happen
     * @see Block#read(InputStream)
     */
    @Override
    protected void read(final InputStream inputStream) throws IOException
    {
    }
}