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

package jhelp.util.debug;

import java.io.PrintStream;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by jhelp on 11/06/17.
 */
public class TestDebug
{
    private static PrintStream err;
    private static PrintStream out;
    private static PrintSpy    spyErr;
    private static PrintSpy    spyOut;

    @BeforeClass
    public static void initialize()
    {
        TestDebug.out = System.out;
        TestDebug.err = System.err;
        TestDebug.spyOut = new PrintSpy(TestDebug.out);
        System.setOut(TestDebug.spyOut);
        TestDebug.spyErr = new PrintSpy(TestDebug.err);
        System.setErr(TestDebug.spyErr);
    }

    @AfterClass
    public static void tearDown()
    {
        System.setOut(TestDebug.out);
        System.setErr(TestDebug.err);
    }

    @Test
    public void testDebugLevel()
    {
        DebugLevel debugLevel = Debug.getDebugLevel();

        Debug.setLevel(DebugLevel.VERBOSE);
        Debug.verbose("Verbose message");
        Assert.assertFalse("VERBOSE:Verbose: Something should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.debug("Debug message");
        Assert.assertFalse("VERBOSE:Debug: Something should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.information("Information message");
        Assert.assertFalse("VERBOSE:Information: Something should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.warning("Warning message");
        Assert.assertFalse("VERBOSE:Warning: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());
        Debug.error("Error message");
        Assert.assertFalse("VERBOSE:Error: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());

        Debug.setLevel(DebugLevel.DEBUG);
        Debug.verbose("Verbose message");
        Assert.assertTrue("DEBUG:Verbose: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.debug("Debug message");
        Assert.assertFalse("DEBUG:Debug: Something should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.information("Information message");
        Assert.assertFalse("DEBUG:Information: Something should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.warning("Warning message");
        Assert.assertFalse("DEBUG:Warning: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());
        Debug.error("Error message");
        Assert.assertFalse("DEBUG:Error: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());

        Debug.setLevel(DebugLevel.INFORMATION);
        Debug.verbose("Verbose message");
        Assert.assertTrue("INFORMATION:Verbose: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.debug("Debug message");
        Assert.assertTrue("INFORMATION:Debug: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.information("Information message");
        Assert.assertFalse("INFORMATION:Information: Something should be write",
                           TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.warning("Warning message");
        Assert.assertFalse("INFORMATION:Warning: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());
        Debug.error("Error message");
        Assert.assertFalse("INFORMATION:Error: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());

        Debug.setLevel(DebugLevel.WARNING);
        Debug.verbose("Verbose message");
        Assert.assertTrue("WARNING:Verbose: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.debug("Debug message");
        Assert.assertTrue("WARNING:Debug: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.information("Information message");
        Assert.assertTrue("WARNING:Information: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.warning("Warning message");
        Assert.assertFalse("WARNING:Warning: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());
        Debug.error("Error message");
        Assert.assertFalse("WARNING:Error: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());

        Debug.setLevel(DebugLevel.ERROR);
        Debug.verbose("Verbose message");
        Assert.assertTrue("FAILED:Verbose: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.debug("Debug message");
        Assert.assertTrue("FAILED:Debug: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.information("Information message");
        Assert.assertTrue("FAILED:Information: Nothing should be write", TestDebug.spyOut.popLastWrote().isEmpty());
        Debug.warning("Warning message");
        Assert.assertTrue("FAILED:Warning: Nothing should be write", TestDebug.spyErr.popLastWrote().isEmpty());
        Debug.error("Error message");
        Assert.assertFalse("FAILED:Error: Something should be write", TestDebug.spyErr.popLastWrote().isEmpty());

        Debug.setLevel(debugLevel);
    }

    @Test
    public void testDebugType()
    {
        DebugLevel debugLevel = Debug.getDebugLevel();
        Debug.setLevel(DebugLevel.VERBOSE);

        Debug.debug("Debug message");
        String message = TestDebug.spyOut.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Debug message", message.contains("Debug message"));
        Debug.error("Error message");
        message = TestDebug.spyErr.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Error message", message.contains("Error message"));
        Debug.exception(new Throwable("Exception test"), "Exception message");
        message = TestDebug.spyErr.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Exception message", message.contains("Exception message"));
        Debug.information("Information message");
        message = TestDebug.spyOut.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Information message", message.contains("Information message"));
        Debug.mark("Mark message");
        message = TestDebug.spyOut.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Mark message", message.contains("Mark message"));
        Debug.trace("Trace message");
        message = TestDebug.spyOut.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Trace message", message.contains("Trace message"));
        Debug.verbose("Verbose message");
        message = TestDebug.spyOut.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Verbose message", message.contains("Verbose message"));
        Debug.warning("Warning message");
        message = TestDebug.spyErr.popLastWrote();
        Assert.assertTrue(message + " NOT CONTAINS Warning message", message.contains("Warning message"));

        Debug.setLevel(debugLevel);
    }
}
