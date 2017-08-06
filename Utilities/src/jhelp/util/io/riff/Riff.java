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

package jhelp.util.io.riff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * RIFF document file
 *
 * @author JHelp
 */
public class Riff
{
    /**
     * RIFF chunks
     */
    private final List<RiffChunk> chunks;

    /**
     * Create a new instance of Riff
     *
     * @param inputStream Stream to parse
     * @throws IOException On reading issue
     */
    public Riff(final InputStream inputStream)
            throws IOException
    {
        this.chunks = new ArrayList<>();

        while (inputStream.available() > 0)
        {
            this.chunks.add(new RiffChunk(inputStream));
        }
    }

    /**
     * Number of chunks
     *
     * @return Number of chunks
     */
    public int chunkCount()
    {
        return this.chunks.size();
    }

    /**
     * Get one chunk
     *
     * @param index Chunk index
     * @return Chunk
     */
    public RiffChunk getChunk(final int index)
    {
        return this.chunks.get(index);
    }
}