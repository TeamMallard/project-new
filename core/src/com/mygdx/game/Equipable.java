package com.mygdx.game;

import java.util.Arrays;

/**
 * Class represents an individual equipable item, ID's are assigned by the order of the equipment in the ItemManager's list.
 * The Modifiers array stores the statistic modifiers this equipment piece gives in the order: Speed, Strength, Dexterity, Intelligence, ArmourValue.
 */
public class Equipable {

    private int ID = -1;
    private String name, description;
    private EquipType type;
    private int[] modifiers = new int[5];//Ordered: speed, strength, dexterity, intelligence, armourVal
    private int levelRequirement;
    private int cost;

    @Override
    public String toString() {
        return "Equipable{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", modifiers=" + Arrays.toString(modifiers) +
                ", levelRequirement=" + levelRequirement +
                '}';
    }

    /**
     * Constructor for a new Equipable.
     * @param name The name of the Equipable.
     * @param description The description of the Equipable.
     * @param type The type of the Equipable.
     * @param speedModifier The speedModifier.
     * @param strengthModifier The strengthModifier.
     * @param dexterityModifier The dexterityModifier.
     * @param intelligenceModifier The intelligenceModifier.
     * @param armourValModifier The armourValue Modifier.
     * @param levelRequirement The level requirement of the Equipable.
     * @param cost
     */
    public Equipable(String name, String description, EquipType type, int speedModifier, int strengthModifier, int dexterityModifier, int intelligenceModifier, int armourValModifier, int levelRequirement, int cost) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.modifiers[0] = speedModifier;
        this.modifiers[1] = strengthModifier;
        this.modifiers[2] = dexterityModifier;
        this.modifiers[3] = intelligenceModifier;
        this.modifiers[4] = armourValModifier;
        this.levelRequirement = levelRequirement;
        this.cost = cost;
    }

    /**
     * Used for generating classes from json.
     */
    public Equipable() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //Used in the CurrentEquipment method, totalStatModifiers. Simply returns modifiers array
    public int[] getModifiers() {
        return modifiers;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public void updateID(int id) {
        this.ID = id;
    }


    public int getID() {
        return ID;
    }

    public EquipType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    /**
     * Represents each different type of equipment.
     */
    public enum EquipType {
        HEAD (0),
        CHEST (1),
        FEET (2),
        ACCESSORY (3),
        WEAPON (4);

        private final int slot;

        EquipType(int slot) {
            this.slot = slot;
        }

        public int slot() {
            return slot;
        }
    }
}