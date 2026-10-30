package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {

    private static final Preferences prefs =
            Gdx.app.getPreferences("pixel-racing-settings");

    public static float getVolume() {
        return prefs.getFloat("volume", 1.0f);
    }

    public static void setVolume(float volume) {
        prefs.putFloat("volume", volume);
        prefs.flush();
    }

    public static boolean isMusicEnabled() {
        return prefs.getBoolean("musicEnabled", true);
    }

    public static void setMusicEnabled(boolean enabled) {
        prefs.putBoolean("musicEnabled", enabled);
        prefs.flush();
    }
}