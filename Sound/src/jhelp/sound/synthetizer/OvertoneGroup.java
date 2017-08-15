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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import jhelp.util.math.Math2;

/**
 * Overtone group<br>
 * It is list of overtone played in same time, duration and intensity
 */
public class OvertoneGroup
        implements Iterable<Overtone>
{
    /**
     * Create a group of overtone
     *
     * @param time      Group duration
     * @param strength  Group strength
     * @param overtones Overtones to add to group
     * @return Create group
     */
    public static OvertoneGroup group(final double time, final int strength, Overtone... overtones)
    {
        OvertoneGroup overtoneGroup = new OvertoneGroup(time, strength);

        for (Overtone overtone : overtones)
        {
            overtoneGroup.addOvertone(overtone);
        }

        return overtoneGroup;
    }

    /**
     * Create a major group
     *
     * @param time     Group duration time
     * @param strength Group strength
     * @param first    First overtone of the group
     * @return Created group OR {@code null} if first note to high, so major can't be created
     */
    public static OvertoneGroup major(final double time, final int strength, Overtone first)
    {
        Overtone second = first.add(4);

        if (second == null)
        {
            return null;
        }

        Overtone third = second.add(3);

        if (third == null)
        {
            return null;
        }

        OvertoneGroup overtoneGroup = new OvertoneGroup(time, strength);
        overtoneGroup.addOvertone(first);
        overtoneGroup.addOvertone(second);
        overtoneGroup.addOvertone(third);
        return overtoneGroup;
    }

    /**
     * Create a minor group
     *
     * @param time     Group duration time
     * @param strength Group strength
     * @param first    First overtone of the group
     * @return Created group OR {@code null} if first note to high, so minor can't be created
     */
    public static OvertoneGroup minor(final double time, final int strength, Overtone first)
    {
        Overtone second = first.add(3);

        if (second == null)
        {
            return null;
        }

        Overtone third = second.add(4);

        if (third == null)
        {
            return null;
        }

        OvertoneGroup overtoneGroup = new OvertoneGroup(time, strength);
        overtoneGroup.addOvertone(first);
        overtoneGroup.addOvertone(second);
        overtoneGroup.addOvertone(third);
        return overtoneGroup;
    }

    /**
     * Overtones played
     */
    private final ArrayList<Overtone> group;
    /**
     * Strength on key, intensity
     */
    private       int                 strength;
    /**
     * Proportional duration time
     */
    private       double              time;
    /**
     * Duration in millisecond. (Use on play back)
     */
    long timeDuration;
    /**
     * Actual time pass. (Use on play back)
     */
    long timePass;

    /**
     * Constructs OvertoneGroup
     *
     * @param time     Proportional time
     * @param strength Strength, intensity, velocity
     */
    public OvertoneGroup(final double time, final int strength)
    {
        this.group = new ArrayList<>();
        this.time = Math.max(1e-5, time);
        this.strength = Math2.limit(strength, 0, 100);
    }

    /**
     * Add overtone
     *
     * @param overtone Overtone add
     */
    public void addOvertone(final Overtone overtone)
    {
        Objects.requireNonNull(overtone, "overtone MUST NOT be null!");

        if (!this.group.contains(overtone))
        {
            this.group.add(overtone);
        }
    }

    /**
     * Clear the group
     */
    public void clear()
    {
        this.group.clear();
    }

    /**
     * Indicates if the group is empty
     *
     * @return {@code true} if group is empty
     */
    public boolean empty()
    {
        return this.group.isEmpty();
    }

    /**
     * Iterator on overtones
     *
     * @return Iterator on overtones
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<Overtone> iterator()
    {
        return this.group.iterator();
    }

    /**
     * Remove overtone
     *
     * @param overtone Overtone to remove
     */
    public void removeOvertone(final Overtone overtone)
    {
        this.group.remove(overtone);
    }

    /**
     * Modify strength
     *
     * @param strength New strength value
     */
    public void strength(final int strength)
    {
        this.strength = Math2.limit(strength, 0, 100);
    }

    /**
     * Return strength
     *
     * @return strength
     */
    public int strength()
    {
        return this.strength;
    }

    /**
     * Modify proportional time
     *
     * @param time New proportional time value
     */
    public void time(final double time)
    {
        this.time = Math.max(1e-5, time);
    }

    /**
     * Return proportional time
     *
     * @return proportional time
     */
    public double time()
    {
        return this.time;
    }
}