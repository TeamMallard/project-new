package com.mygdx.game;

import com.mygdx.game.battle.BattleMenu;

/**
 * Represents an ability that can be used in battle.
 */
public abstract class UseAbility {

    /**
     * Represents moving toward the enemy.
     */
    public static final int MOVEMENT_GOING = 0;

    /**
     * Represents moving away from the enemy.
     */
    public static final int MOVEMENT_RETURNING = 1;

    /**
     * The user of the ability and the target.
     */
    protected Agent user, target;

    /**
     * The ID of the ability
     */
    protected int abilityID;

    /**
     * The battle menu that this ability is initiated from.
     */
    protected BattleMenu battleMenu;

    /**
     * Creates a new UseAbility with the specified parameters.
     *
     * @param user       the user of the ability
     * @param target     the target of the ability
     * @param abilityID  the ID of the ability
     * @param battleMenu the battle menu that this ability is initiated from
     */
    public UseAbility(Agent user, Agent target, int abilityID, BattleMenu battleMenu) {
        this.user = user;
        this.target = target;
        this.abilityID = abilityID;
        this.battleMenu = battleMenu;
    }

    /**
     * Called when the agent using this UseAbility completes its movement.
     *
     * @param type the type of movement that was completed
     */
    public abstract void movementDone(int type);

    /**
     * Updates the state of this UseAbility.
     *
     * @param delta the time elapsed since the last update
     */
    public abstract void update(float delta);
}
