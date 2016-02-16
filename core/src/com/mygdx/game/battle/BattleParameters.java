package com.mygdx.game.battle;

import com.mygdx.game.Agent;
import com.mygdx.game.PartyManager;

/**
 * Parameters passed to the battle screen on creation.
 */
public class BattleParameters {

    /**
     * The index of the background to use.
     */
    private int background;

    /**
     * The enemy party.
     */
    private PartyManager enemyParty;

    /**
     * Creates a new BattleParameters with the specified background.
     *
     * @param background the index of the background to use
     */
    public BattleParameters(int background) {
        this.background = background;
        enemyParty = new PartyManager();
    }

    /**
     * Adds an enemy to the enemy party.
     *
     * @param enemy the enemy to add
     */
    public void addEnemy(Agent enemy) {
        enemyParty.addMember(enemy);
    }

    /**
     * @return the enemy party
     */
    public PartyManager getEnemyParty() {
        return enemyParty;
    }

    /**
     * @return the index of the background to use
     */
    public int getBackground() {
        return background;
    }
}
