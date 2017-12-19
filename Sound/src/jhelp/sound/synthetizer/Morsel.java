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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jhelp.util.debug.Debug;
import jhelp.util.xml.DynamicWriteXML;
import jhelp.xml.ExceptionParseXML;
import jhelp.xml.InvalidParameterValueException;
import jhelp.xml.InvalidTextException;
import jhelp.xml.MissingRequiredParameterException;
import jhelp.xml.ParseXMLlistener;
import jhelp.xml.ParserXML;
import jhelp.xml.UnexpectedEndOfMarkup;
import jhelp.xml.UnexpectedEndOfParse;

/**
 * Morsel<br>
 * Consist of list of partitions<br>
 * We can save/load morsel<br>
 * Format is zipped XML description
 */
public class Morsel
        implements Iterable<Partition>
{
    /**
     * Markup morsel
     */
    final static String MARKUP_MORSEL                  = "MORSEL";
    /**
     * Markup overtone
     */
    final static String MARKUP_OVERTONE                = "OVERTONE";
    /**
     * Markup overtone group
     */
    final static String MARKUP_OVERTONE_GROUP          = "OVERTONE_GROUP";
    /**
     * Markup overtone group parameter strength
     */
    final static String MARKUP_OVERTONE_GROUP_strength = "strength";
    /**
     * Markup overtone group parameter time
     */
    final static String MARKUP_OVERTONE_GROUP_time     = "time";
    /**
     * Markup overtone parameter gamut
     */
    final static String MARKUP_OVERTONE_gamut          = "gamut";
    /**
     * Markup overtone parameter overtone name
     */
    final static String MARKUP_OVERTONE_overtoneName   = "overtoneName";
    /**
     * Markup overtone partition
     */
    final static String MARKUP_PARTITION               = "PARTITION";
    /**
     * Markup overtone partition parameter instrument
     */
    final static String MARKUP_PARTITION_instrument    = "instrument";

    /**
     * Parser XML listener<br>
     * Use on load a morsel file <br>
     * <br>
     * Last modification : 2 avr. 2009<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    private class MorselParseXMLlistener
            implements ParseXMLlistener
    {
        /**
         * Last read overtone group
         */
        private OvertoneGroup overtoneGroup;
        /**
         * Last read partition
         */
        private Partition     partition;

        /**
         * Constructs MorselParseXMLlistener
         */
        public MorselParseXMLlistener()
        {
        }

        /**
         * Call if comment found
         *
         * @param comment Comment found
         * @see jhelp.xml.ParseXMLlistener#commentFind(String)
         */
        @Override
        public void commentFind(final String comment)
        {
        }

        /**
         * Call when meet a close of markup
         *
         * @param markupName Markup closed
         * @throws UnexpectedEndOfMarkup If markup mustn't close now
         * @see jhelp.xml.ParseXMLlistener#endMarkup(String)
         */
        @Override
        public void endMarkup(final String markupName)
        {
            if (markupName.equals(Morsel.MARKUP_PARTITION))
            {
                this.partition = null;
            }
            else if (markupName.equals(Morsel.MARKUP_OVERTONE_GROUP))
            {
                this.overtoneGroup = null;
            }
        }

        /**
         * Call when parsing end
         *
         * @throws UnexpectedEndOfParse If parse can't end now
         * @see jhelp.xml.ParseXMLlistener#endParse()
         */
        @Override
        public void endParse()
        {
            this.partition = null;
            this.overtoneGroup = null;
        }

        /**
         * Call when fatal error appends on parsing an force stop now parsing
         *
         * @param exceptionParseXML Error append
         * @see jhelp.xml.ParseXMLlistener#exceptionForceEndParse(jhelp.xml.ExceptionParseXML)
         */
        @Override
        public void exceptionForceEndParse(final ExceptionParseXML exceptionParseXML)
        {
            Debug.exception(exceptionParseXML);
        }

        /**
         * Call when start of markup detect
         *
         * @param markupName Markup opened
         * @param parameters Parameters (key, value) for this markup
         * @throws MissingRequiredParameterException If a requiered parameter missing (not in the hashtable)
         * @throws InvalidParameterValueException    If a parameter is not valid
         * @see jhelp.xml.ParseXMLlistener#startMarkup(String, Hashtable)
         */
        @Override
        public void startMarkup(final String markupName, final Hashtable<String, String> parameters)
                throws MissingRequiredParameterException,
                       InvalidParameterValueException
        {
            if (markupName.equals(Morsel.MARKUP_PARTITION))
            {
                this.partition = new Partition();
                final String instrumentName = parameters.get(Morsel.MARKUP_PARTITION_instrument);
                if (instrumentName == null)
                {
                    throw new MissingRequiredParameterException(Morsel.MARKUP_PARTITION_instrument,
                                                                Morsel.MARKUP_PARTITION);
                }
                try
                {
                    final Synthesiser synthesiser = Synthesiser.obtainSynthetiser();
                    this.partition.instrument(synthesiser.obtainInstrument(instrumentName));
                }
                catch (final SynthesiserException e)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_PARTITION_instrument,
                                                             Morsel.MARKUP_PARTITION, "Synthetyzer can't be initiated",
                                                             e);
                }
                Morsel.this.partitions.add(this.partition);
            }
            else if (markupName.equals(Morsel.MARKUP_OVERTONE_GROUP))
            {
                if (this.partition == null)
                {
                    throw new IllegalStateException("OvertoneGroup must be on Partition");
                }
                double time      = 0;
                int    strength  = 0;
                String parameter = parameters.get(Morsel.MARKUP_OVERTONE_GROUP_time);
                if (parameter == null)
                {
                    throw new MissingRequiredParameterException(Morsel.MARKUP_OVERTONE_GROUP_time,
                                                                Morsel.MARKUP_OVERTONE_GROUP);
                }
                try
                {
                    time = Double.parseDouble(parameter);
                }
                catch (final Exception exception)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_GROUP_time,
                                                             Morsel.MARKUP_OVERTONE_GROUP,
                                                             "Is not a double : " + parameter,
                                                             exception);
                }
                if (time < 0)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_GROUP_time,
                                                             Morsel.MARKUP_OVERTONE_GROUP,
                                                             "Is negative : " + parameter);
                }
                parameter = parameters.get(Morsel.MARKUP_OVERTONE_GROUP_strength);
                if (parameter == null)
                {
                    throw new MissingRequiredParameterException(Morsel.MARKUP_OVERTONE_GROUP_strength,
                                                                Morsel.MARKUP_OVERTONE_GROUP);
                }
                try
                {
                    strength = Integer.parseInt(parameter);
                }
                catch (final Exception exception)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_GROUP_strength,
                                                             Morsel.MARKUP_OVERTONE_GROUP,
                                                             "Is not a int : " + parameter,
                                                             exception);
                }
                if (strength < 0)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_GROUP_strength,
                                                             Morsel.MARKUP_OVERTONE_GROUP,
                                                             "Is negative : " + parameter);
                }
                this.overtoneGroup = new OvertoneGroup(time, strength);
                this.partition.addOvertoneGroup(this.overtoneGroup);
            }
            else if (markupName.equals(Morsel.MARKUP_OVERTONE))
            {
                if (this.overtoneGroup == null)
                {
                    throw new IllegalStateException("Overtone must be on OvertoneGroup");
                }
                int          gamut        = 0;
                int          overtone     = 0;
                OvertoneName overtoneName = null;
                String       parameter    = parameters.get(Morsel.MARKUP_OVERTONE_gamut);
                if (parameter == null)
                {
                    throw new MissingRequiredParameterException(Morsel.MARKUP_OVERTONE_gamut, Morsel.MARKUP_OVERTONE);
                }
                try
                {
                    gamut = Integer.parseInt(parameter);
                }
                catch (final Exception exception)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_gamut, Morsel.MARKUP_OVERTONE_GROUP,
                                                             "Is not a int : " + parameter, exception);
                }
                parameter = parameters.get(Morsel.MARKUP_OVERTONE_overtoneName);
                if (parameter == null)
                {
                    throw new MissingRequiredParameterException(Morsel.MARKUP_OVERTONE_overtoneName,
                                                                Morsel.MARKUP_OVERTONE);
                }
                try
                {
                    overtone = Integer.parseInt(parameter);
                    overtoneName = OvertoneName.obtainOvertoneName(overtone);
                }
                catch (final Exception exception)
                {
                    throw new InvalidParameterValueException(Morsel.MARKUP_OVERTONE_overtoneName,
                                                             Morsel.MARKUP_OVERTONE_GROUP, "Is not a valid overtone : "
                                                                                           + parameter, exception);
                }
                this.overtoneGroup.addOvertone(Overtone.obtainOvertone(gamut, overtoneName));
            }
        }

        /**
         * Call when parsing start
         *
         * @see jhelp.xml.ParseXMLlistener#startParse()
         */
        @Override
        public void startParse()
        {
            Morsel.this.clearAll();
            this.partition = null;
            this.overtoneGroup = null;
        }

        /**
         * Call when test find
         *
         * @param text Test found
         * @throws InvalidTextException If text is invalid in the current context
         * @see jhelp.xml.ParseXMLlistener#textFind(String)
         */
        @Override
        public void textFind(final String text)
        {
        }
    }

    /**
     * Listener when parsing to create morsel on loading
     */
    private final MorselParseXMLlistener morselParseXMLlistener;
    /**
     * Partitions list
     */
    final         ArrayList<Partition>   partitions;

    /**
     * Constructs Morsel
     */
    public Morsel()
    {
        this.morselParseXMLlistener = new MorselParseXMLlistener();
        this.partitions = new ArrayList<>();
    }

    /**
     * Add partition
     *
     * @param partition Partition add
     */
    public void addPartition(final Partition partition)
    {
        this.partitions.add(partition);
    }

    /**
     * Clear morsel
     */
    public void clear()
    {
        this.partitions.clear();
    }

    /**
     * Clear morsel and all partition and all overtone grroup
     */
    public void clearAll()
    {
        for (final Partition partition : this.partitions)
        {
            partition.clearAll();
        }
        this.partitions.clear();
    }

    /**
     * Insert partition
     *
     * @param partition Partition to insert
     * @param index     Index for insertion
     */
    public void insertPartition(final Partition partition, final int index)
    {
        this.partitions.add(index, partition);
    }

    /**
     * Partition iterator
     *
     * @return Partition iterator
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<Partition> iterator()
    {
        return this.partitions.iterator();
    }

    /**
     * Load morsel
     *
     * @param inputStream Stream to read
     * @throws IOException       On reading problem
     * @throws ExceptionParseXML If stream not a valid morsel
     */
    public void loadMorsel(final InputStream inputStream) throws IOException, ExceptionParseXML
    {
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        zipInputStream.getNextEntry();

        final ParserXML parserXML = new ParserXML();
        parserXML.parse(this.morselParseXMLlistener, zipInputStream);
    }

    /**
     * Number of partition
     *
     * @return Number of partition
     */
    public int numberOfPartition()
    {
        return this.partitions.size();
    }

    /**
     * Obtain parititon
     *
     * @param index Index
     * @return Partition
     */
    public Partition partition(final int index)
    {
        return this.partitions.get(index);
    }

    /**
     * Remove partition
     *
     * @param partition Partition to remove
     */
    public void removePartition(final Partition partition)
    {
        this.partitions.remove(partition);
    }

    /**
     * Save morsel
     *
     * @param outputStream Stream where write
     * @throws IOException          On writing problem
     * @throws SynthesiserException If synthetyzer can't be initiated
     */
    public void saveMorsel(final OutputStream outputStream) throws IOException, SynthesiserException
    {
        if (outputStream == null)
        {
            throw new NullPointerException("outputStream must not be null");
        }
        final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
        zipOutputStream.setLevel(9);
        final ZipEntry zipEntry = new ZipEntry("morsel.xml");
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOutputStream.putNextEntry(zipEntry);

        final Synthesiser     synthetyser     = Synthesiser.obtainSynthetiser();
        final DynamicWriteXML dynamicWriteXML = new DynamicWriteXML(zipOutputStream, true, false, false);
        dynamicWriteXML.openMarkup(Morsel.MARKUP_MORSEL);
        for (final Partition partition : this.partitions)
        {
            dynamicWriteXML.openMarkup(Morsel.MARKUP_PARTITION);
            dynamicWriteXML.appendParameter(Morsel.MARKUP_PARTITION_instrument,
                                            synthetyser.obtainNameOfInstrument(partition.instrument()));
            for (final OvertoneGroup overtoneGroup : partition)
            {
                dynamicWriteXML.openMarkup(Morsel.MARKUP_OVERTONE_GROUP);
                dynamicWriteXML.appendParameter(Morsel.MARKUP_OVERTONE_GROUP_strength,
                                                String.valueOf(overtoneGroup.strength()));
                dynamicWriteXML.appendParameter(Morsel.MARKUP_OVERTONE_GROUP_time,
                                                String.valueOf(overtoneGroup.time()));
                for (final Overtone overtone : overtoneGroup)
                {
                    dynamicWriteXML.openMarkup(Morsel.MARKUP_OVERTONE);
                    dynamicWriteXML.appendParameter(Morsel.MARKUP_OVERTONE_gamut, String.valueOf(overtone.gamut()));
                    dynamicWriteXML.appendParameter(Morsel.MARKUP_OVERTONE_overtoneName,
                                                    String.valueOf(overtone.overtoneName().overtone()));
                    dynamicWriteXML.closeMarkup();
                }
                dynamicWriteXML.closeMarkup();
            }
            dynamicWriteXML.closeMarkup();
        }
        dynamicWriteXML.closeMarkup();

        zipOutputStream.flush();
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        zipOutputStream.close();
    }
}