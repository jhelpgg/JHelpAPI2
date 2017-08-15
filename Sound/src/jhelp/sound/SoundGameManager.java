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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.debug.Debug;
import jhelp.util.list.Queue;
import jhelp.util.math.JHelpRandom;
import jhelp.util.resources.ResourceDirectory;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;

/**
 * Sound manager, design for games.<br>
 * It can play a background sound and a FX sound.<br>
 * Background sounds are played randomly from a directory.<br>
 * FX are played one after the other.
 */
public class SoundGameManager
{
    /**
     * Sound background ID
     */
    private static final int ID_BACKGROUND = 0;
    /**
     * Sound FX ID
     */
    private static final int ID_FX         = 1;
    /**
     * No sound ID
     */
    private static final int NO_ID         = -1;

    /**
     * Event manager, here manage sounds
     *
     * @author JHelp
     */
    private class EventManager
            implements JHelpSoundListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when sound destroy <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Destroyed sound
         * @see jhelp.sound.JHelpSoundListener#soundDestroy(JHelpSound)
         */
        @Override
        public void soundDestroy(final JHelpSound sound)
        {
        }

        /**
         * Called when a sound loop <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Sound that just looped
         * @see jhelp.sound.JHelpSoundListener#soundLoop(JHelpSound)
         */
        @Override
        public void soundLoop(final JHelpSound sound)
        {
        }

        /**
         * Called when sound start <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Started sound
         * @see jhelp.sound.JHelpSoundListener#soundStart(JHelpSound)
         */
        @Override
        public void soundStart(final JHelpSound sound)
        {
        }

        /**
         * Called when sound stop <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Stopped sound
         * @see jhelp.sound.JHelpSoundListener#soundStop(JHelpSound)
         */
        @Override
        public void soundStop(final JHelpSound sound)
        {
            switch (sound.getDeveloperId())
            {
                case SoundGameManager.ID_BACKGROUND:
                    SoundGameManager.this.initializeBackgroundSound();
                    break;
                case SoundGameManager.ID_FX:
                    SoundGameManager.this.nextFXSound();
                    break;
            }
        }
    }

    /**
     * Indicates if background sound is playing
     */
    private final AtomicBoolean backgroundPlaying;
    /**
     * Current background sound
     */
    private       JHelpSound    backgroundSound;
    /**
     * Event manager
     */
    private final EventManager  eventManager;
    /**
     * FX sound queue
     */
    private final Queue<String> fxQueue;
    /**
     * Current FX sound
     */
    private       JHelpSound    fxSound;
    /**
     * List of background sounds
     */
    private final List<String>  listBackgrounds;
    /**
     * Resources link where found sounds
     */
    private final Resources     resources;

    /**
     * Create a new instance of SoundGameManager
     *
     * @param resources           Resources where find sound
     * @param directoryBackground Directory where background sounds are
     * @throws IOException On creation issue
     */
    public SoundGameManager(final Resources resources, final ResourceDirectory directoryBackground)
            throws IOException
    {
        if (resources == null)
        {
            throw new NullPointerException("resources musn't be null");
        }

        if (directoryBackground == null)
        {
            throw new NullPointerException("directoryBackground musn't be null");
        }

        this.fxQueue = new Queue<String>();
        this.resources = resources;
        final ResourcesSystem resourcesSystem = this.resources.obtainResourcesSystem();
        this.backgroundPlaying = new AtomicBoolean(false);
        this.eventManager = new EventManager();
        this.listBackgrounds = new ArrayList<String>();
        final List<ResourceElement> list = resourcesSystem.obtainList(directoryBackground);

        for (final ResourceElement resourceElement : list)
        {
            if (!resourceElement.isDirectory())
            {
                this.listBackgrounds.add(resourceElement.getPath());
            }
        }

        if (this.listBackgrounds.isEmpty())
        {
            throw new IllegalArgumentException("The given directory " + directoryBackground.getPath() + " is empty");
        }
    }

    /**
     * Initialize background sound
     */
    void initializeBackgroundSound()
    {
        if (this.backgroundSound != null)
        {
            this.backgroundSound.removeSoundListener(this.eventManager);
            this.backgroundSound.setDeveloperId(SoundGameManager.NO_ID);
            this.backgroundSound.stop();
            this.backgroundSound.position(0);
        }

        this.backgroundSound = SoundFactory.getSoundFromResource(JHelpRandom.random(this.listBackgrounds),
                                                                 this.resources);
        this.backgroundSound.addSoundListener(this.eventManager);
        this.backgroundSound.setDeveloperId(SoundGameManager.ID_BACKGROUND);
        this.backgroundSound.position(0);
        this.backgroundSound.play();
    }

    /**
     * Play next FX sound
     */
    void nextFXSound()
    {
        synchronized (this.fxQueue)
        {
            if ((this.fxSound != null) && (this.fxSound.getDeveloperId() == SoundGameManager.ID_FX))
            {
                Debug.verbose("Stop previous");
                this.fxSound.removeSoundListener(this.eventManager);
                this.fxSound.setDeveloperId(SoundGameManager.NO_ID);
                this.fxSound.destroy();
                this.fxSound = null;
            }

            if (this.fxQueue.empty())
            {
                Debug.verbose("Queue is empty");
                return;
            }

            final String next = this.fxQueue.outQueue();
            Debug.verbose("next=", next);

            try
            {
                this.fxSound = SoundFactory.getSoundFromResourceNoCache(next, this.resources);
                this.fxSound.setDeveloperId(SoundGameManager.ID_FX);
                this.fxSound.position(0);
                this.fxSound.addSoundListener(this.eventManager);
                this.fxSound.play();
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Sound load failed : ", next);
                this.fxSound = null;
            }
        }
    }

    /**
     * Launch the background music
     */
    public void playBackground()
    {
        synchronized (this.backgroundPlaying)
        {
            if (this.backgroundPlaying.get())
            {
                return;
            }

            this.backgroundPlaying.set(true);

            if (this.backgroundSound == null)
            {
                this.initializeBackgroundSound();
            }
            else
            {
                this.backgroundSound.play();
            }
        }
    }

    /**
     * Play one FX sound
     *
     * @param path Resource path of FX sound to play
     */
    public void playFX(final String path)
    {
        Debug.verbose("path=", path);
        boolean empty = true;

        synchronized (this.fxQueue)
        {
            empty = this.fxQueue.empty();
            this.fxQueue.inQueue(path);
        }

        Debug.verbose("empty=", empty, " fxSound=", this.fxSound);

        if ((empty) && (this.fxSound == null))
        {
            Debug.verbose("call next");
            this.nextFXSound();
        }
    }

    /**
     * Stop background sound
     */
    public void stopBackground()
    {
        synchronized (this.backgroundPlaying)
        {
            if (!this.backgroundPlaying.get())
            {
                return;
            }

            this.backgroundPlaying.set(false);

            if (this.backgroundSound != null)
            {
                this.backgroundSound.pause();
            }
        }
    }
}