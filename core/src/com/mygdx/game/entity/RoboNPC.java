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
 * Represents the robot boss of the game.
 */
public class RoboNPC extends NPC {

    /**
     * The messages to display on the dialogue.
     */
    private String[] messages;

    /**
     * Creates a new RoboNPC in the specified level at the specified tile.
     *
     * @param level       the level this RoboNPC belongs to
     * @param currentTile the tile this RoboNPC begins on
     */
    public RoboNPC(Level level, Vector2 currentTile) {
        super(level, currentTile, Assets.roboWalkingTextures);
        messages = new String[2];
        messages[0] = "01011001 01101111 01110101 00100000 01110111 01101001 01101100 01101100 00100000 01101110 01100101 01110110 01100101 01110010 00100000 01100100 01100101 01100110 01100101 01100001 01110100 00100000 01101101 01100101 00100001!!!";
        messages[1] = "Robo duck has challenged you to a battle.";
    }

    /**
     * Called when a player first interacts with this RoboNPC.
     *
     * @param delta     the time elapsed since the last update
     * @param uiManager the UI manager to show messages on
     */
    @Override
    public void initializeInteraction(float delta, UIManager uiManager) {
        uiManager.createDialogue(messages);
        this.uiManager = uiManager;
    }

    /**
     * Called every frame while a player interacts with this RoboNPC.
     *
     * @param delta the time elapsed since the last update
     * @return true if update should continue
     */
    @Override
    public boolean updateInteracting(float delta) {
        return uiManager.updateDialogue(delta);
    }

    /**
     * Called when a player has finished interacting with this RoboNPC.
     */
    @Override
    public void action(GameWorld gameWorld) {
        Assets.sfxBattleStart.play(Game.masterVolume);
        uiManager.addNotification("Robo Duck has been defeated.");
        BattleParameters params = new BattleParameters(Game.segment);

        // List of enemy ducks.
        List<Integer> emptyList = new ArrayList<Integer>();
        Agent enemyDuck = new Agent("Robo Duck", Agent.AgentType.ENEMY, new Statistics(250, 500, 8, 2, 3, 3, 3, 201, 9), emptyList, new CurrentEquipment(0, 0, 0, 0, 0), 1);
        enemyDuck.addSkill(4);
        params.addEnemy(enemyDuck);
        gameWorld.setBattle(params);

        // If Roboduck is dead then the player has completed the objective.
        if (enemyDuck.isDead() && Game.objective instanceof DefeatRoboduckObjective) {
            ((DefeatRoboduckObjective) Game.objective).roboduckDefeated();
        }

        level.characters.remove(this);
    }

    /**
     * Not implemented.
     *
     * @param delta the time elapsed since the last update
     */
    @Override
    protected void updateTransitioning(float delta) {
    }

    /**
     * Not implemented.
     *
     * @param delta the time elapsed since the last update
     */
    @Override
    protected void updateStationary(float delta) {
    }
}
