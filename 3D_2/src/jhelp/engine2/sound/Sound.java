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

package jhelp.engine2.sound;

/**
 * A generic sound
 */
public abstract class Sound
{
    /**
     * Create a sound
     */
    Sound()
    {
    }

    /**
     * Transfer data to a buffer to play sound in OpenAL
     *
     * @param buffer Buffer where put data
     * @return {@code true} if transfer succeed
     */
    abstract boolean transferToBuffer(int buffer);

    /**
     * Sound duration in milliseconds
     *
     * @return Sound duration in milliseconds
     */
    abstract public long duration();
}
