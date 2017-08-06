package samples.jhelp.util.thread;

import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.debug.Debug;
import jhelp.util.math.JHelpRandom;
import jhelp.util.thread.ConsumerTask;

public class MainSoundStateMachine implements ConsumerTask<SoundState>
{
    private static final AtomicInteger NEXT = new AtomicInteger(0);
    private static SoundStateMachine soundStateMachine;

    public static void main(String[] arguments)
    {
        MainSoundStateMachine mainSoundStateMachine = new MainSoundStateMachine();
        MainSoundStateMachine.soundStateMachine = new SoundStateMachine();
        MainSoundStateMachine.soundStateMachine.registerSoundPlayingListener(mainSoundStateMachine);
        MainSoundStateMachine.soundStateMachine.registerSoundReadyListener(mainSoundStateMachine);
        MainSoundStateMachine.soundStateMachine.setURL("http://..._");
        MainSoundStateMachine.soundStateMachine.registerSoundNotInitializedListener(mainSoundStateMachine);
    }

    /**
     * Consume the value
     *
     * @param parameter Task parameter
     */
    @Override
    public void consume(final SoundState parameter)
    {
        Debug.verbose("Sound state=", parameter);

        switch (parameter)
        {
            case READY:
                if (JHelpRandom.random())
                {
                    MainSoundStateMachine.soundStateMachine.playResume();
                }
                else
                {
                    MainSoundStateMachine.soundStateMachine.release();
                }
                break;
            case LOADING:
                Debug.warning("How does it goes here ?");
                break;
            case PLAYING:
                if (JHelpRandom.random())
                {
                    MainSoundStateMachine.soundStateMachine.stopPauseFinished();
                }
                else
                {
                    MainSoundStateMachine.soundStateMachine.release();
                }
                break;
            case NOT_INITIALIZED:
                if (JHelpRandom.random())
                {
                    MainSoundStateMachine.soundStateMachine.setURL(
                            "http://..._" + MainSoundStateMachine.NEXT.incrementAndGet());
                }
                break;
            case RELEASING:
                Debug.warning("How does it goes here ?");
                break;
        }
    }
}
