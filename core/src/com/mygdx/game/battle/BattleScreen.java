package com.mygdx.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.objective.CollectItemObjective;
import com.mygdx.game.objective.DefeatRoboduckObjective;
import com.mygdx.game.objective.WinBattlesObjective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Manages the battle display.
 */
public class BattleScreen extends ScreenAdapter {

    /**
     * The game this BattleScreen belongs to.
     */
    private Game game;

    /**
     * The sprite batch to render on.
     */
    private SpriteBatch batch;
    /**
     * The camera looking at the battle display.
     */
    private OrthographicCamera camera;
    /**
     * The battle menu instance.
     */
    private BattleMenu battleMenu = new BattleMenu(this);
    /**
     * The battle animator instance.
     */
    private BattleAnimator battleAnimator = new BattleAnimator();

    /**
     * The index of the background to use.
     */
    private int backgroundNumber;

    /**
     * The enemy party.
     */
    public PartyManager enemyParty = new PartyManager();

    /**
     * The order in which agents should have their turns.
     */
    public List<Agent> turnOrder = new ArrayList<Agent>();
    /**
     * The pointer to the current turn's agent.
     */
    private int turnOrderPointer = 0;
    /**
     * The current turn's agent.
     */
    private Agent currentTurnAgent;

    /**
     * The ability currently being used.
     */
    private UseAbility currentUseAbility;

    /**
     * Whether the current enemy has finished using their skill.
     */
    private boolean enemyHasUsedSkill = false;

    /**
     * Whether the battle is over and whether it is won (by the player).
     */
    private boolean isBattleOver = false, isBattleWon;

