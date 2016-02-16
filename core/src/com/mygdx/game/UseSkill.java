package com.mygdx.game;

import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleAnimator;
import com.mygdx.game.battle.BattleMenu;

/**
 * Represents the action of using a skill to attack in battle.
 */
public class UseSkill extends UseAbility {

    /**
     * The battle animator which animates this UseSkill.
     */
    private BattleAnimator battleAnimator;

    /**
     * The skill being used.
     */
    private Skill skill;

    /**
     * Creates a new UseItem with the specified parameters.
     *
     * @param user       the user of the ability
     * @param target     the target of the ability
     * @param abilityID  the ID of the skill to use
     * @param battleMenu the battle menu that this ability is initiated from
     */
    public UseSkill(Agent user, Agent target, int abilityID, BattleMenu battleMenu) {
        super(user, target, abilityID, battleMenu);

        InputHandler.disableAllInput();

        battleAnimator = new BattleAnimator();

        skill = Game.skills.getSkill(abilityID);

        battleMenu.showTurnIndicator = false;

        if (skill.getSkillType() == Skill.SkillType.ATTACK) {
            user.setAttacking(true);
        }

        battleAnimator.moveAgentTo(user, target.getX(), this);//Moves the agent to the target
        battleMenu.createInfoBox(user.getName() + " uses " + skill.getName() + " on " + target.getName(), 3);//Create an info box with information on the current action
    }

    /**
     * Updates the state of this UseAbility.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        battleAnimator.update(delta);
    }

    /**
     * Called when the agent using this UseSkill completes its movement.
     *
     * @param type the type of movement that was completed
     */
    public void movementDone(int type) {
        if (type == MOVEMENT_GOING) {
            StringBuilder infoBoxText = new StringBuilder();

            if (skill.getSkillType() == Skill.SkillType.ATTACK) {
                int damage = user.getTotalStrength();
                int damageDone = target.dealDamage(damage, user);

                infoBoxText.append(target.getName()).append(damageDone > 0 ? " takes " + damageDone + " damage" : " dodges the attack");

                if (target.isDead()) {
                    infoBoxText.append(" and is defeated.");
                }

                Assets.sfxHitNoise.play(Game.masterVolume);
                user.setAttacking(false);
            } else if (skill.getSkillType() == Skill.SkillType.HEAL) {
                target.dealHealth(skill.getBasePower());
                user.takeMana(skill.getMpCost());

                Assets.sfxHealNoise.play(Game.masterVolume);
            }

            battleMenu.createInfoBox(infoBoxText.toString(), 3);
            battleAnimator.returnAgent();
        } else if (type == MOVEMENT_RETURNING) {
            InputHandler.enableAllInput();//re enable input
            battleMenu.showTurnIndicator = false;
            battleMenu.battleScreen.endTurn(); //End the turn
        }
    }

}
