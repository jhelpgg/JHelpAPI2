package jhelp.util.thread;

/**
 * Created by jhelp on 28/06/17.
 */
public interface RequestCancelListener<R>
{
    void cancellationRequested(Promise<R> promise, String reason);
}
