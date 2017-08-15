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
 * Sound event listener
 */
public interface JHelpSoundListener
{
    /**
     * Call when sound destroy
     *
     * @param sound Destroyed sound
     */
    void soundDestroy(JHelpSound sound);

    /**
     * Call when sound loop
     *
     * @param sound Looped sound
     */
    void soundLoop(JHelpSound sound);

    /**
     * Call when sound start
     *
     * @param sound Started sound
     */
    void soundStart(JHelpSound sound);

    /**
     * Call when sound stop
     *
     * @param sound Stopped sound
     */
    void soundStop(JHelpSound sound);
}