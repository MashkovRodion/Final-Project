package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {

	public SpriteBatch batch;
	public OrthographicCamera gameCamera;
	public OrthographicCamera uiCamera;
	public Viewport gameViewport;
	public Viewport uiViewport;

	public AudioManager audioManager;

	public PauseScreen pauseScreen;
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public OptionScreen optionScreen;
	public GameOverScreen gameOverScreen;

	public LeaderboardScreen leaderboardScreen;
	public SkinSelectionScreen skinSelectionScreen;

	@Override
	public void create() {

		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		Options.fullscreen = true;

		batch = new SpriteBatch();
		gameCamera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();

		gameViewport = new ExtendViewport(
				GameSettings.SCREEN_WIDTH,
				GameSettings.SCREEN_HEIGHT,
				gameCamera
		);

		uiViewport = new ScreenViewport(uiCamera);

		gameViewport.apply(true);
		uiViewport.apply(true);

		GameResources.loadTextures();
		SkinManager.loadTextures();

		audioManager = new AudioManager();
		if (audioManager.backgroundMusic != null) {
			audioManager.backgroundMusic.play();
		}

		pauseScreen = new PauseScreen(this);
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		optionScreen = new OptionScreen(this);
		skinSelectionScreen = new SkinSelectionScreen(this);
		leaderboardScreen = new LeaderboardScreen(this);

		setScreen(menuScreen);
	}

	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height, true);
		uiViewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		if (gameScreen != null) {
			gameScreen.dispose();
		}

		if (audioManager != null) {
			if (audioManager.gasMusic != null) audioManager.gasMusic.dispose();
			if (audioManager.brakeMusic != null) audioManager.brakeMusic.dispose();
			if (audioManager.backgroundMusic != null) audioManager.backgroundMusic.dispose();
		}

		SkinManager.disposeTextures();
		GameResources.dispose();
	}
}