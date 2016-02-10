package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages every item in the game.
 */
public class ItemManager {

    private List<Equipable> equipables = new ArrayList<Equipable>();
    private List<Consumable> consumables = new ArrayList<Consumable>();


    public ItemManager() {

        //Add default Empty equipments to first index of each list
        equipables.add(new Equipable("EmptyEquipment", "EmptyEquipment", Equipable.EquipType.WEAPON, 0, 0, 0, 0, 0, 0, 0));
        consumables.add(new Consumable("EmptyConsumable","EmptyConsumable", Consumable.ConsumeType.HEAL,0, 0));
    }

    /**
     * Adds a new equipable item.
     * @param equipable The Equipable to add.
     */
    public void addEquipable(Equipable equipable) {

        equipable.updateID(equipables.size());
        equipables.add(equipable);//Check size function
    }

    /**
     * Returns the Equipable stored at the given index.
     * @param itemID The index of the Equipable to retrieve.
     * @return An Equipable
     */
    public Equipable getEquipable(int itemID) {
        return equipables.get(itemID);
    }

    /**
     * Adds a new consumable item.
     */
    public void addConsumable(Consumable consumable) {

        consumable.updateID(consumables.size());
        consumables.add(consumable);//Check size function
    }

    /**
     * Returns the Consumable stored at the given index.
     * @param itemID The index of the Consumable to retrieve.
     * @return A Consumable
     */
    public Consumable getConsumable(int itemID) {
        return consumables.get(itemID);
    }

    @Override
    public String toString() {
        return "ItemManager{" +
                "equipables=" + equipables +
                ", consumables=" + consumables +
                '}';
    }
}
