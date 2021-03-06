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

package jhelp.util.text;

import java.util.ArrayList;
import java.util.StringTokenizer;
import jhelp.util.list.Pair;
import jhelp.util.util.Utilities;

/**
 * Cut string with separator, like {@link StringTokenizer}, but in addition it can detect Strings and not cut on them,
 * it can
 * also ignore escaped character.<br>
 * By example, you type :<br>
 * <code lang="java"><!--
 * StringExtractor extractor=new StringExtractor("Hello world ! 'This is a phrase'");
 * System.out.println(extractor.next());
 * System.out.println(extractor.next());
 * System.out.println(extractor.next());
 * System.out.println(extractor.next());
 * --></code> <br>
 * You will obtain <br>
 * <code>Hello<br>world<br>!<br>This is a phrase</code>
 *
 * @author JHelp
 */
public class StringExtractor
{
    /**
     * Indicates if empty string result is allowaed
     */
    private       boolean                               canReturnEmptyString;
    /**
     * Current word start index
     */
    private       int                                   currentWordStart;
    /**
     * Escape characters
     */
    private final char[]                                escapeCharacters;
    /**
     * Current read index
     */
    private       int                                   index;
    /**
     * String to parse length
     */
    private final int                                   length;
    /**
     * Open/close pairs, to consider like "normal" character something between an open and a close character
     */
    private final ArrayList<Pair<Character, Character>> openCloseIgnore;
    /**
     * Indicates if separators are return
     */
    private final boolean                               returnSeparators;
    /**
     * Separators characters
     */
    private final char[]                                separators;
    /**
     * Indicates if have to stop parsing when meet "string" to treat them separately {@code true} OR treat them as a
     * part of
     * something : {@code false}
     */
    private       boolean                               stopAtString;
    /**
     * String to parse
     */
    private final char[]                                string;
    /**
     * String delimiters
     */
    private final char[]                                stringLimiters;

    /**
     * Create a new instance of StringExtractor with default separators (see {@link UtilText#DEFAULT_SEPARATORS}), string
     * delimiters (see {@link UtilText#DEFAULT_STRING_LIMITERS}) and escape characters (see
     * {@link UtilText#DEFAULT_ESCAPE_CHARACTERS}). And no return of separators
     *
     * @param string String to parse
     */
    public StringExtractor(final String string)
    {
        this(string, false);
    }

    /**
     * Create a new instance of StringExtractor with default separators (see {@link UtilText#DEFAULT_SEPARATORS}), string
     * delimiters (see {@link UtilText#DEFAULT_STRING_LIMITERS}) and escape characters (see
     * {@link UtilText#DEFAULT_ESCAPE_CHARACTERS})
     *
     * @param string           String to parse
     * @param returnSeparators Indicates if return separators
     */
    public StringExtractor(final String string, final boolean returnSeparators)
    {
        this(string, UtilText.DEFAULT_SEPARATORS, UtilText.DEFAULT_STRING_LIMITERS, UtilText.DEFAULT_ESCAPE_CHARACTERS,
             returnSeparators);
    }

    /**
     * Create a new instance of StringExtractor
     *
     * @param string           String to parse
     * @param separators       Separators list
     * @param stringLimiters   String delimiters
     * @param escapeCharacters Escape characters
     * @param returnSeparators Indicates if return separators
     */
    public StringExtractor(
            final String string, final String separators, final String stringLimiters,
            final String escapeCharacters,
            final boolean returnSeparators)
    {
        this.string = string.toCharArray();
        this.separators = separators.toCharArray();
        this.stringLimiters = stringLimiters.toCharArray();
        this.escapeCharacters = escapeCharacters.toCharArray();
        this.returnSeparators = returnSeparators;

        this.index = 0;
        this.currentWordStart = -1;
        this.length = string.length();

        this.openCloseIgnore = new ArrayList<>();
        this.canReturnEmptyString = true;
        this.stopAtString = true;
    }

    /**
     * Create a new instance of StringExtractor with no return separators
     *
     * @param string           String to parse
     * @param separators       Separators list
     * @param stringLimiters   String delimiters
     * @param escapeCharacters Escape characters
     */
    public StringExtractor(
            final String string, final String separators, final String stringLimiters,
            final String escapeCharacters)
    {
        this(string, separators, stringLimiters, escapeCharacters, false);
    }

