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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Generate a private and public key pair<br>
 * Its strongly recommends to generate the private key in private field and never let an other object to access it or give it to
 * on an other object.<br>
 * Its also strongly recommends to generate a different private/public pair for each instance.<br>
 * To help the developer that use a implementation of this interface to persist the pair, there include storage secure method.
 * See RSA implementation to have an example of how do it
 *
 * @param <PUBLIC_KEY> Public key type
 * @author JHelp
 */
public interface JHelpKeyPairs<PUBLIC_KEY extends JHelpPublicKey>
{
    /**
     * Decrypt a stream encrypted by the associated public key
     *
     * @param inputStream  Stream to decrypt
     * @param outputStream Stream where write decrypt message
     * @throws IOException On reading/writing problem
     */
    void decrypt(InputStream inputStream, OutputStream outputStream) throws IOException;

    /**
     * Generated public key in this pair. You can distribute public key for encrypt, for decryt, use this key pair instance
     *
     * @return Associated public key
     */
    PUBLIC_KEY getPublicKey();

    /**
     * Load a pair
     *
     * @param storeName Store name
     * @param key       Secret key
     * @throws IOException On reading/writing problem
     */
    void loadPair(String storeName, byte[] key) throws IOException;

    /**
     * Create a signature
     *
     * @param message   Message
     * @param signature Generated signature
     * @throws IOException On read/write issue
     */
    void sign(InputStream message, OutputStream signature) throws IOException;

    /**
     * Store a pair
     *
     * @param storeName Store name
     * @param key       Secret key
     * @throws IOException On reading/writing problem
     */
    void storePair(String storeName, byte[] key) throws IOException;
}