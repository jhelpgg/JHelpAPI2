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
package jhelp.sound.synthetizer;

/**
 * Overtone<br>
 * Composed of gamut and name
 */
public class Overtone
{
    /**
     * Already created overtones
     */
    private static final Overtone[][] overtones = new Overtone[10][12];

    /**
     * Add number to node to have next/previous one
     *
     * @param overtone Overtone reference
     * @param more     Number to add
     * @return Overtone result or {@code null} if result overtone out of managed overtone
     */
    public static Overtone add(Overtone overtone, int more)
    {
        int code  = overtone.overtoneCode + more - 3;
        int gamut = code / 12;

        if (gamut < 0 || gamut > 9)
        {
            return null;
        }

        return Overtone.obtainOvertone(gamut, OvertoneName.obtainOvertoneName(code % 12));
    }

    /**
     * Obtain/create overtone
     *
     * @param gamut        Gamut in [0, 9]
     * @param overtoneName Overtone name
     * @return Overtone
     */
    public static Overtone obtainOvertone(final int gamut, final OvertoneName overtoneName)
    {
        if (overtoneName == null)
        {
            throw new NullPointerException("overtoneName must not be null");
        }

        if ((gamut < 0) || (gamut > 9))
        {
            throw new IllegalArgumentException("gamut must be in [0, 9], not " + gamut);
        }

        if (Overtone.overtones[gamut][overtoneName.overtone()] == null)
        {
            Overtone.overtones[gamut][overtoneName.overtone()] = new Overtone(gamut, overtoneName);
        }

        return Overtone.overtones[gamut][overtoneName.overtone()];
    }

    /**
     * Gamut
     */
    private final int          gamut;
    /**
     * Overtone code
     */
    private final int          overtoneCode;
    /**
     * Overtone name
     */
    private final OvertoneName overtoneName;

    /**
     * Constructs Overtone
     *
     * @param gamut        Gamut
     * @param overtoneName Overtone name
     */
    private Overtone(final int gamut, final OvertoneName overtoneName)
    {
        this.gamut = gamut;
        this.overtoneName = overtoneName;
        this.overtoneCode = 3 + overtoneName.overtone() + (12 * gamut);
    }

    /**
     * Add number to node to have next/previous one
     *
     * @param more Number to add
     * @return Overtone result or {@code null} if result overtone out of managed overtone
     */
    public Overtone add(int more)
    {
        return Overtone.add(this, more);
    }

    /**
     * Return gamut
     *
     * @return gamut
     */
    public int gamut()
    {
        return this.gamut;
    }

    /**
     * Return overtoneCode
     *
     * @return overtoneCode
     */
    public int overtoneCode()
    {
        return this.overtoneCode;
    }

    /**
     * Return overtoneName
     *
     * @return overtoneName
     */
    public OvertoneName overtoneName()
    {
        return this.overtoneName;
    }

    /**
     * String representation
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return this.overtoneName.name() + " [" + this.gamut + "]";
    }
}