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

package jhelp.security.utilities;

import java.math.BigInteger;
import java.util.Random;

/**
 * Utilities for big numbers manipulation
 */
public class MathBigInteger
{
    /**
     * Random function
     */
    private final static Random     RANDOM         = new Random();
    /**
     * One constant
     */
    public static final  BigInteger ONE            = new BigInteger("1");
    /**
     * The minimum order
     */
    public static final  BigInteger ORDER_MINMUM   = new BigInteger("256");
    /**
     * Maximum of the order : 2<sup>ORDER_NUM_BITS</sup>
     */
    public static final  int        ORDER_NUM_BITS = 63;
    /**
     * Two constant
     */
    public static final  BigInteger TWO            = new BigInteger("2");
    /**
     * Zero constant
     */
    public static final  BigInteger ZERO           = new BigInteger("0");

    /**
     * Create a big integer from a long
     *
     * @param integer the big integer to create
     * @return The big integer created
     */
    public static BigInteger createBigInteger(final long integer)
    {
        return new BigInteger(String.valueOf(integer));
    }

    /**
     * Get a valid order for the Elgamal algorithm
     *
     * @return An order
     */
    public static BigInteger getOrder()
    {
        BigInteger order = MathBigInteger.getPrimeNumber(MathBigInteger.ORDER_NUM_BITS);
        while (order.compareTo(MathBigInteger.ORDER_MINMUM) < 0)
        {
            order = MathBigInteger.getPrimeNumber(MathBigInteger.ORDER_NUM_BITS);
        }
        return order;
    }

    /**
     * Get a random prime number between 0 to 2<sup>numBits</sup>
     *
     * @param numBits The way to precise the maximum
     * @return The prime number
     */
    public static BigInteger getPrimeNumber(final int numBits)
    {
        BigInteger bigInteger = new BigInteger(numBits, MathBigInteger.RANDOM);
        while (!MathBigInteger.isPrime(bigInteger))
        {
            bigInteger = new BigInteger(numBits, MathBigInteger.RANDOM);
        }
        return bigInteger;
    }

    /**
     * Get a random big integer between two and an other one
     *
     * @param maximum The maximum value
     * @return The chosen value
     */
    public static BigInteger getRandomBigInteger(final BigInteger maximum)
    {
        return MathBigInteger.getRandomBigInteger(MathBigInteger.TWO, maximum);
    }

    /**
     * Get a random big integer between two others
     *
     * @param minimum The minimum value
     * @param maximum The maximum value
     * @return The chosen value
     */
    public static BigInteger getRandomBigInteger(final BigInteger minimum, final BigInteger maximum)
    {
        final int  numBits = Math.max(minimum.bitLength(), maximum.bitLength());
        BigInteger integer = new BigInteger(numBits, MathBigInteger.RANDOM);
        while ((integer.compareTo(minimum) <= 0) || (integer.compareTo(maximum) >= 0))
        {
            integer = new BigInteger(numBits, MathBigInteger.RANDOM);
        }
        return integer;
    }

    /**
     * Get a random prime number less a value
     *
     * @param maximum The maximum value
     * @return The chosen prime number
     */
    public static BigInteger getRandomPrimeBigInteger(final BigInteger maximum)
    {
        BigInteger prime = MathBigInteger.getRandomBigInteger(maximum);
        while (!MathBigInteger.isPrime(prime))
        {
            prime = MathBigInteger.getRandomBigInteger(maximum);
        }
        return prime;
    }

    /**
     * Indicates if an big integer is prime or not
     *
     * @param bigInteger The big integer to test
     * @return {@code true} if the big integer is prime
     */
    public static boolean isPrime(final BigInteger bigInteger)
    {
        return bigInteger.isProbablePrime(Integer.MAX_VALUE);
    }
}