    /**
     * Add a open close pairs, to consider like "normal" character something between an open and a close character
     *
     * @param open  Open character
     * @param close Close character
     */
    public void addOpenCloseIgnore(final char open, final char close)
    {
        if (open == close)
        {
            throw new IllegalArgumentException("Open and close can't have same value");
        }

        for (final Pair<Character, Character> openClose : this.openCloseIgnore)
        {
            if ((openClose.first == open) || (openClose.second == open) || (openClose.first == close) ||
                (openClose.second == close))
            {
                throw new IllegalArgumentException("Open or close is already used !");
            }
        }

        this.openCloseIgnore.add(new Pair<>(open, close));
    }

    public int currentWordEnd()
    {
        return this.index;
    }

    public int currentWordStart()
    {
        return this.currentWordStart;
    }

    /**
     * Indicates if empty strings may be return
     *
     * @return Indicates if empty strings may be return
     */
    public boolean isCanReturnEmptyString()
    {
        return this.canReturnEmptyString;
    }

    /**
     * Change the possibility to return empty string
     *
     * @param canReturnEmptyString Use {@code true} to allow empty strings, {@code false} to forbid them
     */
    public void setCanReturnEmptyString(final boolean canReturnEmptyString)
    {
        this.canReturnEmptyString = canReturnEmptyString;
    }

    /**
     * Indicates if have to stop parsing when meet "string" to treat them separately {@code true} OR treat them as a
     * part of
     * something : {@code false}
     *
     * @return {@code true} if have to stop parsing when meet "string" to treat them separately
     */
    public boolean isStopAtString()
    {
        return this.stopAtString;
    }

    /**
     * Change the way to treat "string" : stop parsing when meet "string" to treat them separately {@code true} OR treat
     * them as
     * a part of something : {@code false}
     *
     * @param stopAtString New way to treat "string"
     */
    public void setStopAtString(final boolean stopAtString)
    {
        this.stopAtString = stopAtString;
    }

    /**
     * Next extracted string.<br>
     * It can be a separator if you ask for return them.<br>
     * It returns {@code null} if no more string to extract
     *
     * @return Next part or {@code null} if no more to extract
     */
    public String next()
    {
        this.currentWordStart = this.index;

        if (this.index >= this.length)
        {
            return null;
        }

        boolean                    insideString         = false;
        int                        start                = this.index;
        int                        end                  = this.length;
        char                       currentStringLimiter = ' ';
        Pair<Character, Character> openClose            = null;
        char                       character            = this.string[this.index];

        do
        {
            if (openClose == null)
            {
                for (final Pair<Character, Character> openClos : this.openCloseIgnore)
                {
                    if (openClos.first == character)
                    {
                        openClose = openClos;
                        break;
                    }
                }

                if (openClose != null)
                {
                    if (Utilities.contains(character, this.separators))
                    {
                        if (start < this.index)
                        {
                            end = this.index;

                            break;
                        }
                    }
                }
            }

            if (openClose == null)
            {
                if (Utilities.contains(character, this.escapeCharacters))
                {
                    this.index++;
                }
                else if (insideString)
                {
                    if (currentStringLimiter == character)
                    {
                        insideString = false;

                        if (this.stopAtString)
                        {
                            end = this.index;
                            this.index++;
                            break;
                        }
                    }
                }
                else if (Utilities.contains(character, this.stringLimiters))
                {
                    if ((start < this.index) && (this.stopAtString))
                    {
                        end = this.index;

                        break;
                    }

                    if (this.stopAtString)
                    {
                        start++;
                    }

                    insideString = true;
                    currentStringLimiter = character;
                }
                else if (Utilities.contains(character, this.separators))
                {
                    if (start < this.index)
                    {
                        end = this.index;

                        break;
                    }

                    if (this.returnSeparators)
                    {
                        end = start + 1;
                        this.index++;

                        break;
                    }

                    start++;
                }
            }
            else if (character == openClose.second)
            {
                openClose = null;
            }

            this.index++;

            if (this.index < this.length)
            {
                character = this.string[this.index];
            }
        }
        while (this.index < this.length);

        if ((!this.canReturnEmptyString) && (end == start))
        {
            return this.next();
        }

        return new String(this.string, start, end - start);
    }
}