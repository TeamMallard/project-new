package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleParameters;
import com.mygdx.game.battle.BattleScreen;
import com.mygdx.game.objective.CollectItemObjective;
import com.mygdx.game.objective.DefeatRoboduckObjective;
import com.mygdx.game.objective.Objective;
import com.mygdx.game.objective.WinBattlesObjective;

import java.io.IOException;

/**
 * The core game class. Everything starts here.
 */
public class Game extends com.badlogic.gdx.Game {

    /**
     * The friendly party.
     */
    public static PartyManager party;

    /**
     * The party manager containing the possible enemy agents.
     */
    public static PartyManager enemies;

    /**
     * Contains all items available in the game.
     */
    public static ItemManager items;

    /**
     * Contains all skills available in the game.
     */
    public static SkillManager skills;

    /**
     * Contains all shops accessible in the game.
     */
    public static ShopManager shops;

    /**
     * The current objective.
     */
    public static Objective objective;
    /**
     * How many points the player currently has.
     */
    public static int pointsScore = 0;
    /**
     * The map segment the player is currently in.
     */
    public static int segment = 0;

    /**
     * The JSON loader for loading item/agent information.
     */
    private static JsonLoader jsonLoader = new JsonLoader();
    /**
     * The master volume for game sounds.
     */
    public static float masterVolume = 0.1f;

    /**
     * The world screen.
     */
    public static WorldScreen worldScreen;

    /**
     * Whether the last battle was won by the player.
     */
    public boolean wonBattle;

    /**
     * Called when the game starts to load assets and display the game.
     */
    @Override
    public void create() {
        loadFiles();
        Assets.load();
        wonBattle = false;
        setScreen(new StartScreen(this));
    }

    /**
     * Loads all item/agent information from JSON files.
     */
    private void loadFiles() {
        try {
            skills = jsonLoader.parseSkillManager("skills.json");
            items = jsonLoader.parseItemManager("items.json");
            party = jsonLoader.parsePartyManager("party.json");
            enemies = jsonLoader.parsePartyManager("enemies.json");
            shops = jsonLoader.parseShopManager("shops.json");
        } catch (IOException ignored) {
        }
    }

    /**
     * Called when a battle has ended.
     *
     * @param won true if the player won the battle
     */
    public void returnToWorld(boolean won) {
        wonBattle = won;
        setScreen(worldScreen);
    }

    /**
     * Disposes of assets when game is no longer used.
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Creates a new battle and sets the battle screen as the current screen.
     *
     * @param battleParams the battle parameters
     */
    public void newBattle(BattleParameters battleParams) {
        BattleScreen battleScreen = new BattleScreen(this, battleParams);
        setScreen(battleScreen);
    }

    /**
     * Used when switching to from the start screen to the world screen.
     */
    public void newWorldScreen() {
        worldScreen = new WorldScreen(this);
        setScreen(worldScreen);
        setObjective();
    }

    /**
     * Used when switching to the win screen.
     */
    public void winScreen() {
        WinScreen winScreen = new WinScreen();
        setScreen(winScreen);
    }

    /**
     * CHANGE O2: Completing the objective unlocks the door for the next segment, and grants bonus XP.
     */

    /**
     * Updates party experience and changes the objective once a segment has been completed.
     */
    public static void setObjective() {
        for (int i = 0; i < Game.party.size(); i++)
            Game.party.getMember(i).getStats().increaseXP(Game.segment * 5);
        switch (segment) {
            case 0:
                objective = new WinBattlesObjective(1);
                break;
            case 1:
                objective = new CollectItemObjective(7, 1);
                break;
            case 2:
                objective = new WinBattlesObjective(3);
                break;
            case 3:
                objective = new CollectItemObjective(8, 1);
                break;
            case 4:
                objective = new WinBattlesObjective(5);
                break;
            case 5:
                objective = new CollectItemObjective(9, 1);
                break;
            case 6:
                objective = new WinBattlesObjective(7);
                break;
            case 7:
                objective = new DefeatRoboduckObjective();
                break;
        }
    }
}