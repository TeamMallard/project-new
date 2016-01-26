package com.mygdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameWorld;
import com.mygdx.game.Level;
import com.mygdx.game.ui.UIManager;

/**
 * NPC extends the character class to provide functionality that is unique to NPC's.
 * An npc will move randomly around the level.
 */
public abstract class NPC extends Character {
    private final int MOVEMENT_PROBABILITY = 480;
    private float runningTime;
    protected UIManager uiManager;

    public NPC(Level level, Vector2 currentTile) {
        super(level, currentTile);
    }

    /**
     * Generates a random integer and moves to a new tile.
     * @param delta The time since the last frame was rendered.
     */
    protected void updateStationary(float delta) {
        int randomInt = MathUtils.random(MOVEMENT_PROBABILITY);
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
     * @param delta The time since the last frame was rendered.
     */
    public abstract void initializeInteraction(float delta, UIManager uiManager);

    /**
     * This abstract method is called every frame while a player interacts with the NPC.
     * @param delta The time since the last frame was rendered.
     * @return Returns true if update should continue.
     */
    public abstract boolean updateInteracting(float delta);

    /**
     * This abstract method is called when a player has finished interacting with the NPC.
     */
    public abstract void action(GameWorld gameWorld);
}
