package com.mygdx.game;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.Player;
import com.mygdx.game.entity.Character.CharacterState;

import static com.mygdx.game.Level.TILE_SIZE;

import java.util.ArrayList;

/**
 * The level class contains the map used by the game and stores all characters.
 */
public class Level {

    public static final int TILE_SIZE = 16;

    private GameWorld gameWorld;
    public TiledMap map;
    public boolean[][] collisionMap;
    public Player player;
    public ArrayList<Character> characters;
    public boolean stopInput;

    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;
    public Vector2 mapBounds;

    /**
     * The constructor loads the map and creates a new player in the appropriate position.
     */
    public Level(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        map = new TmxMapLoader().load("newMap.tmx");

        MapProperties prop = map.getProperties();
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        tileWidth = prop.get("tilewidth", Integer.class);
        tileHeight = prop.get("tileheight", Integer.class);
        mapBounds = new Vector2(mapWidth * tileWidth, mapHeight * tileHeight);

        collisionMap = new boolean[mapWidth][mapHeight];
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        for (int x = 0; x < mapWidth; x++) {
            for (int y = mapHeight - 1; y >= 0; y--) {
                collisionMap[x][y] = layer.getCell(x, y).getTile().getProperties().containsKey("blocked");
            }
        }

        player = new Player(this, new Vector2(115, 94));
        characters = new ArrayList<Character>();
        characters.add(player);
        stopInput = false;
    }

    /**
     * This method is called once per frame and updates each character in the level.
     */
    public void update(float delta) {
        characters.sort(new Character.CharacterComparator());
        updateCollisionMap();
        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).update(delta);
            if(characters.get(i) instanceof Player) {
	            if(characters.get(i).getCurrentTile().equals(new Vector2(115, 95)) && characters.get(i).getState() != CharacterState.TRANSITIONING) {
	            	characters.get(i).setCurrentTile(new Vector2(115, 94));
	            }
            }
        }
        
    }

    /**
     * The CollisionMap allows characters to know if their path is blocked by a player or a blocked tile.
     */
    private void updateCollisionMap() {
        for (int i = 0; i < collisionMap.length; i++) {
            for (int j = 0; j < collisionMap[i].length; j++) {
                collisionMap[i][j] = false;
            }
        }
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        for (int x = 0; x < mapWidth; x++) {
            for (int y = mapHeight - 1; y >= 0; y--) {
                collisionMap[x][y] = layer.getCell(x, y).getTile().getProperties().containsKey("blocked");
            }
        }
        for (int i = 0; i < characters.size(); i++) {
            collisionMap[(int) characters.get(i).getCurrentTile().x][(int) characters.get(i).getCurrentTile().y] = true;
            collisionMap[(int) characters.get(i).targetTile.x][(int) characters.get(i).targetTile.y] = true;
        }
        collisionMap[(int) player.targetTile.x][(int) player.targetTile.y] = true;
        collisionMap[(int) player.getCurrentTile().x][(int) player.getCurrentTile().y] = true;
    }

    /**
     * @return Returns null if no character exists at x, y.
     */
    public Character getCharacterAt(float tileX, float tileY){
        if (characters != null) {
            for (Character c : characters) {
                if (c.getCurrentTile().equals(new Vector2(tileX, tileY)))
                    return c;
            }
        }
        return null;
    }


}
