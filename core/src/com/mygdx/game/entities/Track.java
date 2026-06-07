package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Track {
    private final Texture texture;

    private final float width;
    private final float segmentHeight;

    private float offsetY;

    public Track(
            Texture texture,
            float width,
            float segmentHeight
    ) {
        this.texture = texture;
        this.width = width;
        this.segmentHeight = segmentHeight;
    }

    public void update(
            float speed,
            float delta
    ) {
        offsetY -= speed * delta;

        while (offsetY <= -segmentHeight) {
            offsetY += segmentHeight;
        }
    }

    public void draw(SpriteBatch batch) {

        float startY = offsetY - segmentHeight;

        for (int i = 0; i < 5; i++) {

            batch.draw(
                    texture,
                    0,
                    startY + i * segmentHeight,
                    width,
                    segmentHeight
            );
        }
    }

    public void reset() {
        offsetY = 0;
    }

    public float getWidth()
    {
        return width;
    }
}