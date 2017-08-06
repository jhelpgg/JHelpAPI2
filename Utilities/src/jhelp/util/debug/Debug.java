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

package jhelp.util.debug;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Mutex;

/**
 * Utilities for debug
 */
public final class Debug
{
    /**
     * Describe a debug message
     */
    static class DebugInformation
    {
        /**
         * Message debug level
         */
        final DebugLevel        debugLevel;
        /**
         * Message to print
         */
        final Object[]          message;
        /**
         * Stream where print
         */
        final PrintStream       printStream;
        /**
         * Stack trace element to localize in code the message
         */
        final StackTraceElement stackTraceElement;
        /**
         * Throwable to print
         */
        final Throwable         throwable;

        /**
         * Create the information
         *
         * @param debugLevel        Message debug level
         * @param stackTraceElement Stack trace element to localize in code the message
         * @param message           Message to print
         * @param throwable         Throwable to print
         * @param printStream       Stream where print
         */
        DebugInformation(
                @NotNull final DebugLevel debugLevel,
                @NotNull final StackTraceElement stackTraceElement,
                @Nullable final Object[] message,
                @Nullable final Throwable throwable,
                @NotNull final PrintStream printStream)
        {
            this.debugLevel = debugLevel;
            this.stackTraceElement = stackTraceElement;
            this.message = message;
            this.throwable = throwable;
            this.printStream = printStream;
        }
    }

    /**
     * Function that print a message information
     */
    static class PrintFunction implements ConsumerTask<DebugInformation>
    {
        /**
         * Create the function
         */
        PrintFunction()
        {
        }

        /**
         * Print a debug information
         *
         * @param debugInformation Information to print
         */
        @Override
        public void consume(@NotNull final DebugInformation debugInformation)
        {
            Debug.printDate(debugInformation.printStream);
            debugInformation.printStream.print(debugInformation.debugLevel.header());
            Debug.printTrace(debugInformation.stackTraceElement, debugInformation.printStream, true);

            if (debugInformation.message == null)
            {
                debugInformation.printStream.println("null");
            }
            else
            {
                for (Object part : debugInformation.message)
                {
                    Debug.printObject(part, debugInformation.printStream);
                }

                debugInformation.printStream.println();
            }

            Debug.printTrace(debugInformation.throwable, debugInformation.printStream);
        }
    }
    /**
     * Header used for mark
     */
    private static final String        MARK_HEADER    = "*=> MARK\n";
    /**
     * Format integer to show at least 2 digits
     */
    private static final DecimalFormat NUMBER2        = new DecimalFormat("00");
    /**
     * Format integer to show at least 3 digits
     */
    private static final DecimalFormat NUMBER3        = new DecimalFormat("000");
    /**
     * Function to print debug message
     */
    private static final PrintFunction PRINT_FUNCTION = new PrintFunction();
    /**
     * Current debug level
     */
    private static       DebugLevel    debugLevel     = DebugLevel.VERBOSE;
    /**
     * Synchronization mutex
     */
    private static final Mutex         mutex          = new Mutex();

    /**
     * Print debug message
     *
     * @param message Message to print
     */
    public static void debug(@Nullable Object... message)
    {
        Debug.print(DebugLevel.DEBUG, message, null, System.out);
    }

    /**
     * Print error message
     *
     * @param message Message to print
     */
    public static void error(@Nullable Object... message)
    {
        Debug.print(DebugLevel.ERROR, message, null, System.err);
    }

    /**
     * Print exception message
     *
     * @param throwable Error/exception trace
     * @param message   Message to print
     */
    public static void exception(@NotNull Throwable throwable, @Nullable Object... message)
    {
        Debug.print(DebugLevel.ERROR, message, throwable, System.err);
    }

    /**
     * Current debug level
     *
     * @return Current debug level
     */
    public static @NotNull DebugLevel getDebugLevel()
    {
        return Debug.debugLevel;
    }

