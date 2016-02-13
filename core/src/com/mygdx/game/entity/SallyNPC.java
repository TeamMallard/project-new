package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Game;
import com.mygdx.game.GameWorld;
import com.mygdx.game.Level;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.UIManager;
import com.mygdx.game.ui.UIShop;

/**
 * This class represents the first npc of the game.
 */
public class SallyNPC extends NPC {

    private boolean doneInteraction;
    private String[] messages;

    public SallyNPC(Level level, Vector2 currentTile) {
        super(level, currentTile, Assets.sallyWalkingTextures);
        messages = new String[3];
        messages[0] = "Help! There is a robo duck on the loose!";
        messages[1] = "Please help us by finding him and defeating him!";
        messages[2] = "The last time I saw him was by the Catalyst building.";
        doneInteraction = false;
    }

    @Override
    public void initializeInteraction(float delta, UIManager uiManager) {
        if (!doneInteraction) {
            uiManager.createDialogue(messages);
            this.uiManager = uiManager;
        }
    }

    @Override
    public boolean updateInteracting(float delta) {
        if (doneInteraction) {
            return false;
        }
        return uiManager.updateDialogue(delta);
    }

    @Override
    public void action(GameWorld gameWorld) {
        if (!doneInteraction) {
        	Game.worldScreen.getGameWorld().setShop(new UIShop(100, 500, 910, 440, Game.party, Game.shops.getShop(0)));
            uiManager.addNotification("You gained 40 points.");
            Game.pointsScore += 40;
            doneInteraction = true;
        }
    }
}
