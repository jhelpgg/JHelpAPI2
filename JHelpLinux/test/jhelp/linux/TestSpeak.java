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

import jhelp.util.debug.Debug;

public class TestSpeak
{
    public static void main(String[] arguments)
    {
        try
        {
            int result = LinuxCommand.speak(PicoLanguage.DEUTCH_DEUTCH, "Ich liebe dich!");
            Debug.information("DEUTCH_DEUTCH:", result);
            result = LinuxCommand.speak(PicoLanguage.ENGLISH_GREAT_BRITAN, "Hello world!");
            Debug.information("ENGLISH_GREAT_BRITAN:", result);
            result = LinuxCommand.speak(PicoLanguage.ENGLISH_USA, "Hello every body!");
            Debug.information("ENGLISH_USA:", result);
            result = LinuxCommand.speak(PicoLanguage.ESPANA_ESPANA, "Si senior");
            Debug.information("ESPANA_ESPANA:", result);
            result = LinuxCommand.speak(PicoLanguage.ITALIAN_ITALY, "Coccelina");
            Debug.information("ITALIAN_ITALY:", result);
            result = LinuxCommand.speak(PicoLanguage.FRANCAIS_FRANCE, "Salut les gars!");
            Debug.information("FRANCAIS_FRANCE:", result);
        }
        catch (Exception e)
        {
            Debug.exception(e, "Issue while speaking!");
        }
    }
}
