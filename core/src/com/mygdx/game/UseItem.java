package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleAnimator;
import com.mygdx.game.battle.BattleMenu;

/**
 * Class to manage item usage and the movement required for each type of item.
 * Can be extended so that each individual item will perform different actions.
 * This class makes heavy use of the BattleAnimator to perform its actions.
 */
public class UseItem extends UseAbility {



    BattleAnimator battleAnimator;
    Consumable item;

    /**
     * UseItem constructor. Immediately begins the process of using the given item on the target as soon as it is instantiated.
     * @param user The Agent that is using the item
     * @param target The target Agent
     * @param abilityID The ID of the item being used
     * @param battleMenu The instance of the battleMenu
     */
    public UseItem(Agent user, Agent target, int abilityID, BattleMenu battleMenu) {
        super(user, target, abilityID, battleMenu);

        InputHandler.disableAllInput();

        battleAnimator = new BattleAnimator();

        item = Game.items.getConsumable(abilityID);

        switch (item.getType()) {
            case HEAL:
                if(!target.isDead()) {
                    battleAnimator.moveAgentTo(user, target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                }
                else {
                    battleMenu.createInfoBox(target.getName() + " cannot be healed", 3);
                    InputHandler.enableAllInput();
                }
                break;
            case REVIVE:
                if(target.isDead()){
                    battleAnimator.moveAgentTo(user,target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                }
                else {
                    battleMenu.createInfoBox(target.getName() + " cannot be revived", 3);
                    InputHandler.enableAllInput();
                }
                break;
            case MANA:
                if(!target.isDead()) {
                    battleAnimator.moveAgentTo(user, target.getX(), this);
                    battleMenu.createInfoBox(user.getName() + " uses " + item.getName()
                            + " on " + target.getName(), 3);
                }
                else {
                    battleMenu.createInfoBox(target.getName() + " cannot be given mana", 3);
                    InputHandler.enableAllInput();
                }
                break;
        }
    }

    public void update(float delta){
        battleAnimator.update(delta);
    }

    /**
     * Called by the BattleAnimator after it has completed a movement.
     * This function, after the first movement, will cause the intended effect of the item (e.g. revive etc.).
     * @param type An integer corresponding to the type of movement that has completed
     */
    public void movementDone(int type){
        //Type 0=moved, type 1=returned
        if(type==0) {
            switch(item.getType()){
                case HEAL:{
                    Assets.sfx_healNoise.play(Game.masterVolume);
                    target.dealHealth(item.getPower());
                    Game.party.getConsumables().remove(item.getID());
                    battleMenu.createInfoBox(target.getName() + " is healed for " + item.getPower()
                            + " health",3);
                    battleAnimator.returnAgent();
                    break;
                }
                case REVIVE:{
                    Assets.sfx_healNoise.play(Game.masterVolume);
                    target.dealHealth(item.getPower());
                    Game.party.getConsumables().remove(item.getID());
                    battleMenu.createInfoBox(target.getName() + " is revived on " + item.getPower()
                            + " health",3);
                    battleAnimator.returnAgent();
                    break;
                }
                case MANA:{
                    Assets.sfx_healNoise.play(Game.masterVolume);
                    Game.party.getConsumables().remove(item.getID());
                    battleMenu.createInfoBox(target.getName() + " gains " + item.getPower()
                            + " mana",3);
                    battleAnimator.returnAgent();
                    break;
                }

            }
        }
        else if(type==1){
            InputHandler.enableAllInput();
            battleMenu.showTurnIndicator=false;
            battleMenu.battleScreen.endTurn();//Ends turn
        }
    }

}
