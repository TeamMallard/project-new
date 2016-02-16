package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.InputHandler;
import com.mygdx.game.Level;
import com.mygdx.game.assets.Assets;

/**
 * This class is a character that is controlled by the user.
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
     * Updates the movement of the character based on user input.
     * @param delta The time since the last frame was rendered.
     */
    protected void updateStationary(float delta) {
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
     * Similar to NPC but requires user input to determine direction.
     * @param delta The time since the last frame was rendered.
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
     * Extends parent class by also updating the current interactingNPC of the player.
     * @param delta The time since the last frame was rendered.
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

    @Override
    public TextureRegion getCurrentTexture() {
        if(level.checkWater((int) getCurrentTile().x, (int) getCurrentTile().y)) {
            return Assets.playerSwimmingTextures.getTexture(getDirection(), getStateTime());
        } else {
            return super.getCurrentTexture();
        }
    }
}
