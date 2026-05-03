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

    private Rectangle newGameArea;
    private Rectangle optionsArea;
    private Rectangle quitArea;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("menu_bg.png");
        font = new BitmapFont();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        newGameArea = new Rectangle(
                screenWidth * 0.50f,   // x: 35% от ширины экрана
                screenHeight * 0.6f,   // y: 60% от высоты
                screenWidth * 0.70f,    // ширина: 30% экрана
                screenHeight * 0.08f   // высота: 8% экрана
        );

        optionsArea = new Rectangle(
                screenWidth * 0.35f,
                screenHeight * 0.45f,
                screenWidth * 0.3f,
                screenHeight * 0.08f
        );

        quitArea = new Rectangle(
                screenWidth * 0.35f,
                screenHeight * 0.3f,
                screenWidth * 0.3f,
                screenHeight * 0.08f
        );

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float y = Gdx.graphics.getHeight() - screenY;

        if (newGameArea.contains(screenX, y)) {
            System.out.println("NEW GAME pressed");
            game.setScreen(new WhiteScreen(game));
            return true;
        }

        if (optionsArea.contains(screenX, y)) {
            System.out.println("OPTIONS pressed");
            return true;
        }

        if (quitArea.contains(screenX, y)) {
            System.out.println("QUIT pressed");
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
        newGameArea.set(width * 0.35f, height * 0.6f, width * 0.3f, height * 0.08f);
        optionsArea.set(width * 0.35f, height * 0.45f, width * 0.3f, height * 0.08f);
        quitArea.set(width * 0.35f, height * 0.3f, width * 0.3f, height * 0.08f);
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
        font.dispose();
    }
}