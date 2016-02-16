package com.mygdx.game.objective;

/**
 * Represents an objective that involves winning a certain number of battles.
 */
public class WinBattlesObjective implements Objective {

    /**
     * The number of battles that must be won.
     */
    private int battles;

    /**
     * Creates a new WinBattlesObjective with the specified number of battles to win.
     *
     * @param battles the number of battles that must be won
     */
    public WinBattlesObjective(int battles) {
        this.battles = battles;
    }

    /**
     * Called when a battle is won to update progress towards this WinBattlesObjective.
     */
    public void wonBattle() {
        battles--;
    }

    /**
     * @return whether this WinBattlesObjective has been completed yet
     */
    @Override
    public boolean isComplete() {
        return battles <= 0;
    }

    /**
     * @return a string describing this WinBattlesObjective
     */
    @Override
    public String getObjectiveString() {
        return "Win " + battles + " more battles";
    }
}
