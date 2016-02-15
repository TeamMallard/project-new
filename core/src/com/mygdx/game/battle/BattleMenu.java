package com.mygdx.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class manages the ui menus for the battlescreen. Stores data for them also updates and renders them.
 */
public class BattleMenu {

    //Declare variables
    public BattleScreen battleScreen;

    //Variables for menu state system
    private int baseMenuPointer = 0;
    private List<Integer> skillMenu = new ArrayList<Integer>();
    private int skillMenuPointer = 0;
    private List<Integer> itemMenu = new ArrayList<Integer>();
    private int itemMenuPointer = 0;

    private int[][] battleLayout = new int[4][2];
    private int targetMenuPointerRow = battleLayout.length-1,targetMenuPointerColumn=0;

    private int menuPointer = 0; //Menus in order: baseMenu=0, skillMenu=1, itemMenu=2, targetingMenu=3
    private int previousMenuPointer = 0;

    //Variables for the targeting system
    private boolean isSkillTargeting =false, isItemTargeting = false;
    private int skillOrItemID = 0;
    private float targetingIndicatorX,targetingIndicatorY;
    private boolean isTargetingIndicatorPointLeft = false;


    private float turnIndicatorX = 0, turnIndicatorY =0;//Locations of the turn indicator
    public boolean showTurnIndicator = false;

    private UIManager battleUI = new UIManager(Game.party);//Stores the UI elements
    private UIRenderer uiRenderer = new UIRenderer(battleUI);

    //Camera offset values
    private float xOffset=-Gdx.graphics.getWidth();
    private float yOffset=-Gdx.graphics.getHeight();

    private UIBattleBaseMenu baseMenuList;
    private UIBattleStatus partyStatusList;

    //Declare UIDoubleLists for the skill and item UI elements
    private UIBattleSkillItemMenu skillUI;
    private UIBattleSkillItemMenu itemUI;

    private UIMessageBox infoBox;
    private float infoBoxTimer;

    private UIDialogue resultsDialog;
    private boolean showResultsDialog=false;

    private UseAbility currentUseAbility;

    /**
     * BattleMenu Constructor. Creates the partyStatus UI which shows party members and their hp/mp.
     * Also creates the base menu that the user can navigate.
     * @param thisBattleScreen pass the BattleScreen so methods can be called.
     */
    public BattleMenu(BattleScreen thisBattleScreen) {
        battleScreen = thisBattleScreen;

        partyStatusList = new UIBattleStatus(0, yOffset, Gdx.graphics.getWidth()/5+10, Gdx.graphics.getHeight()/4, 10, 10, Game.party);
        battleUI.addUIComponent(partyStatusList);

        baseMenuList = new UIBattleBaseMenu(partyStatusList.width, yOffset, Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/5, 10, 10);
        baseMenuList.addListItem("Skills");
        baseMenuList.addListItem("Items");
        baseMenuList.selectItem(baseMenuPointer);
        battleUI.addUIComponent(baseMenuList);
    }

    /**
     * Renders the ui elements.
     * @param delta The time passed between frames.
     * @param batch The spritebatch to draw the elements to.
     */
    public void render(float delta, SpriteBatch batch) {
        uiRenderer.render(batch);
        if(menuPointer==3){
            batch.draw(Assets.selectArrow,targetingIndicatorX,targetingIndicatorY,Assets.selectArrow.getWidth(),Assets.selectArrow.getHeight(),0,0,Assets.selectArrow.getWidth(),Assets.selectArrow.getHeight(),isTargetingIndicatorPointLeft,false);
        }
        if(showTurnIndicator){
            batch.draw(Assets.turnArrow,turnIndicatorX,turnIndicatorY);
        }
        if(showResultsDialog){
            resultsDialog.render(batch, Assets.patch);
        }
    }

    /**
     * Updates the necessary UI elements. Including updating the targeting indicator if necessary.
     * @param delta the change in time between frames.
     */
    public void update(float delta) {
        if(menuPointer==3)
            updateTargetingIndicator();

        if(infoBoxTimer<=0)
            destroyInfoBox();
        else {
            infoBoxTimer -= delta;
        }

        if(currentUseAbility!=null) {
            currentUseAbility.update(delta);
        	if(battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead() && !battleScreen.enemyParty.isDead()) {
        		targetingMenuInput(InputHandler.inputType.UP);
        	}
        }

        if(showResultsDialog){ //Update resultsDialog if should be shown
            boolean resultsFinished = resultsDialog.update(delta);
            if(!resultsFinished) {
                battleScreen.endBattle();
            }
        }
    }

