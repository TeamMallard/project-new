package com.mygdx.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Agent;
import com.mygdx.game.Assets;

/**
 * This UI component uses information about an agent and is used in a list of players in the party.
 */
public class UIPlayer extends UIComponent {

    private BitmapFont font;

    private final float LINE_HEIGHT = 25f;
    private Agent player;

    float paddingX;
    float paddingY;

    public boolean selected;

    public UIPlayer(float x, float y, float width, Agent player) {
        super(x, y, width, 70);
        this.player = player;
        paddingX = 20;
        paddingY = 20;
        font = Assets.consolas22;
        selected = false;
    }

    /**
     * Called once per frame to render the player information.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        String level = "LV:  " + player.getStats().getCurrentLevel();
        String xp = "XP:  " + player.getStats().getExperience() + " /" + player.getStats().getMaxExp();
        String hp = "HP:  " + player.getStats().getCurrentHP() + " /" + player.getStats().getMaxHP();
        String mp = "MP:  " + player.getStats().getCurrentMP() + " /" + player.getStats().getMaxMP();
        if (selected) {
            renderText(batch, player.getName(), x, y, Color.WHITE);
        } else {
            renderText(batch, player.getName(), x, y, Color.LIGHT_GRAY);
        }

        renderText(batch, level, x, y - LINE_HEIGHT, Color.WHITE);
        renderText(batch, xp, x+230, y - LINE_HEIGHT, Color.WHITE);
        renderText(batch, hp, x, y - LINE_HEIGHT*2, Color.WHITE);
        renderText(batch, mp, x+230, y - LINE_HEIGHT*2, Color.WHITE);
    }

    /**
     * Helper function for render that actually does the rendering.
     * @param batch the spritebatch to use.
     * @param message The string to add.
     * @param x The x location.
     * @param y The y location.
     * @param color The colour to render the text as.
     */
    private void renderText(SpriteBatch batch, String message, float x, float y, Color color) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, Align.left, false);

        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, Align.left, false);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }
}
