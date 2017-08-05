package jhelp.util.thread;

/**
 * {@link Future} status
 */
public enum FutureStatus
{
    /**
     * Indicates if task still computing (May be not already launched)
     */
    COMPUTING,
    /**
     * Indicates that task finished successfully
     */
    RESULT,
    /**
     * Indicates that task had error on its execution
     */
    ERROR,
    /**
     * Indicates that task wad cancelled during its execution
     */
    CANCELLED
}
