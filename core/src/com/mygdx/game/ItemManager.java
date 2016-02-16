package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains every item accessible in the game.
 */
public class ItemManager {

    /**
     * The complete list of equipment available in the game.
     */
    private List<Equipment> equipment = new ArrayList<Equipment>();

    /**
     * The complete list of consumables accessible in the game.
     */
    private List<Consumable> consumables = new ArrayList<Consumable>();

    /**
     * Creates a new ItemManager with the default empty items.
     */
    public ItemManager() {
        equipment.add(new Equipment("EmptyEquipment", "EmptyEquipment", Equipment.EquipType.WEAPON, 0, 0, 0, 0, 0, 0));
        consumables.add(new Consumable("EmptyConsumable", "EmptyConsumable", Consumable.ConsumeType.HEAL, 0, 0));
    }

    /**
     * Returns the equipment item with the specified ID.
     *
     * @param itemID the ID of the equipment
     * @return the equipment
     */
    public Equipment getEquipment(int itemID) {
        return equipment.get(itemID);
    }

    /**
     * Returns the consumable item with the specified ID.
     *
     * @param itemID the ID of the consumable
     * @return the consumable
     */
    public Consumable getConsumable(int itemID) {
        return consumables.get(itemID);
    }

    /**
     * @return a string representation of this ItemManager
     */
    @Override
    public String toString() {
        return "ItemManager{" +
                "equipment=" + equipment +
                ", consumables=" + consumables +
                '}';
    }
}
