package com.mygdx.game.utils;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {

    public static GameState state;

    public long startTime;
    public long pauseStartTime;

    public GameSession() {
        state = GameState.ENDED;
    }

    public void startGame() {
        state = GameState.PLAYING;
        startTime = TimeUtils.millis();
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }


    public void resumeGame() {
        state = GameState.PLAYING;
        startTime += (TimeUtils.millis() - pauseStartTime);
    }
}

