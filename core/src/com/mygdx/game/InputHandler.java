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

    private static Boolean upPressed = false;
    private static Boolean downPressed = false;
    private static Boolean rightPressed = false;
    private static Boolean leftPressed = false;
    private static Boolean actPressed = false;
    private static Boolean escPressed = false;

    private static Boolean upJustPressed = false;
    private static Boolean downJustPressed = false;
    private static Boolean rightJustPressed = false;
    private static Boolean leftJustPressed = false;
    private static Boolean actJustPressed = false;
    private static Boolean escJustPressed = false;

    private static final int UP = Input.Keys.W;
    private static final int DOWN = Input.Keys.S;
    private static final int LEFT = Input.Keys.A;
    private static final int RIGHT = Input.Keys.D;
    private static final int ACT = Input.Keys.E;
    private static final int ESC = Input.Keys.Q;

    /**
     * Updates and polls to see which inputs are active.
     */
    public static void update() {
        if(isInputEnabled) {
            upPressed = false;
            upJustPressed = false;
            if (Gdx.input.isKeyPressed(UP)) {
                upPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(UP)) {
                upJustPressed = true;
            }

            downPressed = false;
            downJustPressed = false;
            if (Gdx.input.isKeyPressed(DOWN)) {
                downPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(DOWN)) {
                downJustPressed = true;
            }

            rightPressed = false;
            rightJustPressed = false;
            if (Gdx.input.isKeyPressed(RIGHT)) {
                rightPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(RIGHT)) {
                rightJustPressed = true;
            }

            leftPressed = false;
            leftJustPressed = false;
            if (Gdx.input.isKeyPressed(LEFT)) {
                leftPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(LEFT)) {
                leftJustPressed = true;
            }

            actPressed = false;
            actJustPressed = false;
            if (Gdx.input.isKeyPressed(ACT)) {
                actPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(ACT)) {
                actJustPressed = true;
            }

            escPressed = false;
            escJustPressed = false;
            if (Gdx.input.isKeyPressed(ESC)) {
                escPressed = true;
            }
            if (Gdx.input.isKeyJustPressed(ESC)) {
                escJustPressed = true;
            }
        }
    }

    public static Boolean isUpPressed() {
        return upPressed;
    }

    public static Boolean isUpJustPressed() {
        return upJustPressed;
    }

    public static Boolean isDownPressed() {
        return downPressed;
    }

    public static Boolean isDownJustPressed() {
        return downJustPressed;
    }

    public static Boolean isRightPressed() {
        return rightPressed;
    }

    public static Boolean isRightJustPressed() {
        return rightJustPressed;
    }

    public static Boolean isLeftPressed() {
        return leftPressed;
    }

    public static Boolean isLeftJustPressed() {
        return leftJustPressed;
    }

    public static Boolean isActPressed() { return actPressed; }

    public static Boolean isActJustPressed() {
        return actJustPressed;
    }

    public static Boolean isEscPressed(){return escPressed;}

    public static Boolean isEscJustPressed(){return escJustPressed;}

    public enum inputType{
        UP,DOWN,LEFT,RIGHT,ACT,ESC
    }

    /**
     * Disables all input updating and sets inputs to false.
     */
    public static void disableAllInput(){
        isInputEnabled=false;
        upPressed = false;
        downPressed = false;
        rightPressed = false;
        leftPressed = false;
        actPressed = false;
        escPressed = false;

        upJustPressed = false;
        downJustPressed = false;
        rightJustPressed = false;
        leftJustPressed = false;
        actJustPressed = false;
        escJustPressed = false;
    }
    public static void enableAllInput(){
        isInputEnabled=true;
    }

}
