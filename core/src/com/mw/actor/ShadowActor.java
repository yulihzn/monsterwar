package com.mw.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;

/**
 * Created by BanditCat on 2016/10/31.
 */

public class ShadowActor extends Actor{
    private OrthographicCamera camera;
    private Pixmap pixmap;
    private Texture texture;
    private int width = 256;
    private int height = 256;
    private Color sightColor = new Color(0,0,0,1f);
    private int shadowRange = 0;

    public ShadowActor(OrthographicCamera camera) {
        this.camera = camera;
        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        texture = new Texture(width, height, Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
//        pixmap.setColor(new Color(0,0,0,0.8f));
//        pixmap.fill();
//        pixmap.setColor(new Color(0,0,0,1f));
//        pixmap.fillRectangle(0,0,1,1);
        texture.draw(pixmap, 0, 0);
        ColorAction colorAction = new ColorAction();
        colorAction.setColor(sightColor);
        colorAction.setEndColor(new Color(0,0,0,0));
        colorAction.setDuration(2);
        addAction(colorAction);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //混合模式
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(texture,getX(),getY());
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    public void drawShadow(int x,int y){
        Pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(new Color(0,0,0,0.95f));
        pixmap.fill();
        pixmap.setColor(sightColor);
        pixmap.fillCircle(x,height-y,shadowRange);
        texture.draw(pixmap,0,0);
    }
}
