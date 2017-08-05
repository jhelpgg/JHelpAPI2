package samples.jhelp.util.thread;

import jhelp.util.thread.ThreadManager;

public class MainSampleThreadManagement
{
    public static void main(String[] arguments)
    {
        FibonacciTask fibonacciTask = new FibonacciTask();
        ThreadManager.doTask(fibonacciTask, 6);
        ThreadManager.doTask(fibonacciTask, 120);
        ThreadManager.doTask(fibonacciTask, 12);
        ThreadManager.doTask(fibonacciTask, -66);
    }
}
