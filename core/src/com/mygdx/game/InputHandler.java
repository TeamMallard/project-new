package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * CHANGE N2: Refactored InputHandler class.
 */

/**
 * Handles input from the keyboard.
 */
public class InputHandler {

    /**
     * Whether input is enabled.
     */
    private static boolean isInputEnabled = true;

    /**
     * CHANGE M5: Added additional movement control, the arrow keys.
     */

    /**
     * Keys for moving up.
     */
    private static final int[] UP = {Input.Keys.W, Input.Keys.UP};

    /**
     * Keys for moving down.
     */
    private static final int[] DOWN = {Input.Keys.S, Input.Keys.DOWN};

    /**
     * Keys for moving left.
     */
    private static final int[] LEFT = {Input.Keys.A, Input.Keys.LEFT};

    /**
     * Keys for moving right.
     */
    private static final int[] RIGHT = {Input.Keys.D, Input.Keys.RIGHT};

    /**
     * Action keys.
     */
    private static final int[] ACT = {Input.Keys.E, Input.Keys.ENTER, Input.Keys.Z};

    /**
     * Back keys.
     */
    private static final int[] ESC = {Input.Keys.Q, Input.Keys.ESCAPE, Input.Keys.X};

    /**
     * Checks whether any of the keys from the specified list is pressed.
     *
     * @param possibleKeys a set of possible keys for a given action
     * @return whether any of the given keys is pressed
     */
    private static boolean anyKeyPressed(int[] possibleKeys) {
        for (int key : possibleKeys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether any of the keys from the specified list has just been pressed.
     *
     * @param possibleKeys a set of possible keys for a given action
     * @return whether any of the given keys is the only one pressed
     */
    private static boolean anyKeyJustPressed(int[] possibleKeys) {
        for (int key : possibleKeys) {
            if (Gdx.input.isKeyJustPressed(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether an up key is pressed
     */
    public static boolean isUpPressed() {
        return isInputEnabled && anyKeyPressed(UP);
    }

    /**
     * @return whether an up key has just been pressed
     */
    public static boolean isUpJustPressed() {
        return isInputEnabled && anyKeyJustPressed(UP);
    }

    /**
     * @return whether a down key is pressed
     */
    public static boolean isDownPressed() {
        return isInputEnabled && anyKeyPressed(DOWN);
    }

    /**
     * @return whether a down key has just been pressed
     */
    public static boolean isDownJustPressed() {
        return isInputEnabled && anyKeyJustPressed(DOWN);
    }

    /**
     * @return whether a right key is pressed
     */
    public static boolean isRightPressed() {
        return isInputEnabled && anyKeyPressed(RIGHT);
    }

    /**
     * @return whether a right key has just been pressed
     */
    public static boolean isRightJustPressed() {
        return isInputEnabled && anyKeyJustPressed(RIGHT);
    }

    /**
     * @return whether a left key is pressed
     */
    public static boolean isLeftPressed() {
        return isInputEnabled && anyKeyPressed(LEFT);
    }

    /**
     * @return whether a left key has just been pressed
     */
    public static boolean isLeftJustPressed() {
        return isInputEnabled && anyKeyJustPressed(LEFT);
    }

    /**
     * @return whether an action key has just been pressed
     */
    public static boolean isActJustPressed() {
        return isInputEnabled && anyKeyJustPressed(ACT);
    }

    /**
     * @return whether an escape key has just been pressed
     */
    public static boolean isEscJustPressed() {
        return isInputEnabled && anyKeyJustPressed(ESC);
    }

    /**
     * Represents a type of input.
     */
    public enum InputType {
        UP, DOWN, LEFT, RIGHT, ACT, ESC
    }

    /**
     * Disables user input.
     */
    public static void disableAllInput() {
        isInputEnabled = false;
    }

    /**
     * Enables user input.
     */
    public static void enableAllInput() {
        isInputEnabled = true;
    }

}
