package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private Texture texture;

    private float x;
    private float y;

    private float width;
    private float height;

    private Rectangle bounds;

    private int type;

    private boolean finish;


    public Obstacle(
            Texture texture,
            float x,
            float y,
            float width,
            float height,
            int type
    ) {
        this.texture = texture;
        this.type = type;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        bounds = new Rectangle(
                x,
                y,
                width,
                height
        );
    }

    public void update(float speed, float delta) {

        y -= speed * delta;

        bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {

        batch.draw(
                texture,
                x,
                y,
                width,
                height
        );
    }

    public boolean isOutOfScreen() {
        return y + height < 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getY() {
        return y;
    }
    public int getType() {
        return type;
    }
    public boolean isFinish() {
        return finish;
    }

}