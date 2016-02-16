package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleAnimator;
import com.mygdx.game.battle.BattleMenu;

/**
 * Represents the action of using a consumable in battle.
 */
public class UseItem extends UseAbility {

    /**
     * The battle animator which animates this UseItem.
     */
    private BattleAnimator battleAnimator;

    /**
     * The consumable being used.
     */
    private Consumable item;

    /**
     * Creates a new UseItem with the specified parameters.
     *
     * @param user       the user of the ability
     * @param target     the target of the ability
     * @param abilityID  the ID of the consumable item
     * @param battleMenu the battle menu that this ability is initiated from
     */
    public UseItem(Agent user, Agent target, int abilityID, BattleMenu battleMenu) {
        super(user, target, abilityID, battleMenu);

        InputHandler.disableAllInput();

        battleAnimator = new BattleAnimator();

        item = Game.items.getConsumable(abilityID);

        switch (item.getType()) {
            case HEAL:
                if (!target.isDead()) {
                    battleAnimator.moveAgentTo(user, target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                } else {
                    battleMenu.createInfoBox(target.getName() + " cannot be healed", 3);
                    InputHandler.enableAllInput();
                }
                break;
            case REVIVE:
                if (target.isDead()) {
                    battleAnimator.moveAgentTo(user, target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                } else {
                    battleMenu.createInfoBox(target.getName() + " cannot be revived", 3);
                    InputHandler.enableAllInput();
                }
                break;
            case MANA:
                if (!target.isDead()) {
                    battleAnimator.moveAgentTo(user, target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                } else {
                    battleMenu.createInfoBox(target.getName() + " cannot be given mana", 3);
                    InputHandler.enableAllInput();
                }
                break;
        }
    }

    /**
     * Updates the state of this UseItem.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        battleAnimator.update(delta);
    }

    /**
     * Called when the agent using this UseItem completes its movement (actually applies the effect).
     *
     * @param type the type of movement that was completed
     */
    public void movementDone(int type){
        //Type 0=moved, type 1=returned
        if(type==0) {
            switch(item.getType()){
                case HEAL:
                    Assets.sfxHealNoise.play(Game.masterVolume);
                    target.dealHealth(item.getPower());
                    Game.party.getConsumables().remove(new Integer(item.getID()));
                    battleMenu.createInfoBox(target.getName() + " is healed for " + item.getPower()
                            + " health", 3);
                    battleAnimator.returnAgent();
                    break;

                case REVIVE:
                    Assets.sfxHealNoise.play(Game.masterVolume);
                    target.dealHealth(item.getPower());
                    Game.party.getConsumables().remove(new Integer(item.getID()));
                    battleMenu.createInfoBox(target.getName() + " is revived on " + item.getPower()
                            + " health", 3);
                    battleAnimator.returnAgent();
                    break;

                case MANA:
                    Assets.sfxHealNoise.play(Game.masterVolume);
                    Game.party.getConsumables().remove(new Integer(item.getID()));
                    battleMenu.createInfoBox(target.getName() + " gains " + item.getPower()
                            + " mana", 3);
                    battleAnimator.returnAgent();
                    break;
            }
        } else if (type == UseAbility.MOVEMENT_RETURNING) {
            InputHandler.enableAllInput();
            battleMenu.showTurnIndicator = false;
            battleMenu.battleScreen.endTurn();
        }
    }

}
