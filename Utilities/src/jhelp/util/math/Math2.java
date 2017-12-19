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

package jhelp.util.math;

import com.sun.istack.internal.NotNull;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.BigInteger;
import jhelp.util.list.ArrayInt;

/**
 * Math utilities
 */
public class Math2
{
    /**
     * -1 in high definition
     */
    public static final BigInteger BIG_MINUS_ONE       = Math2.createBigInteger(-1);
    /**
     * 2 in high definition
     */
    public static final BigInteger BIG_TWO             = Math2.createBigInteger(2);
    /**
     * One centimeter in pica
     */
    public static final double     CENTIMETER_IN_PICA  = 6.0 / 2.54;
    /**
     * One centimeter in point
     */
    public static final double     CENTIMETER_IN_POINT = 72.0 / 2.54;
    /**
     * Double precision, the "zero"
     */
    public static final double EPSILON             = Math2.maximum(Double.MIN_VALUE,
                                                                       Math.abs(Math.E - Math.exp(1.0)),
                                                                       Math.abs(Math.PI - Math.acos(-1.0)));
    /**
     * Float precision, the "zero"
     */
    public static final float  EPSILON_FLOAT       = Math2.maximum(Float.MIN_VALUE,
                                                                       Math.abs((float) Math.E - (float) Math.exp(1.0)),
                                                                       Math.abs((float) Math.PI -
                                                                                (float) Math.acos(-1.0)));
    /**
     * One grade in degree
     */
    public static       double GRADE_IN_DEGREE     = 0.9;
    /**
     * One grade in radian
     */
    public static       double GRADE_IN_RADIAN     = Math.PI / 200d;
    /**
     * One inch in centimeter
     */
    public static final double INCH_IN_CENTIMETER  = 2.54;
    /**
     * One inch in millimeter
     */
    public static final double INCH_IN_MILLIMETER  = 25.4;
    /**
     * One inch in pica
     */
    public static final double INCH_IN_PICA        = 6.0;
    /**
     * One inch in point
     */
    public static final double INCH_IN_POINT       = 72.0;
    /**
     * One millimeter in point
     */
    public static final double MILLIMETER_IN_POINT = 72.0 / 25.4;
    /**
     * One pica in millimeter
     */
    public static final double     PICA_IN_MILLIMETER  = 25.4 / 6.0;
    /**
     * One pica in point
     */
    public static final double     PICA_IN_POINT       = 12.0;
    /**
     * PI / 2
     */
    public static final double     PI_2                = Math.PI / 2;
    /**
     * 2 * PI
     */
    public static final double     TWO_PI              = Math.PI * 2;

    /**
     * Compute the Bernoulli value
     *
     * @param n Number of elements
     * @param m Total of elements
     * @param t Factor in [0, 1]
     * @return Bernoulli value
     */
    public static double Bernoulli(final int n, final int m, final double t)
    {
        return Math2.numberOfCombination(n, m) * Math.pow(t, n) * Math.pow(1d - t, m - n);
    }

    /**
     * Convert big integer to big decimal
     *
     * @param integer Big integer to convert
     * @return Converted big decimal
     */
    public static BigDecimal bigIntegerToBigDecimal(final BigInteger integer)
    {
        return new BigDecimal(integer.toString());
    }

    /**
     * Convert centimeter to inch
     *
     * @param centimeter Centimeter to convert
     * @return Converted inch
     */
    public static double centimeterToInch(final double centimeter)
    {
        return centimeter / Math2.INCH_IN_CENTIMETER;
    }

    /**
     * Convert centimeter to millimeter
     *
     * @param centimeter Centimeter to convert
     * @return Converted millimeter
     */
    public static double centimeterToMillimeter(final double centimeter)
    {
        return centimeter * 10.0;
    }

    /**
     * Convert centimeter to pica
     *
     * @param centimeter Centimeter to convert
     * @return Converted pica
     */
    public static double centimeterToPica(final double centimeter)
    {
        return centimeter * Math2.CENTIMETER_IN_PICA;
    }

    /**
     * Convert centimeter to point
     *
     * @param centimeter Centimeter to convert
     * @return Converted point
     */
    public static double centimeterToPoint(final double centimeter)
    {
        return centimeter * Math2.CENTIMETER_IN_POINT;
    }

