package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Agent;
import com.mygdx.game.PartyManager;
import com.mygdx.game.Statistics;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * For creating and rendering the Party status UI element on the battlescreen.
 * Displays health and mana.
 */
public class UIBattleStatus extends UIComponent{

    public List<String> textList;
    private int selected = 0;
    private PartyManager party;

    float paddingX;
    float paddingY;

    private BitmapFont bigFont, smallFont;

    public UIBattleStatus(float x, float y, float width, float height, float paddingX, float paddingY, PartyManager party){
        super(x,y,width,height);
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        this.party = party;

        bigFont = Assets.consolas22;
        smallFont = Assets.consolas16;
    }

    /**
     * Fills the textList with alternating strings of agent names and agent stats.
     */
    private void listToString() {
        textList = new ArrayList<String>();
        for (int x = 0; x < party.size(); x++) {
            Agent thisAgent = party.getMember(x);
            Statistics thisAgentStats = thisAgent.getStats();
            textList.add(thisAgent.getName());
            textList.add("HP:"+thisAgentStats.getCurrentHP()+"/"+thisAgentStats.getMaxHP()+" MP:"+thisAgentStats.getCurrentMP()+"/"+thisAgentStats.getMaxMP());
        }
    }


    public void selectAgent(int selected) {
        this.selected = selected;
    }

    /**
     * Manages the rendering of the UI.
     * @param batch The Spritebatch to use.
     * @param patch The Ninepatch to use.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        listToString();
        float thisX= x;
        float thisY = y;

        //Draws the background box
        patch.draw(batch, x, y, width, height + (paddingY * 2));

        //The even indexes are the Agent names and the odd indexes are hp & mana so the two are rendered differently
        for (int x = 0; x < textList.size(); x++) {
            if (x==selected*2) {
                renderText(batch, textList.get(x), thisX, thisY, Color.WHITE, bigFont);
            }
            else if (x%2!=0){
                renderText(batch, textList.get(x), thisX+5, thisY-2, Color.LIGHT_GRAY, smallFont); //Render the odd indexes with smaller font
            }
            else {
                renderText(batch, textList.get(x), thisX, thisY, Color.LIGHT_GRAY, bigFont);
            }
            thisY-=23;
        }

    }

    /**
     * Helper function for render that actually does the rendering.
     * @param batch the spritebatch to use.
     * @param message The string to add.
     * @param x The x location.
     * @param y The y location.
     * @param color The colour to render the text as.
     * @param font The font to use.
     */
    private void renderText(SpriteBatch batch, String message, float x, float y, Color color, BitmapFont font) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, Align.left, false);

        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, Align.left, false);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }
}
