package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Assets is a static class containing all assets that are used in rendering the game.
 * This class handles loading and disposing of asset resources, done so by calling the
 * respective methods.
 */
public class Assets {

    public static Texture selectArrow, turnArrow;

    //  MAP ASSETS
    public static Texture[] battleBGs = new Texture[4];

    //  UI ASSETS
    public static BitmapFont consolas22;
    public static BitmapFont consolas16;
    public static NinePatch patch;
    public static TextureAtlas atlas;
    public static Texture dialoguePointer;
    public static Texture shield;

    public static Texture shadow;

    public static Texture title;


    public static WalkingTextures playerWalkingTextures, playerSwimmingTextures, roboWalkingTextures, sallyWalkingTextures;
    public static BattleTextures[] battleTextures = new BattleTextures[6];

    public static TextureRegion[] equipment = new TextureRegion[5];

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
        battleTextures = new BattleTextures[6];

        loadPlayerTextures();
        loadRoboTextures();
        loadSallyTextures();

        loadEquipmentTextures();

        battleTextures[2] = loadBattleTextures("ooze", 32, 32);
        battleTextures[3] = loadBattleTextures("radiated_duck", 32, 32);
        battleTextures[4] = loadBattleTextures("scar_duck", 32, 32);
        battleTextures[5] = loadBattleTextures("undead_duck", 32, 32);

        turnArrow = new Texture("turnPointer.png");
        selectArrow = new Texture("arrow.png");

        //  MAP ASSETS
        battleBGs[0] = new Texture("backgrounds/CS_centrefixed.png");
        battleBGs[1] = new Texture("backgrounds/LM_path.png");
        battleBGs[2] = new Texture("backgrounds/RCH_lake.png");
        battleBGs[3] = new Texture("backgrounds/Background_1.png");

        //  UI ASSETS
        consolas22 = new BitmapFont(Gdx.files.internal("fonts/consolas22.fnt"));
        consolas16 = new BitmapFont(Gdx.files.internal("fonts/consolas16.fnt"));
        atlas = new TextureAtlas(Gdx.files.internal("packedimages/pack.atlas"));
        patch = atlas.createPatch("knob2");
        dialoguePointer = new Texture("dialoguePointer.png");
        selectArrow = new Texture("arrow.png");
        shield = new Texture("shield.png");

        sfx_menuMove = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuMove.wav"));
        sfx_menuSelect = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuSelect.wav"));
        sfx_menuBack = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuBack.wav"));
        sfx_hitNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Damage.wav"));
        sfx_healNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Heal.wav"));

        sfx_battleStart = Gdx.audio.newSound(Gdx.files.internal("sound_effects/EnterBattle.wav"));
        sfx_battleWin = Gdx.audio.newSound(Gdx.files.internal("sound_effects/WinBattle.wav"));
        sfx_battleLose = Gdx.audio.newSound(Gdx.files.internal("sound_effects/LoseBattle.wav"));

        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/BattleTheme.ogg"));
        worldMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/WorldTheme.ogg"));

        battleMusic.setLooping(true);
        worldMusic.setLooping(true);


