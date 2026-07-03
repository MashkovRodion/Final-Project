package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Obstacle {

    private Texture texture;

    private float x;
    private float y;

    private float width;
    private float height;

    private Rectangle rectBounds;
    private Circle circleBounds;
    private boolean circular;

    private Array<Circle> tireBounds;

    private int type;


    public Obstacle(
            Texture texture,
            float x,
            float y,
            float width,
            float height,
            int type,
            boolean circular
    ) {
        this.texture = texture;
        this.type = type;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.circular = circular;

        if (type == 0) {

            tireBounds = new Array<>();

            final float textureWidth = 897f;
            final float textureHeight = 193f;

            final int tiresCount = 4;
            final float gap = 43f;

            // ширина одной шины на исходной текстуре
            float tireWidth = (textureWidth - gap * (tiresCount - 1)) / tiresCount;

            // масштаб изображения
            float scaleX = width / textureWidth;
            float scaleY = height / textureHeight;

            // радиус круга
            float radius = Math.min(tireWidth * scaleX, textureHeight * scaleY) * 0.45f;

            for (int i = 0; i < tiresCount; i++) {

                float tireLeft = i * (tireWidth + gap);

                float centerX = x + (tireLeft + tireWidth / 2f) * scaleX;
                float centerY = y + (textureHeight / 2f) * scaleY;

                tireBounds.add(new Circle(centerX, centerY, radius));
            }
        }
        else if (circular) {

            circleBounds = new Circle(
                    x + width / 2f,
                    y + height / 2f,
                    Math.min(width, height) * 0.45f
            );

        }
        else {

            rectBounds = new Rectangle(
                    x,
                    y,
                    width,
                    height
            );

        }
    }

    public void update(float speed, float delta) {

        y -= speed * delta;

        if (type == 0) {

            float spacing = width / tireBounds.size;

            for (int i = 0; i < tireBounds.size; i++) {

                tireBounds.get(i).setPosition(
                        x + spacing * i + spacing / 2f,
                        y + height / 2f
                );
            }
        }
        else if (circular) {
            circleBounds.setPosition(
                    x + width / 2f,
                    y + height / 2f
            );
        } else {
            rectBounds.setPosition(x, y);
        }
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

    public float getY() {
        return y;
    }
    public int getType() {
        return type;
    }

    public boolean isCircular() {
        return circular;
    }

    public Rectangle getRectBounds() {
        return rectBounds;
    }

    public Circle getCircleBounds() {
        return circleBounds;
    }

    public Array<Circle> getTireBounds() {
        return tireBounds;
    }

}