package com.mygdx.game.managers;

import com.badlogic.gdx.graphics.Texture;

public class SkinManager {
    public enum CarSkin {
        DEFAULT("skins/Ferrari SS.PNG", "Ferrari"),
        SPORTS("skins/MclarenSS.PNG", "Mclaren"),
        CLASSIC("skins/Mercedes SS.png", "Mercedes"),
        TRUCK("skins/RBR SS.PNG", "RedBull");

        private final String texturePath;
        private final String displayName;
        private Texture texture;

        CarSkin(String texturePath, String displayName) {
            this.texturePath = texturePath;
            this.displayName = displayName;
        }

        public String getTexturePath() { return texturePath; }
        public String getDisplayName() { return displayName; }
        public Texture getTexture() { return texture; }
        public void setTexture(Texture texture) { this.texture = texture; }
    }

    private static CarSkin currentSkin = CarSkin.DEFAULT;

    public static void loadTextures() {
        for (CarSkin skin : CarSkin.values()) {
            if (skin.getTexture() == null) {
                skin.setTexture(new Texture(skin.getTexturePath()));
            }
        }
    }

    public static void disposeTextures() {
        for (CarSkin skin : CarSkin.values()) {
            if (skin.getTexture() != null) {
                skin.getTexture().dispose();
                skin.setTexture(null);
            }
        }
    }

    public static CarSkin getCurrentSkin() {
        return currentSkin;
    }

    public static void setCurrentSkin(CarSkin skin) {
        currentSkin = skin;
    }

    public static Texture getCurrentCarTexture() {
        return currentSkin.getTexture();
    }
}