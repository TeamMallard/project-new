package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a standard set of textures for characters during a battle.
 */
public final class BattleTextures {

    /**
     * Textures to use for when the character is alive or dead.
     */
    private TextureRegion alive, dead;

    /**
     * Character's battle animation.
     */
    private Animation battleAnimation;

    /**
     * Crates a new BattleTextures with the specified parameters.
     *
     * @param alive the alive texture
     * @param dead  the dead texture
     */
    public BattleTextures(TextureRegion alive, TextureRegion dead) {
        this(alive, dead, new Animation(1, alive));
    }

    /**
     * Creates a new BattleTextures with the specified parameters.
     *
     * @param alive           the alive texture
     * @param dead            the dead texture
     * @param battleAnimation the battle animation
     */
    public BattleTextures(TextureRegion alive, TextureRegion dead, Animation battleAnimation) {
        this.alive = alive;
        this.dead = dead;
        this.battleAnimation = battleAnimation;
    }

    /**
     * Gets a texture for the character.
     *
     * @param dead whether the character is dead
     * @return the texture
     */
    public TextureRegion getTexture(boolean dead) {
        return dead ? this.dead : this.alive;
    }

    /**
     * CHANGE E3: Added animations to abilities used while in an encounter.
     */

    /**
     * Gets a frame for the character's battle animation.
     *
     * @param stateTime the time since the character started attacking
     * @return the frame
     */
    public TextureRegion getBattleTexture(float stateTime) {
        return battleAnimation.getKeyFrame(stateTime, true);
    }
}
