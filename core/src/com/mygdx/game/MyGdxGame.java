package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public Viewport viewport;
	public GameScreen gameScreen;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		viewport = new FitViewport(GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT, camera);
		viewport.apply(true);

		GameResources resources = new GameResources();
		resources.loadTextures();

		gameScreen = new GameScreen(this);
		setScreen(new MenuScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		GameResources.dispose();
	}
}