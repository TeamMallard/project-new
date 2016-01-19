package com.mygdx.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;
import com.mygdx.game.Skill;

/**
 * The skill menu is part of the party menu and show information about a particular skill.
 */
public class UISkill extends UIComponent {

    private BitmapFont font;

    private final float LINE_HEIGHT = 25f;
    private Skill skill;

    float paddingX;
    float paddingY;

    public UISkill(float x, float y, float width, Skill skill) {
        super(x, y, width, 50);
        this.skill = skill;
        paddingX = 20;
        paddingY = 20;
        font = Assets.consolas22;
    }

    /**
     * Called once per frame to render the skill information.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        renderText(batch, skill.getName(), x, y, Color.WHITE);
        renderText(batch, skill.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY);
        renderText(batch, "MP COST: " + skill.getMPCost(), x+250, y, Color.WHITE);
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
