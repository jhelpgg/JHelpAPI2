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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.sound;

import java.util.ArrayList;
import java.util.Objects;
import jhelp.sound.mp3.SoundMP3;
import jhelp.util.list.Pair;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

/**
 * Engine generic sound<br>
 * It can play/pause/resume/stop : MP3, wav, midi, au sounds.<br>
 * To obtain an instance use {@link SoundFactory#getSoundFromFile(java.io.File)} ,
 * {@link SoundFactory#getSoundFromResource(String, Class)} or {@link SoundFactory#getSoundFromURL(java.net.URL)}, be sure that
 * your files have correct extension<br>
 * <br>
 * Last modification : 9 aoet 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public final class JHelpSound
{
    /**
     * State destroy
     */
    private static final int SOUND_DESTROY = 3;
    /**
     * State loop
     */
    private static final int SOUND_LOOP    = 2;
    /**
     * State start
     */
    private static final int SOUND_START   = 0;
    /**
     * State stop
     */
    private static final int SOUND_STOP    = 1;

    /**
     * Indicates if sound is destroy after playing all loops
     */
    private boolean destroyOnEnd;

    /**
     * ID give by developer
     */
    private       int     developerId;
    /**
     * Loop left
     */
    private       int     loop;
    /**
     * Sound name
     */
    private final String  name;
    /**
     * Indicates if sound is pause
     */
    private       boolean pause;
    /**
     * Real sound play
     */
    private       Sound   sound;
    /**
     * Indicates if sound can be destroy
     */
    boolean canDestroy;
    /**
     * Fire sound state for one listener
     */
    final ConsumerTask<Pair<Integer, JHelpSoundListener>> fireSoundStateOneListener =
            new ConsumerTask<Pair<Integer, JHelpSoundListener>>()
            {
                /**
                 * Play the action <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param parameter
                 *           New sound state
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(
                        final Pair<Integer, JHelpSoundListener> parameter)
                {
                    JHelpSound.this.delayedFireSoundState(parameter.second,
                                                          parameter.first);
                }
            };
    /**
     * Synchronization lock
     */
    final Mutex                         lock;
    /**
     * Sound listeners
     */
    final ArrayList<JHelpSoundListener> soundListeners;
    /**
     * Signal to listeners that sound state changed
     */
    private final ConsumerTask<Integer> fireSoundState        = new ConsumerTask<Integer>()
    {
        /**
         * Play the action <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter
         *           New sound state
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Integer parameter)
        {
            JHelpSound.this.lock.playInCriticalSectionVoid(() ->
                                                           {
                                                               JHelpSound.this.canDestroy = false;

                                                               try
                                                               {
                                                                   for (final JHelpSoundListener soundListener : JHelpSound.this.soundListeners)
                                                                   {
                                                                       ThreadManager.parallel(
                                                                               JHelpSound.this.fireSoundStateOneListener,
                                                                               new Pair<>(parameter,
                                                                                          soundListener));
                                                                   }
                                                               }
                                                               catch (final Exception ignored)
                                                               {
                                                               }

                                                               JHelpSound.this.canDestroy = true;
                                                           });
        }
    };
    /**
     * Sound listener
     */
    private final SoundListener         soundListenerInternal = new SoundListener()
    {
        /**
         * Call when sound end
         *
         * @see jhelp.sound.SoundListener#soundEnd()
         */
        @Override
        public void soundEnd()
        {
            JHelpSound.this.soundEnd();
        }

        /**
         * Call when sound loop
         *
         * @see jhelp.sound.SoundListener#soundLoop()
         */
        @Override
        public void soundLoop()
        {
            JHelpSound.this.soundLoop();
        }
    };

    /**
     * Constructs EngineSound
     *
     * @param sound Embed sound
     * @param name  Sound name
     */
    JHelpSound(final Sound sound, final String name)
    {
        this.lock = new Mutex();
        this.name = name;
        this.sound = sound;
        this.sound.soundListener(this.soundListenerInternal);
        this.soundListeners = new ArrayList<JHelpSoundListener>();
        this.pause = false;
        this.destroyOnEnd = false;
    }

    /**
     * Signal to a listener that sound state change
     *
     * @param soundListener Listener to alert
     * @param state         New sound state
     */
    void delayedFireSoundState(final JHelpSoundListener soundListener, final int state)
    {
        if (soundListener == null)
        {
            return;
        }

        switch (state)
        {
            case JHelpSound.SOUND_START:
                soundListener.soundStart(this);
                break;
            case JHelpSound.SOUND_LOOP:
                soundListener.soundLoop(this);
                break;
            case JHelpSound.SOUND_STOP:
                soundListener.soundStop(this);
                break;
            case JHelpSound.SOUND_DESTROY:
                soundListener.soundDestroy(this);
                break;
        }
    }

    /**
     * Call when sound end
     */
    void soundEnd()
    {
        if (this.pause)
        {
            return;
        }

        if (this.loop > 1)
        {
            this.loop--;
            this.play();

            ThreadManager.parallel(this.fireSoundState, JHelpSound.SOUND_LOOP);

            return;
        }

        ThreadManager.parallel(this.fireSoundState, JHelpSound.SOUND_STOP);

        if (this.destroyOnEnd)
        {
            this.destroy();
        }
    }

    /**
     * Call when sound loop
     */
    void soundLoop()
    {
    }

    /**
     * add sound listener
     *
     * @param soundListener New sound listener
     */
    public void addSoundListener(final JHelpSoundListener soundListener)
    {
        Objects.requireNonNull(soundListener, "soundListener MUST NOT be null!");
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                if (!this.soundListeners.contains(soundListener))
                                                {
                                                    this.soundListeners.add(soundListener);
                                                }
                                            });
    }

    /**
     * Destroy the sound
     */
    public void destroy()
    {
        this.lock.playInCriticalSectionVoid(() -> this.canDestroy = false);

        ThreadManager.parallel(this.fireSoundState, JHelpSound.SOUND_DESTROY);

        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                if (this.sound != null)
                                                {
                                                    this.sound.destroy();
                                                }
                                                this.sound = null;
                                            });

        while (!this.canDestroy)
        {
            Utilities.sleep(8);
        }

        this.lock.playInCriticalSectionVoid(() -> this.soundListeners.clear());
    }

    /**
     * Return destroyOnEnd
     *
     * @return destroyOnEnd
     */
    public boolean destroyOnEnd()
    {
        return this.destroyOnEnd;
    }

    /**
     * Modify destroyOnEnd
     *
     * @param destroyOnEnd New destroyOnEnd value
     */
    public void destroyOnEnd(final boolean destroyOnEnd)
    {
        this.destroyOnEnd = destroyOnEnd;
    }

    /**
     * Return developerId
     *
     * @return developerId
     */
    public int getDeveloperId()
    {
        return this.developerId;
    }

    /**
     * Modify developerId
     *
     * @param developerId New developerId value
     */
    public void setDeveloperId(final int developerId)
    {
        this.developerId = developerId;
    }

    /**
     * Return name
     *
     * @return name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Indicates if sound is pause
     *
     * @return {@code true} if sound is pause
     */
    public boolean isPause()
    {
        return this.pause;
    }

    /**
     * Indicates if sound is playing
     *
     * @return {@code true} if sound is playing
     */
    public boolean isPlaying()
    {
        return this.lock.playInCriticalSection(() -> this.sound != null && this.sound.playing());
    }

    /**
     * Loop the sound "infinite" time
     */
    public void loop()
    {
        this.loop(Integer.MAX_VALUE);
    }

    /**
     * Loop the sound
     *
     * @param loop Number of loop
     */
    public void loop(final int loop)
    {
        this.loop = loop;
        this.play();
    }

    /**
     * Pause the sound
     */
    public void pause()
    {
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                this.pause = true;

                                                if (this.sound instanceof SoundMP3)
                                                {
                                                    ((SoundMP3) this.sound).setPause(true);
                                                }
                                                else if (this.sound.playing())
                                                {
                                                    this.sound.stop();
                                                }
                                            });
    }

    /**
     * Play the sound
     */
    public void play()
    {
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                if (this.sound != null)
                                                {
                                                    if (((this.sound instanceof SoundMP3)) && (this.pause))
                                                    {
                                                        ((SoundMP3) this.sound).setPause(false);
                                                    }
                                                    else if (!this.sound.playing())
                                                    {
                                                        ThreadManager.parallel(this.fireSoundState,
                                                                               JHelpSound.SOUND_START);

                                                        this.sound.play();
                                                    }
                                                }

                                                this.pause = false;
                                            });
    }

    /**
     * Sound position
     *
     * @return Sound position
     */
    public long position()
    {
        return this.lock.playInCriticalSection(() ->
                                               {
                                                   if (this.sound != null)
                                                   {
                                                       return this.sound.position();
                                                   }

                                                   return 0L;
                                               });
    }

    /**
     * Change sound position
     *
     * @param position Sound position
     */
    public void position(final long position)
    {
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                if (this.sound != null)
                                                {
                                                    this.sound.position(position);
                                                }
                                            });
    }

    /**
     * Remove sound listener
     *
     * @param soundListener Listener to remove
     */
    public void removeSoundListener(final JHelpSoundListener soundListener)
    {
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                try
                                                {
                                                    this.soundListeners.remove(soundListener);
                                                }
                                                catch (final Exception ignored)
                                                {
                                                }
                                            });
    }

    /**
     * Stop the sound
     */
    public void stop()
    {
        this.lock.playInCriticalSectionVoid(() ->
                                            {
                                                if (this.sound != null)
                                                {
                                                    if (this.sound.playing())
                                                    {
                                                        this.sound.stop();
                                                    }
                                                    this.sound.position(0);
                                                }
                                            });
    }

    /**
     * Sound total size
     *
     * @return Sound total size
     */
    public long totalSize()
    {
        return this.lock.playInCriticalSection(() ->
                                               {
                                                   if (this.sound != null)
                                                   {
                                                       return this.sound.totalSize();
                                                   }

                                                   return 0L;
                                               });
    }
}