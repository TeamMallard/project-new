package com.mygdx.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

import java.util.*;

/**
 * The BattleScreen controls all the systems for the Battle system, including turn order, skill use and drawing agents to the screen.
 * (0,0) is at the top left corner. Moving positively in x goes left to right. Moving Negatively in y goes top to bottom.
 */
public class BattleScreen extends ScreenAdapter {

    public Game game;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BattleMenu battleMenu = new BattleMenu(this);
    public BattleAnimator battleAnimator = new BattleAnimator();

    int backgroundNumber;//the background to use

    PartyManager enemyParty = new PartyManager();

    List<Agent> turnOrder = new ArrayList<Agent>();
    int turnOrderPointer = 0;
    private Agent currentTurnAgent;


    List<Integer> emptyList = new ArrayList<Integer>();

    UseAbility currentUseAbility;

    boolean enemyHasUsedSkill= false;

    boolean isBattleOver = false;
    boolean isBattleWon;

    public BattleScreen (Game game, BattleParameters battleParams) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        initialiseBattleScreen(battleParams);

    }

    public void initialiseBattleScreen(BattleParameters battleParams){
        //Set (0,0) to be the top left corner
        camera.translate(Gdx.graphics.getWidth()/2,-(Gdx.graphics.getHeight()/2));
        camera.update();

        backgroundNumber = battleParams.getBackground();

        //Adding new party member test
/*        Agent tomDuck = new Agent("Tom the Duck", Agent.AgentType.FRIENDLY,new Statistics(30,10,2,2,2,2,2,2,2),list1,new CurrentEquipment(0,0,0,0,0));
        Game.party.addMember(tomDuck);
*/

        enemyParty = battleParams.getEnemyParty();

        determineTurnOrder();
        assignInitialPositions();

        //json.toJson(Game.items.getEquipment(0), Equipment.class, new FileHandle("E:\\My Documents\\Uni Stuff\\Second Year\\SEPR\\PrivateDev\\core\\assets\\items.json"));


        //Initialise the size of the battlemenu menus
        battleMenu.setItemMenuSize(Game.party.getConsumables().size());
        battleMenu.updateBattleLayout();

        //System.out.println("turnorder: "+turnOrder);

        Assets.battleMusic.setVolume(Game.masterVolume+0.3f);
        Assets.battleMusic.play();
        startTurn();

    }

    /**
     * Uses the turnorder and agent type to assign positions on the screen to each agent in the battle.
     */
    private void assignInitialPositions(){
        int friendlyPointer=0;
        int enemyPointer= 0;

        for (int i = turnOrder.size()-1; i >= 0; i--) {
            Agent thisAgent = turnOrder.get(i);
            if(thisAgent.type== Agent.AgentType.FRIENDLY){
                thisAgent.setX(Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/6)-(friendlyPointer*50));
                thisAgent.setY(assignInitialYPositions(friendlyPointer));
                friendlyPointer++;
            }
            else{
                thisAgent.setX(Gdx.graphics.getWidth()/6+(enemyPointer*50));
                thisAgent.setY(assignInitialYPositions(enemyPointer));
                enemyPointer++;
            }
        }
    }
    private float assignInitialYPositions(int index){
        return (((1+index)*Gdx.graphics.getHeight())/12)-Gdx.graphics.getHeight()+190;
    }

    /**
     * Fills the turnOrder list with the agents in the battle, ordered based on speed
     * with highest speed first.
     */
    private void determineTurnOrder(){

        for(int index = 0; index<Game.party.size();index++){
            turnOrder.add(Game.party.getMember(index));
        }
        for(int index = 0; index<enemyParty.size();index++){
            turnOrder.add(enemyParty.getMember(index));
        }
        //Calls the custom sort function defined in the Agent class
        Collections.sort(turnOrder);
    }

    /**
     * The Main Update Loop for the battle system. Checks inputs and updates the menus with them as well as
     * updating other classes.
     * @param delta the delta time between frames.
     */
    public void update (float delta) {
        InputHandler.update();

        if(!isBattleOver) {
            //Check inputs if the current turn agent is friendly
            if (currentTurnAgent.type == Agent.AgentType.FRIENDLY) {

                //Input checks
                if (InputHandler.isActJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.ACT);
                } else if (InputHandler.isEscJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.ESC);
                } else if (InputHandler.isUpJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.UP);
                } else if (InputHandler.isDownJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.DOWN);
                } else if (InputHandler.isLeftJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.LEFT);
                } else if (InputHandler.isRightJustPressed()) {
                    battleMenu.newKeypress(InputHandler.inputType.RIGHT);
                }

            } else {
                if (!enemyHasUsedSkill) {
                    //Enemy targetting
                    currentUseAbility = new UseSkill(currentTurnAgent, turnOrder.get(getTarget(Agent.AgentType.FRIENDLY)), currentTurnAgent.getSkills().get(0), battleMenu);
                    enemyHasUsedSkill = true;
                }
            }
        }

        battleMenu.update(delta);
        battleAnimator.update(delta);
        if(currentUseAbility!=null)
            currentUseAbility.update(delta);

    }

    /**
     * Rudimentary targeting for the AI agents.
     * @param typeToGet The type of agent to target.
     */
    public int getTarget(Agent.AgentType typeToGet){
        Random random = new Random();
        int index = random.nextInt(turnOrder.size());
        if(turnOrder.get(index).type != typeToGet || turnOrder.get(index).isDead())
            return getTarget(typeToGet);
        else
            return index;

    }


    /**
     * Ends the current turn, checks for win/lose conditions. If conditions met, start the battleResults dialogs.
     */
    public void endTurn(){
        if(turnOrder.get(turnOrderPointer).type== Agent.AgentType.FRIENDLY) {
            turnOrder.get(turnOrderPointer).setX(turnOrder.get(turnOrderPointer).getX() + 10);
            battleMenu.resetMenus(); //Reset battleUI menus
        }
        //Check for win/lose condition
        if(enemyParty.isDead()){ //Win battle
            setBattleResults(true);
            isBattleOver = true;
            isBattleWon = true;
        }
        else if(Game.party.isDead()){ //Lose battle
            //END BATTLE
            setBattleResults(false);
            isBattleOver = true;
            isBattleWon = false;
        }
        else {
            nextTurn();
        }
    }

    /**
     * Creates the Text for and applies the end of battle results/rewards.
     * XP gain is currently calculated by the sum of each enemy's level multiplied by 3.
     * A slight random amount is also applied to each individual xp amount.
     * Dead players don't recieve xp.
     * @param battleIsWon true if the player won, false otherwise.
     */
    private void setBattleResults(boolean battleIsWon){

        List<String> resultsText = new ArrayList<String>();
        Assets.battleMusic.stop();
        if(battleIsWon) {
            Assets.sfx_battleWin.play(Game.masterVolume+0.3f);
            resultsText.add("You Won!");

            //work out baseline xp to give to each Agent
            int xpGain= 0;
            for(int i=0; i<enemyParty.size(); i++){
                xpGain += enemyParty.getMember(i).getStats().getCurrentLevel()*3;
            }

            Random random = new Random();

            for(int i=0; i<Game.party.size(); i++) {
                Agent thisAgent = Game.party.getMember(i);

                //If the agent isn't dead, give the agent xp with a slight increase or decrease
                if(!thisAgent.isDead()){

                    int increaseXPReturn, thisXpGain;
                    thisXpGain = Math.round((float)xpGain * (random.nextFloat()%0.1f+1));
                    increaseXPReturn = thisAgent.getStats().increaseXP(thisXpGain);
                    resultsText.add(thisAgent.getName() +" received "+thisXpGain +" experience.");

                    //If levelled up add message
                    if(increaseXPReturn>0){
                        resultsText.add(thisAgent.getName() +" levelled up to level "+thisAgent.getStats().getCurrentLevel() +".");
                    }

                }
                else{
                    thisAgent.getStats().increaseHP(1);//Set dead Agent's hp to 1
                }
            }
            String messages[] = new String[resultsText.size()];
            for(int i=0; i<resultsText.size(); i++){
                messages[i]=resultsText.get(i);
            }
            battleMenu.addResultsDialog(messages);

            Game.pointsScore+=xpGain*3.5f;
        } else {
            resultsText.add("You Lost.");
            Assets.sfx_battleLose.play(Game.masterVolume+0.3f);
            String messages[] = new String[resultsText.size()];
            for(int i=0; i<resultsText.size(); i++){
                messages[i]=resultsText.get(i);
            }
            battleMenu.addResultsDialog(messages);
            Game.pointsScore -= Game.pointsScore * 0.25;
        }
    }

    /**
     * Returns the game to the world screen if the battle is over.
     */
    public void endBattle(){
        if(isBattleOver){
            if(isBattleWon)
                game.returnToWorld(true);
            else
                game.returnToWorld(false);
        }
    }

    /**
     * Sets up next turn, ensuring that the agent with the next turn isn't dead.
     */
    private void nextTurn(){

        //Increment turn pointer and wrap it around if out of range
        turnOrderPointer++;
        if(turnOrderPointer>=turnOrder.size()){
            turnOrderPointer=0;
        }

        if(turnOrder.get(turnOrderPointer).isDead()) //Skip turn if the agent is dead
            nextTurn();
        else {
            enemyHasUsedSkill = false;
            startTurn();
        }
    }

    /**
     * Starts next turn.
     * If the this turn's agent is friendly, adjust their location and move the turnIndicator.
     */
    public void startTurn(){
        if(turnOrder.get(turnOrderPointer).type== Agent.AgentType.FRIENDLY) {
            turnOrder.get(turnOrderPointer).setX(turnOrder.get(turnOrderPointer).getX() - 20);

            //Set battleMenu skill list size for new current agent's turn
            battleMenu.setSkillMenuSize(turnOrder.get(turnOrderPointer).getSkills().size());
            battleMenu.resetMenus(); //Reset the menus
        }
        //if(!turnOrder.get(turnOrderPointer).isDead()) {
        	currentTurnAgent = turnOrder.get(turnOrderPointer);
        //} else {
        //	currentTurnAgent = turnOrder.get(turnOrderPointer)
        //}
        battleMenu.createInfoBox(currentTurnAgent.getName() + "'s turn", 10);
        battleMenu.updateTurnIndicator();
    }

    /**
     * Removes the target agent from the turnOrder list, adjusting turnOrderPointer if needed.
     * @param target the agent that is to be removed from the turnOrder.
     */
    public void removeFromBattle(Agent target){
        int index = -1;
        for (int i = 0; i < turnOrder.size(); i++) {
            if(turnOrder.get(i)==target){
                index=i;
                break;
            }
        }
        if(index>=0)
            removeFromBattle(index);
    }

    /**
     * Removes the Agent at given index from the turnOrder list, adjusting turnOrderPointer if needed.
     * @param index the index of the Agent to remove from the turnOrder list.
     */
    public void removeFromBattle(int index){
        turnOrder.remove(index);
        if(index<=turnOrderPointer){
            turnOrderPointer-=1;
        }
    }

    /**
     * Renders background and calls all other necessary render functions.
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
        battleMenu.render(delta, batch);
        batch.end();


    }

    public Agent getCurrentTurnAgent() {
        return currentTurnAgent;
    }
}
