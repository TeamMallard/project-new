package com.mygdx.game.objective;

/**
 * Represents an objective that involves defeating Roboduck.
 */
public class DefeatRoboduckObjective implements Objective {

    /**
     * Whether this objective has been completed.
     */
    private boolean complete;

    /**
     * Called when Roboduck is defeated to signal that this DefeatRoboduckObjective has been completed.
     */
    public void roboduckDefeated() {
        complete = true;
    }

    /**
     * @return whether this DefeatRoboduckObjective has been completed yet
     */
    @Override
    public boolean isComplete() {
        return complete;
    }

    /**
     * @return a string describing this DefeatRoboduckObjective
     */
    @Override
    public String getObjectiveString() {
        return "Defeat Roboduck";
    }
}
