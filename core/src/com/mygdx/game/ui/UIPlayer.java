package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Agent;
import com.mygdx.game.assets.Assets;

/**
 * Represents a player in the party menu.
 */
public class UIPlayer extends UIComponent {

    /**
     * How tall one line of text is.
     */
    private static final float LINE_HEIGHT = 25f;

    /**
     * The agent this UIPlayer represents.
     */
    private Agent player;

    /**
     * Text padding.
     */
    private float paddingX, paddingY;

    /**
     * Whether this UIPlayer is currently selected.
     */
    public boolean selected;

    /**
     * Creates a new UIPlayer with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the menu
     * @param player the player represented
     */
    public UIPlayer(float x, float y, float width, Agent player) {
        super(x, y, width, 70);
        this.player = player;
        paddingX = 20;
        paddingY = 20;
        selected = false;
    }

    /**
     * Renders this UIPlayer onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        String level = "LV:  " + player.getStats().getCurrentLevel();
        String xp = "XP:  " + player.getStats().getExperience() + " /" + player.getStats().getLevelCap();
        String hp = "HP:  " + player.getStats().getCurrentHP() + " /" + player.getStats().getMaxHP();
        String mp = "MP:  " + player.getStats().getCurrentMP() + " /" + player.getStats().getMaxMP();
        if (selected) {
            renderText(batch, player.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            batch.draw(Assets.selectArrow, x, y + 45);
        } else {
            renderText(batch, player.getName(), x, y, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
        }

        renderText(batch, level, x, y - LINE_HEIGHT, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, xp, x + 230, y - LINE_HEIGHT, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, hp, x, y - LINE_HEIGHT * 2, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, mp, x + 230, y - LINE_HEIGHT * 2, paddingX, paddingY, Color.WHITE, Assets.consolas22);
    }
}
