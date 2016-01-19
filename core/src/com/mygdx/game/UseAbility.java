package com.mygdx.game;

import com.mygdx.game.battle.BattleMenu;

/**
 * The super class for the UseItem and UseSkill classes.
 */
public abstract class UseAbility {

    Agent user, target;
    int abilityID;
    BattleMenu battleMenu;

    public UseAbility(Agent user, Agent target, int abilityID, BattleMenu battleMenu){
        this.user = user;
        this.target = target;
        this.abilityID = abilityID;
        this.battleMenu = battleMenu;

    }


    public abstract void movementDone(int type);

    public abstract void update(float delta);
}
