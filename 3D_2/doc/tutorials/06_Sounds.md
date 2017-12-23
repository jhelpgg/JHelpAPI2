# Sounds

Adding some sound can make things more fun. Here we will benefits of OpenAL
3D sound effect.

3D sound effect means:
* If sound source are at user right, the sound is played is its right speaker.
* If sound source are at user left, the sound is played is its left speaker.
* More far the sound source is, less sound volume is
* More near the sound source is, more sound volume is

> Note: The 3D effects are only applied on MONO sounds.
        STEREO sounds are just played as they are.
        Other type of sounds aren't managed

In the engine we accept some wav (All mono/stereo 8/16 managed "natively" by Java)

We accept also MP3 (mono/stereo)

Complete code of this tutorial: [Sounds](../../samples/jhelp/engine2/tutorials/Sounds.java)

We start from keyboard/joystick tutorial.

For manage sound, we need a sound manager:

````java
        //Obtain the sound manager
        SoundManager soundManager = Sounds.window3D.soundManager();
````

We want link a sound to the box, sound the sound move with the box and give
sensation that is the box that emits the sound.

First we load a MONO sound (To benefits 3D effects)

````java
        //Load sound will be linked to the box
        Sound soundBox = new SoundMP3(Sounds.class.getResourceAsStream("Kuma.mp3"));
````

Then we create a sound source

````java
        //Create a sound source to link to the box
        SoundSource soundSourceBox = soundManager.createSource();
````

And we link the source sound to the box, so the sound will follow the box

````java
        // Link source to the box
        soundSourceBox.link(box);
````

Now we play the sound enough time to manipulate the box and ear the
3D effect when box move

````java
        //Play the sound box ten times
        for (int i = 0; i < 10; i++)
        {
            soundSourceBox.enqueueSound(soundBox);
        }
````

Launch the code, and ear the 3D effect on moving the box.
The sound seems comes from the box.

It is possible to add several sound source. Just don't make too much,
think about user ears.

It is better for memory and performance reasons, to reuse a sound source
than destroy one and create a new one just after.

The sound manager have a special sound type, called background.
The background sound can be see like in game an ambient music.
It is possible to change the sound level of background sound, but since
we use OpenAL 3D effect to simulate the level, STEREO sounds will not care
and played with the volume they were recorded.

Add a background sound:

````java
        //Load sound to play in background
        Sound soundBackground = SoundWav.create(Sounds.class.getResourceAsStream("succeed.wav"));

        //Play the sound in background ten times
        for (int i = 0; i < 10; i++)
        {
            soundManager.enqueueBackground(soundBackground);
        }

        // Down background sound level to ear more easy the other sound
        soundManager.backgroundLevel(0.5f);
````

Like previously we load a sound. This time we decide to load a WAV to
show how load WAV sounds. We use a MONO sound to be able change the background
sound level.

Then we enqueued the sound to the background.

And finally we changed the level to still ear the 3D effect on the box.
