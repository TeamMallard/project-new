package com.mygdx.game;

/**
 * Represents a skill usable by agents in battle.
 */
public class Skill {

    /**
     * The ID of this Skill.
     */
    private int id = -1;

    /**
     * The name and description of this Skill.
     */
    private String name, description;

    /**
     * What type this Skill is.
     */
    private SkillType skillType;

    /**
     * The base power of this Skill and how much MP is costs to use.
     */
    private int basePower, mpCost;

    /**
     * Creates a new Skill with the specified parameters.
     *
     * @param name        the name of this Skill
     * @param description the description of this Skill
     * @param skillType   the type of this Skill
     * @param basePower   the base power of this Skill
     * @param mpCost      how much MP this Skill costs to use
     */
    public Skill(String name, String description, SkillType skillType, int basePower, int mpCost) {
        this.name = name;
        this.description = description;
        this.skillType = skillType;
        this.basePower = basePower;
        this.mpCost = mpCost;
    }

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public Skill() {
    }

    /**
     * Changes the ID of this Skill.
     *
     * @param id the new ID
     */
    public void updateID(int id) {
        this.id = id;
    }

    /**
     * @return a string representation of this Skill
     */
    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", skillType=" + skillType +
                ", basePower=" + basePower +
                ", mpCost=" + mpCost +
                '}';
    }

    /**
     * @return the base power of this Skill
     */
    public int getBasePower() {
        return basePower;
    }

    /**
     * @return the ID of this Skill
     */
    public int getID() {
        return id;
    }

    /**
     * @return the name of this Skill
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of this Skill
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the type of this Skill
     */
    public SkillType getSkillType() {
        return skillType;
    }

    /**
     * @return how much MP this Skill costs to use
     */
    public int getMpCost() {
        return mpCost;
    }

    /**
     * Represents the type of a skill.
     */
    public enum SkillType {
        ATTACK, HEAL
    }
}
