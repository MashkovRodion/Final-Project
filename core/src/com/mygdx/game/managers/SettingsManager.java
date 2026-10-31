
package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {

    private static final Preferences prefs =
            Gdx.app.getPreferences("pixel-racing-settings");

    public static float getMusicVolume() {
        return prefs.getFloat("musicVolume", 1.0f);
    }

    public static void setMusicVolume(float volume) {
        prefs.putFloat("musicVolume", volume);
        prefs.flush();
    }

    public static float getSoundVolume() {
        return prefs.getFloat("soundVolume", 1.0f);
    }

    public static void setSoundVolume(float volume) {
        prefs.putFloat("soundVolume", volume);
        prefs.flush();
    }
}
