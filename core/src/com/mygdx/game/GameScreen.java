package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Input;

public class GameScreen extends ScreenAdapter implements InputProcessor {

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

    private Button pauseButton;

    private Texture currentGasTexture;
    private Texture currentBrakeTexture;

    private Sprite steeringWheel;
    private Sprite carSprite;
    private final Rectangle wheelBoundsRect;
    private float wheelCenterX;
    private float wheelCenterY;
    private boolean isWheelPressed;
    private int wheelPointerId = -1;
    private float startWheelRotation;
    private float startTouchAngle;
    private float dynamicRamSpeedX;

    private float pedalSize;
    private float speedometerY;
    float speedometerWidth;
    float speedometerHeight;

    private ObstacleManager obstacleManager;

    private final GlyphLayout speedLayout = new GlyphLayout();

    private Track track;

    private float carX;
    private float carY;
    private float carWidth;
    private float carHeight;
    private float roadLeftBound;
    private float roadRightBound;
    private float roadBottomBound;
    private float roadTopBound;
    private float carMoveSpeed = 500f;
    private float carVerticalSpeed = 300f;

    private float passedDistance = 0;

    private float finishLineY;
    private boolean finishVisible = false;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isGasKeyPressed;
    private boolean isBrakeKeyPressed;

    // Для бесконечного вращения руля
    private float wheelRotation = 0f;

    public GameScreen(MyGdxGame game) {
        this.game = game;
        startTime = TimeUtils.millis();

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        touchPos = new Vector3();

        maxSpeed = GameSettings.MAX_SPEED;
        acceleration = GameSettings.ACCELERATION;
        brakeForce = GameSettings.BRAKE_FORCE;
        friction = GameSettings.FRICTION;

        gasButtonRect = new Rectangle();
        brakeButtonRect = new Rectangle();
        wheelBoundsRect = new Rectangle();

        currentGasTexture = GameResources.gasNormal;
        currentBrakeTexture = GameResources.brakeNormal;

        pauseButton = new Button(GameResources.pauseButton, 0, 0, 0, 0);

        Texture wheelTexture = GameResources.wheelTexture;
        steeringWheel = new Sprite(wheelTexture);

        carSprite = new Sprite(SkinManager.getCurrentCarTexture());
    }

