package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameWorld;
import com.mygdx.game.Level;
import com.mygdx.game.assets.WalkingTextures;
import com.mygdx.game.ui.UIManager;

/**
 * Represents a non-player character in the overworld.
 */
public abstract class NPC extends Character {

    /**
     * How long the NPC has been moving for.
     */
    private float runningTime;

    /**
     * The UI manager to show messages on.
     */
    protected UIManager uiManager;

    /**
     * Creates a new NPC with the specified parameters.
     *
     * @param level           the level this NPC is a part of
     * @param currentTile     the tile this NPC begins on
     * @param walkingTextures the walking textures to use
     */
    public NPC(Level level, Vector2 currentTile, WalkingTextures walkingTextures) {
        super(level, currentTile, walkingTextures);
    }

    /**
     * Randomly moves to a new tile.
     *
     * @param delta the time elapsed since the last update
     */
    protected void updateStationary(float delta) {
        int randomInt = MathUtils.random(480);
        if (randomInt == 0) {
            updateMovement(Direction.UP);
        } else if (randomInt == 1) {
            updateMovement(Direction.DOWN);
        } else if (randomInt == 2) {
            updateMovement(Direction.LEFT);
        } else if (randomInt == 3) {
            updateMovement(Direction.RIGHT);
        }
    }

    @Override
    protected void updateTransitioning(float delta) {
        runningTime += delta;
        float t = runningTime / TRANSITION_SPEED;
        getAbsPos().set(oldPos.x + (targetPos.x - oldPos.x) * t, oldPos.y + (targetPos.y - oldPos.y) * t);
        if (t >= 1) {
            setState(CharacterState.STATIONARY);
            runningTime = 0;
            getCurrentTile().set(targetTile);
            oldPos.set(getAbsPos());
        }
    }

    /**
     * This abstract method is called when a player first interacts with the NPC.
     *
     * @param delta     the time elapsed since the last update
     * @param uiManager the UI manager to show messages on
     */
    public abstract void initializeInteraction(float delta, UIManager uiManager);

    /**
     * This abstract method is called every frame while a player interacts with the NPC.
     *
     * @param delta the time elapsed since the last update
     * @return true if update should continue
     */
    public abstract boolean updateInteracting(float delta);

    /**
     * This abstract method is called when a player has finished interacting with the NPC.
     */
    public abstract void action(GameWorld gameWorld);
}
