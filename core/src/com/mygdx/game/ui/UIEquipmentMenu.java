package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of equipment items (including equipped ones) in the overworld.
 */
public class UIEquipmentMenu extends UIComponent {

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
     * Which player/equipment is currently selected.
     */
    private int selectedPlayer = 0, selectedEquipment = 0;

    /**
     * Whether or not input is currently focused on this component.
     */
    private boolean hasFocus = false;

    /**
     * The current list of equipment to display.
     */
    private Item[] currentEquipment;

    /**
     * Creates a new UIConsumableMenu with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the menu
     * @param height the height of the menu
     * @param parent the party menu this UIEquipmentMenu belongs to
     */
    public UIEquipmentMenu(float x, float y, float width, float height, UIPartyMenu parent) {
        super(x, y, width, height);

        this.parent = parent;
        this.partyManager = parent.getParty();

        updateEquipment();
    }

    /**
     * Renders this UIEquipmentMenu onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        int page = selectedEquipment / MAX_ITEMS_PER_PAGE;
        int offset = page * MAX_ITEMS_PER_PAGE;

        for (int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentEquipment.length - offset); i++) {
            currentEquipment[i].render(batch, patch);

            if (hasFocus && selectedEquipment == i) {
                batch.draw(Assets.selectArrow, x, y - (75 * (i % MAX_ITEMS_PER_PAGE)) + 30);
            }
        }

        renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentEquipment.length / MAX_ITEMS_PER_PAGE), x + 20, y - 2 * height - 10, 0, 0, Color.WHITE, Assets.consolas16);
    }

    /**
     * Updates the list of equipment to display.
     */
    private void updateEquipment() {
        List<Item> equipment = new ArrayList<Item>();
        int index = 0;

        // Add player's current equipment.
        for (int i = 0; i < getSelectedPlayer().getCurrentEquipment().equipSlots.length; i++) {
            int equipmentId = getSelectedPlayer().getCurrentEquipment().equipSlots[i];

            if (equipmentId != 0) {
                equipment.add(new Item(x, y - (75 * index++), width, Game.items.getEquipment(equipmentId), true));
            }
        }

        // Add rest of equipment in inventory.
        for (Integer equipmentId : partyManager.getEquipment()) {
            equipment.add(new Item(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), width, Game.items.getEquipment(equipmentId), false));
        }

        currentEquipment = equipment.toArray(new Item[equipment.size()]);
        selectedEquipment = 0;
    }

    /**
     * Sets the currently selected player.
     *
     * @param index the index of the selected player
     */
    public void selectPlayer(int index) {
        this.selectedPlayer = index;

        updateEquipment();
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
        if (InputHandler.isUpJustPressed() && selectedEquipment > 0) {
            selectedEquipment--;
        } else if (InputHandler.isDownJustPressed() && selectedEquipment < currentEquipment.length - 1) {
            selectedEquipment++;
        }

        if (InputHandler.isActJustPressed()) {
            setPlayerEquipment(partyManager.getMember(selectedPlayer), selectedEquipment);
            selectedEquipment -= selectedEquipment > 0 ? 1 : 0;

            updateEquipment();

            selectedEquipment = 0;
            hasFocus = false;
            parent.focus();
        }
        if (currentEquipment.length == 0) {
            hasFocus = false;
            parent.focus();
        }
    }

    /**
     * Equips/unequips the specified selected equipment from the specified agent.
     *
     * @param player            the currently selected player
     * @param selectedEquipment the currently selected equipment
     */
    private void setPlayerEquipment(Agent player, int selectedEquipment) {
        Item selected = currentEquipment[selectedEquipment];
        Equipment.EquipType slot = selected.equipment.getType();

        if (selected.equipped) {
            // Already equipped, unequip.
            player.getCurrentEquipment().unequip(slot);
            partyManager.getEquipment().add(selected.equipment.getID());
        } else {
            // Remove the appropraite item from the party's equipment.
            partyManager.getEquipment().remove(selectedEquipment - player.getCurrentEquipment().getNumberEquipped());

            // If player already has an item in the slot, remove it.
            if (player.getCurrentEquipment().isEquipped(slot)) {
                partyManager.getEquipment().add(player.getCurrentEquipment().unequip(slot));
            }

            // Equip the item.
            player.getCurrentEquipment().equip(selected.equipment.getID());
        }
    }

    /**
     * Represents an item of equipment in the UIEquipmentMenu.
     */
    private class Item extends UIComponent {

        /**
         * The equipment this Item represents.
         */
        private Equipment equipment;

        /**
         * Whether this Item represents equipment that is currently equipped.
         */
        private boolean equipped;

        /**
         * How tall one line of text is.
         */
        private final float LINE_HEIGHT = 25f;

        /**
         * Text padding.
         */
        private float paddingX = 20, paddingY = 10;

        /**
         * String describing the stats for this Item.
         */
        private String statString;

        /**
         * Creates a new Item with the specified parameters.
         *
         * @param x         the x coordinate
         * @param y         the y coordinate
         * @param width     the width of the menu
         * @param equipment the equipment this Item represents
         * @param equipped  whether this Item represents equipment that is currently equipped
         */
        public Item(float x, float y, float width, Equipment equipment, boolean equipped) {
            super(x, y, width, 55);
            this.equipment = equipment;
            this.equipped = equipped;

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
         * Renders this Item onto the specified sprite batch.
         *
         * @param batch the sprite batch to render on
         * @param patch the nine patch for drawing boxes
         */
        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, equipment.getName(), x, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, equipment.getDescription(), x, y - LINE_HEIGHT, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);

            renderText(batch, "LVL " + equipment.getLevelRequirement(), x + 340, y, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            renderText(batch, statString, x, y - LINE_HEIGHT * 2, paddingX, paddingY, Color.WHITE, Assets.consolas16);

            batch.draw(equipment.getType().getTexture(), x + width - 70, y + 20);

            if (equipped) {
                batch.draw(Assets.shield, x + width - 50, y + 20);
            }
        }
    }
}