    public void updateCarSkin() {
        if (carSprite != null) {
            carSprite.setTexture(SkinManager.getCurrentCarTexture());
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

        if (track == null) {
            track = new Track(
                    GameResources.trackTexture,
                    game.gameViewport.getWorldWidth(),
                    GameResources.trackTexture.getHeight()
            );
        }

        if (obstacleManager == null) {
            obstacleManager = new ObstacleManager(
                    game.gameViewport.getWorldWidth()
            );
        }

        updateLayout();
    }

    @Override
    public void resize(int width, int height) {
        float worldWidth = game.gameViewport.getWorldWidth();
        float worldHeight = game.gameViewport.getWorldHeight();

        // Границы трассы (X)
        roadLeftBound = worldWidth * 0.198f;
        roadRightBound = worldWidth * 0.8f;

        // Границы трассы (Y) - машина может двигаться по вертикали
        roadBottomBound = worldHeight * 0.05f;
        roadTopBound = worldHeight * 0.75f;

        pedalSize = worldHeight * 0.18f;
        float pedalSpacing = pedalSize * 0.15f;
        float pedalMargin = worldHeight * 0.03f;
        float pedalY = pedalMargin;

        float gasX = worldWidth - pedalSize - pedalMargin;
        float brakeX = gasX - pedalSize - pedalSpacing;

        gasButtonRect.set(gasX, pedalY, pedalSize, pedalSize);
        brakeButtonRect.set(brakeX, pedalY - 10, pedalSize, pedalSize + 13);

        speedometerWidth = pedalSize * 1.8f;
        speedometerHeight = pedalSize;

        float pedalsCenterX = (brakeButtonRect.x + gasButtonRect.x + gasButtonRect.width) / 2f;
        dynamicRamSpeedX = pedalsCenterX - speedometerWidth / 2f;
        speedometerY = pedalY + pedalSize + pedalMargin - 40;

        float wSize = GameSettings.WHEEL_SIZE;
        float wX = GameSettings.PADDING_X;
        float wY = GameSettings.PADDING_Y;

        steeringWheel.setSize(wSize, wSize);
        steeringWheel.setPosition(wX, wY);
        steeringWheel.setOrigin(wSize / 2f, wSize / 2f);

        wheelCenterX = wX + wSize / 2f;
        wheelCenterY = wY + wSize / 2f;

        wheelBoundsRect.set(wX - 20, wY - 20, wSize + 40, wSize + 40);

        track = new Track(
                GameResources.trackTexture,
                worldWidth,
                GameResources.trackTexture.getHeight()
        );

        carWidth = worldHeight * 0.1f;
        carHeight = worldHeight * 0.18f;

        // Начальная позиция по центру дороги и внизу экрана
        float roadCenter = (roadLeftBound + roadRightBound - carWidth) / 2f;
        carX = MathUtils.clamp(roadCenter, roadLeftBound, roadRightBound - carWidth);
        carY = roadBottomBound;

        carSprite.setSize(carWidth, carHeight);
        carSprite.setPosition(carX, carY);
        carSprite.setOrigin(carWidth / 2f, carHeight / 2f);

        carSprite.setRotation(wheelRotation);

        updateLayout();
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateSpeed(delta);
        updateCarMovement(delta);
        passedDistance += currentSpeed * delta * 3f;

        float distanceLeft =
                GameSettings.FINISH_DISTANCE - passedDistance;

        if (distanceLeft <= 500 && !finishVisible) {

            finishVisible = true;

            finishLineY =
                    game.gameViewport.getWorldHeight() + 200;

            obstacleManager.clear(); // удалить все препятствия
        }

        checkCollisions();

        track.update(currentSpeed * 3f, delta);
        obstacleManager.update(currentSpeed * 3f, delta, passedDistance);

        if (finishVisible) {
            finishLineY -= currentSpeed * 3f * delta;
        }
        if (finishVisible) {

            Rectangle finishRect =
                    new Rectangle(
                            roadLeftBound,
                            finishLineY,
                            roadRightBound - roadLeftBound,
                            80
                    );

            Rectangle carRect =
                    new Rectangle(
                            carX,
                            carY,
                            carWidth,
                            carHeight
                    );

            if (finishRect.overlaps(carRect)) {

                resetControls();

                game.setScreen(
                        new FinishScreen(
                                game,
                                getFormattedTime()
                        )
                );

                return;
            }
        }


        draw(delta);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.gameViewport.unproject(mousePos);
        pauseButton.update(mousePos.x, mousePos.y);
    }

    private void updateCarMovement(float delta) {

        float steerFactor = wheelRotation / 180f;

        // движение в стороны зависит от скорости
        carX -= steerFactor * currentSpeed * 5f * delta;

        carX = MathUtils.clamp(
                carX,
                roadLeftBound,
                roadRightBound - carWidth
        );

        float t = currentSpeed / maxSpeed;

        float targetY =
                roadBottomBound +
                        t * (roadTopBound - roadBottomBound);

        carY += (targetY - carY) * delta * 3f;

        carY = MathUtils.clamp(
                carY,
                roadBottomBound,
                roadTopBound
        );

        carSprite.setPosition(carX, carY);

        // машина поворачивается слабее руля
        carSprite.setRotation(wheelRotation * 0.4f);
    }

    private void checkCollisions() {

        Rectangle carBounds =
                new Rectangle(carX, carY, carWidth, carHeight);

        for (Obstacle obstacle : obstacleManager.getObstacles()) {

            if (obstacle.getBounds().overlaps(carBounds)) {

                // barrier5
                if (obstacle.getType() == 4) {

                    currentSpeed *= 0.9f; // потерять 50% скорости

                    break;
                }

                resetControls();
                game.setScreen(new GameOverScreen(game, getFormattedTime()));
                break;
            }
        }
    }

    private String getFormattedTime() {

        long elapsed = TimeUtils.timeSinceMillis(startTime);

        long mins = elapsed / 60000;
        long secs = (elapsed % 60000) / 1000;
        long millis = elapsed % 1000;

        return String.format("%02d:%02d.%03d",
                mins,
                secs,
                millis);
    }

    private void handleInput(float delta) {
        boolean gasTouched = false;
        boolean brakeTouched = false;
        boolean stillHoldingWheel = false;

        for (int i = 0; i < 5; i++) {
            if (Gdx.input.isTouched(i)) {
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                game.gameViewport.unproject(touchPos);

                if (gasButtonRect.contains(touchPos.x, touchPos.y)) gasTouched = true;
                if (brakeButtonRect.contains(touchPos.x, touchPos.y)) brakeTouched = true;

                if (!isWheelPressed && wheelBoundsRect.contains(touchPos.x, touchPos.y)) {
                    isWheelPressed = true;
                    wheelPointerId = i;
                    startWheelRotation = wheelRotation;
                    startTouchAngle = MathUtils.atan2(touchPos.y - wheelCenterY, touchPos.x - wheelCenterX) * MathUtils.radiansToDegrees;
                }

                if (isWheelPressed && i == wheelPointerId) {
                    stillHoldingWheel = true;
                    float currentTouchAngle = MathUtils.atan2(touchPos.y - wheelCenterY, touchPos.x - wheelCenterX) * MathUtils.radiansToDegrees;
                    float angleDelta = currentTouchAngle - startTouchAngle;
                    wheelRotation = startWheelRotation + angleDelta;
                    // Ограничиваем угол поворота руля (опционально)
                    wheelRotation = MathUtils.clamp(wheelRotation, -180f, 180f);
                    steeringWheel.setRotation(wheelRotation);
                    carSprite.setRotation(wheelRotation);
                }
            }
        }

        if (!stillHoldingWheel) {
            isWheelPressed = false;
            wheelPointerId = -1;
        }

        // Автовозврат руля
        if (!isWheelPressed && !isLeftKeyPressed && !isRightKeyPressed) {
            float returnSpeed = 300f * delta;

            if (Math.abs(wheelRotation) < returnSpeed) {
                wheelRotation = 0;
            } else {
                wheelRotation -= Math.signum(wheelRotation) * returnSpeed;
            }
            steeringWheel.setRotation(wheelRotation);
            carSprite.setRotation(wheelRotation);
        }
        if (isLeftKeyPressed) {
            wheelRotation += 175f * delta;
        }

        if (isRightKeyPressed) {
            wheelRotation -= 175f * delta;
        }

        wheelRotation = MathUtils.clamp(wheelRotation, -180f, 180f);
        steeringWheel.setRotation(wheelRotation);

        isGasPressed = gasTouched || isGasKeyPressed;
        isBrakePressed = brakeTouched || isBrakeKeyPressed;

        currentGasTexture = isGasPressed ? GameResources.gasPressed : GameResources.gasNormal;
        currentBrakeTexture = isBrakePressed ? GameResources.brakePressed : GameResources.brakeNormal;

        if (game.audioManager != null) {
            if (isGasPressed) {
                if (!game.audioManager.gasMusic.isPlaying()) {
                    game.audioManager.gasMusic.setVolume(Options.soundVolume);
                    game.audioManager.gasMusic.setLooping(true);
                    game.audioManager.gasMusic.play();
                }
            } else {
                game.audioManager.gasMusic.pause();
            }

            if (isBrakePressed) {
                if (!game.audioManager.brakeMusic.isPlaying()) {
                    game.audioManager.brakeMusic.setVolume(Options.soundVolume);
                    game.audioManager.brakeMusic.setLooping(true);
                    game.audioManager.brakeMusic.play();
                }
            } else {
                game.audioManager.brakeMusic.pause();
            }
        }
    }

    private void updateSpeed(float delta) {
        if (isGasPressed && !isBrakePressed) {
            currentSpeed += acceleration * delta;
            if (currentSpeed > maxSpeed) {
                currentSpeed = maxSpeed;
            }
        } else if (isBrakePressed && !isGasPressed) {
            currentSpeed -= brakeForce * delta;
            if (currentSpeed < 0) {
                currentSpeed = 0;
            }
        } else {
            currentSpeed *= friction;
            if (currentSpeed < 0.5f) {
                currentSpeed = 0;
            }
        }


    }

    public void draw(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.gameViewport.apply();
        game.batch.setProjectionMatrix(game.gameCamera.combined);
        game.batch.begin();

        track.draw(game.batch);

        if (finishVisible) {

            game.batch.draw(
                    GameResources.finishLine,
                    roadLeftBound,
                    finishLineY,
                    roadRightBound - roadLeftBound,
                    80
            );
        }
        obstacleManager.draw(game.batch);

        float worldWidth = game.gameViewport.getWorldWidth();
        float worldHeight = game.gameViewport.getWorldHeight();

        game.batch.draw(GameResources.ramSpeed, dynamicRamSpeedX, speedometerY, speedometerWidth, speedometerHeight);
        game.batch.draw(currentGasTexture, gasButtonRect.x, gasButtonRect.y, gasButtonRect.width, gasButtonRect.height);
        game.batch.draw(currentBrakeTexture, brakeButtonRect.x, brakeButtonRect.y, brakeButtonRect.width, brakeButtonRect.height);

        steeringWheel.draw(game.batch);
        carSprite.draw(game.batch);

        String speedText = String.valueOf((int) currentSpeed);
        speedLayout.setText(font, speedText);

        font.draw(
                game.batch,
                speedLayout,
                dynamicRamSpeedX + (speedometerWidth - speedLayout.width) / 2f,
                speedometerY + (speedometerHeight + speedLayout.height) / 2f
        );

        long elapsed = TimeUtils.timeSinceMillis(startTime);

        long mins = elapsed / 60000;
        long secs = (elapsed % 60000) / 1000;
        long millis = elapsed % 1000;

        String timerText = String.format("%02d:%02d.%03d", mins, secs, millis);

        font.draw(
                game.batch,
                timerText,
                worldWidth / 2f - 60,
                worldHeight - 20
        );
//        long totalSeconds = elapsed / 1000;


//        float starY = worldHeight - 50;
//        game.batch.draw((totalSeconds >= 60) ? GameResources.star_tusk : GameResources.star, 40, starY, 40, 40);
//        game.batch.draw((totalSeconds >= 40) ? GameResources.star_tusk : GameResources.star, 90, starY, 40, 40);
//        game.batch.draw((totalSeconds >= 20) ? GameResources.star_tusk : GameResources.star, 140, starY, 40, 40);
//        pauseButton.draw(game.batch);
          game.batch.end();
    }

    private void updateLayout() {
        float w = game.gameViewport.getWorldWidth();
        float h = game.gameViewport.getWorldHeight();

        float buttonSize = h * 0.1f;
        float margin = h * 0.02f;
        float buttonWidth = h * 0.2f;

        pauseButton.setPosition(
                w - buttonSize - margin - 10,
                h - buttonSize - margin + 10,
                buttonWidth,
                buttonSize
        );
    }

    public void startNewGame() {
        startTime = TimeUtils.millis();
        currentSpeed = 0;
        passedDistance = 0;
        finishVisible = false;
        finishLineY = 0;

        float roadCenter = (roadLeftBound + roadRightBound - carWidth) / 2f;
        carX = MathUtils.clamp(roadCenter, roadLeftBound, roadRightBound - carWidth);
        carY = roadBottomBound;
        carSprite.setPosition(carX, carY);

        resetControls();

        if (obstacleManager != null) {
            obstacleManager.clear();
        }

        if (track != null) {
            track.reset();
        }
    }

    private void resetControls() {
        isGasPressed = false;
        isBrakePressed = false;
        isWheelPressed = false;
        isGasKeyPressed = false;
        isBrakeKeyPressed = false;

        isLeftKeyPressed = false;
        isRightKeyPressed = false;
        wheelPointerId = -1;

        currentGasTexture = GameResources.gasNormal;
        currentBrakeTexture = GameResources.brakeNormal;

        wheelRotation = 0;
        steeringWheel.setRotation(0);
        carSprite.setRotation(0);

        if (game.audioManager != null) {
            if (game.audioManager.gasMusic.isPlaying()) {
                game.audioManager.gasMusic.pause();
            }
            if (game.audioManager.brakeMusic.isPlaying()) {
                game.audioManager.brakeMusic.pause();
            }
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        game.gameViewport.unproject(touchPos);

        if (pauseButton.isTapped(touchPos.x, touchPos.y)) {
            if (game.audioManager != null) {
                if (game.audioManager.gasMusic.isPlaying()) {
                    game.audioManager.gasMusic.pause();
                }
                if (game.audioManager.brakeMusic.isPlaying()) {
                    game.audioManager.brakeMusic.pause();
                }
            }
            resetControls();
            game.setScreen(game.pauseScreen);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            resetControls();
            game.setScreen(game.pauseScreen);
            return true;
        }
        if (keycode == Input.Keys.W) {
            isGasKeyPressed = true;
            return true;
        }

        if (keycode == Input.Keys.S) {
            isBrakeKeyPressed = true;
            return true;
        }

        if (keycode == Input.Keys.A) {
            isLeftKeyPressed = true;
            return true;
        }

        if (keycode == Input.Keys.D) {
            isRightKeyPressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            isGasKeyPressed = false;
            return true;
        }

        if (keycode == Input.Keys.S) {
            isBrakeKeyPressed = false;
            return true;
        }

        if (keycode == Input.Keys.A) {
            isLeftKeyPressed = false;
            return true;
        }

        if (keycode == Input.Keys.D) {
            isRightKeyPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}