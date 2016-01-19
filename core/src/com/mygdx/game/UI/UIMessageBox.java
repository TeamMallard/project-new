package com.mygdx.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;

/**
 * A message box with simple text.
 */
public class UIMessageBox extends UIComponent {
    public Color color;
    public BitmapFont font;
    public String message;
    public int alignment;
    float paddingX;
    float paddingY;

    /**
     * Constructor for message box
     * @param message The message to be displayed
     * @param x X position of the bottom left corner of the message box.
     * @param y Y position of the bottom left corner of the message box.
     * @param width Width of the message box.
     * @param height Height of the message box.
     * @param paddingX X padding of the text in the message box.
     * @param paddingY Y padding of the text in the message box.
     */
    public UIMessageBox(String message, float x, float y, float width, float height, float paddingX, float paddingY) {
        super(x, y, width, height);
        this.message = message;
        this.font = Assets.consolas22;
        this.color = Color.WHITE;
        this.alignment = Align.left;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
    }

    public UIMessageBox(String message, BitmapFont font, Color color, int alignment, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.message = message;
        this.font = font;
        this.color = color;
        this.alignment = alignment;
        paddingX = 40;
        paddingY = 20;
    }

    public UIMessageBox(String message, BitmapFont font, Color color, int alignment, float x, float y, float width, float height, float padding) {
        super(x, y, width, height);
        this.message = message;
        this.font = font;
        this.color = color;
        this.alignment = alignment;
        this.paddingX = padding;
        this.paddingY = padding;
    }

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
     * Renders the UIComponent and should be rendered once per frame.
     * @param batch The batch used to render the UI.
     * @param patch The patch used from Assets.
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

    public void setMessage(String message) {
        this.message = message;
    }
}
