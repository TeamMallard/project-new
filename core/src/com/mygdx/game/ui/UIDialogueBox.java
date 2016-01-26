package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.Assets;

/**
 * A dialogue box is a single message with a pointer in the bottom right corner.
 */
public class UIDialogueBox extends UIComponent {

    private Boolean showArrow;
    private UIMessageBox messageBox;

    public UIDialogueBox(float x, float y, float width, float height, String message) {
        super(x, y, width, height);

        this.messageBox = new UIMessageBox(message, Assets.consolas22, Color.WHITE, Align.center, x, y, width, height);
        showArrow = false;
    }

    /**
     * Called once per frame to render the dialogue box.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        messageBox.render(batch, patch);
        if (showArrow) {
            batch.draw(Assets.dialoguePointer, x + width - 20, y + 10);
        }
    }

    /**
     * Used to determine whether the dialogue show show an arrow in the bottom right corner.
     */
    public void setArrow(Boolean show) {
        showArrow = show;
    }
}
