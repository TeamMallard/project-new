package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * CHANGE I6: Added the shop interface in full.
 */

/**
 * Represents the shop where the player can buy/sell items.
 */
public class UIShop extends UIComponent {

    /**
     * The maximum number of items to render on one page.
     */
    public static final int MAX_ITEMS_PER_PAGE = 5;

    /**
     * How much the cost of each item is multiplied by when selling to the shop.
     */
    public static final float SELLING_PRICE_MULTIPLIER = 0.8f;

    /**
     * The party.
     */
    private PartyManager party;

    /**
     * The shop this UIShop represents.
     */
    private Shop shop;

    /**
     * Whether the shop is being displayed.
     */
    private boolean show;

    /**
     * Whether the current focus is on the list of shop items instead of player items.
     */
    private boolean shopItemsFocus = true;

    /**
     * Current items held by the player.
     */
    private ShopItem[] currentPlayerItems;

    /**
     * Current items held by the shop.
     */
    private ShopItem[] currentShopItems;

    /**
     * Currently selected player/shop item.
     */
    private int selectedPlayerItem = 0, selectedShopItem = 0;
    /**
     * Label for the buy column.
     */
    private UIMessageBox buyMessageBox = new UIMessageBox("BUY", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x, y + 75, width / 6, 0, 10);

    /**
     * Label for the sell column.
     */
    private UIMessageBox sellMessageBox = new UIMessageBox("SELL", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + 460, y + 75, width / 6, 0, 10);

    /**
     * Creates a new UIShop with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the shop
     * @param height the height of the shop
     * @param party  the party
     * @param shop   the shop this UIShop represents
     */
    public UIShop(float x, float y, float width, float height, PartyManager party, Shop shop) {
        super(x, y, width, height);
        this.party = party;
        this.shop = shop;
        updateItems();

        show = true;
    }

    /**
     * Generates a list of items to show in the sell column based on the party's inventory.
     *
     * @return the list of items for the party
     */
    private ShopItem[] generatePlayerItems() {
        List<ShopItem> items = new ArrayList<ShopItem>();
        int index = 0;

        // Add consumables.
        for (int i = 0; i < party.getConsumables().size(); i++) {
            items.add(new ConsumableShopItem(x + 460, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getConsumable(party.getConsumables().get(i)), true));
        }

        // Add equipment.
        for (int i = 0; i < party.getEquipment().size(); i++) {
            items.add(new EquipmentShopItem(x + 460, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getEquipment(party.getEquipment().get(i)), true));
        }

        return items.toArray(new ShopItem[items.size()]);
    }

    /**
     * Generates a list of items to show in the buy column based on the shop's stock.
     *
     * @return the list of items for the shop
     */
    private ShopItem[] generateShopItems() {
        List<ShopItem> items = new ArrayList<ShopItem>();
        int index = 0;

        // Add consumables.
        for (int i = 0; i < shop.getConsumables().size(); i++) {
            items.add(new ConsumableShopItem(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getConsumable(shop.getConsumables().get(i)), false));
        }

        // Add equipment.
        for (int i = 0; i < shop.getEquipment().size(); i++) {
            items.add(new EquipmentShopItem(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), 450, Game.items.getEquipment(shop.getEquipment().get(i)), false));
        }

