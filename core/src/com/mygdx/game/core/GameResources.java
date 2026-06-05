package com.mygdx.game.core;

import com.badlogic.gdx.graphics.Texture;

public class GameResources {

    public static Texture gasNormal;
    public static Texture gasPressed;

    public static Texture brakeNormal;
    public static Texture brakePressed;

    public static Texture ramSpeed;

    public static Texture star;
    public static Texture star_tusk;


    public static Texture menu_bg;

    public static Texture startButton;
    public static Texture optionsButton;
    public static Texture quitButton;

    public static Texture backButton;

    public static Texture plusButton;
    public static Texture minusButton;

    public static Texture fullscreenOn;
    public static Texture fullscreenOff;

    public static Texture musicPanel;
    public static Texture wheelTexture;

    public static Texture pauseButton;
    public static Texture continueButton;
    public static Texture restartButton;
    public static Texture menuPauseButton;
    public static Texture trackTexture;

    public static Texture barrier1;
    public static Texture barrier2;
    public static Texture barrier3;
    public static Texture barrier4;
    public static Texture barrier5;
    public static Texture sfxPanel;

    public static Texture skinsButton;
    public static Texture checkmark;
    public static Texture clearRecords;
    public static Texture finishLine;
    public static Texture leaderboardButton;


    public static void loadTextures() {

        gasNormal = new Texture("gas_and_brake/gas_normal.png");
        gasPressed = new Texture("gas_and_brake/gas_pressed.png");

        brakeNormal = new Texture("gas_and_brake/brake_normal.png");
        brakePressed = new Texture("gas_and_brake/brake_pressed.png");

        ramSpeed = new Texture("objects/ram.speed.png");

        star = new Texture("stars/star.png");
        star_tusk = new Texture("stars/star_tusk.png");


        menu_bg = new Texture("backgrounds/menu_bg.png");

        startButton = new Texture("buttons/NEW_GAME.png");

        optionsButton = new Texture("buttons/OPTIONS.png");

        quitButton = new Texture("buttons/QUIT_GAME.png");

        backButton = new Texture("buttons/BACK.png");

        plusButton = new Texture("buttons/PLUS.png");

        minusButton = new Texture("buttons/MINUS.png");

        fullscreenOn = new Texture("buttons/FULLSCREEN_ON.png");

        fullscreenOff = new Texture("buttons/FULLSCREEN_OFF.png");

        musicPanel = new Texture("buttons/music.png");
        sfxPanel = new Texture("buttons/SFX.png");

        wheelTexture = new Texture("objects/wheel.png");

        skinsButton = new Texture("buttons/skins.png");

        pauseButton = new Texture("buttons/Pause.png");

        continueButton = new Texture("buttons/Continue.png");
        restartButton = new Texture("buttons/Restart.png");
        menuPauseButton = new Texture("buttons/Menu.png");

        trackTexture = new Texture("backgrounds/track.png");
        barrier1 = new Texture("barriers/barier1.png");
        barrier2 = new Texture("barriers/barier2.png");
        barrier3 = new Texture("barriers/barier3.png");
        barrier4 = new Texture("barriers/barier4.png");
        barrier5 = new Texture("barriers/barier5.png");

        checkmark = new Texture("stars/checkmark.png");
        clearRecords = new Texture("buttons/ClearRecords.png");

        finishLine = new Texture("objects/finish_line.png");
        leaderboardButton = new Texture("buttons/leaderboard.png");
    }

    public static final String GAS_MUSIC_PATH = "sounds/gas.mp3";
    public static final String BRAKE_MUSIC_PATH = "sounds/brake.mp3";
    public static final String FON_MUSIC_PATH = "sounds/fon_music.mp3";

    public static void dispose() {

        if (gasNormal != null) gasNormal.dispose();
        if (gasPressed != null) gasPressed.dispose();
        if (brakeNormal != null) brakeNormal.dispose();
        if (brakePressed != null) brakePressed.dispose();

        if (ramSpeed != null) ramSpeed.dispose();
        if (star != null) star.dispose();
        if (star_tusk != null) star_tusk.dispose();

        if (menu_bg != null) menu_bg.dispose();

        if (startButton != null) startButton.dispose();
        if (optionsButton != null) optionsButton.dispose();
        if (quitButton != null) quitButton.dispose();

        if (backButton != null) backButton.dispose();

        if (skinsButton != null) skinsButton.dispose();

        if (plusButton != null) plusButton.dispose();
        if (minusButton != null) minusButton.dispose();

        if (fullscreenOn != null) fullscreenOn.dispose();
        if (fullscreenOff != null) fullscreenOff.dispose();

        if (musicPanel != null) musicPanel.dispose();
        if (sfxPanel != null) sfxPanel.dispose();

        if (wheelTexture != null) wheelTexture.dispose();

        if (pauseButton != null) pauseButton.dispose();

        if (continueButton != null) continueButton.dispose();
        if (restartButton != null) restartButton.dispose();
        if (menuPauseButton != null) menuPauseButton.dispose();

        if (trackTexture != null) trackTexture.dispose();

        if (barrier1 != null) barrier1.dispose();
        if (barrier2 != null) barrier2.dispose();
        if (barrier3 != null) barrier3.dispose();
        if (barrier4 != null) barrier4.dispose();
        if (barrier5 != null) barrier5.dispose();
        if (checkmark != null) checkmark.dispose();
        if (clearRecords != null) clearRecords.dispose();
        if (finishLine != null) finishLine.dispose();
        if (leaderboardButton != null) leaderboardButton.dispose();
    }
}