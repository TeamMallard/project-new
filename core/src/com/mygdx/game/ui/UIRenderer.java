package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.assets.Assets;

/**
 * The UI renderer calls the render methods of each ui component in the game.
 */
public class UIRenderer {
    private UIManager uiManager;
    OrthographicCamera UICamera;
    SpriteBatch uiBatch;
    NinePatchDrawable patchDraw;

    public UIRenderer(UIManager uiManager) {
        this.uiManager = uiManager;
        patchDraw = new NinePatchDrawable();
        patchDraw.setPatch(Assets.patch);
        uiBatch = new SpriteBatch();
        UICamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiBatch.setProjectionMatrix(UICamera.combined);
    }

    /**
     * To be called once per frame to render every UI component in the UI manager.
     */
    public void render() {
        uiBatch.begin();
        for (int x = 0; x < uiManager.getUIComponents().size();x++) {
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
     * To be called once per frame to render every UI component in the UI manager.
     * This allows the spritebatch to be passed to the components.
     */
    public void render(SpriteBatch batch) {
        for (int x = 0; x < uiManager.getUIComponents().size();x++) {
            uiManager.getUIComponent(x).render(batch, Assets.patch);
        }
        if (uiManager.dialogue != null) {
            uiManager.dialogue.render(batch, Assets.patch);
        }
    }

    public void resize(int width, int height) {
        UICamera.setToOrtho(false);
        uiBatch.setProjectionMatrix(UICamera.combined);
    }

    public void dispose() {
        uiBatch.dispose();
    }

}
