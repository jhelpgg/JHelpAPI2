package jhelp.util.thread;

import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.list.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 11/06/17.
 */
public class TestMutex
{
    static final         Mutex         mutex         = new Mutex();
    static final         AtomicInteger count         = new AtomicInteger(0);
    static               int           maxInSameTime = 0;
    private static final int           NUMBER_THREAD = 16;
    private static final int           SLEEP         = 256;

    static class MyThread extends Thread implements ConsumerTask<Pair<Integer, Integer>>
    {
        private final int id;
        private final int sleep;

        /**
         * Applies this function to the given argument.
         *
         * @param integerIntegerPair the function argument
         */
        @Override
        public void consume(final Pair<Integer, Integer> integerIntegerPair)
        {
            int number = TestMutex.count.incrementAndGet();
            TestMutex.maxInSameTime = Math.max(TestMutex.maxInSameTime, number);
            System.out.println("number in critical section:" + number);
            System.out.println(integerIntegerPair.first + " do its work ...");

            try
            {
                Thread.sleep(integerIntegerPair.second);
            }
            catch (Exception exception)
            {
            }

            System.out.println(integerIntegerPair.first + " finished its work!");
            TestMutex.count.decrementAndGet();
        }

        MyThread(int id, int sleep)
        {
            this.id = id;
            this.sleep = sleep;
        }

        public void run()
        {
            System.out.println(this.id + " Will enter");
            TestMutex.mutex.playInCriticalSection(this, new Pair<>(this.id, this.sleep));
            System.out.println(this.id + " Exited");
        }
    }

    @Test
    public void testMutex()
    {
        MyThread[] threads = new MyThread[TestMutex.NUMBER_THREAD];

        for (int i = 0; i < TestMutex.NUMBER_THREAD; i++)
        {
            threads[i] = new MyThread(i, TestMutex.SLEEP + (int) (Math.random() * TestMutex.SLEEP) + i);
        }

        for (int i = 0; i < TestMutex.NUMBER_THREAD; i++)
        {
            threads[i].start();
        }

        for (int i = 0; i < TestMutex.NUMBER_THREAD; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Assert.assertEquals(1, TestMutex.maxInSameTime);
    }
}
