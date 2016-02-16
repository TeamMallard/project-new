package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Contains all assets used in the game.
 */
public final class Assets {

    /**
     * List of battle textures to load. The index in the battleTextures array will be the same as the index in this array for each enemy.
     */
    public static final String[] BATTLE_TEXTURES = new String[]
            {"player", "robo", "blue_ooze", "green_ooze", "red_ooze", "yellow_ooze", "black_ooze", "poisonous_duck", "radiated_duck", "radiated_scar", "radiated_undead", "scar_duck", "super_radiated", "undead_duck"};

    /**
     * Arrows for selection, turn pointing and dialogue pointing.
     */
    public static Texture selectArrow, turnArrow, dialoguePointer;

    /**
     * Backgrounds for battles, indexed by segment number.
     */
    public static Texture[] battleBackgrounds = new Texture[8];

    /**
     * UI fonts.
     */
    public static BitmapFont consolas22, consolas16;

    /**
     * UI nine patch for drawing boxes.
     */
    public static NinePatch patch;
    /**
     * Texture atlas for loading the nine patch.
     */
    public static TextureAtlas atlas;

    /**
     * Shadow texture for drawing below world characters.
     */
    public static Texture shadow;

    /**
     * Title screen.
     */
    public static Texture title;

    /**
     * Player's walking and swimming textures.
     */
    public static WalkingTextures playerWalkingTextures, playerSwimmingTextures;

    /**
     * Walking textures for world NPCs.
     */
    public static WalkingTextures roboWalkingTextures, sallyWalkingTextures;

    /**
     * Battle textures for enemies.
     */
    public static BattleTextures[] battleTextures = new BattleTextures[14];

    /**
     * The shield texture to show that a piece of equipment is currently equipped.
     */
    public static Texture shield;

    /**
     * Textures representing each type of equipment (head, body, feet, accessory, weapon).
     */
    public static TextureRegion[] equipment = new TextureRegion[5];

    /**
     * Sound effects for moving in a menu, selecting, going back and the hit/heal noises.
     */
    public static Sound sfxMenuMove, sfxMenuSelect, sfxMenuBack, sfxHitNoise, sfxHealNoise;

    /**
     * Sound effects for the start of a battle, winning and losing.
     */
    public static Sound sfxBattleStart, sfxBattleWin, sfxBattleLose;

    /**
     * Music for battle and the overworld.
     */
    public static Music battleMusic, worldMusic;

    /**
     * Loads all assets from the asset folder.
     */
    public static void load() {
        title = new Texture("Start Screen.png");

        battleTextures = new BattleTextures[BATTLE_TEXTURES.length];
        loadAllBattleTextures();

        roboWalkingTextures = loadWalkingTextures("robo", 32, 32, 2, 0.175f);
        sallyWalkingTextures = loadWalkingTextures("sally", 32, 32, 2, 0.175f);

        loadPlayerTextures();
        loadEquipmentTextures();
        loadBattleBackgrounds();
        loadUITextures();
        loadSounds();

        shadow = new Texture("shadow.png");
    }

    /**
     * Loads all UI-related textures.
     */
    private static void loadUITextures() {
        turnArrow = new Texture("turnPointer.png");
        selectArrow = new Texture("arrow.png");
        consolas22 = new BitmapFont(Gdx.files.internal("fonts/consolas22.fnt"));
        consolas16 = new BitmapFont(Gdx.files.internal("fonts/consolas16.fnt"));
        atlas = new TextureAtlas(Gdx.files.internal("packedimages/pack.atlas"));
        patch = atlas.createPatch("knob2");
        dialoguePointer = new Texture("dialoguePointer.png");
        selectArrow = new Texture("arrow.png");
        shield = new Texture("shield.png");
    }

    /**
     * Loads all sound effects and music.
     */
    private static void loadSounds() {
        sfxMenuMove = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuMove.wav"));
        sfxMenuSelect = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuSelect.wav"));
        sfxMenuBack = Gdx.audio.newSound(Gdx.files.internal("sound_effects/MenuBack.wav"));
        sfxHitNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Damage.wav"));
        sfxHealNoise = Gdx.audio.newSound(Gdx.files.internal("sound_effects/Heal.wav"));

        sfxBattleStart = Gdx.audio.newSound(Gdx.files.internal("sound_effects/EnterBattle.wav"));
        sfxBattleWin = Gdx.audio.newSound(Gdx.files.internal("sound_effects/WinBattle.wav"));
        sfxBattleLose = Gdx.audio.newSound(Gdx.files.internal("sound_effects/LoseBattle.wav"));

        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/BattleTheme.ogg"));
        worldMusic = Gdx.audio.newMusic(Gdx.files.internal("sound_effects/WorldTheme.ogg"));

        battleMusic.setLooping(true);
        worldMusic.setLooping(true);
    }

    /**
     * Loads all battle backgrounds.
     */
    private static void loadBattleBackgrounds() {
        for (int i = 0; i < battleBackgrounds.length; i++) {
            battleBackgrounds[i] = loadTexture("backgrounds/segment_" + i + ".png");
        }
    }

    /**
     * Loads all battle textures.
     */
    private static void loadAllBattleTextures() {
        for (int i = 0; i < BATTLE_TEXTURES.length; i++) {
            battleTextures[i] = loadBattleTextures(BATTLE_TEXTURES[i], 32, 32);
        }
    }

    private static void loadEquipmentTextures() {
        Texture texture = loadTexture("equipment.png");

        for (int i = 0; i < equipment.length; i++) {
            equipment[i] = new TextureRegion(texture, i * 15, 0, 15, 15);
        }
    }

    /**
     * Loads all player textures.
     */
    private static void loadPlayerTextures() {
        playerWalkingTextures = loadWalkingTextures("player", 32, 32, 2, 0.175f);

        // Load swimming textures for world.
        Texture swimming = loadTexture("world/player_swimming.png");
        TextureRegion swimmingDown = new TextureRegion(swimming, 0, 0, 32, 32);
        TextureRegion swimmingUp = new TextureRegion(swimming, 32, 0, 32, 32);
        TextureRegion swimmingLeft = new TextureRegion(swimming, 64, 0, 32, 32);
        TextureRegion swimmingRight = new TextureRegion(swimming, 96, 0, 32, 32);
        playerSwimmingTextures = new WalkingTextures(swimmingDown, swimmingUp, swimmingLeft, swimmingRight);
    }

    /**
     * Loads a standard set of walking textures from the world folder.
     *
     * @param prefix        the file prefix of the textures to load
     * @param width         the width of each texture
     * @param height        the height of each texture
     * @param frameCount    the number of frames in the walking animations
     * @param frameDuration how long each frame in the walking animation should be displayed for
     * @return the set of walking textures
     */
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

    /**
     * Loads a standard set of battle textures from the battle folder.
     *
     * @param prefix the file prefix of the textures to load
     * @param width  the width of each texture
     * @param height the height of each texture
     * @return the set of battle textures
     */
    private static BattleTextures loadBattleTextures(String prefix, int width, int height) {
        Texture battle = loadTexture("battle/" + prefix + "_battle.png");

        TextureRegion alive = new TextureRegion(battle, 0, 0, width, height);
        TextureRegion dead = new TextureRegion(battle, width, 0, width, height);

        // Load the battle animation if it exists.
        if (Gdx.files.internal("battle/" + prefix + "_anim.png").exists()) {
            Texture texture = loadTexture("battle/" + prefix + "_anim.png");

            return new BattleTextures(alive, dead, loadAnimation(texture, width, height, texture.getWidth() / width, 0.025f));
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
     * Loads an animation from the specified texture.
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
