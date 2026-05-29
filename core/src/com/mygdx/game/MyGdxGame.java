package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {

	public SpriteBatch batch;
	public OrthographicCamera gameCamera;
	public OrthographicCamera uiCamera;
	public Viewport gameViewport;
	public Viewport uiViewport;

	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public OptionScreen optionScreen;

	@Override
	public void create() {



		batch = new SpriteBatch();
		gameCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();

		gameViewport = new FitViewport(
				GameSettings.SCREEN_WIDTH,
				GameSettings.SCREEN_HEIGHT,
				gameCamera
		);

		uiViewport = new ScreenViewport(uiCamera);

		gameViewport.apply(true);
		uiViewport.apply(true);

		GameResources.loadTextures();

		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		optionScreen = new OptionScreen(this);

		setScreen(menuScreen);
	}

	@Override
	public void resize(int width, int height) {

		gameViewport.update(width, height, true);
		uiViewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		GameResources.dispose();
	}
}