    /**
     * Compare two real<br>
     * It returns:
     * <ul>
     * <li>-1 : if first is lower than the second </li>
     * <li>0 : if first and second are equals </li>
     * <li>1 : if first is greater than the second </li>
     * </ul>
     *
     * @param value1 First real
     * @param value2 Second real
     * @return Comparison result
     */
    public static int compare(double value1, double value2)
    {
        return Math2.sign(value1 - value2);
    }

    /**
     * Compare two real<br>
     * It returns:
     * <ul>
     * <li>-1 : if first is lower than the second </li>
     * <li>0 : if first and second are equals </li>
     * <li>1 : if first is greater than the second </li>
     * </ul>
     *
     * @param value1 First real
     * @param value2 Second real
     * @return Comparison result
     */
    public static int compare(float value1, float value2)
    {
        return Math2.sign(value1 - value2);
    }

    /**
     * Compute intersection area between two rectangles
     *
     * @param rectangle1 First rectangle
     * @param rectangle2 Second rectangle
     * @return Computed area
     */
    public static int computeIntersectedArea(final Rectangle rectangle1, final Rectangle rectangle2)
    {
        final int xmin1 = rectangle1.x;
        final int xmax1 = rectangle1.x + rectangle1.width;
        final int ymin1 = rectangle1.y;
        final int ymax1 = rectangle1.y + rectangle1.height;
        final int xmin2 = rectangle2.x;
        final int xmax2 = rectangle2.x + rectangle2.width;
        final int ymin2 = rectangle2.y;
        final int ymax2 = rectangle2.y + rectangle2.height;

        if ((xmin1 > xmax2) || (ymin1 > ymax2) || (xmin2 > xmax1) || (ymin2 > ymax1))
        {
            return 0;
        }

        final int xmin = Math.max(xmin1, xmin2);
        final int xmax = Math.min(xmax1, xmax2);
        if (xmin >= xmax)
        {
            return 0;
        }

        final int ymin = Math.max(ymin1, ymin2);
        final int ymax = Math.min(ymax1, ymax2);
        if (ymin >= ymax)
        {
            return 0;
        }

        return (xmax - xmin) * (ymax - ymin);
    }

    /**
     * Create big integer from an integer
     *
     * @param value Integer base
     * @return Big integer created
     */
    public static BigInteger createBigInteger(final int value)
    {
        return new BigInteger(String.valueOf(value));
    }

    /**
     * Create big integer from an integer
     *
     * @param value Integer base
     * @return Big integer created
     */
    public static BigInteger createBigInteger(final long value)
    {
        return new BigInteger(String.valueOf(value));
    }

    /**
     * Compute the cubic interpolation
     *
     * @param cp Start value
     * @param p1 First control point
     * @param p2 Second control point
     * @param p3 Third control point
     * @param t  Factor in [0, 1]
     * @return Interpolation
     */
    public static double cubic(final double cp, final double p1, final double p2, final double p3, final double t)
    {
        final double u = 1d - t;
        return (u * u * u * cp) + (3d * t * u * u * p1) + (3d * t * t * u * p2) + (t * t * t * p3);
    }

    /**
     * Compute several cubic interpolation
     *
     * @param cp        Start value
     * @param p1        First control point
     * @param p2        Second control point
     * @param p3        Third control point
     * @param precision Number of interpolation
     * @param cubic     Where write interpolations. If {@code null} or length too small, a new array is created
     * @return Interpolations
     */
    public static double[] cubic(
            final double cp, final double p1, final double p2, final double p3, final int precision, double[] cubic)
    {
        double step;
        double actual;
        int    i;

        if ((cubic == null) || (cubic.length < precision))
        {
            cubic = new double[precision];
        }

        step = 1.0 / (precision - 1.0);
        actual = 0;
        for (i = 0; i < precision; i++)
        {
            if (i == (precision - 1))
            {
                actual = 1.0;
            }
            cubic[i] = Math2.cubic(cp, p1, p2, p3, actual);
            actual += step;
        }
        return cubic;
    }

