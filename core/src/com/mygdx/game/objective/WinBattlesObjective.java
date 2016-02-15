package com.mygdx.game.objective;

/**
 * Created by olivermcclellan on 15/02/2016.
 */
public class WinBattlesObjective implements Objective {

    private int battles;

    public WinBattlesObjective(int battles) {
        this.battles = battles;
    }

    public void wonBattle() {
        battles--;
    }

    @Override
    public boolean isComplete() {
        return battles == 0;
    }

    @Override
    public String getObjectiveString() {
        return "Win " + battles + " more battles";
    }
}
