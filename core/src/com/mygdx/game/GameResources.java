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

        wheelTexture = new Texture("wheel.png");

        pauseButton = new Texture("Pause.png");

        continueButton = new Texture("Continue.png");
        restartButton = new Texture("Restart.png");
        menuPauseButton = new Texture("Menu.png");
    }

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

        if (plusButton != null) plusButton.dispose();
        if (minusButton != null) minusButton.dispose();

        if (fullscreenOn != null) fullscreenOn.dispose();
        if (fullscreenOff != null) fullscreenOff.dispose();

        if (musicPanel != null) musicPanel.dispose();
        if (wheelTexture != null) wheelTexture.dispose();

        if (pauseButton != null) pauseButton.dispose();

        if (continueButton != null) continueButton.dispose();
        if (restartButton != null) restartButton.dispose();
        if (menuPauseButton != null) menuPauseButton.dispose();
    }
}