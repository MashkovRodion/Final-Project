package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LeaderboardManager {

    private static final Preferences prefs =
            Gdx.app.getPreferences("leaderboard");

    public static void addTime(long timeMillis) {

        long[] times = getTimes();

        long[] newTimes = new long[11];

        System.arraycopy(times, 0, newTimes, 0, 10);

        newTimes[10] = timeMillis;

        for (int i = 0; i < newTimes.length - 1; i++) {
            for (int j = i + 1; j < newTimes.length; j++) {

                if (newTimes[i] == 0 ||
                        (newTimes[j] > 0 &&
                                newTimes[j] < newTimes[i])) {

                    long tmp = newTimes[i];
                    newTimes[i] = newTimes[j];
                    newTimes[j] = tmp;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            prefs.putLong("time" + i, newTimes[i]);
        }

        prefs.flush();
    }

    public static long[] getTimes() {

        long[] times = new long[10];

        for (int i = 0; i < 10; i++) {
            times[i] = prefs.getLong("time" + i, 0);
        }

        return times;
    }

    public static void clear() {

        prefs.clear();
        prefs.flush();
    }
}