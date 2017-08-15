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

package jhelp.ontology;

import java.util.Objects;
import jhelp.util.io.ByteArray;
import jhelp.util.text.UtilText;

/**
 * Triplet composed of subject, predicate, information
 */
public final class Triplet implements Comparable<Triplet>
{
    /***
     * Parse triplet from byte array
     * @param byteArray Byte array to parse
     * @return Parsed triplet
     * @throws Exception If byte array not contains valid triplet data
     */
    public static Triplet parse(ByteArray byteArray) throws Exception
    {
        final Node subject     = Node.parse(byteArray);
        final Node predicate   = Node.parse(byteArray);
        final Node information = Node.parse(byteArray);
        return new Triplet(subject, predicate, information);
    }

    /**
     * Information
     */
    private final Node information;
    /**
     * Predicate
     */
    private final Node predicate;
    /**
     * Subject
     */
    private final Node subject;

    /**
     * Create a triplet
     *
     * @param subject     Subject
     * @param predicate   Predicate
     * @param information Information
     */
    public Triplet(final Node subject, final Node predicate, final Node information)
    {
        Objects.requireNonNull(subject, "subject MUST NOT be null!");
        Objects.requireNonNull(predicate, "predicate MUST NOT be null!");
        Objects.requireNonNull(information, "information MUST NOT be null!");
        this.subject = subject;
        this.predicate = predicate;
        this.information = information;
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
     * @param triplet the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final Triplet triplet)
    {
        int comparison = this.subject.compareTo(triplet.subject);

        if (comparison != 0)
        {
            return comparison;
        }

        comparison = this.predicate.compareTo(triplet.predicate);

        if (comparison != 0)
        {
            return comparison;
        }

        return this.information.compareTo(triplet.information);
    }

    /**
     * Indicates if given object equals to this triplet
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this triplet
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        if (!Triplet.class.equals(object.getClass()))
        {
            return false;
        }

        Triplet triplet = (Triplet) object;
        return this.subject.equals(triplet.subject) &&
               this.predicate.equals(triplet.predicate) &&
               this.information.equals(triplet.information);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return UtilText.concatenate(this.subject, " ==(", this.predicate, ")=> ", this.information);
    }

    /**
     * Information
     *
     * @return Information
     */
    public Node information()
    {
        return this.information;
    }

    /**
     * Predicate
     *
     * @return Predicate
     */
    public Node predicate()
    {
        return this.predicate;
    }

    /**
     * Serialize triplet in byte array
     *
     * @param byteArray Byte array where write
     */
    public void serialize(ByteArray byteArray)
    {
        this.subject.serialize(byteArray);
        this.predicate.serialize(byteArray);
        this.information.serialize(byteArray);
    }

    /**
     * Subject
     *
     * @return Subject
     */
    public Node subject()
    {
        return this.subject;
    }
}