    /**
     * Convert degree to grade
     *
     * @param degree Degree to convert
     * @return Converted grade
     */
    public static double degreeToGrade(final double degree)
    {
        return degree * Math2.GRADE_IN_DEGREE;
    }

    /**
     * Convert degree to radian
     *
     * @param degree Degree to convert
     * @return Converted radian
     */
    public static double degreeToRadian(final double degree)
    {
        return (degree * Math.PI) / 180.0;
    }

    /**
     * Indicates if two given real can be considered as equals
     *
     * @param value1 First real
     * @param value2 Second real
     * @return {@code true} if two given real can be considered as equals
     */
    public static boolean equals(double value1, double value2)
    {
        return Math2.isNul(value1 - value2);
    }

    /**
     * Indicates if two given real can be considered as equals
     *
     * @param value1 First real
     * @param value2 Second real
     * @return {@code true} if two given real can be considered as equals
     */
    public static boolean equals(float value1, float value2)
    {
        return Math2.isNul(value1 - value2);
    }

    /**
     * Convert grade to degree
     *
     * @param grade Grade to convert
     * @return Converted degree
     */
    public static double gradeToDegree(final double grade)
    {
        return grade / Math2.GRADE_IN_DEGREE;
    }

    /**
     * Convert grade to radian
     *
     * @param grade Grade to convert
     * @return Converted radian
     */
    public static double gradeToRadian(final double grade)
    {
        return (grade * Math.PI) / 200.0;
    }

    /**
     * Compute the greater common divider of two number
     *
     * @param long1 First
     * @param long2 Second
     * @return The greater common divider
     */
    public static long greaterCommonDivider(long long1, long long2)
    {
        long absoluteLong1 = Math.abs(long1);
        long absoluteLong2 = Math.abs(long2);
        long minimum       = Math.min(absoluteLong1, absoluteLong2);
        long maximum       = Math.max(absoluteLong1, absoluteLong2);
        long temporary;

        while (minimum > 0)
        {
            temporary = minimum;
            minimum = maximum % minimum;
            maximum = temporary;
        }

        return maximum;
    }

    /**
     * Compute the greater common divider of two number
     *
     * @param int1 First
     * @param int2 Second
     * @return The greater common divider
     */
    public static int greaterCommonDivider(int int1, int int2)
    {
        int absoluteInt1 = Math.abs(int1);
        int absoluteInt2 = Math.abs(int2);
        int minimum      = Math.min(absoluteInt1, absoluteInt2);
        int maximum      = Math.max(absoluteInt1, absoluteInt2);
        int temporary;

        while (minimum > 0)
        {
            temporary = minimum;
            minimum = maximum % minimum;
            maximum = temporary;
        }

        return maximum;
    }

    /**
     * Convert inch to centimeter
     *
     * @param inch Inch to convert
     * @return Converted centimeter
     */
    public static double inchToCentimeter(final double inch)
    {
        return inch * Math2.INCH_IN_CENTIMETER;
    }

    /**
     * Convert inch to millimeter
     *
     * @param inch Inch to convert
     * @return Converted millimeter
     */
    public static double inchToMillimeter(final double inch)
    {
        return inch * Math2.INCH_IN_MILLIMETER;
    }

    /**
     * Convert inch to pica
     *
     * @param inch Inch to convert
     * @return Converted pica
     */
    public static double inchToPica(final double inch)
    {
        return inch * Math2.INCH_IN_PICA;
    }

    /**
     * Convert inch to point
     *
     * @param inch Inch to convert
     * @return Converted point
     */
    public static double inchToPoint(final double inch)
    {
        return inch * Math2.INCH_IN_POINT;
    }

    /**
     * Compute cosinus interpolation.<br>
     * f : [0, 1] -> [0, 1]<br>
     * f(0)=0<br>
     * f(1)=1<br>
     * f is strictly increase
     *
     * @param t Value to interpolate in [0, 1]
     * @return Interpolated result in [0, 1]
     */
    public static double interpolationCosinus(final double t)
    {
        return 0.5d + Math.cos((t + 1) * Math.PI) / 2d;
    }

