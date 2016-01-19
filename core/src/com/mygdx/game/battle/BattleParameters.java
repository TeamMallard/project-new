package com.mygdx.game.battle;

import com.mygdx.game.Agent;
import com.mygdx.game.PartyManager;

/**
 * Class holds the parameters to pass to the battlescreen when switching between screens.
 */
public class BattleParameters {

    private int background;
    private PartyManager enemyParty;

    public BattleParameters(int background) {
        this.background = background;
        enemyParty = new PartyManager();
    }

    public void addEnemy(Agent enemy) {
        enemyParty.addMember(enemy);
    }

    public PartyManager getEnemyParty() {
        return enemyParty;
    }

    public int getBackground() {
        return background;
    }

}
