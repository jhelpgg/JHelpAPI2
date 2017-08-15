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
package jhelp.xml.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.StringTokenizer;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.reflection.Reflection;
import jhelp.util.text.StringExtractor;
import jhelp.util.text.UtilText;
import jhelp.util.xml.DynamicWriteXML;
import jhelp.xml.ExceptionParseXML;
import jhelp.xml.MarkupXML;

/**
 * Utilities for serialize/deserialize an object in XML.<br>
 * On serialization you can choose serialize only field annotate by {@link XMLSerializable}<br>
 * <br>
 * Last modification : 23 mai 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class XMLSerializer
{
    /**
     * Deserialize an object from a stream
     *
     * @param <T>
     *           Object type
     * @param clas
     *           Object class
     * @param inputStream
     *           Stream to read
     * @return Deserialized object
     * @throws IOException
     *            On reading problem
     * @throws ExceptionParseXML
     *            If stream is not a valid XML
     * @throws IllegalArgumentException
     *            If access field failed
     * @throws NoSuchFieldException
     *            If a field not exists
     * @throws IllegalAccessException
     *            If access field failed
     * @throws NoSuchMethodException
     *            If problem of deserialize enum, string buffer, ...
     * @throws InvocationTargetException
     *            If problem of deserialize enum, string buffer, ...
     * @throws URISyntaxException
     *            If problem while deserialize a file, URL or URI
     * @throws ClassNotFoundException
     *            If problem while deserialize an array
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(final Class<T> clas, final InputStream inputStream)
            throws IOException, ExceptionParseXML, IllegalArgumentException,
                   NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException,
                   URISyntaxException, ClassNotFoundException
    {
        final T object = (T) Reflection.newInstance(clas);

        final MarkupXML markupXML = MarkupXML.load(inputStream);

        XMLSerializer.deserialize(object, clas, markupXML);

        return object;
    }

    /**
     * Fill an instance of object on deserialize an XML
     *
     * @param object
     *           Object to fill
     * @param clas
     *           Object class
     * @param markupXML
     *           Markup to read
     * @throws IllegalArgumentException
     *            If access field failed
     * @throws NoSuchFieldException
     *            If a field not exists
     * @throws IllegalAccessException
     *            If access field failed
     * @throws NoSuchMethodException
     *            If problem of deserialize enum, string buffer, ...
     * @throws InvocationTargetException
     *            If problem of deserialize enum, string buffer, ...
     * @throws URISyntaxException
     *            If problem while deserialize a file, URL or URI
     * @throws MalformedURLException
     *            If problem while deserialize a file, URL or URI
     * @throws ClassNotFoundException
     *            If problem while deserialize an array
     */
    public static void deserialize(final Object object, final Class<?> clas, final MarkupXML markupXML)
            throws IllegalArgumentException, NoSuchFieldException,
                   IllegalAccessException, NoSuchMethodException, InvocationTargetException, MalformedURLException,
                   URISyntaxException, ClassNotFoundException
    {
        Field    field;
        Object   content;
        Class<?> fieldClass;
        String   value, fieldClassName;
        int      arraySize;

        if (!clas.getName()
                 .equals(markupXML.obtainParameter("name")))
        {
            throw new IllegalArgumentException(
                    "The class is " + clas.getName() + " but the XML is for " + markupXML.obtainParameter("name"));
        }

        for (final MarkupXML fieldMarkup : markupXML.obtainChildren())
        {
            field = Reflection.obtainField(object, fieldMarkup.getName());

            if (field != null)
            {
                fieldClass = field.getType();

                value = fieldMarkup.obtainParameter("value");
                arraySize = fieldMarkup.obtainParameter("array", -1);

                if ((fieldClass.isPrimitive()) && (value != null))
                {
                    fieldClassName = fieldClass.getName();

                    if (fieldClassName.equals("boolean"))
                    {
                        field.setBoolean(object, Boolean.parseBoolean(value));
                    }
                    else if (fieldClassName.equals("char"))
                    {
                        field.setChar(object, value.charAt(0));
                    }
                    else if (fieldClassName.equals("byte"))
                    {
                        field.setByte(object, Byte.parseByte(value));
                    }
                    else if (fieldClassName.equals("short"))
                    {
                        field.setShort(object, Short.parseShort(value));
                    }
                    else if (fieldClassName.equals("int"))
                    {
                        field.setInt(object, Integer.parseInt(value));
                    }
                    else if (fieldClassName.equals("long"))
                    {
                        field.setLong(object, Long.parseLong(value));
                    }
                    else if (fieldClassName.equals("float"))
                    {
                        field.setFloat(object, Float.parseFloat(value));
                    }
                    else if (fieldClassName.equals("double"))
                    {
                        field.setDouble(object, Double.parseDouble(value));
                    }
                }
                else if ((fieldClass.equals(String.class)) && (value != null))
                {
                    field.set(object, value);
                }
                else if (((fieldClass.isEnum()) || (fieldClass.equals(Boolean.class)) || (fieldClass.equals(
                        Character.class))
                          || (fieldClass.equals(Byte.class)) || (fieldClass.equals(Short.class)) || (fieldClass
                        .equals(
                                Integer.class))
                          || (fieldClass.equals(Long.class)) || (fieldClass.equals(Float.class)) || (fieldClass
                        .equals(
                                Double.class)))
                         && (value != null))
                {
                    field.set(object, Reflection.invokePublicMethod(fieldClass, "valueOf", value));
                }
                else if ((fieldClass.equals(StringBuilder.class)) && (value != null))
                {
                    field.set(object, new StringBuilder(UtilText.interpretAntiSlash(value)));
                }
                else if ((fieldClass.equals(StringBuffer.class)) && (value != null))
                {
                    field.set(object, new StringBuffer(UtilText.interpretAntiSlash(value)));
                }
                else if ((fieldClass.equals(URL.class)) && (value != null))
                {
                    field.set(object, new URL(UtilText.interpretAntiSlash(value)));
                }
                else if ((fieldClass.equals(URI.class)) && (value != null))
                {
                    field.set(object, new URI(UtilText.interpretAntiSlash(value)));
                }
                else if (fieldClass.equals(File.class))
                {
                    field.set(object, new File(UtilText.interpretAntiSlash(value)));
                }
                else if (fieldClass.isArray())
                {
                    if (arraySize >= 0)
                    {
                        Class<?> arrayClass = fieldClass.getComponentType();

                        final String realType = fieldMarkup.obtainParameter("arrayRealType");
                        if (realType != null)
                        {
                            arrayClass = Class.forName(realType);
                        }

                        if (arrayClass.isPrimitive())
                        {
                            final String arrayClassName = arrayClass.getName();

                            if (arrayClassName.equals("boolean"))
                            {
                                final boolean[]       temp            = new boolean[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Boolean.parseBoolean(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("char"))
                            {
                                final char[]          temp            = new char[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = stringTokenizer.nextToken().charAt(0);
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("byte"))
                            {
                                final byte[]          temp            = new byte[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Byte.parseByte(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("short"))
                            {
                                final short[]         temp            = new short[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Short.parseShort(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("int"))
                            {
                                final int[]           temp            = new int[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Integer.parseInt(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("long"))
                            {
                                final long[]          temp            = new long[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Long.parseLong(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("float"))
                            {
                                final float[]         temp            = new float[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Float.parseFloat(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                            else if (arrayClassName.equals("double"))
                            {
                                final double[]        temp            = new double[arraySize];
                                final StringTokenizer stringTokenizer = new StringTokenizer(fieldMarkup.getText());
                                int                   index           = 0;
                                while ((index < arraySize) && (stringTokenizer.hasMoreTokens()))
                                {
                                    temp[index++] = Double.parseDouble(stringTokenizer.nextToken());
                                }

                                field.set(object, temp);
                            }
                        }
                        else if (arrayClass.equals(String.class))
                        {
                            final String[]        array                = new String[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = UtilText.interpretAntiSlash(word);
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if ((arrayClass.isEnum()) || (arrayClass.equals(Boolean.class)) || (arrayClass
                                .equals(
                                        Character.class))
                                 || (arrayClass.equals(Byte.class)) || (arrayClass.equals(Short.class)) ||
                                 (arrayClass.equals(
                                         Integer.class))
                                 || (arrayClass.equals(Long.class)) || (arrayClass.equals(Float.class)) ||
                                 (arrayClass.equals(
                                         Double.class)))
                        {
                            final Object          array                = Array.newInstance(arrayClass, arraySize);
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    Array.set(array, index, Reflection.invokePublicMethod(arrayClass, "valueOf", word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if ((arrayClass.equals(StringBuilder.class)) && (value != null))
                        {
                            final StringBuilder[] array                = new StringBuilder[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = new StringBuilder(UtilText.interpretAntiSlash(word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if ((arrayClass.equals(StringBuffer.class)) && (value != null))
                        {
                            final StringBuffer[]  array                = new StringBuffer[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = new StringBuffer(UtilText.interpretAntiSlash(word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if ((arrayClass.equals(URL.class)) && (value != null))
                        {
                            final URL[]           array                = new URL[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = new URL(UtilText.interpretAntiSlash(word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if ((arrayClass.equals(URI.class)) && (value != null))
                        {
                            final URI[]           array                = new URI[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = new URI(UtilText.interpretAntiSlash(word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if (arrayClass.equals(File.class))
                        {
                            final File[]          array                = new File[arraySize];
                            final StringExtractor stringWordExctractor = new StringExtractor(fieldMarkup.getText());
                            int                   index                = 0;
                            String                word;
                            while (index < arraySize)
                            {
                                word = stringWordExctractor.next();

                                if (word == null)
                                {
                                    break;
                                }

                                if (!word.equals("NULL"))
                                {
                                    array[index] = new File(UtilText.interpretAntiSlash(word));
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                        else if (arrayClass.isArray())
                        {
                            throw new IllegalArgumentException("Multiple array not actually work");
                        }
                        else
                        {
                            final Object array = Array.newInstance(arrayClass, arraySize);

                            final EnumerationIterator<MarkupXML> enumeration = fieldMarkup.obtainChildren();
                            int                                  index       = 0;
                            MarkupXML                            xml;
                            Object                               temp;

                            while ((index < arraySize) && (enumeration.hasMoreElements()))
                            {
                                xml = enumeration.nextElement();

                                if (xml.childrenCount() > 0)
                                {
                                    temp = Reflection.newInstance(arrayClass);
                                    XMLSerializer.deserialize(temp, arrayClass, xml.getChild(0));
                                    Array.set(array, index, temp);
                                }

                                index++;
                            }

                            field.set(object, array);
                        }
                    }
                    else
                    {
                        field.set(object, null);
                    }
                }
                else
                {
                    if (fieldMarkup.childrenCount() > 0)
                    {
                        content = Reflection.newInstance(fieldClass);

                        XMLSerializer.deserialize(content, fieldClass, fieldMarkup.getChild(0));

                        field.set(object, content);
                    }
                    else
                    {
                        field.set(object, null);
                    }
                }
            }
        }
    }

    /**
     * Serialize an object. Only fields annotate by {@link XMLSerializable} will be serialize
     *
     * @param object
     *           Object to serialize
     * @param dynamicWriteXML
     *           XML writer for write the serialization
     * @throws IllegalArgumentException
     *            If access field failed
     * @throws IllegalAccessException
     *            If access field failed
     * @throws IOException
     *            On I/O issue
     */
    public static void serialize(final Object object, final DynamicWriteXML dynamicWriteXML)
            throws IllegalArgumentException, IllegalAccessException,
                   IOException
    {
        XMLSerializer.serialize(object, dynamicWriteXML, true);
    }

    /**
     * Serialize an object
     *
     * @param object
     *           Object to serialize
     * @param dynamicWriteXML
     *           XML writer for write the serialization
     * @param onlyAnnotated
     *           Indicates if only fields annotate by {@link XMLSerializable} will be serialize
     * @throws IllegalArgumentException
     *            If access field failed
     * @throws IllegalAccessException
     *            If access field failed
     * @throws IOException
     *            On I/O issue
     */
    public static void serialize(
            final Object object, final DynamicWriteXML dynamicWriteXML, final boolean onlyAnnotated)
            throws IllegalArgumentException,
                   IllegalAccessException, IOException
    {
        if (object == null)
        {
            throw new NullPointerException("object musn't be null");
        }

        if (dynamicWriteXML == null)
        {
            throw new NullPointerException("dynamicWriteXML musn't be null");
        }

        Class<?> fieldClass, arrayClass;
        String   arrayClassName, name;
        Object[] array;
        Object   value;
        int      size;

        Class<?> clas = object.getClass();

        dynamicWriteXML.openMarkup("class");
        dynamicWriteXML.appendParameter("name", clas.getName());

        while (clas != null)
        {
            for (final Field field : clas.getDeclaredFields())
            {
                // Final fileds don't need to be stored, because they can't change
                if (Modifier.isFinal(field.getModifiers()))
                {
                    continue;
                }

                field.setAccessible(true);
                name = field.getName();

                if ((name.charAt(0) != '$') && (!name.equals("serialVersionUID"))
                    // $ : For coverage, they add dummy data the we don't want !
                    // serialVersionUID : no need to store it
                    && ((!onlyAnnotated) || (field.getAnnotation(XMLSerializable.class) != null)))
                {
                    dynamicWriteXML.openMarkup(name);

                    fieldClass = field.getType();
                    value = field.get(object);

                    if (value != null)
                    {
                        if ((fieldClass.isPrimitive()) || (fieldClass.equals(String.class)) || (fieldClass
                                .isEnum())
                            || (fieldClass.equals(Boolean.class)) || (fieldClass.equals(Character.class))
                            || (fieldClass.equals(Byte.class)) || (fieldClass.equals(Short.class)) ||
                            (fieldClass.equals(
                                    Integer.class))
                            || (fieldClass.equals(Long.class)) || (fieldClass.equals(Float.class)) ||
                            (fieldClass.equals(
                                    Double.class))
                            || (fieldClass.equals(StringBuilder.class)) || (fieldClass.equals(StringBuffer.class))
                            || (fieldClass.equals(URL.class)) || (fieldClass.equals(URI.class)))
                        {
                            dynamicWriteXML.appendParameter("value", UtilText.addAntiSlash(value.toString(), false));
                        }
                        else if (fieldClass.equals(File.class))
                        {
                            dynamicWriteXML.appendParameter("value",
                                                            UtilText.addAntiSlash(((File) value).getAbsolutePath(),
                                                                                  false));
                        }
                        else if (fieldClass.isArray())
                        {
                            arrayClass = fieldClass.getComponentType();

                            if (arrayClass.isPrimitive())
                            {
                                arrayClassName = arrayClass.getName();

                                if (arrayClassName.equals("boolean"))
                                {
                                    final boolean[] temp = (boolean[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("char"))
                                {
                                    final char[] temp = (char[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("byte"))
                                {
                                    final byte[] temp = (byte[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("short"))
                                {
                                    final short[] temp = (short[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("int"))
                                {
                                    final int[] temp = (int[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("long"))
                                {
                                    final long[] temp = (long[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("float"))
                                {
                                    final float[] temp = (float[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                                else if (arrayClassName.equals("double"))
                                {
                                    final double[] temp = (double[]) value;
                                    size = temp.length;
                                    dynamicWriteXML.appendParameter("array", size);

                                    final StringBuilder stringBuffer = new StringBuilder();
                                    for (int i = 0; i < size; i++)
                                    {
                                        stringBuffer.append(temp[i]);
                                        stringBuffer.append(' ');
                                    }

                                    dynamicWriteXML.setText(stringBuffer.toString());
                                }
                            }
                            else if ((arrayClass.equals(String.class)) || (arrayClass.isEnum()) || (arrayClass
                                    .equals(
                                            Boolean.class))
                                     || (arrayClass.equals(Character.class)) || (arrayClass.equals(Byte.class))
                                     || (arrayClass.equals(Short.class)) || (arrayClass.equals(
                                    Integer.class)) || (arrayClass.equals(
                                    Long.class))
                                     || (arrayClass.equals(Float.class)) || (arrayClass.equals(Double.class))
                                     || (arrayClass.equals(StringBuilder.class)) ||
                                     (arrayClass.equals(StringBuffer.class))
                                     || (arrayClass.equals(URL.class)) || (arrayClass.equals(URI.class)))
                            {
                                array = (Object[]) value;

                                size = array.length;
                                dynamicWriteXML.appendParameter("array", size);

                                final StringBuilder stringBuffer = new StringBuilder();
                                for (int i = 0; i < size; i++)
                                {
                                    if (array[i] == null)
                                    {
                                        stringBuffer.append("NULL");
                                    }
                                    else
                                    {
                                        stringBuffer.append('"');
                                        stringBuffer.append(UtilText.addAntiSlash(array[i].toString(), false));
                                        stringBuffer.append('"');
                                    }
                                    stringBuffer.append(' ');
                                }

                                dynamicWriteXML.setText(stringBuffer.toString());
                            }
                            else if (arrayClass.equals(File.class))
                            {
                                array = (Object[]) value;

                                size = array.length;
                                dynamicWriteXML.appendParameter("array", size);

                                final StringBuilder stringBuffer = new StringBuilder();
                                for (int i = 0; i < size; i++)
                                {
                                    if (array[i] == null)
                                    {
                                        stringBuffer.append("NULL");
                                    }
                                    else
                                    {
                                        stringBuffer.append('"');
                                        stringBuffer.append(
                                                UtilText.addAntiSlash(((File) array[i]).getAbsolutePath(), false));
                                        stringBuffer.append('"');
                                    }
                                    stringBuffer.append(' ');
                                }

                                dynamicWriteXML.setText(stringBuffer.toString());
                            }
                            else if (arrayClass.isArray())
                            {
                                throw new IllegalArgumentException("Can't serialze multiple array for now");
                            }
                            else
                            {
                                array = (Object[]) value;

                                size = array.length;

                                if ((size > 0) && (array[0] != null))
                                {
                                    final Class<?> realClass = array[0].getClass();

                                    if (arrayClass.equals(realClass))
                                    {
                                        boolean cohrent = true;

                                        for (int i = 1; i < size; i++)
                                        {
                                            if ((array[i] != null) && (!array[i].getClass()
                                                                                .equals(realClass)))
                                            {
                                                cohrent = false;

                                                break;
                                            }
                                        }

                                        if (cohrent)
                                        {
                                            arrayClass = realClass;
                                            dynamicWriteXML.appendParameter("arrayRealType", arrayClass.getName());

                                            if (arrayClass.isPrimitive())
                                            {
                                                arrayClassName = arrayClass.getName();

                                                if (arrayClassName.equals("boolean"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final boolean[] temp = (boolean[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("char"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final char[] temp = (char[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("byte"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final byte[] temp = (byte[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("short"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final short[] temp = (short[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("int"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final int[] temp = (int[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("long"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final long[] temp = (long[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("float"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final float[] temp = (float[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                                else if (arrayClassName.equals("double"))
                                                {
                                                    @SuppressWarnings("ConstantConditions") final double[] temp = (double[]) value;
                                                    size = temp.length;
                                                    dynamicWriteXML.appendParameter("array", size);

                                                    final StringBuilder stringBuffer = new StringBuilder();
                                                    for (int i = 0; i < size; i++)
                                                    {
                                                        stringBuffer.append(temp[i]);
                                                        stringBuffer.append(' ');
                                                    }

                                                    dynamicWriteXML.setText(stringBuffer.toString());
                                                }
                                            }
                                            else if ((arrayClass.equals(String.class)) || (arrayClass.isEnum())
                                                     || (arrayClass.equals(Boolean.class)) || (arrayClass.equals(
                                                    Character.class))
                                                     || (arrayClass.equals(Byte.class)) ||
                                                     (arrayClass.equals(Short.class))
                                                     || (arrayClass.equals(Integer.class)) ||
                                                     (arrayClass.equals(Long.class))
                                                     || (arrayClass.equals(Float.class)) ||
                                                     (arrayClass.equals(Double.class))
                                                     || (arrayClass.equals(StringBuilder.class)) || (arrayClass.equals(
                                                    StringBuffer.class))
                                                     || (arrayClass.equals(URL.class)) ||
                                                     (arrayClass.equals(URI.class)))
                                            {
                                                array = (Object[]) value;

                                                size = array.length;
                                                dynamicWriteXML.appendParameter("array", size);

                                                final StringBuilder stringBuffer = new StringBuilder();
                                                for (int i = 0; i < size; i++)
                                                {
                                                    if (array[i] == null)
                                                    {
                                                        stringBuffer.append("NULL");
                                                    }
                                                    else
                                                    {
                                                        stringBuffer.append('"');
                                                        stringBuffer.append(
                                                                UtilText.addAntiSlash(array[i].toString(), false));
                                                        stringBuffer.append('"');
                                                    }
                                                    stringBuffer.append(' ');
                                                }

                                                dynamicWriteXML.setText(stringBuffer.toString());
                                            }
                                            else if (arrayClass.equals(File.class))
                                            {
                                                array = (Object[]) value;

                                                size = array.length;
                                                dynamicWriteXML.appendParameter("array", size);

                                                final StringBuilder stringBuffer = new StringBuilder();
                                                for (int i = 0; i < size; i++)
                                                {
                                                    if (array[i] == null)
                                                    {
                                                        stringBuffer.append("NULL");
                                                    }
                                                    else
                                                    {
                                                        stringBuffer.append('"');
                                                        stringBuffer.append(UtilText.addAntiSlash(
                                                                ((File) array[i]).getAbsolutePath(), false));
                                                        stringBuffer.append('"');
                                                    }
                                                    stringBuffer.append(' ');
                                                }

                                                dynamicWriteXML.setText(stringBuffer.toString());
                                            }
                                            else if (arrayClass.isArray())
                                            {
                                                throw new IllegalArgumentException(
                                                        "Can't serialze multiple array for now");
                                            }
                                            else
                                            {
                                                dynamicWriteXML.appendParameter("array", size);

                                                for (int i = 0; i < size; i++)
                                                {
                                                    dynamicWriteXML.openMarkup("item");

                                                    if (array[i] != null)
                                                    {
                                                        XMLSerializer.serialize(array[i], dynamicWriteXML,
                                                                                onlyAnnotated);
                                                    }

                                                    dynamicWriteXML.closeMarkup();
                                                }
                                            }
                                        }
                                        else
                                        {
                                            dynamicWriteXML.appendParameter("array", size);

                                            for (int i = 0; i < size; i++)
                                            {
                                                dynamicWriteXML.openMarkup("item");

                                                if (array[i] != null)
                                                {
                                                    XMLSerializer.serialize(array[i], dynamicWriteXML, onlyAnnotated);
                                                }

                                                dynamicWriteXML.closeMarkup();
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    dynamicWriteXML.appendParameter("array", size);

                                    for (int i = 0; i < size; i++)
                                    {
                                        dynamicWriteXML.openMarkup("item");

                                        if (array[i] != null)
                                        {
                                            XMLSerializer.serialize(array[i], dynamicWriteXML, onlyAnnotated);
                                        }

                                        dynamicWriteXML.closeMarkup();
                                    }
                                }
                            }
                        }
                        else
                        {
                            XMLSerializer.serialize(value, dynamicWriteXML, onlyAnnotated);
                        }
                    }

                    dynamicWriteXML.closeMarkup();
                }
            }

            clas = clas.getSuperclass();
        }

        dynamicWriteXML.closeMarkup();
    }
}