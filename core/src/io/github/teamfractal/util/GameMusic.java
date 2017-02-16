package io.github.teamfractal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Christian Beddows on 15/02/2017.
 */
public class GameMusic {
    private Music music;

    /**
     * Initialise the music stream
     */
    public GameMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/squaredance.mp3"));
    }

    /**
     * method to play the music stream
     */
    public void play() {
        music.play();
    }

    /**
     * method to pause the music stream
     */
    public void pause() {
        music.pause();
    }

    /**
     * changes the song in the music stream
     * @param filename
     */
    public void changeMusic(FileHandle filename) {
        music = Gdx.audio.newMusic(filename);
    }

    /**
     * method to stop the music stream
     */
    public void stop(){
        music.stop();
    }

    /**
     * returns the current playback time for the music stream
     * @return float
     */
    public float getPosition(){
        return music.getPosition();
    }

    /**
     * returns the current playback volume for the music stream
     * @return float
     */
    public float getVolume(){
        return music.getVolume();
    }

    /**
     * method for setting the playback time for the music stream
     * @param time
     */
    public void setPosition(float time){
        music.setPosition(time);
    }

    /**
     * method for setting the playback volume for the music stream
     * @param volume
     */
    public void setVolume(float volume){
        music.setVolume(volume);
    }

    /**
     * returns a boolean representing whether the music stream is playing
     * @return boolean
     */
    public boolean isPlaying(){
        return music.isPlaying();
    }

    /**
     * returns a boolean representing whether the music stream is looping
     * @return boolean
     */
    public boolean isLooping(){
        return music.isLooping();
    }

    /**
     * method to set the music stream to loop
     * @param isLooping
     */
    public void setLooping(boolean isLooping){
        music.setLooping(isLooping);
    }

    /**
     * method to dispose of the music stream when it is not needed anymore
     */
    public void dispose(){
        music.dispose();
    }
}
