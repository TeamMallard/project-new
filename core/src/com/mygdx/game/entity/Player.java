package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.InputHandler;
import com.mygdx.game.Level;
import com.mygdx.game.assets.Assets;

/**
 * Represents the player character in the overworld.
 */
public class Player extends Character {

    private Direction tempDirection;

    public NPC interactingNPC;

    private float runningTime;

    public Player(Level level, Vector2 currentTile) {
        super(level, currentTile, Assets.playerWalkingTextures);
        tempDirection = getDirection();
    }

    /**
     * Helper method to update if the player state is STATIONARY (begins movement if WASD is pressed).
     *
     * @param delta the time elapsed since the last update
     */
    protected void updateStationary(float delta) {
        /**
         * CHANGE N1: Single key press in an opposite direction will move you to the next tile rather than just turn you around.
         */

        if (InputHandler.isUpPressed()) {
            updateMovement(Direction.UP);
        } else if (InputHandler.isDownPressed()) {
            updateMovement(Direction.DOWN);
        } else if (InputHandler.isLeftPressed()) {
            updateMovement(Direction.LEFT);
        } else if (InputHandler.isRightPressed()) {
            updateMovement(Direction.RIGHT);
        }
    }

    /**
     * Helper method to update if the player state is TRANSITIONING. Uses keyboard input.
     *
     * @param delta the time elapsed since the last update
     */
    protected void updateTransitioning(float delta) {
        runningTime += delta;
        float t = runningTime / TRANSITION_SPEED;
        getAbsPos().set(oldPos.x + (targetPos.x - oldPos.x) * t, oldPos.y + (targetPos.y - oldPos.y) * t);
        if (t >= 1) {
            setState(CharacterState.STATIONARY);
            runningTime = 0;
            getCurrentTile().set(targetTile);
            oldPos.set(getAbsPos());
            setDirection(tempDirection);
        }
        if (InputHandler.isUpPressed()) {
            tempDirection = Direction.UP;
        } else if (InputHandler.isDownPressed()) {
            tempDirection = Direction.DOWN;
        } else if (InputHandler.isLeftPressed()) {
            tempDirection = Direction.LEFT;
        } else if (InputHandler.isRightPressed()) {
            tempDirection = Direction.RIGHT;
        }
    }

    /**
     * Updates the state of this Player.
     *
     * @param delta the time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        switch (getDirection()) {
            case UP:
                interactingNPC = (NPC) level.getCharacterAt(getCurrentTile().x, getCurrentTile().y + 1);
                break;
            case DOWN:
                interactingNPC = (NPC) level.getCharacterAt(getCurrentTile().x, getCurrentTile().y - 1);
                break;
            case LEFT:
                interactingNPC = (NPC) level.getCharacterAt(getCurrentTile().x - 1, getCurrentTile().y);
                break;
            case RIGHT:
                interactingNPC = (NPC) level.getCharacterAt(getCurrentTile().x + 1, getCurrentTile().y);
                break;
        }
    }

    /**
     * @return the current texture to use when rendering this Player.
     */
    @Override
    public TextureRegion getCurrentTexture() {
        /**
         * CHANGE M6: Swimming movement mode was added, which activated when the player navigates over water.
         */
        // If we're in water, use swimming textures.
        if (level.checkWater((int) getCurrentTile().x, (int) getCurrentTile().y)) {
            return Assets.playerSwimmingTextures.getTexture(getDirection(), getStateTime());
        } else {
            return super.getCurrentTexture();
        }
    }
}
