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

package jhelp.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import jhelp.util.list.SortedArray;
import jhelp.util.text.regex.RegularExpression;

public class BotFile
{
    private static final Pattern WORD_REGEX = RegularExpression.WORD.pattern();

    public static boolean validWord(String string)
    {
        return string != null && BotFile.WORD_REGEX.matcher(string).matches();
    }

    private final Map<String, SortedArray<String>> synonyms;

    public BotFile()
    {
        this.synonyms = new HashMap<>();
    }

    public boolean areSynonym(String word1, String word2)
    {
        if (!BotFile.validWord(word1) || !BotFile.validWord(word2))
        {
            return false;
        }

        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        if (word1.equals(word2))
        {
            return true;
        }

        SortedArray<String> synonyms = this.synonyms.get(word1);

        if (synonyms != null && synonyms.contains(word2))
        {
            return true;
        }

        synonyms = this.synonyms.get(word2);

        return synonyms != null && synonyms.contains(word1);
    }

    public boolean synonym(String reference, String synonym)
    {
        Objects.requireNonNull(reference, "reference MUST NOT be null!");
        Objects.requireNonNull(synonym, "synonym MUST NOT be null!");

        if (!BotFile.validWord(reference))
        {
            throw new IllegalArgumentException("reference \"" + reference + "\" not a valid word");
        }

        if (!BotFile.validWord(synonym))
        {
            throw new IllegalArgumentException("synonym \"" + synonym + "\" not a valid word");
        }

        reference = reference.toLowerCase();
        synonym = synonym.toLowerCase();
        SortedArray<String> synonyms = this.synonyms.get(reference);

        if (synonyms == null)
        {
            synonyms = new SortedArray<>(String.class, true);
            this.synonyms.put(reference, synonyms);
        }

        return synonyms.add(synonym);
    }
}
