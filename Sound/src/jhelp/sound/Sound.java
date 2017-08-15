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
package jhelp.sound;

/**
 * A sound<br>
 */
public interface Sound
{
    /**
     * Destroy the sound
     */
    void destroy();

    /**
     * Play the sound.<br>
     * Launch the playing and return immediately.<br>
     * Never waits than sounds end
     */
    void play();

    /**
     * Indicates if sound is playing
     *
     * @return {@code true} if sound is playing
     */
    boolean playing();

    /**
     * Sound position
     *
     * @return Sound position
     */
    long position();

    /**
     * Change sound position
     *
     * @param position Sound position
     */
    void position(long position);

    /**
     * Define the sound listener
     *
     * @param soundListener Sound listener
     */
    void soundListener(SoundListener soundListener);

    /**
     * Stop the sound
     */
    void stop();

    /**
     * Sound total size
     *
     * @return Sound total size
     */
    long totalSize();
}