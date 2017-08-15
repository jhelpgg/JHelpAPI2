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
import jhelp.sound.synthetizer.SynthesiserListener;
import jhelp.util.debug.Debug;

public class MainSample implements SynthesiserListener
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

            synthesiser.addSynthesiserListener(new MainSample());
            Morsel morsel = new Morsel();

            int instrument = synthesiser.obtainInstrument("FX 8");
            Debug.information("instrument 1=", synthesiser.obtainNameOfInstrument(instrument));
            Partition partition = new Partition();
            partition.instrument(instrument);
            partition.addMajorMinor(Synthesiser.BLACK, 75, 30, Synthesiser.dotTime(Synthesiser.WHITE),
                                    true, 8, true, Overtone.obtainOvertone(5, OvertoneName.I));
            morsel.addPartition(partition);

            instrument = synthesiser.obtainInstrument("Xylophone");
            Debug.information("instrument 2=", synthesiser.obtainNameOfInstrument(instrument));
            partition = new Partition();
            partition.instrument(instrument);
            final double base   = Synthesiser.WHITE;
            final double half   = base * 0.5;
            final double twice  = base * 2.0;
            final double fourth = base * 4.0;
            final int    gamut  = 4;
            final int    gamut1 = gamut - 1;

            for (int i = 0; i < 5; i++)
            {
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.C));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.E));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.C));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.E));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut1, OvertoneName.I));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut1, OvertoneName.G));
                partition.addPause(half);
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.C));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.E));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.C));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(base, 100, Overtone.obtainOvertone(gamut, OvertoneName.E));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut, OvertoneName.D));
                partition.addOvertone(twice, 100, Overtone.obtainOvertone(gamut1, OvertoneName.I));
                partition.addOvertone(fourth, 100, Overtone.obtainOvertone(gamut, OvertoneName.F));
                partition.addPause(fourth);
            }

            morsel.addPartition(partition);
            synthesiser.play(morsel);
        }
        catch (SynthesiserException e)
        {
            Debug.exception(e);
        }
    }

    /**
     * Call when synthetyzer change pause status
     *
     * @param pause New pause status
     */
    @Override
    public void synthesiserChangePause(final boolean pause)
    {
        Debug.information("Pause=", pause);
    }

    /**
     * Call when synthetyzer start play morsel
     */
    @Override
    public void synthesiserStartPlay()
    {
        Debug.mark("START");
    }

    /**
     * Call when synthetyzer stop play morsel
     */
    @Override
    public void synthesiserStopPlay()
    {
        Debug.mark("STOP");
    }
}
