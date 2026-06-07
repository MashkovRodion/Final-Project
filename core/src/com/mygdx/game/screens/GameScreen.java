package com.mygdx.game.screens;

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
import com.mygdx.game.ui.Button;
import com.mygdx.game.utils.GameSession;
import com.mygdx.game.utils.GameSettings;
import com.mygdx.game.managers.LeaderboardManager;
import com.mygdx.game.entities.Obstacle;
import com.mygdx.game.managers.ObstacleManager;
import com.mygdx.game.options.Options;
import com.mygdx.game.managers.SkinManager;
import com.mygdx.game.entities.Track;

import com.mygdx.game.core.GameResources;
import com.mygdx.game.core.MyGdxGame;
import com.mygdx.game.utils.GameState;

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

    // Компоненты UI меню паузы, перенесенные из PauseScreen
    private final Button continueButton;
    private final Button restartButton;
    private final Button menuButton;
    private final GlyphLayout pauseTitleLayout = new GlyphLayout();

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



    private int durability = 3;
    private final float[] hpBlinkTimers = new float[3];


    private float passedDistance = 0;

    private boolean isOnSand = false;
    private boolean isSandSlowApplied = false;

    private float finishLineY;
    private boolean finishVisible = false;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isGasKeyPressed;
    private boolean isBrakeKeyPressed;

    private float wheelRotation = 0f;

    private long pausedElapsedTime = 0;

    // Объект нашей игровой сессии
    private final GameSession gameSession = new GameSession();

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

        // Основная кнопка вызова паузы
        pauseButton = new Button(GameResources.pauseButton, 0, 0, 0, 0);

        // Инициализация новых кнопок меню паузы
        continueButton = new Button(GameResources.continueButton, 0, 0, 0, 0);
        restartButton = new Button(GameResources.restartButton, 0, 0, 0, 0);
        menuButton = new Button(GameResources.menuPauseButton, 0, 0, 0, 0);

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

        roadLeftBound = worldWidth * 0.198f;
        roadRightBound = worldWidth * 0.8f;

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

        for (int i = 0; i < 3; i++) {
            if (hpBlinkTimers[i] > 0) {
                hpBlinkTimers[i] -= delta;
            }
        }


        handleInput(delta);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.gameViewport.unproject(mousePos);

        // Игровая симуляция работает исключительно в состоянии PLAYING
        if (GameSession.state == GameState.PLAYING) {
            updateSpeed(delta);
            updateCarMovement(delta);
            float steerAmount = Math.abs(wheelRotation) / 180f;

            float forwardSpeed = currentSpeed * (1f - steerAmount * 0.5f);

            passedDistance += forwardSpeed * delta * 3f;

            float distanceLeft = GameSettings.FINISH_DISTANCE - passedDistance;

            if (distanceLeft <= 500 && !finishVisible) {
                finishVisible = true;
                finishLineY = game.gameViewport.getWorldHeight() + 200;
                obstacleManager.clear();
            }

            checkCollisions();

            track.update(forwardSpeed * 3f, delta);
            obstacleManager.update(forwardSpeed * 3f, delta, passedDistance);

            if (finishVisible) {
                finishLineY -= currentSpeed * 3f * delta;
            }
            if (finishVisible) {
                Rectangle finishRect = new Rectangle(roadLeftBound, finishLineY, roadRightBound - roadLeftBound, 80);
                Rectangle carRect = new Rectangle(carX, carY, carWidth, carHeight);

                if (finishRect.overlaps(carRect)) {
                    long finishTime = TimeUtils.timeSinceMillis(startTime);
                    LeaderboardManager.addTime(finishTime);
                    resetControls();
                    gameSession.state = GameState.ENDED;
                    game.setScreen(new FinishScreen(game, getFormattedTime()));
                    return;
                }
            }
            // Подсветка кнопки вызова паузы работает в активной игре
            pauseButton.update(mousePos.x, mousePos.y);
        }
        // Если игра на паузе — обновляем ховеры кнопок меню паузы
        else if (GameSession.state == GameState.PAUSED) {
            continueButton.update(mousePos.x, mousePos.y);
            restartButton.update(mousePos.x, mousePos.y);
            menuButton.update(mousePos.x, mousePos.y);
        }

        draw(delta);
    }

    private void updateCarMovement(float delta) {
        float steerFactor = wheelRotation / 180f;
        carX -= steerFactor * currentSpeed * 5f * delta;

        carX = MathUtils.clamp(carX, roadLeftBound, roadRightBound - carWidth);

        float t = currentSpeed / maxSpeed;
        float targetY = roadBottomBound + t * (roadTopBound - roadBottomBound);

        carY += (targetY - carY) * delta * 3f;
        carY = MathUtils.clamp(carY, roadBottomBound, roadTopBound);

        carSprite.setPosition(carX, carY);
        carSprite.setRotation(wheelRotation * 0.4f);
    }

    private void checkCollisions() {

        isOnSand = false;
        Rectangle carBounds = new Rectangle(carX, carY, carWidth, carHeight);

        for (Obstacle obstacle : obstacleManager.getObstacles()) {
            if (obstacle.getBounds().overlaps(carBounds)) {
                if (obstacle.getType() == 4) {
                    isOnSand = true;
                    break;
                }

                if (game.audioManager != null && game.audioManager.crashSound != null) {
                    game.audioManager.crashSound.play(Options.soundVolume);
                }

                durability--;
                hpBlinkTimers[durability] = 3f;
                obstacleManager.removeObstacle(obstacle);
                currentSpeed *= 0.5f;

                if (durability <= 0) {
                    resetControls();
                    game.setScreen(new GameOverScreen(game, getFormattedTime()));
                }
                break;
            }
        }

        if (isOnSand && !isSandSlowApplied) {
            currentSpeed *= 0.5f;
            isSandSlowApplied = true;
        }

        if (!isOnSand) {
            isSandSlowApplied = false;
        }
    }

    private String getFormattedTime() {
        long elapsed = TimeUtils.timeSinceMillis(startTime);

        long mins = elapsed / 60000;
        long secs = (elapsed % 60000) / 1000;
        long millis = elapsed % 1000;

        return String.format("%02d:%02d.%03d", mins, secs, millis);
    }

    private void handleInput(float delta) {
        if (GameSession.state != GameState.PLAYING) {
            return;
        }

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

        if (!isWheelPressed && !isLeftKeyPressed && !isRightKeyPressed) {
            float returnSpeed = 250f * delta;
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
            float accelerationMultiplier;

            switch (durability) {
                case 3:
                    accelerationMultiplier = 1.0f;
                    break;
                case 2:
                    accelerationMultiplier = 0.7f;
                    break;
                case 1:
                    accelerationMultiplier = 0.4f;
                    break;
                default:
                    accelerationMultiplier = 0.0f;
            }

            if (isOnSand) {
                accelerationMultiplier *= 0.2f;
            }

            currentSpeed += acceleration * accelerationMultiplier * delta;
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
        Gdx.gl.glEnable(GL20.GL_BLEND);

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

        float hpX = 20;
        float hpY = game.gameViewport.getWorldHeight() - 60;

        float hpSize = 60;
        float hpSpacing = 50;

        for (int i = 0; i < 3; i++) {

            if (i < durability) {

                game.batch.draw(
                        GameResources.durabilityFull,
                        hpX + i * hpSpacing,
                        hpY,
                        hpSize,
                        hpSize
                );
            }
            else {

                if (hpBlinkTimers[i] > 0) {

                    boolean visible =
                            ((int)(hpBlinkTimers[i] * 6f)) % 2 == 0;

                    if (visible) {

                        game.batch.draw(
                                GameResources.durabilityFull,
                                hpX + i * hpSpacing,
                                hpY,
                                hpSize,
                                hpSize
                        );
                    }
                }
            }
        }

        String speedText = String.valueOf((int) currentSpeed);
        speedLayout.setText(font, speedText);

        font.draw(
                game.batch,
                speedLayout,
                dynamicRamSpeedX + (speedometerWidth - speedLayout.width) / 2f,
                speedometerY + (speedometerHeight + speedLayout.height) / 2f
        );

        long elapsed;

        if (GameSession.state == GameState.PAUSED) {
            elapsed = pausedElapsedTime;
        } else {
            elapsed = TimeUtils.timeSinceMillis(startTime);
        }

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
        pauseButton.draw(game.batch);

        if (GameSession.state == GameState.PAUSED) {

            game.batch.setColor(0f, 0f, 0f, 0.8f);

            game.batch.draw(
                    GameResources.darkOverlay,
                    0,
                    0,
                    worldWidth,
                    worldHeight
            );

            game.batch.setColor(1f, 1f, 1f, 1f);

            font.getData().setScale(2f);

            pauseTitleLayout.setText(font, "GAME PAUSED");
            font.draw(
                    game.batch,
                    pauseTitleLayout,
                    (worldWidth - pauseTitleLayout.width) / 2f,
                    worldHeight * 0.85f
            );

            font.getData().setScale(1.5f);

            continueButton.draw(game.batch);
            restartButton.draw(game.batch);
            menuButton.draw(game.batch);
        }

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

        float bw = w * 0.45f;
        float bh = h * 0.4f;
        float cx = w / 2f - bw / 2f;

        continueButton.setPosition(cx, h * 0.41f, bw, bh);
        restartButton.setPosition(cx, h * 0.21f, bw, bh);
        menuButton.setPosition(cx, h * 0.01f, bw, bh);
    }

    public void startNewGame() {
        gameSession.startGame();
        startTime = gameSession.startTime;

        currentSpeed = 0;
        passedDistance = 0;
        finishVisible = false;
        finishLineY = 0;
        durability = 3;

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

        if (GameSession.state == GameState.PLAYING) {
            if (pauseButton.isTapped(touchPos.x, touchPos.y)) {
                if (game.audioManager != null) {
                    if (game.audioManager.gasMusic.isPlaying()) game.audioManager.gasMusic.pause();
                    if (game.audioManager.brakeMusic.isPlaying())
                        game.audioManager.brakeMusic.pause();
                }
                resetControls();
                pausedElapsedTime = TimeUtils.timeSinceMillis(startTime);
                gameSession.pauseGame();
                return true;
            }
        } else if (GameSession.state == GameState.PAUSED) {
            if (continueButton.isTapped(touchPos.x, touchPos.y)) {
                gameSession.resumeGame();
                this.startTime = gameSession.startTime;
                return true;
            }

            if (restartButton.isTapped(touchPos.x, touchPos.y)) {
                startNewGame();
                return true;
            }

            if (menuButton.isTapped(touchPos.x, touchPos.y)) {
                gameSession.state = GameState.ENDED;
                game.setScreen(game.menuScreen);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (GameSession.state == GameState.PLAYING) {
                if (game.audioManager != null) {
                    if (game.audioManager.gasMusic.isPlaying()) game.audioManager.gasMusic.pause();
                    if (game.audioManager.brakeMusic.isPlaying())
                        game.audioManager.brakeMusic.pause();
                }
                resetControls();
                pausedElapsedTime = TimeUtils.timeSinceMillis(startTime);
                gameSession.pauseGame();
                return true;
            } else if (GameSession.state == GameState.PAUSED) {
                gameSession.resumeGame();
                this.startTime = gameSession.startTime;
                return true;
            }
        }

        // Кнопки вождения активны только в режиме игры
        if (GameSession.state == GameState.PLAYING) {
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