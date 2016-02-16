package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.assets.Assets;

/**
 * A screen which displays the game world.
 */
public class WorldScreen extends ScreenAdapter {

    /**
     * The game world being shown on this WorldScreen.
     */
    private GameWorld gameWorld;

    /**
     * The world renderer responsible for rendering the game world.
     */
    private WorldRenderer worldRenderer;

    /**
     * Creates a new GameWorld.
     *
     * @param game the parent game
     */
    public WorldScreen(Game game) {
        gameWorld = new GameWorld(game);
        worldRenderer = new WorldRenderer(gameWorld);
    }

    /**
     * Called when the WorldScreen is displayed.
     */
    public void show() {
        Assets.worldMusic.setVolume(Game.masterVolume + 0.3f);
        Assets.worldMusic.play();
    }

    /**
     * Called when the WorldScreen is hidden.
     */
    @Override
    public void hide() {
        Assets.worldMusic.stop();
        Assets.worldMusic.dispose();
        super.hide();
    }

    /**
     * Updates the game world that this world screen is responsible for.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        gameWorld.update(delta);
    }

    /**
     * Updates and renders the game world.
     *
     * @param delta the time elapsed since the last render
     */
    @Override
    public void render(float delta) {
        update(delta);
        worldRenderer.render(delta);
    }

    /**
     * Re-sizes the game world camera.
     *
     * @param width  the new width
     * @param height the new height
     */
    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    /**
     * Cleans up resources used by this WorldScreen.
     */
    @Override
    public void dispose() {
        worldRenderer.dispose();
    }

    /**
     * @return the game world this WorldScreen is responsible for
     */
    public GameWorld getGameWorld() {
        return gameWorld;
    }
}

