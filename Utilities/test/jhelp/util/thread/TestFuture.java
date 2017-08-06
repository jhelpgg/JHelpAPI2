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

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.debug.Debug;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.SizedIterable;
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
    public void testAndCombine()
    {
        Future<Integer> future = UtilTest.waitFuture(100, 100)
                                         .andCombine(integer -> UtilTest.waitFuture(integer, integer + 1));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testAndCombineLink()
    {
        Future<Integer> future = UtilTest.waitFuture(100, 100)
                                         .andCombineLink(integer -> UtilTest.waitFuture(integer, integer + 1));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testAndConsume()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .andConsume(pointer::data);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(10, (int) pointer.data());
    }

    @Test
    public void testAndConsumeLink()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .andConsumeLink(pointer::data);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(10, (int) pointer.data());
    }

    @Test
    public void testAndDoOnCancelledFuture()
    {
        Future<Integer> future = UtilTest.canceledFuture();
        future = future.andDo(integer -> integer);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.CANCELLED, future.status());
    }

    @Test
    public void testAndEmptyList()
    {
        Future<List<String>> future = Future.and();

        try
        {
            List<String> list = future.get();
            Assert.assertEquals(0, list.size());
        }
        catch (TaskException taskException)
        {
            Debug.exception(taskException);
            Assert.fail("Unexpected exception: " + taskException);
        }
    }

    @Test
    public void testAndOneCancelled()
    {
        Future<List<String>> future = Future.and(Future.of("RER"), UtilTest.canceledFuture());

        try
        {
            future.get();
            Assert.fail("Should be on cancel");
        }
        catch (TaskException taskException)
        {
            //That's what we want
        }

        Assert.assertEquals(FutureStatus.CANCELLED, future.status());
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
    public void testAndProduce()
    {
        Future<Integer> future = UtilTest.waitFuture(10, 100)
                                         .andProduce(() -> 101);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testAndProduceLink()
    {
        Future<Integer> future = UtilTest.waitFuture(10, 100)
                                         .andProduceLink(() -> 101);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testAndRun()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .andRun(() -> pointer.data(11));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(11, (int) pointer.data());
    }

    @Test
    public void testAndRunLink()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .andRunLink(() -> pointer.data(11));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(11, (int) pointer.data());
    }

    @Test
    public void testConsume()
    {
        final AtomicBoolean consumed = new AtomicBoolean(false);
        Future<Void>        future   = Future.launchConsume(ignored -> consumed.set(true), "");
        future.waitFinish();
        Assert.assertTrue(consumed.get());
    }

    @Test
    public void testFirstCollectionMatchFound()
    {
        List<Integer>   list   = Utilities.toList(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer> future = Future.firstMatch(list, number -> number == 42);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(42, (int) future.value());
    }

    @Test
    public void testFirstCollectionMatchNotFound()
    {
        List<Integer>   list   = Utilities.toList(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer> future = Future.firstMatch(list, number -> number < 0);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.ERROR, future.status());
    }

    @Test
    public void testFirstIterableMatchFound()
    {
        List<Integer>   list   = Utilities.toList(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer> future = Future.firstMatch((Iterable<Integer>) list, number -> number == 42);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(42, (int) future.value());
    }

    @Test
    public void testFirstIterableMatchNotFound()
    {
        List<Integer>   list   = Utilities.toList(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer> future = Future.firstMatch((Iterable<Integer>) list, number -> number < 0);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.ERROR, future.status());
    }

    @Test
    public void testFirstSizedIterableMatchFound()
    {
        ArrayObject<Integer> list   = Utilities.toArrayObject(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer>      future = Future.firstMatch((SizedIterable<Integer>) list, number -> number == 42);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(42, (int) future.value());
    }

    @Test
    public void testFirstSizedIterableMatchNotFound()
    {
        ArrayObject<Integer> list   = Utilities.toArrayObject(5, 8, 42, 98, 73, 3, 123, 128, 1);
        Future<Integer>      future = Future.firstMatch((SizedIterable<Integer>) list, number -> number < 0);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.ERROR, future.status());
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
    public void testOfNull()
    {
        Future<String> future = Future.of(null);
        Assert.assertNull(future.value());
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
    public void testOrEmpty()
    {
        Future<String> future = Future.or();
        Assert.assertNull(future.value());

        future = Future.or(ignored -> true);
        Assert.assertNull(future.value());
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
    public void testOrFilteredNoneSucceed()
    {
        Future<Integer> future = Future.or(number -> number < 0,
                                           UtilTest.waitFuture(666, 666),
                                           UtilTest.waitFuture(73, 73));

        future.waitFinish();
        Assert.assertEquals(FutureStatus.ERROR, future.status());
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
    public void testRequestCancel()
    {
        Future<Integer> future = UtilTest.waitFuture(1000, 100)
                                         .andDo(integer -> -integer);
        future.requestCancel("Don't want to wait!");
        future.waitFinish();
        Assert.assertEquals(FutureStatus.CANCELLED, future.status());
    }

    @Test
    public void testRun()
    {
        final AtomicBoolean ran    = new AtomicBoolean(false);
        Future<Void>        future = Future.launchRun(() -> ran.set(true));
        future.waitFinish();
        Assert.assertTrue(ran.get());
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
    public void testThenCombine()
    {
        Future<Integer> future = UtilTest.waitFuture(100, 100)
                                         .thenCombine(
                                                 integer -> UtilTest.waitFuture(integer.value(), integer.value() + 1));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testThenCombineLink()
    {
        Future<Integer> future = UtilTest.waitFuture(100, 100)
                                         .thenCombineLink(
                                                 integer -> UtilTest.waitFuture(integer.value(), integer.value() + 1));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testThenConsume()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .thenConsume(fut -> pointer.data(fut.value()));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(10, (int) pointer.data());
    }

    @Test
    public void testThenConsumeLink()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .thenConsumeLink(fut -> pointer.data(fut.value()));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(10, (int) pointer.data());
    }

    @Test
    public void testThenDoOnFinishedTask()
    {
        Future<Integer> future = Future.of(73)
                                       .thenDo(integer -> -integer.value());
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(-73, (int) future.value());
    }

    @Test
    public void testThenProduce()
    {
        Future<Integer> future = UtilTest.waitFuture(10, 100)
                                         .thenProduce(() -> 101);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testThenProduceLink()
    {
        Future<Integer> future = UtilTest.waitFuture(10, 100)
                                         .thenProduceLink(() -> 101);
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(101, (int) future.value());
    }

    @Test
    public void testThenRun()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .thenRun(() -> pointer.data(11));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(11, (int) pointer.data());
    }

    @Test
    public void testThenRunLink()
    {
        final Pointer<Integer> pointer = new Pointer<>();
        Future<Void> future = UtilTest.waitFuture(10, 10)
                                      .thenRunLink(() -> pointer.data(11));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(11, (int) pointer.data());
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

    @Test
    public void testValueOnError()
    {
        Future<Void> future = UtilTest.errorFuture();

        try
        {
            future.value();
            Assert.fail("Exception excepted!");
        }
        catch (Exception ignored)
        {
            //That's what we want
        }
    }

    @Test
    public void testWaitAllCancel()
    {
        Future<List<Future<?>>> future = Future.waitAll(Future.of("tre"),
                                                        UtilTest.waitFuture(10, 10),
                                                        UtilTest.waitFuture(100, 100),
                                                        UtilTest.waitFuture(1000, 1000),
                                                        UtilTest.waitFuture(100, 10),
                                                        UtilTest.waitFuture(10, 100));
        future.requestCancel("Don't want to wait!");
        future.waitFinish();
        Assert.assertEquals(FutureStatus.CANCELLED, future.status());
    }

    @Test
    public void testWaitAllEmpty()
    {
        Future<List<Future<?>>> future = Future.waitAll();
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertTrue(future.value().isEmpty());
    }

    @Test
    public void testWaitAllSucceed()
    {
        Future<List<Future<?>>> future = Future.waitAll(Future.of("tre"),
                                                        UtilTest.waitFuture(10, 10),
                                                        UtilTest.waitFuture(100, 100),
                                                        UtilTest.waitFuture(100, 10),
                                                        UtilTest.waitFuture(10, 100));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        List<Future<?>> list = future.value();
        Assert.assertEquals(5, list.size());
    }

    @Test
    public void testWaitFor()
    {
        Future<Integer> future = UtilTest.waitFuture(100, 111)
                                         .waitFor(Future.of("tre"),
                                                  UtilTest.waitFuture(10, 10),
                                                  UtilTest.waitFuture(100, 100),
                                                  UtilTest.waitFuture(100, 10),
                                                  UtilTest.waitFuture(10, 100));
        future.waitFinish();
        Assert.assertEquals(FutureStatus.RESULT, future.status());
        Assert.assertEquals(111, (int) future.value());
    }
}