    /**
     * Updates the targeting indicator position, including if it needs to be flipped or not.
     */
    private void updateTargetingIndicator() {
        Agent currentTarget = battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]);
        //Adjust the location and direction of the targeting indicator based on which side of the screen the agent to draw it to is on
        if(currentTarget.getX()>=Gdx.graphics.getWidth()/2){
            targetingIndicatorX=currentTarget.getX()-80;
            isTargetingIndicatorPointLeft=false;
        }
        else{
            targetingIndicatorX=currentTarget.getX()+20;
            isTargetingIndicatorPointLeft=true;
        }
        targetingIndicatorY=currentTarget.getY()-25;

    }

    /**
     * Updates the indicator for which friendly agent's turn it is.
     * Also updates the partyStatus Menu's highlighted Agent.
     */
    public void updateTurnIndicator() {

        if(battleScreen.getCurrentTurnAgent().getType() == Agent.AgentType.FRIENDLY) {
            turnIndicatorX = battleScreen.getCurrentTurnAgent().getX()-20;
            turnIndicatorY = battleScreen.getCurrentTurnAgent().getY()+27;
            showTurnIndicator=true;

            partyStatusList.selectAgent(Game.party.getIndex(battleScreen.getCurrentTurnAgent()));
        }
        else{
            turnIndicatorX = -100;//move turn indicator offscreen
            turnIndicatorY = -100;
            showTurnIndicator=false;
            partyStatusList.selectAgent(-1);
        }
    }

    /**
     * Creates results dialog for displaying results of the battle.
     * @param messages The String array of text that will appear in individual boxes.
     */
    public void addResultsDialog(String[] messages) {
        resultsDialog = new UIDialogue(Gdx.graphics.getWidth()/10,-(Gdx.graphics.getHeight()/4)*2,(Gdx.graphics.getWidth()/10)*8,(Gdx.graphics.getHeight()/12),messages, 0.25f);
        showResultsDialog = true;
    }


    /**
     * Creates an information box at the top of the screen containing the given text.
     * @param text The String to be shown in the box.
     * @param duration The duration the box should stay on screen for in seconds.
     */
    public void createInfoBox(String text, float duration) {
        if(infoBox!=null)
            destroyInfoBox();
        infoBox = new UIMessageBox(text, 0f,-90f,Gdx.graphics.getWidth(),70,10,10);//FIX POSITIONING
        infoBoxTimer=duration;
        battleUI.addUIComponent(infoBox);
    }

    /**
     * Destroys the current InfoBox.
     */
    public void destroyInfoBox() {
        battleUI.removeUIComponent(infoBox);
        infoBox=null;
    }


    /**
     * Uses the turnOrder stored in the BattleScreen to fill the battleLayout array
     * so it matches the layout of the sprites on screen.
     */
    public void updateBattleLayout() {
        resetBattleLayout();
        int enemyPointer= 4-battleScreen.enemyParty.size();
        int friendlyPointer = 4-Game.party.size();
        for(int i=0; i<battleScreen.turnOrder.size();i++){
            if(battleScreen.turnOrder.get(i).getType()== Agent.AgentType.FRIENDLY){
                battleLayout[friendlyPointer][1]=i;
                friendlyPointer++;
            }
            else{
                battleLayout[enemyPointer][0]=i;
                enemyPointer++;
            }
        }
    }

    /**
     * Fills the battleLayout 2D integer array with -1 in each cell.
     */
    private void resetBattleLayout() {
        for(int i=0;i<battleLayout.length;i++){
            for(int j=0; j<battleLayout[i].length;j++){
                battleLayout[i][j]=-1;
            }
        }
    }

    /**
     * Resets all the menus pointers to 0.
     */
    public void resetMenus() {
        baseMenuPointer=0;
        skillMenuPointer=0;
        itemMenuPointer=0;
        menuPointer=0;

        if(isSkillTargeting = true) { //reset the targeting type
            battleUI.removeUIComponent(skillUI); //remove the skillUI component
            isSkillTargeting=false;
        }
        if(isItemTargeting = true) { //reset the targeting type
            battleUI.removeUIComponent(itemUI); //remove the skillUI component
            isItemTargeting=false;
        }
    }

    /**
     * Takes an input to update the Battle Menu with. Calls the relevant function based on the current menuPointer.
     * @param input The input to apply to the Battle Menu.
     */
    public void newKeypress(InputHandler.inputType input) {

        switch (input){
            case ACT:
                Assets.sfx_menuSelect.play(Game.masterVolume);
                break;
            case ESC:
                Assets.sfx_menuBack.play(Game.masterVolume);
                break;
            default:
                Assets.sfx_menuMove.play(Game.masterVolume);
        }

        switch (menuPointer){
            case 0:{
                baseMenuInput(input);
                break;
            }
            case 1:{
                skillMenuInput(input);
                break;
            }
            case 2:{
                itemMenuInput(input);
                break;
            }
            case 3:{
                targetingMenuInput(input);
                break;
            }
        }

    }

    /**
     * Uses the given input to update the baseMenu.
     * @param input the input to update the menu with.
     */
    private void baseMenuInput(InputHandler.inputType input) {
        switch (input){
            case ACT:{
                menuPointer=baseMenuPointer+1;//Set menuPointer to the relevant menu
                if(menuPointer==1)
                    populateSkillUI();
                else
                    populateItemUI();
                break;
            }
            case UP:{
                if(baseMenuPointer!=0)
                    baseMenuPointer-=1;
                break;
            }
            case DOWN:{
                if(baseMenuPointer!=1)//Would want to change this value to the max size of the baseMenu
                    baseMenuPointer+=1;
                break;
            }
        }
        ((UIBattleBaseMenu)battleUI.getUIComponent(1)).selectItem(baseMenuPointer); //Moves the selector indicator to the correct menu option
    }

    /**
     * Creates the skillUI using the skills of the Agent with the current turn.
     */
    private void populateSkillUI() {
        skillUI = new UIBattleSkillItemMenu(battleUI.getUIComponent(0).width+battleUI.getUIComponent(1).width,yOffset,Gdx.graphics.getWidth()/2+50,Gdx.graphics.getHeight()/5, 20, 10);
        for(int i=0; i<battleScreen.getCurrentTurnAgent().getSkills().size();i++){
            skillUI.addListItem(Game.skills.getSkill(battleScreen.getCurrentTurnAgent().getSkills().get(i)).getName());
        }
        battleUI.addUIComponent(skillUI);
        skillUI.selectItem(skillMenuPointer);
    }

    /**
     * Creates the itemUI using the consumable items the party currently has.
     */
    private void populateItemUI() {

        itemUI = new UIBattleSkillItemMenu(battleUI.getUIComponent(0).width+battleUI.getUIComponent(1).width,yOffset,Gdx.graphics.getWidth()/2+50,Gdx.graphics.getHeight()/5,20,10);
        for(int i=0; i<Game.party.getConsumables().size();i++){
            itemUI.addListItem(Game.items.getConsumable(Game.party.getConsumables().get(i)).getName());
        }
        battleUI.addUIComponent(itemUI);
        itemUI.selectItem(itemMenuPointer);
    }

    /**
     * Uses the given input to update the skillMenu based on a 2x2 grid system.
     * No menu wrapping is implemented e.g. pressing up when on the first element will not take you to the last element.
     * @param input the input to update the menu with.
     */
    private void skillMenuInput(InputHandler.inputType input) {
        switch (input){
            case ACT:{
                skillOrItemID=battleScreen.getCurrentTurnAgent().getSkills().get(skillMenuPointer); //Set to the skillID of the skill that is currently selected
                setSkillTargeting(); //Set the targetting type to skill
                break;
            }
            case ESC:{
                menuPointer=0;
                battleUI.removeUIComponent(skillUI);//Removes the skillUI component
                break;
            }
            case RIGHT:{
                if(skillMenuPointer%2==0)
                    skillMenuPointer+=1;
                break;
            }
            case LEFT:{
                if(skillMenuPointer%2!=0)
                    skillMenuPointer-=1;
                break;
            }
            case UP:{
                if(skillMenuPointer!=0 && skillMenuPointer!=1)
                    skillMenuPointer-=2;
                break;
            }
            case DOWN:{
                if(skillMenuPointer!=skillMenu.size()-1 && skillMenuPointer!= skillMenu.size()-2)
                    skillMenuPointer+=2;
                break;
            }
        }
        skillUI.selectItem(skillMenuPointer);

    }

    /**
     * Uses the given input to update the itemMenu based on a 2x2 grid system.
     * No menu wrapping is implemented e.g. pressing up when on the first element will not take you to the last element.
     * @param input the input to update the menu with.
     */
    private void itemMenuInput(InputHandler.inputType input){
        switch (input){
            case ACT:{
                skillOrItemID=Game.party.getConsumables().get(itemMenuPointer);
                setItemTargeting();
                break;
            }
            case ESC:{
                menuPointer=0;
                battleUI.removeUIComponent(itemUI);
                break;
            }
            case RIGHT:{
                if(itemMenuPointer%2==0)
                    itemMenuPointer+=1;
                break;
            }
            case LEFT:{
                if(itemMenuPointer%2!=0)
                    itemMenuPointer-=1;
                break;
            }
            case UP:{
                if(itemMenuPointer!=0 && itemMenuPointer!=1)
                    itemMenuPointer-=2;
                break;
            }
            case DOWN:{
                if(itemMenuPointer!=itemMenu.size()-1 && itemMenuPointer!= itemMenu.size()-2)
                    itemMenuPointer+=2;
                break;
            }

        }
       itemUI.selectItem(itemMenuPointer);

    }

    /**
     * Sets the targeting type to skill while storing the previous menu pointer as the same targeting function
     * is used for both skill and item targeting.
     */
    private void setSkillTargeting() {
        previousMenuPointer = menuPointer;
        menuPointer=3;
        isSkillTargeting =true;
    }

    /**
     * Sets the targeting type to skill while storing the previous menu pointer as the same targeting function
     * is used for both skill and item targeting
     */
    private void setItemTargeting() {
        previousMenuPointer = menuPointer;
        menuPointer=3;
        isItemTargeting =true;
    }

    /**
     * Uses the given input to update the targeting system.
     * Uses the battleLayout 2D array and row and column pointers.
     * You cannot wrap around the screen i.e. pressing left in the leftmost column won't select the rightmost column.
     */
    private void targetingMenuInput(InputHandler.inputType input){

        switch (input){
            case ACT:
                if(isSkillTargeting) {
                    if(battleScreen.getCurrentTurnAgent().getType()== Agent.AgentType.FRIENDLY){
                        if(battleScreen.getCurrentTurnAgent().getStats().getCurrentMP()-Game.skills.getSkill(0).getMPCost()<0){
                            createInfoBox(battleScreen.getCurrentTurnAgent().getName() + " does not have enough MP to use this skill", 3);
                            break;
                        }
                    }
                    currentUseAbility = new UseSkill(battleScreen.getCurrentTurnAgent(),battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]), skillOrItemID, this);
                } else if(isItemTargeting) {
                    currentUseAbility = new UseItem(battleScreen.getCurrentTurnAgent(),battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]), skillOrItemID, this);
                }
                break;
                
            case ESC:
                menuPointer = previousMenuPointer;
                isSkillTargeting = false;
                isItemTargeting = false;
                break;
                
            case RIGHT:
                if(targetMenuPointerColumn == 0 && battleLayout[targetMenuPointerRow][targetMenuPointerColumn+1] != -1)
                    targetMenuPointerColumn += 1;
                break;
                
            case LEFT:
            	System.out.println(targetMenuPointerRow + " " + targetMenuPointerColumn);
                if(targetMenuPointerColumn == 1)
                    targetMenuPointerColumn -= 1;
                if(battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                	targetingMenuInput(InputHandler.inputType.DOWN);
                } else if(battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead()) {
            		targetingMenuInput(InputHandler.inputType.DOWN);
            	}
                break;
                
            case UP:
            	targetMenuPointerRow -= 1;
            	//targetMenuPointerRow = targetMenuPointerRow % (battleLayout.length - 1);
            	if(targetMenuPointerRow < 0)
            		targetMenuPointerRow = battleLayout.length - 1;
                if(battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                	targetingMenuInput(InputHandler.inputType.UP);
                } else if(battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead() 
                    	&& battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).getType() != battleScreen.getCurrentTurnAgent().type) {
                	targetingMenuInput(InputHandler.inputType.UP);
                }
                break;
                
            case DOWN:
            	targetMenuPointerRow += 1;
            	//targetMenuPointerRow = targetMenuPointerRow % (battleLayout.length - 1);
            	if(targetMenuPointerRow > battleLayout.length - 1)
            		targetMenuPointerRow = 0;
                if(battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                	targetingMenuInput(InputHandler.inputType.DOWN);
                } else if(battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead() 
                    	&& battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).getType() != battleScreen.getCurrentTurnAgent().type) {
                	targetingMenuInput(InputHandler.inputType.DOWN);
                }
                break;

        }

    }

    /**
     * Sets the size of the skillMenu.
     * @param size the integer size for the skillMenu.
     */
    public void setSkillMenuSize(int size){
        List<Integer> newMenu = new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            newMenu.add(i);
        }
        skillMenu=newMenu;
    }

    /**
     * Sets the size of the itemMenu.
     * @param size the integer size for the itemMenu.
     */
    public void setItemMenuSize(int size){
        List<Integer> newMenu = new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            newMenu.add(i);
        }
        itemMenu=newMenu;
    }


}
