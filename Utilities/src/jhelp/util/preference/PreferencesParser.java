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
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jhelp.util.debug.Debug;
import jhelp.util.list.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser for preferences
 *
 * @author JHelp
 */
class PreferencesParser
        extends DefaultHandler
        implements PreferencesFileConstants
{
    /**
     * Indicates if we wait first main markup
     */
    private       boolean                                       mainMarkup;
    /**
     * Preference to fill
     */
    private final HashMap<String, Pair<PreferenceType, Object>> preferences;

    /**
     * Create a new instance of PreferencesParser and fill preferences from file
     *
     * @param preferencesFile Preference file to parse
     * @param preferences     Preferences to fill
     */
    PreferencesParser(final File preferencesFile, final HashMap<String, Pair<PreferenceType, Object>> preferences)
    {
        this.preferences = preferences;

        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try
        {
            this.mainMarkup = true;

            final SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(preferencesFile, this);
        }
        catch (final ParserConfigurationException | IOException | SAXException exception)
        {
            Debug.exception(exception);
        }
    }

    /**
     * Get a parameter value
     *
     * @param parameter  Parameter name
     * @param attributes Attributes where extract the value
     * @return Extracted value
     * @throws SAXException If parameter ask doesn't exists
     */
    private String getParameter(final String parameter, final Attributes attributes) throws SAXException
    {
        final String value = attributes.getValue(parameter);

        if (value == null)
        {
            throw new SAXException("Missing the parameter " + parameter + " !");
        }

        return value;
    }

    /**
     * Call by parser when a markup open <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param uri        URI
     * @param localName  Local name
     * @param qName      Q name
     * @param attributes Attributes with parameters and corresponding value
     * @throws SAXException If markup not valid or a parameter missing
     * @see DefaultHandler#startElement(String, String, String,
     * Attributes)
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException
    {
        final String name = (localName == null || localName.length() == 0)
                            ? qName
                            : localName;

        if (this.mainMarkup)
        {
            if (!PreferencesFileConstants.MARKUP_PREFERENCES.equals(name))
            {
                throw new SAXException(
                        "The first markup MUST be " + PreferencesFileConstants.MARKUP_PREFERENCES + " not " + name);
            }

            this.mainMarkup = false;

            return;
        }

        if (!PreferencesFileConstants.MARKUP_PREFERENCE.equals(name))
        {
            throw new SAXException("The markup MUST be " + PreferencesFileConstants.MARKUP_PREFERENCE + " not " + name);
        }

        final String preferenceName  = this.getParameter(PreferencesFileConstants.PARAMETER_NAME, attributes);
        final String type            = this.getParameter(PreferencesFileConstants.PARAMETER_TYPE, attributes);
        final String preferenceValue = this.getParameter(PreferencesFileConstants.PARAMETER_VALUE, attributes);

        final PreferenceType preferenceType = PreferenceType.valueOf(type);

        this.preferences.put(preferenceName,//
                             new Pair<>(preferenceType,
                                        Preferences.parse(preferenceValue, preferenceType)));
    }

    /**
     * Call by parser when warning happen <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Warning
     * @throws SAXException Not throw
     * @see DefaultHandler#warning(SAXParseException)
     */
    @Override
    public void warning(final SAXParseException e)
    {
        Debug.exception(e, "Warning /!\\");
    }

    /**
     * Call by parser when error happen <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Error happen
     * @throws SAXException Not throw
     * @see DefaultHandler#error(SAXParseException)
     */
    @Override
    public void error(final SAXParseException e)
    {
        Debug.exception(e, "FAILED !!!");
    }

    /**
     * Call by parser when fatal error happen <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Fatal error
     * @throws SAXException Not throw
     * @see DefaultHandler#fatalError(SAXParseException)
     */
    @Override
    public void fatalError(final SAXParseException e)
    {
        Debug.exception(e, "FATAL FAILED !!!");
    }
}