package io.github.teamfractal.util;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Christian Beddows on 16/02/2017.
 */
public class GameAudio {
    private Sound click;

    /**
     * Initialise all audio sound streams
     */
    public GameAudio() {
        click = Gdx.audio.newSound(Gdx.files.internal("audio/click.ogg"));
    }

    /**
     * Create an audio stream
     * @param filename
     * @return Sound
     */
    public Sound newSound(FileHandle filename) {
        return Gdx.audio.newSound(filename);
    }

    /**
     * Find the specific ID of a sound stream, needed for playback manipulation
     * @param sound
     * @return long
     */
    public long getSoundID(Sound sound) {
        return sound.play(0);
    }

    /**
     * Plays a click sound
     */
    public void click() {
        click.play();
    }

    /**
     * plays a specific sound stream
     * @param sound
     */
    public void play(Sound sound) {sound.play();}

    /**
     * stops playback of all instances of a sound stream
     * @param sound
     */
    public void stop(Sound sound) {sound.stop();}

    /**
     * Sets the pitch of a sound stream, requires the ID of the stream and the desired pitch
     * @param sound
     * @param soundID
     * @param pitch
     */
    public void setPitch (Sound sound, long soundID, float pitch) {sound.setPitch(soundID, pitch);}

    /**
     * Dispose of an audio stream when it is no longer needed
     * @param sound
     */
    public void dispose(Sound sound) {sound.dispose();}

    /**
     * Make an audio stream loop
     * @param sound
     */
    public void loop(Sound sound){ sound.loop();}


}
