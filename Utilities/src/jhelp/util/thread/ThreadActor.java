package jhelp.util.thread;

/**
 * Actor that play a task
 */
class ThreadActor implements Runnable
{
    /**
     * Task element
     */
    TaskElement<?, ?> taskElement = null;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        if (this.taskElement != null)
        {
            this.taskElement.playTask();
        }

        this.taskElement = null;
        ThreadManager.THREAD_MANAGER.anActorIsFree();
    }
}
