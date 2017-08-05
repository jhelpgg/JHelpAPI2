package jhelp.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jhelp on 24/07/17.
 */
public interface InputOutputStreamOperation<I extends InputStream, O extends OutputStream>
{
    void operate(I inputStream, O outputStream) throws IOException;
}
