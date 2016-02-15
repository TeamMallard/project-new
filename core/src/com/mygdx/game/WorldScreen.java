package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.assets.Assets;

/**
 * The screen that contains the gameWorld and worldRenderer.
 * This class is responsible for calling the gameWorld update method
 * and the worldRenderer render method once per frame.
 */
public class WorldScreen extends ScreenAdapter {

    private Game game;
    private GameWorld gameWorld;
    private WorldRenderer worldRenderer;


    public WorldScreen(Game game) {
        this.game = game;
        gameWorld = new GameWorld(game);
        worldRenderer = new WorldRenderer(gameWorld);
    }

    public void show() {
        Assets.worldMusic.setVolume(Game.masterVolume+0.3f);//Start the worldMusic
        Assets.worldMusic.play();
    }

    @Override
    public void hide() {
        Assets.worldMusic.stop();
        Assets.worldMusic.dispose();
        super.hide();
    }

    public void update(float delta) {
        gameWorld.update(delta);
    }


    @Override
    public void render(float delta) {
        update(delta);
        worldRenderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
    }
    
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}

