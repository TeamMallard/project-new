package com.mygdx.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.entity.Character;

/**
 * Represents a standard set of textures for character facing and walking animation.
 */
public final class WalkingTextures {

    /**
     * Textures to use when the character isn't moving.
     */
    private TextureRegion[] idleTextures = new TextureRegion[4];

    /**
     * Animations to use when the character is walking.
     */
    private Animation[] walkingAnimations = new Animation[4];

    /**
     * Initialises this TextureSet with a single texture used for everything.
     *
     * @param all the texture to use for everything
     */
    public WalkingTextures(TextureRegion all) {
        this(all, all, all, all);
    }

    /**
     * Initialises this TextureSet with textures for facing. No movement animations will be used.
     *
     * @param down  the forward facing texture
     * @param up    the backward facing texture
     * @param left  the left facing texture
     * @param right the right facing texture
     */
    public WalkingTextures(TextureRegion down, TextureRegion up, TextureRegion left, TextureRegion right) {
        this(down, up, left, right, new Animation(0, down),
                new Animation(0, up), new Animation(0, left), new Animation(0, right));
    }

    /**
     * Initialises this TextureSet with textures for facing and movement animations.
     *
     * @param down         the forward facing texture
     * @param up           the backward facing texture
     * @param left         the left facing texture
     * @param right        the right facing texture
     * @param walkingDown  the walking forward animation
     * @param walkingUp    the walking backward animation
     * @param walkingLeft  the walking left animation
     * @param walkingRight the walking right animation
     */
    public WalkingTextures(TextureRegion down, TextureRegion up, TextureRegion left, TextureRegion right,
                           Animation walkingDown, Animation walkingUp, Animation walkingLeft, Animation walkingRight) {
        idleTextures[Character.Direction.DOWN.getIndex()] = down;
        idleTextures[Character.Direction.UP.getIndex()] = up;
        idleTextures[Character.Direction.LEFT.getIndex()] = left;
        idleTextures[Character.Direction.RIGHT.getIndex()] = right;

        walkingAnimations[Character.Direction.DOWN.getIndex()] = walkingDown;
        walkingAnimations[Character.Direction.UP.getIndex()] = walkingUp;
        walkingAnimations[Character.Direction.LEFT.getIndex()] = walkingLeft;
        walkingAnimations[Character.Direction.RIGHT.getIndex()] = walkingRight;
    }

    /**
     * Gets the texture for the specified facing at the specified state time. A state time of 0 means not moving; idle.
     *
     * @param facing    the facing
     * @param stateTime the state time
     * @return the appropriate texture
     */
    public TextureRegion getTexture(Character.Direction facing, float stateTime) {
        if (stateTime > 0) {
            return walkingAnimations[facing.getIndex()].getKeyFrame(stateTime, true);
        } else {
            return idleTextures[facing.getIndex()];
        }
    }
}
