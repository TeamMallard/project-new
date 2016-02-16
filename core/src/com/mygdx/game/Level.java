package com.mygdx.game;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.Character.CharacterState;
import com.mygdx.game.entity.Player;

import java.util.ArrayList;

/**
 * The level class contains the map used by the game and stores all characters.
 */
public class Level {

    /**
     * The size of one tile in the level map.
     */
    public static final int TILE_SIZE = 32;

    /**
     * The world map.
     */
    public TiledMap map;

    /**
     * The collision layer on the world map.
     */
    private TiledMapTileLayer collisionLayer;

    /**
     * The player.
     */
    public Player player;

    /**
     * The list of all world characters (including the player).
     */
    public ArrayList<Character> characters;

    /**
     * Whether input is blocked.
     */
    public boolean stopInput;

    /**
     * The size of the map in pixels.
     */
    public Vector2 mapBounds;

    /**
     * The doors for entering for advancing map segment.
     */
    public Vector2[] doors = {new Vector2(58, 36), new Vector2(20, 35), new Vector2(46, 69), new Vector2(47, 69),
            new Vector2(78, 96), new Vector2(119, 65), new Vector2(147, 89), new Vector2(201, 65)};

    /**
     * Where each of the doors in the doors array teleports the player to.
     */
    public Vector2[] exits = {new Vector2(48, 49), new Vector2(22, 55), new Vector2(59, 76), new Vector2(59, 76),
            new Vector2(89, 106), new Vector2(119, 87), new Vector2(157, 97), new Vector2(211, 73)};


    /**
     * Creates a new Level by loading the map and setting up the player.
     */
    public Level() {
        map = new TmxMapLoader().load("map.tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");

        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);

        int tileWidth = prop.get("tilewidth", Integer.class);
        int tileHeight = prop.get("tileheight", Integer.class);
        mapBounds = new Vector2(mapWidth * tileWidth, mapHeight * tileHeight);

        player = new Player(this, new Vector2(85, 59));
        characters = new ArrayList<Character>();
        characters.add(player);
        stopInput = false;
    }

    /**
     * Updates the state of this Level by updating each of its characters.
     *
     * @param delta the time elapsed since the last update
     */
    public void update(float delta) {
        characters.sort(new Character.CharacterComparator());
        for (Character character : characters) {
            character.update(delta);
            if (character instanceof Player) {
                for (int i = 0; i < doors.length; i++) {
                    if (character.getCurrentTile().equals(doors[i]) && character.getState() != CharacterState.TRANSITIONING) {
                        character.setCurrentTile(exits[i]);
                        Game.segment += 1;
                        Game.setObjective();
                    }
                }
            }
        }

        // Remove closed door.
        if (Game.objective.isComplete()) {
            map.getLayers().remove(map.getLayers().get("door" + (Game.segment + 1)));
        }

    }

    /**
     * Checks whether a tile is water.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return true if the specified tile is water, false if not
     */
    public boolean checkWater(int x, int y) {
        return ((TiledMapTileLayer) map.getLayers().get(0)).getCell(x, y).getTile().getProperties().containsKey("water");
    }

    /**
     * Checks whether the specified tile is impassable due to it being blocked or occupied.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return true if the specified tile is blocked or occupied.
     */
    public boolean checkCollision(int x, int y) {
        return isTileBlocked(x, y) || isTileOccupied(x, y);
    }

    /**
     * Checks whether the specified tile is blocked.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return true if the specified tile is blocked
     */
    private boolean isTileBlocked(int x, int y) {
        // Check each door layer.
        for (int i = 1; i < 8; i++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("door" + i);
            if (layer != null && layer.getCell(x, y) != null) {
                return true;
            }
        }

        return collisionLayer.getCell(x, y) != null;
    }

    /**
     * Checks whether the specified tile is occupied by a character.
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return true if the specified tile is occupied
     */
    private boolean isTileOccupied(int x, int y) {
        for (Character character : characters) {
            if ((int) character.getCurrentTile().x == x && (int) character.getCurrentTile().y == y || (int) character.targetTile.x == x && (int) character.targetTile.y == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the character at the specified tile.
     *
     * @param tileX the x coordinate of the tile
     * @param tileY the y coordinate of the tile
     * @return the character at the specified tile or null if there is no character there
     */
    public Character getCharacterAt(float tileX, float tileY) {
        if (characters != null) {
            for (Character c : characters) {
                if (c.getCurrentTile().equals(new Vector2(tileX, tileY)))
                    return c;
            }
        }
        return null;
    }

}
