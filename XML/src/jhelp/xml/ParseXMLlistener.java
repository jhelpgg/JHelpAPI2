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
package jhelp.xml;

import java.util.Hashtable;

/**
 * Listener of parse XML<br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public interface ParseXMLlistener
{
    /**
     * Call when comment found
     *
     * @param comment
     *           Comment find
     */
    void commentFind(String comment);

    /**
     * Call when a end of markup meet
     *
     * @param markupName
     *           Markup name
     * @throws ExceptionXML
     *            On parsing issue. (Usually if markup can't aend now, see {@link UnexpectedEndOfMarkup})
     */
    void endMarkup(String markupName) throws ExceptionXML;

    /**
     * Call when parsing end
     *
     * @throws ExceptionXML
     *            On XML parsing issue (Usually when meet an unexpected end of parse. See {@link UnexpectedEndOfParse})
     */
    void endParse() throws ExceptionXML;

    /**
     * Call when exception happen that force the parsing to end
     *
     * @param exceptionParseXML
     *           Exception cause
     */
    void exceptionForceEndParse(ExceptionParseXML exceptionParseXML);

    /**
     * Call when start of markup meet.<br>
     * You can use {@link ParserXML#obtainBoolean(String, Hashtable, String, boolean, boolean)},
     * {@link ParserXML#obtainInteger(String, Hashtable, String, boolean, int)} and
     * {@link ParserXML#obtainParameter(String, Hashtable, String, boolean)} as helpers to obtain parameter value
     *
     * @param markupName
     *           Markup name
     * @param parameters
     *           Markup parameters
     * @throws ExceptionXML
     *            On XML parsing issue (Usually if a mandatory parameter missing, or a parameter value is invalid. See
     *            {@link MissingRequiredParameterException} and {@link InvalidParameterValueException})
     */
    void startMarkup(String markupName, Hashtable<String, String> parameters) throws ExceptionXML;

    /**
     * Call when parsing start
     */
    void startParse();

    /**
     * Call when a text meet
     *
     * @param text
     *           Text found
     * @throws ExceptionXML
     *            On XML parsing issue (Usually when text is not valid. See {@link InvalidTextException})
     */
    void textFind(String text) throws ExceptionXML;
}