        return items.toArray(new ShopItem[items.size()]);
    }

    /**
     * Updates the lists of items on display.
     */
    private void updateItems() {
        currentPlayerItems = generatePlayerItems();
        currentShopItems = generateShopItems();
    }

    /**
     * Renders this UIShop onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        if (show) {
            patch.draw(batch, x, y - height + 110, width, height);
            buyMessageBox.setColor(Color.WHITE);
            sellMessageBox.setColor(Color.WHITE);
            buyMessageBox.render(batch, patch);
            sellMessageBox.render(batch, patch);

            // Render shop items.
            int page = selectedShopItem / MAX_ITEMS_PER_PAGE;
            int offset = page * MAX_ITEMS_PER_PAGE;

            for (int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentShopItems.length - offset); i++) {
                currentShopItems[i].render(batch, patch);

                if (shopItemsFocus && selectedShopItem == i) {
                    batch.draw(Assets.selectArrow, x, y - (75 * (i % MAX_ITEMS_PER_PAGE)) + 30);
                }
            }

            renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentShopItems.length / MAX_ITEMS_PER_PAGE), x, y - 2 * height + 120, 20, 10, Color.WHITE, Assets.consolas16);

            // Render player items.
            page = selectedPlayerItem / MAX_ITEMS_PER_PAGE;
            offset = page * MAX_ITEMS_PER_PAGE;

            for (int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentPlayerItems.length - offset); i++) {
                currentPlayerItems[i].render(batch, patch);

                if (!shopItemsFocus && selectedPlayerItem == i) {
                    batch.draw(Assets.selectArrow, x + 460, y - (75 * (i % MAX_ITEMS_PER_PAGE)) + 30);
                }
            }

            renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentPlayerItems.length / MAX_ITEMS_PER_PAGE), x + 460, y - 2 * height + 120, 20, 10, Color.WHITE, Assets.consolas16);
        }

    }

    /**
     * Updates the state of this UIShop.
     *
     * @return returns true if the shop should continue to be displayed
     */
    public boolean update() {
        if (InputHandler.isEscJustPressed()) {
            show = false;
            return false;
        } else {
            optionUpdate();
            return true;
        }
    }

    /**
     * Handles user input to update which shop item is currently selected.
     */
    private void optionUpdate() {
        if (InputHandler.isUpJustPressed()) {
            if (shopItemsFocus && selectedShopItem > 0) {
                selectedShopItem--;
            } else if (!shopItemsFocus && selectedPlayerItem > 0) {
                selectedPlayerItem--;
            }
        } else if (InputHandler.isDownJustPressed()) {
            if (shopItemsFocus && selectedShopItem < currentShopItems.length - 1) {
                selectedShopItem++;
            } else if (!shopItemsFocus && selectedPlayerItem < currentPlayerItems.length - 1) {
                selectedPlayerItem++;
            }
        }

        if (InputHandler.isLeftJustPressed()) {
            shopItemsFocus = true;
        } else if (InputHandler.isRightJustPressed()) {
            shopItemsFocus = false;
        }

        if (InputHandler.isActJustPressed()) {
            if (shopItemsFocus) {
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
     * Sells the currently selected item from currentPlayerItems to the shop.
     */
    private void sell() {
        ShopItem selected = currentPlayerItems.length > 0 ? currentPlayerItems[selectedPlayerItem] : null;

        if (selected == null) {
            return;
        }

        if (selected instanceof ConsumableShopItem) {
            party.getConsumables().remove(selectedPlayerItem);
            shop.getConsumables().add(((ConsumableShopItem) selected).consumable.getID());
        } else if (selected instanceof EquipmentShopItem) {
            party.getEquipment().remove(selectedPlayerItem - party.getConsumables().size());
            shop.getEquipment().add(((EquipmentShopItem) selected).equipment.getID());
        }

        Game.pointsScore += getSellingPrice(selected.getCost());
    }

    /**
     * Buys the currently selected item from currentShopItem to the player.
     */
    private void buy() {
        ShopItem selected = currentShopItems.length > 0 ? currentShopItems[selectedShopItem] : null;

        if (selected == null) {
            return;
        }

        if (selected.getCost() <= Game.pointsScore) {
            if (selected instanceof ConsumableShopItem) {
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

    private static int getSellingPrice(int cost) {
        return (int) Math.ceil(cost * SELLING_PRICE_MULTIPLIER);
    }

    /**
     * Represents an item listed in the shop.
     */
    private abstract class ShopItem extends UIComponent {
        /**
         * How tall one line of text is.
         */
        protected final float LINE_HEIGHT = 25f;

        /**
         * Text padding.
         */
        protected float paddingX = 20, paddingY = 10;

        /**
         * Whether this item is for selling to the shop.
         */
        protected boolean selling;

        /**
         * Creates a new ShopItem with the specified parameters.
         *
         * @param x     the x coordinate
         * @param y     the y coordinate
         * @param width the width of the item
         */
        public ShopItem(float x, float y, float width, boolean selling) {
            super(x, y, width, 55);
            this.selling = selling;
        }

        /**
         * Renders this ShopItem onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public abstract void render(SpriteBatch batch, NinePatch patch);

        /**
         * @return the cost of this ShopItem in points
         */
        public abstract int getCost();
    }

    /**
     * Represents a shop item of a consumable.
     */
    private class ConsumableShopItem extends ShopItem {

        /**
         * The consumable this ConsumableShopItem represents.
         */
        private Consumable consumable;

        /**
         * Creates a new ConsumableShopItem with the specified parameters.
         *
         * @param x          the x coordinate
         * @param y          the y coordinate
         * @param width      the width of the item
         * @param consumable the consumable this ConsumableShopItem represents
         */
        public ConsumableShopItem(float x, float y, float width, Consumable consumable, boolean selling) {
            super(x, y, width, selling);

            this.consumable = consumable;
        }

        /**
         * Renders this ConsumableShopItem onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            int cost = selling ? getSellingPrice(getCost()) : getCost();

            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, consumable.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, consumable.getDescription(), x, y - LINE_HEIGHT, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
            renderText(batch, (selling ? "SELL: " : "BUY: ") + cost, x + 324, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
        }

        /**
         * @return the cost of this ConsumableShopItem in points
         */
        @Override
        public int getCost() {
            return consumable.getCost();
        }
    }

    /**
     * Represents a shop item of a piece of equipment.
     */
    private class EquipmentShopItem extends ShopItem {

        /**
         * The equipment this EquipmentShopItem represents.
         */
        private Equipment equipment;

        /**
         * String describing the stats for this EquipmentShopItem.
         */
        private String statString;

        /**
         * Creates a new EquipmentShopItem with the specified parameters.
         *
         * @param x         the x coordinate
         * @param y         the y coordinate
         * @param width     the width of the item
         * @param equipment the equipment this EquipmentShopItem represents
         */
        public EquipmentShopItem(float x, float y, float width, Equipment equipment, boolean selling) {
            super(x, y, width, selling);

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

        /**
         * Renders this EquipmentShopItem onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            int cost = selling ? getSellingPrice(getCost()) : getCost();

            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, equipment.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, equipment.getDescription(), x, y - LINE_HEIGHT, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
            renderText(batch, (selling ? "SELL: " : "BUY: ") + cost, x + 324, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, statString, x, y - LINE_HEIGHT * 2, paddingX, paddingY, Color.WHITE, Assets.consolas16);

            batch.draw(equipment.getType().getTexture(), x + 324, y + 50);
        }

        /**
         * @return the cost of this EquipmentShopItem in points
         */
        @Override
        public int getCost() {
            return equipment.getCost();
        }
    }
}
