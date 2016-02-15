package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 06/02/2016.
 */
public class UIConsumableMenu extends UIComponent {

    public static final int MAX_ITEMS_PER_PAGE = 5;

    private UIPartyMenu parent;

    private PartyManager partyManager;

    private int selectedPlayer = 0, selectedConsumable = 0;

    private boolean hasFocus = false;

    private Item[] currentConsumable;

    public UIConsumableMenu(float x, float y, float width, float height, UIPartyMenu parent) {
        super(x, y, width, height);
        this.parent = parent;
        this.partyManager = parent.getParty();

        updateConsumable();
    }

    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        int page = selectedConsumable / MAX_ITEMS_PER_PAGE;
        int offset = page * MAX_ITEMS_PER_PAGE;

        for(int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentConsumable.length - offset); i++) {
            currentConsumable[i].render(batch, patch);

            if(hasFocus && selectedConsumable == i) {
                batch.draw(Assets.selectArrow, x, y - (75* (i % MAX_ITEMS_PER_PAGE)) + 30);
            }
        }

        renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentConsumable.length / MAX_ITEMS_PER_PAGE), x + 20, y - 2 * height - 10, Color.WHITE, Assets.consolas16);
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
                Color.BLACK, width, Align.left, false);

        font.draw(batch, layout, x, y);
        layout.setText(font, message,
                color, width, Align.left, false);
        font.draw(batch, layout, x, y);
    }

    private Item[] generateConsumable() {
        List<Item> consumable = new ArrayList<Item>();
        int index = 0;

        for(Integer consumableId : partyManager.getConsumables()) {
            consumable.add(new Item(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), width, Game.items.getConsumable(consumableId)));
        }

        return consumable.toArray(new Item[consumable.size()]);
    }

    private void updateConsumable() {
        selectedConsumable = 0;
        currentConsumable = generateConsumable();
    }

    public void selectPlayer(int index) {
        this.selectedPlayer = index;

        updateConsumable();
    }

    private Agent getSelectedPlayer() {
        return partyManager.getMember(selectedPlayer);
    }

    public void focus() {
        hasFocus = true;
    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public void update() {
        if(InputHandler.isUpJustPressed() && selectedConsumable > 0) {
            selectedConsumable--;
        } else if(InputHandler.isDownJustPressed() && selectedConsumable < currentConsumable.length - 1) {
            selectedConsumable++;
        }

        if(InputHandler.isActJustPressed()) {
            UIManager uiManager = Game.worldScreen.getGameWorld().uiManager;
        	Consumable item = currentConsumable[selectedConsumable].consumable;
            switch(item.getType()){
                case HEAL:{
                    if(!getSelectedPlayer().isDead()) {
	                    Assets.sfx_healNoise.play(Game.masterVolume);
	                    this.getSelectedPlayer().dealHealth(item.getPower());
	                    partyManager.getConsumables().remove(selectedConsumable);
	                    uiManager.addNotification(getSelectedPlayer().getName() + " is healed for " + item.getPower() + " health");
                    } else {
	                    uiManager.addNotification(getSelectedPlayer().getName() + " cannot be healed");
                    }
                    break;
                }
                case REVIVE:{
                    if(getSelectedPlayer().isDead()) {
	                    Assets.sfx_healNoise.play(Game.masterVolume);
	                    this.getSelectedPlayer().dealHealth(item.getPower());
	                    partyManager.getConsumables().remove(selectedConsumable);
	                    uiManager.addNotification(getSelectedPlayer().getName() + " is revived on " + item.getPower() + " health");
	                } else {
	                    uiManager.addNotification(getSelectedPlayer().getName() + " cannot be revived");
	                }
                    break;
                }
                case MANA:{
                    if(!getSelectedPlayer().isDead()) {
	                    Assets.sfx_healNoise.play(Game.masterVolume);
	                    this.getSelectedPlayer().giveMana(item.getPower());
	                    partyManager.getConsumables().remove(selectedConsumable);
	                    uiManager.addNotification(getSelectedPlayer().getName() + " gains " + item.getPower() + " mana");
	                } else {
	                    uiManager.addNotification(getSelectedPlayer().getName() + " cannot be given mana");
	                }
                    break;
                }
                default:
                    uiManager.addNotification("This item cannot be used outside of combat");
            		break;
                	
            }
            updateConsumable();
        	
            hasFocus = false;
            parent.focus();
        }
        if(currentConsumable.length == 0) {
            hasFocus = false;
            parent.focus();
        }
        	
    }

    public class Item extends UIComponent {
        private BitmapFont font = Assets.consolas22;
        private BitmapFont smallFont = Assets.consolas16;

        private Consumable consumable;

        private final float LINE_HEIGHT = 25f;

        private float paddingX = 20;
        private float paddingY = 10;


        public Item(float x, float y, float width, Consumable consumable) {
            super(x, y, width, 55);
            this.consumable = consumable;
        }

        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, consumable.getName(), x, y, Color.WHITE, font);
            renderText(batch, consumable.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY, font);
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
