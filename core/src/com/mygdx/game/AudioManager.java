package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class AudioManager {
    public boolean isMusicOn;
    public Music gasMusic;
    public Music brakeMusic;

    public AudioManager() {
        gasMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.GAS_MUSIC_PATH));
        brakeMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BRAKE_MUSIC_PATH));

        isMusicOn = false;


        gasMusic.setVolume(Options.soundVolume);
        brakeMusic.setVolume(Options.soundVolume);
    }
}

