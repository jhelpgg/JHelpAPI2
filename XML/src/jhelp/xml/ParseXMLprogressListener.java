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

/**
 * Listener to follow the parsing progression<br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public interface ParseXMLprogressListener
{
    /**
     * Call when parsing cause an exception
     *
     * @param exceptionParseXML
     *           Exception cause
     */
    void exceptionAppend(ExceptionParseXML exceptionParseXML);

    /**
     * Call when meet a close of markup
     *
     * @param markupName
     *           Markup name
     * @param line
     *           Current line in XML
     * @param column
     *           Current column in XML
     */
    void findCloseMarkup(String markupName, int line, int column);

    /**
     * Call when meet an open of markup
     *
     * @param markupName
     *           Markup name
     * @param line
     *           Current line in XML
     * @param column
     *           Current column in XML
     */
    void findOpenMarkup(String markupName, int line, int column);

    /**
     * Call when find parameter in markup
     *
     * @param markupName
     *           Markup name
     * @param parameterName
     *           Parameter name
     * @param line
     *           Current line in XML
     * @param column
     *           Current column in XML
     */
    void findParameter(String markupName, String parameterName, int line, int column);

    /**
     * Call when meet text in markup
     *
     * @param markupName
     *           Markup name
     * @param text
     *           Text find
     * @param line
     *           Current line in XML
     * @param column
     *           Current column in XML
     */
    void findTextMarkup(String markupName, String text, int line, int column);
}