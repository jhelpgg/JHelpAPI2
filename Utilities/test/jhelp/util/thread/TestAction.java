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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import jhelp.util.list.ArrayObject;
import jhelp.util.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

public class TestAction
{
    @Test
    public void testLaunch()
    {
        final AtomicBoolean launched = new AtomicBoolean(false);
        Action action = new Action()
        {
            @Override
            public void doAction()
            {
                launched.set(true);
            }
        };
        Assert.assertFalse(launched.get());
        action.launch();
        Utilities.sleep(128);
        Assert.assertTrue(launched.get());
    }

    @Test
    public void testLaunchStatic()
    {
        final AtomicBoolean launched = new AtomicBoolean(false);
        Action              action   = Action.launch(() -> launched.set(true));
        Utilities.sleep(128);
        Assert.assertTrue(launched.get());
    }

    @Test
    public void testOnError()
    {
        final String message = "Expected exception";
        Action action = Action.launch(() ->
                                      {
                                          throw new RuntimeException(message);
                                      });
        final Pointer<ActionException> pointer = new Pointer<>();
        action.onError(pointer::data);
        Utilities.sleep(128);
        ActionException actionException = pointer.data();
        Assert.assertNotNull(actionException);
        Throwable current = actionException;
        Throwable last    = current;

        while (current != null)
        {
            last = current;
            current = current.getCause();
        }

        Assert.assertEquals(message, last.getMessage());
        Assert.assertEquals(RuntimeException.class, last.getClass());
    }

    @Test
    public void testOnFinish()
    {
        final AtomicBoolean onFinished = new AtomicBoolean(false);
        Action              action     = Action.launch(() -> Utilities.sleep(32)).onFinish(() -> onFinished.set(true));
        Utilities.sleep(128);
        Assert.assertTrue(onFinished.get());
    }

    @Test
    public void testParallel()
    {
        final AtomicInteger status = new AtomicInteger(0);
        final AtomicLong    time1  = new AtomicLong();
        final AtomicLong    time2  = new AtomicLong();
        final AtomicLong    time3  = new AtomicLong();
        Action action = Action.launch(() ->
                                      {
                                          time1.set(System.currentTimeMillis());
                                          Utilities.sleep(128);
                                          status.set(1);
                                      }).
                                      parallel(Action.launch(() ->
                                                             {
                                                                 time2.set(System.currentTimeMillis());
                                                                 Utilities.sleep(32);
                                                                 status.set(2);
                                                             })).
                                      onFinish(() ->
                                               {
                                                   time3.set(System.currentTimeMillis());
                                                   status.set(3);
                                               });
        Utilities.sleep(256);
        Assert.assertEquals(3, status.get());
        Assert.assertTrue(Math.abs(time2.get() - time1.get()) < 100);
        Assert.assertTrue(Math.abs(time3.get() - time1.get()) < 128 + 32);
    }

    @Test
    public void testThenDo()
    {
        final List<String> list = new ArrayObject<>();
        Action action = Action.launch(() -> list.add("first"))
                              .thenDo(Action.launch(() -> list.add("second")))
                              .thenDo(Action.launch(() -> list.add("third")))
                              .onFinish(() -> list.add("END"));
        Utilities.sleep(128);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals("first", list.get(0));
        Assert.assertEquals("second", list.get(1));
        Assert.assertEquals("third", list.get(2));
        Assert.assertEquals("END", list.get(3));
    }
}
