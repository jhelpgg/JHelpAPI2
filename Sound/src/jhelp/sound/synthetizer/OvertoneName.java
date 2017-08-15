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
 * Name of overtone
 */
public enum OvertoneName
{
    /**
     * C overtone
     */
    C(0),
    /**
     * C# overtone
     */
    C_SHARP(1),
    /**
     * D overtone
     */
    D(2),
    /**
     * D# overtone
     */
    D_SHARP(3),
    /**
     * E overtone
     */
    E(4),
    /**
     * F overtone
     */
    F(5),
    /**
     * F# overtone
     */
    F_SHARP(6),
    /**
     * G overtone
     */
    G(7),
    /**
     * G# overtone
     */
    G_SHARP(8),
    /**
     * H overtone
     */
    H(9),
    /**
     * H# overtone
     */
    H_SHARP(10),
    /**
     * I overtone
     */
    I(11);

    /**
     * Obtain overtone name with overtone range [0, 11]
     *
     * @param overtone Overtone range
     * @return Overtone name
     */
    public static OvertoneName obtainOvertoneName(final int overtone)
    {
        for (final OvertoneName overtoneName : OvertoneName.values())
        {
            if (overtoneName.overtone == overtone)
            {
                return overtoneName;
            }
        }

        throw new IllegalArgumentException("overtone must be in [0, 11], not " + overtone);
    }

    /**
     * Overtone range
     */
    private final int overtone;

    /**
     * Constructs OvertoneName
     *
     * @param overtone Overtone range
     */
    OvertoneName(final int overtone)
    {
        this.overtone = overtone;
    }

    /**
     * Add # to overtone name
     *
     * @return Overtone name with #
     */
    public OvertoneName addSharp()
    {
        switch (this)
        {
            case C:
                return OvertoneName.C_SHARP;
            case D:
                return OvertoneName.D_SHARP;
            case F:
                return OvertoneName.F_SHARP;
            case G:
                return OvertoneName.G_SHARP;
            case H:
                return OvertoneName.H_SHARP;
            case E:
            case I:
                throw new IllegalStateException("Can't add sharp for E or I");
            default:
                return this;
        }
    }

    /**
     * Compute the name without #
     *
     * @return Name without #
     */
    public OvertoneName obtainWithoutSharp()
    {
        switch (this)
        {
            case C_SHARP:
                return OvertoneName.C;
            case D_SHARP:
                return OvertoneName.D;
            case F_SHARP:
                return OvertoneName.F;
            case G_SHARP:
                return OvertoneName.G;
            case H_SHARP:
                return OvertoneName.H;
            default:
                return this;
        }
    }

    /**
     * Overtone range
     *
     * @return Overtone range
     */
    public int overtone()
    {
        return this.overtone;
    }

    /**
     * Indicates if the overtone have #
     *
     * @return {@code true} if the overtone have #
     */
    public boolean sharp()
    {
        switch (this)
        {
            case C:
            case D:
            case E:
            case F:
            case G:
            case H:
            case I:
                return false;
            default:
                return true;
        }
    }
}