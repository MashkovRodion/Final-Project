package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final BitmapFont font;

    private float currentSpeed = 0;
    private final float maxSpeed;
    private final float acceleration;
    private final float brakeForce;
    private final float friction;

    private boolean isGasPressed;
    private boolean isBrakePressed;

    private final Rectangle gasButtonRect;
    private final Rectangle brakeButtonRect;

    private final Vector3 touchPos;
    private long startTime;

    private Texture currentGasTexture;
    private Texture currentBrakeTexture;

    //ПЕРЕМЕННЫЕ ДЛЯ РУЛЯ
    private Sprite steeringWheel;
    private final Rectangle wheelBoundsRect;
    private float wheelCenterX;
    private float wheelCenterY;
    private boolean isWheelPressed;
    private int wheelPointerId = -1;
    private float startWheelRotation;
    private float startTouchAngle;
    private float dynamicRamSpeedX;

    public GameScreen(MyGdxGame game) {
        this.game = game;

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        touchPos = new Vector3();

        maxSpeed = GameSettings.MAX_SPEED;
        acceleration = GameSettings.ACCELERATION;
        brakeForce = GameSettings.BRAKE_FORCE;
        friction = GameSettings.FRICTION;

        // Выделяем память под прямоугольники один раз
        gasButtonRect = new Rectangle();
        brakeButtonRect = new Rectangle();
        wheelBoundsRect = new Rectangle();

        currentGasTexture = GameResources.gasNormal;
        currentBrakeTexture = GameResources.brakeNormal;

        Texture wheelTexture = GameResources.wheelTexture;
        steeringWheel = new Sprite(wheelTexture);
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void resize(int width, int height) {
        // Получаем текущие размеры экрана из ExtendViewport (первая версия)
        float worldWidth = game.gameViewport.getWorldWidth();
        //float worldHeight = game.gameViewport.getWorldHeight();

        // Расчет динамического отступа для правого края под физический экран
        float rightEdgeX = worldWidth - GameSettings.BUTTON_SIZE - 40f;

        // Пересчет позиций педалей и рамки спидометра
        gasButtonRect.set(rightEdgeX, GameSettings.GAZ_Y, GameSettings.BUTTON_SIZE, GameSettings.BUTTON_SIZE);
        brakeButtonRect.set(rightEdgeX, GameSettings.BRAKE_Y, GameSettings.BUTTON_SIZE, GameSettings.BUTTON_SIZE);
        dynamicRamSpeedX = rightEdgeX - (GameSettings.GAS_X - GameSettings.RAM_SPEED_X);

        // Позиционирование руля у левого края
        float wSize = GameSettings.WHEEL_SIZE;
        float wX = GameSettings.PADDING_X;
        float wY = GameSettings.PADDING_Y;

        steeringWheel.setSize(wSize, wSize);
        steeringWheel.setPosition(wX, wY);
        steeringWheel.setOrigin(wSize / 2f, wSize / 2f);

        // Центр руля для математики вращения
        wheelCenterX = wX + wSize / 2f;
        wheelCenterY = wY + wSize / 2f;

        // Хитбокс руля (чуть шире для удобства тача)
        wheelBoundsRect.set(wX - 20, wY - 20, wSize + 40, wSize + 40);
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateSpeed(delta);
        draw(delta);
    }

    private void handleInput() {
        boolean gasTouched = false;
        boolean brakeTouched = false;
        boolean stillHoldingWheel = false;

        // Опрос мультитача (до 5 пальцев)
        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                game.gameViewport.unproject(touchPos);

                if (gasButtonRect.contains(touchPos.x, touchPos.y)) gasTouched = true;
                if (brakeButtonRect.contains(touchPos.x, touchPos.y)) brakeTouched = true;

                // Захват руля
                if (!isWheelPressed && wheelBoundsRect.contains(touchPos.x, touchPos.y)) {
                    isWheelPressed = true;
                    wheelPointerId = i;
                    startWheelRotation = steeringWheel.getRotation();
                    startTouchAngle = MathUtils.atan2(touchPos.y - wheelCenterY, touchPos.x - wheelCenterX) * MathUtils.radiansToDegrees;
                }

                // Вращение руля выбранным пальцем
                if (isWheelPressed && i == wheelPointerId) {
                    stillHoldingWheel = true;
                    float currentTouchAngle = MathUtils.atan2(touchPos.y - wheelCenterY, touchPos.x - wheelCenterX) * MathUtils.radiansToDegrees;
                    float angleDelta = currentTouchAngle - startTouchAngle;
                    steeringWheel.setRotation(startWheelRotation + angleDelta);
                }
            }
        }

        if (!stillHoldingWheel) {
            isWheelPressed = false;
            wheelPointerId = -1;
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

        // Автовозврат руля
        if (!isWheelPressed) {
            float currentRotation = steeringWheel.getRotation();
            float returnSpeed = 400f * delta;

            if (Math.abs(currentRotation) < returnSpeed) {
                steeringWheel.setRotation(0);
            } else {
                steeringWheel.setRotation(currentRotation - Math.signum(currentRotation) * returnSpeed);
            }
        }
    }

    private void draw(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.gameViewport.apply();
        game.batch.setProjectionMatrix(game.gameCamera.combined);
        game.batch.begin();

        float worldWidth = game.gameViewport.getWorldWidth();
        float worldHeight = game.gameViewport.getWorldHeight();

        // Отрисовка интерфейса педалей по динамическим координатам
        game.batch.draw(GameResources.ramSpeed, GameSettings.RAM_SPEED_X, GameSettings.RAM_SPEED_Y, gasButtonRect.width, gasButtonRect.height);
        game.batch.draw(currentGasTexture, gasButtonRect.x, gasButtonRect.y, gasButtonRect.width, gasButtonRect.height);
        game.batch.draw(currentBrakeTexture, brakeButtonRect.x, brakeButtonRect.y, brakeButtonRect.width, brakeButtonRect.height);

        // Отрисовка руля
        steeringWheel.draw(game.batch);

        // Текст скорости
        font.draw(game.batch, " " + (int)currentSpeed, gasButtonRect.x + 10, GameSettings.RAM_SPEED_Y + 60);

        // Таймер и звезды
        long totalSeconds = TimeUtils.timeSinceMillis(startTime) / 1000;
        long mins = totalSeconds / 60;
        long secs = totalSeconds % 60;
        String strMins = (mins < 10) ? "0" + mins : "" + mins;
        String strSecs = (secs < 10) ? "0" + secs : "" + secs;

        font.draw(game.batch, strMins + ":" + strSecs, worldWidth / 2f - 25, worldHeight - 20);

        float starY = worldHeight - 50;
        game.batch.draw((totalSeconds >= 60) ? GameResources.star_tusk : GameResources.star, 40, starY, 40, 40);
        game.batch.draw((totalSeconds >= 40) ? GameResources.star_tusk : GameResources.star, 90, starY, 40, 40);
        game.batch.draw((totalSeconds >= 20) ? GameResources.star_tusk : GameResources.star, 140, starY, 40, 40);

        game.batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        if (steeringWheel != null && steeringWheel.getTexture() != null) {
            steeringWheel.getTexture().dispose();
        }
    }
}
