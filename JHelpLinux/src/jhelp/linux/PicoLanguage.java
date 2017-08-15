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

package jhelp.linux;

import java.util.Locale;

public enum PicoLanguage
{
    DEUTCH_DEUTCH("de-DE"),
    ENGLISH_GREAT_BRITAN("en-GB"),
    ENGLISH_USA("en-US"),
    ESPANA_ESPANA("es-ES"),
    FRANCAIS_FRANCE("fr-FR"),
    ITALIAN_ITALY("it-IT");

    public static PicoLanguage obtainPicoLanguageFromLocale(final Locale locale)
    {
        final String language = locale.getLanguage();
        final String country  = locale.getCountry();

        if (country.length() == 0)
        {
            for (final PicoLanguage picoLanguage : PicoLanguage.values())
            {
                if (picoLanguage.language.startsWith(language))
                {
                    return picoLanguage;
                }
            }

            return PicoLanguage.ENGLISH_USA;
        }

        final String complete = language + "-" + country;
        for (final PicoLanguage picoLanguage : PicoLanguage.values())
        {
            if (picoLanguage.language.equals(complete))
            {
                return picoLanguage;
            }
        }

        for (final PicoLanguage picoLanguage : PicoLanguage.values())
        {
            if (picoLanguage.language.startsWith(language))
            {
                return picoLanguage;
            }
        }

        for (final PicoLanguage picoLanguage : PicoLanguage.values())
        {
            if (picoLanguage.language.endsWith(country))
            {
                return picoLanguage;
            }
        }

        return PicoLanguage.ENGLISH_USA;
    }

    private final String language;
    private       Locale locale;

    PicoLanguage(final String language)
    {
        this.language = language;
    }

    public String getLanguage()
    {
        return this.language;
    }

    public Locale toLocale()
    {
        if (this.locale == null)
        {
            this.locale = new Locale(this.language.substring(0, 2), this.language.substring(3, 5));
        }

        return this.locale;
    }
}
