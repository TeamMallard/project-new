package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.Assets;

/**
 * A simple screen that is used before the game world is loaded.
 */
public class WinScreen extends ScreenAdapter {

    /**
     * The current alpha of the black texture, used for the fading in effect.
     */
    private float fadeInCounter;

    /**
     * How long the StartScreen has been displayed for.
     */
    private float runningTime;
    /**
     * The message to display to the user on the screen.
     */
    private final String MESSAGE = "YOU WIN!";
    /**
     * The GlyphLayout to use for grey text.
     */
    private GlyphLayout grey = new GlyphLayout(Assets.consolas22, MESSAGE, Color.GRAY, Gdx.graphics.getWidth(), Align.center, false);

    /**
     * The GlyphLayout to use for white text.
     */
    private GlyphLayout white = new GlyphLayout(Assets.consolas22, MESSAGE, Color.WHITE, Gdx.graphics.getWidth(), Align.center, false);

    /**
     * The sprite batch to draw on.
     */
    private SpriteBatch batch = new SpriteBatch();
    /**
     * The pixmap to use for the fade in black texture.
     */
    private Pixmap black;

    /**
     * The black texture.
     */
    private Texture blackTex;

    /**
     * Creates a new WinScreen.
     */
    public WinScreen() {
        black = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        blackTex = new Texture(black);
    }

    /**
     * Called when the WinScreen is displayed.
     */
    public void show() {
        fadeInCounter = 1f;
        runningTime = 0;
    }

    /**
     * Renders this WinScreen.
     *
     * @param delta the time elapsed since the last render
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        runningTime += delta;
        batch.begin();
        batch.draw(Assets.title, 160, 0);
        black.setColor(0, 0, 0, fadeInCounter);
        fadeInCounter -= 0.01f;
        if (fadeInCounter < 0) {
            fadeInCounter = 0;
        }
        black.fill();
        if (runningTime % 1 > 0.5f) {
            Assets.consolas22.draw(batch, grey, 0, 100);
        } else {
            Assets.consolas22.draw(batch, white, 0, 100);
        }
        batch.draw(blackTex, 0, 0);
        batch.end();
    }
}
