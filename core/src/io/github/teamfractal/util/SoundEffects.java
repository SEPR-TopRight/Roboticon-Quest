package io.github.teamfractal.util;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Christian Beddows on 16/02/2017.
 */
public class SoundEffects {

    public static void click() { Gdx.audio.newSound(Gdx.files.internal("audio/click.ogg")).play();}

    public static void chime() { Gdx.audio.newSound(Gdx.files.internal("audio/chime.ogg")).play();}

    public static void correct() { Gdx.audio.newSound(Gdx.files.internal("audio/correct.ogg")).play();}

    public static void notificationbad() { Gdx.audio.newSound(Gdx.files.internal("audio/notificationbad.ogg")).play();}

    public static void wrong() { Gdx.audio.newSound(Gdx.files.internal("audio/wrong.ogg")).play();}

    public static void error() {Gdx.audio.newSound(Gdx.files.internal("audio/error.ogg")).play();}

    public static void pulse() {Gdx.audio.newSound(Gdx.files.internal("audio/pulse.ogg")).play();}

    public static void anxiety() { Gdx.audio.newSound(Gdx.files.internal("audio/anxiety.ogg")).play();}
}