package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.options.Options;
import com.mygdx.game.core.GameResources;

public class AudioManager {
    public boolean isMusicOn;
    public Music gasMusic;
    public Music brakeMusic;
    public Music backgroundMusic;
    public Sound crashSound;

    public AudioManager() {
        gasMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.GAS_MUSIC_PATH));
        brakeMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BRAKE_MUSIC_PATH));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.FON_MUSIC_PATH));
        crashSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.CRASH_MUSIC_PATH));

        isMusicOn = true;
        backgroundMusic.setVolume(Options.musicVolume);
        backgroundMusic.setLooping(true);


        gasMusic.setVolume(Options.soundVolume);
        brakeMusic.setVolume(Options.soundVolume);
    }
}

