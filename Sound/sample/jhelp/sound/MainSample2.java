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

package jhelp.sound;

import jhelp.sound.synthetizer.Morsel;
import jhelp.sound.synthetizer.Overtone;
import jhelp.sound.synthetizer.OvertoneName;
import jhelp.sound.synthetizer.Partition;
import jhelp.sound.synthetizer.Synthesiser;
import jhelp.sound.synthetizer.SynthesiserException;
import jhelp.util.debug.Debug;

public class MainSample2
{
    public static void main(String[] arguments)
    {
        try
        {
            Synthesiser synthesiser = Synthesiser.obtainSynthetiser();

            for (String name : synthesiser.obtainAllInstrumentsName())
            {
                Debug.information(name);
            }

            Morsel morsel     = new Morsel();
            int    instrument = synthesiser.obtainInstrument("piano");
            Debug.information("instrument=", synthesiser.obtainNameOfInstrument(instrument));
            Partition partition = new Partition();
            partition.instrument(instrument);

            final double time  = Synthesiser.WHITE;
            final int    gamut = 6;

            for (int i = 0; i < 5; i++)
            {
                for (int j = 0; j < 5; j++)
                {
                    partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.D_SHARP));
                    partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.F_SHARP));
                }

                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.G_SHARP));
                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.H_SHARP));
                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.G_SHARP));
                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.F_SHARP));
                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.D_SHARP));
                partition.addOvertone(time, 100, Overtone.obtainOvertone(gamut, OvertoneName.C_SHARP));
            }

            morsel.addPartition(partition);
            synthesiser.play(morsel);
        }
        catch (SynthesiserException e)
        {
            Debug.exception(e);
        }
    }
}
