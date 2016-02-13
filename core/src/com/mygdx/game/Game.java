package com.mygdx.game;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.battle.BattleParameters;
import com.mygdx.game.battle.BattleScreen;
import com.mygdx.game.objective.DefeatRoboduckObjective;
import com.mygdx.game.objective.Objective;

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
	private static JsonLoader jsonLoader = new JsonLoader();
	public static int pointsScore=0;

	public static Objective objective;

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
		objective = new DefeatRoboduckObjective("Roboduck");
	}

	/**
	 * Loads json files for the party, items and skills.
	 */
	private void loadFiles(){
		try {
			skills = jsonLoader.parseSkillManager("skills.json");
			items = jsonLoader.parseItemManager("items.json");
			party = jsonLoader.parsePartyManager("party.json");
			enemies = jsonLoader.parsePartyManager("enemies.json");
			shops = jsonLoader.parseShopManager("shops.json");
			System.out.println(shops.toString());
		}
		catch (FileNotFoundException ex) {
			// Do something with 'ex'
		} catch (IOException ex2) {
			// Do something with 'ex2'
		}
	}

	/**
	 * Called when a battle has ended.
	 * @param won True if the player won the battle.
	 */
	public void returnToWorld(boolean won){
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
	}
}