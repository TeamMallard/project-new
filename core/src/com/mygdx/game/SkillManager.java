package com.mygdx.game;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages every skill.
 */

public class SkillManager {

    private List<Skill> skills = new ArrayList<Skill>();


    public SkillManager() {

    }

    /**
     * Add new Skill.
     * @param skill The Skill to add
     */
    public void addSkill(Skill skill) {

        skill.updateID(skills.size()); //Gives the skill the ID of it's index
        skills.add(skill);//Check size function
    }

    /**
     * Returns the Skill stored at the given index.
     * @param skillID The index of the Skill to retrieve
     * @return Skill
     */
    public Skill getSkill(int skillID) {
        return skills.get(skillID);
    }


    @Override
    public String toString() {
        return "SkillManager{" +
                "skills=" + skills +
                '}';
    }


}
