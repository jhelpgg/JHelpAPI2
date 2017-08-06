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

package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;

/**
 * A condition to check
 */
public interface Condition<T>
{
    /**
     * Condition that check if a boolean is true
     */
    Condition<Boolean> IS_TRUE  = new Condition<Boolean>()
    {
        /**
         * Indicates if value is {@code true}
         * @param value Tested value
         * @return Parameter validity
         */
        @Override
        public boolean isValid(final @Nullable Boolean value)
        {
            return value != null && value;
        }

        /**
         * Opposite condition
         * @return Opposite condition
         */
        @Override
        public @NotNull Condition<Boolean> negate()
        {
            return Condition.IS_FALSE;
        }
    };
    /**
     * Condition that check if a boolean is false
     */
    Condition<Boolean> IS_FALSE = new Condition<Boolean>()
    {
        /**
         * Indicates if value is {@code false}
         * @param value Tested value
         * @return Parameter validity
         */
        @Override
        public boolean isValid(final @Nullable Boolean value)
        {
            return value == null || !value;
        }

        /**
         * Opposite condition
         * @return Opposite condition
         */
        @Override
        public @NotNull Condition<Boolean> negate()
        {
            return Condition.IS_TRUE;
        }
    };

    /**
     * Create condition result of an "and" on all given conditions.<br>
     * That is to say, the result condition wil be validate if and only if all given conditions are validate
     *
     * @param condition1 First condition
     * @param conditions Other conditions
     * @param <T>        Element type that conditions have to full fill
     * @return Created condition
     */
    static @SafeVarargs @NotNull <T> Condition<T> and(
            final @NotNull Condition<T> condition1, final @NotNull Condition<T>... conditions)
    {
        Objects.requireNonNull(condition1, "condition1 MUST NOT be null!");
        Objects.requireNonNull(conditions, "conditions MUST NOT be null!");

        for (Condition<T> condition : conditions)
        {
            if (condition == null)
            {
                throw new NullPointerException("One of given condition is null!");
            }
        }

        if (conditions.length == 0)
        {
            return condition1;
        }

        return value ->
        {
            if (!condition1.isValid(value))
            {
                return false;
            }

            for (Condition<T> condition : conditions)
            {
                if (!condition.isValid(value))
                {
                    return false;
                }
            }

            return true;
        };
    }

    /**
     * Create condition to check a value is inside a given interval
     *
     * @param number1 First interval number
     * @param number2 Second interval number
     * @param <N>     Number type
     * @return Created condition
     */
    static @NotNull <N extends Number> Condition<N> insideInterval(@NotNull N number1, @NotNull N number2)
    {
        Objects.requireNonNull(number1, "number1 MUST NOT be null!");
        Objects.requireNonNull(number2, "number2 MUST NOT be null!");
        return Condition.insideInterval(new Interval<>(number1, number2));
    }

    /**
     * Create condition to check a value is inside a given interval
     *
     * @param interval Interval to be inside
     * @param <N>      Number type
     * @return Created condition
     */
    static @NotNull <N extends Number> Condition<N> insideInterval(@NotNull Interval<N> interval)
    {
        Objects.requireNonNull(interval, "interval MUST NOT be null!");
        return new Condition<N>()
        {
            /**
             * Indicates if number inside interval
             * @param value Tested value Tested number
             * @return {@code true} if number inside
             */
            @Override
            public boolean isValid(final @Nullable N value)
            {
                return value != null && interval.inside(value);
            }

            /**
             * Opposite condition
             * @return Opposite condition
             */
            @Override
            public @NotNull Condition<N> negate()
            {
                return Condition.outsideInterval(interval);
            }
        };
    }

    /**
     * Create condition that negate the given one.<br>
     * That is to say the return a condition which is valid if and only if given condition is not valid
     *
     * @param condition Condition to negate
     * @param <T>       Element type of condition to negate
     * @return Opposite condition
     */
    static @NotNull <T> Condition<T> not(@NotNull Condition<T> condition)
    {
        return condition.negate();
    }

    /**
     * Create condition result of an "or" on all given conditions.<br>
     * Tht is to say, the result condition wil be validate if and only if at least one of given conditions is validate
     *
     * @param condition1 First condition
     * @param conditions Other conditions
     * @param <T>        Element type that conditions have to full fill
     * @return Created condition
     */
    static @SafeVarargs @NotNull <T> Condition<T> or(
            final @NotNull Condition<T> condition1, final @NotNull Condition<T>... conditions)
    {
        Objects.requireNonNull(condition1, "condition1 MUST NOT be null!");
        Objects.requireNonNull(conditions, "conditions MUST NOT be null!");

        for (Condition<T> condition : conditions)
        {
            if (condition == null)
            {
                throw new NullPointerException("One of given condition is null!");
            }
        }

        if (conditions.length == 0)
        {
            return condition1;
        }

        return value ->
        {
            if (condition1.isValid(value))
            {
                return true;
            }

            for (Condition<T> condition : conditions)
            {
                if (condition.isValid(value))
                {
                    return true;
                }
            }

            return false;
        };
    }

    /**
     * Create condition to check a value is outside a given interval
     *
     * @param number1 First outside number
     * @param number2 Second outside number
     * @param <N>     Number type
     * @return Created condition
     */
    static @NotNull <N extends Number> Condition<N> outsideInterval(@NotNull N number1, @NotNull N number2)
    {
        Objects.requireNonNull(number1, "number1 MUST NOT be null!");
        Objects.requireNonNull(number2, "number2 MUST NOT be null!");
        return Condition.outsideInterval(new Interval<>(number1, number2));
    }

    /**
     * Create condition to check a value is outside a given interval
     *
     * @param interval Interval to be outside
     * @param <N>      Number type
     * @return Created condition
     */
    static @NotNull <N extends Number> Condition<N> outsideInterval(@NotNull Interval<N> interval)
    {
        Objects.requireNonNull(interval, "interval MUST NOT be null!");
        return new Condition<N>()
        {
            /**
             * Indicates if number outside interval
             * @param value Tested value Tested number
             * @return {@code true} if number outside
             */
            @Override
            public boolean isValid(final @Nullable N value)
            {
                return value == null || !interval.inside(value);
            }

            /**
             * Opposite condition
             * @return Opposite condition
             */
            @Override
            public @NotNull Condition<N> negate()
            {
                return Condition.insideInterval(interval);
            }
        };
    }

    /**
     * Indicates if condition is full fill
     *
     * @param value Tested value
     * @return {@code true} if value respects the condition
     */
    boolean isValid(@Nullable T value);

    /**
     * Create opposite condition to this one
     *
     * @return Opposite condition
     */
    default @NotNull Condition<T> negate()
    {
        return new Condition<T>()
        {
            /**
             * Indicates if condition is full fill
             *
             * @param value Tested value
             * @return {@code true} if value respects the condition
             */
            @Override
            public boolean isValid(final @Nullable T value)
            {
                return !Condition.this.isValid(value);
            }

            /**
             * Create opposite condition to this one
             *
             * @return Opposite condition
             */
            @Override
            public @NotNull Condition<T> negate()
            {
                return Condition.this;
            }
        };
    }
}
