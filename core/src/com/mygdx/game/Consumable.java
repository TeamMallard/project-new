package com.mygdx.game;

/**
 * Represents a consumable item.
 */
public class Consumable {

    /**
     * The ID of this consumable.
     */
    private int id = -1;

    /**
     * The name and description of this Consumable.
     */
    private String name, description;

    /**
     * The type of effect this Consumable has.
     */
    private ConsumeType type;

    /**
     * The power of this Consumable.
     */
    private int power;

    /**
     * How much this Consumable costs in the shop.
     */
    private int cost;

    /**
     * Creates a new Consumable with the specified parameters.
     *
     * @param name        the name of this Consumable
     * @param description the description of this Consumable
     * @param type        the type of effect this Consumable has
     * @param power       the power of this Consumable
     * @param cost        how much this Consumable costs in the shop
     */
    public Consumable(String name, String description, ConsumeType type, int power, int cost) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.power = power;
        this.cost = cost;
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public Consumable() {
    }

    /**
     * Changes the ID of this Consumable.
     *
     * @param id the new ID
     */
    public void updateID(int id) {
        this.id = id;
    }

    /**
     * @return the name of this Consumable
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of this Consumable
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the type of effect this Consumable has
     */
    public ConsumeType getType() {
        return type;
    }

    /**
     * @return the power of this Consumable
     */
    public int getPower() {
        return power;
    }

    /**
     * @return the ID of this Consumable
     */
    public int getID() {
        return id;
    }

    /**
     * @return how much this Consumable costs in the shop
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return a string representation of this Consumable
     */
    @Override
    public String toString() {
        return "Consumable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", power=" + power +
                ", cost=" + cost +
                '}';
    }

    /**
     * Represents which type of effect a consumable item has.
     */
    public enum ConsumeType {
        HEAL, REVIVE, MANA
    }
}
