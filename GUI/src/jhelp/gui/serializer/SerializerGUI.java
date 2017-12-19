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
package jhelp.gui.serializer;

import java.io.IOException;
import java.util.Hashtable;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpGradient;
import jhelp.util.gui.JHelpGradientHorizontal;
import jhelp.util.gui.JHelpGradientVertical;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.list.SortedArray;
import jhelp.util.text.UtilText;
import jhelp.util.xml.DynamicWriteXML;
import jhelp.xml.ExceptionXML;
import jhelp.xml.InvalidParameterValueException;
import jhelp.xml.ParserXML;

/**
 * Tools for serialize GUI to XML<br>
 * Tools for parse XML to GUI
 *
 * @author JHelp
 */
public class SerializerGUI
{
    /**
     * Represents a percentage for gradients
     *
     * @author JHelp
     */
    public static class Percent
            implements Comparable<Percent>
    {
        /**
         * Color
         */
        final int color;
        /**
         * Percentage
         */
        final int percent;

        /**
         * Create a new instance of Percent
         *
         * @param percent Percentage
         * @param color   Color
         */
        Percent(final int percent, final int color)
        {
            this.percent = percent;
            this.color = color;
        }

        /**
         * Compare with an other percentage.<br>
         * It returns
         * <table>
         * <tr>
         * <th>&lt; 0</th>
         * <td>:</td>
         * <td>If this percentage is before the given one</td>
         * </tr>
         * <tr>
         * <th>0</th>
         * <td>:</td>
         * <td>If this percentage is the same as the given one</td>
         * </tr>
         * <tr>
         * <th>&gt; 0</th>
         * <td>:</td>
         * <td>If this percentage is after the given one</td>
         * </tr>
         * </table>
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param percent Percentage to compare with
         * @return Comparison result
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(final Percent percent)
        {
            return this.percent - percent.percent;
        }

        /**
         * Associated color
         *
         * @return Color
         */
        public int getColor()
        {
            return this.color;
        }

        /**
         * Percentage
         *
         * @return Percentage
         */
        public int getPercent()
        {
            return this.percent;
        }

        /**
         * String representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return String representation
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            return UtilText.concatenate(this.percent, "% ", UtilText.colorText(this.color));
        }
    }

    /**
     * Percentages list
     */
    private static final SortedArray<Percent> PERCENTS = new SortedArray<Percent>(Percent.class);

    /**
     * Parse percentage from XML markup
     *
     * @param markupName Markup name
     * @param parameters Markup parameters
     * @throws ExceptionXML If the markup and parameters not represents a valid percentage
     */
    public static void deseralizePercent(final String markupName, final Hashtable<String, String> parameters)
            throws ExceptionXML
    {
        if (!Constants.MARKUP_PERCENT.equals(markupName))
        {
            throw new ExceptionXML("The markup must be " + Constants.MARKUP_PERCENT + " not " + markupName);
        }

        final int color   = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_COLOR, true, 0);
        final int percent = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_PERCENT, true, 0);

