package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
/**
 * Stores and manages a list of Agents and allows higher level functions across the list.
 */
public class PartyManager {

    private List<Agent> partyList = new ArrayList<Agent>();
    private List<Integer> equipables = new ArrayList<Integer>();
    private List<Integer> consumables = new ArrayList<Integer>();


    public PartyManager() {

    }

    /**
     * Add an Agent to the Party.
     * @param member The Agent to add
     */
    public void addMember(Agent member){

        if(partyList.size()<=4)
            partyList.add(member);
    }

    /**
     * Returns the Agent stored at the given index.
     * @param index The index to receive the Agent at
     */
    public Agent getMember(int index){
        return partyList.get(index);
    }

    /**
     * Returns the index of a given Agent.
     * @param member The Agent to find the index of
     * @return The index of the given agent
     */
    public int getIndex(Agent member){
        for(int i=0; i<partyList.size(); i++){
            if(partyList.get(i)==member){
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds party members with names that contain the search term.
     * @param searchString The search string- case insensitive
     * @return returns list of agents with names that contain the search string
     */
    public List<Agent> getMember(String searchString){
        List<Agent> returnList = new ArrayList<Agent>();
        for (int i=0;i<partyList.size();i++){
            if(partyList.get(i).getName().toLowerCase().contains(searchString.toLowerCase()))
                returnList.add(partyList.get(i));
        }
        return returnList;
    }

    public int size(){
        return partyList.size();
    }

    //The update function for the party
    public void update(){


    }

    /**
     * Renders the the party at their respective locations with their respective sprite.
     * @param delta The time between frames
     * @param batch The SpriteBatch to use
     */
    public void render(float delta, SpriteBatch batch){
        for (int i = 0; i < partyList.size(); i++) {
            Agent thisAgent = partyList.get(i);

            if(!thisAgent.isDead())
                batch.draw(Assets.battleSprites[thisAgent.getTexture()][0],thisAgent.getX()-(32),thisAgent.getY()-(32),96f,96f);
            else
                batch.draw(Assets.battleSprites[thisAgent.getTexture()][1],thisAgent.getX()-(32),thisAgent.getY()-(32),96f,96f);
        }

    }

    /**
     * Checks to see if the entire Party is dead.
     * @return True if entire party is dead, false otherwise
     */
    public boolean isDead(){
        boolean isDead = true;
        for(int i=0; i<partyList.size(); i++) {
            if (!partyList.get(i).isDead()){
                isDead = false;
                break;
            }
        }
        return isDead;
    }

    public List<Integer> getConsumables() {
        return consumables;
    }

    public List<Integer> getEquipables() {
        return equipables;
    }

    @Override
    public String toString() {
        return "PartyManager{" +
                "partyList=" + partyList +
                '}';
    }

    /**
     * Sets the health of each party member.
     * @param x The health to set each party member to
     */
    public void setHealths(int x) {
        for(int i=0; i<partyList.size(); i++) {
            partyList.get(i).dealHealth(x);
        }
    }
}
