package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Handles input
 * There are 6 different inputs for the game. Up, down, left, right, esc and act.
 * The keybinds for these inputs are assigned here.
 */
public class InputHandler {
    private static Boolean isInputEnabled=true;

    private static final int[] UP = {Input.Keys.W, Input.Keys.UP};
    private static final int[] DOWN = {Input.Keys.S, Input.Keys.DOWN};
    private static final int[] LEFT = {Input.Keys.A, Input.Keys.LEFT};
    private static final int[] RIGHT = {Input.Keys.D, Input.Keys.RIGHT};
    private static final int[] ACT = {Input.Keys.E, Input.Keys.ENTER, Input.Keys.Z};
    private static final int[] ESC = {Input.Keys.Q, Input.Keys.ESCAPE, Input.Keys.X};
    
    /**
     * Checks whether any of the keys from the list is pressed
     * @param possibleKeys a set of possible keys for a given action
     * @return whether any of the given keys is pressed
     */
    private static boolean anyKeyPressed(int[] possibleKeys){
    	for(int key:possibleKeys){
    		if(Gdx.input.isKeyPressed(key)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks whether any of the keys from the list is the only key pressed
     * @param possibleKeys a set of possible keys for a given action
     * @return whether any of the given keys is the only one pressed
     */
    private static boolean anyKeyJustPressed(int[] possibleKeys){
    	for(int key:possibleKeys){
    		if(Gdx.input.isKeyJustPressed(key)){
    			return true;
    		}
    	}
    	return false;
    }
    
    //TODO remove and refactor
    public static void update() {}

    public static Boolean isUpPressed() {
        return isInputEnabled?anyKeyPressed(UP):false;
    }

    public static Boolean isUpJustPressed() {
        return isInputEnabled?anyKeyJustPressed(UP):false;
    }

    public static Boolean isDownPressed() {
    	return isInputEnabled?anyKeyPressed(DOWN):false;
    }

    public static Boolean isDownJustPressed() {
    	return isInputEnabled?anyKeyJustPressed(DOWN):false;
    }

    public static Boolean isRightPressed() {
    	return isInputEnabled?anyKeyPressed(RIGHT):false;
    }

    public static Boolean isRightJustPressed() {
    	return isInputEnabled?anyKeyJustPressed(RIGHT):false;
    }

    public static Boolean isLeftPressed() {
    	return isInputEnabled?anyKeyPressed(LEFT):false;
    }

    public static Boolean isLeftJustPressed() {
    	return isInputEnabled?anyKeyJustPressed(LEFT):false;
    }

    public static Boolean isActPressed() {
    	return isInputEnabled?anyKeyPressed(ACT):false;
    	}

    public static Boolean isActJustPressed() {
    	return isInputEnabled?anyKeyJustPressed(ACT):false;
    }

    public static Boolean isEscPressed(){
    	return isInputEnabled?anyKeyPressed(ESC):false;
    	}

    public static Boolean isEscJustPressed(){
    	return isInputEnabled?anyKeyJustPressed(ESC):false;
    	}

    public enum inputType{
        UP,DOWN,LEFT,RIGHT,ACT,ESC
    }

    /**
     * Disables all input updating and sets inputs to false.
     */
    public static void disableAllInput(){
        isInputEnabled=false;
    }
    
    public static void enableAllInput(){
        isInputEnabled=true;
    }

}
