package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ui.Button;
import com.mygdx.game.managers.LeaderboardManager;

import com.mygdx.game.core.GameResources;
import com.mygdx.game.core.MyGdxGame;

public class LeaderboardScreen implements Screen, InputProcessor {

    private final MyGdxGame game;

    private BitmapFont font;

    private Button backButton;

    public LeaderboardScreen(MyGdxGame game) {

        this.game = game;

        font = new BitmapFont();

        backButton =
                new Button(
                        GameResources.backButton,
                        0,0,0,0
                );
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this);

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        backButton.setPosition(
                w / 2f - 250,
                10,
                500,
                325
        );
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
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

        backButton.update(
                mousePos.x,
                mousePos.y
        );

        game.batch.begin();

        game.batch.draw(
                GameResources.menu_bg,
                0,
                0,
                game.uiViewport.getWorldWidth(),
                game.uiViewport.getWorldHeight()
        );

        font.getData().setScale(3);

        long[] times = LeaderboardManager.getTimes();

        float centerX = game.uiViewport.getWorldWidth() / 2f;

        float tableX = centerX - 200;
        float y = 825;

        font.draw(game.batch, "PLACE", tableX, y);
        font.draw(game.batch, "TIME", tableX + 220, y);

        y -= 50;

        for (int i = 0; i < times.length; i++) {

            if (times[i] == 0) {
                continue;
            }

            font.draw(
                    game.batch,
                    "#" + (i + 1),
                    tableX,
                    y
            );

            font.draw(
                    game.batch,
                    formatTime(times[i]),
                    tableX + 220,
                    y
            );

            y -= 45;
        }

        backButton.draw(game.batch);

        game.batch.end();
    }

    private String formatTime(long millis) {

        long mins = millis / 60000;
        long secs = (millis % 60000) / 1000;
        long ms = millis % 1000;

        return String.format(
                "%02d:%02d.%03d",
                mins,
                secs,
                ms
        );
    }

    @Override
    public boolean touchDown(
            int screenX,
            int screenY,
            int pointer,
            int button
    ) {

        Vector3 pos =
                new Vector3(screenX, screenY, 0);

        game.uiViewport.unproject(pos);

        if (backButton.isTapped(pos.x, pos.y)) {

            game.setScreen(game.menuScreen);

            return true;
        }

        return false;
    }

    @Override public void resize(int width,int height){}
    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}
    @Override public void dispose(){font.dispose();}
    @Override public boolean keyDown(int keycode){return false;}
    @Override public boolean keyUp(int keycode){return false;}
    @Override public boolean keyTyped(char character){return false;}
    @Override public boolean touchUp(int x,int y,int p,int b){return false;}
    @Override public boolean touchCancelled(int x,int y,int p,int b){return false;}
    @Override public boolean touchDragged(int x,int y,int p){return false;}
    @Override public boolean mouseMoved(int x,int y){return false;}
    @Override public boolean scrolled(float x,float y){return false;}
}