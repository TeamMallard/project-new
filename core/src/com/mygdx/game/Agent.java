package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a party member or an enemy in battle.
 */
public class Agent implements Comparable<Agent> {

    /**
     * The name of this Agent.
     */
    private String name;

    /**
     * The type of this Agent (friendly/enemy).
     */
    public AgentType type;

    /**
     * The statistics of this Agent.
     */
    private Statistics stats;

    /**
     * The list of skills this Agent can use.
     */
    private List<Integer> skills;

    /**
     * The current equipment this Agent has equipped.
     */
    private CurrentEquipment equipment;

    /**
     * The position of this Agent on the battle screen.
     */
    private float x, y;

    /**
     * The index of the texture to use for this Agent.
     */
    private int texture;

    /**
     * Whether or not this Agent is currently attacking.
     */
    private boolean attacking;

    /**
     * The time that the Agent has spent on the current attack.
     */
    private float attackTime = 0;

    /**
     * Compares this Agent to another based on speed. Used to determine turn order.
     *
     * @param agent the Agent to compare to
     * @return the result of the comparison
     */
    @Override
    public int compareTo(Agent agent) {
        int thisSpeed = this.stats.getSpeed() + this.getCurrentEquipment().getTotalSpeedModifiers();
        int otherSpeed = agent.getStats().getSpeed() + agent.getCurrentEquipment().getTotalSpeedModifiers();
        return otherSpeed - thisSpeed;
    }

    /**
     * Creates a new Agent with the specified parameters.
     *
     * @param name      the name of this Agent
     * @param type      the type of this Agent (friendly/enemy)
     * @param stats     the statistics of this Agent
     * @param skills    the list of skills that this Agent can use
     * @param equipment the current equipment this Agent has equipped
     * @param texture   the index of the texture to use for this Agent
     */
    public Agent(String name, AgentType type, Statistics stats, List<Integer> skills, CurrentEquipment equipment, int texture) {
        this.name = name;
        this.type = type;
        this.stats = stats;
        this.skills = skills;
        this.equipment = equipment;
        this.x = 0;
        this.y = 0;
        this.texture = texture;
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public Agent() {
    }

    /**
     * Damages this Agent. Takes defence and attacker strength into account. Also has a change to evade the attack based on dexterity.
     *
     * @param power    the power of the attack
     * @param attacker the attacker
     * @return the actual damage taken
     */
    public int dealDamage(int power, Agent attacker) {
        // Calculate chance to take damage based on attacker's and defender's dexterity.
        float hitChance = MathUtils.clamp(1 - ((float) getTotalDexterity() / attacker.getTotalDexterity()) / 10, 0.1f, 0.9f);

        int damageDone = 0;
        if (hitChance > MathUtils.random()) {
            damageDone = MathUtils.round((float) (power * MathUtils.clamp(Math.sqrt((float) attacker.getTotalStrength() / getTotalDefence()), 0.1, 1)));
        }

        stats.reduceHP(damageDone);

        // Return the actual damage done.
        return damageDone;
    }

    /**
     * Increases the health of this Agent by the specified amount.
     *
     * @param amount the amount of health to increase by
     */
    public void dealHealth(int amount) {
        stats.increaseHP(amount);
    }

    /**
     * Increases the mana of this Agent by the specified amount.
     *
     * @param amount the amount of mana to increase by
     */
    public void giveMana(int amount) {
        stats.increaseMP(amount);
    }

    /**
     * Decreases the mana of this Agent by the specified amount.
     *
     * @param amount the amount of mana to decrease by
     */
    public void takeMana(int amount) {
        stats.reduceMP(amount);
    }

    /**
     * @return the statistics of this Agent
     */
    public Statistics getStats() {
        return this.stats;
    }

    /**
     * @return the current equipment this Agent has equipped
     */
    public CurrentEquipment getCurrentEquipment() {
        return equipment;
    }

    /**
     * Adds a skill to the list of skills that can be used by this Agent.
     *
     * @param skillID the skill ID
     */
    public void addSkill(int skillID) {
        if (!skills.contains(skillID)) {
            skills.add(skillID);
        } else {
            skills.add(skillID);
        }

    }

    /**
     * Gets the current list of skills that can be used by this Agent (takes level into account for unlocking skills).
     *
     * @return the current list of skills that can be used by this Agent
     */
    public List<Integer> getSkills() {
        List<Integer> tempSkills = new ArrayList<Integer>();
        if (this.getStats().getCurrentLevel() >= 8 && skills.size() >= 4) {
            tempSkills.add(skills.get(3));
        }
        if (this.getStats().getCurrentLevel() >= 4 && skills.size() >= 3) {
            tempSkills.add(skills.get(2));
        }
        if (this.getStats().getCurrentLevel() >= 2 && skills.size() >= 2) {
            tempSkills.add(skills.get(1));
        }
        tempSkills.add(skills.get(0));
        return tempSkills;
    }

    /**
     * @return the total speed stat of this Agent
     */
    public int getTotalSpeed() {
        return this.getStats().getSpeed() + this.getCurrentEquipment().getTotalSpeedModifiers();
    }

    /**
     * @return the total strength stat of this Agent
     */
    public int getTotalStrength() {
        return this.getStats().getStrength() + this.getCurrentEquipment().getTotalStrengthModifiers();
    }

    /**
     * @return the total dexterity stat of this Agent
     */
    public int getTotalDexterity() {
        return this.getStats().getDexterity() + this.getCurrentEquipment().getTotalDexterityModifiers();
    }

    /**
     * @return the total intelligence stat of this Agent
     */
    public int getTotalInteligence() {
        return this.getStats().getIntelligence() + this.getCurrentEquipment().getTotalIntelligenceModifiers();
    }

    /**
     * @return the total defence stat of this Agent
     */
    public int getTotalDefence() {
        return this.getStats().getArmourVal() + this.getCurrentEquipment().getTotalArmourValModifiers();
    }

    /**
     * @return the name of this Agent
     */
    public String getName() {
        return name;
    }

    /**
     * @return whether this Agent is dead
     */
    public boolean isDead() {
        return stats.getCurrentHP() <= 0;
    }

    /**
     * @return the type of this Agent (friendly/enemy).
     */
    public AgentType getType() {
        return type;
    }

    /**
     * @return the x coordinate of this Agent on the battle screen
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x coordinate of this Agent on the battle screen
     *
     * @param x the x coordinate of this Agent on the battle screen
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y coordinate of this Agent on the battle screen
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y coordinate of this Agent on the battle screen
     *
     * @param y the y coordinate of this Agent on the battle screen
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return whether this Agent is currently attacking
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Sets whether this Agent is currently attacking.
     *
     * @param attacking whether this Agent is currently attacking
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;

        if (!attacking) {
            attackTime = 0;
        }
    }

    /**
     * @return how long this Agent has been attacking for
     */
    public float getAttackTime() {
        return attackTime;
    }

    /**
     * Updates the time this Agent has been attacking.
     *
     * @param delta the time elapsed since the last update
     */
    public void updateAttackTime(float delta) {
        this.attackTime += delta;
    }

    /**
     * @return a string representation of this Agent
     */
    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", stats=" + stats +
                ", skills=" + skills +
                ", equipment=" + equipment +
                '}';
    }

    /**
     * @return index of the texture to use for this Agent
     */
    public int getTexture() {
        return texture;
    }

    /**
     * Represents the type of agent (either friendly or enemy).
     */
    public enum AgentType {
        ENEMY, FRIENDLY
    }

}
