package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Skill;
import com.mygdx.game.assets.Assets;

/**
 * Represents a skill that a player has in the party menu.
 */
public class UISkill extends UIComponent {

    /**
     * The skill this UISkill represents.
     */
    private Skill skill;

    /**
     * Text padding.
     */
    private float paddingX, paddingY;

    /**
     * Creates a new UISkill with the specified parameters.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param width the width of the skill display
     * @param skill the skill this UISkill represents
     */
    public UISkill(float x, float y, float width, Skill skill) {
        super(x, y, width, 50);
        this.skill = skill;
        paddingX = 20;
        paddingY = 20;
    }

    /**
     * Renders this UISkill onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        renderText(batch, skill.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, skill.getDescription(), x, y - 25f, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
        renderText(batch, "MP COST: " + skill.getMPCost(), x + 250, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
    }
}
