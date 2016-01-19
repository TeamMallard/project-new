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
 * Creates and manages rendering of the item and skill menus on the battlescreen.
 * The selected skill/item is highlighted and indicated with a '{@literal >}'
 * The UI is rendered as a grid of 2 columns and rows of (number of elements/2).
 * Elements are rendered left to right in the order they are added to the class.
 *
 * The UIBattleSkillItemMenu class is a subclass of UIComponent.
 */
public class UIBattleSkillItemMenu extends UIComponent {
    public List<String> listItems;

    private int selected = 0;
    float paddingX,paddingY;

    public UIBattleSkillItemMenu(float x, float y, float width, float height, float paddingX, float paddingY) {
        super(x, y, width, height);

        this.paddingX=paddingX;
        this.paddingY=paddingY;

        listItems = new ArrayList<String>();
    }

    public int getSelected() {
        return selected;
    }

    /**
     * Add a string to the UI.
     * @param name the string to add.
     */
    public void addListItem(String name) {
        listItems.add(name);
    }

    public void selectItem(int selected){
        this.selected=selected;
    }

    /**
     * Manages rendering the menu.
     * @param batch the spritebatch to use.
     * @param patch the ninepatch to use.
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        float leftX = x+20;
        float rightX = x+(width/2)+20;
        float thisY = y;

        patch.draw(batch, x, y, width, height + (paddingY * 2));

        for(int i=0; i<listItems.size();i++) {
            if(i%2==0){
                if(i==selected) {
                    renderText(batch, ">", leftX - 20, thisY, Color.WHITE, Assets.consolas22);
                    renderText(batch, listItems.get(i), leftX, thisY, Color.WHITE, Assets.consolas22);
                }
                else
                    renderText(batch, listItems.get(i), leftX, thisY, Color.LIGHT_GRAY, Assets.consolas22);
            }
            else{
                if(i==selected){
                    renderText(batch, ">", rightX - 20, thisY, Color.WHITE, Assets.consolas22);
                    renderText(batch, listItems.get(i), rightX, thisY, Color.WHITE, Assets.consolas22);
                }
                else
                    renderText(batch, listItems.get(i), rightX, thisY, Color.LIGHT_GRAY, Assets.consolas22);

                thisY-=25;
            }

        }

    }

    /**
     * Helper function to actually render the text.
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
