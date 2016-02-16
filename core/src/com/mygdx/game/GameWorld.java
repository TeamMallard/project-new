package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleParameters;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.NPC;
import com.mygdx.game.entity.RoboNPC;
import com.mygdx.game.entity.SallyNPC;
import com.mygdx.game.ui.UIManager;
import com.mygdx.game.ui.UIObjective;
import com.mygdx.game.ui.UIScore;
import com.mygdx.game.ui.UIShop;

/**
 * Contains the logic for exploring the game world and triggering encounters.
 */
public class GameWorld {

    /**
     * The parent game.
     */
    public Game game;

    /**
     * The level that this GameWorld manages.
     */
    public Level level;

    /**
     * The UI manager to draw on.
     */
    public UIManager uiManager;

    /**
     * The current state of the game.
     */
    public GameState gameState;

    /**
     * The NPC currently being interacted with.
     */
    private NPC interactingNPC;

    /**
     * The current battle parameters.
     */
    private BattleParameters battleParams;

    /**
     * Used to calculate the chance of a battle encounter.
     */
    private int battleChance;

    /**
     * Creates a new GameWorld with the specified game parent.
     *
     * @param game the parent game
     */
    public GameWorld(Game game) {
        this.game = game;
        gameState = GameState.FREEROAM;
        level = new Level(this);
        uiManager = new UIManager(Game.party);
        battleChance = 2000;
        level.characters.add(new SallyNPC(level, new Vector2(84, 59)));
        level.characters.add(new SallyNPC(level, new Vector2(47, 49)));
        level.characters.add(new SallyNPC(level, new Vector2(21, 55)));
        level.characters.add(new SallyNPC(level, new Vector2(59, 77)));
        level.characters.add(new SallyNPC(level, new Vector2(89, 107)));
        level.characters.add(new SallyNPC(level, new Vector2(118, 87)));
        level.characters.add(new SallyNPC(level, new Vector2(157, 98)));
        level.characters.add(new SallyNPC(level, new Vector2(211, 74)));
        level.characters.add(new RoboNPC(level, new Vector2(222, 83)));
        uiManager.addUIComponent(new UIScore());
        uiManager.addUIComponent(new UIObjective());
    }

    /**
     * Updates the state of this GameWorld.
     */
    public void update(float delta) {
        level.update(delta);
        uiManager.updateNotification(delta);
        switch (gameState) {
            case BATTLE_DIALOGUE:
                if (!uiManager.updateDialogue(delta)) {
                    level.stopInput = false;
                    gameState = GameState.BATTLE;
                    game.newBattle(battleParams);
                }
                break;
            case FREEROAM:
                level.stopInput = false;

                if (level.player.getState() == Character.CharacterState.TRANSITIONING && MathUtils.random(battleChance--) == 0) {
                    triggerEncounter();
                } else if (InputHandler.isActJustPressed()) {
                    interactingNPC = level.player.interactingNPC;
                    level.stopInput = true;
                    if (interactingNPC != null) {
                        interactingNPC.initializeInteraction(delta, uiManager);
                        gameState = GameState.INTERACTION;
                    } else {
                        uiManager.openPartyMenu();
                        gameState = GameState.PARTY_MENU;
                    }
                }
                break;
            case PARTY_MENU:
                if (!uiManager.updatePartyMenu()) {
                    gameState = GameState.FREEROAM;
                }
                break;
            case SHOP_MENU:
                if (!uiManager.updateShop()) {
                    gameState = GameState.FREEROAM;
                }
                break;
            case INTERACTION:
                if (!interactingNPC.updateInteracting(delta)) {
                    interactingNPC.action(this);
                    if (gameState != GameState.SHOP_MENU)
                        gameState = GameState.FREEROAM;
                }
                break;
            case BATTLE:
                if (game.wonBattle) {
                    uiManager.addNotification("You won the battle!");
                } else {
                    Game.party.setHealths(1);
                    level.player.setCurrentTile(level.exits[Game.segment - 1]);
                    uiManager.addNotification("You lost the battle! You have been moved backwards.");
                }
                gameState = GameState.FREEROAM;
                break;
        }
    }

    /**
     * Triggers an encounter.
     */
    private void triggerEncounter() {
        uiManager.createDialogue(new String[]{"You have been stopped by a group of... somethings!"});
        level.stopInput = true;
        battleChance = 1000;
        BattleParameters params = new BattleParameters(Game.segment);

        //Get a number of agents from the list of enemies, make new agent instances with their information and setup the next battle
        for (int i = 0; i < MathUtils.random(1, 4); i++) {
            Agent thisAgent = Game.enemies.getMember(MathUtils.random(0, 2) + (3 * Game.segment));
            Statistics thisAgentStats = thisAgent.getStats();
            Statistics newStats = new Statistics(thisAgentStats.getMaxHP(), thisAgentStats.getMaxMP(), thisAgentStats.getSpeed(), thisAgentStats.getStrength(), thisAgentStats.getDexterity(), thisAgentStats.getIntelligence(), thisAgentStats.getArmourVal(), thisAgentStats.getExperience(), thisAgentStats.getCurrentLevel());
            params.addEnemy(new Agent(thisAgent.getName(), thisAgent.getType(), newStats, thisAgent.getSkills(), thisAgent.getCurrentEquipment(), thisAgent.getTexture()));
        }

        battleParams = params;
        Assets.worldMusic.stop();//Stop the worldMusic
        Assets.sfxBattleStart.play(Game.masterVolume);
        gameState = GameState.BATTLE_DIALOGUE;
        level.stopInput = true;
    }

    /**
     * Creates a new battle with the specified battle parameters.
     *
     * @param battleParams the battle parameters
     */
    public void setBattle(BattleParameters battleParams) {
        gameState = GameState.BATTLE;
        this.battleParams = battleParams;
        game.newBattle(battleParams);
    }

    /**
     * Creates and displays a shop with the specified shop items.
     *
     * @param shop the shop
     */
    public void setShop(UIShop shop) {
        System.out.println(gameState);
        uiManager.setShop(shop);
        uiManager.addUIComponent(shop);
        gameState = GameState.SHOP_MENU;
    }

    /**
     * Represents the current state of the game.
     */
    private enum GameState {
        FREEROAM, PARTY_MENU, SHOP_MENU, BATTLE, INTERACTION, BATTLE_DIALOGUE
    }
}
