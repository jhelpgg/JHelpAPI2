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

package jhelp.gui.code;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.util.list.Pair;
import jhelp.util.text.UtilText;

/**
 * Descriptor of a language.<br>
 * It defines keywords, comments, ...<br>
 * Use {@link JHelpAutoStyledTextArea#describeLanguage(LanguageDescriptor)} to apply it
 */
public class LanguageDescriptor
{
    /**
     * Rules descriptors
     */
    private final HashMap<Rules, List<Pair<Pattern, Integer>>> descriptors;

    /**
     * Create language descriptor
     */
    public LanguageDescriptor()
    {
        this.descriptors = new HashMap<>();
    }

    /**
     * Add the description to a {@link JHelpAutoStyledTextArea}<br>
     * The previous description on {@link JHelpAutoStyledTextArea} aren't remove, this is just an addition<br>
     * For having only this description use {@link JHelpAutoStyledTextArea#describeLanguage(LanguageDescriptor)}
     *
     * @param autoStyledTextArea {@link JHelpAutoStyledTextArea} to add the description
     */
    public final void addToAutoStyledText(@NotNull JHelpAutoStyledTextArea autoStyledTextArea)
    {
        String name;

        for (Map.Entry<Rules, List<Pair<Pattern, Integer>>> entry : this.descriptors.entrySet())
        {
            name = entry.getKey()
                        .name();

            for (Pair<Pattern, Integer> pair : entry.getValue())
            {
                autoStyledTextArea.associate(name, pair.first, pair.second);
            }
        }
    }

    /**
     * Associate some key words to a rule
     *
     * @param rules    Rule to associate
     * @param keyWords Key words associated to the rule
     */
    public final void associate(final @NotNull Rules rules, final @NotNull String... keyWords)
    {
        if (rules == null)
        {
            throw new NullPointerException("rules MUST NOT be null !");
        }

        if (rules == Rules.DEFAULT)
        {
            return;
        }

        List<Pair<Pattern, Integer>> patterns = this.descriptors.get(rules);

        if (patterns == null)
        {
            patterns = new ArrayList<Pair<Pattern, Integer>>();
            this.descriptors.put(rules, patterns);
        }

        for (final String keyWord : keyWords)
        {
            // We want get the word if it is alone, so we have to check if it in middle a word or not.
            // That's why we add something before and something after.
            // But we want apply style on only the second thing between parenthesis : the second group (Remember put
            // something between parenthesis in regular expression make a capturing group)
            // So we precise to take care the second group for style
            patterns.add(new Pair<Pattern, Integer>(
                    Pattern.compile(UtilText.concatenate("([^a-zA-Z0-9_]|^)(", //
                                                         Pattern.quote(keyWord), //
                                                         ")([^a-zA-Z0-9_]|$)")), //
                    2));
        }
    }

    /**
     * Associate a regular expression to a rule
     *
     * @param rules   Rule to associate
     * @param pattern Regular expression
     * @param group   Group, in regular expression, to apply decoration
     */
    public final void associate(@NotNull Rules rules, @NotNull Pattern pattern, int group)
    {
        if (rules == null)
        {
            throw new NullPointerException("rules MUST NOT be null !");
        }

        if (pattern == null)
        {
            throw new NullPointerException("pattern MUST NOT be null !");
        }

        if (rules == Rules.DEFAULT)
        {
            return;
        }

        List<Pair<Pattern, Integer>> patterns = this.descriptors.get(rules);

        if (patterns == null)
        {
            patterns = new ArrayList<Pair<Pattern, Integer>>();
            this.descriptors.put(rules, patterns);
        }

        patterns.add(new Pair<Pattern, Integer>(pattern, group));
    }
}