        SerializerGUI.PERCENTS.add(new Percent(percent, color));
    }

    /**
     * Parse a font from markup XML
     *
     * @param markupName Markup name
     * @param parameters Markup parameters
     * @return Parsed font
     * @throws ExceptionXML If markup and parameters not describes a valid font
     */
    public static JHelpFont deserialzeFont(final String markupName, final Hashtable<String, String> parameters)
            throws ExceptionXML
    {
        if (!Constants.MARKUP_FONT.equals(markupName))
        {
            throw new ExceptionXML("The markup must be " + Constants.MARKUP_FONT + " not " + markupName);
        }

        final String family = ParserXML.obtainParameter(markupName, parameters, Constants.PARAMETER_FAMILY, true);
        final int    size   = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_SIZE, true, 0);

        if (size < 1)
        {
            throw new InvalidParameterValueException(Constants.PARAMETER_SIZE, markupName,
                                                     "The parameter must be >=1 not " + size);
        }

        final boolean bold = ParserXML.obtainBoolean(markupName, parameters, Constants.PARAMETER_BOLD, false,
                                                     false);
        final boolean italic = ParserXML.obtainBoolean(markupName, parameters, Constants.PARAMETER_ITALIC, false,
                                                       false);
        final boolean underline = ParserXML.obtainBoolean(markupName, parameters, Constants.PARAMETER_UNDERLINE, false,
                                                          false);

        return new JHelpFont(family, size, bold, italic, underline);
    }

    /**
     * Parse a gradient from markup
     *
     * @param markupName Markup name
     * @param parameters Markup parameters
     * @return Parsed gradient
     * @throws ExceptionXML If markup or parameters don't represents a valid gradient
     */
    public static JHelpGradient deserialzeJHelpGradient(
            final String markupName, final Hashtable<String, String> parameters) throws ExceptionXML
    {
        if (!Constants.MARKUP_GRADIENT.equals(markupName))
        {
            throw new ExceptionXML("The markup must be " + Constants.MARKUP_GRADIENT + " not " + markupName);
        }

        final int upLeft    = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_UP_LEFT, true, 0);
        final int upRight   = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_UP_RIGHT, true, 0);
        final int downLeft  = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_DOWN_LEFT, true, 0);
        final int downRight = ParserXML.obtainInteger(markupName, parameters, Constants.PARAMETER_DOWN_RIGHT, true, 0);

        return new JHelpGradient(upLeft, upRight, downLeft, downRight);
    }

    /**
     * Terminate the parsing of horizontal or vertical gradient at the close of markup
     *
     * @param markupName Markup name
     * @return Parsed gradient
     * @throws ExceptionXML If markup not close properly an horizontal or vertical gradient
     */
    public static JHelpPaint endDeseralizeGradientHorizontalOrVertical(final String markupName) throws ExceptionXML
    {
        if ((!Constants.MARKUP_GRADIENT_HORIZONTAL.equals(markupName)) && (!Constants.MARKUP_GRADIENT_VERTICAL.equals(
                markupName)))
        {
            throw new ExceptionXML("The markup must be " + Constants.MARKUP_GRADIENT_HORIZONTAL + " or " +
                                   Constants.MARKUP_GRADIENT_VERTICAL + " not "
                                   + markupName);
        }

        final int size  = SerializerGUI.PERCENTS.size();
        boolean   valid = size > 1;
        Percent   first = null;
        Percent   last  = null;

        if (valid)
        {
            first = SerializerGUI.PERCENTS.get(0);
            last = SerializerGUI.PERCENTS.get(size - 1);

            valid = (first.percent == 0) && (last.percent == 100);
        }

        if (!valid)
        {
            SerializerGUI.PERCENTS.clear();

            throw new ExceptionXML(
                    "The gradient description is not valid, it must have at least 2 'Percent' children, one associated with 0, one associated with 100");
        }

        final int limit = size - 1;
        Percent   percent;

        if (Constants.MARKUP_GRADIENT_HORIZONTAL.equals(markupName))
        {
            final JHelpGradientHorizontal gradientHorizontal = new JHelpGradientHorizontal(first.color, last.color);

            for (int i = 1; i < limit; i++)
            {
                percent = SerializerGUI.PERCENTS.get(i);

                gradientHorizontal.addColor(percent.percent, percent.color);
            }

            SerializerGUI.PERCENTS.clear();

            return gradientHorizontal;
        }

        final JHelpGradientVertical gradientVertical = new JHelpGradientVertical(first.color, last.color);

        for (int i = 1; i < limit; i++)
        {
            percent = SerializerGUI.PERCENTS.get(i);

            gradientVertical.addColor(percent.percent, percent.color);
        }

        SerializerGUI.PERCENTS.clear();

        return gradientVertical;
    }

    /**
     * Serialize a font
     *
     * @param font            Font to serialize
     * @param dynamicWriteXML XML where write
     * @throws IOException On writing issue
     */
    public static void serialize(final JHelpFont font, final DynamicWriteXML dynamicWriteXML) throws IOException
    {
        dynamicWriteXML.openMarkup(Constants.MARKUP_FONT);

        dynamicWriteXML.appendParameter(Constants.PARAMETER_FAMILY, font.getFamily());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_SIZE, font.getSize());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_BOLD, font.isBold());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_ITALIC, font.isItalic());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_UNDERLINE, font.isUnderline());

        dynamicWriteXML.closeMarkup();
    }

    /**
     * Serialize a gradient
     *
     * @param gradient        Gradient to serialize
     * @param dynamicWriteXML XML where write
     * @throws IOException On writing issue
     */
    public static void serialize(final JHelpGradient gradient, final DynamicWriteXML dynamicWriteXML) throws IOException
    {
        dynamicWriteXML.openMarkup(Constants.MARKUP_GRADIENT);

        dynamicWriteXML.appendParameter(Constants.PARAMETER_UP_LEFT, gradient.getColorUpLeft());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_UP_RIGHT, gradient.getColorUpRight());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_DOWN_LEFT, gradient.getColorDownLeft());
        dynamicWriteXML.appendParameter(Constants.PARAMETER_DOWN_RIGHT, gradient.getColorDownRight());

        dynamicWriteXML.closeMarkup();
    }

    /**
     * Serialize a gradient horizontal
     *
     * @param gradient        Gradient horizontal to serialize
     * @param dynamicWriteXML XML where write
     * @throws IOException On writing issue
     */
    public static void serialize(final JHelpGradientHorizontal gradient, final DynamicWriteXML dynamicWriteXML)
            throws IOException
    {
        dynamicWriteXML.openMarkup(Constants.MARKUP_GRADIENT_HORIZONTAL);

        for (final JHelpGradientHorizontal.Percent percent : gradient.obtainPercents())
        {
            dynamicWriteXML.openMarkup(Constants.MARKUP_PERCENT);

            dynamicWriteXML.appendParameter(Constants.PARAMETER_COLOR, percent.getColor());
            dynamicWriteXML.appendParameter(Constants.PARAMETER_PERCENT, percent.getPercent());

            dynamicWriteXML.closeMarkup();
        }

        dynamicWriteXML.closeMarkup();
    }

    /**
     * Serialize a gradient vertical
     *
     * @param gradient        Gradient vertical to serialize
     * @param dynamicWriteXML XML where write
     * @throws IOException On writing issue
     */
    public static void serialize(final JHelpGradientVertical gradient, final DynamicWriteXML dynamicWriteXML)
            throws IOException
    {
        dynamicWriteXML.openMarkup(Constants.MARKUP_GRADIENT_VERTICAL);

        for (final JHelpGradientVertical.Percent percent : gradient.obtainPercents())
        {
            dynamicWriteXML.openMarkup(Constants.MARKUP_PERCENT);

            dynamicWriteXML.appendParameter(Constants.PARAMETER_COLOR, percent.getColor());
            dynamicWriteXML.appendParameter(Constants.PARAMETER_PERCENT, percent.getPercent());

            dynamicWriteXML.closeMarkup();
        }

        dynamicWriteXML.closeMarkup();
    }

    /**
     * Start to parse a horizontal or vertical gradient
     *
     * @param markupName Markup name
     * @param parameters Markup parameters
     * @throws ExceptionXML If markup or parameters didn't describes a valid horizontal or vertical gradient
     */
    public static void startDeseralizeGradientHorizontalOrVertical(
            final String markupName, final Hashtable<String, String> parameters) throws ExceptionXML
    {
        if ((!Constants.MARKUP_GRADIENT_HORIZONTAL.equals(markupName)) && (!Constants.MARKUP_GRADIENT_VERTICAL.equals(
                markupName)))
        {
            throw new ExceptionXML("The markup must be " + Constants.MARKUP_GRADIENT_HORIZONTAL + " or " +
                                   Constants.MARKUP_GRADIENT_VERTICAL + " not "
                                   + markupName);
        }

        SerializerGUI.PERCENTS.clear();
    }
}