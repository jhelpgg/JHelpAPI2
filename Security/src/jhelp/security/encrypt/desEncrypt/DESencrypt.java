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
package jhelp.security.encrypt.desEncrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import jhelp.security.encrypt.JHelpEncrypt;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;

/**
 * DES encryption
 */
public class DESencrypt
        implements JHelpEncrypt
{
    /**
     * DES encryption singleton
     */
    public static final DESencrypt DES = new DESencrypt();

    /**
     * Compute a valid size key from a string
     *
     * @param password Password to transform on key
     * @return Valid size key
     */
    public static byte[] computeKey(final String password)
    {
        final char[] car  = password.toCharArray();
        long         hash = 0;

        for (final char c : car)
        {
            hash = (31L * hash) + c;
        }

        final byte[] key = new byte[8];
        for (int i = 0; i < 8; i++)
        {
            key[i] = (byte) (hash & 0xFF);

            hash >>= 8;
        }

        return key;
    }

    /**
     * Transform a "random" size key to good size key
     *
     * @param keySource Key to transform
     * @return Good size key
     */
    public static byte[] computeSecureKey(final byte[] keySource)
    {
        long hash = 0;

        for (final byte b : keySource)
        {
            hash = (31L * hash) + (b & 0xFF);
        }

        final byte[] key = new byte[8];
        for (int i = 0; i < 8; i++)
        {
            key[i] = (byte) (hash & 0xFF);

            hash >>= 8;
        }

        return key;
    }

    /**
     * Constructs DESencrypt
     */
    private DESencrypt()
    {
    }

    /**
     * Decrypt a stream
     *
     * @param key           Key to use
     * @param encryptStream Encrypted stream
     * @param clearStream   Clear stream result
     * @throws IOException On read/write issue
     * @see jhelp.security.encrypt.JHelpEncrypt#decrypt(byte[], InputStream, OutputStream)
     */
    @Override
    public void decrypt(byte[] key, InputStream encryptStream, final OutputStream clearStream) throws IOException
    {
        try
        {
            encryptStream = new NoiseInputStream(encryptStream);

            if (key.length != 8)
            {
                key = DESencrypt.computeSecureKey(key);
            }

            final DESKeySpec       desKeySpec = new DESKeySpec(key);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey        secretKey  = keyFactory.generateSecret(desKeySpec);

            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            final byte[]            temp              = new byte[8];
            final CipherInputStream cipherInputStream = new CipherInputStream(encryptStream, cipher);

            int read = cipherInputStream.read(temp);
            while (read >= 0)
            {
                clearStream.write(temp, 0, read);

                read = cipherInputStream.read(temp);
            }
            clearStream.flush();
            cipherInputStream.close();
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't decrypt message", exception);
        }
    }

    /**
     * Encrypt a stream
     *
     * @param key           Key to use
     * @param clearStream   Clear stream
     * @param encryptStream Encrypted stream result
     * @throws IOException On read/write issue
     * @see jhelp.security.encrypt.JHelpEncrypt#encrypt(byte[], InputStream, OutputStream)
     */
    @Override
    public void encrypt(byte[] key, final InputStream clearStream, OutputStream encryptStream) throws IOException
    {
        try
        {
            encryptStream = new NoiseOutputStream(encryptStream);

            if (key.length != 8)
            {
                key = DESencrypt.computeSecureKey(key);
            }

            final DESKeySpec       desKeySpec = new DESKeySpec(key);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey        secretKey  = keyFactory.generateSecret(desKeySpec);

            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            final byte[]            temp              = new byte[8];
            final CipherInputStream cipherInputStream = new CipherInputStream(clearStream, cipher);

            int read = cipherInputStream.read(temp);
            while (read >= 0)
            {
                encryptStream.write(temp, 0, read);

                read = cipherInputStream.read(temp);
            }
            encryptStream.flush();
            cipherInputStream.close();
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't encrypt message", exception);
        }
    }

    /**
     * Decrypt a stream
     *
     * @param password      Password to use
     * @param encryptStream Encrypted stream
     * @param clearStream   Clear stream result
     * @throws IOException On read/write issue
     */
    public void decrypt(final String password, final InputStream encryptStream, final OutputStream clearStream)
            throws IOException
    {
        this.decrypt(DESencrypt.computeKey(password), encryptStream, clearStream);
    }

    /**
     * Encrypt a stream
     *
     * @param password      Password to use
     * @param clearStream   Clear stream
     * @param encryptStream Encrypted stream result
     * @throws IOException On read/write issue
     */
    public void encrypt(final String password, final InputStream clearStream, final OutputStream encryptStream)
            throws IOException
    {
        this.encrypt(DESencrypt.computeKey(password), clearStream, encryptStream);
    }
}