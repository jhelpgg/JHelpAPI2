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

package jhelp.util.reflection;

import com.sun.istack.internal.Nullable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Stack;

/**
 * Utilities for reflection
 */
public class Reflection
{
    /**
     * Default Byte value
     */
    public static final Byte      DEFAULT_BYTE      = new Byte((byte) 0);
    /**
     * Default Character value
     */
    public static final Character DEFAULT_CHARACTER = new Character('\u0000');
    /**
     * Default Double value
     */
    public static final Double    DEFAULT_DOUBLE    = new Double(0.0);
    /**
     * Default Float value
     */
    public static final Float     DEFAULT_FLOAT     = new Float(0.0f);
    /**
     * Default Integer value
     */
    public static final Integer   DEFAULT_INTEGER   = new Integer(0);
    /**
     * Default Long value
     */
    public static final Long      DEFAULT_LONG      = new Long(0L);
    /**
     * Default Short value
     */
    public static final Short     DEFAULT_SHORT     = new Short((short) 0);

    /**
     * Indicates if given class can be considered as void
     *
     * @param clazz Tested class
     * @return {@code true} if given class can be considered as void
     */
    public static boolean canBeConsideredAsVoid(@Nullable Class<?> clazz)
    {
        return clazz == null || void.class.equals(clazz) || Void.class.equals(clazz);
    }

    /**
     * Compute a default value for a given class
     *
     * @param clazz Class to have a default value
     * @return Default value
     */
    public static @Nullable Object defaultValue(@Nullable Class<?> clazz)
    {
        if (Reflection.canBeConsideredAsVoid(clazz))
        {
            return null;
        }

        if (clazz.isPrimitive())
        {
            if (boolean.class.equals(clazz))
            {
                return false;
            }

            if (char.class.equals(clazz))
            {
                return '\u0000';
            }

            if (byte.class.equals(clazz))
            {
                return (byte) 0;
            }

            if (short.class.equals(clazz))
            {
                return (short) 0;
            }

            if (int.class.equals(clazz))
            {
                return 0;
            }

            if (long.class.equals(clazz))
            {
                return 0L;
            }

            if (float.class.equals(clazz))
            {
                return 0.0f;
            }

            if (double.class.equals(clazz))
            {
                return 0.0;
            }
        }

        if (Boolean.class.equals(clazz))
        {
            return Boolean.FALSE;
        }

        if (Character.class.equals(clazz))
        {
            return Reflection.DEFAULT_CHARACTER;
        }

        if (Byte.class.equals(clazz))
        {
            return Reflection.DEFAULT_BYTE;
        }

        if (Short.class.equals(clazz))
        {
            return Reflection.DEFAULT_SHORT;
        }

        if (Integer.class.equals(clazz))
        {
            return Reflection.DEFAULT_INTEGER;
        }

        if (Long.class.equals(clazz))
        {
            return Reflection.DEFAULT_LONG;
        }

        if (Float.class.equals(clazz))
        {
            return Reflection.DEFAULT_FLOAT;
        }

        if (Double.class.equals(clazz))
        {
            return Reflection.DEFAULT_DOUBLE;
        }

        if (clazz.isEnum())
        {
            try
            {
                Object values = clazz.getMethod("values").invoke(null);

                if (values != null && Array.getLength(values) > 0)
                {
                    return Array.get(values, 0);
                }
            }
            catch (Exception ignored)
            {
            }
        }

        if (CharSequence.class.isAssignableFrom(clazz))
        {
            return "";
        }

        if (clazz.isArray())
        {
            Class<?> componentType = clazz.getComponentType();
            int      count         = 1;

            while (componentType.isArray())
            {
                componentType = componentType.getComponentType();
                count++;
            }

            return Array.newInstance(componentType, new int[count]);
        }

        return null;
    }

