package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen implements Screen, InputProcessor {

    private MyGdxGame game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font;


    private Button buttonStart;
    private Button buttonOptions;
    private Button buttonQuit;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("menu_bg.png");
        float buttonWidth = 400;
        float buttonHeight = 250;

        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2f;

        buttonStart = new Button(
                new Texture("NEW_GAME.png"),
                centerX,
                225,
                buttonWidth,
                buttonHeight
        );

        buttonOptions = new Button(
                new Texture("OPTIONS.png"),
                centerX,
                75,
                buttonWidth,
                buttonHeight
        );

        buttonQuit = new Button(
                new Texture("QUIT_GAME.png"),
                centerX,
                -75,
                buttonWidth,
                buttonHeight
        );
        font = new BitmapFont();

        resize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        buttonStart.update(mouseX, mouseY);
        buttonOptions.update(mouseX, mouseY);
        buttonQuit.update(mouseX, mouseY);

        batch.begin();

        batch.draw(background, 0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        buttonStart.draw(batch);
        buttonOptions.draw(batch);
        buttonQuit.draw(batch);

        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float y = Gdx.graphics.getHeight() - screenY;

        if (buttonStart.isTapped(screenX, y)) {
            System.out.println("NEW GAME");
            game.setScreen(new WhiteScreen(game));
            return true;
        }

        if (buttonOptions.isTapped(screenX, y)) {
            System.out.println("OPTIONS");
            return true;
        }

        if (buttonQuit.isTapped(screenX, y)) {
            System.out.println("QUIT");
            Gdx.app.exit();
            return true;
        }

        return false;
    }

    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        buttonStart.dispose();
        buttonOptions.dispose();
        buttonQuit.dispose();
        font.dispose();
    }
}