package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * Assets is a static class containing all assets that are used in rendering the game.
 * This class handles loading and disposing of asset resources, done so by calling the
 * respective methods.
 */
public class Assets {
//  BATTLE ASSETS
    public static Texture playerTexture;
    public static Texture deadPlayerTexture;
    public static Texture[][] battleSprites;

    public static Texture battleTurnPointer;
    public static Texture targetingPointer;

//  MAP ASSETS
    public static Texture[] battleBGs = new Texture[4];
    public static Texture mapTexture;

//  UI ASSETS
    public static BitmapFont consolas22;
    public static BitmapFont consolas16;
    public static NinePatch patch;
    public static TextureAtlas atlas;
    public static Texture dialoguePointer;
    public static Texture arrow;

    private static final int PLAYER_WALKSHEET_COLS = 4;
    private static final int PLAYER_WALKSHEET_ROWS = 2;
    private static final int NPC_WALKSHEET_COLS = 4;
    private static final int NPC_WALKSHEET_ROWS = 2;

    //  CHARACTER TEXTURE SHEETS
    public static Animation[] playerWalkAnimation;
    public static Texture playerWalkSheet;

    public static Animation[] SallyNPCWalkAnimation;
    public static Texture SallyNPCWalkSheet;

    public static Animation[] RoboNPCWalkAnimation;
    public static Texture RoboNPCWalkSheet;

    public static Texture shadow;

    public static Texture title;


    //SOUNDS
    public static Sound sfx_menuMove;
    public static Sound sfx_menuSelect;
    public static Sound sfx_menuBack;
    public static Sound sfx_hitNoise;
    public static Sound sfx_healNoise;

    public static Sound sfx_battleStart;
    public static Sound sfx_battleWin;
    public static Sound sfx_battleLose;

