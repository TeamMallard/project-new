package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all of the skills usable in the game.
 */
public class SkillManager {

    /**
     * The list of skills usable in the game.
     */
    private List<Skill> skills = new ArrayList<Skill>();

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public SkillManager() {
    }

    /**
     * Returns the skill with the specified ID.
     *
     * @param skillID the ID of the skill
     * @return the skill
     */
    public Skill getSkill(int skillID) {
        return skills.get(skillID);
    }

    /**
     * @return a string representation of the specified SkillManager
     */
    @Override
    public String toString() {
        return "SkillManager{" +
                "skills=" + skills +
                '}';
    }
}
