package com.mygdx.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.ui.Button;

import com.mygdx.game.core.GameResources;
import com.mygdx.game.core.MyGdxGame;

public class MenuScreen implements Screen, InputProcessor {

    private final MyGdxGame game;

    private Texture background;

    private Button buttonStart;
    private Button buttonOptions;
    private Button buttonQuit;
    private Button buttonSkins;
    private Button buttonLeaderboard;

    private float aspect;
    private float height;
    private float width;
    float cx;
    float by;


    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        background = GameResources.menu_bg;

        buttonStart = new Button(GameResources.startButton,0,0,0,0);
        buttonOptions = new Button(GameResources.optionsButton,0,0,0,0);
        buttonQuit = new Button(GameResources.quitButton,0,0,0,0);
        buttonSkins = new Button(GameResources.skinsButton,0,0,0,0);
        buttonLeaderboard = new Button(GameResources.leaderboardButton,0,0,0,0);

        if (game.audioManager != null && game.audioManager.backgroundMusic != null && !game.audioManager.backgroundMusic.isPlaying()) {
            game.audioManager.backgroundMusic.play();
        }

        updateButtons();

        Gdx.input.setInputProcessor(this);
    }

    private void updateButtons() {

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();
        float spacing = w * 0.05f;
        float widthOptions;

        aspect = (float) GameResources.startButton.getHeight() / GameResources.startButton.getWidth();
        width = w * 0.45f;
        height = width * aspect;
        cx = w / 2f - width / 2f;
        by = h * 0.6f;

        buttonStart.setPosition(cx, by, width, height);

        aspect = (float) GameResources.optionsButton.getHeight() / GameResources.optionsButton.getWidth();
        width = w * 0.45f;
        height = width * aspect;
        cx = w / 2f - width / 2f;
        by = h * 0.35f;
        widthOptions = width;

        buttonOptions.setPosition(cx, by, width, height);

        aspect = (float) GameResources.leaderboardButton.getHeight() / GameResources.leaderboardButton.getWidth();
        width = w * 0.09f;
        height = width * aspect;

        buttonLeaderboard.setPosition(cx - spacing - width, by * 1.02f, width, height);

        aspect = (float) GameResources.skinsButton.getHeight() / GameResources.skinsButton.getWidth();
        width = w * 0.09f;
        height = width * aspect;

        buttonSkins.setPosition(cx + widthOptions + spacing, by * 1.02f, width, height);

        aspect = (float) GameResources.quitButton.getHeight() / GameResources.quitButton.getWidth();
        width = w * 0.45f;
        height = width * aspect;
        cx = w / 2f - width / 2f;
        by = h * 0.1f;

        buttonQuit.setPosition(cx, by, width, height);


    }

    @Override
    public void render(float delta) {

        game.uiViewport.apply();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.uiViewport.unproject(m);

        buttonStart.update(m.x, m.y);
        buttonOptions.update(m.x, m.y);
        buttonQuit.update(m.x, m.y);
        buttonSkins.update(m.x, m.y);
        buttonLeaderboard.update(m.x, m.y);

        game.batch.setProjectionMatrix(game.uiCamera.combined);

        game.batch.begin();

        game.batch.draw(background,
                0,0,
                game.uiViewport.getWorldWidth(),
                game.uiViewport.getWorldHeight());

        buttonStart.draw(game.batch);
        buttonOptions.draw(game.batch);
        buttonQuit.draw(game.batch);
        buttonSkins.draw(game.batch);
        buttonLeaderboard.draw(game.batch);

        game.batch.end();
    }

    @Override
    public boolean touchDown(int x,int y,int p,int b) {

        Vector3 t = new Vector3(x,y,0);
        game.uiViewport.unproject(t);

        if (buttonStart.isTapped(t.x,t.y)) {
            game.gameScreen.startNewGame();
            game.setScreen(game.gameScreen);
            return true;
        }

        if (buttonOptions.isTapped(t.x,t.y)) {
            game.setScreen(game.optionScreen);
            return true;
        }

        if (buttonQuit.isTapped(t.x,t.y)) {
            Gdx.app.exit();
            return true;
        }

        if (buttonSkins.isTapped(t.x,t.y)) {
            game.setScreen(game.skinSelectionScreen);
            return true;
        }
        if (buttonLeaderboard.isTapped(t.x,t.y)) {
            game.setScreen(game.leaderboardScreen);
            return true;
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {

        game.uiViewport.update(width, height, true);

        updateButtons();
    }

    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void hide(){}

    @Override
    public void dispose() {

    }

    @Override public boolean keyDown(int k){return false;}
    @Override public boolean keyUp(int k){return false;}
    @Override public boolean keyTyped(char c){return false;}
    @Override public boolean touchUp(int x,int y,int p,int b){return false;}
    @Override public boolean touchDragged(int x,int y,int p){return false;}
    @Override public boolean mouseMoved(int x,int y){return false;}
    @Override public boolean scrolled(float a,float b){return false;}
    @Override public boolean touchCancelled(int x,int y,int p,int b){return false;}
}