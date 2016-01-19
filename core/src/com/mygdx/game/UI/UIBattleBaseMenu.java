package com.mygdx.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * For creating the base menu containing the first options on the Battlescreen.
 * The selected item is highlighted and indicated by a '{@literal >}'.
 */
public class UIBattleBaseMenu extends UIComponent {
    public List<String> listItems;
    private int selected = 0;
    float paddingX,paddingY;

    public UIBattleBaseMenu(float x, float y, float width, float height, float paddingX, float paddingY) {
        super(x, y, width, height);
        this.paddingX=paddingX;
        this.paddingY=paddingY;

        listItems = new ArrayList<String>();
    }

    /**
     * Add an item to the list.
     * @param name The string to add to the list.
     */
    public void addListItem(String name) {
        listItems.add(name);
    }

    /**
     * Highlights and points to the item at the given index.
     */
    public void selectItem(int selected) {
        this.selected = selected;
    }

    @Override
    /**
     * Manages the rendering of the UI component.
     */
    public void render(SpriteBatch batch, NinePatch patch) {
        float thisX=x;
        float thisY=y;

        patch.draw(batch, x, y, width, height + (paddingY * 2));

        for(int i=0; i<listItems.size();i++){
            if(i==selected) {
                renderText(batch, ">", thisX, thisY, Color.WHITE, Assets.consolas22);
                renderText(batch, listItems.get(i), thisX+width/4, thisY, Color.WHITE, Assets.consolas22);
            }
            else{
                renderText(batch, listItems.get(i), thisX+width/4, thisY, Color.LIGHT_GRAY, Assets.consolas22);
            }
            thisY-=25;
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
