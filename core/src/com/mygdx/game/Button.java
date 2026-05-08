package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {

    private Texture texture;

    // Реальная область картинки
    private Rectangle drawBounds;

    // Область нажатия
    private Rectangle hitbox;

    public Button(Texture texture,
                  float x, float y,
                  float width, float height) {

        this.texture = texture;

        drawBounds = new Rectangle(x, y, width, height);

        // Хитбокс меньше картинки
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

    public void draw(SpriteBatch batch) {

        batch.draw(texture,
                drawBounds.x,
                drawBounds.y,
                drawBounds.width,
                drawBounds.height);
    }

    public void dispose() {
        texture.dispose();
    }
}