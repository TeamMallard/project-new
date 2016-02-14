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

import java.util.ArrayList;
import java.util.List;

/**
 * The party menu allows the user to see information about each party member.
 * It contains a party member's skills and statistics.
 */
public class UIShop extends UIComponent {

    public static final int MAX_ITEMS_PER_PAGE = 5;

    protected float paddingX = 20;
    protected float paddingY = 10;

    private PartyManager party;
    private Shop shop;
    private boolean show;

    // Whether the current focus is on the list of shop items instead of player items.
    private boolean shopItemsFocus = true;

    // Items held by the player and shop
    private ShopItem[] currentPlayerItems;
    private ShopItem[] currentShopItems;

    // Selected item of each menu.
    private int selectedPlayerItem = 0;
    private int selectedShopItem = 0;
    /**
     * Labels for the titles on the party menu.
     */
    private UIMessageBox buyMessageBox = new UIMessageBox("BUY", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x, y + 75, width / 6, 0, 10);
    private UIMessageBox sellMessageBox = new UIMessageBox("SELL", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + 460, y + 75, width / 6, 0, 10);

    public UIShop(float x, float y, float width, float height, PartyManager party, Shop shop) {
        super(x, y, width, height);
        this.party = party;
        this.shop = shop;
        updateItems();

        show = true;
    }

    public ShopItem[] generatePlayerItems() {
        List<ShopItem> items = new ArrayList<ShopItem>();
        int index = 0;

        for(int i = 0; i < party.getConsumables().size(); i++) {
            items.add(new ConsumableShopItem(x + 460, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getConsumable(party.getConsumables().get(i))));
        }

        for (int i = 0; i < party.getEquipment().size(); i++) {
            items.add(new EquipmentShopItem(x + 460, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getEquipment(party.getEquipment().get(i))));
        }

        return items.toArray(new ShopItem[items.size()]);
    }

    public ShopItem[] generateShopItems() {
        List<ShopItem> items = new ArrayList<ShopItem>();
        int index = 0;

        for(int i = 0; i < shop.getConsumables().size(); i++) {
            items.add(new ConsumableShopItem(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getConsumable(shop.getConsumables().get(i))));
        }

        for(int i = 0; i < shop.getEquipment().size(); i++) {
            items.add(new EquipmentShopItem(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getEquipment(shop.getEquipment().get(i))));
        }

        return items.toArray(new ShopItem[items.size()]);
    }
    
    public void updateItems() {
        currentPlayerItems = generatePlayerItems();
        currentShopItems = generateShopItems();
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
            buyMessageBox.render(batch, patch);
            sellMessageBox.render(batch, patch);

            // Render shop items.
            int page = selectedShopItem / MAX_ITEMS_PER_PAGE;
            int offset = page * MAX_ITEMS_PER_PAGE;

            for(int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentShopItems.length - offset); i++) {
                currentShopItems[i].render(batch, patch);

                if(shopItemsFocus && selectedShopItem == i) {
                    batch.draw(Assets.selectArrow, x, y - (75* (i % MAX_ITEMS_PER_PAGE)) + 30);
                }
            }

            renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentShopItems.length / MAX_ITEMS_PER_PAGE), x, y - 2 * height + 120, Color.WHITE, Assets.consolas16);

            // Render player items.
            page = selectedPlayerItem / MAX_ITEMS_PER_PAGE;
            offset = page * MAX_ITEMS_PER_PAGE;

            for(int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentPlayerItems.length - offset); i++) {
                currentPlayerItems[i].render(batch, patch);

                if(!shopItemsFocus && selectedPlayerItem == i) {
                    batch.draw(Assets.selectArrow, x + 460, y - (75* (i % MAX_ITEMS_PER_PAGE)) + 30);
                }
            }

            renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentPlayerItems.length / MAX_ITEMS_PER_PAGE), x + 460, y - 2 * height + 120, Color.WHITE, Assets.consolas16);
        }

    }

    private void renderText(SpriteBatch batch, String message, float x, float y, Color color, BitmapFont font) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, Align.left, false);

        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, Align.left, false);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }

    /**
     * Makes the UI component visible on screen.
     */
    public void show() {
        selectedPlayerItem = 0;
        selectedShopItem = 0;
        shopItemsFocus = true;
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
            if(shopItemsFocus && selectedShopItem > 0) {
                selectedShopItem--;
            } else if(!shopItemsFocus && selectedPlayerItem > 0) {
                selectedPlayerItem--;
            }
        } else if (InputHandler.isDownJustPressed()) {
            if(shopItemsFocus && selectedShopItem < currentShopItems.length - 1) {
                selectedShopItem++;
            } else if(!shopItemsFocus && selectedPlayerItem < currentPlayerItems.length - 1) {
                selectedPlayerItem++;
            }
        }
        
        if (InputHandler.isLeftJustPressed()) {
            shopItemsFocus = true;
        } else if (InputHandler.isRightJustPressed()) {
        	shopItemsFocus = false;
        }

        if (InputHandler.isActJustPressed()) {
            if(shopItemsFocus) {
                buy();
                selectedShopItem -= selectedShopItem > 0 ? 1 : 0;
            } else {
                sell();
                selectedPlayerItem -= selectedPlayerItem > 0 ? 1 : 0;
            }

            updateItems();
        }
    }

    /**
     * Sells the specified item from currentPlayerItems to the shop.
     */
    private void sell() {
        ShopItem selected = currentPlayerItems.length > 0 ? currentPlayerItems[selectedPlayerItem] : null;

        if(selected == null) {
            return;
        }

        if (selected instanceof ConsumableShopItem) {
            party.getConsumables().remove(selectedPlayerItem);
            shop.getConsumables().add(((ConsumableShopItem) selected).consumable.getID());
        } else if (selected instanceof EquipmentShopItem) {
            party.getEquipment().remove(selectedPlayerItem - party.getConsumables().size());
            shop.getEquipment().add(((EquipmentShopItem) selected).equipment.getID());
        }

        Game.pointsScore += (int) Math.ceil(selected.getCost() * 0.8f);
        System.out.println((int) Math.ceil(selected.getCost() * 0.8f));
    }

    /**
     * Buys the selected item from currentShopItem to the player.
     */
    private void buy() {
        ShopItem selected = currentShopItems.length > 0 ? currentShopItems[selectedShopItem] : null;

        if(selected == null) {
            return;
        }

        System.out.println("Buying index " + selectedShopItem);

        if (selected.getCost() <= Game.pointsScore) {
            if(selected instanceof ConsumableShopItem) {
                // Item is a consumable.
                party.getConsumables().add(((ConsumableShopItem) selected).consumable.getID());
                shop.getConsumables().remove(selectedShopItem);
            } else if (selected instanceof EquipmentShopItem) {
                // Item is equipment.
                party.getEquipment().add(((EquipmentShopItem) selected).equipment.getID());
                // Account for equipment being listed after consumables.
                shop.getEquipment().remove(selectedShopItem - shop.getConsumables().size());
            }

            Game.pointsScore -= selected.getCost();
        }
    }
    
    public abstract class ShopItem extends UIComponent {
        protected final BitmapFont font = Assets.consolas22;
        protected final BitmapFont smallFont = Assets.consolas16;

        protected final float LINE_HEIGHT = 25f;

        protected float paddingX = 20;
        protected float paddingY = 10;

        public ShopItem(float x, float y, float width) {
            super(x, y, width, 55);
        }

        @Override
        public abstract void render(SpriteBatch batch, NinePatch patch);

        public abstract int getCost();

        /**
         * Helper function for render that actually does the rendering.
         * @param batch the spritebatch to use.
         * @param message The string to add.
         * @param x The x location.
         * @param y The y location.
         * @param color The colour to render the text as.
         */
        protected void renderText(SpriteBatch batch, String message, float x, float y, Color color, BitmapFont font) {
            GlyphLayout layout = new GlyphLayout(font, message,
                    Color.BLACK, width - paddingX * 2, Align.left, false);

            font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
            layout.setText(font, message,
                    color, width - paddingX * 2, Align.left, false);
            font.draw(batch, layout, x + paddingX, y + height + paddingY);
        }
    }

    public class ConsumableShopItem extends ShopItem {

        private Consumable consumable;

        public ConsumableShopItem(float x, float y, float width, Consumable consumable) {
            super(x, y, width);

            this.consumable = consumable;
        }

        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, consumable.getName(), x, y, Color.WHITE, font);
            renderText(batch, consumable.getDescription(), x, y - LINE_HEIGHT, Color.LIGHT_GRAY, font);
            renderText(batch, "COST: " + consumable.getCost(), x + 324, y, Color.WHITE, font);
        }

        @Override
        public int getCost() {
            return consumable.getCost();
        }
    }

    public class EquipmentShopItem extends ShopItem {

        private Equipment equipment;
        private String statString;

        public EquipmentShopItem(float x, float y, float width, Equipment equipment) {
            super(x, y, width);

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

            renderText(batch, "LVL " + equipment.getLevelRequirement(), x + 350, y - LINE_HEIGHT, Color.WHITE, font);
            renderText(batch, "COST: " + equipment.getCost(), x + 324, y, Color.WHITE, font);
            renderText(batch, statString, x, y - LINE_HEIGHT * 2, Color.WHITE, smallFont);

            batch.draw(equipment.getType().getTexture(), x + 324, y + 50);
        }

        @Override
        public int getCost() {
            return equipment.getCost();
        }
    }
}