    public static Music battleMusic;
    public static Music worldMusic;
    /**
     * Loads the assets from the asset folder and initialises animation frames.
     */
    public static void load() {

        title = new Texture("Start Screen.png");
        //  BATTLE ASSETS

        playerTexture = new Texture("Duck2.png");
        deadPlayerTexture = new Texture("DuckDead.png");
        battleSprites = new Texture[6][2];
        battleSprites[0][0] = new Texture("Duck2.png");
        battleSprites[0][1] = new Texture("DuckDead.png");
        battleSprites[1][0] = new Texture("Roboduck.png");
        battleSprites[1][1] = new Texture("deadroboduck.png");

        battleSprites[2][0] = new Texture("enemy_sprites/Ooze.png");
        battleSprites[2][1] = new Texture("enemy_sprites/OozeDead.png");
        battleSprites[3][0] = new Texture("enemy_sprites/RadiatedDuck.png");
        battleSprites[3][1] = new Texture("enemy_sprites/RadiatedDuckDead.png");
        battleSprites[4][0] = new Texture("enemy_sprites/ScarDuck.png");
        battleSprites[4][1] = new Texture("enemy_sprites/ScarDuckDead.png");
        battleSprites[5][0] = new Texture("enemy_sprites/UndeadDuck.png");
        battleSprites[5][1] = new Texture("enemy_sprites/UndeadDuckDead.png");

        battleTurnPointer = new Texture("turnPointer.png");
        targetingPointer = new Texture("targetingPointer.png");

        //  MAP ASSETS
        battleBGs[0] = new Texture("backgrounds/CS_centrefixed.png");
        battleBGs[1] = new Texture("backgrounds/LM_path.png");
        battleBGs[2] = new Texture("backgrounds/RCH_lake.png");
        battleBGs[3] = new Texture("backgrounds/Background_1.png");

        mapTexture = new Texture("map.jpg");

        //  UI ASSETS
        consolas22 = new BitmapFont(Gdx.files.internal("fonts/consolas22.fnt"));
        consolas16 = new BitmapFont(Gdx.files.internal("fonts/consolas16.fnt"));
        atlas = new TextureAtlas(Gdx.files.internal("packedimages/pack.atlas"));
        patch = atlas.createPatch("knob2");
        dialoguePointer = new Texture("dialoguePointer.png");
        arrow = new Texture("arrow.png");

        sfx_menuMove = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuMove.wav"));
        sfx_menuSelect = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuSelect.wav"));
        sfx_menuBack = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuBack.wav"));
        sfx_hitNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Damage.wav"));
        sfx_healNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Heal.wav"));

        sfx_battleStart  = Gdx.audio.newSound(Gdx.files.internal("sound_effects/EnterBattle.wav"));
        sfx_battleWin  = Gdx.audio.newSound(Gdx.files.internal("sound_effects/WinBattle.wav"));
        sfx_battleLose  = Gdx.audio.newSound(Gdx.files.internal("sound_effects/LoseBattle.wav"));

        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/BattleTheme.ogg"));
        worldMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/WorldTheme.ogg"));

        battleMusic.setLooping(true);
        worldMusic.setLooping(true);



        //  CHARACTER TEXTURE SHEETS
        shadow = new Texture("shadow.png");

        playerWalkSheet = new Texture("DuckAnimationFrames.png");
        TextureRegion[][] tmp = TextureRegion.split(playerWalkSheet, playerWalkSheet.getWidth() / PLAYER_WALKSHEET_COLS, playerWalkSheet.getHeight() / PLAYER_WALKSHEET_ROWS);
        TextureRegion[][] walkFrameDirections = new TextureRegion[PLAYER_WALKSHEET_COLS][PLAYER_WALKSHEET_ROWS];
        int index = 0;
        for (int i = 0; i < PLAYER_WALKSHEET_ROWS; i++) {
            for (int j = 0; j < PLAYER_WALKSHEET_COLS; j++) {
                if (j % 2 == 0) {
                    walkFrameDirections[index][j % 2] = tmp[i][j];
                } else {
                    walkFrameDirections[index++][j % 2] = tmp[i][j];
                }
            }
        }
        playerWalkAnimation = new Animation[PLAYER_WALKSHEET_COLS];
        for (int x = 0; x < walkFrameDirections.length;x++) {
            playerWalkAnimation[x] = new Animation(0.175f, walkFrameDirections[x]);
            playerWalkAnimation[x].setPlayMode(Animation.PlayMode.LOOP);
        }

        SallyNPCWalkSheet = new Texture("EvilDuckAnimationFrames.png");
        tmp = TextureRegion.split(SallyNPCWalkSheet, SallyNPCWalkSheet.getWidth() / NPC_WALKSHEET_COLS, SallyNPCWalkSheet.getHeight() / NPC_WALKSHEET_ROWS);
        walkFrameDirections = new TextureRegion[NPC_WALKSHEET_COLS][NPC_WALKSHEET_ROWS];
        index = 0;
        for (int i = 0; i < NPC_WALKSHEET_ROWS; i++) {
            for (int j = 0; j < NPC_WALKSHEET_COLS; j++) {
                if (j % 2 == 0) {
                    walkFrameDirections[index][j % 2] = tmp[i][j];
                } else {
                    walkFrameDirections[index++][j % 2] = tmp[i][j];
                }
            }
        }
        SallyNPCWalkAnimation = new Animation[NPC_WALKSHEET_COLS];
        for (int x = 0; x < walkFrameDirections.length;x++) {
            SallyNPCWalkAnimation[x] = new Animation(0.175f, walkFrameDirections[x]);
            SallyNPCWalkAnimation[x].setPlayMode(Animation.PlayMode.LOOP);
        }

        RoboNPCWalkSheet = new Texture("RoboDuckAnimationFrames.png");
        tmp = TextureRegion.split(RoboNPCWalkSheet, RoboNPCWalkSheet.getWidth() / NPC_WALKSHEET_COLS, RoboNPCWalkSheet.getHeight() / NPC_WALKSHEET_ROWS);
        walkFrameDirections = new TextureRegion[NPC_WALKSHEET_COLS][NPC_WALKSHEET_ROWS];
        index = 0;
        for (int i = 0; i < NPC_WALKSHEET_ROWS; i++) {
            for (int j = 0; j < NPC_WALKSHEET_COLS; j++) {
                if (j % 2 == 0) {
                    walkFrameDirections[index][j % 2] = tmp[i][j];
                } else {
                    walkFrameDirections[index++][j % 2] = tmp[i][j];
                }
            }
        }
        RoboNPCWalkAnimation = new Animation[NPC_WALKSHEET_COLS];
        for (int x = 0; x < walkFrameDirections.length;x++) {
            RoboNPCWalkAnimation[x] = new Animation(0.175f, walkFrameDirections[x]);
            RoboNPCWalkAnimation[x].setPlayMode(Animation.PlayMode.LOOP);
        }

    }

    /**
     * Disposes of assets when not in use.
     */
    public static void dispose() {
        playerTexture.dispose();
        playerTexture.dispose();
        battleTurnPointer.dispose();
        mapTexture.dispose();
        consolas22.dispose();
        atlas.dispose();

    }
}
