package com.mygdx.game;

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
    public static Texture  clearRecords;

    public static void loadTextures() {

        gasNormal = new Texture("gas and brake/gas_normal.png");
        gasPressed = new Texture("gas and brake/gas_pressed.png");

        brakeNormal = new Texture("gas and brake/brake_normal.png");
        brakePressed = new Texture("gas and brake/brake_pressed.png");

        ramSpeed = new Texture("ram.speed.png");

        star = new Texture("stars/star.png");
        star_tusk = new Texture("stars/star_tusk.png");


        menu_bg = new Texture("menu_bg.png");

        startButton = new Texture("NEW_GAME.png");

        optionsButton = new Texture("OPTIONS.png");

        quitButton = new Texture("QUIT_GAME.png");

        backButton = new Texture("BACK.png");

        plusButton = new Texture("PLUS.png");

        minusButton = new Texture("MINUS.png");

        fullscreenOn = new Texture("FULLSCREEN_ON.png");

        fullscreenOff = new Texture("FULLSCREEN_OFF.png");

        musicPanel = new Texture("music.png");
        sfxPanel = new Texture("SFX.png");

        wheelTexture = new Texture("wheel.png");

        skinsButton = new Texture("skins.png");

        pauseButton = new Texture("Pause.png");

        continueButton = new Texture("Continue.png");
        restartButton = new Texture("Restart.png");
        menuPauseButton = new Texture("Menu.png");

        trackTexture = new Texture("track.png");
        barrier1 = new Texture("barier1.png");
        barrier2 = new Texture("barier2.png");
        barrier3 = new Texture("barier3.png");
        barrier4 = new Texture("barier4.png");
        barrier5 = new Texture("barier5.png");

        checkmark = new Texture("stars/checkmark.png");
        clearRecords = new Texture("ClearRecords.png/");
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
    }
}