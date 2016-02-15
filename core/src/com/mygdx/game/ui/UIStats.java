package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Agent;
import com.mygdx.game.Game;
import com.mygdx.game.assets.Assets;

/**
 * A menu that shows the statistics of an agent.
 */
public class UIStats extends UIComponent {

    private BitmapFont font;

    private final float LINE_HEIGHT = 30f;
    private Agent player;

    float paddingX;
    float paddingY;

    public boolean selected;

    public UIStats(float x, float y, float width, Agent player) {
        super(x, y, width, 350);
        this.player = player;
        paddingX = 20;
        paddingY = 20;
        font = Assets.consolas22;
        selected = false;
    }

    /**
     * Called once per frame to render the ui component.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        patch.draw(batch, x, y, width, height + (paddingY * 2));
        String level = "LV:  " + player.getStats().getCurrentLevel();
        String xp = "XP:  " + player.getStats().getExperience() + " /" + player.getStats().getLevelCap();
        String hp = "HP:  " + player.getStats().getCurrentHP() + " /" + player.getStats().getMaxHP();
        String mp = "MP:  " + player.getStats().getCurrentMP() + " /" + player.getStats().getMaxMP();
        String sp = "SPEED:  " + player.getTotalSpeed() + "(" + player.getStats().getSpeed() + ")";
        String de = "DEXTERITY:  " + player.getTotalDexterity() + "(" + player.getStats().getDexterity() + ")";
        String st = "STRENGTH:  " +  player.getTotalStrength() + "(" + player.getStats().getStrength() + ")";
        String in= "INTELLIGENCE:  " +  player.getTotalInteligence() + "(" + player.getStats().getIntelligence() + ")";
        String ar = "ARMOUR:  " +  player.getTotalDefence() + "(" + player.getStats().getArmourVal() + ")";
        String eq = "EQUIPMENT:";
        String eqHead = "HEAD:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[0]).getName();
        String eqChest = "CHEST:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[1]).getName();
        String eqFeet = "FEET:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[2]).getName();
        String eqAccessory = "ACCESSORY:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[3]).getName();
        String eqWeapon = "WEAPON:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[4]).getName();


        renderText(batch, level, x, y, Color.WHITE);
        renderText(batch, xp, x + 200, y, Color.WHITE);
        renderText(batch, hp, x, y - LINE_HEIGHT, Color.WHITE);
        renderText(batch, mp, x + 200, y - LINE_HEIGHT, Color.WHITE);
        renderText(batch, sp, x, y - LINE_HEIGHT*2, Color.WHITE);
        renderText(batch, de, x + 200, y - LINE_HEIGHT*2, Color.WHITE);
        renderText(batch, st, x, y - LINE_HEIGHT*3, Color.WHITE);
        renderText(batch, in, x + 200, y - LINE_HEIGHT*3, Color.WHITE);
        renderText(batch, ar, x, y - LINE_HEIGHT*4, Color.WHITE);
        renderText(batch, eq, x, y - LINE_HEIGHT*6, Color.WHITE);
        renderText(batch, eqHead, x, y - LINE_HEIGHT*7, Color.WHITE);
        renderText(batch, eqChest, x, y - LINE_HEIGHT*8, Color.WHITE);
        renderText(batch, eqFeet, x, y - LINE_HEIGHT*9, Color.WHITE);
        renderText(batch, eqAccessory, x, y - LINE_HEIGHT*10, Color.WHITE);
        renderText(batch, eqWeapon, x, y - LINE_HEIGHT*11, Color.WHITE);
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
