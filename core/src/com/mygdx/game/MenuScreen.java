package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen implements Screen, InputProcessor {

    private final MyGdxGame game;

    private Texture background;

    private Button buttonStart;
    private Button buttonOptions;
    private Button buttonQuit;
    private Button buttonSkins;

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

        updateButtons();

        Gdx.input.setInputProcessor(this);
    }

    private void updateButtons() {

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        // Большие кнопки (START, OPTIONS, QUIT)
        float bw = w * 0.45f;
        float bh = h * 0.4f;
        float cx = w / 2f - bw / 2f;

        buttonStart.setPosition(cx, h * 0.48f, bw, bh);
        buttonOptions.setPosition(cx, h * 0.25f, bw, bh);
        buttonQuit.setPosition(cx, h * 0.01f, bw, bh);

        // Кнопка скинов - больше по высоте и левее
        float skinsHeight = h * 0.12f;      // Увеличено с 0.08 до 0.12
        float skinsWidth = h * 0.2f;        // Увеличено с 0.15 до 0.2
        float margin = h * 0.015f;          // Уменьшен отступ, чтобы была левее

        // Располагаем кнопку скинов справа от OPTIONS, но с меньшим отступом
        float optionsRight = cx + bw;
        float skinsX = optionsRight + margin;
        float skinsY = h * 0.27f + (bh - skinsHeight) / 2f;

        buttonSkins.setPosition(skinsX, skinsY, skinsWidth, skinsHeight);
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