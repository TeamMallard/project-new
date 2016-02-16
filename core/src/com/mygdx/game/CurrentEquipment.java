package com.mygdx.game;

import java.util.Arrays;

/**
 * Represents the equipment that an agent currently has equipped.
 */
public class CurrentEquipment {

    /**
     * The currently equipped equipment in each slot (in order: head, chest, feet, accessory, weapon).
     */
    public int[] equipSlots = new int[Slot.values().length];

    /**
     * The current stat modifiers for the equipment (in order: speed, strength, dexterity, intelligence, defence).
     */
    private int[] totalStatModifiers = new int[5];

    /**
     * Creates a new CurrentEquipment with the specified parameters.
     *
     * @param head      the equipment ID of the head slot
     * @param chest     the equipment ID of the chest slot
     * @param feet      the equipment ID of the feet slot
     * @param accessory the equipment ID of the accessory slot
     * @param weapon    the equipment ID of the weapon slot
     */
    public CurrentEquipment(int head, int chest, int feet, int accessory, int weapon) {
        equipSlots[Slot.HEAD.ordinal()] = head;
        equipSlots[Slot.CHEST.ordinal()] = chest;
        equipSlots[Slot.FEET.ordinal()] = feet;
        equipSlots[Slot.ACCESSORY.ordinal()] = accessory;
        equipSlots[Slot.WEAPON.ordinal()] = weapon;

        totalStatModifiers = calculateTotalStatModifiers();
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public CurrentEquipment() {
    }

    /**
     * @return a string representation of this CurrentEquipment
     */
    @Override
    public String toString() {
        return "CurrentEquipment{" +
                "equipSlots=" + Arrays.toString(equipSlots) +
                '}';
    }

    /**
     * Calculates the total modifiers for each stat based on the current equipment.
     *
     * @return new stat modifiers for the equipment (in order: speed, strength, dexterity, intelligence, defence)
     */
    private int[] calculateTotalStatModifiers() {
        int[][] totalStatModifiersPerItem = new int[5][5];

        for (int i = 0; i < Slot.values().length; i++) {
            totalStatModifiersPerItem[i] = Game.items.getEquipment(equipSlots[i]).getModifiers();
        }

        int[] totalStatModifiers = new int[5];


        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 5; row++) {
                totalStatModifiers[column] += totalStatModifiersPerItem[row][column];
            }
        }
        System.out.println("total modifiers:" + Arrays.toString(totalStatModifiers));
        return totalStatModifiers;
    }

    /**
     * Equips the specified item of equipment.
     *
     * @param id the ID of the equipment to equip
     */
    public void equip(int id) {
        Equipment item = Game.items.getEquipment(id);
        equipSlots[item.getType().slot()] = id;
        totalStatModifiers = calculateTotalStatModifiers();
    }

    /**
     * Unequips the equipment in the specified slot.
     *
     * @param slot the slot to unequip from
     * @return the id of the item that was in the slot
     */
    public int unequip(Equipment.EquipType slot) {
        int id = equipSlots[slot.slot()];
        equipSlots[slot.slot()] = 0;
        return id;
    }

    /**
     * Checks if something is equipped in the specified slot.
     *
     * @param slot the slot to check for equipment
     * @return true if there is an item equipped in the slot, false if not
     */
    public boolean isEquipped(Equipment.EquipType slot) {
        return equipSlots[slot.slot()] != 0;
    }

    /**
     * @return the total number of equipment that the player is currently wearing
     */
    public int getNumberEquipped() {
        int equipped = 0;

        for (int equipSlot : equipSlots) {
            if (equipSlot != 0) {
                equipped++;
            }
        }

        return equipped;
    }

    /**
     * @return the total speed modifier
     */
    public int getTotalSpeedModifiers() {
        return totalStatModifiers[0];
    }

    /**
     * @return the total strength modifier
     */
    public int getTotalStrengthModifiers() {
        return totalStatModifiers[1];
    }

    /**
     * @return the total dexterity modifier
     */
    public int getTotalDexterityModifiers() {
        return totalStatModifiers[2];
    }

    /**
     * @return the total intelligence modifier
     */
    public int getTotalIntelligenceModifiers() {
        return totalStatModifiers[3];
    }

    /**
     * @return the total defence modifier
     */
    public int getTotalArmourValModifiers() {
        return totalStatModifiers[4];
    }

    /**
     * Represents the slot in which a piece of equipment can be equipped.
     */
    public enum Slot {
        HEAD, CHEST, FEET, ACCESSORY, WEAPON
    }
}
