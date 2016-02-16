package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.Character;
import com.mygdx.game.ui.UIRenderer;

/**
 * Responsible for rendering the game world.
 */
public class WorldRenderer {

    /**
     * The distance the player must move before the camera moves.
     */
    public static final float PLAYER_CAMERA_BOUND = 8f;

    /**
     * Camera scale.
     */
    public final float SCALE = 3f;

    /**
     * The game world to render.
     */
    private GameWorld world;

    /**
     * The camera looking at the game world.
     */
    private OrthographicCamera camera;
    /**
     * The sprite batch to draw on.
     */
    private SpriteBatch batch;

    /**
     * The map renderer.
     */
    private OrthogonalTiledMapRenderer mapRenderer;

    /**
     * The UI renderer.
     */
    private UIRenderer uiRenderer;

    /**
     * The offset to render characters relative to their coordinates.
     */
    private Vector2 textureOffset = new Vector2(-2, 0);

    /**
     * Creates a new WorldRenderer, rendering the specified game world.
     *
     * @param world the game world to render
     */
    public WorldRenderer(GameWorld world) {
        this.world = world;

        Assets.load();

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
        camera.zoom = 2f;
        batch.setProjectionMatrix(camera.combined);

        mapRenderer = new OrthogonalTiledMapRenderer(world.level.map, batch);
        mapRenderer.setView(camera);

        uiRenderer = new UIRenderer(world.uiManager);
    }

    /**
     * Renders the game world.
     *
     * @param delta the time elapsed since the last render
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        /**
         * CHANGE M4: Modified the rendering system.
         */
        mapRenderer.setMap(world.level.map);
        mapRenderer.setView(camera);

        // Render all layers but the trees layer.
        for (MapLayer layer : world.level.map.getLayers()) {
            if (layer.isVisible() && !layer.getName().equals("trees")) {
                mapRenderer.renderTileLayer((TiledMapTileLayer) layer);
            }
        }

        renderPlayers(delta);

        // Render treetops layer over player.
        mapRenderer.renderTileLayer((TiledMapTileLayer) world.level.map.getLayers().get("trees"));

        batch.end();

        uiRenderer.renderWorld();
    }

    /**
     * Renders each character in the game world.
     *
     * @param delta the time elapsed since the last render
     */
    private void renderPlayers(float delta) {
        for (int i = 0; i < world.level.characters.size(); i++) {
            Character c = world.level.characters.get(i);

            if (c.getState() != Character.CharacterState.TRANSITIONING) {
                c.setStateTime(0);
            } else {
                c.setStateTime(c.getStateTime() + delta);
            }

            TextureRegion texture = c.getCurrentTexture();

            batch.draw(texture, c.getAbsPos().x - textureOffset.x, c.getAbsPos().y - textureOffset.y, 64, 64);
        }
    }

    /**
     * Updates the camera bounds based on the player's position.
     */
    private void updateCamera() {
        // Constrain camera to area around player.
        float boundWidth = camera.viewportWidth / PLAYER_CAMERA_BOUND;
        float boundHeight = camera.viewportHeight / PLAYER_CAMERA_BOUND;

        if (world.level.player.getAbsPos().x + Character.CHARACTER_SIZE.x > camera.position.x + boundWidth) {
            camera.position.x = world.level.player.getAbsPos().x + Character.CHARACTER_SIZE.x - boundWidth;
        } else if (world.level.player.getAbsPos().x < camera.position.x - boundWidth) {
            camera.position.x = world.level.player.getAbsPos().x + boundWidth;
        }

        if (world.level.player.getAbsPos().y + Character.CHARACTER_SIZE.y > camera.position.y + boundHeight) {
            camera.position.y = world.level.player.getAbsPos().y + Character.CHARACTER_SIZE.y - boundHeight;
        } else if (world.level.player.getAbsPos().y < camera.position.y - boundHeight) {
            camera.position.y = world.level.player.getAbsPos().y + boundHeight;
        }

        // Constrain camera to map
        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth / 2f, world.level.mapBounds.x - camera.viewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2f, world.level.mapBounds.y - camera.viewportHeight / 2f);

        camera.update();
    }

    /**
     * Re-sizes the game world camera.
     *
     * @param width  the new width
     * @param height the new height
     */
    public void resize(int width, int height) {
        camera.viewportWidth = width / SCALE;
        camera.viewportHeight = height / SCALE;
        camera.update();
        uiRenderer.resize();
    }

    /**
     * Cleans up resources used by this WorldRenderer.
     */
    public void dispose() {
        batch.dispose();
        uiRenderer.dispose();
        mapRenderer.dispose();
    }
}
