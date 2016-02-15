package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a selectable menu in battle.
 */
public class UIBattleBaseMenu extends UIComponent {

    /**
     * Menu items.
     */
    private List<String> listItems;
    /**
     * Which menu item is selected.
     */
    private int selected = 0;

    /**
     * Text padding.
     */
    float paddingX, paddingY;

    /**
     * Creates a new UIBattleBaseMenu with the specified parameters.
     *
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param width    the width of the menu
     * @param height   the height of the menu
     * @param paddingX horizontal text padding
     * @param paddingY vertical text padding
     */
    public UIBattleBaseMenu(float x, float y, float width, float height, float paddingX, float paddingY) {
        super(x, y, width, height);
        this.paddingX = paddingX;
        this.paddingY = paddingY;

        listItems = new ArrayList<String>();
    }

    /**
     * Add an item to the list of items.
     *
     * @param name the item to add
     */
    public void addListItem(String name) {
        listItems.add(name);
    }

    /**
     * Sets the selected item to the specified index.
     *
     * @param selected the index of the item to select
     */
    public void selectItem(int selected) {
        this.selected = selected;
    }

    /**
     * Renders this UIBattleBaseMenu onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        float thisX = x;
        float thisY = y;

        patch.draw(batch, x, y, width, height + (paddingY * 2));

        for (int i = 0; i < listItems.size(); i++) {
            if (i == selected) {
                renderText(batch, ">", thisX, thisY, paddingX, paddingY, Color.WHITE, Assets.consolas22);
                renderText(batch, listItems.get(i), thisX + width / 4, thisY, paddingX, paddingY, Color.WHITE, Assets.consolas22);
            } else {
                renderText(batch, listItems.get(i), thisX + width / 4, thisY, paddingX, paddingY, Color.LIGHT_GRAY, Assets.consolas22);
            }
            thisY -= 25;
        }
    }
}