    /**
     * Compute exponential interpolation.<br>
     * f : [0, 1] -> [0, 1]<br>
     * f(0)=0<br>
     * f(1)=1<br>
     * f is strictly increase
     *
     * @param t Value to interpolate in [0, 1]
     * @return Interpolated result in [0, 1]
     */
    public static double interpolationExponential(final double t)
    {
        return Math.expm1(t) / (Math.E - 1d);
    }

    /**
     * Compute logarithm interpolation.<br>
     * f : [0, 1] -> [0, 1]<br>
     * f(0)=0<br>
     * f(1)=1<br>
     * f is strictly increase
     *
     * @param t Value to interpolate in [0, 1]
     * @return Interpolated result in [0, 1]
     */
    public static double interpolationLogarithm(final double t)
    {
        return Math.log1p(t) / Math.log(2d);
    }

    /**
     * Compute sinus interpolation.<br>
     * f : [0, 1] -> [0, 1]<br>
     * f(0)=0<br>
     * f(1)=1<br>
     * f is strictly increase
     *
     * @param t Value to interpolate in [0, 1]
     * @return Interpolated result in [0, 1]
     */
    public static double interpolationSinus(final double t)
    {
        return 0.5d + (Math.sin((t * Math.PI) - Math2.PI_2) / 2d);
    }

    /**
     * Indicates if given value can be considered as zero
     *
     * @param value Value to test
     * @return {@code true} if given value can be considered as zero
     */
    public static boolean isNul(double value)
    {
        return Math.abs(value) <= Math2.EPSILON;
    }

    /**
     * Indicates if given value can be considered as zero
     *
     * @param value Value to test
     * @return {@code true} if given value can be considered as zero
     */
    public static boolean isNul(float value)
    {
        return Math.abs(value) <= Math2.EPSILON_FLOAT;
    }

    /**
     * Limit an integer between 2 values.<br>
     * If the integer is between given bounds, the integer is returned.<br>
     * If the integer is lower the minimum of the given bounds, the minimum is returned.<br>
     * If the integer is upper the maximum of the given bounds, the maximum is returned.
     *
     * @param integer Integer to limit
     * @param bound1  First bound
     * @param bound2  Second bound
     * @return Limited integer
     */
    public static int limit(final int integer, final int bound1, final int bound2)
    {
        final int min = Math.min(bound1, bound2);
        final int max = Math.max(bound1, bound2);
        return Math.max(min, Math.min(max, integer));
    }

    /**
     * Limit an integer between 2 values.<br>
     * If the integer is between given bounds, the integer is returned.<br>
     * If the integer is lower the minimum of the given bounds, the minimum is returned.<br>
     * If the integer is upper the maximum of the given bounds, the maximum is returned.
     *
     * @param integer Integer to limit
     * @param bound1  First bound
     * @param bound2  Second bound
     * @return Limited integer
     */
    public static long limit(final long integer, final long bound1, final long bound2)
    {
        final long min = Math.min(bound1, bound2);
        final long max = Math.max(bound1, bound2);
        return Math.max(min, Math.min(max, integer));
    }

    /**
     * Return the given integer, if the integer is in [0, 255]. If integer<0, we return 0, if integer>255, we return 255
     *
     * @param integer Integer to limit in [0, 255]
     * @return Limited integer
     */
    public static int limit0_255(final int integer)
    {
        return Math2.limit(integer, 0, 255);
    }

    /**
     * Compute the lower common multiple of two numbers
     *
     * @param long1 First
     * @param long2 Second
     * @return The greater common divider
     */
    public static long lowerCommonMultiple(long long1, long long2)
    {
        long gcd = Math2.greaterCommonDivider(long1, long2);

        if (gcd == 0)
        {
            return 0;
        }

        return long1 * (long2 / gcd);
    }

    /**
     * Compute the lower common multiple of two numbers
     *
     * @param int1 First
     * @param int2 Second
     * @return The greater common divider
     */
    public static int lowerCommonMultiple(int int1, int int2)
    {
        int gcd = Math2.greaterCommonDivider(int1, int2);

        if (gcd == 0)
        {
            return 0;
        }

        return int1 * (int2 / gcd);
    }

    /**
     * Get maximum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Maximum
     */
    public static double maximum(double first, @NotNull double... others)
    {
        double maximum = first;

        for (double other : others)
        {
            maximum = Math.max(maximum, other);
        }

        return maximum;
    }

