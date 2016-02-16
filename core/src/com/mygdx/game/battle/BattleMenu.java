package com.mygdx.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the battle screen UI.
 */
public class BattleMenu {

    /**
     * The battle screen this BattleMenu is a part of.
     */
    public BattleScreen battleScreen;

    /**
     * Pointers for each of the menus.
     */
    private int baseMenuPointer = 0, skillMenuPointer = 0, itemMenuPointer = 0;

    /**
     * List of skills and items to be displayed.
     */
    private List<Integer> skillMenu = new ArrayList<Integer>(), itemMenu = new ArrayList<Integer>();

    /**
     * Locations of each agent on the screen.
     */
    private int[][] battleLayout = new int[4][2];

    /**
     * Pointers to the target agent.
     */
    private int targetMenuPointerRow = battleLayout.length - 1, targetMenuPointerColumn = 0;

    /**
     * Pointers to the active and previous menus (0: base, 1: skill, 2: item, 3: targeting).
     */
    private int menuPointer = 0, previousMenuPointer = 0;

    /**
     * Whether targeting is currently on the skill/item list.
     */
    private boolean isSkillTargeting = false, isItemTargeting = false;

    /**
     * Gets the last selected skill or item ID.
     */
    private int skillOrItemID = 0;

    /**
     * Where to draw the targeting indicator.
     */
    private float targetingIndicatorX, targetingIndicatorY;

    /**
     * Whether the targeting indicator should be flipped (to point to the enemies/party).
     */
    private boolean isTargetingIndicatorPointLeft = false;

    /**
     * Where to draw the current turn indicator.
     */
    private float turnIndicatorX = 0, turnIndicatorY = 0;

    /**
     * Whether to draw the turn indicator.
     */
    public boolean showTurnIndicator = false;

    /**
     * The UI manager for the battle UI.
     */
    private UIManager battleUI = new UIManager(Game.party);

    /**
     * The UI renderer for the battle UI manager.
     */
    private UIRenderer battleUIRenderer = new UIRenderer(battleUI);

    /**
     * The y offset to draw various components on the battle screen.
     */
    private float yOffset = -Gdx.graphics.getHeight();

    /**
     * The battle status display for the party.
     */
    private UIBattleStatus partyStatusList;

    /**
     * The skill and item menus.
     */
    private UIBattleSkillItemMenu skillUI, itemUI;

    /**
     * The information box display.
     */
    private UIMessageBox infoBox;

    /**
     * How long the information box should be displayed for.
     */
    private float infoBoxTimer;

    /**
     * The dialogue to show at the end of a battle.
     */
    private UIDialogue resultsDialog;

    /**
     * Whether the results dialogue is displayed.
     */
    private boolean showResultsDialog = false;

    /**
     * The ability currently being used.
     */
    private UseAbility currentUseAbility;

    /**
     * Creates a new BattleMenu with the specified battle screen.
     *
     * @param battleScreen the battle screen this BattleMenu is a part of
     */
    public BattleMenu(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;

        partyStatusList = new UIBattleStatus(0, yOffset, Gdx.graphics.getWidth() / 5 + 10, Gdx.graphics.getHeight() / 4, 10, 10, Game.party);
        battleUI.addUIComponent(partyStatusList);

        UIBattleBaseMenu baseMenuList = new UIBattleBaseMenu(partyStatusList.getWidth(), yOffset, Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 5, 10, 10);
        baseMenuList.addListItem("Skills");
        baseMenuList.addListItem("Items");
        baseMenuList.selectItem(baseMenuPointer);
        battleUI.addUIComponent(baseMenuList);
    }

    /**
     * Renders this BattleMenu onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     */
    public void render(SpriteBatch batch) {
        battleUIRenderer.renderBattle(batch);
        if (menuPointer == 3) {
            batch.draw(Assets.selectArrow, targetingIndicatorX, targetingIndicatorY, Assets.selectArrow.getWidth(), Assets.selectArrow.getHeight(), 0, 0, Assets.selectArrow.getWidth(), Assets.selectArrow.getHeight(), isTargetingIndicatorPointLeft, false);
        }
        if (showTurnIndicator) {
            batch.draw(Assets.turnArrow, turnIndicatorX, turnIndicatorY);
        }
        if (showResultsDialog) {
            resultsDialog.render(batch, Assets.patch);
        }
    }

