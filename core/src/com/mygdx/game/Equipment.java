package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.assets.Assets;

import java.util.Arrays;

/**
 * Class represents an individual item of equipment, id's are assigned by the order of the equipment in the ItemManager's list.
 * The Modifiers array stores the statistic modifiers this equipment piece gives in the order: Speed, Strength, Dexterity, Intelligence, ArmourValue.
 */
public class Equipment {

    private int id = -1;
    private String name, description;
    private EquipType type;
    private int[] modifiers = new int[5];//Ordered: speed, strength, dexterity, intelligence, armourVal
    private int levelRequirement;
    private int cost;

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", modifiers=" + Arrays.toString(modifiers) +
                ", levelRequirement=" + levelRequirement +
                '}';
    }

    /**
     * Constructor for a new Equipment.
     * @param name The name of the Equipment.
     * @param description The description of the Equipment.
     * @param type The type of the Equipment.
     * @param speedModifier The speedModifier.
     * @param strengthModifier The strengthModifier.
     * @param dexterityModifier The dexterityModifier.
     * @param intelligenceModifier The intelligenceModifier.
     * @param armourValModifier The armourValue Modifier.
     * @param levelRequirement The level requirement of the Equipment.
     * @param cost
     */
    public Equipment(String name, String description, EquipType type, int speedModifier, int strengthModifier, int dexterityModifier, int intelligenceModifier, int armourValModifier, int levelRequirement, int cost) {
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
    public Equipment() {
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
        this.id = id;
    }


    public int getID() {
        return id;
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

        public TextureRegion getTexture() {
            return Assets.equipment[slot];
        }
    }
}