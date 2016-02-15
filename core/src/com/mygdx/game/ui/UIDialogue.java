package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.InputHandler;
import com.mygdx.game.assets.Assets;

/**
 * Represents a sequence of dialog boxes.
 */
public class UIDialogue extends UIComponent {

    /**
     * The index currently displayed message.
     */
    private int currentMessage;
    /**
     * How long the current message has been displayed for.
     */
    private float dialogueTimer;
    /**
     * The minimum time before the dialogue can be advanced.
     */
    private float dialogueWaitTime;

    /**
     * The sequence of dialogue boxes to display.
     */
    private UIDialogueBox[] dialogueBoxes;

    /**
     * Creates a new UIDialogue with the specified parameters.
     *
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param width    the width of the menu
     * @param height   the height of the menu
     * @param messages the messages in the dialogue
     */
    public UIDialogue(float x, float y, float width, float height, String[] messages) {
        this(x, y, width, height, messages, 1f);
    }

    /**
     * Creates a new UIDialogue with the specified parameters.
     *
     * @param x                the x coordinate
     * @param y                the y coordinate
     * @param width            the width of the menu
     * @param height           the height of the menu
     * @param messages         the messages in the dialogue
     * @param dialogueWaitTime the minimum time before the dialogue can be advanced
     */
    public UIDialogue(float x, float y, float width, float height, String[] messages, float dialogueWaitTime) {
        super(x, y, width, height);
        dialogueBoxes = new UIDialogueBox[messages.length];
        for (int i = 0; i < messages.length; i++) {
            dialogueBoxes[i] = new UIDialogueBox(x, y, width, height, messages[i]);
        }
        currentMessage = 0;
        this.dialogueWaitTime = dialogueWaitTime;
    }

    /**
     * Renders this UIDialogue onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        dialogueBoxes[currentMessage].render(batch, patch);
    }

    /**
     * Updates the state of this UIDialogue.
     *
     * @param delta the elapsed time since the last update
     * @return whether this UIDialogue should continue being displayed
     */
    public boolean update(float delta) {
        dialogueTimer += delta;
        if (dialogueTimer > dialogueWaitTime) {
            dialogueBoxes[currentMessage].setArrow(dialogueTimer % 1 > 0.5f);
            if (InputHandler.isActJustPressed()) {
                if (currentMessage < dialogueBoxes.length - 1) {
                    currentMessage++;
                    dialogueTimer = 0;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Represents a single message in the UIDialogue.
     */
    public class UIDialogueBox extends UIComponent {

        /**
         * Whether to show the advance arrow.
         */
        private boolean showArrow;

        /**
         * The message box to render the message on.
         */
        private UIMessageBox messageBox;

        /**
         * Creates a new UIDialogueBox with the specified parameters.
         *
         * @param x       the x coordinate
         * @param y       the y coordinate
         * @param width   the width of the menu
         * @param height  the height of the menu
         * @param message the message to display
         */
        public UIDialogueBox(float x, float y, float width, float height, String message) {
            super(x, y, width, height);

            this.messageBox = new UIMessageBox(message, Assets.consolas22, Color.WHITE, Align.center, x, y, width, height);
            showArrow = false;
        }

        /**
         * Renders this UIDialogueBox onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            messageBox.render(batch, patch);
            if (showArrow) {
                batch.draw(Assets.dialoguePointer, x + width - 20, y + 10);
            }
        }

        /**
         * @param show whether to show the advance arrow
         */
        public void setArrow(Boolean show) {
            showArrow = show;
        }
    }
}
