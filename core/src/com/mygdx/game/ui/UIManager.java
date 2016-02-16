package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.PartyManager;
import com.mygdx.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages UI components.
 */
public class UIManager {

    /**
     * The party menu instance.
     */
    public UIPartyMenu partyMenu;

    /**
     * The shop instance.
     */
    public UIShop shop;

    /**
     * The currently displayed notifications.
     */
    public List<UIMessageBox> notifications;

    /**
     * The list of currently displayed UI components.
     */
    private List<UIComponent> uiComponents;

    /**
     * The current dialogue box.
     */
    public UIDialogue dialogue;

    /**
     * How long the current notification has been displayed for.
     */
    private float notificationTimer;

    /**
     * Creates this UIManager.
     *
     * @param party the party
     */
    public UIManager(PartyManager party) {
        notifications = new ArrayList<UIMessageBox>();
        notificationTimer = 0;
        uiComponents = new ArrayList<UIComponent>();
        partyMenu = new UIPartyMenu(40, 150, Gdx.graphics.getWidth() - 80, Gdx.graphics.getHeight() - 320, party);
    }

    /**
     * Opens the party menu.
     */
    public void openPartyMenu() {
        partyMenu.show();
    }

    /**
     * Updates the party menu.
     *
     * @return false if the party menu is closed
     */
    public boolean updatePartyMenu() {
        return partyMenu.update();
    }


    /**
     * Updates the shop.
     *
     * @return false if the shop is closed
     */
    public boolean updateShop() {
        return shop.update();
    }

    /**
     * Creates a new dialogue from an array of messages.
     *
     * @param messages the messages
     */
    public void createDialogue(String[] messages) {
        dialogue = new UIDialogue(20, 20, Gdx.graphics.getWidth() / 2 - 40, 0, messages);
    }

    /**
     * Updates the current dialogue.
     *
     * @param delta the time elapsed since the last update
     * @return false when dialogue has finished
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
     *
     * @param message the notification message
     */
    public void addNotification(String message) {
        notifications.add(new UIMessageBox(message, Assets.consolas22, Color.WHITE, Align.center, 20, Gdx.graphics.getHeight() - 80, Gdx.graphics.getWidth() / 2, 0));
    }

    /**
     * Updates notifications.
     *
     * @param delta the time elapsed since the last update
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

    /**
     * Adds a component to this UIManager.
     *
     * @param c the component to add
     */
    public void addUIComponent(UIComponent c) {
        uiComponents.add(c);
    }

    /**
     * Removes the specified component from this UIManager.
     *
     * @param c the component to remove
     */
    public void removeUIComponent(UIComponent c) {
        uiComponents.remove(c);
    }

    /**
     * Gets the UI component at the specified index.
     *
     * @param i the index of the component
     * @return the component
     */
    public UIComponent getUIComponent(int i) {
        return uiComponents.get(i);
    }

    /**
     * @return the list of UI components in this UIManager
     */
    public List<UIComponent> getUIComponents() {
        return uiComponents;
    }

    /**
     * Sets the shop to display.
     *
     * @param shop the shop
     */
    public void setShop(UIShop shop) {
        this.shop = shop;
    }
}
