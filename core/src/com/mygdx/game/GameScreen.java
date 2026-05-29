package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final BitmapFont font;

    private float currentSpeed = 0;
    private float maxSpeed;
    private float acceleration;
    private float brakeForce;
    private float friction;

    private boolean isGasPressed;
    private boolean isBrakePressed;

    private Rectangle gasButtonRect;
    private Rectangle brakeButtonRect;

    private Vector3 touchPos;

    private long startTime;

    private com.badlogic.gdx.graphics.Texture currentGasTexture;
    private com.badlogic.gdx.graphics.Texture currentBrakeTexture;

    public GameScreen(MyGdxGame game) {

        this.game = game;

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        touchPos = new Vector3();

        maxSpeed = GameSettings.MAX_SPEED;
        acceleration = GameSettings.ACCELERATION;
        brakeForce = GameSettings.BRAKE_FORCE;
        friction = GameSettings.FRICTION;

        gasButtonRect = new Rectangle(
                GameSettings.GAS_X,
                GameSettings.GAZ_Y,
                GameSettings.BUTTON_SIZE,
                GameSettings.BUTTON_SIZE
        );

        brakeButtonRect = new Rectangle(
                GameSettings.BRAKE_X,
                GameSettings.BRAKE_Y,
                GameSettings.BUTTON_SIZE,
                GameSettings.BUTTON_SIZE
        );

        currentGasTexture = GameResources.gasNormal;
        currentBrakeTexture = GameResources.brakeNormal;
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {

        game.gameViewport.apply();

        handleInput();
        updateSpeed(delta);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.gameCamera.combined);

        game.batch.begin();

        game.batch.draw(GameResources.ramSpeed,
                GameSettings.RAM_SPEED_X,
                GameSettings.RAM_SPEED_Y);

        game.batch.draw(currentGasTexture,
                gasButtonRect.x,
                gasButtonRect.y,
                gasButtonRect.width,
                gasButtonRect.height);

        game.batch.draw(currentBrakeTexture,
                brakeButtonRect.x,
                brakeButtonRect.y,
                brakeButtonRect.width,
                brakeButtonRect.height);

        font.draw(game.batch,
                "" + (int) currentSpeed,
                GameSettings.SPEED_TEXT_X,
                GameSettings.SPEED_TEXT_Y);

        game.batch.end();
    }

    private void handleInput() {

        boolean gasTouched = false;
        boolean brakeTouched = false;

        for (int i = 0; i < 5; i++) {

            if (Gdx.input.isTouched(i)) {

                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                game.gameViewport.unproject(touchPos);

                if (gasButtonRect.contains(touchPos.x, touchPos.y))
                    gasTouched = true;

                if (brakeButtonRect.contains(touchPos.x, touchPos.y))
                    brakeTouched = true;
            }
        }

        isGasPressed = gasTouched;
        isBrakePressed = brakeTouched;

        currentGasTexture = gasTouched ?
                GameResources.gasPressed :
                GameResources.gasNormal;

        currentBrakeTexture = brakeTouched ?
                GameResources.brakePressed :
                GameResources.brakeNormal;
    }

    private void updateSpeed(float delta) {

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
}