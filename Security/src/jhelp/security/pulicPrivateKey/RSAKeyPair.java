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
package jhelp.security.pulicPrivateKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.Cipher;
import jhelp.security.encrypt.desEncrypt.DESencrypt;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * RSA private and public key pair.<br>
 * For example A and B wants exchange message, A and B compute there key pair<br>
 * A give its public key to B and B its own to A. Each time A wants send message to B it use B's public key to encrypt, and only
 * B with its private key can decrypt
 */
class RSAKeyPair
        implements JHelpKeyPairs<RSAPublicKey>
{
    /**
     * name of the equivalent cipher alg
     */
    final static String RSA_CIPHER = "RSA/ECB/PKCS1Padding";

    /**
     * name of the special signature alg
     */
    final static String RSA_SIGNATURE = "NONEwithRSA";
    /**
     * Private key
     */
    private       PrivateKey   privateKey;
    /**
     * Public key
     */
    private final RSAPublicKey publicKey;

    /**
     * Constructs RSAKeyPair
     *
     * @throws NoSuchAlgorithmException If RSA not support by the JVM
     */
    public RSAKeyPair()
            throws NoSuchAlgorithmException
    {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        final KeyPair keyPair = generator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = new RSAPublicKey(keyPair.getPublic());
    }

    /**
     * Decrypt a message encrypt by the public key
     *
     * @param inputStream  Encrypted stream
     * @param outputStream Stream where write clear message
     * @throws IOException On read/write issue
     * @see jhelp.security.pulicPrivateKey.JHelpKeyPairs#decrypt(InputStream, OutputStream)
     */
    @Override
    public void decrypt(InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        try
        {
            inputStream = new NoiseInputStream(inputStream);

            final Cipher cipher = Cipher.getInstance(RSAKeyPair.RSA_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            final byte[] temp = new byte[256];

            int size = inputStream.read();
            if (size == 0)
            {
                size = 256;
            }
            int read = UtilIO.readStream(inputStream, temp, 0, size);

            while ((read >= 0) && (size >= 0))
            {
                outputStream.write(cipher.doFinal(temp, 0, read));

                size = inputStream.read();
                if (size >= 0)
                {
                    if (size == 0)
                    {
                        size = 256;
                    }

                    read = UtilIO.readStream(inputStream, temp, 0, size);
                }
            }

            outputStream.flush();
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't decrypt !", exception);
        }
    }

    /**
     * Public key to share to those who wants send crypted message
     *
     * @return Public key
     * @see jhelp.security.pulicPrivateKey.JHelpKeyPairs#getPublicKey()
     */
    @Override
    public RSAPublicKey getPublicKey()
    {
        return this.publicKey;
    }

    /**
     * Load a pair saved previously by {@link #storePair(String, byte[])}. Since this contains the private key make sure to store
     * it in private secure zone and never share it
     *
     * @param storeName Storage name
     * @param key       Key/password
     * @throws IOException On reading issue
     * @see jhelp.security.pulicPrivateKey.JHelpKeyPairs#loadPair(String, byte[])
     */
    @Override
    public void loadPair(final String storeName, final byte[] key) throws IOException
    {
        try
        {
            final File file = UtilIO.obtainExternalFile(storeName);
            if (!file.exists())
            {
                throw new FileNotFoundException(
                        UtilText.concatenate("The specified store name ", storeName, " doesn't exists ! Path=",
                                             file.getAbsolutePath()));
            }

            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final FileInputStream       fileInputStream = new FileInputStream(file);
            final ByteArrayOutputStream outputStream    = new ByteArrayOutputStream();

            DESencrypt.DES.decrypt(key, fileInputStream, outputStream);

            final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // Special private key, to not use external method with it
            int length = inputStream.read() << 24;
            length |= inputStream.read() << 16;
            length |= inputStream.read() << 8;
            length |= inputStream.read();

            byte[] temp = new byte[length];

            int index = 0;
            int read  = inputStream.read(temp);
            if (read >= 0)
            {
                length -= read;
                index += read;
            }

            while ((read >= 0) && (length > 0))
            {
                read = inputStream.read(temp, index, length);

                if (read >= 0)
                {
                    length -= read;
                    index += read;
                }
            }

            final BigInteger modulus = new BigInteger(temp);

            length = inputStream.read() << 24;
            length |= inputStream.read() << 16;
            length |= inputStream.read() << 8;
            length |= inputStream.read();

            temp = new byte[length];

            index = 0;
            read = inputStream.read(temp);
            if (read >= 0)
            {
                length -= read;
                index += read;
            }

            while ((read >= 0) && (length > 0))
            {
                read = inputStream.read(temp, index, length);

                if (read >= 0)
                {
                    length -= read;
                    index += read;
                }
            }

            final BigInteger exponent = new BigInteger(temp);
            temp = null;

            final RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            this.privateKey = keyFactory.generatePrivate(privateKeySpec);
            //

            this.publicKey.deserialize(inputStream);

            fileInputStream.close();
        }
        catch (final Exception exception)
        {
            throw new IOException("load issue !", exception);
        }
    }

    /**
     * Sign a message
     *
     * @param message   Message to sign (not too big)
     * @param signature Signature generated
     * @throws IOException On read/write issue
     * @see jhelp.security.pulicPrivateKey.JHelpKeyPairs#sign(InputStream, OutputStream)
     */
    @Override
    public void sign(final InputStream message, OutputStream signature) throws IOException
    {
        try
        {
            signature = new NoiseOutputStream(signature);

            final Signature sign = Signature.getInstance(RSAKeyPair.RSA_SIGNATURE);
            sign.initSign(this.privateKey);
            final byte[] temp = new byte[4096];

            int read = message.read(temp);
            while (read >= 0)
            {
                sign.update(temp, 0, read);

                read = message.read(temp);
            }

            signature.write(sign.sign());
            signature.flush();
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't sign !", exception);
        }
    }

    /**
     * Store the pair . Since this contains the private key make sure to store it in private secure zone and never share it. To
     * read the data again use {@link #loadPair(String, byte[])}
     *
     * @param storeName Storage name
     * @param key       Key/password
     * @throws IOException On read/write issue
     * @see jhelp.security.pulicPrivateKey.JHelpKeyPairs#storePair(String, byte[])
     */
    @Override
    public void storePair(final String storeName, final byte[] key) throws IOException
    {
        try
        {
            final File file = UtilIO.obtainExternalFile(storeName);
            if (!UtilIO.createFile(file))
            {
                throw new FileNotFoundException(
                        UtilText.concatenate("The specified store name ", storeName, " can't be created ! Path=",
                                             file.getAbsolutePath()));
            }

            final KeyFactory        keyFactory     = KeyFactory.getInstance("RSA");
            final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(this.privateKey, RSAPrivateKeySpec.class);

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Special private key, to not use external method with it
            byte[] temp   = privateKeySpec.getModulus().toByteArray();
            int    length = temp.length;

            byteArrayOutputStream.write((length >> 24) & 0xFF);
            byteArrayOutputStream.write((length >> 16) & 0xFF);
            byteArrayOutputStream.write((length >> 8) & 0xFF);
            byteArrayOutputStream.write(length & 0xFF);

            byteArrayOutputStream.flush();
            byteArrayOutputStream.write(temp);

            byteArrayOutputStream.flush();

            temp = privateKeySpec.getPrivateExponent().toByteArray();
            length = temp.length;

            byteArrayOutputStream.write((length >> 24) & 0xFF);
            byteArrayOutputStream.write((length >> 16) & 0xFF);
            byteArrayOutputStream.write((length >> 8) & 0xFF);
            byteArrayOutputStream.write(length & 0xFF);

            byteArrayOutputStream.flush();
            byteArrayOutputStream.write(temp);

            byteArrayOutputStream.flush();

            temp = null;
            //

            this.publicKey.serialize(byteArrayOutputStream);

            final ByteArrayInputStream inputStream      = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            final FileOutputStream     fileOutputStream = new FileOutputStream(file);

            DESencrypt.DES.encrypt(key, inputStream, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

        }
        catch (final Exception exception)
        {
            throw new IOException("Store issue !", exception);
        }
    }
}