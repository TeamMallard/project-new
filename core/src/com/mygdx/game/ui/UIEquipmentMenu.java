package com.mygdx.game.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

/**
 * Created by Oliver on 06/02/2016.
 */
public class UIEquipmentMenu extends UIComponent {

    private UIPartyMenu parent;

    private PartyManager partyManager;

    private int selectedPlayer = 0, selectedEquipment = 0;

    private boolean hasFocus = false;

    public UIEquipmentMenu(float x, float y, float width, float height, UIPartyMenu parent) {
        super(x, y, width, height);

        this.parent = parent;
        this.partyManager = parent.getParty();
    }

    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        for(int i = 0; i < partyManager.getEquipables().size(); i++) {
            Equipable equipable = Game.items.getEquipable(partyManager.getEquipables().get(i));
            new Item(x, y - (75*i), width, equipable).render(batch, patch);

            if(hasFocus && selectedEquipment == i) {
                batch.draw(Assets.arrow, x, y - (75*i) + 30);
            }
        }
    }

    public void selectPlayer(int index) {
        this.selectedPlayer = index;
    }

    public void focus() {
        hasFocus = true;
    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public void update() {
        if(InputHandler.isUpJustPressed()) {
            selectedEquipment -= selectedEquipment > 0 ? 1 : 0;
        } else if(InputHandler.isDownJustPressed()) {
            selectedEquipment += selectedEquipment < partyManager.size() - 1 ? 1 : 0;
        }

        if(InputHandler.isActJustPressed()) {
            if(selectedEquipment < partyManager.getEquipables().size()) {
                setPlayerEquipment(partyManager.getMember(selectedPlayer), selectedEquipment);
                selectedEquipment -= selectedEquipment > 0 ? 1 : 0;
            }

            selectedEquipment = 0;
            hasFocus = false;
            parent.focus();
        }
    }

    private void setPlayerEquipment(Agent player, int selectedEquipment) {
        player.equipEquipment(partyManager.getEquipables().get(selectedEquipment));
        partyManager.getEquipables().remove(selectedEquipment);
    }

    public class Item extends UIComponent {
        private BitmapFont font = Assets.consolas22;
        private BitmapFont smallFont = Assets.consolas16;

        private Equipable equipment;

        private final float LINE_HEIGHT = 25f;

        private float paddingX = 20;
        private float paddingY = 10;

        private String statString;

        public Item(float x, float y, float width, Equipable equipment) {
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
}
