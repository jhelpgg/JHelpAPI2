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

/**
 * Partition for one instrument<br>
 * It is a list of group note
 */
public class Partition
        implements Iterable<OvertoneGroup>
{
    /**
     * Instrument played this partition
     */
    private       int                      instrument;
    /**
     * Group notes
     */
    private final ArrayList<OvertoneGroup> overtones;

    /**
     * Constructs Partition
     */
    public Partition()
    {
        this.overtones = new ArrayList<>();
        this.instrument = 0;
    }

    /**
     * Add a major/minor couple<br>
     * Same as
     * {@link #addMajorMinor(double, int, int, double, boolean, int, boolean, Overtone)
     * addMajorMinor(Synthesiser.BLACK, 75, 25, Synthesiser.dotTime(Synthesiser.WHITE, major1, space, major2, base)}
     *
     * @param major1 Indicates if first element is major ({@code true}) or minor ({@code false})
     * @param space  Space between first and second major/minor
     * @param major2 Indicates if second element is major ({@code true}) or minor ({@code false})
     * @param base   Start overtone
     * @return {@code true} if add succeed OR {@code false} if add failed
     */
    public boolean addMajorMinor(boolean major1, int space, boolean major2, Overtone base)
    {
        return this.addMajorMinor(Synthesiser.BLACK, 75, 25, Synthesiser.dotTime(Synthesiser.WHITE),
                                  major1, space, major2, base);
    }

    /**
     * Add a major/minor couple
     *
     * @param time       Groups duration
     * @param strength   Groups strength
     * @param repeatTime Number of time to repeat the couple
     * @param pauseTime  Time pause between each major/minor
     * @param major1     Indicates if first element is major ({@code true}) or minor ({@code false})
     * @param space      Space between first and second major/minor
     * @param major2     Indicates if second element is major ({@code true}) or minor ({@code false})
     * @param base       Start overtone
     * @return {@code true} if add succeed OR {@code false} if add failed
     */
    public boolean addMajorMinor(
            final double time, final int strength, int repeatTime, double pauseTime,
            boolean major1, int space, boolean major2, Overtone base)
    {
        Overtone secondBase = base.add(space);

        if (secondBase == null)
        {
            return false;
        }

        OvertoneGroup first;

        if (major1)
        {
            first = OvertoneGroup.major(time, strength, base);
        }
        else
        {
            first = OvertoneGroup.minor(time, strength, base);
        }

        if (first == null)
        {
            return false;
        }

        OvertoneGroup second;

        if (major2)
        {
            second = OvertoneGroup.major(time, strength, base);
        }
        else
        {
            second = OvertoneGroup.minor(time, strength, base);
        }

        if (second == null)
        {
            return false;
        }

        for (int i = 0; i < repeatTime; i++)
        {
            this.addOvertoneGroup(first);
            this.addPause(pauseTime);
            this.addOvertoneGroup(second);
            this.addPause(pauseTime);
        }

        return true;
    }

    /**
     * Convenient method for add a group of a single overtone
     *
     * @param time     Overtone time
     * @param strength Overtone strength
     * @param overtone Overtone to add
     */
    public void addOvertone(final double time, final int strength, Overtone overtone)
    {
        Objects.requireNonNull(overtone, "overtone MUST NOT be null!");
        OvertoneGroup overtoneGroup = new OvertoneGroup(time, strength);
        overtoneGroup.addOvertone(overtone);
        this.addOvertoneGroup(overtoneGroup);
    }

    /**
     * Add overtone group
     *
     * @param overtoneGroup Overtone group to add
     */
    public void addOvertoneGroup(final OvertoneGroup overtoneGroup)
    {
        Objects.requireNonNull(overtoneGroup, "overtoneGroup MUST NOT be null!");
        this.overtones.add(overtoneGroup);
    }

    /**
     * Convenient method for add a pause
     *
     * @param time Pause time
     */
    public void addPause(double time)
    {
        this.addOvertoneGroup(new OvertoneGroup(time, 0));
    }

    /**
     * Clear partition
     */
    public void clear()
    {
        this.overtones.clear();
    }

    /**
     * Clear partition and all overtone group in it
     */
    public void clearAll()
    {
        for (final OvertoneGroup overtoneGroup : this.overtones)
        {
            overtoneGroup.clear();
        }

        this.overtones.clear();
    }

    /**
     * Insert overtone group
     *
     * @param overtoneGroup Overtone group to insert
     * @param index         Index where insert
     */
    public void insertOvertoneGroup(final OvertoneGroup overtoneGroup, final int index)
    {
        this.overtones.add(index, overtoneGroup);
    }

    /**
     * Return instrument
     *
     * @return instrument
     */
    public int instrument()
    {
        return this.instrument;
    }

    /**
     * Modify instrument
     *
     * @param instrument New instrument value
     */
    public void instrument(final int instrument)
    {
        this.instrument = instrument;
    }

    /**
     * Overtone group iterator
     *
     * @return Overtone group iterator
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<OvertoneGroup> iterator()
    {
        return this.overtones.iterator();
    }

    /**
     * Number of overtone group
     *
     * @return Number of overtone group
     */
    public int numberOfOvertoneGroups()
    {
        return this.overtones.size();
    }

    /**
     * Obtain overtone group
     *
     * @param index Overtone group index
     * @return Overtone group
     */
    public OvertoneGroup overtoneGroup(final int index)
    {
        return this.overtones.get(index);
    }

    /**
     * Remove overtone group
     *
     * @param overtoneGroup Overtone group to remove
     */
    public void removeOvertoneGroup(final OvertoneGroup overtoneGroup)
    {
        this.overtones.remove(overtoneGroup);
    }
}