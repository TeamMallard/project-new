package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Level;
import com.mygdx.game.assets.WalkingTextures;

import java.util.Comparator;

import static com.mygdx.game.Level.TILE_SIZE;

/**
 * Represents a character in the overworld.
 */
public abstract class Character {

    /**
     * The speed at which Characters move between tiles.
     */
    public final float TRANSITION_SPEED = 0.25f;

    /**
     * The time to wait before stopping moving.
     */
    public final float WAIT_PERIOD = 0.15f;

    /**
     * The size of each Character, used for moving the camera.
     */
    public static final Vector2 CHARACTER_SIZE = new Vector2(26, 42);

    /**
     * Which tile the Character is on.
     */
    private Vector2 currentTile;

    /**
     * The absolute position of this Character in the world.
     */
    private Vector2 absPos;

    /**
     * Which tile the Character is moving to.
     */
    public Vector2 targetTile;
    /**
     * The absolute position of the target.
     */
    protected Vector2 targetPos;

    /**
     * The position of this Character before movement.
     */
    protected Vector2 oldPos;

    /**
     * The direction this Character is moving in.
     */
    private Direction direction;

    /**
     * The state this Character is in.
     */
    private CharacterState state;

    /**
     * How long the Character has been in its current state.
     */
    private float stateTime;

    /**
     * How long the character has been waiting before stopping moving.
     */
    protected float waitTime;

    /**
     * The set of walking textures to use.
     */
    private WalkingTextures walkingTextures;

    /**
     * The level this Character is a part of.
     */
    protected Level level;

    /**
     * Creates a new Character with the specified parameters.
     *
     * @param level           the level this Character is a part of
     * @param currentTile     the tile this Character begins on
     * @param walkingTextures the walking textures to use
     */
    public Character(Level level, Vector2 currentTile, WalkingTextures walkingTextures) {
        this.level = level;
        this.walkingTextures = walkingTextures;

        waitTime = 0;
        this.currentTile = currentTile;
        absPos = new Vector2(currentTile.cpy().scl(TILE_SIZE));
        targetTile = new Vector2(currentTile);

        targetPos = new Vector2(targetTile.cpy().scl(TILE_SIZE));
        oldPos = new Vector2(absPos);

        setDirection(Direction.UP);
        setState(CharacterState.STATIONARY);
    }

    /**
     * Updates the state of this Character.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        if (!level.stopInput) {
            if (getState() == CharacterState.STATIONARY) {
                updateStationary(delta);
            } else if (getState() == CharacterState.WAITING) {
                updateWaiting(delta);
            } else {
                updateTransitioning(delta);
            }
        }
    }

    /**
     * Helper method to update if the character state is WAITING.
     *
     * @param delta the time elapsed since the last update
     */
    private void updateWaiting(float delta) {
        waitTime += delta;
        if (waitTime > WAIT_PERIOD) {
            setState(CharacterState.STATIONARY);
        }
    }

    /**
     * Helper method to update if the character state is TRANSITIONING.
     *
     * @param delta the time elapsed since the last update
     */
    protected abstract void updateTransitioning(float delta);

    /**
     * Helper method that determines if a character can move to a particular position in the level.
     *
     * @param requestedDirection the time elapsed since the last update
     */
    protected void updateMovement(Direction requestedDirection) {
        setDirection(requestedDirection);
        int currentX = (int) getCurrentTile().x, currentY = (int) getCurrentTile().y;
        int deltaX = 0, deltaY = 0;

        switch (requestedDirection) {
            case UP:
                deltaY = 1;
                break;
            case DOWN:
                deltaY = -1;
                break;
            case LEFT:
                deltaX = -1;
                break;
            case RIGHT:
                deltaX = 1;
        }

        if (!level.checkCollision(currentX + deltaX, currentY + deltaY)) {
            setState(CharacterState.TRANSITIONING);
            targetTile.add(deltaX, deltaY);
            targetPos.set(targetTile.cpy().scl(TILE_SIZE));
        }
    }

    /**
     * Helper method to update if the character state is STATIONARY.
     *
     * @param delta the time elapsed since the last update
     */
    protected abstract void updateStationary(float delta);

    /**
     * @return the current tile that the Character is on
     */
    public Vector2 getCurrentTile() {
        return currentTile;
    }

    /**
     * Sets the current tile that the Character is on.
     *
     * @param currentTile the tile
     */
    public void setCurrentTile(Vector2 currentTile) {
        this.currentTile = currentTile;
        this.targetTile = currentTile;
        absPos.set(currentTile.cpy().scl(TILE_SIZE));
        absPos = new Vector2(currentTile.cpy().scl(TILE_SIZE));
        targetPos = new Vector2(targetTile.cpy().scl(TILE_SIZE));
        oldPos = new Vector2(absPos);
    }

    /**
     * Gets the absolute position of this Character in the world.
     *
     * @return the absolute position
     */
    public Vector2 getAbsPos() {
        return absPos;
    }

    /**
     * @return the direction this Character is facing
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction this Character is facing.
     *
     * @param direction the direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the state of this Character
     */
    public CharacterState getState() {
        return state;
    }

    /**
     * Sets the state of this Character.
     *
     * @param state the state
     */
    public void setState(CharacterState state) {
        this.state = state;
    }

    /**
     * @return how long this Character has been in its current state
     */
    public float getStateTime() {
        return stateTime;
    }

    /**
     * Sets the state time of this Character.
     *
     * @param stateTime how long this Character has been in its current state
     */
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    /**
     * @return the current texture to use when rendering this Character.
     */
    public TextureRegion getCurrentTexture() {
        return walkingTextures.getTexture(direction, stateTime);
    }

    /**
     * Represents the state of a character.
     */
    public enum CharacterState {
        STATIONARY, WAITING, TRANSITIONING
    }

    /**
     * Represents the direction that a character is facing.
     */
    public enum Direction {
        DOWN(0), UP(1), LEFT(2), RIGHT(3);

        private int index;

        Direction(int index) {
            this.index = index;
        }

        /**
         * @return the direction represented (down: 0, up: 1, left: 2, right: 3)
         */
        public int getIndex() {
            return index;
        }
    }

    /**
     * Compares characters according to their y coordinate.
     */
    public static class CharacterComparator implements Comparator<Character> {

        /**
         * Compares characters according to their y coordinate.
         *
         * @param o1 the first character
         * @param o2 the second character
         * @return the result of the comparison
         */
        @Override
        public int compare(Character o1, Character o2) {
            if (o1.getCurrentTile().y > o2.getCurrentTile().y)
                return -1;
            if (o1.getCurrentTile().y == o2.getCurrentTile().y)
                return 0;
            else
                return 1;
        }
    }
}
