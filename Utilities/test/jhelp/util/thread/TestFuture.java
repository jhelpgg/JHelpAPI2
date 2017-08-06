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

package jhelp.util.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.debug.Debug;
import jhelp.util.test.UtilTest;
import jhelp.util.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 29/06/17.
 */
public class TestFuture
{
    @Test
    public void testAndAllSucceed()
    {
        Future<Integer> future = Future.and(Future.of(1), Future.of(2), Future.of(3))
                                       .andDo(list -> list.stream().mapToInt(i -> i).sum());

        try
        {
            Assert.assertEquals(6, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testAndOneFail()
    {
        Future<Integer> future = Future.and(Future.of(1), UtilTest.errorFuture(), Future.of(3))
                                       .andDo(list -> list.stream().mapToInt(i -> i).sum());

        try
        {
            future.get();
            Assert.fail("Expected exception!");
        }
        catch (TaskException ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testLaunch()
    {
        Future<Integer> future = Future.launch(number -> number + 1, 73);

        try
        {
            Assert.assertEquals(74, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testLinkAfter()
    {
        Future<Integer> longTime = UtilTest.waitFuture(100000, 42);
        Future<Integer> toCancel = longTime.thenDoLink(future ->
                                                       {
                                                           try
                                                           {
                                                               return future.get();
                                                           }
                                                           catch (TaskException ignored)
                                                           {
                                                               return 666;
                                                           }
                                                       });
        toCancel.requestCancel("For test");
        Utilities.sleep(1000);
        Assert.assertEquals(FutureStatus.CANCELLED, longTime.status());
    }

    @Test
    public void testOf()
    {
        Future<Integer> future = Future.of(73);

        try
        {
            Assert.assertEquals(73, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testOnError()
    {
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        Future<?> future = UtilTest.errorFuture(500)
                                   .onError(ignored -> onErrorCalled.set(true))
                                   .andConsume(ignored ->
                                               {
                                               });

        try
        {
            future.get();
            Assert.fail("Expected exception!");
        }
        catch (TaskException ignored)
        {
            //That's what we want
        }

        Utilities.sleep(100);
        Assert.assertTrue(onErrorCalled.get());
    }

    @Test
    public void testOnFinished()
    {
        Future<Integer> future = UtilTest.waitFuture(500, 42)
                                         .thenDo(fut ->
                                                 {
                                                     try
                                                     {
                                                         return -fut.get();
                                                     }
                                                     catch (TaskException e)
                                                     {
                                                         return 666;
                                                     }
                                                 });

        try
        {
            Assert.assertEquals(-42, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }

        Future<?> future2 = UtilTest.errorFuture(500)
                                    .thenDo(fut ->
                                            {
                                                try
                                                {
                                                    return fut.get();
                                                }
                                                catch (TaskException e)
                                                {
                                                    return 666;
                                                }
                                            });

        try
        {
            Assert.assertEquals(666, future2.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }

        future2 = UtilTest.canceledFuture(500)
                          .thenDo(fut ->
                                  {
                                      try
                                      {
                                          return fut.get();
                                      }
                                      catch (TaskException e)
                                      {
                                          return 666;
                                      }
                                  });

        try
        {
            Assert.assertEquals(666, future2.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testOnResult()
    {
        AtomicBoolean onErrorCalled = new AtomicBoolean(false);
        Future<Integer> future = UtilTest.waitFuture(500, 42)
                                         .onError(ignored -> onErrorCalled.set(true))
                                         .andDo(number -> -number);

        try
        {
            Assert.assertEquals(-42, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }

        Assert.assertFalse(onErrorCalled.get());
    }

    @Test
    public void testOrAllFailed()
    {
        Future<Integer> future = Future.or(UtilTest.errorFuture(), UtilTest.errorFuture());

        try
        {
            future.get();
            Assert.fail("Expected exception!");
        }
        catch (TaskException ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testOrAllSucceed()
    {
        Future<Integer> future = Future.or(UtilTest.waitFuture(666, 666), UtilTest.waitFuture(42, 42));

        try
        {
            Assert.assertEquals(42, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testOrFilteredAllFailed()
    {
        Future<Integer> future = Future.or(number -> (number & 1) == 0, UtilTest.errorFuture(), UtilTest.errorFuture());

        try
        {
            future.get();
            Assert.fail("Expected exception!");
        }
        catch (TaskException ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testOrFilteredAllSucceed()
    {
        Future<Integer> future = Future.or(number -> (number & 1) == 0,
                                           UtilTest.waitFuture(666, 666),
                                           UtilTest.waitFuture(73, 73));

        try
        {
            Assert.assertEquals(666, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testOrFilteredOneSucceed()
    {
        Future<Integer> future = Future.or(number -> (number & 1) == 0,
                                           UtilTest.waitFuture(666, 666),
                                           UtilTest.errorFuture());

        try
        {
            Assert.assertEquals(666, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testOrOneSucceed()
    {
        Future<Integer> future = Future.or(UtilTest.waitFuture(666, 666), UtilTest.errorFuture());

        try
        {
            Assert.assertEquals(666, (int) future.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testStatus()
    {
        Assert.assertEquals(FutureStatus.COMPUTING, UtilTest.waitFuture(2000, 1).status());
        Assert.assertEquals(FutureStatus.RESULT, Future.of(42).status());
        Assert.assertEquals(FutureStatus.CANCELLED, UtilTest.canceledFuture().status());
        Assert.assertEquals(FutureStatus.ERROR, UtilTest.errorFuture().status());
    }

    @Test
    public void testUnwrap()
    {
        Future<Future<Integer>> futureFuture = Future.of(Future.of(73));
        Future<Integer>         unwraped     = Future.unwrap(futureFuture);

        try
        {
            Assert.assertEquals(73, (int) unwraped.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testUnwrapMax()
    {
        Future<Future<Future<Future<Future<Integer>>>>> bigFuture =
                Future.of(Future.of(Future.of(Future.of(Future.of(73)))));
        Future<Integer> unwraped = (Future<Integer>) Future.unwrapMax(bigFuture);

        try
        {
            Assert.assertEquals(73, (int) unwraped.get());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }
}
