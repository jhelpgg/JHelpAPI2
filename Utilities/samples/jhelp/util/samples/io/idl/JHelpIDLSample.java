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

package jhelp.util.samples.io.idl;

import jhelp.util.debug.Debug;
import jhelp.util.io.idl.JHelpIDL;
import jhelp.util.text.UtilText;

public class JHelpIDLSample
        implements TestInterface
{

    /**
     * TODO Explains what does the method main in jhelp.util.samples.io.idl [JHelpUtil]
     *
     * @param args
     */
    public static void main(final String[] args)
    {
        try
        {
            JHelpIDL.declareIDLs(TestInterface.class);

            JHelpIDL.registerReceiver(new JHelpIDLSample(), TestInterface.class);
            final TestInterface sender = JHelpIDL.obtainSenderInstance(TestInterface.class);

            final byte[] array =
                    {
                            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                    };
            String   result;
            TestEnum testEnum = TestEnum.T1;
            for (int i = 0; i < 10; i++)
            {
                array[0] = (byte) (i & 0xFF);
                result = sender.test(i, "String" + i, array);

                Debug.verbose("result=", result);

                Debug.verbose("BEFORE : ", testEnum);
                testEnum = sender.next(testEnum);
                Debug.verbose("AFTER : ", testEnum);

                Debug.verbose("Bin=", sender.obtainTestBinarizable(i));
            }

            JHelpIDL.stopReceiver(TestInterface.class);
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }
    }

    @Override
    public TestEnum next(final TestEnum actual)
    {
        switch (actual)
        {
            case T1:
                return TestEnum.T2;
            case T2:
                return TestEnum.T3;
            case T3:
                return TestEnum.T1;
        }

        return actual;
    }

    @Override
    public TestBinarizable obtainTestBinarizable(final int integer)
    {
        return new TestBinarizable(integer * integer);
    }

    @Override
    public String test(final int integer, final String string, final byte[] array)
    {
        return UtilText.concatenate("integer=", integer, " string=", string, " array=", array);
    }
}