package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter {

    MyGdxGame myGdxGame;
    BitmapFont font;

    Texture currentGasTexture;
    Texture currentBrakeTexture;

    Rectangle gasButtonRect;
    Rectangle brakeButtonRect;

    float currentSpeed;
    float maxSpeed;
    float acceleration;
    float brakeForce;
    float friction;

    boolean isGasPressed;
    boolean isBrakePressed;

    Vector3 touchPos;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        touchPos = new Vector3();

        currentGasTexture = GameResources.gasNormal;
        currentBrakeTexture = GameResources.brakeNormal;

        maxSpeed = GameSettings.MAX_SPEED;
        acceleration = GameSettings.ACCELERATION;
        brakeForce = GameSettings.BRAKE_FORCE;
        friction = GameSettings.FRICTION;
        currentSpeed = 0;
        isGasPressed = false;
        isBrakePressed = false;

        gasButtonRect = new Rectangle(GameSettings.GAS_X, GameSettings.BUTTON_Y, GameSettings.BUTTON_SIZE, GameSettings.BUTTON_SIZE);
        brakeButtonRect = new Rectangle(GameSettings.BRAKE_X, GameSettings.BUTTON_Y, GameSettings.BUTTON_SIZE, GameSettings.BUTTON_SIZE);
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateSpeed(delta);
        draw();
    }

    void handleInput() {
        boolean gasTouched = false;
        boolean brakeTouched = false;

        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                myGdxGame.viewport.unproject(touchPos);

                if (gasButtonRect.contains(touchPos.x, touchPos.y)) gasTouched = true;
                if (brakeButtonRect.contains(touchPos.x, touchPos.y)) brakeTouched = true;
            }
        }

        if (gasTouched != isGasPressed) {
            isGasPressed = gasTouched;
            currentGasTexture = gasTouched ? GameResources.gasPressed : GameResources.gasNormal;
        }

        if (brakeTouched != isBrakePressed) {
            isBrakePressed = brakeTouched;
            currentBrakeTexture = brakeTouched ? GameResources.brakePressed : GameResources.brakeNormal;
        }
    }

    void updateSpeed(float delta) {
        if (isGasPressed) {
            currentSpeed += acceleration * delta;
            if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;
        } else if (isBrakePressed) {
            currentSpeed -= brakeForce * delta;
            if (currentSpeed < 0) currentSpeed = 0;
        } else {
            currentSpeed *= friction;
            if (currentSpeed < 0.5f) currentSpeed = 0;
        }
    }

    void draw() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myGdxGame.viewport.apply();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        myGdxGame.batch.draw(currentGasTexture, gasButtonRect.x, gasButtonRect.y, gasButtonRect.width, gasButtonRect.height);
        myGdxGame.batch.draw(currentBrakeTexture, brakeButtonRect.x, brakeButtonRect.y, brakeButtonRect.width, brakeButtonRect.height);
        font.draw(myGdxGame.batch, "Speed: " + (int)currentSpeed, GameSettings.SPEED_TEXT_X, GameSettings.SPEED_TEXT_Y);

        myGdxGame.batch.end();
    }


    @Override
    public void dispose() {
        font.dispose();
    }
}
