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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;

/**
 * Factory to get key pair with algorithm name
 */
public class FactoryKeyPairs
{
    static
    {
        final HashMap<String, Class<? extends JHelpKeyPairs<? extends JHelpPublicKey>>> algorithms = new HashMap<String, Class<? extends JHelpKeyPairs<? extends JHelpPublicKey>>>();

        algorithms.put(FactoryKeyPairs.ALGORITHM_RSA.toLowerCase(), RSAKeyPair.class);

        ALGORITHMS = algorithms;
    }

    /**
     * List of known algorithm
     */
    private static final HashMap<String, Class<? extends JHelpKeyPairs<? extends JHelpPublicKey>>> ALGORITHMS;
    /**
     * RSA algorithm
     */
    public static final String ALGORITHM_RSA = "RSA";

    /**
     * Deserialize a public key
     *
     * @param inputStream Stream to read
     * @param algorithm   Algorithm to use
     * @return Read public key
     * @throws IOException On read issue
     */
    public static JHelpPublicKey desrializePublicKey(final InputStream inputStream, final String algorithm)
            throws IOException
    {
        if (FactoryKeyPairs.ALGORITHM_RSA.equals(algorithm.toLowerCase()))
        {
            final RSAPublicKey rsaPublicKey = new RSAPublicKey(null);
            rsaPublicKey.deserialize(inputStream);
            return rsaPublicKey;
        }

        throw new NoSuchAlgorithmException(algorithm, new Exception("No such algorithm"));
    }

    /**
     * Generate a certificate
     *
     * @param <PUBLIC_KEY> Public key type
     * @param keyPair      Key pair to use
     * @param outputStream Stream where write certificate
     * @throws IOException On writing issue
     */
    public static <PUBLIC_KEY extends JHelpPublicKey> void generateCertificate(
            final JHelpKeyPairs<PUBLIC_KEY> keyPair, final OutputStream outputStream)
            throws IOException
    {
        final NoiseOutputStream noiseOutputStream = new NoiseOutputStream(outputStream);

        final byte[] messageRandom = new byte[111];
        int          value;

        for (int i = 0; i < 111; i++)
        {
            value = (int) (Math.random() * 256);

            messageRandom[i] = (byte) value;
            noiseOutputStream.write(value);
        }

        noiseOutputStream.flush();

        keyPair.sign(new ByteArrayInputStream(messageRandom), noiseOutputStream);
        noiseOutputStream.flush();
    }

    /**
     * Obtain a key pair with algorithm
     *
     * @param <PUBLIC_KEY> Public key type
     * @param algorithm    Algorithm to use
     * @return The key pair
     */
    @SuppressWarnings("unchecked")
    public static <PUBLIC_KEY extends JHelpPublicKey> JHelpKeyPairs<PUBLIC_KEY> obtainKeyPair(final String algorithm)
    {
        try
        {
            return (JHelpKeyPairs<PUBLIC_KEY>) FactoryKeyPairs.ALGORITHMS.get(algorithm.toLowerCase()).newInstance();
        }
        catch (final Exception e)
        {
            throw new NoSuchAlgorithmException(algorithm, e);
        }
    }

    /**
     * Validate a certificate
     *
     * @param publicKey   Public key
     * @param inputStream Stream certificate
     * @return {@code true} if certificate is valid
     * @throws IOException On read issue
     */
    public static boolean validateCertificate(final JHelpPublicKey publicKey, final InputStream inputStream)
            throws IOException
    {
        final NoiseInputStream noiseInputStream = new NoiseInputStream(inputStream);

        final byte[] messageRandom = new byte[111];
        int          length        = 111;
        int          index         = 0;

        int read = noiseInputStream.read(messageRandom, index, length);
        index += read;
        length -= read;

        while ((read >= 0) && (length > 0))
        {
            read = noiseInputStream.read(messageRandom, index, length);
            index += read;
            length -= read;
        }

        return publicKey.validSignature(new ByteArrayInputStream(messageRandom), noiseInputStream);
    }
}