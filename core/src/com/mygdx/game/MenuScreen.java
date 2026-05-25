package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen, InputProcessor {

    private MyGdxGame game;

    private SpriteBatch batch;

    private Texture background;

    private Button buttonStart;
    private Button buttonOptions;
    private Button buttonQuit;

    private OrthographicCamera camera;
    private Viewport viewport;

    public MenuScreen(MyGdxGame game) {

        this.game = game;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        background = new Texture("menu_bg.png");

        // ===== CAMERA =====

        camera = new OrthographicCamera();

        viewport = new ScreenViewport(camera);

        viewport.apply();

        // ===== BUTTONS =====

        buttonStart = new Button(
                new Texture("NEW_GAME.png"),
                0,0,0,0
        );

        buttonOptions = new Button(
                new Texture("OPTIONS.png"),
                0,0,0,0
        );

        buttonQuit = new Button(
                new Texture("QUIT_GAME.png"),
                0,0,0,0
        );

        updateButtonPositions();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ===== MOUSE POSITION =====

        Vector3 mousePos = new Vector3(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        camera.unproject(mousePos);

        float mouseX = mousePos.x;
        float mouseY = mousePos.y;

        // ===== UPDATE =====

        buttonStart.update(mouseX, mouseY);

        buttonOptions.update(mouseX, mouseY);

        buttonQuit.update(mouseX, mouseY);

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // ===== BACKGROUND =====

        batch.draw(
                background,
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        // ===== BUTTONS =====

        buttonStart.draw(batch);

        buttonOptions.draw(batch);

        buttonQuit.draw(batch);

        batch.end();
    }

    private void updateButtonPositions() {

        float screenWidth =
                Gdx.graphics.getWidth();

        float screenHeight =
                Gdx.graphics.getHeight();

        float buttonWidth = screenWidth * 0.45f;

        float buttonHeight = screenHeight * 0.4f;

        float centerX =
                screenWidth / 2f - buttonWidth / 2f;

        // ===== START =====

        buttonStart.setPosition(
                centerX,
                screenHeight * 0.539f,
                buttonWidth,
                buttonHeight
        );

        // ===== OPTIONS =====

        buttonOptions.setPosition(
                centerX,
                screenHeight * 0.27f,
                buttonWidth,
                buttonHeight
        );

        // ===== QUIT =====

        buttonQuit.setPosition(
                centerX,
                screenHeight * 0.001f,
                buttonWidth,
                buttonHeight
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

        // ===== NEW GAME =====

        if (buttonStart.isTapped(x, y)) {

            game.setScreen(
                    new WhiteScreen(game)
            );

        if (buttonStart.isTapped(screenX, y)) {
            System.out.println("NEW GAME");
            game.setScreen(game.gameScreen);
            //game.setScreen(new WhiteScreen(game));
            return true;
        }

        // ===== OPTIONS =====

        if (buttonOptions.isTapped(x, y)) {

            game.setScreen(
                    new OptionScreen(game)
            );

            return true;
        }

        // ===== QUIT =====

        if (buttonQuit.isTapped(x, y)) {

            Gdx.app.exit();

            return true;
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);

        updateButtonPositions();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {

        batch.dispose();

        background.dispose();

        buttonStart.dispose();

        buttonOptions.dispose();

        buttonQuit.dispose();
    }

    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchCancelled(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {
        return false;
    }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override public boolean scrolled(float amountX, float amountY) { return false; }

    @Override public boolean keyDown(int keycode) { return false; }

    @Override public boolean keyUp(int keycode) { return false; }

    @Override public boolean keyTyped(char character) { return false; }
}