    public static Object invokePublicMethod(Class<?> clazz, String methodName, Object... parameters)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
    {
        final Class<?>[] types  = Reflection.obtainTypes(parameters);
        final Method     method = Reflection.obtainPublicMethod(clazz, methodName, types);

        if (Modifier.isStatic(method.getModifiers()))
        {
            return method.invoke(null, parameters);
        }
        else
        {
            return method.invoke(Reflection.newInstance(clazz), parameters);
        }
    }

    /**
     * Indicates if a class extends an other one
     *
     * @param test   Class to test
     * @param parent Class to extends
     * @return {@code true} if class extends an other one
     */
    public final static boolean isInherit(Class<?> test, final Class<?> parent)
    {
        if (test.equals(parent) == true)
        {
            return true;
        }

        if ((test.isPrimitive() == true) || (test.isArray() == true))
        {
            return false;
        }

        final Stack<Class<?>> stack = new Stack<Class<?>>();
        stack.push(test);

        while (stack.isEmpty() == false)
        {
            test = stack.pop();

            if (test.equals(parent) == true)
            {
                return true;
            }

            if (test.getSuperclass() != null)
            {
                stack.push(test.getSuperclass());
            }

            for (final Class<?> interf : test.getInterfaces())
            {
                stack.push(interf);
            }
        }

        return false;
    }

