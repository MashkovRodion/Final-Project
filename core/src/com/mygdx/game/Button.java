package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {

    private Texture texture;

    private Rectangle drawBounds;

    private Rectangle hitbox;

    private float scale = 1f;
    private float targetScale = 1f;

    public Button(Texture texture, float x, float y, float width, float height) {

        this.texture = texture;

        drawBounds = new Rectangle(x, y, width, height);

        hitbox = new Rectangle(
                x + width * 0.08f,
                y + height * 0.15f,
                width * 0.84f,
                height * 0.7f
        );
    }

    public void setPosition(float x, float y,
                            float width, float height) {

        drawBounds.set(x, y, width, height);

        hitbox.set(
                x + width * 0.08f,
                y + height * 0.15f,
                width * 0.84f,
                height * 0.7f
        );
    }

    public boolean isTapped(float screenX, float screenY) {
        return hitbox.contains(screenX, screenY);
    }


    public void dispose() {
        texture.dispose();
    }


    public void draw(SpriteBatch batch) {
        float w = drawBounds.width * scale;
        float h = drawBounds.height * scale;

        float x = drawBounds.x - (w - drawBounds.width) / 2;
        float y = drawBounds.y - (h - drawBounds.height) / 2;

        batch.draw(texture, x, y, w, h);
    }
    public void update(float mouseX, float mouseY) {
        boolean hovered = hitbox.contains(mouseX, mouseY);
        targetScale = hovered ? 1.1f : 1f;

        scale += (targetScale - scale) * 0.2f;
    }




}