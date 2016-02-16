package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.Assets;

/**
 * Represents a message box with simple text.
 */
public class UIMessageBox extends UIComponent {

    /**
     * Text colour.
     */
    private Color color;

    /**
     * The font to use.
     */
    private BitmapFont font;

    /**
     * Message to be displayed.
     */
    private String message;

    /**
     * Text alignment in the message box.
     */
    private int alignment;

    /**
     * Text padding.
     */
    private float paddingX, paddingY;

    /**
     * Creates a new UIMessageBox with the specified parameters.
     *
     * @param message  the message to be displayed
     * @param x        x coordinate
     * @param y        y coordinate
     * @param width    width of message box
     * @param height   height of message box
     * @param paddingX horizontal padding
     * @param paddingY vertical padding
     */
    public UIMessageBox(String message, float x, float y, float width, float height, float paddingX, float paddingY) {
        this(message, Assets.consolas22, Color.WHITE, Align.left, x, y, width, height, paddingX, paddingY);
    }

    /**
     * Creates a new UIMessageBox with the specified parameters and default padding.
     *
     * @param message   the message to be displayed
     * @param font      the font to use
     * @param color     the colour of the message
     * @param alignment the alignment of the message in the box
     * @param x         x coordinate
     * @param y         y coordinate
     * @param width     width of message box
     * @param height    height of message box
     */
    public UIMessageBox(String message, BitmapFont font, Color color, int alignment, float x, float y, float width, float height) {
        this(message, font, color, alignment, x, y, width, height, 40, 20);
    }

    /**
     * Creates a new UIMessageBox with the specified parameters and default padding.
     *
     * @param message   the message to be displayed
     * @param font      the font to use
     * @param color     the colour of the message
     * @param alignment the alignment of the message in the box
     * @param x         x coordinate
     * @param y         y coordinate
     * @param width     width of message box
     * @param height    height of message box
     * @param padding   horizontal and vertical padding
     */
    public UIMessageBox(String message, BitmapFont font, Color color, int alignment, float x, float y, float width, float height, float padding) {
        this(message, font, color, alignment, x, y, width, height, padding, padding);
    }

    /**
     * Creates a new UIMessageBox with the specified parameters and default padding.
     *
     * @param message   the message to be displayed
     * @param font      the font to use
     * @param color     the colour of the message
     * @param alignment the alignment of the message in the box
     * @param x         x coordinate
     * @param y         y coordinate
     * @param width     width of message box
     * @param height    height of message box
     * @param paddingX  horizontal padding
     * @param paddingY  vertical padding
     */
    public UIMessageBox(String message, BitmapFont font, Color color, int alignment, float x, float y, float width, float height, float paddingX, float paddingY) {
        super(x, y, width, height);
        this.message = message;
        this.font = font;
        this.color = color;
        this.alignment = alignment;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
    }

    /**
     * Renders this UIMessageBox onto the specified sprite batch.
     *
     * @param batch the sprite batch to render on
     * @param patch the nine patch for drawing boxes
     */
    @Override
    public void render(SpriteBatch batch, NinePatch patch) {
        GlyphLayout layout = new GlyphLayout(font, message,
                Color.BLACK, width - paddingX * 2, alignment, true);
        if (layout.height > height) {
            height = (int) layout.height;
        }

        patch.draw(batch, x, y, width, height + (paddingY * 2));
        font.draw(batch, layout, x + paddingX, y + height + paddingY - 2);
        layout.setText(font, message,
                color, width - paddingX * 2, alignment, true);
        font.draw(batch, layout, x + paddingX, y + height + paddingY);
    }

    /**
     * Sets the message of this UIMessageBox.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the text colour of this UIMessageBox.
     *
     * @param color the new colour
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
