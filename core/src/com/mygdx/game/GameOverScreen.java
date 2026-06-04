package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class GameOverScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private final String survivalTime;

    private BitmapFont font;

    private Button restartButton;
    private Button menuButton;

    private Vector3 touchPos;

    public GameOverScreen(MyGdxGame game, String survivalTime) {

        this.game = game;
        this.survivalTime = survivalTime;

        touchPos = new Vector3();
    }

    @Override
    public void show() {

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            return;
        }

        touchPos.set(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0
        );

        game.gameViewport.unproject(touchPos);

        float worldWidth = game.gameViewport.getWorldWidth();
        float worldHeight = game.gameViewport.getWorldHeight();

        float buttonWidth = 280f;
        float buttonHeight = 150f;
        float centerX = worldWidth / 2f - buttonWidth / 2f;

        float restartButtonY = worldHeight * 0.30f;
        float menuButtonY = worldHeight * 0.12f;

        restartButton.setPosition(
                centerX,
                restartButtonY,
                buttonWidth,
                buttonHeight
        );

        menuButton.setPosition(
                centerX,
                menuButtonY,
                buttonWidth,
                buttonHeight
        );

        restartButton.update(touchPos.x, touchPos.y);
        menuButton.update(touchPos.x, touchPos.y);

        if (Gdx.input.justTouched()) {

            if (restartButton.isTapped(
                    touchPos.x,
                    touchPos.y
            )) {

                game.gameScreen.startNewGame();
                game.setScreen(game.gameScreen);
                return;
            }

            if (menuButton.isTapped(
                    touchPos.x,
                    touchPos.y
            )) {

                game.gameScreen.startNewGame();
                game.setScreen(game.menuScreen);
                return;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.gameViewport.apply();
        game.batch.setProjectionMatrix(
                game.gameCamera.combined
        );

        game.batch.begin();

        GlyphLayout loseText = new GlyphLayout(font, "You lost");
        float textCenterX = worldWidth / 2f - loseText.width / 2f;
        float textY = restartButtonY + buttonHeight + 40;

        font.draw(
                game.batch,
                loseText,
                textCenterX,
                textY
        );

        restartButton.draw(game.batch);
        menuButton.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}