package com.mygdx.game.objective;

/**
 * Created by olivermcclellan on 10/02/2016.
 */
public class DefeatRoboduckObjective implements Objective {

    private boolean complete;

    public void roboduckDefeated() {
        complete = true;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public String getObjectiveString() {
        return "Defeat Roboduck";
    }
}
