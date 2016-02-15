package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Oliver on 13/02/2016.
 */
public final class BattleTextures {

    private TextureRegion alive, dead;

    private Animation battleAnimation;

    public BattleTextures(TextureRegion alive, TextureRegion dead) {
        this(alive, dead, new Animation(1, alive));
    }

    public BattleTextures(TextureRegion alive, TextureRegion dead, Animation battleAnimation) {
        this.alive = alive;
        this.dead = dead;
        this.battleAnimation = battleAnimation;
    }

    public TextureRegion getTexture(boolean dead) {
        return dead ? this.dead : this.alive;
    }

    public TextureRegion getBattleTexture(float stateTime) {
        return battleAnimation.getKeyFrame(stateTime, true);
    }
}
