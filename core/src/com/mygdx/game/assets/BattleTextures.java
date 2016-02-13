package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Oliver on 13/02/2016.
 */
public final class BattleTextures {

    private TextureRegion alive, dead;

    public BattleTextures(TextureRegion alive, TextureRegion dead) {
        this.alive = alive;
        this.dead = dead;
    }

    public TextureRegion getTexture(boolean dead) {
        return dead ? this.dead : this.alive;
    }
}