    /**
     * Get maximum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Maximum
     */
    public static float maximum(float first, @NotNull float... others)
    {
        float maximum = first;

        for (float other : others)
        {
            maximum = Math.max(maximum, other);
        }

        return maximum;
    }

    /**
     * Get maximum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Maximum
     */
    public static long maximum(long first, @NotNull long... others)
    {
        long maximum = first;

        for (long other : others)
        {
            maximum = Math.max(maximum, other);
        }

        return maximum;
    }

    /**
     * Get maximum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Maximum
     */
    public static int maximum(int first, @NotNull int... others)
    {
        int maximum = first;

        for (int other : others)
        {
            maximum = Math.max(maximum, other);
        }

        return maximum;
    }

    /**
     * Convert millimeter to centimeter
     *
     * @param millimeter Millimeter to convert
     * @return Converted centimeter
     */
    public static double millimeterToCentimeter(final double millimeter)
    {
        return millimeter * 0.1;
    }

    /**
     * Convert millimeter to inch
     *
     * @param millimeter Millimeter to convert
     * @return Converted inch
     */
    public static double millimeterToInch(final double millimeter)
    {
        return millimeter / Math2.INCH_IN_MILLIMETER;
    }

    /**
     * Convert millimeter to pica
     *
     * @param millimeter Millimeter to convert
     * @return Converted pica
     */
    public static double millimeterToPica(final double millimeter)
    {
        return millimeter / Math2.PICA_IN_MILLIMETER;
    }

    /**
     * Convert millimeter to point
     *
     * @param millimeter Millimeter to convert
     * @return Converted point
     */
    public static double millimeterToPoint(final double millimeter)
    {
        return millimeter * Math2.MILLIMETER_IN_POINT;
    }

    /**
     * Get minimum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Minimum
     */
    public static double minimum(double first, @NotNull double... others)
    {
        double minimum = first;

        for (double other : others)
        {
            minimum = Math.min(minimum, other);
        }

        return minimum;
    }

    /**
     * Get minimum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Minimum
     */
    public static float minimum(float first, @NotNull float... others)
    {
        float minimum = first;

        for (float other : others)
        {
            minimum = Math.min(minimum, other);
        }

        return minimum;
    }

    /**
     * Get minimum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Minimum
     */
    public static long minimum(long first, @NotNull long... others)
    {
        long minimum = first;

        for (long other : others)
        {
            minimum = Math.min(minimum, other);
        }

        return minimum;
    }

    /**
     * Get minimum of given numbers
     *
     * @param first  First number
     * @param others Other numbers
     * @return Minimum
     */
    public static int minimum(int first, @NotNull int... others)
    {
        int minimum = first;

        for (int other : others)
        {
            minimum = Math.min(minimum, other);
        }

        return minimum;
    }

    /**
     * Compute the modulo of a real
     *
     * @param real   Real to modulate
     * @param modulo Modulo to use
     * @return Result
     */
    public static double modulo(final double real, final double modulo)
    {
        return Math2.moduloInterval(real, 0, modulo);
    }

    /**
     * Compute the modulo of a real
     *
     * @param real   Real to modulate
     * @param modulo Modulo to use
     * @return Result
     */
    public static float modulo(final float real, final float modulo)
    {
        return Math2.moduloInterval(real, 0, modulo);
    }

    /**
     * Mathematical modulo.<br>
     * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
     *
     * @param integer Integer to modulate
     * @param modulo  Modulo to apply
     * @return Mathematical modulo : <code>integer[modulo]</code>
     */
    public static int modulo(int integer, final int modulo)
    {
        integer %= modulo;

        if (((integer < 0) && (modulo > 0)) || ((integer > 0) && (modulo < 0)))
        {
            integer += modulo;
        }

        return integer;
    }

    /**
     * Mathematical modulo.<br>
     * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
     *
     * @param integer Integer to modulate
     * @param modulo  Modulo to apply
     * @return Mathematical modulo : <code>integer[modulo]</code>
     */
    public static long modulo(long integer, final long modulo)
    {
        integer %= modulo;

        if (((integer < 0) && (modulo > 0)) || ((integer > 0) && (modulo < 0)))
        {
            integer += modulo;
        }

        return integer;
    }

