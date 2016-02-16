package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Game;
import com.mygdx.game.GameWorld;
import com.mygdx.game.Level;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.UIManager;
import com.mygdx.game.ui.UIShop;

/**
 * Represents the shop keeper NPC.
 */
public class SallyNPC extends NPC {

    /**
     * Whether this SallyNPC has finished interacting with the player.
     */
    private boolean doneInteraction;

    /**
     * Creates a new SallyNPC in the specified level at the specified tile.
     *
     * @param level       the level this SallyNPC belongs to
     * @param currentTile the tile this SallyNPC begins on
     */
    public SallyNPC(Level level, Vector2 currentTile) {
        super(level, currentTile, Assets.sallyWalkingTextures);
        doneInteraction = false;
    }

    /**
     * @return the dialogue messages to use depending on the game segment
     */
    public String[] getMessage() {
        String[] messages = new String[3];

        switch (Game.segment) {
            case 0:
                messages[0] = "Please help, theres too many monsters around!";
                messages[1] = "Help me by winning a battle against them.";
                messages[2] = "Here, have some points and take a look at my shop.";
                return messages;
            case 1:
                messages[0] = "Could you do me a favour and get me some slime from an Ooze?";
                messages[1] = "Only when you've done this will you be able to progress.";
                messages[2] = "I have a few more things to sell to you.";
                return messages;
            case 2:
                messages[0] = "The monsters are starting to over-run us!";
                messages[1] = "Could you help out again and win 3 battles against them?";
                messages[2] = "Here's what I have to help you out.";
                return messages;
            case 3:
                messages[0] = "This time I'll need some duck bones - don't ask why.";
                messages[1] = "You should be able to find them from any ducks around.";
                messages[2] = "Take a look at some of the new wares I have to offer.";
                return messages;
            case 4:
                messages[0] = "No matter how much we try to hold them back they keep striking.";
                messages[1] = "If you could win 5 battles against them it would be appreciated!";
                messages[2] = "You should equip yourself before you continue.";
                return messages;
            case 5:
                messages[0] = "I sense we're getting closer to the source of the power.";
                messages[1] = "If you could get some Super Duck Bones, I'm sure I could locate it.";
                messages[2] = "Please, take a look at my wares.";
                return messages;
            case 6:
                messages[0] = "Almost there now!";
                messages[1] = "Win 7 more battles and we'll be there!";
                messages[2] = "I have some more things to sell to you.";
                return messages;
            case 7:
                messages[0] = "We've found him! We've found the Roboduck!";
                messages[1] = "Please help us by defeating him!";
                messages[2] = "Take one last look at my shop while you can!";
                return messages;
        }
        return null;
    }

    /**
     * Called when a player first interacts with this SallyNPC.
     *
     * @param delta     the time elapsed since the last update
     * @param uiManager the UI manager to show messages on
     */
    @Override
    public void initializeInteraction(float delta, UIManager uiManager) {
        if (!doneInteraction) {
            uiManager.createDialogue(getMessage());
            this.uiManager = uiManager;
        }
    }

    /**
     * Called every frame while a player interacts with this SallyNPC.
     *
     * @param delta the time elapsed since the last update
     * @return true if update should continue
     */
    @Override
    public boolean updateInteracting(float delta) {
        return !doneInteraction && uiManager.updateDialogue(delta);
    }

    /**
     * Called when a player has finished interacting with this SallyNPC.
     */
    @Override
    public void action(GameWorld gameWorld) {
        if (!doneInteraction) {
            Game.worldScreen.getGameWorld().setShop(new UIShop(100, 500, 910, 440, Game.party, Game.shops.getShop(Game.segment)));
            uiManager.addNotification("You gained 50 points.");
            Game.pointsScore += 50;
            doneInteraction = true;
        }
    }

    /**
     * Not implemented.
     *
     * @param delta the time elapsed since the last update
     */
    @Override
    protected void updateTransitioning(float delta) {
    }

    /**
     * Not implemented.
     *
     * @param delta the time elapsed since the last update
     */
    @Override
    protected void updateStationary(float delta) {
    }
}
