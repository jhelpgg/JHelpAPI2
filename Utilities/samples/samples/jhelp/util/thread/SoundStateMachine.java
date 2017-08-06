package samples.jhelp.util.thread;

import jhelp.util.debug.Debug;
import jhelp.util.math.JHelpRandom;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.StateMachine;

public class SoundStateMachine extends StateMachine<SoundState>
{
    private String url;

    public SoundStateMachine()
    {
        super(SoundState.NOT_INITIALIZED);
        this.register((ConsumerTask<SoundState>) this::loading, SoundState.LOADING);
        this.register((ConsumerTask<SoundState>) this::releasing, SoundState.RELEASING);
    }

    private void loading(SoundState soundState)
    {
        Debug.verbose("Loading: ", this.url);

        if (JHelpRandom.random())
        {
            this.loadingSucceed();
        }
        else
        {
            this.loadingFailed();
        }
    }

    private void loadingFailed()
    {
        this.changeState(SoundState.NOT_INITIALIZED);
    }

    private void loadingSucceed()
    {
        this.changeState(SoundState.READY);
    }

    private void releasing(SoundState soundState)
    {
        this.url = null;
        this.releasingDone();
    }

    private void releasingDone()
    {
        this.changeState(SoundState.NOT_INITIALIZED);
    }

    /**
     * Indicates if it is allow to go from a state to an other state.<br>
     * By default all transition are allowed
     *
     * @param oldState State it leave
     * @param newState State want go
     * @return {@code true} if it is allow to go from a state to an other state
     */
    @Override
    public boolean allowedTransition(final SoundState oldState, final SoundState newState)
    {
        switch (oldState)
        {
            case NOT_INITIALIZED:
                return newState == SoundState.LOADING;
            case LOADING:
                return newState == SoundState.NOT_INITIALIZED || newState == SoundState.READY;
            case READY:
                return newState == SoundState.PLAYING || newState == SoundState.RELEASING;
            case PLAYING:
                return newState == SoundState.READY || newState == SoundState.RELEASING;
            case RELEASING:
                return newState == SoundState.NOT_INITIALIZED;
            default:
                throw new RuntimeException("Should never goes here! Not managed state=" + oldState);
        }
    }

    public boolean playResume()
    {
        return this.changeState(SoundState.PLAYING);
    }

    public void registerSoundNotInitializedListener(ConsumerTask<SoundState> listener)
    {
        this.register(listener, SoundState.NOT_INITIALIZED);
    }

    public void registerSoundPlayingListener(ConsumerTask<SoundState> listener)
    {
        this.register(listener, SoundState.PLAYING);
    }

    public void registerSoundReadyListener(ConsumerTask<SoundState> listener)
    {
        this.register(listener, SoundState.READY);
    }

    public boolean release()
    {
        return this.changeState(SoundState.RELEASING);
    }

    public boolean setURL(String url)
    {
        this.url = url;
        return this.changeState(SoundState.LOADING);
    }

    public boolean stopPauseFinished()
    {
        return this.changeState(SoundState.READY);
    }

    public void unregisterSoundNotInitializedListener(ConsumerTask<SoundState> listener)
    {
        this.register(listener, SoundState.NOT_INITIALIZED);
    }

    public void unregisterSoundPlayingListener(ConsumerTask<SoundState> listener)
    {
        this.unregister(listener, SoundState.PLAYING);
    }

    public void unregisterSoundReadyListener(ConsumerTask<SoundState> listener)
    {
        this.unregister(listener, SoundState.READY);
    }
}
