package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleAnimator;
import com.mygdx.game.battle.BattleMenu;

/**
 * Class to manage skill usage and the movement required for each type of attack.
 * Can be extended so that each individual skill will perform different actions.
 * This class makes heavy use of the BattleAnimator to perform its actions.
 */
public class UseSkill extends UseAbility {

    BattleAnimator battleAnimator;
    Skill skill;

    /**
     * UseSkill constructor. Immediately begins the process of using the given skill on the target as soon as it is instantiated.
     * @param user The Agent that is using the skill
     * @param target The target Agent
     * @param abilityID The ID of the skill being used
     * @param battleMenu The instance of the battleMenu
     */
    public UseSkill(Agent user, Agent target, int abilityID, BattleMenu battleMenu){
        super(user, target, abilityID, battleMenu);

        InputHandler.disableAllInput();

        battleAnimator = new BattleAnimator();

        skill = Game.skills.getSkill(abilityID);

        battleMenu.showTurnIndicator=false;

        switch (skill.getSkillType()){
            case MELEE:{
                break;
            }
            case RANGED:{
                break;
            }
            case MAGIC:{
                battleAnimator.moveAgentTo(user,target.getX(),target.getY(),this);//Moves the agent to the target
                battleMenu.createInfoBox(user.getName() + " uses " + skill.getName()+" on "+target.getName(),3);//Create an info box with information on the current action

                break;
            }
            case HEAL:{
                battleAnimator.moveAgentTo(user, target.getX(), target.getY(), this);
                battleMenu.createInfoBox(user.getName() + " uses " + skill.getName()+" on "+target.getName(),3);

                break;
            }
        }



    }

    /**
     * Updates the battleAnimator.
     * @param delta The amount of time passed between frames
     */
    public void update(float delta){
        battleAnimator.update(delta);
    }

    /**
     * Called by the BattleAnimator after it has completed a movement.
     * This function, after the first movement, will cause the intended effect of the skill (e.g. deal damage etc.).
     * @param type An integer corresponding to the type of movement that has completed.
     */
    public void movementDone(int type){
        //Type 0=moved, type 1=returned
        if(type==0) {
            switch (skill.getSkillType()) {
                case MELEE: {
                    break;
                }
                case RANGED: {
                    break;
                }
                case MAGIC: {
                    Assets.sfx_hitNoise.play(Game.masterVolume);
                    target.dealDamage(user.getStats().getIntelligence() + user.getCurrentEquipment().getTotalIntelligenceModifiers() + skill.getBasePower());
                    String infoBoxText = (target.getName() + " takes "+(user.getStats().getIntelligence() + user.getCurrentEquipment().getTotalIntelligenceModifiers() + skill.getBasePower()) + " damage");
                    if(target.isDead())
                        infoBoxText+=" and is defeated.";
                    battleMenu.createInfoBox( infoBoxText, 3);
                    battleAnimator.returnAgent();//Return agent to start point

                    break;
                }
                case HEAL: {
                    Assets.sfx_healNoise.play(Game.masterVolume);
                    target.dealHealth(skill.getBasePower());
                    battleMenu.createInfoBox(target.getName() + " healed for " + skill.getBasePower()
                            + " health", 3);
                    battleAnimator.returnAgent();
                    break;
                }
            }
            user.takeMana(skill.getMPCost());
        } else if(type==1){
            InputHandler.enableAllInput();//re enable input
            battleMenu.showTurnIndicator=false;
            battleMenu.battleScreen.endTurn(); //End the turn

        }
    }

}
