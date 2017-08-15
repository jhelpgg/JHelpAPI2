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
package jhelp.sound.synthetizer;

/**
 * Listener for events on {@link Synthesiser}
 */
public interface SynthesiserListener
{
    /**
     * Call when {@link Synthesiser} change pause status
     *
     * @param pause New pause status
     */
    void synthesiserChangePause(boolean pause);

    /**
     * Call when {@link Synthesiser} start play morsel
     */
    void synthesiserStartPlay();

    /**
     * Call when {@link Synthesiser} stop play morsel
     */
    void synthesiserStopPlay();
}