    /**
     * Indicates if given class is managed by {@link #defaultValue(Class)}.<br>
     * {@link #defaultValue(Class)} will return {@code null} for void or not managed class
     *
     * @param clazz Class to test
     * @return {@code true} if given class is managed by {@link #defaultValue(Class)}.
     */
    public static boolean managedByDefault(Class<?> clazz)
    {
        return Reflection.canBeConsideredAsVoid(clazz) || clazz.isEnum() || clazz.isPrimitive() || clazz.isArray()
               || Number.class.isAssignableFrom(clazz) || CharSequence.class.isAssignableFrom(clazz)
               || Character.class.equals(clazz) || Boolean.class.equals(clazz);
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and may use illegal argument for create the instance</li>
     * </ul>
     *
     * @param type Class type to create
     * @return Created instance
     */
    public static Object newInstance(Class<?> type)
    {
        if (type == null)
        {
            throw new NullPointerException("type MUST NOT be null");
        }
        if (Reflection.managedByDefault(type) && !type.isArray())
        {
            return Reflection.defaultValue(type);
        }

        if (type.isArray())
        {
            Class<?> componentType = type.getComponentType();
            int      count         = 1;

            while (componentType.isArray())
            {
                componentType = componentType.getComponentType();
                count++;
            }

            final int dim[] = new int[count];
            for (int i = 0; i < count; i++)
            {
                dim[i] = 1;
            }

            final Object o = Array.newInstance(componentType, dim);
            if (type.isPrimitive())
            {
                return o;
            }

            Object val = o;
            for (int i = 1; i < count; i++)
            {
                val = Array.get(val, 0);
            }

            Array.set(val, 0, Reflection.newInstance(componentType));

            return o;
        }

        final Constructor<?> constructors[]   = type.getConstructors();
        Constructor<?>       foundConstructor = null;
        int                  nb               = Integer.MAX_VALUE;
        Class<?>             types[]          = null;

        for (final Constructor<?> constructor : constructors)
        {
            final Class<?> typ[] = constructor.getParameterTypes();

            if ((typ == null) || (typ.length < nb) || (foundConstructor == null))
            {
                types = typ;
                foundConstructor = constructor;
                if (types == null)
                {
                    nb = 0;
                }
                else
                {
                    nb = types.length;
                }
            }
        }

        if (foundConstructor != null)
        {
            final Object arguments[] = new Object[nb];
            for (int i = 0; i < nb; i++)
            {
                arguments[i] = Reflection.newInstance(types[i]);
            }

            try
            {
                return foundConstructor.newInstance(arguments);
            }
            catch (final IllegalArgumentException | InvocationTargetException | IllegalAccessException |
                    InstantiationException ignored)
            {
            }
        }

        return null;
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and can use illegal argument for create the instance</li>
     * </ul>
     *
     * @param typeName Class name
     * @return Instance created
     * @throws ClassNotFoundException If the class can't be resolve
     */
    public static Object newInstance(final String typeName) throws ClassNotFoundException
    {
        if (typeName == null)
        {
            throw new NullPointerException("typeName MUST NOT be null");
        }
        return Reflection.newInstance(Class.forName(typeName));
    }

    /**
     * Try create an instance of a class.<br>
     * It first look for "little" public constructor, that is to say a constructor with few parameters, if it founds
     * default
     * constructor without parameters, it use it.<br>
     * If the constructor founds have parameters, it creates a instance of each parameters (on calling itself) an the use
     * them
     * for construct the instance.<br>
     * <b>BEWARE:</b>
     * <ul>
     * <li>In some case, it can falls in infinite loop</li>
     * <li>It is not human, so can't read documentation and can use illegal argument for create the instance</li>
     * </ul>
     *
     * @param typeName    Type name
     * @param classLoader Class loader to use
     * @return Instance created
     * @throws ClassNotFoundException If the class can't be resolve
     */
    public static Object newInstance(final String typeName, final ClassLoader classLoader) throws ClassNotFoundException
    {
        if (typeName == null)
        {
            throw new NullPointerException("typeName MUST NOT be null");
        }
        return Reflection.newInstance(classLoader.loadClass(typeName));
    }

    public static <I> Field obtainField(I instance, String name)
    {
        Class<?> clazz = instance.getClass();
        Field    field = null;

        while (clazz != null)
        {
            for (Field fieldLook : clazz.getDeclaredFields())
            {
                if (name.equals(fieldLook.getName()))
                {
                    field = fieldLook;
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }

        if (field == null)
        {
            throw new IllegalArgumentException(
                    "Filed '" + name + "' not found in class " + instance.getClass().getName());
        }

        field.setAccessible(true);
        return field;
    }

    /**
     * Obtain public method from a class
     *
     * @param clazz      Class where method lies
     * @param methodName Method name
     * @param types      Method parameters type
     * @return The method
     * @throws NoSuchMethodException If the method not public or not exists with specified types
     */
    public static Method obtainPublicMethod(final Class<?> clazz, final String methodName, final Class<?>... types)
            throws NoSuchMethodException
    {
        Method         method        = null;
        final Method[] publicMethods = clazz.getMethods();
        if (publicMethods != null)
        {
            for (int i = 0; (i < publicMethods.length) && (method == null); i++)
            {
                final Method m = publicMethods[i];
                if ((m.getName()
                      .equals(methodName)) && (Reflection.typeMatch(types, m.getParameterTypes())))
                {
                    method = m;
                }
            }
        }
        if (method == null)
        {
            final StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(clazz.getName());
            stringBuffer.append('.');
            stringBuffer.append(methodName);
            stringBuffer.append('(');
            if ((types != null) && (types.length > 0))
            {
                if (types[0] == null)
                {
                    stringBuffer.append("null");
                }
                else
                {
                    stringBuffer.append(types[0].getName());

                }
                for (int i = 1; i < types.length; i++)
                {
                    stringBuffer.append(", ");
                    if (types[i] == null)
                    {
                        stringBuffer.append("null");
                    }
                    else
                    {
                        stringBuffer.append(types[i].getName());

                    }
                }
            }
            stringBuffer.append(')');
            throw new NoSuchMethodException(stringBuffer.toString());
        }
        return method;
    }

    /**
     * Extract all types of given parameters.<br>
     * If the parameter is {@code null}, just {@code null} is return.<br>
     * If some parameters are {@code null}, the corresponding type was also {@code null}
     *
     * @param parameters Parameters to extract is types
     * @return Extracted types.
     */
    public static Class<?>[] obtainTypes(final Object... parameters)
    {
        if (parameters == null)
        {
            return null;
        }
        final int        length = parameters.length;
        final Class<?>[] types  = new Class[length];
        for (int i = 0; i < length; i++)
        {
            if (parameters[i] != null)
            {
                types[i] = parameters[i].getClass();
            }
        }
        return types;
    }

    /**
     * Indicates if 2 types are similar.<br>
     * {@code null} is accepted for parameters
     *
     * @param class1 First type
     * @param class2 Second type
     * @return {@code true} if types are similar
     */
    public static boolean typeMatch(final Class<?> class1, final Class<?> class2)
    {
        if (class1 == null)
        {
            if (class2 == null)
            {
                return true;
            }
            return !class2.isPrimitive();
        }
        if (class2 == null)
        {
            return !class1.isPrimitive();
        }
        if (class1.equals(class2))
        {
            return true;
        }
        if (class1.getName()
                  .equals(class2.getName()))
        {
            return true;
        }
        if (class1.isPrimitive())
        {
            if (boolean.class.equals(class1))
            {
                return class2.getName()
                             .equals(Boolean.class.getName());
            }
            if (byte.class.equals(class1))
            {
                return class2.getName()
                             .equals(Byte.class.getName());
            }
            if (short.class.equals(class1))
            {
                return class2.getName()
                             .equals(Short.class.getName());
            }
            if (int.class.equals(class1))
            {
                return class2.getName()
                             .equals(Integer.class.getName());
            }
            if (float.class.equals(class1))
            {
                return class2.getName()
                             .equals(Float.class.getName());
            }
            if (long.class.equals(class1))
            {
                return class2.getName()
                             .equals(Long.class.getName());
            }
            if (double.class.equals(class1))
            {
                return class2.getName()
                             .equals(Double.class.getName());
            }
            if (char.class.equals(class1))
            {
                return class2.getName()
                             .equals(Character.class.getName());
            }
        }
        if (class2.isPrimitive())
        {
            if (boolean.class.equals(class2))
            {
                return class1.getName()
                             .equals(Boolean.class.getName());
            }
            if (byte.class.equals(class2))
            {
                return class1.getName()
                             .equals(Byte.class.getName());
            }
            if (short.class.equals(class2))
            {
                return class1.getName()
                             .equals(Short.class.getName());
            }
            if (int.class.equals(class2))
            {
                return class1.getName()
                             .equals(Integer.class.getName());
            }
            if (float.class.equals(class2))
            {
                return class1.getName()
                             .equals(Float.class.getName());
            }
            if (long.class.equals(class2))
            {
                return class1.getName()
                             .equals(Long.class.getName());
            }
            if (double.class.equals(class2))
            {
                return class1.getName()
                             .equals(Double.class.getName());
            }
            if (char.class.equals(class2))
            {
                return class1.getName()
                             .equals(Character.class.getName());
            }
        }
        if ((class1.isArray()) && (class2.isArray()))
        {
            return Reflection.typeMatch(class1.getComponentType(), class2.getComponentType());
        }
        return false;
    }

    /**
     * Indicates if 2 arrays of types are similar.<br>
     * Array says similar if they have same length and each type of arrays are one to one similar<br>
     * {@code null} are consider like zero length array<br>
     * Each array can contains {@code null} elements.<br>
     * For the compare 2 types, it use {@link #typeMatch(Class, Class)}
     *
     * @param types1 First array
     * @param types2 Second array
     * @return {@code true} if arrays are similar
     */
    public static boolean typeMatch(final Class<?>[] types1, final Class<?>[] types2)
    {
        if (types1 == null)
        {
            return (types2 == null) || (types2.length == 0);
        }
        if (types2 == null)
        {
            return types1.length == 0;
        }
        if (types1.length != types2.length)
        {
            return false;
        }
        for (int i = 0; i < types1.length; i++)
        {
            if (!Reflection.typeMatch(types1[i], types2[i]))
            {
                return false;
            }
        }
        return true;
    }
}
