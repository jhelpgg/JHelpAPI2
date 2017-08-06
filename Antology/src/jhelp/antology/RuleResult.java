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

package jhelp.antology;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import jhelp.util.cache.Cache;
import jhelp.util.cache.CacheElement;
import jhelp.util.io.ByteArray;

/**
 * Describe how compute {@link Rule} result
 */
public final class RuleResult implements Comparable<RuleResult>
{
    /**
     * Rule result element cache
     */
    private static class RuleResultElement extends CacheElement<RuleResult>
    {
        /**
         * Embed result
         */
        private final Result result;

        /**
         * Create cache element
         *
         * @param result Result to obtain
         */
        RuleResultElement(final Result result)
        {
            this.result = result;
        }

        /**
         * Create the element
         *
         * @return Created element
         */
        @Override
        protected RuleResult create()
        {
            return new RuleResult(this.result, null);
        }
    }

    /**
     * Rule result cache
     */
    private static final Cache<RuleResult> CACHE = new Cache<>();

    /**
     * Parse rule result from byte array
     *
     * @param byteArray Byte array to read
     * @return Parsed rule result
     * @throws Exception If byte array not contains valid data for rule result
     */
    public static RuleResult parse(ByteArray byteArray) throws Exception
    {
        if (byteArray.readBoolean())
        {
            return RuleResult.ruleResult(byteArray.readEnum(Result.class));
        }

        return RuleResult.ruleResult(Node.parse(byteArray));
    }

    /**
     * Create rule result with a fixed node.<br>
     * That is to say the result will be always the given node
     *
     * @param node Fixed result value
     * @return Created rule result
     * @throws IllegalArgumentException if given node is {@link Node#WILDCARD}
     */
    public static @NotNull RuleResult ruleResult(@NotNull Node node)
    {
        if (node == Node.WILDCARD)
        {
            throw new IllegalArgumentException("Wildcard can't be a result!");
        }

        return new RuleResult(null, node.duplicate());
    }

    /**
     * Create rule result from a {@link Result}
     *
     * @param result Result to apply
     * @return Created rule result
     */
    public static @NotNull RuleResult ruleResult(@NotNull Result result)
    {
        return RuleResult.CACHE.get(result.name(), new RuleResultElement(result));
    }

    /**
     * Fixed node value
     */
    private final Node   fixNode;
    /**
     * Result to apply
     */
    private final Result result;

    /**
     * Create rule result
     *
     * @param result  Result to apply
     * @param fixNode Fixed node value
     */
    private RuleResult(final Result result, final Node fixNode)
    {
        this.result = result;
        this.fixNode = fixNode;
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
     * @param ruleResult the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final RuleResult ruleResult)
    {
        if (this.result != null)
        {
            if (ruleResult.result == null)
            {
                return -1;
            }

            return this.result.ordinal() - ruleResult.result.ordinal();
        }

        if (ruleResult.result != null)
        {
            return 1;
        }

        return this.fixNode.compareTo(ruleResult.fixNode);
    }

    /**
     * Indicates if given object equals to this rule result
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this rule result
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

        if (!RuleResult.class.equals(object.getClass()))
        {
            return false;
        }

        RuleResult ruleResult = (RuleResult) object;

        if (this.result != null)
        {
            return this.result == ruleResult.result;
        }

        if (ruleResult.result != null)
        {
            return false;
        }

        return this.fixNode.equals(ruleResult.fixNode);
    }

    /**
     * Fixed value<br>
     * {@code null} if rule result based on {@link Result}
     *
     * @return Fixed value
     */
    public @Nullable Node fixNode()
    {
        return this.fixNode;
    }

    /**
     * Based {@link Result}.<br>
     * {@code null} if rule result is a fixed value
     *
     * @return Based {@link Result}
     */
    public @Nullable Result result()
    {
        return this.result;
    }

    /**
     * Serialize rule result in byte array
     *
     * @param byteArray Byte array where write
     */
    public void serialize(ByteArray byteArray)
    {
        byteArray.writeBoolean(this.result != null);

        if (this.result != null)
        {
            byteArray.writeEnum(this.result);
        }
        else
        {
            this.fixNode.serialize(byteArray);
        }
    }
}
