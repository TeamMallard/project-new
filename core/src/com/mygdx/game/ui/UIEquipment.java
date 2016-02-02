package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.Equipable;
import com.mygdx.game.assets.Assets;

/**
 * Created by olivermcclellan on 02/02/2016.
 */
public class UIEquipment extends UIComponent {

    private BitmapFont font = Assets.consolas22;
    private BitmapFont smallFont = Assets.consolas16;

    private Equipable equipment;

    private final float LINE_HEIGHT = 25f;

    private float paddingX = 20;
    private float paddingY = 10;

    private String statString;

    public UIEquipment(float x, float y, float width, Equipable equipment) {
        super(x, y, width, 55);
        this.equipment = equipment;

        StringBuilder statStringBuilder = new StringBuilder();
        int[] modifiers = equipment.getModifiers();
        statStringBuilder.append(modifiers[0]);
        statStringBuilder.append(" AGI / ");
        statStringBuilder.append(modifiers[1]);
        statStringBuilder.append(" STR / ");
        statStringBuilder.append(modifiers[2]);
        statStringBuilder.append(" DEX / ");
        statStringBuilder.append(modifiers[3]);
        statStringBuilder.append(" INT / ");
        statStringBuilder.append(modifiers[4]);
        statStringBuilder.append(" DEF");

        statString = statStringBuilder.toString();
    }

    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        renderText(batch, equipment.getName(), x, y, Color.WHITE, font);
        renderText(batch, equipment.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY, font);
        //renderText(batch, "MP COST: " + skill.getMPCost(), x+250, y, Color.WHITE);

        renderText(batch, "LVL " + equipment.getLevelRequirement(), x + 340, y, Color.WHITE, font);
        renderText(batch, statString, x, y - LINE_HEIGHT * 2, Color.WHITE, smallFont);
    }

    /**
     * Helper function for render that actually does the rendering.
     * @param batch the spritebatch to use.
     * @param message The string to add.
     * @param x The x location.
     * @param y The y location.
     * @param color The colour to render the text as.
     */
    private void renderText(SpriteBatch batch, String message, float x, float y, Color color, BitmapFont font) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, Align.left, false);

        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, Align.left, false);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }
}
