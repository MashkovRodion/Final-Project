package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class OptionScreen implements Screen, InputProcessor {

    private MyGdxGame game;
    private BitmapFont font;

    private Button fullscreenButton;
    private Button buttonMusicPlus;
    private Button buttonMusicMinus;
    private Button buttonBack;

    private int oldWindowWidth = 1280;
    private int oldWindowHeight = 720;

    // ===== PANEL DATA =====

    private float panelX;
    private float buttonY;
    private float panelY;
    private float panelWidth;
    private float panelHeight;

    private final GlyphLayout volumeLayout = new GlyphLayout();

    public OptionScreen(MyGdxGame game) {

        this.game = game;
        font = new BitmapFont();

        fullscreenButton = new Button(GameResources.fullscreenOff, 0,0,0,0);

        buttonMusicPlus = new Button(GameResources.plusButton,0,0,0,0);

        buttonMusicMinus = new Button(GameResources.minusButton,0,0,0,0);

        buttonBack = new Button(GameResources.backButton,0,0,0,0);
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

        buttonMusicPlus.update(mouseX, mouseY);

        buttonMusicMinus.update(mouseX, mouseY);

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

        // ===== VOLUME PANEL =====

        game.batch.draw(
                GameResources.musicPanel,
                panelX,
                panelY,
                panelWidth,
                panelHeight
        );

        // ===== VOLUME TEXT =====

        String volumeText = "" + (int)(Options.musicVolume * 100);

        volumeLayout.setText(font, volumeText);

        float textOffsetX = panelWidth * 0.19f;
        float textOffsetY = panelHeight * 0.05f;

        font.draw(
                game.batch,
                volumeLayout,
                panelX + (panelWidth - volumeLayout.width) / 2f + textOffsetX,
                panelY + panelHeight / 2f + volumeLayout.height / 2f + textOffsetY);

        // ===== BUTTONS =====

        fullscreenButton.draw(game.batch);

        buttonMusicMinus.draw(game.batch);

        buttonMusicPlus.draw(game.batch);

        buttonBack.draw(game.batch);

        game.batch.end();
    }

    private void updateLayout() {

        float w = game.uiViewport.getWorldWidth();

        float h = game.uiViewport.getWorldHeight();

        float centerX = w / 2f;

        // =====================================
        // FULLSCREEN BUTTON
        // =====================================

        float fullscreenWidth =
                w * 0.45f;

        float fullscreenHeight =
                h * 0.4f;

        fullscreenButton.setPosition(
                centerX - fullscreenWidth / 2f,
                h * 0.45f,
                fullscreenWidth,
                fullscreenHeight
        );

        // =====================================
        // VOLUME PANEL
        // =====================================

        panelWidth = w * 0.42f;

        panelHeight = h * 0.4f;

        panelX = centerX - panelWidth / 2f;

        panelY = h * 0.245f;
        buttonY = h * 0.41f;


        // Font
        font.getData().setScale(h * 0.0038f);

        // =====================================
        // PLUS / MINUS BUTTONS
        // =====================================

        // одинаковая высота с панелью
        float buttonSize = h * 0.1f;


        // расстояние от панели
        float spacing = w * 0.025f;

        // MINUS

        buttonMusicMinus.setPosition(
                panelX - buttonSize - spacing,
                buttonY,
                buttonSize,
                buttonSize
        );

        // PLUS

        buttonMusicPlus.setPosition(
                panelX + panelWidth + spacing,
                buttonY,
                buttonSize,
                buttonSize
        );

        // =====================================
        // BACK BUTTON
        // =====================================

        float backWidth = w * 0.4f;

        float backHeight = h * 0.4f;

        buttonBack.setPosition(
                centerX - backWidth / 2f,
                h * 0.001f,
                backWidth,
                backHeight
        );
    }

    @Override
    public boolean touchDown(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {

        Vector3 touchPos = new Vector3(
                screenX,
                screenY,
                0
        );

        game.uiViewport.unproject(touchPos);

        float x = touchPos.x;

        float y = touchPos.y;

        // ===== FULLSCREEN =====

        if (fullscreenButton.isTapped(x, y)) {

            applyFullscreenToggle();
            return true;
        }

        // ===== MUSIC PLUS =====

        if (buttonMusicPlus.isTapped(x, y)) {

            Options.musicVolume += 0.01f;

            if (Options.musicVolume > 1f) {

                Options.musicVolume = 1f;
            }

            return true;
        }

        // ===== MUSIC MINUS =====

        if (buttonMusicMinus.isTapped(x, y)) {

            Options.musicVolume -= 0.01f;

            if (Options.musicVolume < 0f) {

                Options.musicVolume = 0f;
            }

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

        game.uiViewport.update(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                true
        );

        updateLayout();


    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {

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
    public boolean touchUp(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {
        return false;
    }

    @Override
    public boolean touchCancelled(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {
        return false;
    }

    @Override
    public boolean touchDragged(
            int screenX,
            int screenY,
            int pointer
    ) {
        return false;
    }

    @Override
    public boolean mouseMoved(
            int screenX,
            int screenY
    ) {
        return false;
    }

    @Override
    public boolean scrolled(
            float amountX,
            float amountY
    ) {
        return false;
    }
}