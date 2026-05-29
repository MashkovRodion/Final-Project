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

    public void setPosition(float x, float y, float width, float height) {

        drawBounds.set(x, y, width, height);

        hitbox.set(
                x + width * 0.08f,
                y + height * 0.15f,
                width * 0.84f,
                height * 0.7f
        );
    }

    public boolean isTapped(float x, float y) {
        return hitbox.contains(x, y);
    }

    public void update(float mouseX, float mouseY) {
        boolean hovered = hitbox.contains(mouseX, mouseY);
        targetScale = hovered ? 1.1f : 1f;
        scale += (targetScale - scale) * 0.2f;
    }

    public void draw(SpriteBatch batch) {

        float w = drawBounds.width * scale;
        float h = drawBounds.height * scale;

        float x = drawBounds.x - (w - drawBounds.width) / 2f;
        float y = drawBounds.y - (h - drawBounds.height) / 2f;

        batch.draw(texture, x, y, w, h);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {return this.texture;}
}