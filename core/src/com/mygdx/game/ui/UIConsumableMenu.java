package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of consumable items in the overworld.
 */
public class UIConsumableMenu extends UIComponent {

    /**
     * The maximum number of items to render on one page.
     */
    public static final int MAX_ITEMS_PER_PAGE = 5;

    /**
     * The party menu this UIConsumableMenu belongs to.
     */
    private UIPartyMenu parent;

    /**
     * The party.
     */
    private PartyManager partyManager;

    /**
     * Which player/consumable is currently selected.
     */
    private int selectedPlayer = 0, selectedConsumable = 0;

    /**
     * Whether or not input is currently focused on this component.
     */
    private boolean hasFocus = false;

    /**
     * The current list of consumables to display.
     */
    private Item[] currentConsumable;

    /**
     * Creates a new UIConsumableMenu with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the menu
     * @param height the height of the menu
     * @param parent the party menu this UIConsumableMenu belongs to
     */
    public UIConsumableMenu(float x, float y, float width, float height, UIPartyMenu parent) {
        super(x, y, width, height);
        this.parent = parent;
        this.partyManager = parent.getParty();

        updateConsumable();
    }

    /**
     * Renders this UIConsumableMenu onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        int page = selectedConsumable / MAX_ITEMS_PER_PAGE;
        int offset = page * MAX_ITEMS_PER_PAGE;

        for (int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentConsumable.length - offset); i++) {
            currentConsumable[i].render(batch, patch);

            if (hasFocus && selectedConsumable == i) {
                batch.draw(Assets.selectArrow, x, y - (75 * (i % MAX_ITEMS_PER_PAGE)) + 30);
            }
        }

        renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentConsumable.length / MAX_ITEMS_PER_PAGE), x + 20, y - 3 * height - 10, 0, 0, Color.WHITE, Assets.consolas16);
    }

    /**
     * Updates the list of consumables to display.
     */
    private void updateConsumable() {
        List<Item> consumable = new ArrayList<Item>();
        int index = 0;

        for (Integer consumableId : partyManager.getConsumables()) {
            consumable.add(new Item(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), width, Game.items.getConsumable(consumableId)));
        }

        currentConsumable = consumable.toArray(new Item[consumable.size()]);
        selectedConsumable = 0;
    }

    /**
     * Sets the currently selected player.
     *
     * @param index the index of the selected player
     */
    public void selectPlayer(int index) {
        this.selectedPlayer = index;

        updateConsumable();
    }

    /**
     * @return the agent representing the currently selected player
     */
    private Agent getSelectedPlayer() {
        return partyManager.getMember(selectedPlayer);
    }

    /**
     * Focuses input on this UIConsumableMenu.
     */
    public void focus() {
        hasFocus = true;
    }

    /**
     * @return whether or not input is currently focused on this component
     */
    public boolean hasFocus() {
        return hasFocus;
    }

    /**
     * Updates the state of this UIConsumableMenu.
     */
    public void update() {
        if (InputHandler.isUpJustPressed() && selectedConsumable > 0) {
            selectedConsumable--;
        } else if (InputHandler.isDownJustPressed() && selectedConsumable < currentConsumable.length - 1) {
            selectedConsumable++;
        }

        if (InputHandler.isActJustPressed()) {
            UIManager uiManager = Game.worldScreen.getGameWorld().uiManager;
            Consumable item = currentConsumable[selectedConsumable].consumable;
            switch (item.getType()) {
                case HEAL: {
                    if (!getSelectedPlayer().isDead()) {
                        Assets.sfx_healNoise.play(Game.masterVolume);
                        this.getSelectedPlayer().dealHealth(item.getPower());
                        partyManager.getConsumables().remove(selectedConsumable);
                        uiManager.addNotification(getSelectedPlayer().getName() + " is healed for " + item.getPower() + " health");
                    } else {
                        uiManager.addNotification(getSelectedPlayer().getName() + " cannot be healed");
                    }
                    break;
                }
                case REVIVE: {
                    if (getSelectedPlayer().isDead()) {
                        Assets.sfx_healNoise.play(Game.masterVolume);
                        this.getSelectedPlayer().dealHealth(item.getPower());
                        partyManager.getConsumables().remove(selectedConsumable);
                        uiManager.addNotification(getSelectedPlayer().getName() + " is revived on " + item.getPower() + " health");
                    } else {
                        uiManager.addNotification(getSelectedPlayer().getName() + " cannot be revived");
                    }
                    break;
                }
                case MANA: {
                    if (!getSelectedPlayer().isDead()) {
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
        if (currentConsumable.length == 0) {
            hasFocus = false;
            parent.focus();
        }

    }

    /**
     * Represents an item in the UIConsumableMenu.
     */
    private class Item extends UIComponent {

        /**
         * The consumable this Item represents.
         */
        private Consumable consumable;

        /**
         * How tall one line of text is.
         */
        private final float LINE_HEIGHT = 25f;

        /**
         * Text padding.
         */
        private float paddingX = 20, paddingY = 10;

        /**
         * Creates a new Item with the specified parameters.
         *
         * @param x          the x coordinate
         * @param y          the y coordinate
         * @param width      the width of the menu
         * @param consumable the consumable this Item represents
         */
        public Item(float x, float y, float width, Consumable consumable) {
            super(x, y, width, 55);
            this.consumable = consumable;
        }

        /**
         * Renders this Item onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, consumable.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, consumable.getDescription(), x, y - LINE_HEIGHT, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
        }
    }
}
