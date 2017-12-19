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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.database.cache;

import jhelp.database.DatabaseException;
import jhelp.util.math.Math2;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of cache based on database
 *
 * @author JHelp
 */
public class DatabaseCacheTest
{
    /**
     * Do a simple test about type and value
     *
     * @throws DatabaseException On database creation/request issue
     */
    @Test
    public void test() throws DatabaseException
    {
        final DatabaseCache database = new DatabaseCache("TEST_CACHE");

        database.store("5", 5);
        database.store("PI", Math.PI);
        database.store("String", "String");
        database.store("TRUE", true);
        database.store("array", new byte[]
                {
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                });

        Assert.assertEquals(5, database.getInteger("5", 0));
        Assert.assertEquals(Math.PI, database.getReal("PI", 0), Math2.EPSILON);
        Assert.assertEquals("String", database.getString("String"));
        Assert.assertEquals(true, database.getBoolean("TRUE", false));
        Assert.assertArrayEquals(new byte[]
                                         {
                                                 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                                         }, database.getByteArray("array"));

        //

        database.store("String", "String2");
        Assert.assertEquals("String2", database.getString("String"));

        database.closeDatabaseCache();
    }
}