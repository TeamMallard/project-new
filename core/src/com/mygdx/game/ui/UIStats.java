package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Agent;
import com.mygdx.game.Game;
import com.mygdx.game.assets.Assets;

/**
 * Represents the statistics of an agent in the party menu.
 */
public class UIStats extends UIComponent {

    /**
     * The agent this UIStats displays the statistics of.
     */
    private Agent player;

    /**
     * Text padding.
     */
    private float paddingX, paddingY;

    /**
     * Creates a new UIStats with the following parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the statistics display
     * @param player the player to display the statistics of
     */
    public UIStats(float x, float y, float width, Agent player) {
        super(x, y, width, 350);
        this.player = player;
        paddingX = 20;
        paddingY = 20;
    }

    /**
     * Renders this UIStats onto the specified sprite batch.
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
        String sp = "SPEED:  " + player.getTotalSpeed() + "(" + player.getStats().getSpeed() + ")";
        String de = "DEXTERITY:  " + player.getTotalDexterity() + "(" + player.getStats().getDexterity() + ")";
        String st = "STRENGTH:  " + player.getTotalStrength() + "(" + player.getStats().getStrength() + ")";
        String in = "INTELLIGENCE:  " + player.getTotalInteligence() + "(" + player.getStats().getIntelligence() + ")";
        String ar = "ARMOUR:  " + player.getTotalDefence() + "(" + player.getStats().getArmourVal() + ")";
        String eq = "EQUIPMENT:";
        String eqHead = "HEAD:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[0]).getName();
        String eqChest = "CHEST:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[1]).getName();
        String eqFeet = "FEET:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[2]).getName();
        String eqAccessory = "ACCESSORY:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[3]).getName();
        String eqWeapon = "WEAPON:  " + Game.items.getEquipment(player.getCurrentEquipment().equipSlots[4]).getName();

        renderText(batch, level, x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, xp, x + 200, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, hp, x, y - 30f, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, mp, x + 200, y - 30f, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, sp, x, y - 30f * 2, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, de, x + 200, y - 30f * 2, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, st, x, y - 30f * 3, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, in, x + 200, y - 30f * 3, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, ar, x, y - 30f * 4, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eq, x, y - 30f * 6, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eqHead, x, y - 30f * 7, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eqChest, x, y - 30f * 8, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eqFeet, x, y - 30f * 9, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eqAccessory, x, y - 30f * 10, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        renderText(batch, eqWeapon, x, y - 30f * 11, paddingX, paddingY, Color.WHITE, Assets.consolas22);
    }
}
