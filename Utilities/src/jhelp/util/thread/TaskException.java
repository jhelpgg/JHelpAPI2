package jhelp.util.thread;

/**
 * Exception that may happen while play a task
 */
public class TaskException extends Exception
{
    /**
     * Create the exception
     *
     * @param message Exception message
     */
    public TaskException(String message)
    {
        super(message);
    }

    /**
     * Create the exception
     *
     * @param message Exception message
     * @param cause   Exception cause
     */
    public TaskException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
