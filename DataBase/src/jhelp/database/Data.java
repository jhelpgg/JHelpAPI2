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
package jhelp.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import jhelp.util.text.UtilText;

/**
 * Data representation on data base
 */
public class Data
{
    /**
     * Create an "infinite" string data
     *
     * @param value Value
     * @return Created data
     */
    public static Data createLongStringData(final String value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }

        final Data data = new Data();

        data.value = value.replace('\'', (char) 127).replace('"', (char) 128);
        data.dataType = DataType.LONGVARCHAR;

        return data;
    }

    /**
     * Data type
     */
    private DataType dataType;
    /**
     * Data value
     */
    private Object   value;

    /**
     * Constructs Data
     */
    private Data()
    {
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final BigDecimal value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.DECIMAL;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final boolean value)
    {
        this.value = Boolean.valueOf(value);
        this.dataType = DataType.BOOLEAN;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final byte value)
    {
        this.value = new Byte(value);
        this.dataType = DataType.TINYINT;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final byte[] value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.BINARY;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final Date value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.DATE;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final double value)
    {
        this.value = new Double(value);
        this.dataType = DataType.DOUBLE;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final int value)
    {
        this.value = new Integer(value);
        this.dataType = DataType.INT;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final long value)
    {
        this.value = new Long(value);
        this.dataType = DataType.BIGINT;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final Object value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.OBJECT;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final short value)
    {
        this.value = new Short(value);
        this.dataType = DataType.SMALLINT;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final String value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value.replace('\'', (char) 127).replace('"', (char) 128);
        this.dataType = DataType.VARCHAR;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final Time value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.TIME;
    }

    /**
     * Constructs Data
     *
     * @param value Value
     */
    public Data(final Timestamp value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.value = value;
        this.dataType = DataType.TIMESTAMP;
    }

    /**
     * Check if data type if the good one
     *
     * @param dataType Data type to verify
     */
    private void checkType(final DataType dataType)
    {
        if (this.dataType != dataType)
        {
            throw new IllegalStateException(
                    UtilText.concatenate("The data is ", this.dataType.name(), " not ", dataType.name()));
        }
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     */
    String toString(final Database database)
    {
        if (this.value == null)
        {
            return "NULL";
        }

        switch (this.dataType)
        {
            case BOOLEAN:
                return this.value.toString().toUpperCase();
            case VARCHAR:
            case LONGVARCHAR:
            case DATE:
            case TIME:
            case TIMESTAMP:
                return UtilText.concatenate("'", database.stringForWrite(this.value.toString()), "'");
        }
        return this.value.toString();
    }

    /**
     * Value on big decimal
     *
     * @return Value
     */
    public BigDecimal getBigDecimal()
    {
        this.checkType(DataType.DECIMAL);
        return (BigDecimal) this.value;
    }

    /**
     * Value on boolean
     *
     * @return Value
     */
    public boolean getBoolean()
    {
        this.checkType(DataType.BOOLEAN);
        return ((Boolean) this.value).booleanValue();
    }

    /**
     * Value on byte
     *
     * @return Value
     */
    public byte getByte()
    {
        this.checkType(DataType.TINYINT);
        return ((Byte) this.value).byteValue();
    }

    /**
     * Value on byte array
     *
     * @return Value
     */
    public byte[] getByteArray()
    {
        this.checkType(DataType.BINARY);
        return (byte[]) this.value;
    }

    /**
     * Return dataType
     *
     * @return dataType
     */
    public DataType getDataType()
    {
        return this.dataType;
    }

    /**
     * Value on {@link Date}
     *
     * @return Value
     */
    public Date getDate()
    {
        this.checkType(DataType.DATE);
        return (Date) this.value;
    }

    /**
     * Value on double
     *
     * @return Value
     */
    public double getDouble()
    {
        this.checkType(DataType.DOUBLE);
        return ((Double) this.value).doubleValue();
    }

    /**
     * Value on int
     *
     * @return Value
     */
    public int getInt()
    {
        this.checkType(DataType.INT);
        return ((Integer) this.value).intValue();
    }

    /**
     * Value on long
     *
     * @return Value
     */
    public long getLong()
    {
        this.checkType(DataType.BIGINT);
        return ((Long) this.value).longValue();
    }

    /**
     * Value on "infinite" String
     *
     * @return Value
     */
    public String getLongString()
    {
        this.checkType(DataType.LONGVARCHAR);
        return ((String) this.value).replace((char) 127, '\'').replace((char) 128, '"');
    }

    /**
     * Change String "infinite" value
     *
     * @param value New value
     */
    public void setLongString(final String value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.LONGVARCHAR);
        this.value = value.replace('\'', (char) 127).replace('"', (char) 128);
    }

    /**
     * Value on {@link Object}
     *
     * @return Value
     */
    public Object getObject()
    {
        this.checkType(DataType.OBJECT);
        return this.value;
    }

    /**
     * Value on short
     *
     * @return Value
     */
    public short getShort()
    {
        this.checkType(DataType.SMALLINT);
        return ((Short) this.value).shortValue();
    }

    /**
     * Value on String (limit to 128 characters)
     *
     * @return Value
     */
    public String getString()
    {
        this.checkType(DataType.VARCHAR);
        return ((String) this.value).replace((char) 127, '\'').replace((char) 128, '"');
    }

    /**
     * Value on {@link Time}
     *
     * @return Value
     */
    public Time getTime()
    {
        this.checkType(DataType.TIME);
        return (Time) this.value;
    }

    /**
     * Value on {@link Timestamp}
     *
     * @return Value
     */
    public Timestamp getTimestamp()
    {
        this.checkType(DataType.TIMESTAMP);
        return (Timestamp) this.value;
    }

    /**
     * Indicates if data is NULL
     *
     * @return {@code true} if data is NULL
     */
    public boolean isNULL()
    {
        return this.value == null;
    }

    /**
     * Change big decimal value
     *
     * @param value New value
     */
    public void set(final BigDecimal value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.DECIMAL);
        this.value = value;
    }

    /**
     * Change boolean value
     *
     * @param value New value
     */
    public void set(final boolean value)
    {
        this.checkType(DataType.BOOLEAN);
        this.value = Boolean.valueOf(value);
    }

    /**
     * Change byte value
     *
     * @param value New value
     */
    public void set(final byte value)
    {
        this.checkType(DataType.TINYINT);
        this.value = new Byte(value);
    }

    /**
     * Change byte array value
     *
     * @param value New value
     */
    public void set(final byte[] value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.BINARY);
        this.value = value;
    }

    /**
     * Change {@link Date} value
     *
     * @param value New value
     */
    public void set(final Date value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.DATE);
        this.value = value;
    }

    /**
     * Change double value
     *
     * @param value New value
     */
    public void set(final double value)
    {
        this.checkType(DataType.DOUBLE);
        this.value = new Double(value);
    }

    /**
     * Change int value
     *
     * @param value New value
     */
    public void set(final int value)
    {
        this.checkType(DataType.INT);
        this.value = new Integer(value);
    }

    /**
     * Change long value
     *
     * @param value New value
     */
    public void set(final long value)
    {
        this.checkType(DataType.BIGINT);
        this.value = new Long(value);
    }

    /**
     * Change {@link Object} value
     *
     * @param value New value
     */
    public void set(final Object value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.OBJECT);
        this.value = value;
    }

    /**
     * Change short value
     *
     * @param value New value
     */
    public void set(final short value)
    {
        this.checkType(DataType.SMALLINT);
        this.value = new Short(value);
    }

    /**
     * Change String "normal" value
     *
     * @param value New value
     */
    public void set(final String value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.VARCHAR);
        this.value = value.replace('\'', (char) 127).replace('"', (char) 128);
    }

    /**
     * Change {@link Time} value
     *
     * @param value New value
     */
    public void set(final Time value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.TIME);
        this.value = value;
    }

    /**
     * Change {@link Timestamp} value
     *
     * @param value New value
     */
    public void set(final Timestamp value)
    {
        if (value == null)
        {
            throw new NullPointerException("value mustn't be null");
        }
        this.checkType(DataType.TIMESTAMP);
        this.value = value;
    }

    /**
     * Make the data to NULL
     */
    public void setNULL()
    {
        this.value = null;
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
        if (this.value == null)
        {
            return "NULL";
        }

        switch (this.dataType)
        {
            case BOOLEAN:
                return this.value.toString().toUpperCase();
            case VARCHAR:
            case LONGVARCHAR:
            case DATE:
            case TIME:
            case TIMESTAMP:
                return UtilText.concatenate("'", this.value.toString(), "'");
        }
        return this.value.toString();
    }
}