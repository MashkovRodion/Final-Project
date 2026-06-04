package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ObstacleManager {

    private Array<Obstacle> obstacles;

    private float worldWidth;

    private float lastSpawnX = 0;

    private final float roadLeft;
    private final float roadRight;

    private float distanceSinceSpawn;

    public ObstacleManager(float worldWidth) {

        this.worldWidth = worldWidth;

        roadLeft = worldWidth * 0.198f;
        roadRight = worldWidth * 0.8f;

        obstacles = new Array<>();

        distanceSinceSpawn = 0;
    }

    public void update(
            float speed,
            float delta,
            float passedDistance
    ) {

        distanceSinceSpawn += speed * delta;

        for (int i = obstacles.size - 1; i >= 0; i--) {

            Obstacle obstacle = obstacles.get(i);

            obstacle.update(speed, delta);

            if (obstacle.isOutOfScreen()) {
                obstacles.removeIndex(i);
            }
        }

        float distanceToFinish =
                GameSettings.FINISH_DISTANCE - passedDistance;

        if (distanceToFinish <= 1500f) {
            return;
        }

        if (distanceSinceSpawn >= 300f) {

            spawnRandomObstacle();

            distanceSinceSpawn = 0;
        }
    }

    public void draw(SpriteBatch batch) {

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch);
        }
    }

    private void spawnRandomObstacle() {

        int type = MathUtils.random(4);

        float width;
        float height;

        switch (type) {

            case 0:
                width = 200;
                height = 45;
                break;

            case 1:
                width = 200;
                height = 60;
                break;

            case 2:
                width = 70;
                height = 80;
                break;

            case 3:
                width = 50;
                height = 50;
                break;

            default:
                width = 200;
                height = 80;
                break;
        }

        float x;
        int attempts = 0;

        do {
            x = MathUtils.random(
                    roadLeft,
                    roadRight - width
            );

            attempts++;

        } while (
                Math.abs(x - lastSpawnX) < 120
                        && attempts < 20
        );

        lastSpawnX = x;

        float y = 900;

        obstacles.add(
                new Obstacle(
                        getTextureForType(type),
                        x,
                        y,
                        width,
                        height,
                        type
                )
        );
    }

    private Texture getTextureForType(int type) {

        switch (type) {

            case 0: return GameResources.barrier1;
            case 1: return GameResources.barrier2;
            case 2: return GameResources.barrier3;
            case 3: return GameResources.barrier4;
            default: return GameResources.barrier5;
        }
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public void clear() {

        obstacles.clear();

        distanceSinceSpawn = 0;
    }
}