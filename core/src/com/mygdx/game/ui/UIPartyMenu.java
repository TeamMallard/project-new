package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Game;
import com.mygdx.game.InputHandler;
import com.mygdx.game.PartyManager;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the party menu in the overworld.
 */
public class UIPartyMenu extends UIComponent {

    /**
     * The party.
     */
    private PartyManager party;

    /**
     * Whether the party menu is being displayed.
     */
    private boolean show;

    /**
     * Which player/tab is currently selected.
     */
    private int playerSelected, menuSelected;

    /**
     * List of players to be displayed in the party menu.
     */
    private List<UIPlayer> playerList;

    /**
     * Whether or not input is currently focused on this component.
     */
    private boolean hasFocus;

    /**
     * The instance of the equipment menu.
     */
    private UIEquipmentMenu equipmentMenu;

    /**
     * The instance of the consumable menu.
     */
    private UIConsumableMenu consumableMenu;

    /**
     * Labels for the titles on the party menu.
     */
    private UIMessageBox[] messageBoxes;

    /**
     * Creates a new UIPartyMenu with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the menu
     * @param height the height of the menu
     * @param party  the party
     */
    public UIPartyMenu(float x, float y, float width, float height, PartyManager party) {
        super(x, y, width, height);
        this.party = party;
        show = false;
        playerSelected = 0;
        menuSelected = 1;
        playerList = new ArrayList<UIPlayer>();
        for (int i = 0; i < party.size(); i++) {
            playerList.add(new UIPlayer(x, (y + height - 70) - (110 * i), width / 2, party.getMember(i)));
        }

        equipmentMenu = new UIEquipmentMenu(x + width / 2, (y + height - 71), width / 2, 150, this);
        consumableMenu = new UIConsumableMenu(x + width / 2, (y + height - 71), width / 2, 150, this);

        messageBoxes = new UIMessageBox[4];
        messageBoxes[0] = new UIMessageBox("STATS", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + width / 2, (y + height + 4), width / 8, 0, 10);
        messageBoxes[1] = new UIMessageBox("SKILLS", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + width / 2 + width / 8, (y + height + 4), width / 8, 0, 10);
        messageBoxes[2] = new UIMessageBox("EQUIPMENT", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + width / 2 + width / 4, (y + height + 4), width / 8, 0, 10);
        messageBoxes[3] = new UIMessageBox("CONSUMABLE", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x + width / 2 + 3 * (width / 8), (y + height + 4), width / 8, 0, 10);
    }

    /**
     * Renders this UIPartyMenu onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        if (show) {
            new UIMessageBox("", Assets.consolas22, Color.WHITE, Align.center, x, y, width, height).render(batch, patch);
            for (UIPlayer aPlayerList : playerList) {
                aPlayerList.render(batch, patch);
            }

            switch (menuSelected) {
                case 0:
                    new UIStats(x + width / 2, (y + height - 386), width / 2, party.getMember(playerSelected)).render(batch, patch);
                    break;
                case 1:
                    for (int i = 0; i < party.getMember(playerSelected).getSkills().size(); i++) {
                        new UISkill(x + width / 2, (y + height - 86) - (90 * i), width / 2, Game.skills.getSkill(party.getMember(playerSelected).getSkills().get(i))).render(batch, patch);
                    }
                    break;
                case 2:
                    equipmentMenu.render(batch, patch);
                    break;
                case 3:
                    consumableMenu.render(batch, patch);
                    break;
            }

            for (int i = 0; i < messageBoxes.length; i++) {
                messageBoxes[i].setColor(menuSelected == i ? Color.WHITE : Color.LIGHT_GRAY);
                messageBoxes[i].render(batch, patch);
            }
        }
    }

    /**
     * Makes the UI component visible on screen.
     */
    public void show() {
        // Initial select the first player in the menu.
        playerSelected = 0;
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).selected = i == 0;
        }

        menuSelected = 1;
        hasFocus = true;
        show = true;
    }

    /**
     * Focuses input on this UIPartyMenu.
     */
    public void focus() {
        hasFocus = true;
    }

    /**
     * @return the party that this menu is showing
     */
    public PartyManager getParty() {
        return party;
    }

    /**
     * Updates the state of this UIConsumableMenu.
     *
     * @return true if the dialogue box should continue to be displayed
     */
    public boolean update() {
        if (InputHandler.isEscJustPressed()) {
            show = false;
            return false;
        } else {
            if (hasFocus) {
                playerList.get(playerSelected).selected = false;
                optionUpdate();
                playerList.get(playerSelected).selected = true;
            } else if (equipmentMenu.hasFocus()) {
                equipmentMenu.update();
            } else if (consumableMenu.hasFocus()) {
                consumableMenu.update();
            }

            return true;
        }
    }

    /**
     * Handles user input to update which player/tab is currently selected.
     */
    private void optionUpdate() {
        if (InputHandler.isUpJustPressed() && playerSelected > 0) {
            playerSelected--;
        } else if (InputHandler.isDownJustPressed() && playerSelected < playerList.size() - 1) {
            playerSelected++;
        }
        equipmentMenu.selectPlayer(playerSelected);
        consumableMenu.selectPlayer(playerSelected);

        if (InputHandler.isLeftJustPressed()) {
            menuSelected--;
        } else if (InputHandler.isRightJustPressed()) {
            menuSelected++;
        }

        // Send focus to equipment menu.
        if (InputHandler.isActJustPressed() && menuSelected == 2) {
            hasFocus = false;
            equipmentMenu.focus();
        }
        if (InputHandler.isActJustPressed() && menuSelected == 3) {
            hasFocus = false;
            consumableMenu.focus();
        }

        if (menuSelected < 0)
            menuSelected = 0;
        if (menuSelected > 3)
            menuSelected = 3;
        if (playerSelected < 0)
            playerSelected = 0;
        if (playerSelected >= party.size())
            playerSelected = party.size() - 1;
    }
}
