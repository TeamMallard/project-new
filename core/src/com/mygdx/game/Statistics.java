package com.mygdx.game;

import java.util.Random;

/**
 * Represents the statistics of an agent.
 */
public class Statistics {

    /**
     * CHANGE P2: Altered the XP amounts and level thresholds for the leveling system.
     */

    /**
     * The experience required for each level.
     */
    private final int[] LEVEL_CURVE = {15, 35, 50, 70, 95, 125, 160, 200, 250, 325};

    /**
     * The current and maximum HP and MP.
     */
    private int currentHP, currentMP, maxHP, maxMP;

    /**
     * The speed, strength, dexterity and intelligence stats.
     */
    private int speed, strength, dexterity, intelligence;

    /**
     * The defence stat.
     */
    private int armourVal;

    /**
     * The current experience and level.
     */
    private int experience, currentLevel;

    /**
     * The maximum level reachable.
     */
    public final int maxLevel = 10;

    /**
     * Creates a new Statistics with the specified parameters.
     *
     * @param maxHP        the maximum (and initial) number of hitpoints
     * @param maxMP        the maximum (and initial) number of mana points
     * @param speed        the speed stat
     * @param strength     the strength stat
     * @param dexterity    the dexterity stat
     * @param intelligence the intelligence stat
     * @param armourVal    the defence stat
     * @param experience   the current experience
     * @param currentLevel the current level
     */
    public Statistics(int maxHP, int maxMP, int speed, int strength, int dexterity, int intelligence, int armourVal, int experience, int currentLevel) {
        this.maxHP = maxHP;
        this.maxMP = maxMP;
        this.currentHP = maxHP; // Current HP & MP set to max
        this.currentMP = maxMP;

        this.speed = speed; // Determines turn order
        this.strength = strength; // Determines melee damage
        this.dexterity = dexterity; // Determines ranged damage
        this.intelligence = intelligence; // Determines magic damage

        this.armourVal = armourVal; // Influenced by armour

        this.experience = experience; // Current amount of xp
        this.currentLevel = currentLevel;
        increaseXP(0);
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public Statistics() {
    }

    /**
     * @return a string representation of this Statistics
     */
    @Override
    public String toString() {
        return "Statistics{" +
                "currentHP=" + currentHP +
                ", currentMP=" + currentMP +
                ", maxHP=" + maxHP +
                ", maxMP=" + maxMP +
                ", speed=" + speed +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", intelligence=" + intelligence +
                ", armourVal=" + armourVal +
                ", experience=" + experience +
                ", currentLevel=" + currentLevel +
                ", maxLevel=" + maxLevel +
                '}';
    }

    /**
     * Increases experience by the specified amount.
     *
     * @param xp the amount of experience to increase by
     * @return the increase in level caused by the increase in experience
     */
    public int increaseXP(int xp) {
        int levelIncrease = 0;
        Random random = new Random();
        experience += xp;
        if (currentLevel < maxLevel) {
            while (experience >= this.getLevelCap()) {
                /**
                 * CHANGE P3: Leveling up fully restores the HP and MP of the party.
                 */

                maxHP += random.nextInt(intelligence * 5) + 10;
                currentHP = maxHP;
                maxMP += random.nextInt(intelligence) + 1;
                currentMP = maxMP;
                speed += random.nextInt(intelligence) + 1;
                strength += random.nextInt(intelligence) + 1;
                dexterity += random.nextInt(intelligence) + 1;
                intelligence += random.nextInt(intelligence) + 1;

                currentLevel += 1;
                levelIncrease += 1;
            }
        }

        return levelIncrease;
    }

    /**
     * Decreases hitpoints by the specified amount.
     *
     * @param amount the amount of hitpoints to decrease by
     */
    public void reduceHP(int amount) {
        currentHP -= amount;
        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    /**
     * Increases hitpoints by the specified amount.
     *
     * @param amount the amount of hitpoints to increase by
     */
    public void increaseHP(int amount) {
        currentHP += amount;
        if (currentHP > maxHP)
            currentHP = maxHP;
    }

    /**
     * Increases mana points by the specified amount.
     *
     * @param amount the amount of mana points to increase by
     */
    public void increaseMP(int amount) {
        currentMP += amount;
        if (currentMP > maxMP)
            currentMP = maxMP;
    }

    /**
     * Decreases mana points by the specified amount.
     *
     * @param amount the amount of mana points to decrease by
     */
    public void reduceMP(int amount) {
        currentMP -= amount;
        if (currentMP < 0)
            currentMP = 0;
    }

    /**
     * @return the current hitpoints
     */
    public int getCurrentHP() {
        return currentHP;
    }

    /**
     * @return the current mana points
     */
    public int getCurrentMP() {
        return currentMP;
    }

    /**
     * @return the maximum hitpoints
     */
    public int getMaxHP() {
        return maxHP;
    }

    /**
     * @return the maximum mana points
     */
    public int getMaxMP() {
        return maxMP;
    }

    /**
     * @return the speed stat
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @return the strength stat
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @return the dexterity stat
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * @return the intelligence stat
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * @return the defence stat
     */
    public int getArmourVal() {
        return armourVal;
    }

    /**
     * @return the current experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @return the current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * @return how much experience required to advance from the current level
     */
    public int getLevelCap() {
        return LEVEL_CURVE[this.getCurrentLevel() - 1];
    }
}
