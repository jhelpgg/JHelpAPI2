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

/**
 * Data type
 */
public enum DataType
{
    /**
     * long
     */
    BIGINT,
    /**
     * byte[]
     **/
    BINARY,
    /**
     * boolean
     */
    BOOLEAN,
    /**
     * java.sql.Date
     */
    DATE,
    /**
     * java.math.BigDecimal
     */
    DECIMAL,
    /**
     * double
     */
    DOUBLE,
    /**
     * int
     */
    INT,
    /**
     * String "infinite"
     */
    LONGVARCHAR,
    /**
     * Object
     */
    OBJECT,
    /**
     * short
     */
    SMALLINT,
    /**
     * java.sql.Time
     */
    TIME,
    /**
     * java.sql.Timestamp
     */
    TIMESTAMP,
    /**
     * byte
     */
    TINYINT,
    /**
     * String (128)
     */
    VARCHAR
}