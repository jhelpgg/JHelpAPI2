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

package jhelp.security.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jhelp.security.encrypt.desEncrypt.DESencrypt;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;
import jhelp.security.pulicPrivateKey.FactoryKeyPairs;
import jhelp.security.pulicPrivateKey.JHelpKeyPairs;
import jhelp.security.pulicPrivateKey.JHelpPublicKey;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * Encrypt utilities
 */
public class UtilEncrypt
{
    /**
     * Decryption way
     */
    public static final String MODE_DECRYPT = "decrypt";
    /**
     * Encryption way
     */
    public static final String MODE_ENCRYPT = "encrypt";

    /**
     * Decrypt a file encrypt with {@link #encrypt(String, File, File)}. Think about give the good name to the encrypted file,
     * else decryption will failed
     *
     * @param password    Password
     * @param source      Encrypted file
     * @param destination Clear file to write
     */
    public static void decrypt(final String password, final File source, final File destination)
    {
        ZipInputStream   zipInputStream   = null;
        ZipEntry         zipEntry         = null;
        FileOutputStream fileOutputStream = null;

        try
        {
            final File privateKey = UtilIO.obtainExternalFile("tempUtilEncrpyt");
            UtilIO.createFile(privateKey);

            final String name  = source.getName();
            final String other = UtilText.concatenate(password, "_JHELP_", name);

            zipInputStream = new ZipInputStream(
                    new NoiseInputStream(new NoiseInputStream(new NoiseInputStream(new FileInputStream(source)))));
            zipEntry = zipInputStream.getNextEntry();
            if (!zipEntry.getName()
                         .equals("a"))
            {
                throw new Exception("First is " + zipEntry.getName());
            }

            fileOutputStream = new FileOutputStream(privateKey);
            UtilEncrypt.tripleDESdecrypt(password, name, other, zipInputStream, fileOutputStream);
            zipInputStream.closeEntry();
            zipEntry = null;
            fileOutputStream.close();
            fileOutputStream = null;

            final JHelpKeyPairs<JHelpPublicKey> keyPair = FactoryKeyPairs.obtainKeyPair(FactoryKeyPairs.ALGORITHM_RSA);
            final byte[]                        key     = DESencrypt.computeKey(password);
            keyPair.loadPair("tempUtilEncrpyt", key);
            UtilIO.delete(privateKey);

            UtilIO.createFile(destination);
            zipEntry = zipInputStream.getNextEntry();
            if (!zipEntry.getName()
                         .equals("b"))
            {
                throw new Exception("Second is " + zipEntry.getName());
            }

            fileOutputStream = new FileOutputStream(destination);
            keyPair.decrypt(zipInputStream, fileOutputStream);
            zipInputStream.closeEntry();
            zipEntry = null;
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
            throw new RuntimeException();
        }
        finally
        {
            if (zipInputStream != null)
            {
                if (zipEntry != null)
                {
                    try
                    {
                        zipInputStream.closeEntry();
                    }
                    catch (final Exception ignored)
                    {
                    }
                }

                try
                {
                    zipInputStream.close();
                }
                catch (final Exception ignored)
                {
                }
            }

            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Encrypt a file. Can be decrypt with {@link #decrypt(String, File, File)}. Beware don't rename the destination file, or
     * remember its original name. The file name is use inside the encryption, it can add some security if you rename the file
     * then rename it to original name before decrypt it
     *
     * @param password    Password
     * @param source      File to encrypt
     * @param destination Encrypted file
     */
    public static void encrypt(final String password, final File source, final File destination)
    {
        try
        {
            UtilIO.createFile(destination);
            final String name  = destination.getName();
            final String other = UtilText.concatenate(password, "_JHELP_", name);

            final byte[] key = DESencrypt.computeKey(password);

            final JHelpKeyPairs<JHelpPublicKey> keyPair = FactoryKeyPairs.obtainKeyPair(FactoryKeyPairs.ALGORITHM_RSA);
            keyPair.storePair("tempUtilEncrpyt", key);

            final File privateKey = UtilIO.obtainExternalFile("tempUtilEncrpyt");

            FileInputStream fileInputStream = new FileInputStream(privateKey);
            final ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new NoiseOutputStream(new NoiseOutputStream(new NoiseOutputStream(new FileOutputStream(
                            destination)))));

            ZipEntry zipEntry = new ZipEntry("a");
            zipOutputStream.putNextEntry(zipEntry);

            UtilEncrypt.tripleDESencrypt(password, name, other, fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
            UtilIO.delete(privateKey);

            fileInputStream = new FileInputStream(source);
            zipEntry = new ZipEntry("b");
            zipOutputStream.putNextEntry(zipEntry);
            keyPair.getPublicKey().encrypt(fileInputStream, zipOutputStream);
            fileInputStream.close();
            zipOutputStream.flush();
            zipOutputStream.closeEntry();

            zipOutputStream.flush();
            zipOutputStream.close();
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
            throw new RuntimeException();
        }
    }

    /**
     * For be use in batch mode. Usage : <code>
     * UtilEncrypt &lt;mode&gt; &lt;password&gt; &lt;pathSource&gt; &lt;pathDestination&gt;<br>
     * &lt;mode&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Way of encryption it is 'encrypt' or 'decrypt'<br>
     * &lt;password&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Password to use<br>
     * &lt;pathSource&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Source file to encrypt/decrypt<br>
     * &lt;pathDestination&gt;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Encrypted/clear file
     * </code>
     *
     * @param args Arguments
     */
    public static void main(final String[] args)
    {
        if ((args == null) || (args.length < 4))
        {
            UtilEncrypt.printUsage();

            return;
        }

        try
        {
            boolean      encrypt = true;
            final String mode    = args[0].toLowerCase();

            if (UtilEncrypt.MODE_ENCRYPT.equals(mode))
            {
                encrypt = true;
            }
            else if (UtilEncrypt.MODE_DECRYPT.equals(mode))
            {
                encrypt = false;
            }
            else
            {
                UtilEncrypt.printUsage();

                return;
            }

            final String password    = args[1];
            final File   source      = new File(args[2]);
            final File   destination = new File(args[3]);

            if (encrypt)
            {
                UtilEncrypt.encrypt(password, source, destination);
            }
            else
            {
                UtilEncrypt.decrypt(password, source, destination);
            }
        }
        catch (final Exception exception)
        {
            Debug.information(exception);

            Debug.information("");
            Debug.information("--------------------------------");
            Debug.information("");

            UtilEncrypt.printUsage();
        }
    }

    /**
     * Print the usage
     */
    private static void printUsage()
    {
        Debug.information("UtilEncrypt <mode> <password> <pathSource> <pathDestination>");
        Debug.information("\t<mode>\t\t\t:\tWay of encryption it is 'encrypt' or 'decrypt'");
        Debug.information("\t<password>\t\t:\tPassword to use");
        Debug.information("\t<pathSource>\t\t:\tSource file to be encrypt or decrypt");
        Debug.information("\t<pathDestination>\t:\tDestination file where write encrypted/decrypted data");
    }

    /**
     * Decrypt an encrypted stream by {@link #tripleDESencrypt(String, String, String, InputStream, OutputStream)}
     *
     * @param key1         First key
     * @param key2         Second key
     * @param key3         Third key
     * @param inputStream  Encrypted stream
     * @param outputStream Clear stream where write
     * @throws IOException On read/write issue
     */
    public static void tripleDESdecrypt(
            final String key1, final String key2, final String key3, final InputStream inputStream,
            final OutputStream outputStream)
            throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DESencrypt.DES.decrypt(key1, inputStream, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream = new ByteArrayOutputStream();
        DESencrypt.DES.decrypt(key2, byteArrayInputStream, byteArrayOutputStream);

        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DESencrypt.DES.decrypt(key3, byteArrayInputStream, outputStream);
    }

    /**
     * Encrypt in Triple DES. Can be decrypt with {@link #tripleDESdecrypt(String, String, String, InputStream, OutputStream)}
     *
     * @param key1         First key
     * @param key2         Second key
     * @param key3         Third key
     * @param inputStream  Stream to encrypt
     * @param outputStream Encrypted stream
     * @throws IOException On read/write issue
     */
    public static void tripleDESencrypt(
            final String key1, final String key2, final String key3, final InputStream inputStream,
            final OutputStream outputStream)
            throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DESencrypt.DES.encrypt(key3, inputStream, byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream = new ByteArrayOutputStream();
        DESencrypt.DES.encrypt(key2, byteArrayInputStream, byteArrayOutputStream);

        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DESencrypt.DES.encrypt(key1, byteArrayInputStream, outputStream);
    }
}