    /**
     * Print information message
     *
     * @param message Message to print
     */
    public static void information(@Nullable Object... message)
    {
        Debug.print(DebugLevel.INFORMATION, message, null, System.out);
    }

    /**
     * Print a mark
     *
     * @param mark Mark to print
     */
    public static void mark(@NotNull String mark)
    {
        int           size    = mark.length() + 12;
        StringBuilder message = new StringBuilder(Debug.MARK_HEADER.length() + 3 * size + 2);

        message.append(Debug.MARK_HEADER);

        for (int i = 0; i < size; i++)
        {
            message.append('*');
        }

        message.append("\n***   ");
        message.append(mark);
        message.append("   ***\n");

        for (int i = 0; i < size; i++)
        {
            message.append('*');
        }

        Debug.print(DebugLevel.INFORMATION, new Object[]{message.toString()}, null, System.out);
    }

    /**
     * Private a message
     *
     * @param debugLevel  Debug level
     * @param message     Message to print
     * @param throwable   Throwable to print trace
     * @param printStream Stream where print
     */
    private static void print(
            @NotNull final DebugLevel debugLevel,
            @Nullable final Object[] message, @Nullable final Throwable throwable,
            @NotNull final PrintStream printStream)
    {
        if (debugLevel.order() > Debug.debugLevel.order())
        {
            return;
        }

        DebugInformation debugInformation = new DebugInformation(debugLevel,
                                                                 (new Throwable()).getStackTrace()[2],
                                                                 message, throwable,
                                                                 printStream);
        Debug.mutex.playInCriticalSectionVoid(Debug.PRINT_FUNCTION, debugInformation);
    }

