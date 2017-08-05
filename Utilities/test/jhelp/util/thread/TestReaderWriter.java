package jhelp.util.thread;

import jhelp.util.debug.Debug;
import jhelp.util.math.JHelpRandom;
import jhelp.util.util.Utilities;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jhelp on 14/07/17.
 */
public class TestReaderWriter
{
    private static final int           READER_TIME = 123;
    private static final int           WRITER_TIME = 234;
    private static final StringBuilder information = new StringBuilder();

    static class Reader implements ConsumerTask<String>
    {
        private final String id;
        private final String expected;

        public Reader(final String id, String expected)
        {
            this.id = id;
            this.expected = expected;
        }

        /**
         * Play the task
         *
         * @param parameter Task parameter
         */
        @Override
        public void consume(final String parameter)
        {
            Debug.information("BEFORE : Reader ", this.id, " : ", parameter);
            Utilities.sleep(JHelpRandom.random(TestReaderWriter.READER_TIME >> 4, TestReaderWriter.READER_TIME));

            if (!this.expected.equalsIgnoreCase(parameter))
            {
                TestReaderWriter.information.append(this.id);
                TestReaderWriter.information.append("|");
                TestReaderWriter.information.append(this.expected);
                TestReaderWriter.information.append(" <> ");
                TestReaderWriter.information.append(parameter);
                TestReaderWriter.information.append(" : ");
            }

            Debug.information("AFTER : Reader ", this.id, " : ", parameter);
        }
    }

    static class Writer implements ProducerTask<String>
    {
        private final String id;

        public Writer(final String id)
        {
            this.id = id;
        }

        /***
         * Play the task
         * @return Task result
         */
        @Override
        public String produce()
        {
            Debug.information("BEFORE : Writer ", this.id);
            Utilities.sleep(JHelpRandom.random(TestReaderWriter.WRITER_TIME >> 4, TestReaderWriter.WRITER_TIME));
            Debug.information("AFTER : Writer ", this.id);
            return this.id;
        }
    }

    @Test
    public void testReaderWriter()
    {
        TestReaderWriter.information.setLength(0);
        String               expected     = "Initial";
        ReaderWriter<String> readerWriter = new ReaderWriter<>(expected);

        for (int time = 0; time < 5; time++)
        {
            for (int i = 0; i < 5; i++)
            {
                readerWriter.read(new Reader(time + "_" + i, expected));
            }

            expected = String.valueOf(time);
            readerWriter.write(new Writer(expected));
        }

        readerWriter.read(new Reader("Final", expected)).waitFinish();

        if (TestReaderWriter.information.length() > 0)
        {
            Assert.fail(TestReaderWriter.information.toString());
        }

        //Wait to be sure readerWriter main thread is finished to be able test if it restart automatically
        Utilities.sleep(1234);
        readerWriter.read(new Reader("Final_2", expected)).waitFinish();

        if (TestReaderWriter.information.length() > 0)
        {
            Assert.fail(TestReaderWriter.information.toString());
        }
    }
}
