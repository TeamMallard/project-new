package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/**
 * Base class for all UI components.
 */
public abstract class UIComponent {

    /**
     * Position of this UIComponent.
     */
    protected float x, y;

    /**
     * Size of this UIComponent.
     */
    protected float width, height;

    /**
     * Creates a new UIComponent with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width
     * @param height the height
     */
    public UIComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Renders this UIComponent onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    public abstract void render(SpriteBatch batch, NinePatch patch);

    /**
     * Helper method for rendering text.
     *
     * @param batch    the sprite batch to render on
     * @param message  the text to render
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param paddingX the horizontal padding
     * @param paddingY the vertical padding
     * @param color    the colour of the text
     * @param font     the font to use
     */
    protected final void renderText(SpriteBatch batch, String message, float x, float y, float paddingX, float paddingY, Color color, BitmapFont font) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, Align.left, false);

        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, Align.left, false);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }
}
