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
package jhelp.security.encrypt.noiseEncrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jhelp.security.encrypt.JHelpEncrypt;

/**
 * Encrypt with a key + noise
 */
public class NoiseEncrypt
        implements JHelpEncrypt
{
    /**
     * Encrypt singleton
     */
    public static NoiseEncrypt NOISE = new NoiseEncrypt();

    /**
     * Constructs NoiseEncrypt
     */
    private NoiseEncrypt()
    {
    }

    /**
     * Decrypt a message
     *
     * @param key           Key for decrypt
     * @param encryptStream Stream to decrypt
     * @param clearStream   Stream for write decrypted
     * @throws IOException On reading/writing problem
     * @see jhelp.security.encrypt.JHelpEncrypt#decrypt(byte[], InputStream, OutputStream)
     */
    @Override
    public void decrypt(final byte[] key, final InputStream encryptStream, final OutputStream clearStream)
            throws IOException
    {
        final int size = key.length;
        if (size < 5)
        {
            throw new IllegalArgumentException("the key must have at least 5 bytes");
        }

        int noise = encryptStream.read();
        while (noise == 0)
        {
            noise = encryptStream.read();
        }

        int read  = encryptStream.read();
        int index = 0;
        int value;

        while (read >= 0)
        {
            value = read - (key[index] & 0xFF);
            if (value < 0)
            {
                value += 256;
            }

            clearStream.write(value);
            index = (index + 1) % size;

            read = encryptStream.read();

            noise--;

            while (noise == 0)
            {
                noise = read;
                read = encryptStream.read();
            }
        }

        clearStream.flush();
    }

    /**
     * Encrypt with a key
     *
     * @param key           Key for encrypt
     * @param clearStream   Stream to encrypt
     * @param encryptStream Stream where write decrypted
     * @throws IOException On reading/writing problem
     * @see jhelp.security.encrypt.JHelpEncrypt#encrypt(byte[], InputStream, OutputStream)
     */
    @Override
    public void encrypt(final byte[] key, final InputStream clearStream, final OutputStream encryptStream)
            throws IOException
    {
        final int size = key.length;
        if (size < 5)
        {
            throw new IllegalArgumentException("the key must have at least 5 bytes");
        }

        final int choice  = Math.min(256, size);
        int       noise   = (int) (Math.random() * choice);
        int       decount = noise;

        encryptStream.write(noise);
        int read  = clearStream.read();
        int index = 0;

        while (read >= 0)
        {
            while (decount == 0)
            {
                noise = (int) (Math.random() * choice);
                encryptStream.write(noise);
                decount = noise;
            }

            decount--;

            encryptStream.write((read + (key[index] & 0xFF)) % 256);
            index = (index + 1) % size;

            read = clearStream.read();
        }

        encryptStream.flush();
    }
}