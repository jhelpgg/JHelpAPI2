package jhelp.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jhelp on 24/07/17.
 */
public interface OutputStreamProducer<O extends OutputStream>
{
    O produce() throws IOException;
}
