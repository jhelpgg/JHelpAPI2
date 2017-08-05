package jhelp.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jhelp on 24/07/17.
 */
public interface InputStreamProducer<I extends InputStream>
{
    I produce() throws IOException;
}
