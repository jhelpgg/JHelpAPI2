package jhelp.util.thread;

/**
 * Created by jhelp on 22/07/17.
 */
public final class Pointer<T>
{
    private T data;

    public Pointer()
    {
    }

    public Pointer(T data)
    {
        this.data = data;
    }

    public void data(T data)
    {
        this.data = data;
    }

    public T data()
    {
        return this.data;
    }
}
