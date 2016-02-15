package com.mygdx.game.objective;

import com.mygdx.game.Game;

/**
 * Created by olivermcclellan on 10/02/2016.
 */
public class CollectItemObjective implements Objective {

    private int itemId;
    private int quantity;

    public CollectItemObjective(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    @Override
    public boolean isComplete() {
        int count = 0;

        for (int item : Game.party.getConsumables()) {
            if (item == itemId)
                count++;
        }

        if (count >= quantity) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getObjectiveString() {
        return "Collect " + quantity + " of " + Game.items.getConsumable(itemId).getName();
    }
}
