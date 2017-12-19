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
package jhelp.gui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jhelp.util.text.UtilText;

/**
 * Filter for String content that use a regular expression
 *
 * @author JHelp <br>
 */
public final class FilterText
        implements Filter<String>
{
    /**
     * Indicates id accent are important
     */
    private       boolean                      accentSensitive;
    /**
     * Indicates if case sensitive
     */
    private       boolean                      caseSensitive;
    /**
     * Listener of filter changes
     */
    private final List<FilterListener<String>> listeners;
    /**
     * Pattern to validate
     */
    private       Pattern                      pattern;

    /**
     * Create a new instance of FilterText that accept all String
     */
    public FilterText()
    {
        this.listeners = new ArrayList<FilterListener<String>>();
        this.caseSensitive = true;
        this.accentSensitive = true;
    }

    /**
     * Signal to listeners that filter changed.<br>
     * Not same String are filtered
     */
    protected void fireFilterChange()
    {
        synchronized (this.listeners)
        {
            for (final FilterListener<String> listener : this.listeners)
            {
                listener.filterChange(this);
            }
        }
    }

    /**
     * Make the filter accept all String
     */
    public void acceptAll()
    {
        this.regex(null);
    }

    /**
     * Indicates if given String is filtered.<br>
     * That is to say respect the regular expression <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param string Tested String
     * @return <code>true</code> if given String is filtered
     * @see Filter#filtered(Object)
     */
    @Override
    public boolean filtered(String string)
    {
        if (this.pattern == null)
        {
            return true;
        }

        if (!this.caseSensitive)
        {
            string = string.toLowerCase();
        }

        if (!this.accentSensitive)
        {
            string = UtilText.removeAccent(string);
        }

        return this.pattern.matcher(string).matches();
    }

    /**
     * Register listener to filter changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param listener Listener to change
     * @see Filter#registerFilterListener(jhelp.gui.model.FilterListener)
     */
    @Override
    public void registerFilterListener(final FilterListener<String> listener)
    {
        if (listener == null)
        {
            return;
        }

        synchronized (this.listeners)
        {
            if (!this.listeners.contains(listener))
            {
                this.listeners.add(listener);
            }
        }
    }

    /**
     * Unregister a listener from filter changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param listener Listener to unregister
     * @see Filter#unregisterFilterListener(jhelp.gui.model.FilterListener)
     */
    @Override
    public void unregisterFilterListener(final FilterListener<String> listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(listener);
        }
    }

    /**
     * Change the regular expression.<br>
     * The given regular expression must be valid, see {@link Pattern} documentation to build a valid regular expression
     *
     * @param regex Regular expression OR <code>null</code> for accept all strings
     * @throws PatternSyntaxException If regular expression not valid
     */
    public void regex(final String regex)
    {
        this.regex(regex, true, true);
    }

    /**
     * Change the regular expression.<br>
     * The given regular expression must be valid, see {@link Pattern} documentation to build a valid regular expression<br>
     * If <code>caseSensitive</code> is false, consider the text is put in lower case before test match with regular expression,
     * so use only lower case in regular expression (A-Z will not match).
     *
     * @param regex         Regular expression OR <code>null</code> for accept all strings
     * @param caseSensitive Indicates if case sensitive (If not, text will put lower case)
     * @throws PatternSyntaxException If regular expression not valid
     */
    public void regex(final String regex, final boolean caseSensitive)
    {
        this.regex(regex, caseSensitive, caseSensitive);
    }

    /**
     * Define the regular expression.<br>
     * If given <code>regular expression</code> is null, the filter will accept all texts.<br>
     * If <code>caseSensitive</code> is false, consider the text is put in lower case before test match with regular expression,
     * so use only lower case regular expression (A-Z will not match).<br>
     * If <code>accentSensitve</code> is false, accent are removed before test matching, so don't include them in your regular
     * expression because they will never match<br>
     * The given regular expression must be valid, see {@link Pattern} documentation to build a valid regular expression
     *
     * @param regex          Regular expression to verify. {@code null} for match all
     * @param caseSensitive  Indicates if case sensitive (If not, text will put lower case)
     * @param accentSensitve Indicates if accent sensitive (If not, accents will be remove from text)
     * @throws PatternSyntaxException If regular expression not valid
     */
    public void regex(final String regex, final boolean caseSensitive, final boolean accentSensitve)
    {
        if (regex == null)
        {
            this.pattern = null;
            this.caseSensitive = true;
            this.accentSensitive = true;
        }
        else
        {
            this.pattern = Pattern.compile(regex);
            this.caseSensitive = caseSensitive;
            this.accentSensitive = accentSensitve;
        }

        this.fireFilterChange();
    }
}