    /**
     * Modulate a real inside an interval
     *
     * @param real Real to modulate
     * @param min  Minimum of interval
     * @param max  Maximum of interval
     * @return Modulated value
     */
    public static double moduloInterval(double real, double min, double max)
    {
        if (min > max)
        {
            final double temp = min;
            min = max;
            max = temp;
        }

        if ((real >= min) && (real <= max))
        {
            return real;
        }

        final double space = max - min;

        if (Math2.isNul(space))
        {
            throw new IllegalArgumentException("Can't take modulo in empty interval");
        }

        real = (real - min) / space;

        return (space * (real - Math.floor(real))) + min;
    }

    /**
     * Modulate a real inside an interval
     *
     * @param real Real to modulate
     * @param min  Minimum of interval
     * @param max  Maximum of interval
     * @return Modulated value
     */
    public static float moduloInterval(float real, float min, float max)
    {
        if (min > max)
        {
            final float temp = min;
            min = max;
            max = temp;
        }

        if ((real >= min) && (real <= max))
        {
            return real;
        }

        final float space = max - min;

        if (Math2.isNul(space))
        {
            throw new IllegalArgumentException("Can't take modulo in empty interval");
        }

        real = (real - min) / space;

        return (float) (space * (real - Math.floor(real))) + min;
    }

