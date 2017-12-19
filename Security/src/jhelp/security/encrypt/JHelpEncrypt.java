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

package jhelp.security.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Encrypt/decrypt something with a secret key
 */
public interface JHelpEncrypt
{
    /**
     * Decrypt a stream
     *
     * @param key           Key for decrypt
     * @param encryptStream Stream to decrypt
     * @param clearStream   Decrypted stream
     * @throws IOException On reading/writing problem
     */
    void decrypt(byte[] key, InputStream encryptStream, OutputStream clearStream) throws IOException;

    /**
     * Encrypt a stream
     *
     * @param key           Key for encrypt
     * @param clearStream   Stream to encrypt
     * @param encryptStream Encrypted stream
     * @throws IOException On reading/writing problem
     */
    void encrypt(byte[] key, InputStream clearStream, OutputStream encryptStream) throws IOException;
}