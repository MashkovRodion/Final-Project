package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class PauseScreen implements Screen, InputProcessor {

    private final MyGdxGame game;

    private final Vector3 touchPos = new Vector3();

    private final Button continueButton;
    private final Button restartButton;
    private final Button menuButton;

    private final BitmapFont font;

    private final GlyphLayout layout = new GlyphLayout();


    public PauseScreen(MyGdxGame game) {
        this.game = game;

        font = new BitmapFont();

        continueButton = new Button(GameResources.continueButton, 0, 0, 0, 0);
        restartButton = new Button(GameResources.restartButton, 0, 0, 0, 0);
        menuButton = new Button(GameResources.menuPauseButton, 0, 0, 0, 0);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this);

        updateLayout();
    }

    @Override
    public void render(float delta) {

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.uiViewport.apply();

        game.batch.setProjectionMatrix(
                game.uiCamera.combined
        );

        Vector3 mousePos = new Vector3(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        game.uiViewport.unproject(mousePos);

        continueButton.update(
                mousePos.x,
                mousePos.y
        );

        restartButton.update(
                mousePos.x,
                mousePos.y
        );

        menuButton.update(
                mousePos.x,
                mousePos.y
        );

        layout.setText(font, "GAME PAUSED");
        game.batch.begin();

        font.draw(
                game.batch,
                layout,
                (w - layout.width) / 2f,
                h * 0.9f
        );

        continueButton.draw(game.batch);

        restartButton.draw(game.batch);

        menuButton.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

        game.uiViewport.update(
                width,
                height,
                true
        );

        updateLayout();
    }

    private void updateLayout() {

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        font.getData().setScale(h * 0.004f);

        float bw = w * 0.45f;
        float bh = h * 0.4f;

        float cx = w / 2f - bw / 2f;

        continueButton.setPosition(
                cx,
                h * 0.51f,
                bw,
                bh
        );

        restartButton.setPosition(
                cx,
                h * 0.26f,
                bw,
                bh
        );

        menuButton.setPosition(
                cx,
                h * 0.01f,
                bw,
                bh
        );
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);

        game.uiViewport.unproject(touchPos);

        if (continueButton.isTapped(touchPos.x, touchPos.y)) {
            game.setScreen(game.gameScreen);

            return true;
        }

        if (restartButton.isTapped(touchPos.x, touchPos.y)) {

            game.gameScreen.startNewGame();
            game.setScreen(game.gameScreen);

            return true;
        }

        if (menuButton.isTapped(touchPos.x, touchPos.y)) {
            game.setScreen(game.menuScreen);

            return true;
        }

        return false;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

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

    @Override
    public boolean touchCancelled(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {
        return false;
    }
}