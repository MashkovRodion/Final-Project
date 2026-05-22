package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OptionScreen implements Screen, InputProcessor {

    private MyGdxGame game;

    private SpriteBatch batch;
    private BitmapFont font;

    private Texture background;
    private Texture volumePanel;

    private Button fullscreenButton;
    private Button buttonMusicPlus;
    private Button buttonMusicMinus;
    private Button buttonBack;

    private Texture fullscreenOn;
    private Texture fullscreenOff;

    private OrthographicCamera camera;
    private Viewport viewport;

    private int oldWindowWidth = 1280;
    private int oldWindowHeight = 720;

    // ===== PANEL DATA =====

    private float panelX;
    private float buttonY;
    private float panelY;
    private float panelWidth;
    private float panelHeight;

    public OptionScreen(MyGdxGame game) {

        this.game = game;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        font = new BitmapFont();

        background = new Texture("menu_bg.png");

        volumePanel = new Texture("music.png");

        fullscreenOn =
                new Texture("FULLSCREEN_ON.png");

        fullscreenOff =
                new Texture("FULLSCREEN_OFF.png");

        camera = new OrthographicCamera();

        viewport = new ScreenViewport(camera);

        viewport.apply();

        fullscreenButton =
                new Button(fullscreenOff, 0,0,0,0);

        buttonMusicPlus =
                new Button(new Texture("PLUS.png"),0,0,0,0);

        buttonMusicMinus =
                new Button(new Texture("MINUS.png"),0,0,0,0);

        buttonBack =
                new Button(new Texture("BACK.png"),0,0,0,0);

        updateLayout();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 mousePos = new Vector3(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        camera.unproject(mousePos);

        float mouseX = mousePos.x;
        float mouseY = mousePos.y;

        fullscreenButton.update(mouseX, mouseY);

        buttonMusicPlus.update(mouseX, mouseY);

        buttonMusicMinus.update(mouseX, mouseY);

        buttonBack.update(mouseX, mouseY);

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        float w = Gdx.graphics.getWidth();

        float h = Gdx.graphics.getHeight();

        // ===== FONT SCALE =====

        font.getData().setScale(
                h * 0.0038f
        );

        // ===== BACKGROUND =====

        batch.draw(
                background,
                0,
                0,
                w,
                h
        );

        // ===== FULLSCREEN BUTTON TEXTURE =====

        if (Gdx.graphics.isFullscreen()) {

            fullscreenButton.setTexture(fullscreenOn);

        } else {

            fullscreenButton.setTexture(fullscreenOff);
        }

        // ===== VOLUME PANEL =====

        batch.draw(
                volumePanel,
                panelX,
                panelY,
                panelWidth,
                panelHeight
        );

        // ===== VOLUME TEXT =====

        font.draw(
                batch,
                "" + (int)(Options.musicVolume * 100),
                panelX + panelWidth * 0.615f,
                panelY + panelHeight * 0.6f
        );

        // ===== BUTTONS =====

        fullscreenButton.draw(batch);

        buttonMusicMinus.draw(batch);

        buttonMusicPlus.draw(batch);

        buttonBack.draw(batch);

        batch.end();
    }

    private void updateLayout() {

        float w = Gdx.graphics.getWidth();

        float h = Gdx.graphics.getHeight();

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

        camera.unproject(touchPos);

        float x = touchPos.x;

        float y = touchPos.y;

        // ===== FULLSCREEN =====

        if (fullscreenButton.isTapped(x, y)) {

            Options.fullscreen =
                    !Options.fullscreen;

            if (Options.fullscreen) {

                oldWindowWidth =
                        Gdx.graphics.getWidth();

                oldWindowHeight =
                        Gdx.graphics.getHeight();

                Gdx.graphics.setFullscreenMode(
                        Gdx.graphics.getDisplayMode()
                );

            } else {

                Gdx.graphics.setWindowedMode(
                        oldWindowWidth,
                        oldWindowHeight
                );
            }

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

            game.setScreen(
                    new MenuScreen(game)
            );

            return true;
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);

        updateLayout();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {

        batch.dispose();

        font.dispose();

        background.dispose();

        volumePanel.dispose();

        fullscreenOn.dispose();

        fullscreenOff.dispose();

        fullscreenButton.dispose();

        buttonMusicPlus.dispose();

        buttonMusicMinus.dispose();

        buttonBack.dispose();
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