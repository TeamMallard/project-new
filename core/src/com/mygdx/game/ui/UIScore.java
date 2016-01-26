package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game;

/**
 * Component for rendering the score display at the top right of the screen.
 */
public class UIScore extends UIComponent {

    UIMessageBox messageBox;
    String messagePrepend="Points: ";

    public UIScore(){
        super(0,0,0,0);
        this.width=200;
        this.height=20;
        this.x= Gdx.graphics.getWidth()-width;
        this.y= Gdx.graphics.getHeight()-height*2;
        this.messageBox = new UIMessageBox(messagePrepend+Integer.toString(Game.pointsScore),this.x,this.y,this.width,this.height,10,10);
    }

    /**
     * Called once per frame to render the score.
     */
    public void render(SpriteBatch batch, NinePatch patch){
        messageBox.setMessage(messagePrepend+Integer.toString(Game.pointsScore));
        messageBox.render(batch,patch);
    }
}