    /**
     * Creates a new BattleScreen with the specified parameters.
     *
     * @param game         the game this BattleScreen belongs to
     * @param battleParams the battle parameters to use
     */
    public BattleScreen(Game game, BattleParameters battleParams) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        initialiseBattleScreen(battleParams);

    }

    /**
     * Initialises this BattleScreen.
     *
     * @param battleParams the battle parameters to use
     */
    private void initialiseBattleScreen(BattleParameters battleParams) {
        //Set (0,0) to be the top left corner
        camera.translate(Gdx.graphics.getWidth() / 2, -(Gdx.graphics.getHeight() / 2));
        camera.update();

        backgroundNumber = battleParams.getBackground();
        enemyParty = battleParams.getEnemyParty();

        determineTurnOrder();
        assignInitialPositions();

        //Initialise the size of the battlemenu menus
        battleMenu.setItemMenuSize(Game.party.getConsumables().size());
        battleMenu.updateBattleLayout();

        Assets.battleMusic.setVolume(Game.masterVolume + 0.3f);
        Assets.battleMusic.play();
        startTurn();

    }

    /**
     * Assigns agents their initial positions based on the turn order.
     */
    private void assignInitialPositions() {
        int friendlyPointer = 0;
        int enemyPointer = 0;

        for (int i = turnOrder.size() - 1; i >= 0; i--) {
            Agent thisAgent = turnOrder.get(i);
            if (thisAgent.type == Agent.AgentType.FRIENDLY) {
                thisAgent.setX(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 6) - (friendlyPointer * 50));
                thisAgent.setY(assignInitialYPositions(friendlyPointer));
                friendlyPointer++;
            } else {
                thisAgent.setX(Gdx.graphics.getWidth() / 6 + (enemyPointer * 50));
                thisAgent.setY(assignInitialYPositions(enemyPointer));
                enemyPointer++;
            }
        }
    }

    /**
     * Calculates the initial y coordinate of an agent.
     *
     * @param index the y-index of the agent
     * @return the y coordinate
     */
    private float assignInitialYPositions(int index) {
        return (((1 + index) * Gdx.graphics.getHeight()) / 12) - Gdx.graphics.getHeight() + 190;
    }

    /**
     * Determines the turn order of agents based on their speed stat.
     */
    private void determineTurnOrder() {

        for (int index = 0; index < Game.party.size(); index++) {
            turnOrder.add(Game.party.getMember(index));
        }
        for (int index = 0; index < enemyParty.size(); index++) {
            turnOrder.add(enemyParty.getMember(index));
        }
        //Calls the custom sort function defined in the Agent class
        Collections.sort(turnOrder);
    }

    /**
     * Updates the state of this BattleMenu.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        if (!isBattleOver) {
            //Check inputs if the current turn agent is friendly
            if (currentTurnAgent.type == Agent.AgentType.FRIENDLY) {

                //Input checks
                if (InputHandler.isActJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.ACT);
                } else if (InputHandler.isEscJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.ESC);
                } else if (InputHandler.isUpJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.UP);
                } else if (InputHandler.isDownJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.DOWN);
                } else if (InputHandler.isLeftJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.LEFT);
                } else if (InputHandler.isRightJustPressed()) {
                    battleMenu.newKeypress(InputHandler.InputType.RIGHT);
                }

            } else {
                if (!enemyHasUsedSkill) {
                    currentUseAbility = new UseSkill(currentTurnAgent, turnOrder.get(getTarget(Agent.AgentType.FRIENDLY)), currentTurnAgent.getSkills().get(0), battleMenu);
                    enemyHasUsedSkill = true;
                }
            }
        }

        battleMenu.update(delta);
        battleAnimator.update(delta);
        if (currentUseAbility != null)
            currentUseAbility.update(delta);

    }

    /**
     * Gets a random target for use by enemy agents.
     *
     * @param typeToGet the type of target to get (friendly/enemy)
     * @return the index in the turn order of the target
     */
    public int getTarget(Agent.AgentType typeToGet) {
        Random random = new Random();
        int index = random.nextInt(turnOrder.size());
        if (turnOrder.get(index).type != typeToGet || turnOrder.get(index).isDead())
            return getTarget(typeToGet);
        else
            return index;

    }


    /**
     * Ends the current turn, checks for win/lose conditions. If conditions met, start the battleResults dialogs.
     */
    public void endTurn() {
        if (turnOrder.get(turnOrderPointer).type == Agent.AgentType.FRIENDLY) {
            turnOrder.get(turnOrderPointer).setX(turnOrder.get(turnOrderPointer).getX() + 10);
            battleMenu.resetMenus(); //Reset battleUI menus
        }
        //Check for win/lose condition
        if (enemyParty.isDead()) { //Win battle
            setBattleResults(true);
            isBattleOver = true;
            isBattleWon = true;
        } else if (Game.party.isDead()) { //Lose battle
            //END BATTLE
            setBattleResults(false);
            isBattleOver = true;
            isBattleWon = false;
        } else {
            nextTurn();
        }
    }

    /**
     * Displays the battle results dialogue and adds experience rewards.
     *
     * @param battleIsWon whether or not the battle was won
     */
    private void setBattleResults(boolean battleIsWon) {

        List<String> resultsText = new ArrayList<String>();
        Assets.battleMusic.stop();
        if (battleIsWon) {
            Assets.sfxBattleWin.play(Game.masterVolume + 0.3f);
            resultsText.add("You Won!");

            //work out baseline xp to give to each Agent
            int xpGain = 0;
            for (int i = 0; i < enemyParty.size(); i++) {
                xpGain += (int) (Math.log(enemyParty.getMember(i).getStats().getCurrentLevel()) * 3);
            }

            Random random = new Random();

            for (int i = 0; i < Game.party.size(); i++) {
                Agent thisAgent = Game.party.getMember(i);

                //If the agent isn't dead, give the agent xp with a slight increase or decrease
                if (!thisAgent.isDead()) {

                    int increaseXPReturn, thisXpGain;
                    thisXpGain = Math.round((float) xpGain * (random.nextFloat() % 0.1f + 1));
                    increaseXPReturn = thisAgent.getStats().increaseXP(thisXpGain);
                    resultsText.add(thisAgent.getName() + " received " + thisXpGain + " experience.");

                    //If levelled up add message
                    if (increaseXPReturn > 0) {
                        resultsText.add(thisAgent.getName() + " levelled up to level " + thisAgent.getStats().getCurrentLevel() + ".");
                    }

                } else {
                    thisAgent.getStats().increaseHP(1); //Set dead agent's hp to 1
                }
            }
            String messages[] = new String[resultsText.size()];
            for (int i = 0; i < resultsText.size(); i++) {
                messages[i] = resultsText.get(i);
            }
            battleMenu.addResultsDialog(messages);

            Game.pointsScore += xpGain * 3.5f;

            if (Game.objective instanceof WinBattlesObjective) {
                ((WinBattlesObjective) Game.objective).wonBattle();
            } else if (Game.objective instanceof CollectItemObjective) {
                for (int i = 0; i < enemyParty.size(); i++) {
                    if (enemyParty.getMember(i).getName().contains("Ooze") && Game.segment == 1) {
                        Game.party.getConsumables().add(7);
                    }
                    if (enemyParty.getMember(i).getName().contains("Duck") && Game.segment == 3) {
                        Game.party.getConsumables().add(8);
                    }
                    if (enemyParty.getMember(i).getName().contains("Duck") && Game.segment == 5) {
                        Game.party.getConsumables().add(9);
                    }
                }
            }
            if (Game.objective instanceof DefeatRoboduckObjective) {
                for (int i = 0; i < enemyParty.size(); i++) {
                    if (enemyParty.getMember(i).getName().contains("Robo")) {
                        ((DefeatRoboduckObjective) Game.objective).roboduckDefeated();
                        game.winScreen();
                    }
                }
            }
            Game.party.getConsumables().add(MathUtils.random(0, 6));
        } else {
            resultsText.add("You Lost.");
            Assets.sfxBattleLose.play(Game.masterVolume + 0.3f);
            String messages[] = new String[resultsText.size()];
            for (int i = 0; i < resultsText.size(); i++) {
                messages[i] = resultsText.get(i);
            }
            battleMenu.addResultsDialog(messages);
            Game.pointsScore -= Game.pointsScore * 0.25;
        }
    }

    /**
     * Returns the game to the world screen if the battle is over.
     */
    public void endBattle() {
        if (isBattleOver) {
            if (isBattleWon)
                game.returnToWorld(true);
            else
                game.returnToWorld(false);
        }
    }

    /**
     * Sets up next turn, ensuring that the agent with the next turn isn't dead.
     */
    private void nextTurn() {
        //Increment turn pointer and wrap it around if out of range
        turnOrderPointer++;
        if (turnOrderPointer >= turnOrder.size()) {
            turnOrderPointer = 0;
        }

        if (turnOrder.get(turnOrderPointer).isDead()) //Skip turn if the agent is dead
            nextTurn();
        else {
            enemyHasUsedSkill = false;
            startTurn();
        }
    }

    /**
     * Starts next turn. If the this turn's agent is friendly, adjust their location and move the turn indicator.
     */
    public void startTurn() {
        if (turnOrder.get(turnOrderPointer).type == Agent.AgentType.FRIENDLY) {
            turnOrder.get(turnOrderPointer).setX(turnOrder.get(turnOrderPointer).getX() - 20);

            //Set battleMenu skill list size for new current agent's turn
            battleMenu.setSkillMenuSize(turnOrder.get(turnOrderPointer).getSkills().size());
            battleMenu.resetMenus(); //Reset the menus
        }
        currentTurnAgent = turnOrder.get(turnOrderPointer);
        battleMenu.createInfoBox(currentTurnAgent.getName() + "'s turn", 10);
        battleMenu.updateTurnIndicator();
    }

    /**
     * Renders this BattleMenu onto the specified sprite batch.
     *
     * @param delta the time elapsed since the last render
     */
    public void render(float delta) {
        super.render(delta);
        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Assets.battleBackgrounds[backgroundNumber], 0, -Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        enemyParty.render(batch);
        Game.party.render(batch);
        battleMenu.render(batch);
        batch.end();
    }

    /**
     * @return the agent whose turn it currently is
     */
    public Agent getCurrentTurnAgent() {
        return currentTurnAgent;
    }
}
