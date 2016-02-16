package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

/**
 * Responsible for rendering each UI component.
 */
public class UIRenderer {

    /**
     * The UI manager containing the components to render
     */
    private UIManager uiManager;

    /**
     * The camera looking at the UI.
     */
    private OrthographicCamera uiCamera;

    /**
     * The batch on which to render the UI.
     */
    private SpriteBatch uiBatch;

    /**
     * Creates a new UIRenderer for the specified UI manager.
     *
     * @param uiManager the UI manager containing the components to render
     */
    public UIRenderer(UIManager uiManager) {
        this.uiManager = uiManager;

        uiBatch = new SpriteBatch();
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiBatch.setProjectionMatrix(uiCamera.combined);
    }

    /**
     * Renders the UI while the player is in the overworld.
     */
    public void renderWorld() {
        uiBatch.begin();
        for (int x = 0; x < uiManager.getUIComponents().size(); x++) {
            uiManager.getUIComponent(x).render(uiBatch, Assets.patch);
        }
        if (uiManager.dialogue != null) {
            uiManager.dialogue.render(uiBatch, Assets.patch);
        }
        if (!uiManager.notifications.isEmpty()) {
            uiManager.notifications.get(0).render(uiBatch, Assets.patch);
        }
        uiManager.partyMenu.render(uiBatch, Assets.patch);
        uiBatch.end();
    }

    /**
     * Renders the UI while the player is in battle.
     *
     * @param batch the sprite batch to render on
     */
    public void renderBattle(SpriteBatch batch) {
        for (int x = 0; x < uiManager.getUIComponents().size(); x++) {
            uiManager.getUIComponent(x).render(batch, Assets.patch);
        }
        if (uiManager.dialogue != null) {
            uiManager.dialogue.render(batch, Assets.patch);
        }
    }

    /**
     * Re-sizes the camera looking at the UI.
     */
    public void resize() {
        uiCamera.setToOrtho(false);
        uiBatch.setProjectionMatrix(uiCamera.combined);
    }

    /**
     * Cleans up any resources being used by this UIRenderer.
     */
    public void dispose() {
        uiBatch.dispose();
    }
}
