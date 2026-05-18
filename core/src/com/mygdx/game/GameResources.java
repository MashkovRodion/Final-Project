package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class GameResources {
    public static Texture gasNormal;
    public static Texture gasPressed;


    public static Texture brakeNormal;
    public static Texture brakePressed;


    public void loadTextures() {
        gasNormal = new Texture("gas and brake/gas_normal.png");
        gasPressed = new Texture("gas and brake/gas_pressed.png");
        brakeNormal = new Texture("gas and brake/brake_normal.png");
        brakePressed = new Texture("gas and brake/brake_pressed.png");
    }


    public static void dispose() {
        if (gasNormal != null) gasNormal.dispose();
        if (gasPressed != null) gasPressed.dispose();
        if (brakeNormal != null) brakeNormal.dispose();
        if (brakePressed != null) brakePressed.dispose();
    }
}