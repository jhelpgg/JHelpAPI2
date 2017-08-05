package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describe a message in {@link MessageHandler}
 */
final class MessageInformation<M> implements Comparable<MessageInformation<M>>
{
    /**
     * Next message ID
     */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    /**
     * Message ID
     */
    private final int  id;
    /**
     * Time when play the message
     */
    final         long time;
    /**
     * Message
     */
    final         M    message;

    /**
     * Create the information
     *
     * @param time    Time when play the message
     * @param message Message
     */
    MessageInformation(final long time, @NotNull final M message)
    {
        this.id = MessageInformation.NEXT_ID.getAndIncrement();
        this.time = time;
        this.message = message;
    }

    /**
     * Indicates if given message is same as embed one
     *
     * @param message Message to compare with
     * @return {@code true} if given message is same as embed one
     */
    boolean hasEmbedMessage(@NotNull M message)
    {
        return this.message.equals(message);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param messageInformation the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final MessageInformation<M> messageInformation)
    {
        if (this.time < messageInformation.time)
        {
            return -1;
        }

        if (this.time > messageInformation.time)
        {
            return 1;
        }

        return this.id - messageInformation.id;
    }
}
