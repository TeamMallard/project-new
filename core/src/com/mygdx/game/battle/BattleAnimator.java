package com.mygdx.game.battle;


import com.mygdx.game.Agent;
import com.mygdx.game.UseAbility;

/**
 * Controls the animation of agents on the battle screen.
 */
public class BattleAnimator {

    /**
     * The base speed at which agents move to attack.
     */
    private static final float BASE_SPEED = 5;

    /**
     * Whether the current turn agent is moving out to attack or returning.
     */
    private boolean isMoving = false, isReturning = false;

    /**
     * The number of pixels moved each frame in each direction.
     */
    private float moveSpeedX, moveSpeedY;

    /**
     * The target and original coordinates of the current move agent.
     */
    private float targetX, targetY, originalX, originalY;

    /**
     * The agent that is currently moving.
     */
    private Agent currentMoveAgent;

    /**
     * The ability that is causing the agent to move.
     */
    private UseAbility currentCaller;

    /**
     * Updates the movement and checks for reaching target location.
     * Will also call the movementDone function of the caller to notify of a movement completion.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        if (isMoving) {
            updateMovement();

            if (checkArrived()) {
                currentMoveAgent.setX(targetX);
                currentMoveAgent.setY(targetY);
                isMoving = false;
                currentCaller.movementDone(UseAbility.MOVEMENT_GOING);
            }

            if (currentMoveAgent.isAttacking()) {
                currentMoveAgent.updateAttackTime(delta);
            }
        } else if (isReturning) {
            updateMovement();

            if (checkArrived()) {
                currentMoveAgent.setX(targetX);
                currentMoveAgent.setY(targetY);
                isReturning = false;
                currentCaller.movementDone(UseAbility.MOVEMENT_RETURNING);
            }
        }
    }

    /**
     * Moves the current move agent one step.
     */
    private void updateMovement() {
        if (getDistance(currentMoveAgent.getX(), targetX) > Math.abs(moveSpeedX))
            currentMoveAgent.setX(currentMoveAgent.getX() + moveSpeedX);
        if (getDistance(currentMoveAgent.getY(), targetY) > Math.abs(moveSpeedY))
            currentMoveAgent.setY(currentMoveAgent.getY() + moveSpeedY);
    }

    /**
     * Checks to see if the agent has arrived at the target coordinates.
     *
     * @return true if agent has arrived
     */
    private boolean checkArrived() {
        return getDistance(currentMoveAgent.getX(), targetX) <= Math.abs(moveSpeedX) && getDistance(currentMoveAgent.getY(), targetY) <= Math.abs(moveSpeedY);
    }

    /**
     * Returns the distance between two points.
     *
     * @param point1 the first point
     * @param point2 the second point
     * @return the distance between the two points in pixels
     */
    private float getDistance(float point1, float point2) {
        return Math.abs(point1 - point2);
    }

    /**
     * Moves the specified agent to the target coordinates.
     *
     * @param agent       the agent to move
     * @param thisTargetX the target x coordinate
     * @param caller      the ability that is calling the function
     */
    public void moveAgentTo(Agent agent, float thisTargetX, UseAbility caller) {
        currentCaller = caller;
        currentMoveAgent = agent;

        originalX = agent.getX();
        originalY = agent.getY();

        targetX = agent.getX() + (thisTargetX < originalX ? -100 : 100);
        targetY = agent.getY();

        calculateMovement();

        isMoving = true;
    }

    /**
     * Returns the current move agent to its starting location.
     */
    public void returnAgent() {
        float temp = targetX;
        targetX = originalX;
        originalX = temp;

        temp = targetY;
        targetY = originalY;
        originalY = temp;

        calculateMovement();

        isReturning = true;

    }

    /**
     * Calculates movement speed based on original and target coordinates.
     */
    private void calculateMovement() {
        float distX, distY;

        distX = getDistance(targetX, originalX);
        distY = getDistance(targetY, originalY);

        moveSpeedY = (distY / distX) * BASE_SPEED;
        moveSpeedX = targetX < originalX ? -BASE_SPEED : BASE_SPEED;

        if (targetY < originalY)
            moveSpeedY = -moveSpeedY;
    }
}