    /**
     * Print current date
     *
     * @param printStream Stream where print
     */
    static void printDate(@NotNull PrintStream printStream)
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        printStream.print(gregorianCalendar.get(GregorianCalendar.YEAR));
        printStream.print("-");
        printStream.print(Debug.NUMBER2.format(gregorianCalendar.get(GregorianCalendar.MONTH) + 1));
        printStream.print("-");
        printStream.print(Debug.NUMBER2.format(gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH)));
        printStream.print(" ");
        printStream.print(Debug.NUMBER2.format(gregorianCalendar.get(GregorianCalendar.HOUR)));
        printStream.print(":");
        printStream.print(Debug.NUMBER2.format(gregorianCalendar.get(GregorianCalendar.MINUTE)));
        printStream.print(":");
        printStream.print(Debug.NUMBER2.format(gregorianCalendar.get(GregorianCalendar.SECOND)));
        printStream.print(":");
        printStream.print(Debug.NUMBER3.format(gregorianCalendar.get(GregorianCalendar.MILLISECOND)));
        printStream.print(" ");
    }

    /**
     * Print an object
     *
     * @param object      Object to print
     * @param printStream Stream where print
     */
    static void printObject(@Nullable Object object, @NotNull PrintStream printStream)
    {
        if (object == null)
        {
            printStream.print("null");
            return;
        }

        Class<?> clazz = object.getClass();

        if (!clazz.isArray())
        {
            if (object instanceof Iterable)
            {
                printStream.print("{");
                boolean first = true;

                for (Object element : (Iterable) object)
                {
                    if (!first)
                    {
                        printStream.print("; ");
                    }

                    Debug.printObject(element, printStream);
                    first = false;
                }

                printStream.print("}");
                return;
            }

            if (object instanceof Map)
            {
                printStream.print("{");
                boolean first = true;

                for (Map.Entry entry : (Set<Map.Entry>) ((Map) object).entrySet())
                {
                    if (!first)
                    {
                        printStream.print(" | ");
                    }

                    Debug.printObject(entry.getKey(), printStream);
                    printStream.print("=");
                    Debug.printObject(entry.getValue(), printStream);
                    first = false;
                }

                printStream.print("}");
                return;
            }

            printStream.print(object);
            return;
        }

        printStream.print("[");
        Class<?> componentType = clazz.getComponentType();

        if (componentType.isPrimitive())
        {
            if (boolean.class.equals(componentType))
            {
                boolean[] array  = (boolean[]) object;
                int       length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (char.class.equals(componentType))
            {
                char[] array  = (char[]) object;
                int    length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (byte.class.equals(componentType))
            {
                byte[] array  = (byte[]) object;
                int    length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (short.class.equals(componentType))
            {
                short[] array  = (short[]) object;
                int     length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (int.class.equals(componentType))
            {
                int[] array  = (int[]) object;
                int   length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (long.class.equals(componentType))
            {
                long[] array  = (long[]) object;
                int    length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (float.class.equals(componentType))
            {
                float[] array  = (float[]) object;
                int     length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
            else if (double.class.equals(componentType))
            {
                double[] array  = (double[]) object;
                int      length = array.length;

                if (length > 0)
                {
                    printStream.print(array[0]);

                    for (int i = 1; i < length; i++)
                    {
                        printStream.print(", ");
                        printStream.print(array[i]);
                    }
                }
            }
        }
        else
        {
            Object[] array  = (Object[]) object;
            int      length = array.length;

            if (length > 0)
            {
                Debug.printObject(array[0], printStream);

                for (int i = 1; i < length; i++)
                {
                    printStream.print(", ");
                    Debug.printObject(array[i], printStream);
                }
            }
        }

        printStream.print("]");
    }

    /**
     * Print a stack trace element
     *
     * @param stackTraceElement Stack trace element
     * @param printStream       Stream where print
     * @param somethingFollow   Indicates if something will be print in same line or not
     */
    static void printTrace(
            @NotNull StackTraceElement stackTraceElement, @NotNull PrintStream printStream, boolean somethingFollow)
    {
        printStream.print(stackTraceElement.getClassName());
        printStream.print(".");
        printStream.print(stackTraceElement.getMethodName());
        printStream.print(" at ");
        printStream.print(stackTraceElement.getLineNumber());

        if (somethingFollow)
        {
            printStream.print(": ");
        }
        else
        {
            printStream.println();
        }
    }

    /**
     * Print a complete trace
     *
     * @param throwable   Throwable to print its trace
     * @param printStream Stream where print
     */
    static void printTrace(@Nullable Throwable throwable, @NotNull PrintStream printStream)
    {
        while (throwable != null)
        {
            printStream.println(throwable.toString());

            for (StackTraceElement stackTraceElement : throwable.getStackTrace())
            {
                printStream.print("   ");
                Debug.printTrace(stackTraceElement, printStream, false);
            }

            throwable = throwable.getCause();

            if (throwable != null)
            {
                printStream.println("Caused by:");
            }
        }
    }

    /**
     * Change debug level
     *
     * @param debugLevel New debug level
     */
    public static void setLevel(@NotNull DebugLevel debugLevel)
    {
        if (debugLevel == null)
        {
            throw new NullPointerException("debugLevel MUST NOT be null!");
        }

        Debug.debugLevel = debugLevel;
    }

    /**
     * Print todo message
     *
     * @param todo Message to print
     */
    public static void todo(@NotNull Object... todo)
    {
        Object[] message = new Object[todo.length + 2];
        message[0] = "-TODO- ";
        System.arraycopy(todo, 0, message, 1, todo.length);
        message[message.length - 1] = " -TODO-";
        Debug.print(DebugLevel.INFORMATION, message, null, System.out);
    }

    /**
     * Print message follow by the call stack trace
     *
     * @param message Message to print
     */
    public static void trace(@Nullable Object... message)
    {
        Debug.print(DebugLevel.DEBUG, message, new Throwable(), System.out);
    }

    /**
     * Print verbose message
     *
     * @param message Message to print
     */
    public static void verbose(@Nullable Object... message)
    {
        Debug.print(DebugLevel.VERBOSE, message, null, System.out);
    }

    /**
     * Print warning message
     *
     * @param message Message to print
     */
    public static void warning(@Nullable Object... message)
    {
        Debug.print(DebugLevel.WARNING, message, null, System.err);
    }
}
