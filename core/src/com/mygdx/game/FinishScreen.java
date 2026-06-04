package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class FinishScreen extends ScreenAdapter implements InputProcessor {

    private final MyGdxGame game;
    private final String time;

    private BitmapFont font;

    private Button restartButton;
    private Button menuButton;

    public FinishScreen(
            MyGdxGame game,
            String time
    ) {
        this.game = game;
        this.time = time;
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this);
        font = new BitmapFont();
        font.getData().setScale(3f);

        restartButton = new Button(
                GameResources.restartButton,
                0, 0, 0, 0
        );

        menuButton = new Button(
                GameResources.menuPauseButton,
                0, 0, 0, 0
        );
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.gameViewport.apply();
        game.batch.setProjectionMatrix(
                game.gameCamera.combined
        );

        Vector3 mousePos = new Vector3(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        game.gameViewport.unproject(mousePos);

        float w = game.gameViewport.getWorldWidth();
        float h = game.gameViewport.getWorldHeight();

        float buttonWidth = 250f;
        float buttonHeight = 150f;
        float centerX = w / 2f - buttonWidth / 2f;

        restartButton.setPosition(
                centerX,
                h * 0.35f,
                buttonWidth,
                buttonHeight
        );

        menuButton.setPosition(
                centerX,
                h * 0.15f,
                buttonWidth,
                buttonHeight
        );

        restartButton.update(mousePos.x, mousePos.y);
        menuButton.update(mousePos.x, mousePos.y);

        game.batch.begin();

        font.draw(game.batch,
                "FINISH!",
                w * 0.42f,
                h * 0.9f);

        font.draw(game.batch,
                "Time: " + time,
                w * 0.33f,
                h * 0.8f);

        restartButton.draw(game.batch);
        menuButton.draw(game.batch);

        game.batch.end();
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 touch = new Vector3(screenX, screenY, 0);
        game.gameViewport.unproject(touch);

        if (restartButton.isTapped(touch.x, touch.y)) {

            game.gameScreen.startNewGame();
            game.setScreen(game.gameScreen);

            return true;
        }

        if (menuButton.isTapped(touch.x, touch.y)) {

            game.setScreen(game.menuScreen);

            return true;
        }

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