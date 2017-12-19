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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import jhelp.sound.midi.SoundMidi;
import jhelp.sound.mp3.SoundMP3;
import jhelp.sound.other.SoundOther;
import jhelp.util.cache.Cache;
import jhelp.util.cache.CacheElement;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.resources.Resources;

/**
 * Factory to obtain good sound
 */
public class SoundFactory
{
    /**
     * Element of sound cache
     */
    static class CacheElementSound
            extends CacheElement<JHelpSound>
            implements JHelpSoundListener
    {
        /**
         * Resource file path
         */
        private File     path;
        /**
         * Reference class to get resource
         */
        private Class<?> referenceClass;
        /**
         * Resource name
         */
        private String   resourceName;
        /**
         * Resource URL
         */
        private URL      url;

        /**
         * Constructs CacheElementBufferedImage for file
         *
         * @param path File
         */
        public CacheElementSound(final File path)
        {
            if (path == null)
            {
                throw new NullPointerException("path mustn't be null");
            }

            this.path = path;
        }

        /**
         * Constructs CacheElementSound for resource
         *
         * @param resourceName   Resource name
         * @param referenceClass Reference class to get resource (From where path is relative to)
         */
        public CacheElementSound(final String resourceName, final Class<?> referenceClass)
        {
            if (resourceName == null)
            {
                throw new NullPointerException("resourceName mustn't be null");
            }

            if (referenceClass == null)
            {
                throw new NullPointerException("referenceClass mustn't be null");
            }

            this.referenceClass = referenceClass;
            this.resourceName = resourceName;
        }

        /**
         * Constructs CacheElementBufferedImage for url
         *
         * @param url URL
         */
        public CacheElementSound(final URL url)
        {
            if (url == null)
            {
                throw new NullPointerException("url mustn't be null");
            }

            this.url = url;
        }

        /**
         * Describe how create the sound
         *
         * @return Created sound or {@code null} on creation problem
         * @see CacheElement#create()
         */
        @Override
        protected JHelpSound create()
        {
            String givenName = null;

            try
            {
                // If it is resource
                if (this.resourceName != null)
                {
                    givenName = this.resourceName;

                    // Get file where sounds are extracted
                    this.path = UtilIO.obtainExternalFile("media/sounds/" + this.resourceName);

                    // If file dosen't extracted, extract it
                    if (!this.path.exists())
                    {
                        UtilIO.createFile(this.path);

                        final InputStream  inputStream  = this.referenceClass.getResourceAsStream(this.resourceName);
                        final OutputStream outputStream = new FileOutputStream(this.path);
                        final byte[]       temp         = new byte[4096];

                        int read = inputStream.read(temp);
                        while (read >= 0)
                        {
                            outputStream.write(temp, 0, read);

                            read = inputStream.read(temp);
                        }

                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();
                    }
                }
                else if (this.url != null)
                {
                    givenName = this.url.toString();

                    // Get file where sounds are extracted
                    this.path = UtilIO.obtainExternalFile("media/sounds/"
                                                          + givenName.replace("://", "/")
                                                                     .replace(":/", "/")
                                                                     .replace(":", "/")
                                                                     .replace("?", "/")
                                                                     .replace("&", "/")
                                                                     .replace("=", "/"));

                    // If file dosen't extracted, extract it
                    if (!this.path.exists())
                    {
                        UtilIO.createFile(this.path);

                        final InputStream  inputStream  = this.url.openStream();
                        final OutputStream outputStream = new FileOutputStream(this.path);
                        final byte[]       temp         = new byte[4096];

                        int read = inputStream.read(temp);
                        while (read >= 0)
                        {
                            outputStream.write(temp, 0, read);

                            read = inputStream.read(temp);
                        }

                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();
                    }
                }
                else
                {
                    givenName = this.path.getAbsolutePath();
                }

                // Choose the right implementation of sound according to file
                // extension
                Sound        sound = null;
                final String name  = this.path.getName().toLowerCase();

                if (name.endsWith(".mp3"))
                {
                    sound = new SoundMP3(this.path);
                }
                else if ((name.endsWith(".mid")) || (name.endsWith(".midi")))
                {
                    sound = new SoundMidi(this.path);
                }
                else
                {
                    sound = new SoundOther(this.path);
                }

                final JHelpSound jhelpSound = new JHelpSound(sound, givenName);
                jhelpSound.addSoundListener(this);
                return jhelpSound;
            }
            catch (final Exception e)
            {
                Debug.exception(e);
            }

            return null;
        }

        /**
         * Called when sound destroy <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Detroyed sound
         * @see jhelp.sound.JHelpSoundListener#soundDestroy(JHelpSound)
         */
        @Override
        public void soundDestroy(final JHelpSound sound)
        {
            sound.removeSoundListener(this);
            this.clear();
        }

        /**
         * Called when sound loop <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param sound Looped sound
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
        }
    }

    /**
     * Sound cache
     */
    private static final Cache<JHelpSound> CACHE = new Cache<JHelpSound>();

    /**
     * Get sound form file
     *
     * @param file File where the sound lies
     * @return The sound or {@code null} if sound can't be get
     */
    public static synchronized JHelpSound getSoundFromFile(final File file)
    {
        final String path = file.getAbsolutePath();

        JHelpSound sound = SoundFactory.CACHE.get(path);
        if (sound == null)
        {
            SoundFactory.CACHE.add(path, new CacheElementSound(file));

            sound = SoundFactory.CACHE.get(path);
        }
        return sound;
    }

    /**
     * Obtain sound from file without using the cache
     *
     * @param path Sound file
     * @return Extracted sound
     */
    public static synchronized JHelpSound getSoundFromFileNoCache(final File path)
    {
        final String givenName = path.getAbsolutePath();
        Sound        sound     = null;
        final String name      = path.getName().toLowerCase();

        if (name.endsWith(".mp3"))
        {
            sound = new SoundMP3(path);
        }
        else if ((name.endsWith(".mid")) || (name.endsWith(".midi")))
        {
            sound = new SoundMidi(path);
        }
        else
        {
            sound = new SoundOther(path);
        }

        return new JHelpSound(sound, givenName);
    }

    /**
     * Get sound form resource
     *
     * @param resourceName   Resource name where the sound lies
     * @param referenceClass Reference class to have from where path is relative to
     * @return The sound or {@code null} if sound can't be get
     */
    public static synchronized JHelpSound getSoundFromResource(final String resourceName, final Class<?> referenceClass)
    {
        JHelpSound sound = SoundFactory.CACHE.get(resourceName);

        if (sound == null)
        {
            SoundFactory.CACHE.add(resourceName, new CacheElementSound(resourceName, referenceClass));

            sound = SoundFactory.CACHE.get(resourceName);
        }

        return sound;
    }

    /**
     * Obtain sound from resource
     *
     * @param resourceName Resource path
     * @param resources    Resources where get the sound
     * @return Extracted sound OR {@code null} if resource not a sound
     */
    public static JHelpSound getSoundFromResource(final String resourceName, final Resources resources)
    {
        return SoundFactory.getSoundFromURL(resources.obtainResourceURL(resourceName));
    }

    /**
     * Obtain sound from resource without using the cache
     *
     * @param resourceName   Resource path
     * @param referenceClass Reference class for get resource
     * @return Extracted sound OR {@code null} if resource not a sound
     * @throws IOException On reading issue
     */
    public static synchronized JHelpSound getSoundFromResourceNoCache(
            final String resourceName, final Class<?> referenceClass) throws IOException
    {

        // Get file where sounds are extracted
        final File path = UtilIO.obtainExternalFile("media/sounds/" + resourceName);

        // If file dosen't extracted, extract it
        if (!path.exists())
        {
            UtilIO.createFile(path);

            final InputStream  inputStream  = referenceClass.getResourceAsStream(resourceName);
            final OutputStream outputStream = new FileOutputStream(path);
            final byte[]       temp         = new byte[4096];

            int read = inputStream.read(temp);
            while (read >= 0)
            {
                outputStream.write(temp, 0, read);

                read = inputStream.read(temp);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }

        // Choose the right implementation of sound according to file
        // extention
        Sound        sound = null;
        final String name  = path.getName().toLowerCase();

        if (name.endsWith(".mp3"))
        {
            sound = new SoundMP3(path);
        }
        else if ((name.endsWith(".mid")) || (name.endsWith(".midi")))
        {
            sound = new SoundMidi(path);
        }
        else
        {
            sound = new SoundOther(path);
        }

        return new JHelpSound(sound, resourceName);
    }

    /**
     * Obtain sound from resource without using cache
     *
     * @param resourceName Resource path
     * @param resources    Resources where get the sound
     * @return Extracted sound
     * @throws IOException On reading issue
     */
    public static JHelpSound getSoundFromResourceNoCache(final String resourceName, final Resources resources)
            throws IOException
    {
        return SoundFactory.getSoundFromURLNoCache(resources.obtainResourceURL(resourceName));
    }

    /**
     * Get sound form url
     *
     * @param url URL where the sound lies
     * @return The sound or {@code null} if sound can't be get
     */
    public static synchronized JHelpSound getSoundFromURL(final URL url)
    {
        final File file = new File(url.getFile());

        if (file.exists())
        {
            return SoundFactory.getSoundFromFile(file);
        }

        final String path = url.toString();

        JHelpSound sound = SoundFactory.CACHE.get(path);

        if (sound == null)
        {
            SoundFactory.CACHE.add(path, new CacheElementSound(url));

            sound = SoundFactory.CACHE.get(path);
        }

        return sound;
    }

    /**
     * Obtain sound from URL without using the cache
     *
     * @param url URL where get the sound
     * @return Extracted sound
     * @throws IOException On reading issue
     */
    public static synchronized JHelpSound getSoundFromURLNoCache(final URL url) throws IOException
    {
        final File file = new File(url.getFile());

        if (file.exists())
        {
            return SoundFactory.getSoundFromFileNoCache(file);
        }

        final String givenName = url.toString();

        // Get file where sounds are extracted
        final File path = UtilIO.obtainExternalFile("media/sounds/"
                                                    + givenName.replace("://", "/")
                                                               .replace(":/", "/")
                                                               .replace(":", "/")
                                                               .replace("?", "/")
                                                               .replace("&", "/")
                                                               .replace("=", "/"));

        // If file dosen't extracted, extract it
        if (!path.exists())
        {
            UtilIO.createFile(path);

            final InputStream  inputStream  = url.openStream();
            final OutputStream outputStream = new FileOutputStream(path);
            final byte[]       temp         = new byte[4096];

            int read = inputStream.read(temp);
            while (read >= 0)
            {
                outputStream.write(temp, 0, read);

                read = inputStream.read(temp);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }

        return SoundFactory.getSoundFromFileNoCache(path);
    }
}