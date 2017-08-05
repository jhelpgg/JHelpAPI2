package jhelp.util.test;

import jhelp.util.thread.Future;
import jhelp.util.thread.Promise;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.TaskException;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;
import org.junit.Assert;

/**
 * Created by jhelp on 29/06/17.
 */
public class UtilTest
{
    public static <R> Future<R> canceledFuture()
    {
        Promise<R> promise = new Promise<>();
        promise.cancel("Cancelled by purpose");
        return promise.future();
    }

    public static <R> Future<R> canceledFuture(long waiting)
    {
        Promise<R> promise = new Promise<>();
        ThreadManager.parallel(() -> promise.cancel("Cancelled by purpose"), waiting);
        return promise.future();
    }

    public static <R> Future<R> errorFuture()
    {
        Promise<R> promise = new Promise<>();
        promise.setError(new TaskException("Expected exception."));
        return promise.future();
    }

    public static <R> Future<R> errorFuture(long waiting)
    {
        Promise<R> promise = new Promise<>();
        ThreadManager.parallel(() -> promise.setError(new TaskException("Expected exception.")), waiting);
        return promise.future();
    }

    /**
     * Test if an expression throw an exception
     *
     * @param message         Message if exception not throw
     * @param expressionThrow Expression that should throw
     */
    public static void testException(String message, RunnableTask expressionThrow)
    {
        try
        {
            expressionThrow.run();
            Assert.fail("Must throw an exception! " + message);
        }
        catch (Exception exception)
        {
            //That what we want
        }
    }

    public static Future<Integer> waitFuture(long waiting, int answer)
    {
        return Future.launchProduce(() ->
                                    {
                                        Utilities.sleep(waiting);
                                        return answer;
                                    });
    }
}