    /**
     * Compute the combination of N elements in M
     * <p>
     * <pre>
     *               m!
     * C(n, m) = -------------
     *            n! * (m-n)!
     * </pre>
     * <p>
     * The issue of ! is that becomes big fast, and if we apply the formula like that, we will quickly goes over int range and
     * result will become very random<br>
     * To solve this issue and have more big value we rewrite the formula (Here we consider <b>m&gt;n</b>, <b>n&gt;1</b> and
     * <b>m-n&gt;1</b>, other cases are easy to treat first)
     * <p>
     * <pre>
     *            m(m-1)..(n+1)n(n-1)...2            m(m-1)...(n+1)
     * C(n, m) = ------------------------------- = ------------------
     *            n(n-1)...1 * (m-n)(m-n-1)...2     (m-n)(m-n-1)...2
     * </pre>
     * <p>
     * Write like that less chance to over load int, but we can do better. <br>
     * For example in <b>(m-n)(m-n-1)...2</b> their 2, we are sure their a value in <b>m(m-1)...(n+1)</b> can be divide by
     * <b>2</b>, so if we divide by <b>2</b> this value before do multiplication we less the chance to overload int.<br>
     * This implementation is based on this idea, we first try simplify at maximum <b>m(m-1)...(n+1)</b> and
     * <b>(m-n)(m-n-1)...2</b> before doing multiplications to reduce the chance of out range int<br>
     * See inside code and inside comments to have more detail of algorithm
     *
     * @param n Number of elements
     * @param m Total of elements
     * @return The combination of N elements in M
     */
    public static long numberOfCombination(final int n, final int m)
    {
        // Dummy cases

        if ((n <= 0) || (m <= 0) || (n >= m))
        {
            return 1;
        }

        if ((n == 1) || (m == (n + 1)))
        {
            return m;
        }

        // Real work : n>1 and m>n and m-n>1

        /*
         * Remember we want reduce
         *
         * <pre>
         *             m(m-1)...(n+1)
         * C(n, m) =  ------------------
         *            (m-n)(m-n-1)...2
         * </pre>
         *
         * We note:
         *
         * <pre>
         *   min = Math.min(n, m-n)
         *   max = Math.max(n, m-n)
         * </pre>
         *
         * Consider the 2 possibles situation :<br>
         * <br>
         * FIRST CASE : n==min AND m-n==max, so :
         *
         * <pre>
         *             m(m-1)...(min+1)
         * C(n, m) =  ------------------
         *             max(max-1)...2
         * </pre>
         *
         * But by nature min <= max so :
         *
         * <pre>
         *             m(m-1)...(max+1)max(max-1)...(min+1)    m(m-1)...(max+1)
         * C(n, m) =  -------------------------------------- =------------------
         *             max(max-1)...(min+1)min(min-1)...2       min(min-1)...2
         * </pre>
         *
         * <br>
         * SECOND CASE : n==max AND m-n==min, so
         *
         * <pre>
         *             m(m-1)...(max+1)
         * C(n, m) =  ------------------
         *             min(min-1)...2
         * </pre>
         *
         * <br>
         * CONCLUSION : <br>
         * We can already reduce the formula to :
         *
         * <pre>
         *             m(m-1)...(max+1)
         * C(n, m) =  ------------------
         *             min(min-1)...2
         * </pre>
         */

        final int diff = m - n;
        final int min  = Math.min(n, diff);
        final int max  = Math.max(n, diff);

        // Collect numerator numbers
        final ArrayInt numerators = new ArrayInt();
        for (int i = m; i > max; i--)
        {
            numerators.add(i);
        }

        int size = numerators.getSize();
        int testedNumerator;
        int denominator, gcd;

        // For each denominator number
        for (int i = min; i >= 2; i--)
        {
            // Current denominator number
            denominator = i;

            // For each left numerator numbers
            for (int j = 0; (j < size) && (denominator > 1); j++)
            {
                // Current numerator number
                testedNumerator = numerators.getInteger(j);
                gcd = Math2.greaterCommonDivider(denominator, testedNumerator);

                // If we can simplify current denominator number with current numerator number
                if (gcd > 1)
                {
                    // Simplify the numerator
                    testedNumerator /= gcd;

                    if (testedNumerator == 1)
                    {
                        // If left nothing (just 1), remove the numerator from list
                        numerators.remove(j);
                        size--;
                        j--;
                    }
                    else
                    {
                        // Update the numerator
                        numerators.setInteger(j, testedNumerator);
                    }

                    // Simplify the denominator
                    denominator /= gcd;
                }
            }
        }

        // We have consume all denominator, so it left only simplified numerator to multiply (No need to divide)
        // We are sure it left only numerator after simplification (1 for denominator)
        // This fact is due m>n and n>1 and m-n>1
        // IF min==n AND max==m-n THEN m=max+n , n=min => m=max+min
        // IF min==m-n AND max==n THEN m=min+n , n=max => m=max+min
        // => c(n,m) = (max+min)...(max+1) / min...2
        // For every p inside {2, 3, ..., min} we are sure to find at least one element in {(max+1), ..., (max+min)} that it can
        // divide
        // q divide p if q=rp (r in N)
        // max = ap+b (a,b in N, a ≥ 0, 0 ≤ b < p) => max + (p-b) in {(max+1), ..., (max+min)} AND max + (p-b) = ap+b+p-b = (a+1)p
        // so max + (p-b) divide p, so it exists at least one element in {(max+1), ..., (max+min)} that can be divide by p
        long result = 1;

        for (int i = 0; i < size; i++)
        {
            result *= numerators.getInteger(i);
        }

        return result;
    }

    /**
     * Convert pica to centimeter
     *
     * @param pica Pica to convert
     * @return Converted centimeter
     */
    public static double picaToCentimeter(final double pica)
    {
        return pica / Math2.CENTIMETER_IN_PICA;
    }

    /**
     * Convert pica to inch
     *
     * @param pica Pica to convert
     * @return Converted inch
     */
    public static double picaToInch(final double pica)
    {
        return pica / Math2.INCH_IN_PICA;
    }

    /**
     * Convert pica to millimeter
     *
     * @param pica Pica to convert
     * @return Converted millimeter
     */
    public static double picaToMillimeter(final double pica)
    {
        return pica * Math2.PICA_IN_MILLIMETER;
    }

    /**
     * Convert pica to point
     *
     * @param pica Pica to convert
     * @return Converted point
     */
    public static double picaToPoint(final double pica)
    {
        return pica * Math2.PICA_IN_POINT;
    }

    /**
     * Convert point to centimeter
     *
     * @param point Point to convert
     * @return Converted centimeter
     */
    public static double pointToCentimeter(final double point)
    {
        return point / Math2.CENTIMETER_IN_POINT;
    }

    /**
     * Convert point to inch
     *
     * @param point Point to convert
     * @return Converted inch
     */
    public static double pointToInch(final double point)
    {
        return point / Math2.INCH_IN_POINT;
    }

