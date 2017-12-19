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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.Hashtable;
import javax.crypto.Cipher;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;
import jhelp.util.xml.DynamicWriteXML;
import jhelp.xml.ExceptionXML;
import jhelp.xml.InvalidParameterValueException;
import jhelp.xml.MarkupXML;
import jhelp.xml.MissingRequiredParameterException;
import jhelp.xml.ParseXMLlistener;

/**
 * RSA public key
 */
class RSAPublicKey
        implements JHelpPublicKey
{
    /**
     * Parameter exponent on XML representation
     */
    public static final String EXPONENT              = "Exponent";
    /**
     * Markup name on XML representation
     */
    public static final String MARKUP_RSA_PUBLIC_KEY = RSAPublicKey.class.getName();
    /**
     * Parameter modulus on XML representation
     */
    public static final String MODULUS               = "Modulus";
    /**
     * Public key
     */
    PublicKey publicKey;

    /**
     * Constructs RSAPublicKey
     *
     * @param publicKey Public key
     */
    public RSAPublicKey(final PublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    /**
     * Deserialize a stream that contains RSA public key
     *
     * @param inputStream Stream to parse
     * @throws IOException On reading issue
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#deserialize(InputStream)
     */
    @Override
    public void deserialize(final InputStream inputStream) throws IOException
    {
        try
        {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final BigInteger modulus  = UtilIO.readBigInteger(inputStream);
            final BigInteger exponent = UtilIO.readBigInteger(inputStream);

            final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            this.publicKey = keyFactory.generatePublic(publicKeySpec);
        }
        catch (final Exception exception)
        {
            throw new IOException("Deserialize problem", exception);
        }
    }

    /**
     * Encrypt a stream
     *
     * @param inputStream  Stream to encrypt
     * @param outputStream Encrypted stream
     * @throws IOException On read/write issue
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#encrypt(InputStream, OutputStream)
     */
    @Override
    public void encrypt(final InputStream inputStream, OutputStream outputStream) throws IOException
    {
        try
        {
            outputStream = new NoiseOutputStream(outputStream);

            final Cipher cipher = Cipher.getInstance(RSAKeyPair.RSA_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            final byte[] temp = new byte[245];
            byte[]       crypted;

            int read = UtilIO.readStream(inputStream, temp);
            while (read >= 0)
            {
                crypted = cipher.doFinal(cipher.update(temp, 0, read));

                outputStream.write(crypted.length % 256);
                outputStream.write(crypted);

                read = UtilIO.readStream(inputStream, temp);
            }

            outputStream.flush();
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't encrypt !", exception);
        }
    }

    /**
     * Parse RSA public key from XML
     *
     * @param markupXML XML to parse
     * @throws ExceptionXML Parsing issue
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#fromXML(jhelp.xml.MarkupXML)
     */
    @Override
    public void fromXML(final MarkupXML markupXML) throws ExceptionXML
    {
        if (!markupXML.getName()
                      .equals(RSAPublicKey.MARKUP_RSA_PUBLIC_KEY))
        {
            throw new ExceptionXML(
                    UtilText.concatenate("The markup ", markupXML.getName(), " is not for RSAPublicKey"));
        }

        BigInteger modulus  = null;
        BigInteger exponent = null;

        String temp = markupXML.obtainParameter(RSAPublicKey.MODULUS);
        if (temp == null)
        {
            throw new MissingRequiredParameterException(RSAPublicKey.MODULUS, markupXML.getName());
        }
        try
        {
            modulus = new BigInteger(temp);
        }
        catch (final Exception exception)
        {
            throw new InvalidParameterValueException(RSAPublicKey.MODULUS, markupXML.getName(), exception);
        }

        temp = markupXML.obtainParameter(RSAPublicKey.EXPONENT);
        if (temp == null)
        {
            throw new MissingRequiredParameterException(RSAPublicKey.EXPONENT, markupXML.getName());
        }
        try
        {
            exponent = new BigInteger(temp);
        }
        catch (final Exception exception)
        {
            throw new InvalidParameterValueException(RSAPublicKey.EXPONENT, markupXML.getName(), exception);
        }

        try
        {
            final KeyFactory       keyFactory    = KeyFactory.getInstance("RSA");
            final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            this.publicKey = keyFactory.generatePublic(publicKeySpec);
        }
        catch (final Exception exception)
        {
            throw new ExceptionXML("Can't parse RSA public key", exception);
        }
    }

    /**
     * Serialize public RSA key in stream
     *
     * @param outputStream Stream where write
     * @throws IOException On writing issue
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#serialize(OutputStream)
     */
    @Override
    public void serialize(final OutputStream outputStream) throws IOException
    {
        try
        {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(this.publicKey, RSAPublicKeySpec.class);

            UtilIO.writeBigInteger(publicKeySpec.getModulus(), outputStream);
            UtilIO.writeBigInteger(publicKeySpec.getPublicExponent(), outputStream);
        }
        catch (final Exception exception)
        {
            throw new IOException("Serialize problem", exception);
        }
    }

    /**
     * Parse a markup (Can be use inside {@link ParseXMLlistener#startMakup(String, Hashtable)}
     *
     * @param markupName Markup name
     * @param parameters Parameters
     * @throws MissingRequiredParameterException If a parameter miss
     * @throws InvalidParameterValueException    If a parameter have wrong value
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#startMarkup(String, Hashtable)
     */
    @Override
    public void startMarkup(final String markupName, final Hashtable<String, String> parameters)
            throws MissingRequiredParameterException,
                   InvalidParameterValueException
    {
        if (!markupName.equals(RSAPublicKey.MARKUP_RSA_PUBLIC_KEY))
        {
            throw new RuntimeException(UtilText.concatenate("The markup ", markupName, " is not for RSAPublicKey"));
        }

        BigInteger modulus  = null;
        BigInteger exponent = null;

        String temp = parameters.get(RSAPublicKey.MODULUS);
        if (temp == null)
        {
            throw new MissingRequiredParameterException(RSAPublicKey.MODULUS, markupName);
        }
        try
        {
            modulus = new BigInteger(temp);
        }
        catch (final Exception exception)
        {
            throw new InvalidParameterValueException(RSAPublicKey.MODULUS, markupName, exception);
        }

        temp = parameters.get(RSAPublicKey.EXPONENT);
        if (temp == null)
        {
            throw new MissingRequiredParameterException(RSAPublicKey.EXPONENT, markupName);
        }
        try
        {
            exponent = new BigInteger(temp);
        }
        catch (final Exception exception)
        {
            throw new InvalidParameterValueException(RSAPublicKey.EXPONENT, markupName, exception);
        }

        try
        {
            final KeyFactory       keyFactory    = KeyFactory.getInstance("RSA");
            final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            this.publicKey = keyFactory.generatePublic(publicKeySpec);
        }
        catch (final Exception exception)
        {
            throw new RuntimeException("Can't parse RSA public key", exception);
        }
    }

    /**
     * Convert RSA public key in XML markup
     *
     * @return XML markup
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#toXML()
     */
    @Override
    public MarkupXML toXML()
    {
        try
        {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(this.publicKey, RSAPublicKeySpec.class);

            final MarkupXML markupXML = new MarkupXML(RSAPublicKey.MARKUP_RSA_PUBLIC_KEY);

            markupXML.addParameter(RSAPublicKey.MODULUS, publicKeySpec.getModulus().toString());
            markupXML.addParameter(RSAPublicKey.EXPONENT, publicKeySpec.getPublicExponent().toString());

            return markupXML;
        }
        catch (final Exception exception)
        {
            throw new RuntimeException("Can't convert to XML", exception);
        }
    }

    /**
     * Valid a signature
     *
     * @param message   Message stream
     * @param signature Signature stream
     * @return {@code true} if signature is valid
     * @throws IOException On reading issue
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#validSignature(InputStream, InputStream)
     */
    @Override
    public boolean validSignature(final InputStream message, InputStream signature) throws IOException
    {
        try
        {
            signature = new NoiseInputStream(signature);

            final Signature sign = Signature.getInstance(RSAKeyPair.RSA_SIGNATURE);
            sign.initVerify(this.publicKey);
            byte[] temp = new byte[4096];

            int read = message.read(temp);
            while (read >= 0)
            {
                sign.update(temp, 0, read);

                read = message.read(temp);
            }

            //
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            read = signature.read(temp);
            while (read >= 0)
            {
                byteArrayOutputStream.write(temp, 0, read);

                read = signature.read(temp);
            }

            byteArrayOutputStream.flush();
            temp = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            return sign.verify(temp);
        }
        catch (final Exception exception)
        {
            throw new IOException("Can't validate !", exception);
        }
    }

    /**
     * Write inside XML
     *
     * @param dynamicWriteXML {@link DynamicWriteXML} where insert
     * @see jhelp.security.pulicPrivateKey.JHelpPublicKey#writeInXML(DynamicWriteXML)
     */
    @Override
    public void writeInXML(final DynamicWriteXML dynamicWriteXML)
    {
        try
        {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(this.publicKey, RSAPublicKeySpec.class);

            dynamicWriteXML.openMarkup(RSAPublicKey.MARKUP_RSA_PUBLIC_KEY);

            dynamicWriteXML.appendParameter(RSAPublicKey.MODULUS, publicKeySpec.getModulus().toString());
            dynamicWriteXML.appendParameter(RSAPublicKey.EXPONENT, publicKeySpec.getPublicExponent().toString());

            dynamicWriteXML.closeMarkup();
        }
        catch (final Exception exception)
        {
            throw new RuntimeException("Can't convert to XML", exception);
        }
    }
}