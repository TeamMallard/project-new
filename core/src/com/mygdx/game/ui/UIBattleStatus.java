package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Agent;
import com.mygdx.game.PartyManager;
import com.mygdx.game.Statistics;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the party's status display during battle.
 */
public class UIBattleStatus extends UIComponent {

    /**
     * The list of text lines to draw.
     */
    public List<String> textList;

    /**
     * Which agent's turn it is.
     */
    private int selected = 0;

    /**
     * The party.
     */
    private PartyManager party;

    /**
     * Text padding.
     */
    private float paddingX, paddingY;

    /**
     * Creates a new UIBattleStatus with the specified parameters.
     *
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param width    the width of the menu
     * @param height   the height of the menu
     * @param paddingX horizontal text padding
     * @param paddingY vertical text padding
     * @param party    the party
     */
    public UIBattleStatus(float x, float y, float width, float height, float paddingX, float paddingY, PartyManager party) {
        super(x, y, width, height);
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        this.party = party;

        textList = new ArrayList<String>();
    }

    /**
     * Fills the textList with alternating strings of agent names and agent stats.
     */
    private void listToString() {
        textList.clear();
        for (int x = 0; x < party.size(); x++) {
            Agent thisAgent = party.getMember(x);
            Statistics thisAgentStats = thisAgent.getStats();
            textList.add(thisAgent.getName());
            textList.add("HP:" + thisAgentStats.getCurrentHP() + "/" + thisAgentStats.getMaxHP() + " MP:" + thisAgentStats.getCurrentMP() + "/" + thisAgentStats.getMaxMP());
        }
    }

    /**
     * Sets the current turn agent to the specified index.
     *
     * @param selected the index of the agent in the party
     */
    public void selectAgent(int selected) {
        this.selected = selected;
    }

    /**
     * Renders this UIBattleStatus onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        listToString();
        float thisX = x;
        float thisY = y;

        //Draws the background box
        patch.draw(batch, x, y, width, height + (paddingY * 2));

        //The even indexes are the Agent names and the odd indexes are hp & mana so the two are rendered differently
        for (int x = 0; x < textList.size(); x++) {
            if (x == selected * 2) {
                renderText(batch, textList.get(x), thisX, thisY, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            } else if (x % 2 != 0) {
                renderText(batch, textList.get(x), thisX + 5, thisY - 2, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas16); //Render the odd indexes with smaller font
            } else {
                renderText(batch, textList.get(x), thisX, thisY, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
            }
            thisY -= 23;
        }

    }
}
