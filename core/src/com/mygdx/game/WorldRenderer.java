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
 * This class renders a GameWorld that has been passed in it's constructor.
 * This also contains a mapRenderer that renders the map,
 * and the uiRenderer that renders UI.
 */
public class WorldRenderer {

    public static final float PLAYER_CAMERA_BOUND = 8f;

    public final float SCALE = 3f;

    private GameWorld world;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private OrthogonalTiledMapRenderer mapRenderer;
    private UIRenderer uiRenderer;

    private Vector2 textureOffset = new Vector2(-2, 0);

    /**
     * @param world required to access state of the game e.g.
     *              character positions and map.
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
     * Renders the game world and should be called once per frame.
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        mapRenderer.setMap(world.level.map);
        mapRenderer.setView(camera);

        // Render all layers but the trees layer.
        for (MapLayer layer : world.level.map.getLayers()) {
            if (layer.isVisible() && !layer.getName().equals("trees")) {
                mapRenderer.renderTileLayer((TiledMapTileLayer)layer);
            }
        }

        renderPlayers(delta);

        // Render treetops layer over player.
        mapRenderer.renderTileLayer((TiledMapTileLayer) world.level.map.getLayers().get("trees"));

        batch.end();

        uiRenderer.renderWorld();
    }

    /**
     * Iterates through each player in the level and renders the correct sprite based
     * on position, orientation ect.
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
     * Updates the position of the camera to be constrained to the player and stay within the
     * bounds of the map.
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

    public void resize(int width, int height) {
        camera.viewportWidth = width / SCALE;
        camera.viewportHeight = height / SCALE;
        camera.update();
        uiRenderer.resize();
    }

    public void dispose() {
        batch.dispose();
        uiRenderer.dispose();
        mapRenderer.dispose();
    }
}
