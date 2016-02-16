package com.mygdx.game.objective;

/**
 * CHANGE O1: Added a system which assigns and tracks objectives. Objectives are determined by segment.
 */

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
