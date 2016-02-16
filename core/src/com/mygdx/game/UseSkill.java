package com.mygdx.game;

import com.badlogic.gdx.utils.StringBuilder;
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

        if(skill.getSkillType() == Skill.SkillType.ATTACK) {
            user.setAttacking(true);
        }

        battleAnimator.moveAgentTo(user,target.getX(), this);//Moves the agent to the target
        battleMenu.createInfoBox(user.getName() + " uses " + skill.getName()+" on "+target.getName(),3);//Create an info box with information on the current action
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
        if(type == MOVEMENT_GOING) {
            StringBuilder infoBoxText = new StringBuilder();

            if(skill.getSkillType() == Skill.SkillType.ATTACK) {
                int damage = user.getTotalStrength();

                System.out.println("DAMAGE OF ATTACK " + damage);

                int damageDone = target.dealDamage(damage, user);

                infoBoxText.append(target.getName()).append(damageDone > 0 ? " takes " + damageDone + " damage" : " dodges the attack");

                if(target.isDead()) {
                    infoBoxText.append(" and is defeated.");
                }

                Assets.sfx_hitNoise.play(Game.masterVolume);
                user.setAttacking(false);
            } else {
                target.dealHealth(skill.getBasePower());
                user.takeMana(skill.getMPCost());

                Assets.sfx_healNoise.play(Game.masterVolume);
            }

            battleMenu.createInfoBox(infoBoxText.toString(), 3);
            battleAnimator.returnAgent();
        } else {
            InputHandler.enableAllInput();//re enable input
            battleMenu.showTurnIndicator = false;
            battleMenu.battleScreen.endTurn(); //End the turn
        }
    }

}
