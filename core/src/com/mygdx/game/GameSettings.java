package com.mygdx.game;

public class GameSettings {
    // Параметры скорости
    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 250f;
    public static final float BRAKE_FORCE = 400f;
    public static final float FRICTION = 0.98f;

    // размеры экрана
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;


    public static final int BUTTON_SIZE = 100;      // Размер кнопки
    public static final int GAS_X = 725;            // X для газа
    public static final int BRAKE_X = 645;          // X для тормоза
    public static final int BUTTON_Y = 20;          // Y от низа

    // Позиция текста скорости
    public static final int SPEED_TEXT_X = SCREEN_WIDTH - 120;  // X Справа
    public static final int SPEED_TEXT_Y = SCREEN_HEIGHT - 320;  // Y Сверху
}