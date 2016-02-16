package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleParameters;
import com.mygdx.game.battle.BattleScreen;
import com.mygdx.game.objective.CollectItemObjective;
import com.mygdx.game.objective.DefeatRoboduckObjective;
import com.mygdx.game.objective.Objective;
import com.mygdx.game.objective.WinBattlesObjective;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is used to switch between the StartScreen, WorldScreen and BattleScreen.
 * It also stores the instances for the friendly party and all items and skills.
 * More information can be found at http://www.teampochard.co.uk/game-releases/
 */
public class Game extends com.badlogic.gdx.Game {


    public static PartyManager party;
    public static PartyManager enemies;
    public static ItemManager items;
    public static SkillManager skills;
    public static ShopManager shops;
    public static Objective objective;
    private static JsonLoader jsonLoader = new JsonLoader();
    public static int pointsScore = 0;
    public static int segment = 0;

    public static float masterVolume = 0.1f;

    public static WorldScreen worldScreen;
    private BattleScreen battleScreen;

    public boolean wonBattle;


    @Override
    public void create() {
        loadFiles();
        Assets.load();
        wonBattle = false;
        setScreen(new StartScreen(this));

        // TODO: change where objective is assigned
    }

    /**
     * Loads json files for the party, items and skills.
     */
    private void loadFiles() {
        try {
            skills = jsonLoader.parseSkillManager("skills.json");
            items = jsonLoader.parseItemManager("items.json");
            party = jsonLoader.parsePartyManager("party.json");
            enemies = jsonLoader.parsePartyManager("enemies.json");
            shops = jsonLoader.parseShopManager("shops.json");
            System.out.println(shops.toString());
        } catch (FileNotFoundException ex) {
            // Do something with 'ex'
        } catch (IOException ex2) {
            // Do something with 'ex2'
        }
    }

    /**
     * Called when a battle has ended.
     *
     * @param won True if the player won the battle.
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
     * Creates a new battle and sets the battleScreen as the current screen.
     *
     * @param battleParams
     */
    public void newBattle(BattleParameters battleParams) {
        battleScreen = new BattleScreen(this, battleParams);
        setScreen(battleScreen);
    }

    /**
     * Used when switching to the worldScreen from the startScreen.
     */
    public void newWorldScreen() {
        worldScreen = new WorldScreen(this);
        setScreen(worldScreen);
        setObjective();
    }
    
    public static void setObjective() {
		for(int i = 0; i < Game.party.size(); i++)
			Game.party.getMember(i).getStats().increaseXP(Game.segment*5);
    	switch(segment) {
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