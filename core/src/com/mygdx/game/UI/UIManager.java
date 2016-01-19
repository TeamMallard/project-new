package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;
import com.mygdx.game.InputHandler;
import com.mygdx.game.PartyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the creation of UI elements on the screen.
 * The ui renderer uses this class to call the render function of each component.
 */
public class UIManager {
    public UIPartyMenu partyMenu;
    public List<UIMessageBox> notifications;

    private List<UIComponent> uiComponents;

    public UIDialogue dialogue;
    private float notificationTimer;

    public UIManager(PartyManager party) {
        notifications = new ArrayList<UIMessageBox>();
        notificationTimer = 0;
        uiComponents = new ArrayList<UIComponent>();
        partyMenu = new UIPartyMenu(40, 150, Gdx.graphics.getWidth()-80, Gdx.graphics.getHeight()-320, party);
    }

    /**
     * Opens the party menu.
     */
    public void openPartyMenu() {
        partyMenu.show();
    }

    /**
     * Called once per frame to update the party menu.
     * @return false if the party menu is closed.
     */
    public boolean updatePartyMenu(float delta) {
        return partyMenu.update(delta);
    }

    /**
     * Creates a new dialogue from an array of messages.
     */
    public void createDialogue(String[] messages) {
        dialogue = new UIDialogue(20, 20, Gdx.graphics.getWidth()/2-40, 0, messages);
    }

    /**
     * To be called if the player is in a dialogue.
     * @return false when dialogue has finished.
     */
    public boolean updateDialogue(float delta) {
        if (!dialogue.update(delta)) {
            dialogue = null;
            return false;
        }
        return true;
    }

    /**
     * Adds a notification to the current list of notifications waiting to be displayed.
     */
    public void addNotification(String message) {
        notifications.add(new UIMessageBox(message, Assets.consolas22, Color.WHITE, Align.center, 20, Gdx.graphics.getHeight()-80, Gdx.graphics.getWidth()/2, 0));
    }

    /**
     * To be called once per frame to update timing of notification components.
     */
    public void updateNotification(float delta) {
        if (notifications.isEmpty()) {
            notificationTimer = 0;
        } else {
            notificationTimer += delta;
            if (notificationTimer > 4f) {
                notificationTimer = 0;
                notifications.remove(0);
            }
        }

    }

    public void addUIComponent(UIComponent c) {
        uiComponents.add(c);
    }

    public void removeUIComponent(UIComponent c) {
        uiComponents.remove(c);
    }

    public UIComponent getUIComponent(int i) {
        return uiComponents.get(i);
    }

    public List<UIComponent> getUIComponents() {
        return uiComponents;
    }
}
