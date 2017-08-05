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
