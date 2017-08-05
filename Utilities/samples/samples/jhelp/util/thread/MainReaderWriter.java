package samples.jhelp.util.thread;

import jhelp.util.debug.Debug;
import jhelp.util.math.JHelpRandom;
import jhelp.util.text.UtilText;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ProducerTask;
import jhelp.util.thread.ReaderWriter;
import jhelp.util.util.Utilities;

public class MainReaderWriter
{
    static class Data
    {
        private int    integer;
        private String string;

        Data(int integer, String string)
        {
            this.integer = integer;
            this.string = string;
        }

        @Override
        public String toString()
        {
            return this.integer + " : " + this.string;
        }
    }

    static void consume(Data data)
    {
        Utilities.sleep(JHelpRandom.random(1, 100));
        Debug.verbose("data = ", data);
    }

    public static void main(String[] arguments)
    {
        ReaderWriter<Data> readerWriter = new ReaderWriter<>(null);
        ConsumerTask<Data> consumer     = MainReaderWriter::consume;
        ProducerTask<Data> producer     = MainReaderWriter::produceData;

        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                readerWriter.read(consumer);
            }

            readerWriter.write(producer);
        }
    }

    static Data produceData()
    {
        Utilities.sleep(JHelpRandom.random(1, 1000));
        return new Data(JHelpRandom.random(-1000, 1000),
                        UtilText.repeat((char) JHelpRandom.random((int) 'A', (int) 'Z'),
                                        JHelpRandom.random(5, 10)));
    }
}
