package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.*;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * The party menu allows the user to see information about each party member.
 * It contains a party member's skills and statistics.
 */
public class UIPartyMenu extends UIComponent {

    private PartyManager party;
    private boolean show;

    private int playerSelected, menuSelected;

    private List<UIPlayer> playerList;

    /**
     * Labels for the titles on the party menu.
     */
    private UIMessageBox statsMessageBox = new UIMessageBox("STATS", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x+width/2, (y + height + 4), width/6, 0, 10);
    private UIMessageBox skillsMessageBox = new UIMessageBox("SKILLS", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x+width/2+width/6, (y + height +4), width/6, 0, 10);
    private UIMessageBox equipmentMessageBox = new UIMessageBox("EQUIPMENT", Assets.consolas22, Color.LIGHT_GRAY, Align.center, x+width/2+width/3, (y + height+4), width/6, 0, 10);

    public UIPartyMenu(float x, float y, float width, float height, PartyManager party) {
        super(x, y, width, height);
        this.party = party;
        show = false;
        playerSelected = 0;
        menuSelected = 1;
        playerList = new ArrayList<UIPlayer>();
        for (int i=0;i<party.size();i++) {
            playerList.add(new UIPlayer(x,(y + height - 70)-(110*i), width/2, party.getMember(i)));
        }
    }

    /**
     * Called once per frame to render the party menu.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {

        if (show) {
            new UIMessageBox("", Assets.consolas22, Color.WHITE, Align.center, x, y, width, height).render(batch, patch);
            for (UIPlayer aPlayerList : playerList) {
                aPlayerList.render(batch, patch);
            }

            statsMessageBox.setColor(Color.LIGHT_GRAY);
            skillsMessageBox.setColor(Color.LIGHT_GRAY);
            equipmentMessageBox.setColor(Color.LIGHT_GRAY);


            if (menuSelected == 0) {
                statsMessageBox.setColor(Color.WHITE);
                new UIStats(x + width/2, (y + height - 386), width/2, party.getMember(playerSelected)).render(batch, patch);
            }
            if (menuSelected == 1) {
                skillsMessageBox.setColor(Color.WHITE);
                for (int i=0;i<party.getMember(playerSelected).getSkills().size();i++) {
                    new UISkill(x + width/2, (y + height - 86)-(90*i), width/2, Game.skills.getSkill(party.getMember(playerSelected).getSkills().get(i))).render(batch, patch);
                }
            }
            if (menuSelected == 2) {
                equipmentMessageBox.setColor(Color.WHITE);
                for(int i = 0; i < party.getEquipables().size(); i++) {
                    Equipable equipable = Game.items.getEquipable(party.getEquipables().get(i));
                    new UIEquipment(x + width / 2, (y + height - 71)-(75*i), width/2, equipable).render(batch, patch);
                }
            }

            statsMessageBox.render(batch, patch);
            skillsMessageBox.render(batch, patch);
            equipmentMessageBox.render(batch, patch);

        }
    }

    /**
     * Makes the UI component visible on screen.
     */
    public void show() {
        playerSelected = 0;
        menuSelected = 1;
        show = true;
    }

    /**
     * Called once per frame to handle input logic for selecting a player and exiting the menu.
     * @return returns true if the dialogue box should continue to be displayed.
     */
    public boolean update(float delta) {
        if (InputHandler.isEscJustPressed()) {
            show = false;
            return false;
        } else {
            playerList.get(playerSelected).selected = false;
            optionUpdate();
            playerList.get(playerSelected).selected = true;
            return true;
        }

    }

    private void optionUpdate() {
        if (InputHandler.isUpJustPressed()) {
            playerSelected--;
        } else if (InputHandler.isDownJustPressed()) {
            playerSelected++;
        }
        if (InputHandler.isLeftJustPressed()) {
            menuSelected--;
        } else if (InputHandler.isRightJustPressed()) {
            menuSelected++;
        }
        if (menuSelected < 0)
            menuSelected = 0;
        if (menuSelected > 2)
            menuSelected = 2;
        if (playerSelected < 0)
            playerSelected = 0;
        if (playerSelected >= party.size())
            playerSelected = party.size()-1;
    }
}
