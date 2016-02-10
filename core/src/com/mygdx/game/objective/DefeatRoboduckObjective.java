package com.mygdx.game.objective;

import com.mygdx.game.entity.NPC;

/**
 * Created by olivermcclellan on 10/02/2016.
 */
public class DefeatRoboduckObjective implements Objective {

    private boolean complete;
    private String name;

    public DefeatRoboduckObjective(String name) {
        this.name = name;
    }

    public void setComplete() {
        complete = true;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public String getObjectiveString() {
        return "Defeat " + name;
    }
}
