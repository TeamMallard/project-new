package com.mygdx.game.objective;

/**
 * Represents an objective to be completed.
 */
public interface Objective {

    /**
     * @return whether this Objective has been completed yet
     */
    boolean isComplete();

    /**
     * @return a string describing this Objective
     */
    String getObjectiveString();

}
