package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class SkinSelectionScreen implements Screen, InputProcessor {

    private final MyGdxGame game;
    private final BitmapFont font;
    private final Vector3 touchPos = new Vector3();

    private Texture background;
    private Button backButton;

    private SkinButton[] skinButtons;
    private SkinManager.CarSkin[] skins;

    private float scrollOffset = 0;
    private float maxScroll;
    private boolean isDragging = false;
    private float lastTouchY;

    private class SkinButton {
        Button button;
        SkinManager.CarSkin skin;
        float x, y, width, height;

        SkinButton(SkinManager.CarSkin skin, float x, float y, float width, float height) {
            this.skin = skin;
            this.button = new Button(skin.getTexture(), x, y, width, height);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        void setPosition(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            button.setPosition(x, y, width, height);
        }

        void update(float mouseX, float mouseY) {
            button.update(mouseX, mouseY);
        }

        void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
            button.draw(batch);
            if (skin == SkinManager.getCurrentSkin()) {
                float borderWidth = 5f;
                batch.draw(GameResources.checkmark,
                        x - borderWidth, y - borderWidth,
                        width + borderWidth * 2, height + borderWidth * 2);
            }
        }

        boolean isTapped(float x, float y) {
            return button.isTapped(x, y);
        }
    }

    public SkinSelectionScreen(MyGdxGame game) {
        this.game = game;
        this.font = new BitmapFont();

        skins = SkinManager.CarSkin.values();
        skinButtons = new SkinButton[skins.length];
    }

    @Override
    public void show() {
        background = GameResources.menu_bg;
        backButton = new Button(GameResources.backButton, 0, 0, 0, 0);
        updateLayout();
        Gdx.input.setInputProcessor(this);
    }

    private void updateLayout() {
        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        font.getData().setScale(h * 0.004f);

        float backWidth = w * 0.35f;
        float backHeight = h * 0.35f;
        backButton.setPosition(
                (w - backWidth) / 2f,
                h * -0.1f,
                backWidth,
                backHeight
        );

        float carButtonSize = Math.min(w * 0.19f, h * 0.24f);
        float spacing = w * 0.04f;

        float leftOffset = -w * 0.05f;
        float rightOffset = w * 0.05f;

        float centerX = w / 2f;
        float startY = h * 0.55f;

        int rows = (int) Math.ceil(skins.length / 2.0);
        float totalHeight = rows * (carButtonSize + spacing);
        maxScroll = Math.max(0, totalHeight - (h * 0.5f));

        for (int i = 0; i < skins.length; i++) {
            int row = i / 2;
            int col = i % 2;

            float horizontalOffset = 0;
            String skinName = skins[i].getDisplayName();

            float baseX;
            if (col == 0) {
                baseX = centerX - carButtonSize - spacing/2;
            } else {
                baseX = centerX + spacing/2;
            }

            float x = baseX + horizontalOffset;
            float y = startY - row * (carButtonSize + spacing) + scrollOffset;

            if (skinButtons[i] == null) {
                skinButtons[i] = new SkinButton(skins[i], x, y, carButtonSize, carButtonSize);
            } else {
                skinButtons[i].setPosition(x, y, carButtonSize, carButtonSize);
                skinButtons[i].button.setTexture(skins[i].getTexture());
            }
        }
    }

    @Override
    public void render(float delta) {
        game.uiViewport.apply();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = game.uiViewport.getWorldWidth();
        float h = game.uiViewport.getWorldHeight();

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.uiViewport.unproject(mousePos);

        backButton.update(mousePos.x, mousePos.y);

        for (SkinButton skinBtn : skinButtons) {
            skinBtn.update(mousePos.x, mousePos.y);
        }

        game.batch.setProjectionMatrix(game.uiCamera.combined);
        game.batch.begin();

        game.batch.draw(background, 0, 0, w, h);

        for (SkinButton skinBtn : skinButtons) {
            if (skinBtn.y + skinBtn.height > h * 0.05f && skinBtn.y < h * 0.88f) {
                skinBtn.draw(game.batch);
                String name = skinBtn.skin.getDisplayName();
                GlyphLayout nameLayout = new GlyphLayout(font, name);
                float nameY = skinBtn.y - 15;
                if (nameY > 10) {
                    font.draw(game.batch, name,
                            skinBtn.x + (skinBtn.width - nameLayout.width) / 2f,
                            nameY);
                }
            }
        }

        backButton.draw(game.batch);

        game.batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        game.uiViewport.unproject(touchPos);

        if (backButton.isTapped(touchPos.x, touchPos.y)) {
            game.setScreen(game.menuScreen);
            return true;
        }

        for (SkinButton skinBtn : skinButtons) {
            if (skinBtn.isTapped(touchPos.x, touchPos.y)) {
                SkinManager.setCurrentSkin(skinBtn.skin);
                if (game.gameScreen != null) {
                    game.gameScreen.updateCarSkin();
                }
                return true;
            }
        }

        isDragging = true;
        lastTouchY = touchPos.y;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            touchPos.set(screenX, screenY, 0);
            game.uiViewport.unproject(touchPos);
            float deltaY = touchPos.y - lastTouchY;
            scrollOffset += deltaY;
            scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffset));
            lastTouchY = touchPos.y;
            updateLayout();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        return true;
    }

    @Override
    public void resize(int width, int height) {
        game.uiViewport.update(width, height, true);
        updateLayout();
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public boolean keyDown(int k) { return false; }
    @Override public boolean keyUp(int k) { return false; }
    @Override public boolean keyTyped(char c) { return false; }
    @Override public boolean touchCancelled(int x, int y, int p, int b) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float a, float b) { return false; }
}