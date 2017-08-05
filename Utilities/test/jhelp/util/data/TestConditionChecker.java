package jhelp.util.data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 13/06/17.
 */
public class TestConditionChecker
{
    @Test
    public void testHello()
    {
        Observable<Boolean> observable = new Observable<>(false);
        AtomicInteger       count      = new AtomicInteger(0);

        ConditionChecker.when(observable, bool ->
        {
            System.out.println("Hello!");
            count.incrementAndGet();
        });

        observable.value(true);
        observable.value(false);
        observable.value(true);
        observable.value(false);
        observable.value(true);

        try
        {
            Thread.sleep(16);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Assert.assertEquals("Must enter one and only one time!", 1, count.get());
    }

    @Test
    public void testAnd()
    {
        Observable<Integer> answer = new Observable<>(0);
        Observable<String>  magic  = new Observable<>("");
        AtomicBoolean       enter  = new AtomicBoolean(false);
        ConditionChecker.when(answer.and(value -> value == 42, magic.validate(value -> value.equals("73"))),
                              bool -> enter.set(true));
        enter.set(false);
        answer.value(42);
        try
        {
            Thread.sleep(16);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Assert.assertFalse(enter.get());

        enter.set(false);
        magic.value("73");
        try
        {
            Thread.sleep(16);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Assert.assertTrue(enter.get());
    }
}
