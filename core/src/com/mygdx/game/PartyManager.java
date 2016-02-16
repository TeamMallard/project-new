package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.BattleTextures;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about a party of agents.
 */
public class PartyManager {

    /**
     * The list of agents in the party.
     */
    private List<Agent> partyList = new ArrayList<Agent>();

    /**
     * The list of equipment shared between party members (does not include equipped items).
     */
    private List<Integer> equipment = new ArrayList<Integer>();

    /**
     * The list of consumables the party has.
     */
    private List<Integer> consumables = new ArrayList<Integer>();

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public PartyManager() {
    }

    /**
     * Adds an agent to this PartyManager.
     *
     * @param member the agent to add
     */
    public void addMember(Agent member) {
        if (partyList.size() <= 4)
            partyList.add(member);
    }

    /**
     * Gets the agent stored at the specified index.
     *
     * @param index the index of the agent
     * @return the agent
     */
    public Agent getMember(int index) {
        return partyList.get(index);
    }

    /**
     * Gets the index of the specified agent in the party.
     *
     * @param member the agent
     * @return the index of the agent, or -1 if the agent is not in the party
     */
    public int getIndex(Agent member) {
        for (int i = 0; i < partyList.size(); i++) {
            if (partyList.get(i) == member) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @return the number of agents in the party
     */
    public int size() {
        return partyList.size();
    }

    /**
     * Renders the party on the battle screen.
     *
     * @param batch the sprite batch to use
     */
    public void render(SpriteBatch batch) {
        for (Agent agent : partyList) {
            BattleTextures battleTextures = Assets.battleTextures[agent.getTexture()];
            TextureRegion texture = agent.isAttacking() ? battleTextures.getBattleTexture(agent.getAttackTime()) : battleTextures.getTexture(agent.isDead());

            batch.draw(texture, agent.getX() - (32), agent.getY() - (32), 96f, 96f);
        }

    }

    /**
     * @return true if all agents in the party are dead
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

    /**
     * @return the list of consumables the party has
     */
    public List<Integer> getConsumables() {
        return consumables;
    }

    /**
     * @return the list of equipment the party has
     */
    public List<Integer> getEquipment() {
        return equipment;
    }

    /**
     * @return a string representation of this PartyManager
     */
    @Override
    public String toString() {
        return "PartyManager{" +
                "partyList=" + partyList +
                '}';
    }

    /**
     * Sets the health of each agent in the party.
     *
     * @param health the health to set each agent to
     */
    public void setHealths(int health) {
        for (Agent agent : partyList) {
            agent.dealHealth(health);
        }
    }
}
