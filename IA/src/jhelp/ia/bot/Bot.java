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

package jhelp.ia.bot;

import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Created by jhelp on 07/07/17.
 */
public class Bot
{
    private static final String EXIT = "exit";

    public static void main(String[] args)
    {
        String input;

        do
        {
            input = UtilIO.readUserInputInConsole();
            // TODO Compute answer and print it
            Debug.todo("Compute answer and print it");
        }
        while (!Bot.EXIT.equalsIgnoreCase(input));
    }
}
