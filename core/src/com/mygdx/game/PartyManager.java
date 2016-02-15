package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.BattleTextures;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages a list of Agents and allows higher level functions across the list.
 */
public class PartyManager {

    private List<Agent> partyList = new ArrayList<Agent>();
    private List<Integer> equipment = new ArrayList<Integer>();
    private List<Integer> consumables = new ArrayList<Integer>();


    public PartyManager() {

    }

    /**
     * Add an Agent to the Party.
     *
     * @param member The Agent to add
     */
    public void addMember(Agent member) {

        if (partyList.size() <= 4)
            partyList.add(member);
    }

    /**
     * Returns the Agent stored at the given index.
     *
     * @param index The index to receive the Agent at
     */
    public Agent getMember(int index) {
        return partyList.get(index);
    }

    /**
     * Returns the index of a given Agent.
     *
     * @param member The Agent to find the index of
     * @return The index of the given agent
     */
    public int getIndex(Agent member) {
        for (int i = 0; i < partyList.size(); i++) {
            if (partyList.get(i) == member) {
                return i;
            }
        }
        return -1;
    }

//    /**
//     * Finds party members with names that contain the search term.
//     *
//     * @param searchString The search string- case insensitive
//     * @return returns list of agents with names that contain the search string
//     */
//    public List<Agent> getMember(String searchString) {
//        List<Agent> returnList = new ArrayList<Agent>();
//        for (Agent agent : partyList) {
//            if (agent.getName().toLowerCase().contains(searchString.toLowerCase()))
//                returnList.add(agent);
//        }
//        return returnList;
//    }

    public int size() {
        return partyList.size();
    }

    /**
     * Renders the the party at their respective locations with their respective sprite.
     *
     * @param batch The SpriteBatch to use
     */
    public void render(SpriteBatch batch) {
        for (Agent agent : partyList) {
            BattleTextures battleTextures = Assets.battleTextures[agent.getTexture()];
            TextureRegion texture = agent.isAttacking() ? battleTextures.getBattleTexture(agent.getAttackTime()) : battleTextures.getTexture(agent.isDead());

            batch.draw(texture, agent.getX() - (32), agent.getY() - (32), 96f, 96f);
        }

    }

    /**
     * Checks to see if the entire Party is dead.
     *
     * @return True if entire party is dead, false otherwise
     */
    public boolean isDead() {
        boolean isDead = true;
        for (Agent agent : partyList) {
            if (!agent.isDead()) {
                isDead = false;
                break;
            }
        }
        return isDead;
    }

    public List<Integer> getConsumables() {
        return consumables;
    }

    public List<Integer> getEquipment() {
        return equipment;
    }

    @Override
    public String toString() {
        return "PartyManager{" +
                "partyList=" + partyList +
                '}';
    }

    /**
     * Sets the health of each party member.
     *
     * @param x The health to set each party member to
     */
    public void setHealths(int x) {
        for (Agent agent : partyList) {
            agent.dealHealth(x);
        }
    }
}