        //  CHARACTER TEXTURE SHEETS
        shadow = new Texture("shadow.png");
    }

    private static void loadEquipmentTextures() {
        Texture texture = loadTexture("equipment.png");

        for (int i = 0; i < equipment.length; i++) {
            equipment[i] = new TextureRegion(texture, i * 15, 0, 15, 15);
        }
    }

    private static void loadPlayerTextures() {
        playerWalkingTextures = loadWalkingTextures("player", 32, 32, 2, 0.175f);

        // Load swimming textures for world.
        Texture swimming = loadTexture("world/player_swimming.png");
        TextureRegion swimmingDown = new TextureRegion(swimming, 0, 0, 32, 32);
        TextureRegion swimmingUp = new TextureRegion(swimming, 32, 0, 32, 32);
        TextureRegion swimmingLeft = new TextureRegion(swimming, 64, 0, 32, 32);
        TextureRegion swimmingRight = new TextureRegion(swimming, 96, 0, 32, 32);
        playerSwimmingTextures = new WalkingTextures(swimmingDown, swimmingUp, swimmingLeft, swimmingRight);

        battleTextures[0] = loadBattleTextures("player", 32, 32);
    }

    private static void loadRoboTextures() {
        roboWalkingTextures = loadWalkingTextures("robo", 32, 32, 2, 0.175f);
        battleTextures[1] = loadBattleTextures("robo", 32, 32);
    }

    private static void loadSallyTextures() {
        sallyWalkingTextures = loadWalkingTextures("sally", 32, 32, 2, 0.175f);
    }

    private static WalkingTextures loadWalkingTextures(String prefix, int width, int height, int frameCount, float frameDuration) {
        Texture idle = loadTexture("world/" + prefix + "_idle.png");

        // Cut idle textures from texture map.
        TextureRegion down = new TextureRegion(idle, 0, 0, width, height);
        TextureRegion up = new TextureRegion(idle, width, 0, width, height);
        TextureRegion left = new TextureRegion(idle, width * 2, 0, width, height);
        TextureRegion right = new TextureRegion(idle, width * 3, 0, width, height);

        // Load walking animations.
        Animation walkingDown = loadAnimation("world/" + prefix + "_walking_down.png", width, height, frameCount, frameDuration);
        Animation walkingUp = loadAnimation("world/" + prefix + "_walking_up.png", width, height, frameCount, frameDuration);
        Animation walkingLeft = loadAnimation("world/" + prefix + "_walking_left.png", width, height, frameCount, frameDuration);
        Animation walkingRight = loadAnimation("world/" + prefix + "_walking_right.png", width, height, frameCount, frameDuration);

        return new WalkingTextures(down, up, left, right, walkingDown, walkingUp, walkingLeft, walkingRight);
    }

    private static BattleTextures loadBattleTextures(String prefix, int width, int height) {
        Texture battle = loadTexture("battle/" + prefix + "_battle.png");

        TextureRegion alive = new TextureRegion(battle, 0, 0, width, height);
        TextureRegion dead = new TextureRegion(battle, width, 0, width, height);

        // Load the battle animation if it exists.
        if (Gdx.files.internal("battle/" + prefix + "_anim.png").exists()) {
            Texture texture = loadTexture("battle/" + prefix + "_anim.png");

            return new BattleTextures(alive, dead, loadAnimation(texture, width, height, texture.getWidth() / width, 0.05f));
        } else {
            return new BattleTextures(alive, dead);
        }
    }

    /**
     * Loads the texture from the specified file.
     *
     * @param file the file to load from
     * @return the texture
     */
    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * Loads the animation from the specified file.
     *
     * @param file          the file to load from
     * @param frameCount    how many frames are in the file
     * @param frameWidth    how wide each frame is in the file
     * @param frameHeight   how tall each frame is in the file
     * @param frameDuration how long each frame should be shown for in seconds
     * @return the animation
     */
    public static Animation loadAnimation(String file, int frameWidth, int frameHeight, int frameCount, float frameDuration) {
        Texture texture = loadTexture(file);
        return loadAnimation(texture, frameWidth, frameHeight, frameCount, frameDuration);
    }

    /**
     * Loads an animation from the specified texture.Ò
     *
     * @param texture       the texture to create from
     * @param frameCount    how many frames are in the file
     * @param frameWidth    how wide each frame is in the file
     * @param frameHeight   how tall each frame is in the file
     * @param frameDuration how long each frame should be shown for in seconds
     * @return the animation
     */
    public static Animation loadAnimation(Texture texture, int frameWidth, int frameHeight, int frameCount, float frameDuration) {
        Array<TextureRegion> keyFrames = new Array<TextureRegion>();

        for (int i = 0; i < frameCount; i++) {
            keyFrames.add(new TextureRegion(texture, i * frameWidth, 0, frameWidth, frameHeight));
        }

        return new Animation(frameDuration, keyFrames);
    }
}
