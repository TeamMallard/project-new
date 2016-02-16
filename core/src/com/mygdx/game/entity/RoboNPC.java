package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleParameters;
import com.mygdx.game.objective.DefeatRoboduckObjective;
import com.mygdx.game.ui.UIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the robot boss of the game.
 */
public class RoboNPC extends NPC {

    private String[] messages;

    public RoboNPC(Level level, Vector2 currentTile) {
        super(level, currentTile, Assets.roboWalkingTextures);
        messages = new String[2];
        messages[0] = "01011001 01101111 01110101 00100000 01110111 01101001 01101100 01101100 00100000 01101110 01100101 01110110 01100101 01110010 00100000 01100100 01100101 01100110 01100101 01100001 01110100 00100000 01101101 01100101 00100001!!!";
        messages[1] = "Robo duck has challenged you to a battle.";
    }

    @Override
    public void initializeInteraction(float delta, UIManager uiManager) {
        uiManager.createDialogue(messages);
        this.uiManager = uiManager;
    }

    @Override
    public boolean updateInteracting(float delta) {
        return uiManager.updateDialogue(delta);
    }

    @Override
    public void action(GameWorld gameWorld) {
        Assets.sfxBattleStart.play(Game.masterVolume);
        uiManager.addNotification("Robo Duck has been defeated.");
        BattleParameters params = new BattleParameters(Game.segment);
        //Enemy ducks
        List<Integer> emptyList = new ArrayList<Integer>();
        Agent enemyDuck = new Agent("Robo Duck", Agent.AgentType.ENEMY, new Statistics(250, 500, 8, 2, 3, 3, 3, 201, 9), emptyList, new CurrentEquipment(0, 0, 0, 0, 0), 1);
//        enemyDuck.equipEquipment(0);
//        enemyDuck.equipEquipment(1);
        enemyDuck.addSkill(4);

        params.addEnemy(enemyDuck);


        gameWorld.setBattle(params);

        // If Roboduck is dead then the player has completed the objective.
        if (enemyDuck.isDead() && Game.objective instanceof DefeatRoboduckObjective) {
            ((DefeatRoboduckObjective) Game.objective).roboduckDefeated();
        }

        level.characters.remove(this);

    }
}
