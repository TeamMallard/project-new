package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages every item in the game.
 */
public class ItemManager {

    private List<Equipment> equipment = new ArrayList<Equipment>();
    private List<Consumable> consumables = new ArrayList<Consumable>();


    public ItemManager() {

        //Add default Empty equipments to first index of each list
        equipment.add(new Equipment("EmptyEquipment", "EmptyEquipment", Equipment.EquipType.WEAPON, 0, 0, 0, 0, 0, 0, 0));
        consumables.add(new Consumable("EmptyConsumable","EmptyConsumable", Consumable.ConsumeType.HEAL,0, 0));
    }

    /**
     * Adds a new equipment item.
     * @param equipment The Equipment to add.
     */
    public void addEquipment(Equipment equipment) {

        equipment.updateID(this.equipment.size());
        this.equipment.add(equipment);//Check size function
    }

    /**
     * Returns the Equipment stored at the given index.
     * @param itemID The index of the Equipment to retrieve.
     * @return An Equipment
     */
    public Equipment getEquipment(int itemID) {
        return equipment.get(itemID);
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
                "equipment=" + equipment +
                ", consumables=" + consumables +
                '}';
    }
}
