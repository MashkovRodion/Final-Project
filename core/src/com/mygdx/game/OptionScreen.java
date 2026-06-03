package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class OptionScreen implements Screen, InputProcessor {

    private MyGdxGame game;
    private BitmapFont font;

    private Button fullscreenButton;
    private Button buttonMusicPlus;
    private Button buttonMusicMinus;

    private Button buttonSoundPlus;
    private Button buttonSoundMinus;

    private Button buttonBack;
    private Button clearRecordsButton;


    private int oldWindowWidth = 1280;
    private int oldWindowHeight = 720;

    // ===== PANEL DATA =====

    private float panelX;
    private float buttonY;
    private float panelY;
    private float panelWidth;
    private float panelHeight;

    private final GlyphLayout volumeLayout = new GlyphLayout();
    private final GlyphLayout soundLayout = new GlyphLayout();

    // ===== ТАЙМЕРЫ ДЛЯ УДЕРЖАНИЯ КНОПОК =====
    private Task autoRepeatTask;
    private int activePointerId = -1;

    public OptionScreen(MyGdxGame game) {
        this.game = game;
        font = new BitmapFont();

        fullscreenButton = new Button(GameResources.fullscreenOff, 0,0,0,0);
        buttonMusicPlus = new Button(GameResources.plusButton,0,0,0,0);
        buttonMusicMinus = new Button(GameResources.minusButton,0,0,0,0);

        buttonSoundPlus = new Button(GameResources.plusButton,0,0,0,0);
        buttonSoundMinus = new Button(GameResources.minusButton,0,0,0,0);

        buttonBack = new Button(GameResources.backButton,0,0,0,0);
        clearRecordsButton = new Button(GameResources.clearRecords, 0, 0, 0, 0);

    }

    @Override
    public void show() {
        updateLayout();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        game.uiViewport.apply();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 mousePos = new Vector3(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        game.uiViewport.unproject(mousePos);

        float mouseX = mousePos.x;
        float mouseY = mousePos.y;

        fullscreenButton.update(mouseX, mouseY);
        clearRecordsButton.update(mouseX, mouseY);
        buttonMusicPlus.update(mouseX, mouseY);
        buttonMusicMinus.update(mouseX, mouseY);

        buttonSoundPlus.update(mouseX, mouseY);
        buttonSoundMinus.update(mouseX, mouseY);
        buttonBack.update(mouseX, mouseY);


        game.batch.setProjectionMatrix(game.uiCamera.combined);
        game.batch.begin();

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        // ===== BACKGROUND =====
        game.batch.draw(
                GameResources.menu_bg,
                0,
                0,
                w,
                h
        );

        // ===== FULLSCREEN BUTTON TEXTURE =====
        if (Gdx.graphics.isFullscreen()) {
            fullscreenButton.setTexture(GameResources.fullscreenOn);
        } else {
            fullscreenButton.setTexture(GameResources.fullscreenOff);
        }

        // ===== VOLUME PANEL (MUSIC) =====
        game.batch.draw(
                GameResources.musicPanel,
                panelX,
                panelY,
                panelWidth,
                panelHeight
        );

        // ===== VOLUME TEXT (MUSIC) =====
        String volumeText = "" + (int)(Options.musicVolume * 100);
        volumeLayout.setText(font, volumeText);

        float textOffsetX = panelWidth * 0.19f;
        float textOffsetY = panelHeight * 0.05f;

        font.draw(
                game.batch,
                volumeLayout,
                panelX + (panelWidth - volumeLayout.width) / 2f + textOffsetX,
                panelY + panelHeight / 2f + volumeLayout.height / 2f + textOffsetY
        );

        // ===== VOLUME PANEL (SOUNDS) =====
        game.batch.draw(
                GameResources.sfxPanel,
                panelX,
                panelY - 180f,
                panelWidth,
                panelHeight
        );

        // ===== VOLUME TEXT (SOUNDS) =====
        String soundText = "" + (int)(Options.soundVolume * 100);
        soundLayout.setText(font, soundText);

        font.draw(
                game.batch,
                soundLayout,
                panelX + (panelWidth - soundLayout.width) / 2f + textOffsetX,
                (panelY - 200f) + panelHeight / 2f + soundLayout.height / 2f + textOffsetY
        );

        // ===== BUTTONS =====
        fullscreenButton.draw(game.batch);

        buttonMusicMinus.draw(game.batch);
        buttonMusicPlus.draw(game.batch);

        buttonSoundMinus.draw(game.batch);
        buttonSoundPlus.draw(game.batch);

        clearRecordsButton.draw(game.batch);

        buttonBack.draw(game.batch);

        game.batch.end();
    }

    private void updateLayout() {
        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();
        float centerX = w / 2f;

        // FULLSCREEN BUTTON
        float fullscreenWidth = w * 0.45f;
        float fullscreenHeight = h * 0.4f;

        fullscreenButton.setPosition(
                centerX - fullscreenWidth / 2f,
                h * 0.52f,
                fullscreenWidth,
                fullscreenHeight
        );

        float clearRecordsWidth = w * 0.33f;
        float clearRecordsHeight = h * 0.31f;

        clearRecordsButton.setPosition(
                centerX - clearRecordsWidth / 2f,
                h * 0.4f,
                clearRecordsWidth,
                clearRecordsHeight
        );

        // VOLUME PANEL
        panelWidth = w * 0.42f;
        panelHeight = h * 0.4f;
        panelX = centerX - panelWidth / 2f;
        panelY = h * 0.2f;
        buttonY = h * 0.365f;

        // Font
        font.getData().setScale(h * 0.0038f);

        // PLUS / MINUS BUTTONS
        float buttonSize = h * 0.1f;
        float spacing = w * 0.025f;

        // MINUS (MUSIC)
        buttonMusicMinus.setPosition(
                panelX - buttonSize - spacing,
                buttonY,
                buttonSize,
                buttonSize
        );

        // PLUS (MUSIC)
        buttonMusicPlus.setPosition(
                panelX + panelWidth + spacing,
                buttonY,
                buttonSize,
                buttonSize
        );

        // MINUS (SOUNDS)
        buttonSoundMinus.setPosition(
                panelX - buttonSize - spacing,
                buttonY - 200f,
                buttonSize,
                buttonSize
        );

        // PLUS (SOUNDS)
        buttonSoundPlus.setPosition(
                panelX + panelWidth + spacing,
                buttonY - 200f,
                buttonSize,
                buttonSize
        );


        // BACK BUTTON
        float backWidth = w * 0.4f;
        float backHeight = h * 0.4f;

        float targetY = (h * 0.001f) - 130f;

        buttonBack.setPosition(
                centerX - backWidth / 2f,
                targetY,
                backWidth,
                backHeight
        );
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        game.uiViewport.unproject(touchPos);
        float x = touchPos.x;
        float y = touchPos.y;


        stopAutoRepeat();


        if (fullscreenButton.isTapped(x, y)) {
            applyFullscreenToggle();
            return true;
        }

        // ===== CLEAR RECORDS =====
        if (clearRecordsButton.isTapped(x, y)) {
            // Пока что ничего не происходит
            return true;
        }

        // ===== MUSIC PLUS =====
        if (buttonMusicPlus.isTapped(x, y)) {
            activePointerId = pointer;
            Options.musicVolume = Math.min(1f, Options.musicVolume + 0.01f);


            autoRepeatTask = Timer.schedule(new Task() {
                @Override
                public void run() {
                    Options.musicVolume = Math.min(1f, Options.musicVolume + 0.01f);
                }
            }, 0.4f, 0.03f);
            return true;
        }

        // ===== MUSIC MINUS =====
        if (buttonMusicMinus.isTapped(x, y)) {
            activePointerId = pointer;
            Options.musicVolume = Math.max(0f, Options.musicVolume - 0.01f);

            autoRepeatTask = Timer.schedule(new Task() {
                @Override
                public void run() {
                    Options.musicVolume = Math.max(0f, Options.musicVolume - 0.01f);
                }
            }, 0.4f, 0.03f);
            return true;
        }

        // ===== SOUND PLUS =====
        if (buttonSoundPlus.isTapped(x, y)) {
            activePointerId = pointer;
            Options.soundVolume = Math.min(1f, Options.soundVolume + 0.01f);
            updateSoundsVolume();

            autoRepeatTask = Timer.schedule(new Task() {
                @Override
                public void run() {
                    Options.soundVolume = Math.min(1f, Options.soundVolume + 0.01f);
                    updateSoundsVolume();
                }
            }, 0.4f, 0.03f);
            return true;
        }

        // ===== SOUND MINUS =====
        if (buttonSoundMinus.isTapped(x, y)) {
            activePointerId = pointer;
            Options.soundVolume = Math.max(0f, Options.soundVolume - 0.01f);
            updateSoundsVolume();

            autoRepeatTask = Timer.schedule(new Task() {
                @Override
                public void run() {
                    Options.soundVolume = Math.max(0f, Options.soundVolume - 0.01f);
                    updateSoundsVolume();
                }
            }, 0.4f, 0.03f);
            return true;
        }

        // ===== BACK =====
        if (buttonBack.isTapped(x, y)) {
            game.setScreen(game.menuScreen);
            return true;
        }

        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == activePointerId) {
            stopAutoRepeat();
        }
        return false;
    }


    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        if (pointer == activePointerId) {
            stopAutoRepeat();
        }
        return false;
    }

    private void updateSoundsVolume() {
        if (game.audioManager != null) {
            game.audioManager.gasMusic.setVolume(Options.soundVolume);
            game.audioManager.brakeMusic.setVolume(Options.soundVolume);
        }
    }

    private void stopAutoRepeat() {
        if (autoRepeatTask != null) {
            autoRepeatTask.cancel();
            autoRepeatTask = null;
        }
        activePointerId = -1;
    }

    @Override
    public void hide() {
        stopAutoRepeat();
    }

    @Override
    public void resize(int width, int height) {
        game.uiViewport.update(width, height, true);
        updateLayout();
    }

    private void applyFullscreenToggle() {
        Options.fullscreen = !Options.fullscreen;

        if (Options.fullscreen) {
            oldWindowWidth = Gdx.graphics.getWidth();
            oldWindowHeight = Gdx.graphics.getHeight();
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(oldWindowWidth, oldWindowHeight);
        }

        game.gameScreen.resize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        updateLayout();
    }

    @Override
    public void pause() {
        stopAutoRepeat();
    }

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stopAutoRepeat();
        font.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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