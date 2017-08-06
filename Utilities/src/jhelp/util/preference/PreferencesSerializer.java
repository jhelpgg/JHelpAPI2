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

package jhelp.util.preference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Pair;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.xml.DynamicWriteXML;

/**
 * Serialize preferences in XML
 *
 * @author JHelp
 */
class PreferencesSerializer
        implements RunnableTask, PreferencesFileConstants
{
    /**
     * Lock for synchronization
     */
    private final Mutex                                         mutex;
    /**
     * Preferences to serialize
     */
    private final HashMap<String, Pair<PreferenceType, Object>> preferences;
    /**
     * File where write XML
     */
    private final File                                          preferencesFile;
    /**
     * Indicates if a new serialization is need
     */
    private       boolean                                       serializeAgain;
    /**
     * Indicates if we are in serializing
     */
    private       boolean                                       serializing;

    /**
     * Create a new instance of PreferencesSerializer
     *
     * @param preferencesFile Preference files where write preferences
     * @param preferences     Preferences to serialize
     */
    PreferencesSerializer(final File preferencesFile, final HashMap<String, Pair<PreferenceType, Object>> preferences)
    {
        this.preferencesFile = preferencesFile;
        this.preferences = preferences;

        this.serializing = false;
        this.serializeAgain = false;
        this.mutex = new Mutex();
    }

    /**
     * Play the task
     */
    @Override
    public void run()
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            if (!UtilIO.createFile(this.preferencesFile))
            {
                throw new IOException("Can't create file " + this.preferencesFile.getAbsolutePath());
            }

            fileOutputStream = new FileOutputStream(this.preferencesFile);

            Pair<PreferenceType, Object> preference;

            final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(fileOutputStream);

            dynamicWriteXML.openMarkup(PreferencesFileConstants.MARKUP_PREFERENCES);

            for (final String name : this.preferences.keySet())
            {
                preference = this.preferences.get(name);

                dynamicWriteXML.openMarkup(PreferencesFileConstants.MARKUP_PREFERENCE);

                dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_NAME, name);
                dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_TYPE, preference.first.name());
                dynamicWriteXML.appendParameter(PreferencesFileConstants.PARAMETER_VALUE,
                                                Preferences.serialize(preference.second, preference.first));

                dynamicWriteXML.closeMarkup();
            }

            dynamicWriteXML.closeMarkup();
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Issue while serializing preferences !");
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.flush();
                }
                catch (final Exception ignored)
                {
                }

                try
                {
                    fileOutputStream.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Serialize the preferences as soon as possible
     */
    public void serialize()
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if (!this.serializing)
                                                 {
                                                     this.serializing = true;

                                                     ThreadManager.parallel(this);
                                                 }
                                                 else
                                                 {
                                                     this.serializeAgain = true;
                                                 }
                                             });
    }

    /**
     * Call when serialization is finish <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param result Unused
     */
    @Override
    public void taskResult(final Void result)
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.serializing = false;

                                                 if (this.serializeAgain)
                                                 {
                                                     this.serializeAgain = false;
                                                     this.serializing = true;

                                                     ThreadManager.parallel(this);
                                                 }
                                             });
    }
}