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
package jhelp.util.gui.alphabet;

import com.sun.istack.internal.NotNull;

/**
 * Alphabet with blue characters size 16x16
 *
 * @author JHelp
 */
public final class AlphabetBlue16x16
        extends AlphabetUnique
{
    /**
     * Characters order in the alphabet
     */
    private static final char[]            CHARACTERS_ORDER   =
            {
                    ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.',
                    '/',//
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ' ', ' ', ' ', ' ',
                    '?',//
                    '©', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                    'O',//
                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^',
                    '_',//
                    '`', '~', 'ø', ' ', Alphabet.CANCEL, Alphabet.INFINITE, '@', '®', Alphabet.HART,
                    Alphabet.DELETE_BACK, Alphabet.RETURN, Alphabet.END, '…', '{', '|',
                    '}'
            };
    /**
     * Alphabet with blue characters size 16x16 singleton
     */
    public static final  AlphabetBlue16x16 ALPHABET_BLUE16X16 = new AlphabetBlue16x16();

    /**
     * Create a new instance of AlphabetBlue16x16
     */
    private AlphabetBlue16x16()
    {
        super(16, 16, false);
    }

    /**
     * Characters order in the alphabet <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Characters order in the alphabet
     * @see AlphabetUnique#charactersOrder()
     */
    @Override
    protected @NotNull char[] charactersOrder()
    {
        return AlphabetBlue16x16.CHARACTERS_ORDER;
    }

    /**
     * Number character per line inside image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Number character per line inside image
     * @see AlphabetUnique#numberCharactersPerLine()
     */
    @Override
    protected int numberCharactersPerLine()
    {
        return 16;
    }

    /**
     * Path of alphabet image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Path of alphabet image
     * @see AlphabetUnique#resourcePath()
     */
    @Override
    protected @NotNull String resourcePath()
    {
        return "alphabetBlue16x16.png";
    }

    /**
     * Space between characters <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Space between characters
     * @see AlphabetUnique#spaceBetweenCharacters()
     */
    @Override
    protected int spaceBetweenCharacters()
    {
        return 0;
    }
}