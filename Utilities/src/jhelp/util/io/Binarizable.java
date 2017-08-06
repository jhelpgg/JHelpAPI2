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

package jhelp.util.io;

/**
 * Indicates that an object is binarizable.<br>
 * A binarizable object is easy to transfer over stream.<br>
 * A binarizable object <b>MUST</b> have at least a public empty constructor.<br>
 * See {@link ByteArray#readBinarizable(Class)}, {@link ByteArray#readBinarizableArray(Class)},
 * {@link ByteArray#writeBinarizable(Binarizable)}, {@link ByteArray#writeBinarizableArray(Binarizable...)},
 * {@link UtilIO#readBinarizable(Class, java.io.InputStream)}, {@link UtilIO#readBinarizableNamed(java.io.InputStream)},
 * {@link UtilIO#writeBinarizable(Binarizable, java.io.OutputStream)} and
 * {@link UtilIO#writeBinarizableNamed(Binarizable, java.io.OutputStream)} for transfer binarizables
 *
 * @author JHelp
 */
public interface Binarizable
{
    /**
     * Parse the array for fill binarizable information.<br>
     * See {@link #serializeBinary(ByteArray)} for fill information
     *
     * @param byteArray Byte array to parse
     */
    void parseBinary(ByteArray byteArray);

    /**
     * Write the binarizable information inside a byte array.<br>
     * See {@link #parseBinary(ByteArray)} for read information
     *
     * @param byteArray Byte array where write
     */
    void serializeBinary(ByteArray byteArray);
}