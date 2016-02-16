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
import com.mygdx.game.Game;
import com.mygdx.game.InputHandler;
import com.mygdx.game.assets.Assets;

/**
 * A simple screen that is used before the game world is loaded.
 */
public class WinScreen extends ScreenAdapter {

    private final Game game;
    private float fadeInCounter;
    private float runningTime;
    private final String MESSAGE = "YOU WIN!";
    private SpriteBatch batch = new SpriteBatch();
    private Pixmap black;

    public WinScreen (Game game){
        this.game = game;
        black = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
    }

    public void show() {
        fadeInCounter = 1f;
        runningTime = 0;
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        runningTime += delta;
        update();
        batch.begin();
        batch.draw(Assets.title, 160, 0);
        black.setColor(0,0,0,fadeInCounter);
        fadeInCounter -= 0.01f;
        if (fadeInCounter < 0) {
            fadeInCounter = 0;
        }
        black.fill();
        if (runningTime %1 > 0.5f) {
            Assets.consolas22.draw(batch, new GlyphLayout(Assets.consolas22, MESSAGE, Color.GRAY, Gdx.graphics.getWidth(), Align.center, false), 0, 100);
        } else {
            Assets.consolas22.draw(batch, new GlyphLayout(Assets.consolas22, MESSAGE, Color.WHITE, Gdx.graphics.getWidth(), Align.center, false), 0, 100);
        }
        batch.draw(new Texture(black),0,0);
        batch.end();
    }

    private void update() {
    }
}
