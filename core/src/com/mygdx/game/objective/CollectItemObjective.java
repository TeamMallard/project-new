package com.mygdx.game.objective;

import com.mygdx.game.Game;

/**
 * Represents an objective that involves collecting a certain amount of a specific item.
 */
public class CollectItemObjective implements Objective {

    /**
     * The ID of the item to be collected and how many.
     */
    private int itemId, quantity;

    /**
     * Creates a new CollectItemObjective with the specified parameters.
     *
     * @param itemId   the ID of the item to be collected
     * @param quantity how many of the specified item must be collected
     */
    public CollectItemObjective(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    /**
     * @return whether this CollectItemObjective has been completed yet
     */
    @Override
    public boolean isComplete() {
        int count = 0;

        for (int item : Game.party.getConsumables()) {
            if (item == itemId)
                count++;
        }

        return count >= quantity;
    }

    /**
     * @return a string describing this CollectItemObjective
     */
    @Override
    public String getObjectiveString() {
        return "Collect " + quantity + " of " + Game.items.getConsumable(itemId).getName();
    }

	@Override
	public void wonBattle() {
		// TODO Auto-generated method stub
		
	}
}
