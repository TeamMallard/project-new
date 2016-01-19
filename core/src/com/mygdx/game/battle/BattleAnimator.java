package com.mygdx.game.battle;


import com.mygdx.game.Agent;
import com.mygdx.game.UseAbility;

/**
 * Controls Agent Animation on the Battle Screen
 */
public class BattleAnimator {

    boolean isMoving = false;
    boolean isReturning = false;
    float baseSpeed = 5;
    float moveSpeedX,moveSpeedY;


    float targetX,targetY,originalX,originalY;
    Agent currentMoveAgent;

    UseAbility currentCaller;


    public void BattleAnimator(){

    }

    /**
     * Updates the movement and checks for reaching target location
     * Will also call the movementDone function of the caller to notify of a movement completion
     */
    public void update(float delta){

        if(isMoving) {
            updateMovement();

            if(checkArrived()){
                currentMoveAgent.setX(targetX);
                currentMoveAgent.setY(targetY);
                isMoving=false;
                currentCaller.movementDone(0);
            }

        }
        else if(isReturning){
            updateMovement();

            if(checkArrived()){
                currentMoveAgent.setX(targetX);
                currentMoveAgent.setY(targetY);
                isReturning=false;
                currentCaller.movementDone(1);
            }
        }
    }

    /**
     * Updates the currentMoveAgent's location using the values already determined
     */
    private void updateMovement(){
        if(getDistance(currentMoveAgent.getX(),targetX)>Math.abs(moveSpeedX))
            currentMoveAgent.setX(currentMoveAgent.getX()+moveSpeedX);
        if(getDistance(currentMoveAgent.getY(),targetY)>Math.abs(moveSpeedY))
            currentMoveAgent.setY(currentMoveAgent.getY()+moveSpeedY);
    }

    /**
     * Checks to see if the agent has arrived at the target
     * @return True if agent has arrived
     */
    private boolean checkArrived(){
        return getDistance(currentMoveAgent.getX(),targetX)<=Math.abs(moveSpeedX) && getDistance(currentMoveAgent.getY(),targetY)<=Math.abs(moveSpeedY);
    }

    /**
     * Returns the distance between 2 points
     * @param point1 float point 1
     * @param point2 float point 2
     * @return the distance between point1 and point2 as a float
     */
    private float getDistance(float point1, float point2){
        return Math.abs(point1-point2);
    }

    /**
     * Moves the given agent to the target coordinates
     * @param agent The agent to move
     * @param thisTargetX The target x coordinates
     * @param thisTargetY The target y coordinates
     * @param caller The class that is calling the function. This is so that once the movement is complete, the calling class can be informed.
     */
    public void moveAgentTo(Agent agent, float thisTargetX, float thisTargetY, UseAbility caller){

        currentCaller = caller;
        currentMoveAgent=agent;

        originalX=agent.getX();
        originalY=agent.getY();
        targetX=thisTargetX;
        targetY=thisTargetY;

        if(targetX<originalX){
            targetX+=52;
        }
        else{
            targetX-=52;
        }

        calculateMovement();

        isMoving=true;

    }

    /**
     * Returns the agent given in moveAgentTo(), to it's starting location
     */
    public void returnAgent(){
        float temp= targetX;
        targetX=originalX;
        originalX=temp;

        temp = targetY;
        targetY=originalY;
        originalY=temp;

        calculateMovement();

        isReturning=true;

    }

    /**
     * Calculates the values needed to make the movement take place
     */
    private void calculateMovement(){
        float distX,distY;
        distX=getDistance(targetX,originalX);
        distY=getDistance(targetY,originalY);

        moveSpeedY=(distY/distX)*baseSpeed;

        if(targetX<originalX){
            moveSpeedX=-baseSpeed;
        }
        else{
            moveSpeedX=baseSpeed;
        }

        if(targetY<originalY)
            moveSpeedY=-moveSpeedY;
    }


}
