package com.mygdx.game;

import java.util.List;

/**
 * Represents a shop in the game with a stock of consumable and equipment items.
 */
public class Shop {

    /**
     * The ID of this Shop.
     */
    private int id;

    /**
     * The list of equipment that this Shop has in stock.
     */
    private List<Integer> equipment;

    /**
     * The list of consumables that this Shop has in stock.
     */
    private List<Integer> consumables;

    /**
     * Creates a new Shop with the specified parameters.
     *
     * @param equipment   the equipment that this Shop has in stock
     * @param consumables the consumables that this Shop has in stock
     */
    public Shop(List<Integer> equipment, List<Integer> consumables) {
        this.equipment = equipment;
        this.consumables = consumables;
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public Shop() {
    }

    /**
     * Changes the ID of this Shop.
     *
     * @param id the new ID
     */
    public void updateID(int id) {
        this.id = id;
    }

    /**
     * @return the ID of this Shop
     */
    public int getID() {
        return id;
    }

    /**
     * @return the list of equipment that this Shop has in stock
     */
    public List<Integer> getEquipment() {
        return equipment;
    }

    /**
     * @return the list of consumables that this Shop has in stock
     */
    public List<Integer> getConsumables() {
        return consumables;
    }
}
