package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game;

/**
 * Represents the player's score display.
 */
public class UIScore extends UIComponent {

    /**
     * The message box containing the score.
     */
    private UIMessageBox messageBox;

    /**
     * Creates a new UIScore.
     */
    public UIScore() {
        super(0, 0, 0, 0);
        this.width = 200;
        this.height = 20;
        this.x = Gdx.graphics.getWidth() - width;
        this.y = Gdx.graphics.getHeight() - height * 2;
        this.messageBox = new UIMessageBox("Points: " + Integer.toString(Game.pointsScore), this.x, this.y, this.width, this.height, 10, 10);
    }

    /**
     * Renders this UIScore onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    public void render(SpriteBatch batch, NinePatch patch) {
        messageBox.setMessage("Points: " + Integer.toString(Game.pointsScore));
        messageBox.render(batch, patch);
    }
}
