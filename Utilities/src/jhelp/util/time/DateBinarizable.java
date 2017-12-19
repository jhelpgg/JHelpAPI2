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

package jhelp.util.time;

import java.util.Calendar;
import java.util.GregorianCalendar;
import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;

/**
 * Embed a date to be able to binarize it
 *
 * @author JHelp
 */
public final class DateBinarizable
        implements Binarizable
{
    /**
     * Embed date
     */
    private final GregorianCalendar gregorianCalendar;

    /**
     * Create a new date at now
     */
    public DateBinarizable()
    {
        this.gregorianCalendar = new GregorianCalendar();
    }

    /**
     * Complete date string representation (Time + day)
     *
     * @return Complete date string representation
     */
    public String datePreciseString()
    {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.gregorianCalendar.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append('H');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.MINUTE));
        stringBuilder.append('M');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.SECOND));
        stringBuilder.append(" : ");

        stringBuilder.append(this.gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append('/');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.MONTH) + 1);
        stringBuilder.append('/');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.YEAR));

        return stringBuilder.toString();
    }

    /**
     * Date only string representation
     *
     * @return Date only string representation
     */
    public String dateString()
    {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append('/');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.MONTH) + 1);
        stringBuilder.append('/');
        stringBuilder.append(this.gregorianCalendar.get(Calendar.YEAR));

        return stringBuilder.toString();
    }

    /**
     * Embed date
     *
     * @return Embed date
     */
    public GregorianCalendar getGregorianCalendar()
    {
        return this.gregorianCalendar;
    }

    /**
     * Parse a byte array to fill the date <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param byteArray Byte array to parse
     * @see Binarizable#parseBinary(ByteArray)
     */
    @Override
    public void parseBinary(final ByteArray byteArray)
    {
        this.gregorianCalendar.setTimeInMillis(byteArray.readLong());
    }

    /**
     * Serialize the date inside a byte array <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param byteArray Byte array where write the date
     * @see Binarizable#serializeBinary(ByteArray)
     */
    @Override
    public void serializeBinary(final ByteArray byteArray)
    {
        byteArray.writeLong(this.gregorianCalendar.getTimeInMillis());
    }

    /**
     * Change the date to be now
     */
    public void setDateToNow()
    {
        this.gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
    }
}