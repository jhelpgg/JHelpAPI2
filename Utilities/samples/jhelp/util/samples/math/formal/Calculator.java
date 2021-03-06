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

package jhelp.util.samples.math.formal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import jhelp.util.debug.Debug;
import jhelp.util.math.formal.Function;

/**
 * Example of use {@link Function} as a calculator.<br>
 * If you want compute the derived on x, start by @. By example you want compute the derived of sin(x), just type : @ sin(x) <br>
 * Else for ordinary operation, just type things like : 14-85 OR 45+23%, OR x-y+x OR ...<br>
 * For exit just use the key word : exit
 *
 * @author JHelp
 */
public class Calculator
{
    /**
     * Launch the calculator
     *
     * @param args Unused
     */
    public static void main(final String[] args)
    {
        try
        {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String               line           = bufferedReader.readLine();
            Function             function;

            while ((line != null) && ("exit".equalsIgnoreCase(line.trim()) == false))
            {
                line = line.trim();

                if (line.startsWith("@") == true)
                {
                    function = Function.parse(line.substring(1)).derive("x");
                }
                else
                {
                    function = Function.parse(line);
                }

                System.out.println(function.simplifyMaximum(System.out));

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }
    }
}