    /**
     * Updates the state of this BattleMenu.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        if (menuPointer == 3)
            updateTargetingIndicator();

        if (infoBoxTimer <= 0)
            destroyInfoBox();
        else {
            infoBoxTimer -= delta;
        }

        if (currentUseAbility != null) {
            currentUseAbility.update(delta);
            if (battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead() 
            		&& battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).getType() != battleScreen.getCurrentTurnAgent().type
            		&& !battleScreen.enemyParty.isDead()) {
                targetingMenuInput(InputHandler.inputType.UP);
            }
        }

        if (showResultsDialog) { //Update resultsDialog if should be shown
            boolean resultsFinished = resultsDialog.update(delta);
            if (!resultsFinished) {
                battleScreen.endBattle();
            }
        }
    }

    /**
     * Updates the position of the targeting indicator.
     */
    private void updateTargetingIndicator() {
        Agent currentTarget = battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]);
        //Adjust the location and direction of the targeting indicator based on which side of the screen the agent to draw it to is on
        if (currentTarget.getX() >= Gdx.graphics.getWidth() / 2) {
            targetingIndicatorX = currentTarget.getX() - 80;
            isTargetingIndicatorPointLeft = false;
        } else {
            targetingIndicatorX = currentTarget.getX() + 20;
            isTargetingIndicatorPointLeft = true;
        }
        targetingIndicatorY = currentTarget.getY() - 25;

    }

    /**
     * Updates the position of the turn indicator.
     */
    public void updateTurnIndicator() {

        if (battleScreen.getCurrentTurnAgent().getType() == Agent.AgentType.FRIENDLY) {
            turnIndicatorX = battleScreen.getCurrentTurnAgent().getX() - 20;
            turnIndicatorY = battleScreen.getCurrentTurnAgent().getY() + 27;
            showTurnIndicator = true;

            partyStatusList.selectAgent(Game.party.getIndex(battleScreen.getCurrentTurnAgent()));
        } else {
            turnIndicatorX = -100;//move turn indicator offscreen
            turnIndicatorY = -100;
            showTurnIndicator = false;
            partyStatusList.selectAgent(-1);
        }
    }

    /**
     * Creates results dialog for displaying results of the battle.
     *
     * @param messages the list of messages to be displayed in sequence
     */
    public void addResultsDialog(String[] messages) {
        resultsDialog = new UIDialogue(Gdx.graphics.getWidth() / 10, -(Gdx.graphics.getHeight() / 4) * 2, (Gdx.graphics.getWidth() / 10) * 8, (Gdx.graphics.getHeight() / 12), messages, 0.25f);
        showResultsDialog = true;
    }


    /**
     * Creates an information box at the top of the screen containing the given text.
     *
     * @param text     the to be shown in the box
     * @param duration the duration the box should stay on screen for in seconds
     */
    public void createInfoBox(String text, float duration) {
        if (infoBox != null)
            destroyInfoBox();
        infoBox = new UIMessageBox(text, 0f, -90f, Gdx.graphics.getWidth(), 70, 10, 10);//FIX POSITIONING
        infoBoxTimer = duration;
        battleUI.addUIComponent(infoBox);
    }

    /**
     * Removes the information box from the screen.
     */
    public void destroyInfoBox() {
        if (infoBox != null) {
            battleUI.removeUIComponent(infoBox);
            infoBox = null;
        }
    }


    /**
     * Updates the layout of agents on the battle screen to reflect the turn order.
     */
    public void updateBattleLayout() {
        // Reset battle layout.
        for (int i = 0; i < battleLayout.length; i++) {
            for (int j = 0; j < battleLayout[i].length; j++) {
                battleLayout[i][j] = -1;
            }
        }

        int enemyPointer = 4 - battleScreen.enemyParty.size();
        int friendlyPointer = 4 - Game.party.size();
        for (int i = 0; i < battleScreen.turnOrder.size(); i++) {
            if (battleScreen.turnOrder.get(i).getType() == Agent.AgentType.FRIENDLY) {
                battleLayout[friendlyPointer][1] = i;
                friendlyPointer++;
            } else {
                battleLayout[enemyPointer][0] = i;
                enemyPointer++;
            }
        }
    }

    /**
     * Resets all menu pointers.
     */
    public void resetMenus() {
        baseMenuPointer = 0;
        skillMenuPointer = 0;
        itemMenuPointer = 0;
        menuPointer = 0;

        if (isSkillTargeting) { //reset the targeting type
            battleUI.removeUIComponent(skillUI); //remove the skillUI component
            isSkillTargeting = false;
        }

        if (isItemTargeting) { //reset the targeting type
            battleUI.removeUIComponent(itemUI); //remove the skillUI component
            isItemTargeting = false;
        }
    }

    /**
     * Updates the appropriate menu based on the previous input.
     *
     * @param input the input to update the menu with
     */
    public void newKeypress(InputHandler.inputType input) {

        switch (input) {
            case ACT:
                Assets.sfxMenuSelect.play(Game.masterVolume);
                break;
            case ESC:
                Assets.sfxMenuBack.play(Game.masterVolume);
                break;
            default:
                Assets.sfxMenuMove.play(Game.masterVolume);
        }

        switch (menuPointer) {
            case 0: {
                baseMenuInput(input);
                break;
            }
            case 1: {
                skillMenuInput(input);
                break;
            }
            case 2: {
                itemMenuInput(input);
                break;
            }
            case 3: {
                targetingMenuInput(input);
                break;
            }
        }

    }

    /**
     * Updates the base menu based on the previous input.
     *
     * @param input the input to update the menu with
     */
    private void baseMenuInput(InputHandler.inputType input) {
        switch (input) {
            case ACT: {
                menuPointer = baseMenuPointer + 1;//Set menuPointer to the relevant menu
                if (menuPointer == 1)
                    populateSkillUI();
                else
                    populateItemUI();
                break;
            }
            case UP: {
                if (baseMenuPointer != 0)
                    baseMenuPointer -= 1;
                break;
            }
            case DOWN: {
                if (baseMenuPointer != 1)//Would want to change this value to the max size of the baseMenu
                    baseMenuPointer += 1;
                break;
            }
        }
        ((UIBattleBaseMenu) battleUI.getUIComponent(1)).selectItem(baseMenuPointer); //Moves the selector indicator to the correct menu option
    }

    /**
     * Creates the skill list and populates it with available skills.
     */
    private void populateSkillUI() {
        skillUI = new UIBattleSkillItemMenu(battleUI.getUIComponent(0).getWidth() + battleUI.getUIComponent(1).getWidth(), yOffset, Gdx.graphics.getWidth() / 2 + 50, Gdx.graphics.getHeight() / 5, 20, 10);
        for (int i = 0; i < battleScreen.getCurrentTurnAgent().getSkills().size(); i++) {
            skillUI.addListItem(Game.skills.getSkill(battleScreen.getCurrentTurnAgent().getSkills().get(i)).getName());
        }
        battleUI.addUIComponent(skillUI);
        skillUI.selectItem(skillMenuPointer);
    }

    /**
     * Creates the item list and populates it with available items.
     */
    private void populateItemUI() {

        itemUI = new UIBattleSkillItemMenu(battleUI.getUIComponent(0).getWidth() + battleUI.getUIComponent(1).getWidth(), yOffset, Gdx.graphics.getWidth() / 2 + 50, Gdx.graphics.getHeight() / 5, 20, 10);
        for (int i = 0; i < Game.party.getConsumables().size(); i++) {
            itemUI.addListItem(Game.items.getConsumable(Game.party.getConsumables().get(i)).getName());
        }
        battleUI.addUIComponent(itemUI);
        itemUI.selectItem(itemMenuPointer);
    }

    /**
     * Uses the given input to update the skill menu.
     *
     * @param input the input to update the menu with
     */
    private void skillMenuInput(InputHandler.inputType input) {
        int oldSkillMenuPointer = skillMenuPointer;

        switch (input) {
            case ACT: {
                skillOrItemID = battleScreen.getCurrentTurnAgent().getSkills().get(skillMenuPointer); //Set to the skillID of the skill that is currently selected
                setSkillTargeting(); //Set the targetting type to skill
                break;
            }
            case ESC: {
                menuPointer = 0;
                battleUI.removeUIComponent(skillUI);//Removes the skillUI component
                break;
            }
            case RIGHT: {
                if (skillMenuPointer % 2 == 0)
                    skillMenuPointer += 1;
                break;
            }
            case LEFT: {
                if (skillMenuPointer % 2 != 0)
                    skillMenuPointer -= 1;
                break;
            }
            case UP: {
                if (skillMenuPointer != 0 && skillMenuPointer != 1)
                    skillMenuPointer -= 2;
                break;
            }
            case DOWN: {
                if (skillMenuPointer != skillMenu.size() - 1 && skillMenuPointer != skillMenu.size() - 2)
                    skillMenuPointer += 2;
                break;
            }
        }

        // Don't allow selection of an item that isn't there.
        // This would be possible before due to there always being 4 skills.
        if (!skillUI.selectItem(skillMenuPointer)) {
            skillMenuPointer = oldSkillMenuPointer;
        }
    }

    /**
     * Uses the given input to update the item menu.
     *
     * @param input the input to update the menu with
     */
    private void itemMenuInput(InputHandler.inputType input) {
        switch (input) {
            case ACT: {
                skillOrItemID = Game.party.getConsumables().get(itemMenuPointer);
                setItemTargeting();
                break;
            }
            case ESC: {
                menuPointer = 0;
                battleUI.removeUIComponent(itemUI);
                break;
            }
            case RIGHT: {
                if (itemMenuPointer % 2 == 0)
                    itemMenuPointer += 1;
                break;
            }
            case LEFT: {
                if (itemMenuPointer % 2 != 0)
                    itemMenuPointer -= 1;
                break;
            }
            case UP: {
                if (itemMenuPointer != 0 && itemMenuPointer != 1)
                    itemMenuPointer -= 2;
                break;
            }
            case DOWN: {
                if (itemMenuPointer != itemMenu.size() - 1 && itemMenuPointer != itemMenu.size() - 2)
                    itemMenuPointer += 2;
                break;
            }

        }
        itemUI.selectItem(itemMenuPointer);

    }

    /**
     * Sets the current targeting type to skill.
     */
    private void setSkillTargeting() {
        previousMenuPointer = menuPointer;
        menuPointer = 3;
        isSkillTargeting = true;
    }

    /**
     * Sets the current targeting type to item.
     */
    private void setItemTargeting() {
        previousMenuPointer = menuPointer;
        menuPointer = 3;
        isItemTargeting = true;
    }

    /**
     * Uses the given input to update the targeting menu.
     *
     * @param input the input to update the menu with
     */
    private void targetingMenuInput(InputHandler.inputType input) {
        switch (input) {
            case ACT:
                if (isSkillTargeting) {
                    if (battleScreen.getCurrentTurnAgent().getType() == Agent.AgentType.FRIENDLY) {
                        if (battleScreen.getCurrentTurnAgent().getStats().getCurrentMP() - Game.skills.getSkill(0).getMPCost() < 0) {
                            createInfoBox(battleScreen.getCurrentTurnAgent().getName() + " does not have enough MP to use this skill", 3);
                            break;
                        }
                    }
                    currentUseAbility = new UseSkill(battleScreen.getCurrentTurnAgent(), battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]), skillOrItemID, this);
                } else if (isItemTargeting) {
                    currentUseAbility = new UseItem(battleScreen.getCurrentTurnAgent(), battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]), skillOrItemID, this);
                }
                break;
            case ESC:
                menuPointer = previousMenuPointer;
                isSkillTargeting = false;
                isItemTargeting = false;
                break;
            case RIGHT:
                if (targetMenuPointerColumn == 0 && battleLayout[targetMenuPointerRow][targetMenuPointerColumn + 1] != -1)
                    targetMenuPointerColumn += 1;
                break;
            case LEFT:
                if (targetMenuPointerColumn == 1)
                    targetMenuPointerColumn -= 1;
                if (battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                    targetingMenuInput(InputHandler.inputType.DOWN);
                } else if (battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead()) {
                    targetingMenuInput(InputHandler.inputType.DOWN);
                }
                break;
            case UP:
                targetMenuPointerRow -= 1;
                if (targetMenuPointerRow < 0)
                    targetMenuPointerRow = battleLayout.length - 1;
                if (battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                    targetingMenuInput(InputHandler.inputType.UP);
                } else if (battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead()
                        && battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).getType() != battleScreen.getCurrentTurnAgent().type) {
                    targetingMenuInput(InputHandler.inputType.UP);
                }
                break;
            case DOWN:
                targetMenuPointerRow += 1;
                if (targetMenuPointerRow > battleLayout.length - 1)
                    targetMenuPointerRow = 0;
                if (battleLayout[targetMenuPointerRow][targetMenuPointerColumn] == -1) {
                    targetingMenuInput(InputHandler.inputType.DOWN);
                } else if (battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).isDead()
                        && battleScreen.turnOrder.get(battleLayout[targetMenuPointerRow][targetMenuPointerColumn]).getType() != battleScreen.getCurrentTurnAgent().type) {
                    targetingMenuInput(InputHandler.inputType.DOWN);
                }
                break;

        }

    }

    /**
     * Sets the size of the skill menu.
     *
     * @param size the size
     */
    public void setSkillMenuSize(int size) {
        List<Integer> newMenu = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            newMenu.add(i);
        }
        skillMenu = newMenu;
    }

    /**
     * Sets the size of the item menu.
     *
     * @param size the size
     */
    public void setItemMenuSize(int size) {
        List<Integer> newMenu = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            newMenu.add(i);
        }
        itemMenu = newMenu;
    }


}
