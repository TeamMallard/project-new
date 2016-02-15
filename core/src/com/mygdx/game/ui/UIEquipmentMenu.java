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
 * Represents the list of equipment items in the overworld.
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

        for(int i = offset; i < offset + Math.min(MAX_ITEMS_PER_PAGE, currentEquipment.length - offset); i++) {
            currentEquipment[i].render(batch, patch);

            if(hasFocus && selectedEquipment == i) {
                batch.draw(Assets.selectArrow, x, y - (75* (i % MAX_ITEMS_PER_PAGE)) + 30);
            }
        }

        renderText(batch, "Page " + (page + 1) + " of " + (int) Math.ceil((float) currentEquipment.length / MAX_ITEMS_PER_PAGE), x + 20, y - 2 * height - 10, 0, 0, Color.WHITE, Assets.consolas16);
    }

    private void updateEquipment() {
        List<Item> equipment = new ArrayList<Item>();
        int index = 0;

        // Add player's current equipment.
        for(int i = 0; i < getSelectedPlayer().getCurrentEquipment().equipSlots.length; i++) {
            int equipmentId = getSelectedPlayer().getCurrentEquipment().equipSlots[i];

            if(equipmentId != 0) {
                equipment.add(new Item(x, y - (75 * index++), width, Game.items.getEquipment(equipmentId), true));
            }
        }

        // Add rest of equipment in inventory.
        for(Integer equipmentId : partyManager.getEquipment()) {
            equipment.add(new Item(x, y - (75 * (index++ % MAX_ITEMS_PER_PAGE)), width, Game.items.getEquipment(equipmentId), false));
        }

        currentEquipment = equipment.toArray(new Item[equipment.size()]);
        selectedEquipment = 0;
    }

    public void selectPlayer(int index) {
        this.selectedPlayer = index;

        updateEquipment();
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
        if(InputHandler.isUpJustPressed() && selectedEquipment > 0) {
            selectedEquipment--;
        } else if(InputHandler.isDownJustPressed() && selectedEquipment < currentEquipment.length - 1) {
            selectedEquipment++;
        }

        if(InputHandler.isActJustPressed()) {
            setPlayerEquipment(partyManager.getMember(selectedPlayer), selectedEquipment);
            selectedEquipment -= selectedEquipment > 0 ? 1 : 0;

            updateEquipment();

            selectedEquipment = 0;
            hasFocus = false;
            parent.focus();
        }
        if(currentEquipment.length == 0) {
            hasFocus = false;
            parent.focus();
        }
    }

    private void setPlayerEquipment(Agent player, int selectedEquipment)
    {
        Item selected = currentEquipment[selectedEquipment];
        Equipment.EquipType slot = selected.equipment.getType();

        System.out.println("EQUIPPING " + selected.equipment.getName());
        System.out.println("FROM SLOT " + selectedEquipment);

        if(selected.equipped) {

            System.out.println("ALREADY EQUIPPED");
            // Already equipped, unequip.
            player.getCurrentEquipment().unequip(slot);
            partyManager.getEquipment().add(selected.equipment.getID());
        } else {
            System.out.println("NOT EQUIPPED");

            // Remove the appropraite item from the party's equipment.
            partyManager.getEquipment().remove(selectedEquipment - player.getCurrentEquipment().getNumberEquipped());

            // If player already has an item in the slot, remove it.
            if(player.getCurrentEquipment().isEquipped(slot)) {
                System.out.println("REPLACING");
                partyManager.getEquipment().add(player.getCurrentEquipment().unequip(slot));
            }

            // Equip the item.
            player.getCurrentEquipment().equip(selected.equipment.getID());
        }
    }

    public class Item extends UIComponent {
        private BitmapFont font = Assets.consolas22;
        private BitmapFont smallFont = Assets.consolas16;

        private Equipment equipment;
        private boolean equipped;

        private final float LINE_HEIGHT = 25f;

        private float paddingX = 20;
        private float paddingY = 10;

        private String statString;

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

        @Override
        public void render(SpriteBatch batch, NinePatch patch) {
            patch.draw(batch, x, y, width, height + (paddingY * 2));
            renderText(batch, equipment.getName(), x, y, paddingX, paddingY, Color.WHITE, font);
            renderText(batch, equipment.getDescription(), x, y - LINE_HEIGHT, paddingX, paddingY, Color.LIGHT_GRAY, font);

            renderText(batch, "LVL " + equipment.getLevelRequirement(), x + 340, y, paddingX, paddingY, Color.WHITE, font);
            renderText(batch, statString, x, y - LINE_HEIGHT * 2, paddingX, paddingY, Color.WHITE, smallFont);

            batch.draw(equipment.getType().getTexture(), x + width - 70, y + 20);

            if(equipped) {
                batch.draw(Assets.shield, x + width - 50, y + 20);
            }
        }
    }
}
