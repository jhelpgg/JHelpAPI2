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

package jhelp.util.time;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;

/**
 * Embed a time to be able binarize it, for stream transfer
 *
 * @author JHelp
 */
public final class TimeBinarizable
        implements Binarizable
{
    /**
     * Embed time
     */
    private final LapsTime lapsTime;
    /**
     * Last time where {@link #start()} was called
     */
    private       long     startTime;

    /**
     * Create a new instance of TimeBinarizable
     */
    public TimeBinarizable()
    {
        this.lapsTime = new LapsTime();
    }

    /**
     * Stop measure time started with {@link #start()}, to get the measured laps time use {@link #getLapsTime()}
     */
    public void end()
    {
        this.lapsTime.addMilliseconds(System.currentTimeMillis() - this.startTime);
    }

    /**
     * Get laps time between last {@link #start()} and last {@link #end()}
     *
     * @return The laps time
     */
    public LapsTime getLapsTime()
    {
        return this.lapsTime;
    }

    /**
     * Parse a byte array and get the time in it <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param byteArray Byte array to parse
     * @see Binarizable#parseBinary(ByteArray)
     */
    @Override
    public void parseBinary(final ByteArray byteArray)
    {
        this.lapsTime.setMicroseconds(byteArray.readLong());
    }

    /**
     * Serialize the embed time inside a byte array <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param byteArray Byte array to fill
     * @see Binarizable#serializeBinary(ByteArray)
     */
    @Override
    public void serializeBinary(final ByteArray byteArray)
    {
        byteArray.writeLong(this.lapsTime.inMicroseconds());
    }

    /**
     * Start measure time, to stop measure call {@link #end()}, then to have the result call {@link #getLapsTime()}
     */
    public void start()
    {
        this.startTime = System.currentTimeMillis();
    }
}