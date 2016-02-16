package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.assets.Assets;

import java.util.Arrays;

/**
 * Represents an item of equipment.
 */
public class Equipment {

    /**
     * The ID of this Equipment.
     */
    private int id = -1;

    /**
     * The name and description of this Equipment.
     */
    private String name, description;

    /**
     * The type of equipment.
     */
    private EquipType type;

    /**
     * The stat modifiers of this Equipment (in order: speed, strength, dexterity, intelligence, defence).
     */
    private int[] modifiers = new int[5];

    /**
     * How much this Equipment item costs in the shop.
     */
    private int cost;

    /**
     * Creates a new Equipment with the specified parameters.
     *
     * @param name                 the name of this Equipment
     * @param description          the description of this Equipment
     * @param type                 the type of Equipment
     * @param speedModifier        the speed modifier
     * @param strengthModifier     the strength modifier
     * @param dexterityModifier    the dexterity modifier
     * @param intelligenceModifier the intelligence modifier
     * @param armourValModifier    the defence modifier
     * @param cost                 how much this Equipment costs in the shop
     */
    public Equipment(String name, String description, EquipType type, int speedModifier, int strengthModifier, int dexterityModifier, int intelligenceModifier, int armourValModifier, int cost) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.modifiers[0] = speedModifier;
        this.modifiers[1] = strengthModifier;
        this.modifiers[2] = dexterityModifier;
        this.modifiers[3] = intelligenceModifier;
        this.modifiers[4] = armourValModifier;
        this.cost = cost;
    }

    /**
     * Used for generating classes from json.
     */
    public Equipment() {
    }

    /**
     * @return a string representation of this Equipment
     */
    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", modifiers=" + Arrays.toString(modifiers) +
                '}';
    }

    /**
     * @return the name of this Equipment
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of this Equipment
     */
    public String getDescription() {
        return description;
    }

    /**
     * CHANGE I2: Added statistic altering effects to equipment.
     */

    /**
     * @return the stat modifiers of this Equipment (in order: speed, strength, dexterity, intelligence, defence)
     */
    public int[] getModifiers() {
        return modifiers;
    }

    /**
     * @return the ID of this Equipment
     */
    public int getID() {
        return id;
    }

    /**
     * @return the type of this Equipment
     */
    public EquipType getType() {
        return type;
    }

    /**
     * @return how much this Equipment costs in the shop
     */
    public int getCost() {
        return cost;
    }

    /**
     * Represents each different type of equipment.
     */
    public enum EquipType {
        HEAD(0),
        CHEST(1),
        FEET(2),
        ACCESSORY(3),
        WEAPON(4);

        private final int slot;

        EquipType(int slot) {
            this.slot = slot;
        }

        /**
         * @return the slot the represented EquipType has in the equipment array
         */
        public int slot() {
            return slot;
        }

        /**
         * @return the texture to use when rendering the represented EquipType in the equipment list
         */
        public TextureRegion getTexture() {
            return Assets.equipment[slot];
        }
    }
}