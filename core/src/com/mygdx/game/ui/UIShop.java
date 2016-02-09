package com.mygdx.game.ui;

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
 * The party menu allows the user to see information about each party member.
 * It contains a party member's skills and statistics.
 */
public class UIShop extends UIComponent {

    private PartyManager party;
    private boolean show;
    private boolean leftFocus = true;

    private int itemSelected, menuSelected;

    private UIEquipmentMenu equipmentMenu;

    /**
     * Labels for the titles on the party menu.
     */
    private UIMessageBox buyMessageBox = new UIMessageBox("BUY", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x, y + 75, width / 6, 0, 10);
    private UIMessageBox sellMessageBox = new UIMessageBox("SELL", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + 460, y + 75, width / 6, 0, 10);
    
    public UIShop(float x, float y, float width, float height, PartyManager party) {
        super(x, y, width, height);
        this.party = party;
        show = true;
        menuSelected = 0;
        itemSelected = 0;
        
        
    }

    /**
     * Called once per frame to render the party menu.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        if (show) {
            patch.draw(batch, x, y-height+110, width, height);
            buyMessageBox.setColor(Color.WHITE);
            sellMessageBox.setColor(Color.WHITE);
        	//BUY ITEMS
        	int i;
            for(i = 0; i < party.getEquipables().size(); i++) {
                Equipable equipable = Game.items.getEquipable(party.getEquipables().get(i));
                new Item(x, y - (75*i), 450, equipable).render(batch, patch);

                if(itemSelected == i && leftFocus == true) {
                    batch.draw(Assets.arrow, x, y - (75*i) + 30);
                }
            }
            for(int ii = 0; ii < party.getConsumables().size()-2; ii++) {
                Consumable consumable = Game.items.getConsumable(party.getConsumables().get(ii));
                new Item(x, y - (75*(ii + i)), 450, consumable).render(batch, patch);

                if(itemSelected == ii + i && leftFocus == true) {
                    batch.draw(Assets.arrow, x, y - (75*(ii + i)) + 30);
                }
            }
            //SELL ITEMS
            for(i = 0; i < party.getEquipables().size(); i++) {
                Equipable equipable = Game.items.getEquipable(party.getEquipables().get(i));
                new Item(x+460, y - (75*i), 450, equipable).render(batch, patch);

                if(itemSelected == i && leftFocus == false) {
                    batch.draw(Assets.arrow, x+460, y - (75*i) + 30);
                }
            }
            for(int ii = 0; ii < party.getConsumables().size()-2; ii++) {
                Consumable consumable = Game.items.getConsumable(party.getConsumables().get(ii));
                new Item(x+460, y - (75*(ii + i)), 450, consumable).render(batch, patch);

                if(itemSelected == ii + i && leftFocus == false) {
                    batch.draw(Assets.arrow, x+460, y - (75*(ii + i)) + 30);
                }
            }
        }
        buyMessageBox.render(batch, patch);
        sellMessageBox.render(batch, patch);
    }

    /**
     * Makes the UI component visible on screen.
     */
    public void show() {
        itemSelected = 0;
        menuSelected = 0;
        show = true;
    }

    public PartyManager getParty() {
        return party;
    }

    /**
     * Called once per frame to handle input logic for selecting a player and exiting the menu.
     *
     * @return returns true if the dialogue box should continue to be displayed.
     */
    public boolean update(float delta) {
        System.out.println("test");
        if (InputHandler.isEscJustPressed()) {
            show = false;
            return false;
        } else {
            optionUpdate();
            return true;
        }
    }

    private void optionUpdate() {
        if (InputHandler.isUpJustPressed()) {
            itemSelected--;
        } else if (InputHandler.isDownJustPressed()) {
        	itemSelected++;
        }
        
        if (InputHandler.isLeftJustPressed()) {
            leftFocus = true;
        } else if (InputHandler.isRightJustPressed()) {
        	leftFocus = false;
        }

        if (InputHandler.isActJustPressed()) {
            //BUY/SELL ITEM
        }

        if (menuSelected < 0)
            menuSelected = 0;
        if (menuSelected > 1)
            menuSelected = 1;
        if (itemSelected < 0)
        	itemSelected = 0;
        if (itemSelected >= party.size())
        	itemSelected = party.size() - 1;
    }
    
    public class Item extends UIComponent {
        private BitmapFont font = Assets.consolas22;
        private BitmapFont smallFont = Assets.consolas16;

        private Equipable equipment;
        private Consumable consumable;

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
        
        public Item(float x, float y, float width, Consumable consumable) {
            super(x, y, width, 55);
            this.consumable = consumable;
        }

        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
        	if(equipment != null) {
	            patch.draw(batch, x, y, width, height + (paddingY * 2));
	            renderText(batch, equipment.getName(), x, y, Color.WHITE, font);
	            renderText(batch, equipment.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY, font);
	            //renderText(batch, "MP COST: " + skill.getMPCost(), x+250, y, Color.WHITE);
	
	            renderText(batch, "LVL " + equipment.getLevelRequirement(), x + 350, y - LINE_HEIGHT, Color.WHITE, font);
	            renderText(batch, "COST: 0", x + 324, y, Color.WHITE, font);
	            renderText(batch, statString, x, y - LINE_HEIGHT * 2, Color.WHITE, smallFont);
        	} else {
	            patch.draw(batch, x, y, width, height + (paddingY * 2));
	            renderText(batch, consumable.getName(), x, y, Color.WHITE, font);
	            renderText(batch, consumable.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY, font);
	            renderText(batch, "COST: 0", x + 324, y, Color.WHITE, font);
        	}
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
