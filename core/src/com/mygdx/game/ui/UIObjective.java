package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game;

/**
 * Represents the player's current objective display.
 */
public class UIObjective extends UIComponent {

    /**
     * The message box containing the objective.
     */
    private UIMessageBox messageBox;

    /**
     * Creates a new UIObjective.
     */
    public UIObjective() {
        super(0, Gdx.graphics.getHeight() - 40, 600, 20);
        this.messageBox = new UIMessageBox("", this.x, this.y, this.width, this.height, 10, 10);
    }

    /**
     * Renders this UIObjective onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    public void render(SpriteBatch batch, NinePatch patch) {
        if (Game.objective != null) {
            messageBox.setMessage(Game.objective.isComplete() ? "Find the exit door" : Game.objective.getObjectiveString());
            messageBox.render(batch, patch);
        }
    }
}