    /**
     * Convert point to millimeter
     *
     * @param point Point to convert
     * @return Converted millimeter
     */
    public static double pointToMillimeter(final double point)
    {
        return point / Math2.MILLIMETER_IN_POINT;
    }

    /**
     * Convert point to point
     *
     * @param point Point to convert
     * @return Converted point
     */
    public static double pointToPica(final double point)
    {
        return point / Math2.PICA_IN_POINT;
    }

    /**
     * Compute the quadratic interpolation
     *
     * @param cp Start value
     * @param p1 First control point
     * @param p2 Second control point
     * @param t  Factor in [0, 1]
     * @return Interpolation
     */
    public static double quadratic(final double cp, final double p1, final double p2, final double t)
    {
        final double u = 1d - t;
        return (u * u * cp) + (2d * t * u * p1) + (t * t * p2);
    }

    /**
     * Compute several quadratic interpolation
     *
     * @param cp        Start value
     * @param p1        First control point
     * @param p2        Second control point
     * @param precision Number of interpolation
     * @param quadratic Where write interpolations
     * @return Interpolations
     */
    public static double[] quadratic(
            final double cp, final double p1, final double p2, final int precision, double[] quadratic)
    {
        double step;
        double actual;
        int    i;

        if ((quadratic == null) || (quadratic.length < precision))
        {
            quadratic = new double[precision];
        }

        step = 1.0 / (precision - 1.0);
        actual = 0;
        for (i = 0; i < precision; i++)
        {
            if (i == (precision - 1))
            {
                actual = 1.0;
            }
            quadratic[i] = Math2.quadratic(cp, p1, p2, actual);
            actual += step;
        }
        return quadratic;
    }

    /**
     * Convert radian to degree
     *
     * @param radian Radian to convert
     * @return Converted degree
     */
    public static double radianToDegree(final double radian)
    {
        return (radian * 180.0) / Math.PI;
    }

    /**
     * Convert radian to grade
     *
     * @param radian Radian to convert
     * @return Converted grade
     */
    public static double radianToGrade(final double radian)
    {
        return (radian * 200.0) / Math.PI;
    }

    /**
     * Sign of a number.<br>
     * It returns:
     * <ul>
     * <li>-1 : if number is negative </li>
     * <li>0 : if number is zero </li>
     * <li>1 : if number is positive </li>
     * </ul>
     *
     * @param value Number to have its sign
     * @return Number's sign
     */
    public static int sign(double value)
    {
        if (Math2.isNul(value))
        {
            return 0;
        }
        else if (value > 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Sign of a number.<br>
     * It returns:
     * <ul>
     * <li>-1 : if number is negative </li>
     * <li>0 : if number is zero </li>
     * <li>1 : if number is positive </li>
     * </ul>
     *
     * @param value Number to have its sign
     * @return Number's sign
     */
    public static int sign(float value)
    {
        if (Math2.isNul(value))
        {
            return 0;
        }
        else if (value > 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Sign of a number.<br>
     * It returns:
     * <ul>
     * <li>-1 : if number is negative </li>
     * <li>0 : if number is zero </li>
     * <li>1 : if number is positive </li>
     * </ul>
     *
     * @param value Number to have its sign
     * @return Number's sign
     */
    public static int sign(long value)
    {
        if (value > 0)
        {
            return 1;
        }
        else if (value < 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Sign of a number.<br>
     * It returns:
     * <ul>
     * <li>-1 : if number is negative </li>
     * <li>0 : if number is zero </li>
     * <li>1 : if number is positive </li>
     * </ul>
     *
     * @param value Number to have its sign
     * @return Number's sign
     */
    public static int sign(int value)
    {
        return Integer.compare(value, 0);
    }

    /**
     * Square of a number
     *
     * @param real Number to square
     * @return Square result
     */
    public static double square(final double real)
    {
        return real * real;
    }

    /**
     * Square of a number
     *
     * @param real Number to square
     * @return Square result
     */
    public static float square(final float real)
    {
        return real * real;
    }

    /**
     * Square of a number
     *
     * @param integer Number to square
     * @return Square result
     */
    public static int square(final int integer)
    {
        return integer * integer;
    }
}
